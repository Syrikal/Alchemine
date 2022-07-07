package syric.alchemine.outputs.general.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public interface PossiblyPermanentBlock {
    BooleanProperty PERMANENT = BooleanProperty.create("permanent");

    default void togglePermanent(Level level, BlockPos pos, BlockState state) {
        if (!state.hasProperty(PERMANENT)) {
            return;
        }
        if (state.getValue(PERMANENT)) {
            BlockState newState = state.setValue(PERMANENT, false);
            level.setBlockAndUpdate(pos, newState);
        } else {
            BlockState newState = state.setValue(PERMANENT, true);
            level.setBlockAndUpdate(pos, newState);
            level.scheduleTick(pos, newState.getBlock(), getDuration());
        }
    }

    default int getDuration() {
        return 1200;
    }
}
