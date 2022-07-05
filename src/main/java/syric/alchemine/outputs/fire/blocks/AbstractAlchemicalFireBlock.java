package syric.alchemine.outputs.fire.blocks;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import syric.alchemine.setup.AlchemineBlocks;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static syric.alchemine.util.ChatPrint.chatPrint;

public class AbstractAlchemicalFireBlock extends BaseFireBlock {
    public static final int MAX_AGE = 15;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_15;
    public static final BooleanProperty NORTH = PipeBlock.NORTH;
    public static final BooleanProperty EAST = PipeBlock.EAST;
    public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
    public static final BooleanProperty WEST = PipeBlock.WEST;
    public static final BooleanProperty UP = PipeBlock.UP;
    private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().filter((c) -> {
        return c.getKey() != Direction.DOWN;
    }).collect(Util.toMap());
    private static final VoxelShape UP_AABB = Block.box(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape WEST_AABB = Block.box(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D);
    private static final VoxelShape EAST_AABB = Block.box(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape NORTH_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D);
    private static final VoxelShape SOUTH_AABB = Block.box(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D);
    private final Map<BlockState, VoxelShape> shapesCache;
    private static final int IGNITE_INSTANT = 60;
    private static final int IGNITE_EASY = 30;
    private static final int IGNITE_MEDIUM = 15;
    private static final int IGNITE_HARD = 5;
    private static final int BURN_INSTANT = 100;
    private static final int BURN_EASY = 60;
    private static final int BURN_MEDIUM = 20;
    private static final int BURN_HARD = 5;
    private final Object2IntMap<Block> igniteOdds = new Object2IntOpenHashMap<>();
    private final Object2IntMap<Block> burnOdds = new Object2IntOpenHashMap<>();

    public AbstractAlchemicalFireBlock(BlockBehaviour.Properties properties) {
        super(properties, 1.0F);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, Integer.valueOf(0)).setValue(NORTH, Boolean.valueOf(false)).setValue(EAST, Boolean.valueOf(false)).setValue(SOUTH, Boolean.valueOf(false)).setValue(WEST, Boolean.valueOf(false)).setValue(UP, Boolean.valueOf(false)));
        this.shapesCache = ImmutableMap.copyOf(this.stateDefinition.getPossibleStates().stream().filter((p_53497_) -> {
            return p_53497_.getValue(AGE) == 0;
        }).collect(Collectors.toMap(Function.identity(), AbstractAlchemicalFireBlock::calculateShape)));
    }

    private static VoxelShape calculateShape(BlockState state) {
        VoxelShape voxelshape = Shapes.empty();
        if (state.getValue(UP)) {
            voxelshape = UP_AABB;
        }

        if (state.getValue(NORTH)) {
            voxelshape = Shapes.or(voxelshape, NORTH_AABB);
        }

        if (state.getValue(SOUTH)) {
            voxelshape = Shapes.or(voxelshape, SOUTH_AABB);
        }

        if (state.getValue(EAST)) {
            voxelshape = Shapes.or(voxelshape, EAST_AABB);
        }

        if (state.getValue(WEST)) {
            voxelshape = Shapes.or(voxelshape, WEST_AABB);
        }

        return voxelshape.isEmpty() ? DOWN_AABB : voxelshape;
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor level, BlockPos pos1, BlockPos pos2) {
        return this.canSurvive(state, level, pos1) ? this.getStateWithAge(level, pos1, state.getValue(AGE)) : Blocks.AIR.defaultBlockState();
    }

    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return this.shapesCache.get(state.setValue(AGE, Integer.valueOf(0)));
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.getStateForPlacement(context.getLevel(), context.getClickedPos());
    }

    protected BlockState getStateForPlacement(BlockGetter getter, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState blockstate = getter.getBlockState(blockpos);
        if (!this.canCatchFire(getter, pos, Direction.UP) && !blockstate.isFaceSturdy(getter, blockpos, Direction.UP)) {
            BlockState blockstate1 = this.defaultBlockState();

            for(Direction direction : Direction.values()) {
                BooleanProperty booleanproperty = PROPERTY_BY_DIRECTION.get(direction);
                if (booleanproperty != null) {
                    blockstate1 = blockstate1.setValue(booleanproperty, Boolean.valueOf(this.canCatchFire(getter, pos.relative(direction), direction.getOpposite())));
                }
            }

            return blockstate1;
        } else {
            return this.defaultBlockState();
        }
    }

    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockpos = pos.below();
        return level.getBlockState(blockpos).isFaceSturdy(level, blockpos, Direction.UP) || this.isValidFireLocation(level, pos);
    }

    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randSource) {
        level.scheduleTick(pos, this, getFireTickDelay(level.random));
        if (level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
            if (!state.canSurvive(level, pos)) {
                level.removeBlock(pos, false);
            }

            BlockState blockstate = level.getBlockState(pos.below());
            boolean source = blockstate.isFireSource(level, pos, Direction.UP);
            int age = state.getValue(AGE);
            if (!source && level.isRaining() && this.isNearRain(level, pos) && randSource.nextFloat() < 0.2F + (float)age * 0.03F) {
                level.removeBlock(pos, false);
            } else {
                int j = Math.min(15, age + randSource.nextInt(3) / 2);
                if (age != j) {
                    state = state.setValue(AGE, Integer.valueOf(j));
                    level.setBlock(pos, state, 4);
                }

                if (!source) {
                    if (!this.isValidFireLocation(level, pos)) {
                        BlockPos blockpos = pos.below();
                        if (!level.getBlockState(blockpos).isFaceSturdy(level, blockpos, Direction.UP) || age > 3) {
                            level.removeBlock(pos, false);
                        }

                        return;
                    }

                    if (age == 15 && randSource.nextInt(4) == 0 && !this.canCatchFire(level, pos.below(), Direction.UP)) {
                        level.removeBlock(pos, false);
                        return;
                    }
                }

                int humidityModifier = level.isHumidAt(pos) ? -50 : 0;
                this.tryCatchFire(level, pos.east(), 300 + humidityModifier, randSource, age, Direction.WEST);
                this.tryCatchFire(level, pos.west(), 300 + humidityModifier, randSource, age, Direction.EAST);
                this.tryCatchFire(level, pos.below(), 250 + humidityModifier, randSource, age, Direction.UP);
                this.tryCatchFire(level, pos.above(), 250 + humidityModifier, randSource, age, Direction.DOWN);
                this.tryCatchFire(level, pos.north(), 300 + humidityModifier, randSource, age, Direction.SOUTH);
                this.tryCatchFire(level, pos.south(), 300 + humidityModifier, randSource, age, Direction.NORTH);

                BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
                for(int xOffset = -1; xOffset <= 1; ++xOffset) {
                    for(int zOffset = -1; zOffset <= 1; ++zOffset) {
                        for(int yOffset = -1; yOffset <= 4; ++yOffset) {
                            if (xOffset != 0 || yOffset != 0 || zOffset != 0) {

                                //We create igniteOdds and rollCap. Then we take a random number from 0 or 1
                                // to rollCap and ignite if it's less than igniteOdds.
                                int rollCap = 100;
                                if (yOffset > 1) {
                                    rollCap += (yOffset - 1) * 100;
                                }

//                                StringBuilder sb = new StringBuilder().append("Indirect spread. ");

                                blockpos$mutableblockpos.setWithOffset(pos, xOffset, yOffset, zOffset);
//                                sb.append("Block: ").append(level.getBlockState(blockpos$mutableblockpos).getBlock());

                                int igniteOdds = this.getIgniteOdds(level, blockpos$mutableblockpos);
//                                sb.append(", Base ignite #: ").append(igniteOdds);
                                if (igniteOdds > 0) {

                                    igniteOdds = modifiedIgniteChance(igniteOdds, age, level, pos);

//                                    sb.append(", Modified ignite #: ").append(igniteOdds);

                                    if (igniteOdds > 0 && randSource.nextInt(rollCap) <= igniteOdds && (!level.isRaining() || !this.isNearRain(level, blockpos$mutableblockpos))) {
//                                        sb.append(". Succeeded.");
                                        int spreadAge = Math.min(15, age + randSource.nextInt(5) / 4);
                                        level.setBlock(blockpos$mutableblockpos, this.getStateWithAge(level, blockpos$mutableblockpos, spreadAge), 3);
                                    } else {
//                                        sb.append(". Failed.");
                                    }
//                                    chatPrint(sb.toString(), level);
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    public int modifiedIgniteChance(int baseIgniteChance, int age, Level level, BlockPos pos) {
        baseIgniteChance += 40;
        baseIgniteChance += level.getDifficulty().getId() * 7;
        baseIgniteChance /= (age + 30);
        if (level.isHumidAt(pos)) {baseIgniteChance /= 2;}
        return baseIgniteChance;
    }


    protected boolean isNearRain(Level level, BlockPos pos) {
        return level.isRainingAt(pos) || level.isRainingAt(pos.west()) || level.isRainingAt(pos.east()) || level.isRainingAt(pos.north()) || level.isRainingAt(pos.south());
    }

    @Deprecated //Forge: Use IForgeBlockState.getFlammability, Public for default implementation only.
    //Returns 0 if it's waterlogged, stored burn odds otherwise.
    public int getBurnOdds(BlockState state) {
        return state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED) ? 0 : this.burnOdds.getInt(state.getBlock());
    }

    @Deprecated //Forge: Use IForgeBlockState.getFireSpreadSpeed
    //Returns 0 if it's waterlogged, stored ignite odds otherwise.
    public int getIgniteOdds(BlockState state) {
        return state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED) ? 0 : this.igniteOdds.getInt(state.getBlock());
    }

    private void tryCatchFire(Level level, BlockPos pos, int baseRollCap, RandomSource source, int fireAge, Direction face) {
//        chatPrint("Attempting to spread fire adjacent", level);
        int flammability = getFlammabilityChangeable(level, pos, face);
        if (source.nextInt(baseRollCap) < flammability) {
            BlockState blockstate = level.getBlockState(pos);
            if (source.nextInt(baseRollCap + 10) < 5 && !level.isRainingAt(pos)) {
//                chatPrint("Successfully spread fire adjacent, placing", level);
                int ageForPlacement = Math.min(baseRollCap + source.nextInt(5) / 4, 15);
                level.setBlock(pos, this.getStateWithAge(level, pos, ageForPlacement), 3);
            } else {
                level.removeBlock(pos, false);
            }
            blockstate.onCaughtFire(level, pos, face, null);
        }

    }

    public int getFlammabilityChangeable(Level level, BlockPos pos, Direction face) {
        return level.getBlockState(pos).getFlammability(level, pos, face);
    }

    private BlockState getStateWithAge(LevelAccessor level, BlockPos pos, int age) {
        BlockState blockstate = getStateForPlacement(level, pos);
        return blockstate.is(this.getSelf()) ? blockstate.setValue(AGE, Integer.valueOf(age)) : blockstate;
    }

    private boolean isValidFireLocation(BlockGetter getter, BlockPos pos) {
        for(Direction direction : Direction.values()) {
            if (this.canCatchFire(getter, pos.relative(direction), direction.getOpposite())) {
                return true;
            }
        }

        return false;
    }

    private int getIgniteOdds(LevelReader levelReader, BlockPos pos) {
        if (!levelReader.isEmptyBlock(pos)) {
            return 0;
        } else {
            int igniteChance = 0;

            for(Direction direction : Direction.values()) {
                BlockState blockstate = levelReader.getBlockState(pos.relative(direction));
                igniteChance = Math.max(getIgniteOdds(blockstate), igniteChance);
            }

            return igniteChance;
        }
    }

    @Deprecated //Forge: Use canCatchFire with more context
    protected boolean canBurn(BlockState state) {
        return this.getIgniteOdds(state) > 0;
    }

    public void onPlace(BlockState state1, Level level, BlockPos pos, BlockState state2, boolean bool) {
        super.onPlace(state1, level, pos, state2, bool);
        level.scheduleTick(pos, this, getFireTickDelay(level.random));
    }

    public int getFireTickDelay(RandomSource randomSource) {
        return 30 + randomSource.nextInt(10);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, NORTH, EAST, SOUTH, WEST, UP);
    }

    public void setFlammable(Block block, int ignitionChance, int burnChance) {
        if (block == Blocks.AIR) throw new IllegalArgumentException("Tried to set air on fire... This is bad.");
        this.igniteOdds.put(block, ignitionChance);
        this.burnOdds.put(block, burnChance);
    }

    /**
     * Side sensitive version that calls the block function.
     *
     * @param world The current world
     * @param pos Block position
     * @param face The side the fire is coming from
     * @return True if the face can catch fire.
     */
    public boolean canCatchFire(BlockGetter world, BlockPos pos, Direction face) {
        return world.getBlockState(pos).isFlammable(world, pos, face);
    }

    public static void defaultFireBootStrap(AbstractAlchemicalFireBlock alchemicalFireBlock) {
        alchemicalFireBlock.setFlammable(Blocks.OAK_PLANKS, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.SPRUCE_PLANKS, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.BIRCH_PLANKS, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.JUNGLE_PLANKS, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.ACACIA_PLANKS, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.DARK_OAK_PLANKS, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.MANGROVE_PLANKS, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.OAK_SLAB, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.SPRUCE_SLAB, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.BIRCH_SLAB, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.JUNGLE_SLAB, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.ACACIA_SLAB, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.DARK_OAK_SLAB, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.MANGROVE_SLAB, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.OAK_FENCE_GATE, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.SPRUCE_FENCE_GATE, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.BIRCH_FENCE_GATE, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.JUNGLE_FENCE_GATE, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.ACACIA_FENCE_GATE, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.DARK_OAK_FENCE_GATE, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.MANGROVE_FENCE_GATE, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.OAK_FENCE, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.SPRUCE_FENCE, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.BIRCH_FENCE, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.JUNGLE_FENCE, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.ACACIA_FENCE, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.DARK_OAK_FENCE, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.MANGROVE_FENCE, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.OAK_STAIRS, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.BIRCH_STAIRS, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.SPRUCE_STAIRS, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.JUNGLE_STAIRS, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.ACACIA_STAIRS, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.DARK_OAK_STAIRS, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.MANGROVE_STAIRS, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.OAK_LOG, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.SPRUCE_LOG, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.BIRCH_LOG, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.JUNGLE_LOG, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.ACACIA_LOG, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.DARK_OAK_LOG, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.MANGROVE_LOG, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.STRIPPED_OAK_LOG, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.STRIPPED_SPRUCE_LOG, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.STRIPPED_BIRCH_LOG, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.STRIPPED_JUNGLE_LOG, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.STRIPPED_ACACIA_LOG, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.STRIPPED_DARK_OAK_LOG, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.STRIPPED_MANGROVE_LOG, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.STRIPPED_OAK_WOOD, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.STRIPPED_SPRUCE_WOOD, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.STRIPPED_BIRCH_WOOD, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.STRIPPED_JUNGLE_WOOD, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.STRIPPED_ACACIA_WOOD, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.STRIPPED_DARK_OAK_WOOD, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.STRIPPED_MANGROVE_WOOD, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.OAK_WOOD, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.SPRUCE_WOOD, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.BIRCH_WOOD, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.JUNGLE_WOOD, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.ACACIA_WOOD, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.DARK_OAK_WOOD, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.MANGROVE_WOOD, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.MANGROVE_ROOTS, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.OAK_LEAVES, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.SPRUCE_LEAVES, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.BIRCH_LEAVES, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.JUNGLE_LEAVES, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.ACACIA_LEAVES, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.DARK_OAK_LEAVES, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.MANGROVE_LEAVES, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.BOOKSHELF, 30, 20);
        alchemicalFireBlock.setFlammable(Blocks.TNT, 15, 100);
        alchemicalFireBlock.setFlammable(Blocks.GRASS, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.FERN, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.DEAD_BUSH, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.SUNFLOWER, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.LILAC, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.ROSE_BUSH, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.PEONY, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.TALL_GRASS, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.LARGE_FERN, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.DANDELION, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.POPPY, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.BLUE_ORCHID, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.ALLIUM, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.AZURE_BLUET, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.RED_TULIP, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.ORANGE_TULIP, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.WHITE_TULIP, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.PINK_TULIP, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.OXEYE_DAISY, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.CORNFLOWER, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.LILY_OF_THE_VALLEY, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.WITHER_ROSE, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.WHITE_WOOL, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.ORANGE_WOOL, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.MAGENTA_WOOL, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.LIGHT_BLUE_WOOL, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.YELLOW_WOOL, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.LIME_WOOL, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.PINK_WOOL, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.GRAY_WOOL, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.LIGHT_GRAY_WOOL, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.CYAN_WOOL, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.PURPLE_WOOL, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.BLUE_WOOL, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.BROWN_WOOL, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.GREEN_WOOL, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.RED_WOOL, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.BLACK_WOOL, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.VINE, 15, 100);
        alchemicalFireBlock.setFlammable(Blocks.COAL_BLOCK, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.HAY_BLOCK, 60, 20);
        alchemicalFireBlock.setFlammable(Blocks.TARGET, 15, 20);
        alchemicalFireBlock.setFlammable(Blocks.WHITE_CARPET, 60, 20);
        alchemicalFireBlock.setFlammable(Blocks.ORANGE_CARPET, 60, 20);
        alchemicalFireBlock.setFlammable(Blocks.MAGENTA_CARPET, 60, 20);
        alchemicalFireBlock.setFlammable(Blocks.LIGHT_BLUE_CARPET, 60, 20);
        alchemicalFireBlock.setFlammable(Blocks.YELLOW_CARPET, 60, 20);
        alchemicalFireBlock.setFlammable(Blocks.LIME_CARPET, 60, 20);
        alchemicalFireBlock.setFlammable(Blocks.PINK_CARPET, 60, 20);
        alchemicalFireBlock.setFlammable(Blocks.GRAY_CARPET, 60, 20);
        alchemicalFireBlock.setFlammable(Blocks.LIGHT_GRAY_CARPET, 60, 20);
        alchemicalFireBlock.setFlammable(Blocks.CYAN_CARPET, 60, 20);
        alchemicalFireBlock.setFlammable(Blocks.PURPLE_CARPET, 60, 20);
        alchemicalFireBlock.setFlammable(Blocks.BLUE_CARPET, 60, 20);
        alchemicalFireBlock.setFlammable(Blocks.BROWN_CARPET, 60, 20);
        alchemicalFireBlock.setFlammable(Blocks.GREEN_CARPET, 60, 20);
        alchemicalFireBlock.setFlammable(Blocks.RED_CARPET, 60, 20);
        alchemicalFireBlock.setFlammable(Blocks.BLACK_CARPET, 60, 20);
        alchemicalFireBlock.setFlammable(Blocks.DRIED_KELP_BLOCK, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.BAMBOO, 60, 60);
        alchemicalFireBlock.setFlammable(Blocks.SCAFFOLDING, 60, 60);
        alchemicalFireBlock.setFlammable(Blocks.LECTERN, 30, 20);
        alchemicalFireBlock.setFlammable(Blocks.COMPOSTER, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.SWEET_BERRY_BUSH, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.BEEHIVE, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.BEE_NEST, 30, 20);
        alchemicalFireBlock.setFlammable(Blocks.AZALEA_LEAVES, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.FLOWERING_AZALEA_LEAVES, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.CAVE_VINES, 15, 60);
        alchemicalFireBlock.setFlammable(Blocks.CAVE_VINES_PLANT, 15, 60);
        alchemicalFireBlock.setFlammable(Blocks.SPORE_BLOSSOM, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.AZALEA, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.FLOWERING_AZALEA, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.BIG_DRIPLEAF, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.BIG_DRIPLEAF_STEM, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.SMALL_DRIPLEAF, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.HANGING_ROOTS, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.GLOW_LICHEN, 15, 100);
    }


    public Block getSelf() {
        return Blocks.FIRE;
    }
}
