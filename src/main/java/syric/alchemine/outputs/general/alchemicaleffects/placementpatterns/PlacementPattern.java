package syric.alchemine.outputs.general.alchemicaleffects.placementpatterns;

import net.minecraft.core.BlockPos;

import java.util.stream.Stream;

public interface PlacementPattern {

    public Stream<BlockPos> blockList();

}
