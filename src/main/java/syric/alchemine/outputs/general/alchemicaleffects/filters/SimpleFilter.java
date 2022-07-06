package syric.alchemine.outputs.general.alchemicaleffects.filters;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface SimpleFilter extends PlacementFilter {

    boolean check(BlockState state);

    default boolean check(Level level, BlockPos pos) {
        return check(level.getBlockState(pos));
    }

}
