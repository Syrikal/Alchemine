package syric.alchemine.outputs.slippery.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
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

import javax.annotation.Nullable;

public class GroovedIceBlock extends RotatedPillarBlock {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;


    public GroovedIceBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(AXIS, Direction.Axis.X));
    }

    //Directionally Slippery Stuff


    //Items only flow in one direction stuff (collison model different, subtract a groove)


    //Orientable Block Stuff
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> state) {
        state.add(AXIS);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction.Axis candidate = context.getClickedFace().getAxis();
        if (candidate == Direction.Axis.Y) {
            candidate = Direction.Axis.X;
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
