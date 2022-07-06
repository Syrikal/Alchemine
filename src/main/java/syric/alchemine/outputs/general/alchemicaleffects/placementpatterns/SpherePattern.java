package syric.alchemine.outputs.general.alchemicaleffects.placementpatterns;

import com.ibm.icu.impl.coll.Collation;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpherePattern implements PlacementPattern{
    private final float radius;
    private final BlockPos origin;

    public SpherePattern(BlockPos origin, float radius) {
        this.origin = origin;
        this.radius = radius;
    }

    @Override
    public Map<BlockPos, Double> blockMap() {
        //Create a box
        int variation = (int) Math.ceil(radius);
        BlockPos pos1 = new BlockPos(origin.getX()-variation, origin.getY()-variation,origin.getZ()-variation);
        BlockPos pos2 = new BlockPos(origin.getX()+variation, origin.getY()+variation, origin.getZ()+variation);

        //Put all blocks within that box into a map if they pass a filter
        //Create a map to collect outputs in
        HashMap<BlockPos, Double> output = new HashMap<BlockPos, Double>();
        //Create a stream of blocks
        BlockPos.betweenClosedStream(pos1, pos2)
                //Filter out ones that don't match our criteria
                .filter(c -> distance(c) <= radius)

                //Put the remaining ones into a map:
                .forEach(c -> {
                    //Put the block into the map
                    output.put(c.immutable(), (double) distance(c));

                    //Log that you've done this. This indicates that many different elements are being placed into the map.
//                    LogUtils.getLogger().info("Placing " + c.toShortString() + " into pattern map");
//                    logSphereMap(output);
                });

        //After the loop, do the *exact same* printing of the output. This suddenly shows that the output
        //is full of many copies of the *same* block, which shouldn't even have passed the filter.
        //It's either the first or last block to have been passed into the stream, not sure.
//        logSphereMap(output);

        //Return
        return output;
    }

    private float distance(BlockPos pos) {
        double xdist = Math.pow(origin.getX() - pos.getX(), 2);
        double zdist = Math.pow(origin.getZ() - pos.getZ(), 2);
        double ydist = Math.pow(origin.getY() - pos.getY(), 2);
        return (float) Math.sqrt(xdist + zdist + ydist);
    }

    private void logSphereMap(Map<BlockPos, Double> map) {
        LogUtils.getLogger().info("Pattern map has " + map.entrySet().size() + " elements");
        StringBuilder sb1 = new StringBuilder("Sphere pattern blocks: ");
        for (BlockPos pos : map.keySet()) {
            sb1.append("(").append(pos.toShortString()).append("), ");
        }
        LogUtils.getLogger().info(sb1.toString());
    }

}
