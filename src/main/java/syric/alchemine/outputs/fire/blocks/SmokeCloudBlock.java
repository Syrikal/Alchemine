package syric.alchemine.outputs.fire.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import syric.alchemine.outputs.general.blocks.PossiblyPermanentBlock;

public class SmokeCloudBlock extends Block implements PossiblyPermanentBlock {

    public SmokeCloudBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(PossiblyPermanentBlock.PERMANENT, false));
    }
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PossiblyPermanentBlock.PERMANENT);
    }


    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource source) {
//        if (source.nextInt(24) == 0) {
//            level.playLocalSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 1.0F + source.nextFloat(), source.nextFloat() * 0.7F + 0.3F, false);
//        }

        for (int i = 0; i < 2; i++) {
            double d0 = (double) pos.getX() - 0.1 + source.nextDouble() * 1.2;
            double d1 = (double) pos.getY() - 0.1 + source.nextDouble() * 1.2;
            double d2 = (double) pos.getZ() - 0.1 + source.nextDouble() * 1.2;
            level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
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
