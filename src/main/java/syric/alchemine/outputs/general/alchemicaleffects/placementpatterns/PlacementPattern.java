package syric.alchemine.outputs.general.alchemicaleffects.placementpatterns;

import net.minecraft.core.BlockPos;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

public interface PlacementPattern {

    public Map<BlockPos, Double> blockMap();

}
