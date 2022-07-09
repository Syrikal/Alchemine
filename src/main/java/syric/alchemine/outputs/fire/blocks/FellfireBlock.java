package syric.alchemine.outputs.fire.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import org.apache.commons.lang3.StringUtils;
import syric.alchemine.setup.AlchemineBlocks;

import static syric.alchemine.util.ChatPrint.chatPrint;

public class FellfireBlock extends AbstractAlchemicalFireBlock {

    public FellfireBlock(Properties properties, float damage) {
        super(properties, damage);
    }

    @Override
    public int getFireTickDelay(RandomSource source) {
        return 20 + source.nextInt(10);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!entity.fireImmune()) {
            entity.setRemainingFireTicks(entity.getRemainingFireTicks() + 1);
            if (entity.getRemainingFireTicks() >= -10 && entity.getRemainingFireTicks() <= 0) {
                entity.setSecondsOnFire(16);
            }
        }
        entity.hurt(DamageSource.IN_FIRE, this.getFireDamage());
    }

    @Override
    //Age makes it slow down more than normal, but it's much faster at first.
    public int modifiedIgniteChance(int baseIgniteChance, int age, Level level, BlockPos pos) {
        baseIgniteChance += 60;
        baseIgniteChance += level.getDifficulty().getId() * 7;
        baseIgniteChance /= (2 * age) + 10;
        if (level.isHumidAt(pos)) {baseIgniteChance /= 2;}
        if (age == 15) {
            baseIgniteChance /= 6;
        }
        return baseIgniteChance;
    }

    @Override
    //Chance to remove it if it's old enough
    public void tick(BlockState fireState, ServerLevel level, BlockPos firePos, RandomSource randSource) {
        if (level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK) && fireState.getValue(AGE) == 15 && randSource.nextInt(50) == 0) {
            level.removeBlock(firePos, false);
            return;
        }
        super.tick(fireState, level, firePos, randSource);
    }


    public Block getSelf() {
        return AlchemineBlocks.FELL_FIRE.get();
    }

    @Override
    public boolean canBlockIgnite(BlockGetter level, BlockPos pos, Direction face) {
        return (level.getBlockState(pos).isFaceSturdy(level, pos, face) || level.getBlockState(pos).is(BlockTags.LEAVES));
    }

    @Override
    public int getBlockIgniteChance(BlockGetter level, BlockPos pos, Direction face) {
        if (!level.getBlockState(pos).isFaceSturdy(level, pos, face) && !level.getBlockState(pos).is(BlockTags.LEAVES)) {
            return 0;
        } else {
            return 40;
        }
    }

    @Override
    public int getBlockBurnChance(Level level, BlockPos pos, Direction face) {
        return 0;
    }


    @Override
    //Making it quieter
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource source) {
        if (source.nextInt(24) == 0) {
            level.playLocalSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 0.1F + source.nextFloat() * 0.2F, source.nextFloat() * 0.7F + 0.3F, false);
        }

        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);

        //Replace canIgniteStored(belowState) with canBlockIgnite(level, pos, face)


        if (!this.canBlockIgnite(level, belowPos, Direction.UP) && !belowState.isFaceSturdy(level, belowPos, Direction.UP)) {
            if (this.canBlockIgnite(level, pos.west(), Direction.EAST)) {
                for(int j = 0; j < 2; ++j) {
                    double d3 = (double)pos.getX() + source.nextDouble() * (double)0.1F;
                    double d8 = (double)pos.getY() + source.nextDouble();
                    double d13 = (double)pos.getZ() + source.nextDouble();
                    level.addParticle(ParticleTypes.LARGE_SMOKE, d3, d8, d13, 0.0D, 0.0D, 0.0D);
                }
            }

            if (this.canBlockIgnite(level, pos.east(), Direction.WEST)) {
                for(int k = 0; k < 2; ++k) {
                    double d4 = (double)(pos.getX() + 1) - source.nextDouble() * (double)0.1F;
                    double d9 = (double)pos.getY() + source.nextDouble();
                    double d14 = (double)pos.getZ() + source.nextDouble();
                    level.addParticle(ParticleTypes.LARGE_SMOKE, d4, d9, d14, 0.0D, 0.0D, 0.0D);
                }
            }

            if (this.canBlockIgnite(level, pos.north(), Direction.SOUTH)) {
                for(int l = 0; l < 2; ++l) {
                    double d5 = (double)pos.getX() + source.nextDouble();
                    double d10 = (double)pos.getY() + source.nextDouble();
                    double d15 = (double)pos.getZ() + source.nextDouble() * (double)0.1F;
                    level.addParticle(ParticleTypes.LARGE_SMOKE, d5, d10, d15, 0.0D, 0.0D, 0.0D);
                }
            }

            if (this.canBlockIgnite(level, pos.south(), Direction.NORTH)) {
                for(int i1 = 0; i1 < 2; ++i1) {
                    double d6 = (double)pos.getX() + source.nextDouble();
                    double d11 = (double)pos.getY() + source.nextDouble();
                    double d16 = (double)(pos.getZ() + 1) - source.nextDouble() * (double)0.1F;
                    level.addParticle(ParticleTypes.LARGE_SMOKE, d6, d11, d16, 0.0D, 0.0D, 0.0D);
                }
            }

            if (this.canBlockIgnite(level, pos.above(), Direction.DOWN)) {
                for(int j1 = 0; j1 < 2; ++j1) {
                    double d7 = (double)pos.getX() + source.nextDouble();
                    double d12 = (double)(pos.getY() + 1) - source.nextDouble() * (double)0.1F;
                    double d17 = (double)pos.getZ() + source.nextDouble();
                    level.addParticle(ParticleTypes.LARGE_SMOKE, d7, d12, d17, 0.0D, 0.0D, 0.0D);
                }
            }
        } else {
            for(int i = 0; i < 3; ++i) {
                double d0 = (double)pos.getX() + source.nextDouble();
                double d1 = (double)pos.getY() + source.nextDouble() * 0.5D + 0.5D;
                double d2 = (double)pos.getZ() + source.nextDouble();
                level.addParticle(ParticleTypes.LARGE_SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
            }
        }


        //OLD VERSION!!!
//        if (!this.canIgniteStored(belowState) && !belowState.isFaceSturdy(level, belowPos, Direction.UP)) {
//            if (this.canIgniteStored(level.getBlockState(pos.west()))) {
//                for(int j = 0; j < 2; ++j) {
//                    double d3 = (double)pos.getX() + source.nextDouble() * (double)0.1F;
//                    double d8 = (double)pos.getY() + source.nextDouble();
//                    double d13 = (double)pos.getZ() + source.nextDouble();
//                    level.addParticle(ParticleTypes.LARGE_SMOKE, d3, d8, d13, 0.0D, 0.0D, 0.0D);
//                }
//            }
//
//            if (this.canIgniteStored(level.getBlockState(pos.east()))) {
//                for(int k = 0; k < 2; ++k) {
//                    double d4 = (double)(pos.getX() + 1) - source.nextDouble() * (double)0.1F;
//                    double d9 = (double)pos.getY() + source.nextDouble();
//                    double d14 = (double)pos.getZ() + source.nextDouble();
//                    level.addParticle(ParticleTypes.LARGE_SMOKE, d4, d9, d14, 0.0D, 0.0D, 0.0D);
//                }
//            }
//
//            if (this.canIgniteStored(level.getBlockState(pos.north()))) {
//                for(int l = 0; l < 2; ++l) {
//                    double d5 = (double)pos.getX() + source.nextDouble();
//                    double d10 = (double)pos.getY() + source.nextDouble();
//                    double d15 = (double)pos.getZ() + source.nextDouble() * (double)0.1F;
//                    level.addParticle(ParticleTypes.LARGE_SMOKE, d5, d10, d15, 0.0D, 0.0D, 0.0D);
//                }
//            }
//
//            if (this.canIgniteStored(level.getBlockState(pos.south()))) {
//                for(int i1 = 0; i1 < 2; ++i1) {
//                    double d6 = (double)pos.getX() + source.nextDouble();
//                    double d11 = (double)pos.getY() + source.nextDouble();
//                    double d16 = (double)(pos.getZ() + 1) - source.nextDouble() * (double)0.1F;
//                    level.addParticle(ParticleTypes.LARGE_SMOKE, d6, d11, d16, 0.0D, 0.0D, 0.0D);
//                }
//            }
//
//            if (this.canIgniteStored(level.getBlockState(pos.above()))) {
//                for(int j1 = 0; j1 < 2; ++j1) {
//                    double d7 = (double)pos.getX() + source.nextDouble();
//                    double d12 = (double)(pos.getY() + 1) - source.nextDouble() * (double)0.1F;
//                    double d17 = (double)pos.getZ() + source.nextDouble();
//                    level.addParticle(ParticleTypes.LARGE_SMOKE, d7, d12, d17, 0.0D, 0.0D, 0.0D);
//                }
//            }
//        } else {
//            for(int i = 0; i < 3; ++i) {
//                double d0 = (double)pos.getX() + source.nextDouble();
//                double d1 = (double)pos.getY() + source.nextDouble() * 0.5D + 0.5D;
//                double d2 = (double)pos.getZ() + source.nextDouble();
//                level.addParticle(ParticleTypes.LARGE_SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
//            }
//        }

    }

}
