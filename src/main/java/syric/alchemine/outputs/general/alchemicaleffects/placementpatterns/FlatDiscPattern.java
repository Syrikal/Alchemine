package syric.alchemine.outputs.general.alchemicaleffects.placementpatterns;

import net.minecraft.core.BlockPos;

import java.util.*;
import java.util.stream.Stream;

public class FlatDiscPattern implements PlacementPattern{
    private final float radius;
    private final BlockPos origin;

    public FlatDiscPattern(BlockPos origin, float radius) {
        this.origin = origin;
        this.radius = radius;
    }

    @Override
    public Map<BlockPos, Double> blockMap() {
        int variation = (int) Math.ceil(radius);
        BlockPos pos1 = new BlockPos(origin.getX()-variation, origin.getY(),origin.getZ()-variation);
        BlockPos pos2 = new BlockPos(origin.getX()+variation, origin.getY(), origin.getZ()+variation);
        HashMap<BlockPos, Double> output = new HashMap<BlockPos, Double>();
        BlockPos.betweenClosedStream(pos1, pos2).filter(c -> distance(c) <= radius).forEach(c -> output.put(c.immutable(), (double) distance(c)));
        return output;
    }

    private float distance(BlockPos pos) {
        double xdist = Math.pow(origin.getX() - pos.getX(), 2);
        double zdist = Math.pow(origin.getZ() - pos.getZ(), 2);
        return (float) Math.sqrt(xdist + zdist);
    }

}
