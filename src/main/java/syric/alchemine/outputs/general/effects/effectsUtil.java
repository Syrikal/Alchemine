package syric.alchemine.outputs.general.effects;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.RegistryObject;
import syric.alchemine.outputs.general.effects.placementpatterns.ReplaceablesFilter;
import syric.alchemine.outputs.general.effects.placementpatterns.PlacementPattern;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static syric.alchemine.util.ChatPrint.chatPrint;

public class effectsUtil {
    public static ReplaceablesFilter BREAK_ON_PUSH = (state) -> state.getMaterial().getPushReaction() == PushReaction.DESTROY || state.getMaterial().equals(Material.AIR);;
    public static ReplaceablesFilter BLOCK_REPLACEABLE = (state) -> state.getMaterial().isReplaceable();
    public static ReplaceablesFilter STRONG = (state) -> state.getMaterial().isReplaceable() || state.getMaterial().getPushReaction() == PushReaction.DESTROY;
    public static ReplaceablesFilter MAGMA_VEIN = (state) -> state.is(BlockTags.BASE_STONE_NETHER) || state.is(BlockTags.BASE_STONE_OVERWORLD);
    //One that doesn't work underwater?

    public static void placeAbsolute(Level level, PlacementPattern pattern, Block block, ReplaceablesFilter replaceables) {
        pattern.blockList().filter(c -> replaceables.check(level.getBlockState(c)))
                .forEach(c -> {
                    level.destroyBlock(c, true);
                    level.setBlockAndUpdate(c, block.defaultBlockState());
                });
    }

    public static void placeAbsolute(Level level, PlacementPattern pattern, BlockState state, ReplaceablesFilter replaceables) {
        pattern.blockList().filter(c -> replaceables.check(level.getBlockState(c)))
                .forEach(c -> {
                    level.destroyBlock(c, true);
                    level.setBlockAndUpdate(c, state);
                });

    }

    public static void placeAbsolute(Level level, PlacementPattern pattern, RegistryObject<Block> blockReg, ReplaceablesFilter replaceables) {
        placeAbsolute(level, pattern, blockReg.get(), replaceables);
    }

    //Swells out from an origin point. Places all blocks at once.
    //Non-instantaneous version: place by distance from origin? Wouldn't work in some situations (spreading over walls would look weird)
    //Maybe make a separate map of distances and add blocks to them when they join the first map, with values based on the block that spread to them.
    public static void placeSwellInstantaneous(Level level, BlockPos origin, int numberBlocks, Block block, ReplaceablesFilter replaceables) {
        ConcurrentHashMap<BlockPos, Integer> blocks = new ConcurrentHashMap<BlockPos, Integer>();
        blocks.put(origin, numberBlocks);
        int maxCyclesCountdown = numberBlocks * 2;
        boolean success = false;
        //Iterate until done spreading (success) OR run out of time (maxCyclesCountdown)
        while (!success && maxCyclesCountdown > 0) {
            //Each iteration, assume you're done until you find somewhere you aren't
            boolean tentativeSuccess = true;
            //For every block currently in our list:
            for (Map.Entry<BlockPos, Integer> entry : blocks.entrySet()) {
                //The block itself
                BlockPos pos = entry.getKey();
                //If it has stuff inside to spread:
                if (entry.getValue() > 1) {
                    //We're not done
                    tentativeSuccess = false;
                    //For each direction, randomly:
                    for (Direction direction : Direction.allShuffled(RandomSource.create())) {
                        //Get a candidate adjacent block
                        BlockPos candidate = pos.relative(direction);
                        //If it can be spread to, send a point that way. This includes blocks already listed which are less full than itself.
                        if (replaceables.check(level.getBlockState(candidate))) {
                            //If it's already in the map, only spread if it's less full than the one doing the spreading!
                            if (blocks.containsKey(candidate)) {
                                if (blocks.get(candidate) < blocks.get(pos)) {
                                    blocks.put(candidate, blocks.get(candidate) + 1);
                                    blocks.put(pos, blocks.get(pos) - 1);
                                }
                            //If it's not in the map yet, spread one point to it.
                            } else {
                                blocks.put(candidate, 1);
                                blocks.put(pos, blocks.get(pos) - 1);
                            }
                        }
                        //If it's not replaceable, don't try to spread there
                    }
                    //Once you're done iterating over directions from this position, move on to the next block in the map
                }
                //If there's nothing to spread from this location, don't bother and move on to the next block in the map
            }

            //Once finished iterating over current blocks:
            //Check to see if we're done, i.e. there was no spreading to do
            success = tentativeSuccess;
        }

        //THE LOOP IS OVER! The map should now be full of block positions with 1 block of output in each.

        for (BlockPos pos : blocks.keySet()) {
            if (blocks.get(pos) > 1) {
                chatPrint("Placing a swelled block with " + blocks.get(pos) + " blocks inside it!", level);
            }

            //Now clear out all the blocks (which should only be valid replaceable ones)
            level.destroyBlock(pos, true);
            //And fill with the new material
            level.setBlockAndUpdate(pos, block.defaultBlockState());

        }
    }


    public static void placeSwellInstantaneous(Level level, BlockPos origin, int numberBlocks, RegistryObject<Block> blockReg, ReplaceablesFilter replaceables) {
        placeSwellInstantaneous(level, origin, numberBlocks, blockReg.get(), replaceables);
    }


}