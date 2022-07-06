package syric.alchemine.outputs.slippery.blocks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import syric.alchemine.setup.AlchemineBlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static syric.alchemine.util.ChatPrint.chatPrint;

public class WallSlideBlock extends HorizontalDirectionalBlock {
    private static final Map<Direction, VoxelShape> SHAPE_MAP = Maps.newEnumMap(ImmutableMap.of(
            Direction.NORTH, Block.box(0.0D, 0.0D, 14.0D, 16.0D, 16.0D, 16.0D),
            Direction.SOUTH, Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 2.0D),
            Direction.EAST, Block.box(0.0D, 0.0D, 0.0D, 2.0D, 16.0D, 16.0D),
            Direction.WEST, Block.box(14.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)));
    public static final BooleanProperty PERMANENT = BooleanProperty.create("permanent");


    public WallSlideBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(PERMANENT, true));
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
        if (isColliding(pos, state, entity)) {
            this.doSlideMovement(level, entity);
        }

        super.entityInside(state, level, pos, entity);
    }

    private boolean isColliding(BlockPos pos, BlockState state, Entity entity) {
        VoxelShape shape = SHAPE_MAP.get(state.getValue(FACING));
        VoxelShape positionedShape = shape.move((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
        return Shapes.joinIsNotEmpty(positionedShape, Shapes.create(entity.getBoundingBox()), BooleanOp.AND);
    }

    private void doSlideMovement(Level level, Entity entity) {
        Vec3 vec3 = entity.getDeltaMovement();
        boolean sliding = false;
        if (vec3.y <= -0.015D) {
            entity.setDeltaMovement(new Vec3(vec3.x*1.01, -0.015D, vec3.z*1.01));
            entity.resetFallDistance();
            sliding = true;
//            chatPrint("Started sliding", entity);
        } else if (vec3.y >= 0D) {
            entity.setDeltaMovement(new Vec3(vec3.x*1.01, vec3.y*0.99, vec3.z*1.01));
            entity.resetFallDistance();
            sliding = true;
        }

        if (doesEntityDoWallSlideEffects(entity) && sliding) {
            if (level.random.nextInt(5) == 0) {
                entity.playSound(SoundEvents.HONEY_BLOCK_SLIDE, 1.0F, 1.0F);
            }

//            if (!level.isClientSide && level.random.nextInt(5) == 0) {
//                level.broadcastEntityEvent(entity, (byte)53);
//            }
        }

    }

    private static boolean doesEntityDoWallSlideEffects(Entity entity) {
        return entity instanceof LivingEntity || entity instanceof AbstractMinecart || entity instanceof PrimedTnt || entity instanceof Boat;
    }

    public boolean skipRendering(BlockState state1, BlockState state2, Direction dir) {
        if (dir == state1.getValue(FACING) || dir == state1.getValue(FACING).getOpposite()) {
            return false;
        } else {
            return (state2.is(this) && state2 == state1) || super.skipRendering(state1, state2, dir);
        }
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    //For placing via effects
    public static BlockState getEffectState(Level level, BlockPos pos, Direction dir) {
        Direction[] directions = {dir, dir.getOpposite(), dir.getClockWise(), dir.getCounterClockWise()};

        for (Direction direction : directions) {
            BlockPos wallCandidate = pos.relative(direction);
            if (level.getBlockState(wallCandidate).isFaceSturdy(level, wallCandidate, direction)) {
                return AlchemineBlocks.WALL_SLIDE.get().defaultBlockState().setValue(FACING, direction);
            }
        }
        return Blocks.AIR.defaultBlockState();
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PERMANENT);
    }

}
