package syric.alchemine.outputs.general.alchemicaleffects.filters;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface PlacementFilter {
    boolean check(Level level, BlockPos pos);

}