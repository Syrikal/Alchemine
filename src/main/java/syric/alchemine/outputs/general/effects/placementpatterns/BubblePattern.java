package syric.alchemine.outputs.general.effects.placementpatterns;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;

import java.util.stream.Stream;

public class BubblePattern implements PlacementPattern{
    private final float inRad;
    private final float outRad;
    private final BlockPos origin;

    public BubblePattern(BlockPos origin, float rad1, float rad2) {
        this.origin = origin;
        this.inRad = Math.min(rad1, rad2);
        this.outRad = Math.max(rad1, rad2);
    }

    @Override
    public Stream<BlockPos> blockList() {
        int variation = (int) Math.ceil(outRad);
        BlockPos pos1 = new BlockPos(origin.getX()-variation, origin.getY()-variation,origin.getZ()-variation);
        BlockPos pos2 = new BlockPos(origin.getX()+variation, origin.getY()+variation, origin.getZ()+variation);
        return BlockPos.betweenClosedStream(pos1, pos2).filter(c -> distance(c) <= outRad).filter(c -> distance(c) >= inRad);
    }

    private float distance(BlockPos pos) {
        double xdist = Math.pow(origin.getX() - pos.getX(), 2);
        double zdist = Math.pow(origin.getZ() - pos.getZ(), 2);
        double ydist = Math.pow(origin.getY() - pos.getY(), 2);
        return (float) Math.sqrt(xdist + zdist + ydist);
    }

}
