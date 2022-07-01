package syric.alchemine.outputs.general.alchemicaleffects.placementpatterns;

import net.minecraft.world.level.block.state.BlockState;

public interface ReplaceablesFilter {
    public boolean check(BlockState state);
}
