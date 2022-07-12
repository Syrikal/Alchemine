package syric.alchemine.outputs.fire.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SmokeCloudBlock extends Block {

    public SmokeCloudBlock(Properties properties) {
        super(properties);
    }


    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource source) {
//        if (source.nextInt(24) == 0) {
//            level.playLocalSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 1.0F + source.nextFloat(), source.nextFloat() * 0.7F + 0.3F, false);
//        }

        for (int i = 0; i < 4; i++) {
            double d0 = (double) pos.getX() + source.nextDouble();
            double d1 = (double) pos.getY() + source.nextDouble();
            double d2 = (double) pos.getZ() + source.nextDouble();
            level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }

    }


}
