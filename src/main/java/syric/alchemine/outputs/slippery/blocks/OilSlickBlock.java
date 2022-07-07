package syric.alchemine.outputs.slippery.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import syric.alchemine.outputs.general.blocks.PossiblyPermanentBlock;

public class OilSlickBlock extends Block implements PossiblyPermanentBlock {
    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
    protected final int duration;


    public OilSlickBlock(Properties properties, int dur) {
        super(properties);
        duration = dur;
        this.registerDefaultState(this.defaultBlockState().setValue(PossiblyPermanentBlock.PERMANENT, false));
    }

    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PossiblyPermanentBlock.PERMANENT);
    }

    public boolean isPathfindable(BlockState state, BlockGetter getter, BlockPos pos, PathComputationType type) {
        return switch (type) {
            case LAND -> true;
            case WATER, AIR -> false;
        };
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
    @Override
    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter getter, BlockPos pos) {
        return SHAPE;
    }
    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter getter, BlockPos pos) { return SHAPE; }
    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor level, BlockPos pos, BlockPos pos2) {
        return !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, state2, level, pos, pos2);
    }

//    @Override
//    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
//        Vec3 stuckVector;
//        Vec3 entityDistance = new Vec3(entity.getX() - pos.getX() - 0.5, 0, entity.getZ() - pos.getZ() - 0.5);
//        if (entity.getY() > pos.getY() + 0.07) {
////            chatPrint("Entity Y is " + entity.getY() + ", higher than block top of " + (pos.getY() + 0.06), entity);
//            return;
//        } else if (stickiness == 3 && (entityDistance.length() < 0.25 || entity.getDeltaMovement().length() < 0.001)) {
////            chatPrint("Distance to center: " + entityDistance.length() + ", delta length: " + entity.getDeltaMovement().length(), entity);
//            stuckVector = new Vec3(0.1, 1, 0.1);
//        } else if (stickiness == 2) {
//            stuckVector = new Vec3(0.2, 1, 0.2);
//        } else if (stickiness == 1) {
//            stuckVector = new Vec3(0.35, 1, 0.35);
//        } else {
//            stuckVector = new Vec3(1, 1, 1);
//        }
//
//        entity.makeStuckInBlock(state, stuckVector);
//    }


//    @Override
//    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
//        return level.getBlockState(pos.below()).getMaterial().isSolid();
////        return !level.isEmptyBlock(pos.below());
//    }

//    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
//        BlockState blockstate = level.getBlockState(pos.below());
//        return Block.isFaceFull(blockstate.getCollisionShape(level, pos.below()), Direction.UP);
//    }

    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return true;
    }

    //Stuff relating to automatic destruction
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState state2, boolean bool) {
        if (!level.isClientSide) {
            level.scheduleTick(pos, this, duration);
        }
    }
    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource source) {
        super.tick(state, level, pos, source);
        if (duration != 0 && !state.getValue(PERMANENT)) {
            level.destroyBlock(pos, false);
        }
    }

}
