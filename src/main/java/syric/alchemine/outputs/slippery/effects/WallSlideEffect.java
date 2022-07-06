package syric.alchemine.outputs.slippery.effects;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import syric.alchemine.outputs.general.alchemicaleffects.AlchemicalEffect;
import syric.alchemine.outputs.general.alchemicaleffects.PlacementSet;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.PlacementPattern;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.SpherePattern;
import syric.alchemine.outputs.slippery.blocks.WallSlideBlock;

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
        new PlacementSet(level).addPattern(place).cull(PlacementSet.AIR_ONLY).placeContextualWall(WallSlideBlock::getEffectState, false, direction);
//        BlockState state1 = AlchemineBlocks.WALL_SLIDE.get().defaultBlockState().setValue(HORIZONTAL_FACING, direction);
//        BlockState state2 = AlchemineBlocks.WALL_SLIDE.get().defaultBlockState().setValue(HORIZONTAL_FACING, direction.getOpposite());
//        BlockState state3 = AlchemineBlocks.WALL_SLIDE.get().defaultBlockState().setValue(HORIZONTAL_FACING, direction.getClockWise());
//        BlockState state4 = AlchemineBlocks.WALL_SLIDE.get().defaultBlockState().setValue(HORIZONTAL_FACING, direction.getCounterClockWise());
//
//        WallPlace(level, place, state1, PlacementSet.AIR_ONLY, direction);
//        WallPlace(level, place, state2, PlacementSet.AIR_ONLY, direction.getOpposite());
//        WallPlace(level, place, state3, PlacementSet.AIR_ONLY, direction.getClockWise());
//        WallPlace(level, place, state4, PlacementSet.AIR_ONLY, direction.getCounterClockWise());
    }

//    private static void placeDirectionalBlock(PlacementSet set, boolean drop, Direction initialDirection) {
//        for (BlockPos pos : set.placementMap.keySet()) {
//            set.level.destroyBlock(pos, drop);
//            BlockState state = WallSlideBlock.getEffectState(set.level, pos, initialDirection);
//            set.level.setBlockAndUpdate(pos, state);
//        }
//    }

//    public static void WallPlace(Level level, PlacementPattern pattern, BlockState state, PlacementFilter replaceables, Direction direction) {
//        pattern.blockMap().entrySet().stream().filter(c -> checkWallPlaceable(level, c.getKey(), direction, replaceables))
//                .forEach(c -> {
//                    level.destroyBlock(c.getKey(), true);
//                    level.setBlockAndUpdate(c.getKey(), state);
//                });
//    }
//
//    private static boolean checkWallPlaceable(Level level, BlockPos pos, Direction dir, PlacementFilter replaceables) {
//        BlockPos wallCandidate = pos.relative(dir.getOpposite());
//        return (replaceables.check(level, pos) && level.getBlockState(wallCandidate).isFaceSturdy(level, pos, dir));
//    }

    @Override
    public String toString() { return "Wall Slide"; }

}
