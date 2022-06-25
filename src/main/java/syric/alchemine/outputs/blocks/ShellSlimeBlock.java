package syric.alchemine.outputs.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.extensions.IForgeBlock;
import syric.alchemine.setup.AlchemineBlocks;
import net.minecraft.world.item.Items;

import java.util.concurrent.atomic.AtomicInteger;

import static syric.alchemine.util.ChatPrint.chatPrint;

public class ShellSlimeBlock extends HalfTransparentBlock implements IForgeBlock {
    public static final IntegerProperty HEALTH = IntegerProperty.create("health", 1, 4);
    protected static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
    protected int breakTime = 0;
    protected int lastBreakProgress = -1;
    protected int timeToBreak = 200;


    public ShellSlimeBlock(BlockBehaviour.Properties properties) {
        super (properties);
        this.registerDefaultState(this.defaultBlockState().setValue(HEALTH, 4));
    }

    public void destroy(LevelAccessor levelAccessor, BlockPos pos, BlockState state) {
        damage(levelAccessor, pos, state, 2);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (player.isCreative()) {
            level.destroyBlock(pos, true);
        }
        return true;
    }

    @Override
    public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {
        damage(level, pos, state, 2);
        this.wasExploded(level, pos, explosion);
    }

    //Deals half the damage directly and attempts to dissipate the rest
    private void damage(LevelAccessor level, BlockPos pos, BlockState state, int damage) {
        if (state.getBlock() != this) {
            return;
        }
        if (level.isClientSide()) {
            return;
        }

        int rawDamage = damage;
        int directDamage = 0;
        int dissipateDamage = 0;
        int failedDissipate = 0;


        while (rawDamage > 0) {
            if (rawDamage % 2 == 1) {
                directDamage++;
            } else {
                dissipateDamage++;
            }
            rawDamage --;
        }

        if (level instanceof Level lv) {
//            chatPrint("Dealing " + directDamage + " direct and " + dissipateDamage + " dissipated damage", lv);
        }



        if (dissipateDamage > 0) {
            failedDissipate = distributeDamage(level, pos, state, dissipateDamage);
        }
        state = directDamage(level, pos, state, directDamage);
        if (failedDissipate > 0) {
            directDamage(level, pos, state, failedDissipate);
        }
    }

    //Reduces the blockstate by the specified amount and returns the new blockstate
    private BlockState directDamage(LevelAccessor level, BlockPos pos, BlockState state, int damage) {
        if (level instanceof Level lv) {
//            chatPrint("dealing " + damage + " direct damage to a shellslime block", lv);
        }
        if (level.isClientSide()) {
            return state;
        }
        if (state.getBlock().equals(this)) {
            if (state.getValue(HEALTH) <= damage) {
                level.destroyBlock(pos, false);
                return Blocks.AIR.defaultBlockState();
            } else {
                int newHealth = state.getValue(HEALTH) - damage;
                BlockState newState = state.setValue(HEALTH, newHealth);
                level.destroyBlock(pos, false);
                level.setBlock(pos, newState, 2);
                return newState;
            }
        }
        return state;
    }

    //Deals a certain amount of damage to other blocks. Returns the number of damage points it failed to dissipate.
    private int distributeDamage(LevelAccessor level, BlockPos pos, BlockState state, int damage) {
        if (level instanceof Level lv) {
//            chatPrint("dealing " + damage + " distributed damage to surrounding blocks", lv);
        }
        int damageToDistribute = damage;
        int damageSuccessfullyDistributed = 0;

        for (int i = 0; i < damageToDistribute; i++) {
            AtomicInteger blocksUsed = new AtomicInteger();
            BlockPos.betweenClosedStream(new AABB(pos).inflate(1))
                    .filter(c -> level.getBlockState(c).getBlock().equals(AlchemineBlocks.SHELL_SLIME.get()))
                    .filter(c -> c.above().equals(pos) || c.below().equals(pos) || c.east().equals(pos) || c.west().equals(pos) || c.north().equals(pos) || c.south().equals(pos))
                    .forEach(c -> {
                        directDamage(level, c, level.getBlockState(c), 1);
                        blocksUsed.getAndIncrement();
                    });
            if (blocksUsed.get() >= 4) {
                damageSuccessfullyDistributed++;
            }
        }
        if (level instanceof Level lv) {
//            chatPrint("Failed to distribute " + (damageToDistribute - damageSuccessfullyDistributed) + " damage", lv);
        }

        return damageToDistribute - damageSuccessfullyDistributed;
    }

    public void onProjectileHit(Level level, BlockState state, BlockHitResult result, Projectile projectile) {
        EntityType<?> type = projectile.getType();
        boolean kill = false;
        if (type == EntityType.ARROW) {
            damage(level, result.getBlockPos(), state, 1);
            kill = true;
        } else if (type == EntityType.FIREBALL) {
            damage(level, result.getBlockPos(), state, 6);
            kill = true;
        } else if (type == EntityType.WITHER_SKULL) {
            directDamage(level, result.getBlockPos(), state, 2);
            damage(level, result.getBlockPos(), state, 4);
            kill = true;
        } else if (type == EntityType.DRAGON_FIREBALL) {
            damage(level, result.getBlockPos(), state, 8);
            kill = true;
        } else if (projectile instanceof ThrownTrident trident) {
            double speed = trident.getDeltaMovement().length();
            if (speed > 0.5) {
//                chatPrint("Speed of trident: " + speed, level);
                directDamage(level, result.getBlockPos(), state, 4);
            }
        }
        if (kill) {
            projectile.kill();
        }
    }

    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof Monster) {

            ++this.breakTime;
            int i = (int)((float)this.breakTime / (float)this.timeToBreak * 10.0F);
            if (i != this.lastBreakProgress) {
                entity.level.destroyBlockProgress(entity.getId(), pos, i);
                this.lastBreakProgress = i;
            }

            if (breakTime == timeToBreak) {
                damage(level, pos, state, 2);
                this.breakTime = 0;
            }


            if (entity instanceof Creeper creeper) {
                creeper.ignite();
            }

        }

        super.entityInside(state, level, pos, entity);
    }


    //Stuff relating to automatic destruction
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState state2, boolean bool) {
        if (!level.isClientSide) {
            level.scheduleTick(pos, this, 1200);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource source) {
        super.tick(state, level, pos, source);
        level.destroyBlock(pos, false);
//        chatPrint("Decay destroyed a Shellslime block", level);
    }



//    public boolean distributeDamage(LevelAccessor level, BlockPos pos) {
//        AtomicInteger successfulDistributions = new AtomicInteger();
//        BlockPos.betweenClosedStream(new AABB(pos).inflate(1))
//                .filter(c -> level.getBlockState(c).getBlock().equals(AlchemineBlocks.SHELL_SLIME.get()))
//                .filter(c -> c.above().equals(pos) || c.below().equals(pos) || c.east().equals(pos) || c.west().equals(pos) || c.north().equals(pos) || c.south().equals(pos))
//                .forEach(c -> {
//                    breakOneLevel(level, c, level.getBlockState(c));
//                    successfulDistributions.getAndIncrement();
//                });
//        return successfulDistributions.get() > 2;
//    }
//
//    public void breakOneLevel(LevelAccessor level, BlockPos pos, BlockState state) {
//        if (state.getValue(HEALTH) > 1) {
//            int currentHealth = state.getValue(HEALTH);
//            level.setBlock(pos, state.setValue(HEALTH, currentHealth - 1), 2);
//        } else {
//            level.destroyBlock(pos, false);
//        }
//    }


    @Override
    public boolean isPathfindable(BlockState state, BlockGetter getter, BlockPos pos, PathComputationType type) {
        return true;
    }

    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HEALTH);
    }

    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        if (context instanceof EntityCollisionContext entityCollisionContext) {
            if (entityCollisionContext.getEntity() instanceof Monster) {
                return SHAPE;
            }
        }
        return Shapes.block();
    }


}