package syric.alchemine.outputs.bouncy.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class AbstractVariablyBouncySlimeBlock extends SlimeBlock {

    public AbstractVariablyBouncySlimeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float distance) {
        if (entity.isSuppressingBounce()) {
            super.fallOn(level, state, pos, entity, distance);
        } else {
            int power = power(level, pos, state);
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
            int power = power(entity);
            double d0 = entity instanceof LivingEntity ? 0.1D * power : 0.8D * power;
            entity.setDeltaMovement(vec3.x, -vec3.y * d0, vec3.z);
        }
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        double d0 = Math.abs(entity.getDeltaMovement().y);
        int power = power(level, pos, state);
        if (d0 < 0.1D && !entity.isSteppingCarefully()) {
            double d1 = 0.4D + d0 * 0.018D * power;
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(d1, 1.0D, d1));
        }

        super.stepOn(level, pos, state, entity);
    }


    public int power(Entity entity) {
        return power(entity.getLevel(), entity.getOnPos(), entity.getLevel().getBlockState(entity.getOnPos()));
    }
    public int power(Level level, BlockPos pos, BlockState state) {
        return 0;
    }

}
