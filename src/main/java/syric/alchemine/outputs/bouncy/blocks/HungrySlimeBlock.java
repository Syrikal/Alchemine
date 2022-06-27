package syric.alchemine.outputs.bouncy.blocks;

import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import syric.alchemine.setup.AlchemineBlocks;

import java.util.List;

public class HungrySlimeBlock extends SlimeBlock {
    public static final IntegerProperty FILL = IntegerProperty.create("fill", 0, 8);
//    public static final BooleanProperty SEED = BooleanProperty.create("seed");
    public static final Object2FloatMap<ItemLike> COMPOSTABLES = new Object2FloatOpenHashMap<>();

    public HungrySlimeBlock(BlockBehaviour.Properties properties) {
        super(properties);
//        this.registerDefaultState(this.defaultBlockState().setValue(FILL, 0).setValue(SEED, true));
        this.registerDefaultState(this.defaultBlockState().setValue(FILL, 0));
        bootStrap();
    }

    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
//        builder.add(FILL, SEED);
        builder.add(FILL);
    }

    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
//        chatPrint("Entity stepped on hungry slime, attempting to eat", level);
        if (entity instanceof ItemEntity ent && !level.isClientSide()) {
            if (COMPOSTABLES.containsKey(ent.getItem().getItem())) {
                eatEdibleStack(level, state, pos, ent.getItem());
            }
        }
    }

    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource source) {
//        chatPrint("Hungry slime got a random tick.", level);
        if (!level.isClientSide()) {
            if (state.getValue(FILL) == 8) {
                triggerSpread(level, state, pos);
            }
            consumeItems(level, state, pos);
        }
    }

    public void triggerSpread(Level level, BlockState state, BlockPos pos) {
//        chatPrint("Hungry slime attempting to spread", level);
        BlockState emptyState = state.setValue(FILL, 0);
        level.destroyBlock(pos, false);
        //an audiovisual effect?
        level.setBlockAndUpdate(pos, emptyState);
        double random = Math.random();
        if (random < 0.25) {
            spreadSlime(level, pos);
        } else if (random < 0.3) {
            spawnSlime(level, pos, 0);
        }
    }

    private void spreadSlime(Level level, BlockPos pos) {
//        chatPrint("Attempting to spread slime", level);
        boolean succeeded = false;
        int tries = 0;

        while (!succeeded) {
            if (tries > 6) {
                break;
            }
            Direction dir = Direction.getRandom(RandomSource.create());
            BlockPos target = pos.relative(dir);
            if (level.getBlockState(target).getMaterial() == Material.AIR) {
                succeeded = true;
                BlockState placeState = AlchemineBlocks.HUNGRY_SLIME.get().defaultBlockState();
                level.setBlockAndUpdate(target, placeState);
                level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(placeState));
            } else if (level.getBlockState(target).getBlock() == this) {
//                chatPrint("Chained spreadSlime", level);
                succeeded = true;
                spreadSlime(level, target);
            } else {
//                chatPrint("spreadSlime failed, trying again", level);
                tries++;
            }
        }
        if (!succeeded) {
//            chatPrint("Giving up", level);
        }
    }

    private void spawnSlime(Level level, BlockPos pos, int tries) {
//        chatPrint("Attempting to spawn slime", level);
        if (tries > 20) {
//            chatPrint("Giving up", level);
            return;
        }

        Slime entity = EntityType.SLIME.create(level); //Slimes have a weirdly high amount of health
        if (entity != null) {
            entity.setSize(2, true);

            double d0 = pos.getX() - 2 + Math.random()*4;
            double d1 = pos.getY() - 1 + Math.random()*2;
            double d2 = pos.getZ() - 2 + Math.random()*4;

            entity.absMoveTo(d0, d1, d2);
            if (!level.noCollision(entity)) {
                spawnSlime(level, pos, tries + 1);
//            chatPrint("Failed to spawn a slime", level);
                return;
            }
            entity.setDeltaMovement(Vec3.ZERO);
            level.addFreshEntity(entity);
        }
    }

    private void consumeItems(Level level, BlockState state, BlockPos pos) {
//        chatPrint("Attempting to consume items", level);
        List<ItemEntity> list = level.getEntities(EntityType.ITEM, new AABB(pos).inflate(1.5), Entity::isOnGround);
        for (ItemEntity ent : list) {
            if (COMPOSTABLES.containsKey(ent.getItem().getItem())) {
                int newValue = eatEdibleStack(level, state, pos, ent.getItem());
                if (newValue == 8) {
//                    chatPrint("Full, aborting consumeItems", level);
                    break;
                }
            }
        }
//        chatPrint("ConsumeItems stopped. Full, or no more items to consume", level);
    }

    private int addLevel(Level level, BlockState state, BlockPos pos, float value) {
        int prevLevel = state.getValue(FILL);
        if (prevLevel == 8) {
//            chatPrint("Not increasing level, slime full", level);
            return 8;
        }
        if (Math.random() < value) {
//            chatPrint("Level increased to " + (prevLevel+1), level);
            level.setBlockAndUpdate(pos, state.setValue(FILL, prevLevel + 1));
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state.setValue(FILL, prevLevel + 1)));
        } else {
//            chatPrint("Level randomly not increased", level);
        }
        return prevLevel + 1;
    }

    private int eatEdibleStack(Level level, BlockState state, BlockPos pos, ItemStack stack) {
//        chatPrint("Attempting to eat a stack of " + stack.getItem().toString(), level);
        boolean full = state.getValue(FILL) == 8;
        int currentLevel = state.getValue(FILL);
        while (!stack.isEmpty() && !full) {
            float value = COMPOSTABLES.getFloat(stack.getItem());
            stack.shrink(1);
//            chatPrint("Eating one of the items", level);
            int newLevel = addLevel(level, state.setValue(FILL, currentLevel), pos, value);
            currentLevel = newLevel;
            if (newLevel == 8) {
//                chatPrint("Full, aborting eatEdibleStack", level);
                full = true;
            }
        }
        return currentLevel;
    }

    public static void bootStrap() {
        COMPOSTABLES.defaultReturnValue(-1.0F);
        float f = 0.3F;
        float f1 = 0.5F;
        float f2 = 0.65F;
        float f3 = 0.85F;
        float f4 = 1.0F;
        add(0.3F, Items.JUNGLE_LEAVES);
        add(0.3F, Items.OAK_LEAVES);
        add(0.3F, Items.SPRUCE_LEAVES);
        add(0.3F, Items.DARK_OAK_LEAVES);
        add(0.3F, Items.ACACIA_LEAVES);
        add(0.3F, Items.BIRCH_LEAVES);
        add(0.3F, Items.AZALEA_LEAVES);
        add(0.3F, Items.MANGROVE_LEAVES);
        add(0.3F, Items.OAK_SAPLING);
        add(0.3F, Items.SPRUCE_SAPLING);
        add(0.3F, Items.BIRCH_SAPLING);
        add(0.3F, Items.JUNGLE_SAPLING);
        add(0.3F, Items.ACACIA_SAPLING);
        add(0.3F, Items.DARK_OAK_SAPLING);
        add(0.3F, Items.MANGROVE_PROPAGULE);
        add(0.3F, Items.BEETROOT_SEEDS);
        add(0.3F, Items.DRIED_KELP);
        add(0.3F, Items.GRASS);
        add(0.3F, Items.KELP);
        add(0.3F, Items.MELON_SEEDS);
        add(0.3F, Items.PUMPKIN_SEEDS);
        add(0.3F, Items.SEAGRASS);
        add(0.3F, Items.SWEET_BERRIES);
        add(0.3F, Items.GLOW_BERRIES);
        add(0.3F, Items.WHEAT_SEEDS);
        add(0.3F, Items.MOSS_CARPET);
        add(0.3F, Items.SMALL_DRIPLEAF);
        add(0.3F, Items.HANGING_ROOTS);
        add(0.3F, Items.MANGROVE_ROOTS);
        add(0.5F, Items.DRIED_KELP_BLOCK);
        add(0.5F, Items.TALL_GRASS);
        add(0.5F, Items.FLOWERING_AZALEA_LEAVES);
        add(0.5F, Items.CACTUS);
        add(0.5F, Items.SUGAR_CANE);
        add(0.5F, Items.VINE);
        add(0.5F, Items.NETHER_SPROUTS);
        add(0.5F, Items.WEEPING_VINES);
        add(0.5F, Items.TWISTING_VINES);
        add(0.5F, Items.MELON_SLICE);
        add(0.5F, Items.GLOW_LICHEN);
        add(0.65F, Items.SEA_PICKLE);
        add(0.65F, Items.LILY_PAD);
        add(0.65F, Items.PUMPKIN);
        add(0.65F, Items.CARVED_PUMPKIN);
        add(0.65F, Items.MELON);
        add(0.65F, Items.APPLE);
        add(0.65F, Items.BEETROOT);
        add(0.65F, Items.CARROT);
        add(0.65F, Items.COCOA_BEANS);
        add(0.65F, Items.POTATO);
        add(0.65F, Items.WHEAT);
        add(0.65F, Items.BROWN_MUSHROOM);
        add(0.65F, Items.RED_MUSHROOM);
        add(0.65F, Items.MUSHROOM_STEM);
        add(0.65F, Items.CRIMSON_FUNGUS);
        add(0.65F, Items.WARPED_FUNGUS);
        add(0.65F, Items.NETHER_WART);
        add(0.65F, Items.CRIMSON_ROOTS);
        add(0.65F, Items.WARPED_ROOTS);
        add(0.65F, Items.SHROOMLIGHT);
        add(0.65F, Items.DANDELION);
        add(0.65F, Items.POPPY);
        add(0.65F, Items.BLUE_ORCHID);
        add(0.65F, Items.ALLIUM);
        add(0.65F, Items.AZURE_BLUET);
        add(0.65F, Items.RED_TULIP);
        add(0.65F, Items.ORANGE_TULIP);
        add(0.65F, Items.WHITE_TULIP);
        add(0.65F, Items.PINK_TULIP);
        add(0.65F, Items.OXEYE_DAISY);
        add(0.65F, Items.CORNFLOWER);
        add(0.65F, Items.LILY_OF_THE_VALLEY);
        add(0.65F, Items.WITHER_ROSE);
        add(0.65F, Items.FERN);
        add(0.65F, Items.SUNFLOWER);
        add(0.65F, Items.LILAC);
        add(0.65F, Items.ROSE_BUSH);
        add(0.65F, Items.PEONY);
        add(0.65F, Items.LARGE_FERN);
        add(0.65F, Items.SPORE_BLOSSOM);
        add(0.65F, Items.AZALEA);
        add(0.65F, Items.MOSS_BLOCK);
        add(0.65F, Items.BIG_DRIPLEAF);
        add(0.85F, Items.HAY_BLOCK);
        add(0.85F, Items.BROWN_MUSHROOM_BLOCK);
        add(0.85F, Items.RED_MUSHROOM_BLOCK);
        add(0.85F, Items.NETHER_WART_BLOCK);
        add(0.85F, Items.WARPED_WART_BLOCK);
        add(0.85F, Items.FLOWERING_AZALEA);
        add(0.85F, Items.BREAD);
        add(0.85F, Items.BAKED_POTATO);
        add(0.85F, Items.COOKIE);
        add(1.0F, Items.CAKE);
        add(1.0F, Items.PUMPKIN_PIE);
    }

    private static void add(float nutrition, ItemLike item) {
        COMPOSTABLES.put(item.asItem(), nutrition);
    }



}
