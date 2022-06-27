package syric.alchemine.outputs.general.effects.placementpatterns;

import net.minecraft.core.BlockPos;

import java.util.stream.Stream;

public class SpherePattern implements PlacementPattern{
    private final float radius;
    private final BlockPos origin;

    public SpherePattern(BlockPos origin, float radius) {
        this.origin = origin;
        this.radius = radius;
    }

    @Override
    public Stream<BlockPos> blockList() {
        int variation = (int) Math.ceil(radius);
        BlockPos pos1 = new BlockPos(origin.getX()-variation, origin.getY()-variation,origin.getZ()-variation);
        BlockPos pos2 = new BlockPos(origin.getX()+variation, origin.getY()+variation, origin.getZ()+variation);
        return BlockPos.betweenClosedStream(pos1, pos2).filter(c -> distance(c) <= radius);
    }

    private float distance(BlockPos pos) {
        double xdist = Math.pow(origin.getX() - pos.getX(), 2);
        double zdist = Math.pow(origin.getZ() - pos.getZ(), 2);
        double ydist = Math.pow(origin.getY() - pos.getY(), 2);
        return (float) Math.sqrt(xdist + zdist + ydist);
    }

}
