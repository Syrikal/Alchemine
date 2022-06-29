package syric.alchemine.outputs.slippery.blocks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;
import java.util.Map;

import static syric.alchemine.util.ChatPrint.chatPrint;

public class WallSlideBlock extends HorizontalDirectionalBlock {
    private static final Map<Direction, VoxelShape> SHAPE_MAP = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(0.0D, 0.0D, 14.0D, 16.0D, 16.0D, 16.0D), Direction.SOUTH, Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 2.0D), Direction.EAST, Block.box(0.0D, 0.0D, 0.0D, 2.0D, 16.0D, 16.0D), Direction.WEST, Block.box(14.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)));


    public WallSlideBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE_MAP.get(state.getValue(FACING));
    }

    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.relative(state.getValue(FACING).getOpposite())).getMaterial().isSolid();
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor level, BlockPos pos1, BlockPos pos2) {
        return direction.getOpposite() == state.getValue(FACING) && !state.canSurvive(level, pos1) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, state2, level, pos1, pos2);
    }

    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (this.insideHitbox(level, state, pos, entity)) {
            this.doSlideMovement(level, entity);
        }

        super.entityInside(state, level, pos, entity);
    }


    private boolean insideHitbox(Level level, BlockState state, BlockPos pos, Entity entity) {
        AABB shape = SHAPE_MAP.get(state.getValue(FACING)).bounds();
        List<Entity> contained = level.getEntities(null, shape);
        List<VoxelShape> contained2 = level.getEntityCollisions(null, shape);
        boolean ret = contained2.contains(Shapes.create(entity.getBoundingBox()));
        chatPrint("Entities in block: " + contained2, level);
//        return contained.contains(entity);
        return ret;
    }

    private void doSlideMovement(Level level, Entity entity) {
        Vec3 vec3 = entity.getDeltaMovement();
        boolean sliding = false;
        if (vec3.y < -0.02D) {
            entity.setDeltaMovement(new Vec3(vec3.x*1.05, -0.02D, vec3.z*1.05));
            entity.resetFallDistance();
            sliding = true;
            chatPrint("Started sliding", entity);
        } else {
//            chatPrint("Not falling fast enough to start sliding", entity);
        }

        if (doesEntityDoWallSlideFfects(entity) && sliding) {
            if (level.random.nextInt(5) == 0) {
                entity.playSound(SoundEvents.HONEY_BLOCK_SLIDE, 1.0F, 1.0F);
            }

            if (!level.isClientSide && level.random.nextInt(5) == 0) {
                level.broadcastEntityEvent(entity, (byte)53);
            }
        }

    }

    private static boolean doesEntityDoWallSlideFfects(Entity entity) {
        return entity instanceof LivingEntity || entity instanceof AbstractMinecart || entity instanceof PrimedTnt || entity instanceof Boat;
    }


    public boolean skipRendering(BlockState state1, BlockState state2, Direction dir) {
        return state2.is(this) ? true : super.skipRendering(state1, state2, dir);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

}
