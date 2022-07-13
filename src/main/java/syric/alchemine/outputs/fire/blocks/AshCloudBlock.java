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
import net.minecraft.world.entity.item.FallingBlockEntity;
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
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import syric.alchemine.outputs.bouncy.blocks.AbstractImmersionBlock;
import syric.alchemine.outputs.general.blocks.PossiblyPermanentBlock;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AshCloudBlock extends Block implements PossiblyPermanentBlock {
    public MobEffectInstance getEffect() {return new MobEffectInstance(MobEffects.WITHER, 100, 2, true, true); }
    public MobEffectInstance getSlow() {return new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1, true, true); }


    public AshCloudBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(PossiblyPermanentBlock.PERMANENT, false));
    }
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PossiblyPermanentBlock.PERMANENT);
    }

    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity live) {
            if (!live.hasEffect(MobEffects.WITHER)) {
                live.addEffect(getEffect());
                live.addEffect(getSlow());
            }
        }
    }


    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource source) {
        if (source.nextInt(100) == 0) {
            level.playLocalSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, SoundEvents.SNOW_FALL, SoundSource.BLOCKS, source.nextFloat() * 0.07F, source.nextFloat() * 0.4F + 0.2F, false);
        }

        for (int i = 0; i < 2; i++) {
            double d0 = (double) pos.getX() - 0.1 + source.nextDouble() * 1.2;
            double d1 = (double) pos.getY() - 0.1 + source.nextDouble() * 1.2;
            double d2 = (double) pos.getZ() - 0.1 + source.nextDouble() * 1.2;
            level.addParticle(getParticle(source), d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }

    }

    private ParticleOptions getParticle(RandomSource source) {
        double d = source.nextDouble();
        if (d < 0.4) {
            return ParticleTypes.ASH;
        } else if (d < 0.8) {
            return ParticleTypes.WHITE_ASH;
        } else {
            return ParticleTypes.CAMPFIRE_SIGNAL_SMOKE;
        }

    }

    //Stuff relating to automatic destruction
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState state2, boolean bool) {
        if (!level.isClientSide) {
            level.scheduleTick(pos, this, this.getDuration());
        }
    }
    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource source) {
        super.tick(state, level, pos, source);
        if (!state.getValue(PERMANENT)) {
            level.destroyBlock(pos, false);
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
