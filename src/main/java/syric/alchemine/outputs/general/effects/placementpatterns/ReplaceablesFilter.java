package syric.alchemine.outputs.general.effects.placementpatterns;

import net.minecraft.world.level.block.state.BlockState;

public interface ReplaceablesFilter {
    public boolean check(BlockState state);
}
