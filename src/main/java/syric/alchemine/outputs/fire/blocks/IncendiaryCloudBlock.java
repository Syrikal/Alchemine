package syric.alchemine.outputs.fire.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import syric.alchemine.outputs.general.blocks.PossiblyPermanentBlock;

public class IncendiaryCloudBlock extends Block implements PossiblyPermanentBlock {

    public IncendiaryCloudBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(PossiblyPermanentBlock.PERMANENT, false));
    }
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PossiblyPermanentBlock.PERMANENT);
    }

    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity live) {
            if (!entity.fireImmune()) {
                entity.setRemainingFireTicks(entity.getRemainingFireTicks() + 1);
                if (entity.getRemainingFireTicks() == 0) {
                    entity.setSecondsOnFire(12);
                }
            }
            entity.hurt(DamageSource.IN_FIRE, 2F);
        }
    }


    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource source) {
        if (source.nextInt(200) == 0) {
            level.playLocalSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 0.5F + source.nextFloat(), source.nextFloat() * 0.7F + 0.3F, false);
        }

        for (int i = 0; i < 1; i++) {
            double d0 = (double) pos.getX() - 0.1 + source.nextDouble() * 1.2;
            double d1 = (double) pos.getY() - 0.1 + source.nextDouble() * 1.2;
            double d2 = (double) pos.getZ() - 0.1 + source.nextDouble() * 1.2;

            ParticleOptions particleOptions = getParticle(source);
            if (particleOptions.getType() == ParticleTypes.LAVA) {
                d1 = Math.max(d1, (double) pos.getY() + source.nextDouble());
            }
            level.addParticle(particleOptions, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }

    }

    private ParticleOptions getParticle(RandomSource source) {
        double d = source.nextDouble();
        if (d < 0.3) {
            return ParticleTypes.CAMPFIRE_SIGNAL_SMOKE;
        } else if (d < 0.8) {
            return ParticleTypes.SMALL_FLAME;
        } else if (d < 0.9) {
            return ParticleTypes.LAVA;
        } else {
            return ParticleTypes.LARGE_SMOKE;
        }

    }

    //Fire should spread from it. On placement and destruction, probably
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState state2, boolean bool) {
        if (level.isClientSide()) {
            return;
        }
        level.scheduleTick(pos, this, 1200);

        RandomSource rdm = RandomSource.create();
        if (rdm.nextDouble() < 0.1) {
            //Try to set blocks on fire.
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            for(int xOffset = -1; xOffset <= 1; ++xOffset) {
                for(int zOffset = -1; zOffset <= 1; ++zOffset) {
                    for(int yOffset = -1; yOffset <= 4; ++yOffset) {
                        if (xOffset != 0 || yOffset != 0 || zOffset != 0) {

                            //We create positionIgniteChance and rollCap. Then we take a random number from 0
                            //to rollCap and ignite if it's less than positionIgniteChance.
                            int rollCap = 100;
                            if (yOffset > 1) {
                                rollCap += (yOffset - 1) * 100;
                            }

                            blockpos$mutableblockpos.setWithOffset(pos, xOffset, yOffset, zOffset);

                            int positionIgniteChance = getPositionIgniteChance(level, blockpos$mutableblockpos);
                            if (positionIgniteChance > 0) {

                                int newIgniteChance = modifiedIgniteChance(positionIgniteChance, 0, level, pos);

                                if (newIgniteChance > 0 && rdm.nextInt(rollCap) <= newIgniteChance && (!level.isRaining() || !AbstractAlchemicalFireBlock.isNearRain(level, blockpos$mutableblockpos))) {
                                    level.setBlockAndUpdate(blockpos$mutableblockpos, BaseFireBlock.getState(level, blockpos$mutableblockpos));
                                }
                            }
                        }
                    }
                }
            }





        }
    }

    private int getPositionIgniteChance(LevelReader levelReader, BlockPos pos) {
        if (!levelReader.isEmptyBlock(pos)) {
            return 0;
        } else {
            int igniteChance = 0;

            for(Direction direction : Direction.values()) {
                BlockState blockstate = levelReader.getBlockState(pos.relative(direction));
//                igniteChance = Math.max(getBlockStateIgniteOdds(blockstate), igniteChance);
                int contender = getFireSpreadSpeed(blockstate, levelReader, pos.relative(direction), direction.getOpposite());
                igniteChance = Math.max(contender, igniteChance);
            }
            return igniteChance;
        }
    }

    public int modifiedIgniteChance(int baseIgniteChance, int age, Level level, BlockPos pos) {
        baseIgniteChance += 40;
        baseIgniteChance += level.getDifficulty().getId() * 7;
        baseIgniteChance /= (age + 30);
        if (level.isHumidAt(pos)) {baseIgniteChance /= 2;}
        return baseIgniteChance;
    }


    @Override
    public void destroy(LevelAccessor levelAccessor, BlockPos pos, BlockState state) {
        if (levelAccessor.isClientSide()) {
            return;
        }
        if (levelAccessor.isEmptyBlock(pos)) {
            ((Level) levelAccessor).setBlockAndUpdate(pos, BaseFireBlock.getState(levelAccessor, pos));
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource source) {
        super.tick(state, level, pos, source);
        if (!state.getValue(PERMANENT)) {
            level.destroyBlock(pos, true);
            if (level.isEmptyBlock(pos)) {
                level.setBlockAndUpdate(pos, BaseFireBlock.getState(level, pos));
            }
        }
    }


    //SHAPE AND RENDERING
    @Override
    public boolean skipRendering(BlockState state, BlockState state2, Direction dir) {
        return state2.is(this) || super.skipRendering(state, state2, dir);
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    public VoxelShape getOcclusionShape(BlockState state, BlockGetter getter, BlockPos pos) {
        return Shapes.empty();
    }

    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

}
