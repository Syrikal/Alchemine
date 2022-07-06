package syric.alchemine.outputs.general.alchemicaleffects;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import syric.alchemine.outputs.general.alchemicaleffects.filters.SimpleFilter;

import java.util.Collection;

import static syric.alchemine.util.ChatPrint.chatPrint;

public class effectsUtil {
    public static SimpleFilter AIR_ONLY = (c) -> c.getMaterial().equals(Material.AIR);
    public static SimpleFilter BREAK_ON_PUSH = (c) -> c.getMaterial().getPushReaction() == PushReaction.DESTROY || c.getMaterial().equals(Material.AIR);;
    public static SimpleFilter BLOCK_REPLACEABLE = (c) -> c.getMaterial().isReplaceable();
    public static SimpleFilter STRONG = (c) -> c.getMaterial().isReplaceable() || c.getMaterial().getPushReaction() == PushReaction.DESTROY;
    public static SimpleFilter MAGMA_VEIN = (c) -> c.is(BlockTags.BASE_STONE_NETHER) || c.is(BlockTags.BASE_STONE_OVERWORLD);


    //Pattern produces a set of blocks.
    //Cull filters them. You cull a set of blocks and get a set of blocks.
    //place will place them.
    //Something that will sort them into a map for sequential placement? an entity would need to do that sequential placement. Do that later.


    public static void place(Level level, BlockState state, Collection<BlockPos> placement, boolean drop) {
        for (BlockPos pos : placement) {
            level.destroyBlock(pos, drop);
            level.setBlockAndUpdate(pos, state);
        }
    }

    //A function like place but which can alter block states?


    //Swells out from an origin point. Places all blocks at once.
    //Non-instantaneous version: place by distance from origin? Wouldn't work in some situations (spreading over walls would look weird)
    //Maybe make a separate map of distances and add blocks to them when they join the first map, with values based on the block that spread to them.
//    public static void placeSwellInstantaneous(Level level, BlockPos origin, int numberBlocks, Block block, PlacementFilter replaceables) {
//        ConcurrentHashMap<BlockPos, Integer> blocks = new ConcurrentHashMap<BlockPos, Integer>();
//        blocks.put(origin, numberBlocks);
//        int maxCyclesCountdown = numberBlocks * 2;
//        boolean success = false;
//        //Iterate until done spreading (success) OR run out of time (maxCyclesCountdown)
//        while (!success && maxCyclesCountdown > 0) {
//            //Each iteration, assume you're done until you find somewhere you aren't
//            boolean tentativeSuccess = true;
//            //For every block currently in our list:
//            for (Map.Entry<BlockPos, Integer> entry : blocks.entrySet()) {
//                //The block itself
//                BlockPos pos = entry.getKey();
//                //If it has stuff inside to spread:
//                if (entry.getValue() > 1) {
//                    //We're not done
//                    tentativeSuccess = false;
//                    //For each direction, randomly:
//                    for (Direction direction : Direction.allShuffled(RandomSource.create())) {
//                        //Get a candidate adjacent block
//                        BlockPos candidate = pos.relative(direction);
//                        //If it can be spread to, send a point that way. This includes blocks already listed which are less full than itself.
//                        if (replaceables.check(level.getBlockState(candidate))) {
//                            //If it's already in the map, only spread if it's less full than the one doing the spreading!
//                            if (blocks.containsKey(candidate)) {
//                                if (blocks.get(candidate) < blocks.get(pos)) {
//                                    blocks.put(candidate, blocks.get(candidate) + 1);
//                                    blocks.put(pos, blocks.get(pos) - 1);
//                                }
//                            //If it's not in the map yet, spread one point to it.
//                            } else {
//                                blocks.put(candidate, 1);
//                                blocks.put(pos, blocks.get(pos) - 1);
//                            }
//                        }
//                        //If it's not replaceable, don't try to spread there
//                    }
//                    //Once you're done iterating over directions from this position, move on to the next block in the map
//                }
//                //If there's nothing to spread from this location, don't bother and move on to the next block in the map
//            }
//
//            //Once finished iterating over current blocks:
//            //Check to see if we're done, i.e. there was no spreading to do
//            success = tentativeSuccess;
//        }
//
//        //THE LOOP IS OVER! The map should now be full of block positions with 1 block of output in each.
//
//        for (BlockPos pos : blocks.keySet()) {
//            if (blocks.get(pos) > 1) {
//                chatPrint("Placing a swelled block with " + blocks.get(pos) + " blocks inside it!", level);
//            }
//
//            //Now clear out all the blocks (which should only be valid replaceable ones)
//            level.destroyBlock(pos, true);
//            //And fill with the new material
//            level.setBlockAndUpdate(pos, block.defaultBlockState());
//
//        }
//    }
//
//
//    public static void placeSwellInstantaneous(Level level, BlockPos origin, int numberBlocks, RegistryObject<Block> blockReg, PlacementFilter replaceables) {
//        placeSwellInstantaneous(level, origin, numberBlocks, blockReg.get(), replaceables);
//    }
//
//
//    public static List<BlockPos> cull(List<BlockPos> input, Level level, PlacementFilter filter) {
//        List<BlockPos> output = new ArrayList<>();
//        input.stream().filter(c -> filter.check(level, c)).forEach(output::add);
//        return output;
//    }


}