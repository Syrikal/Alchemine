package syric.alchemine.outputs.slippery.effects;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import syric.alchemine.outputs.general.alchemicaleffects.AlchemicalEffect;
import syric.alchemine.outputs.general.alchemicaleffects.effectsUtil;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.PlacementPattern;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.ReplaceablesFilter;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.SpherePattern;
import syric.alchemine.outputs.slippery.blocks.WallSlideBlock;
import syric.alchemine.setup.AlchemineBlocks;

import java.util.Collection;

import static net.minecraft.world.level.block.DirectionalBlock.FACING;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class WallSlideEffect implements AlchemicalEffect {

    public WallSlideEffect(){};

    public void detonate(UseOnContext context) {
        BlockPos pos = AlchemicalEffect.getOrigin(context);
        Level level = context.getLevel();
        Direction direction = context.getClickedFace();
        if (direction.getAxis() == Direction.Axis.Y) {
            direction = context.getHorizontalDirection().getCounterClockWise();
        }

        PlacementPattern place = new SpherePattern(pos, 10F);
        BlockState state1 = AlchemineBlocks.WALL_SLIDE.get().defaultBlockState().setValue(HORIZONTAL_FACING, direction);
        BlockState state2 = AlchemineBlocks.WALL_SLIDE.get().defaultBlockState().setValue(HORIZONTAL_FACING, direction.getOpposite());
        BlockState state3 = AlchemineBlocks.WALL_SLIDE.get().defaultBlockState().setValue(HORIZONTAL_FACING, direction.getClockWise());
        BlockState state4 = AlchemineBlocks.WALL_SLIDE.get().defaultBlockState().setValue(HORIZONTAL_FACING, direction.getCounterClockWise());

        WallPlace(level, place, state1, effectsUtil.AIR_ONLY, direction);
        WallPlace(level, place, state2, effectsUtil.AIR_ONLY, direction.getOpposite());
        WallPlace(level, place, state3, effectsUtil.AIR_ONLY, direction.getClockWise());
        WallPlace(level, place, state4, effectsUtil.AIR_ONLY, direction.getCounterClockWise());

    }

    public static void WallPlace(Level level, PlacementPattern pattern, BlockState state, ReplaceablesFilter replaceables, Direction direction) {
        pattern.blockList().filter(c -> checkWallPlaceable(level, c, direction, replaceables))
                .forEach(c -> {
                    level.destroyBlock(c, true);
                    level.setBlockAndUpdate(c, state);
                });
    }

    private static boolean checkWallPlaceable(Level level, BlockPos pos, Direction dir, ReplaceablesFilter replaceables) {
        BlockPos wallCandidate = pos.relative(dir.getOpposite());
        return (replaceables.check(level.getBlockState(pos)) && level.getBlockState(wallCandidate).isFaceSturdy(level, pos, dir));
    }

}
