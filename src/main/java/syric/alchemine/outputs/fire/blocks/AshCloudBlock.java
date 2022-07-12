package syric.alchemine.outputs.fire.blocks;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AshCloudBlock extends Block {
    public MobEffectInstance getEffect() {return new MobEffectInstance(MobEffects.WITHER, 100, 0, true, true); }

    public AshCloudBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity live) {
            if (!live.hasEffect(MobEffects.WITHER)) {
                live.addEffect(getEffect());
            }
        }
    }


    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource source) {
//        if (source.nextInt(24) == 0) {
//            level.playLocalSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 1.0F + source.nextFloat(), source.nextFloat() * 0.7F + 0.3F, false);
//        }

        for (int i = 0; i < 6; i++) {
            double d0 = (double) pos.getX() + source.nextDouble();
            double d1 = (double) pos.getY() + source.nextDouble();
            double d2 = (double) pos.getZ() + source.nextDouble();
            level.addParticle(getParticle(source), d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }

    }

    private ParticleOptions getParticle(RandomSource source) {
        double d = source.nextDouble();
        if (d < 0.5) {
            return ParticleTypes.ASH;
        } else if (d < 0.85) {
            return ParticleTypes.WHITE_ASH;
        } else {
            return ParticleTypes.LARGE_SMOKE;
        }

    }


}
