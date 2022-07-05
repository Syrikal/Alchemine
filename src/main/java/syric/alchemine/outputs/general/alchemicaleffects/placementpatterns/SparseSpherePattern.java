package syric.alchemine.outputs.general.alchemicaleffects.placementpatterns;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;

import java.util.stream.Stream;

public class SparseSpherePattern implements PlacementPattern{
    private final float radius;
    private final BlockPos origin;
    private final double placementChance;

    public SparseSpherePattern(BlockPos origin, float radius, double placementChance) {
        this.origin = origin;
        this.radius = radius;
        this.placementChance = placementChance;
    }

    @Override
    public Stream<BlockPos> blockList() {
        int variation = (int) Math.ceil(radius);
        BlockPos pos1 = new BlockPos(origin.getX()-variation, origin.getY()-variation,origin.getZ()-variation);
        BlockPos pos2 = new BlockPos(origin.getX()+variation, origin.getY()+variation, origin.getZ()+variation);
        RandomSource randomSource = RandomSource.create();
        return BlockPos.betweenClosedStream(pos1, pos2).filter(c -> distance(c) <= radius).filter(c -> randomSource.nextDouble() < placementChance);
    }

    private float distance(BlockPos pos) {
        double xdist = Math.pow(origin.getX() - pos.getX(), 2);
        double zdist = Math.pow(origin.getZ() - pos.getZ(), 2);
        double ydist = Math.pow(origin.getY() - pos.getY(), 2);
        return (float) Math.sqrt(xdist + zdist + ydist);
    }

}
