package syric.alchemine.outputs.slippery.blocks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.*;
import net.minecraftforge.common.extensions.IForgeBlock;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.Map;

import static syric.alchemine.util.ChatPrint.chatPrint;

public class GroovedIceBlock extends Block implements IForgeBlock {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;
    private static final Map<Direction.Axis, VoxelShape> SHAPE_MAP = Maps.newEnumMap(ImmutableMap.of(Direction.Axis.Z, Block.box(5.0D, 15.0D, 0.0D, 11.0D, 16.0D, 16.0D), Direction.Axis.X, Block.box(0.0D, 15.0D, 5.0D, 16.0D, 16.0D, 11.0D), Direction.Axis.Y, Shapes.empty()));


    public GroovedIceBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(AXIS, Direction.Axis.X));
    }

    //Directionally Slippery Stuff
//    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
//        Vec3 vec3 = entity.getDeltaMovement();
//        Vec3 multiplier = new Vec3(1, 1, 1);
//        double x = vec3.x();
//        double z = vec3.z();
//        if (state.getValue(AXIS) == Direction.Axis.X) {
//            multiplier = new Vec3(1.6, 1, 1);
//        } else if (state.getValue(AXIS) == Direction.Axis.Z) {
//            multiplier = new Vec3(1, 1, 1.6);
//        } else {
//            LogUtils.getLogger().info("Axis on grooved ice is fucked up");
//        }
//        Vec3 newMotion = vec3.multiply(multiplier);
//        if (!level.isClientSide()) {
//            chatPrint("New vector: " + newMotion, level);
//            entity.setDeltaMovement(newMotion);
//            if (entity instanceof ServerPlayer) {
//                ((ServerPlayer) entity).connection.send(new ClientboundSetEntityMotionPacket(entity));
//            }
//        }
//        super.stepOn(level, pos, state, entity);
//    }

    @Override
    public float getFriction(BlockState state, LevelReader level, BlockPos pos, @org.jetbrains.annotations.Nullable Entity entity) {
        if (entity == null) {
            return 0.8F;
        }

        if (entity instanceof ServerPlayer) {
            return 1.2F;
        }
        //Get a unit vector in the appropriate direction
        Vec3 axisUnitVector = Vec3.ZERO;
        if (state.getValue(AXIS) == Direction.Axis.X) {
            axisUnitVector = new Vec3(1, 0, 0);
        } else if (state.getValue(AXIS) == Direction.Axis.Z) {
            axisUnitVector = new Vec3(0, 0, 1);
        }
        //The block can't be placed vertically

        //Take the dot product of that vector with a unit vector in the direction of the entity's movement
        //This effectively just returns the cosine of the angle between the entity's vector and the chosen axis
        //i.e. 0 when perpendicular, 1 when parallel, etc.
        Vec3 normalizedMovement = entity.getDeltaMovement().normalize();
        double dotProduct = Math.abs(normalizedMovement.dot(axisUnitVector));

        //Produce a string for reporting
        String vectorString = "(" + StringUtils.truncate(String.valueOf(normalizedMovement.x), 4) + "," + StringUtils.truncate(String.valueOf(normalizedMovement.y), 4) + "," + StringUtils.truncate(String.valueOf(normalizedMovement.z), 4) + ")";


        //Friction ranges from 0.6 to 1 depending on that cosine
        double finalMultiplier = Mth.lerp(dotProduct, 0.6, 1.02);
//        if (!level.isClientSide()) { finalMultiplier = 0.1; }
//        chatPrint("Friction: " + StringUtils.truncate(String.valueOf(finalMultiplier), 5) + ", dot:" + StringUtils.truncate(String.valueOf(dotProduct), 5) + ", vector:" + vectorString + (level.isClientSide() ? "clientside" : "serverside"), (Level) level);


        return (float) finalMultiplier;

    }


    //Items only flow in one direction stuff (collision model different, subtract a groove)
//    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
//        if (context instanceof EntityCollisionContext ecc) {
//            if (ecc.getEntity() instanceof ItemEntity) {
//                return Shapes.join(Shapes.block(), SHAPE_MAP.get(state.getValue(AXIS)), BooleanOp.ONLY_FIRST);
//            }
//        }
//        return Shapes.block();
//    }


    //Orientable Block Stuff
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> state) {
        state.add(AXIS);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction.Axis candidate = context.getClickedFace().getAxis();
        if (candidate == Direction.Axis.Y) {
            candidate = context.getHorizontalDirection().getAxis();
        }
        return this.defaultBlockState().setValue(AXIS, candidate);
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return rotatePillar(state, rot);
    }

    public static BlockState rotatePillar(BlockState state, Rotation rot) {
        return switch (rot) {
            case COUNTERCLOCKWISE_90, CLOCKWISE_90 -> switch ((Direction.Axis) state.getValue(AXIS)) {
                case X -> state.setValue(AXIS, Direction.Axis.Z);
                case Z -> state.setValue(AXIS, Direction.Axis.X);
                default -> state;
            };
            default -> state;
        };
    }



    //Ice Stuff
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        super.playerDestroy(level, player, pos, state, blockEntity, stack);
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, stack) == 0) {
            if (level.dimensionType().ultraWarm()) {
                level.removeBlock(pos, false);
                return;
            }

            Material material = level.getBlockState(pos.below()).getMaterial();
            if (material.blocksMotion() || material.isLiquid()) {
                level.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
            }
        }

    }

    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource source) {
        if (level.getBrightness(LightLayer.BLOCK, pos) > 11 - state.getLightBlock(level, pos)) {
            this.melt(state, level, pos);
        }

    }

    protected void melt(BlockState state, Level level, BlockPos pos) {
        if (level.dimensionType().ultraWarm()) {
            level.removeBlock(pos, false);
        } else {
            level.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
            level.neighborChanged(pos, Blocks.WATER, pos);
        }
    }

    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.NORMAL;
    }

}
