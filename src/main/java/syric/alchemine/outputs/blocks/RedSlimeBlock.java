package syric.alchemine.outputs.blocks;

import com.mojang.math.Vector3f;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collections;

public class RedSlimeBlock extends SlimeBlock {
    private static final Vec3[] COLORS = Util.make(new Vec3[16], (input) -> {
        for(int i = 0; i <= 15; ++i) {
            float f = (float)i / 15.0F;
            float f1 = f * 0.6F + (f > 0.0F ? 0.4F : 0.3F);
            float f2 = Mth.clamp(f * f * 0.7F - 0.5F, 0.0F, 1.0F);
            float f3 = Mth.clamp(f * f * 0.6F - 0.7F, 0.0F, 1.0F);
            input[i] = new Vec3((double)f1, (double)f2, (double)f3);
        }
    });

    public RedSlimeBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float distance) {
        if (entity.isSuppressingBounce()) {
            super.fallOn(level, state, pos, entity, distance);
        } else {
            int power = level.getBestNeighborSignal(pos);
            float multiplier = Math.max(1 - (float) power / 10.0F, 0.0F);
            entity.causeFallDamage(distance, multiplier, DamageSource.FALL);
        }

    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter getter, Entity entity) {
        if (entity.isSuppressingBounce()) {
            super.updateEntityAfterFallOn(getter, entity);
        } else {
            this.bounceUp(entity);
        }
    }

    private void bounceUp(Entity entity) {
        Vec3 vec3 = entity.getDeltaMovement();
        if (vec3.y < 0.0D) {
            int power = getPower(entity.getLevel(), entity.getOnPos());
            double d0 = entity instanceof LivingEntity ? 0.1D * power : 0.8D * power;
            entity.setDeltaMovement(vec3.x, -vec3.y * d0, vec3.z);
        }
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        double d0 = Math.abs(entity.getDeltaMovement().y);
        int power = level.getBestNeighborSignal(pos);
        if (d0 < 0.1D && !entity.isSteppingCarefully()) {
            double d1 = 0.4D + d0 * 0.018D * power;
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(d1, 1.0D, d1));
        }

        super.stepOn(level, pos, state, entity);
    }


    private int getPower(Level level, BlockPos pos) {
        if (level.getBlockState(pos).getBlock() == this) {
            return level.getBestNeighborSignal(pos);
        } else if (level.getBlockState(pos.below()).getBlock() == this) {
            return level.getBestNeighborSignal(pos.below());
        } else {
            BlockPos pos1 = new BlockPos(pos.getX()-1, pos.getY()-1,pos.getZ()-1);
            BlockPos pos2 = new BlockPos(pos.getX()+1, pos.getY()+1, pos.getZ()+1);
            ArrayList<Integer> options = new ArrayList<Integer>();
            BlockPos.betweenClosedStream(pos1, pos2)
                    .filter(c -> level.getBlockState(c).getBlock().equals(this))
                    .forEach(c -> options.add(level.getBestNeighborSignal(c)));
            if (options.isEmpty()) {
                return 0;
            } else {
                return Collections.max(options);
            }
        }
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource source) {
        int power = getPower(level, pos);
        if (power != 0) {
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();
            double d0 = (double)i + Mth.randomBetween(source, -0.1F, 1.1F);
            double d1 = (double)j + Mth.randomBetween(source, -0.1F, 1.1F);
            double d2 = (double)k + Mth.randomBetween(source, -0.1F, 1.1F);
            Vec3 color = COLORS[power];
            level.addParticle(new DustParticleOptions(new Vector3f(color), 1.0F), d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
   }


}
