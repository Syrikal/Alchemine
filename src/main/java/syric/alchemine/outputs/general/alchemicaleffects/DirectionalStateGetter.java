package syric.alchemine.outputs.general.alchemicaleffects;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

//When I make effectContext, this can be a single contextualStateGetter interface.
public interface DirectionalStateGetter {
    BlockState getState(Level level, BlockPos pos, Direction initialDirection);
}