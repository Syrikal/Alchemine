package syric.alchemine.outputs.general.alchemicaleffects;

import com.mojang.logging.LogUtils;
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
import syric.alchemine.outputs.general.alchemicaleffects.filters.PlacementFilter;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.PlacementPattern;
import syric.alchemine.outputs.general.alchemicaleffects.filters.SimpleFilter;
import syric.alchemine.outputs.slippery.blocks.WallSlideBlock;

import java.util.HashMap;
import java.util.Map;

public class PlacementSet {
    public final Level level;
    public final Map<BlockPos, Double> placementMap;

    public static SimpleFilter AIR_ONLY = (c) -> c.getMaterial().equals(Material.AIR);
    public static SimpleFilter BREAK_ON_PUSH = (c) -> c.getMaterial().getPushReaction() == PushReaction.DESTROY || c.getMaterial().equals(Material.AIR);;
    public static SimpleFilter BLOCK_REPLACEABLE = (c) -> c.getMaterial().isReplaceable();
    public static SimpleFilter STRONG = (c) -> c.getMaterial().isReplaceable() || c.getMaterial().getPushReaction() == PushReaction.DESTROY;
    public static SimpleFilter MAGMA_VEIN = (c) -> c.is(BlockTags.BASE_STONE_NETHER) || c.is(BlockTags.BASE_STONE_OVERWORLD);
    public static SimpleFilter randomFilter(float chance) { RandomSource source = RandomSource.create(); return (c) -> source.nextFloat() < chance; }

    public PlacementSet(Level level) {
        this.level = level;
        placementMap = new HashMap<BlockPos, Double>();
    }

    //Pattern returns a new placement set of blocks in the given pattern.
    public PlacementSet addPattern(PlacementPattern inputPattern) {
        PlacementSet output = new PlacementSet(level);
        Map<BlockPos, Double> patternMap = inputPattern.blockMap();
        for (Map.Entry<BlockPos, Double> entry : patternMap.entrySet()) {
            output.add(entry);
        }
        return output;
    }

    //Cull returns a new PlacementSet culled with the provided filter.
    public PlacementSet cull(PlacementFilter filter) {
        PlacementSet output = new PlacementSet(level);
        placementMap.entrySet().stream().filter(c -> filter.check(level, c.getKey())).forEach(output::add);
        return output;
    }

    //Place will place them. placeImmediate places them all at once.
    public void placeImmediate(BlockState state, boolean drop) {
        for (BlockPos pos : placementMap.keySet()) {
            level.destroyBlock(pos, drop);
            level.setBlockAndUpdate(pos, state);
        }
    }
    public void placeImmediate(Block block, boolean drop) {
        placeImmediate(block.defaultBlockState(), drop);
    }
    public void placeImmediate(RegistryObject<Block> block, boolean drop) {
        placeImmediate(block.get().defaultBlockState(), drop);
    }

    //Places a contextual wall block.
    public void placeContextualWall(DirectionalStateGetter stateProvider, boolean drop, Direction initialDirection) {
        for (BlockPos pos : placementMap.keySet()) {
            level.destroyBlock(pos, drop);
            BlockState state = stateProvider.getState(level, pos, initialDirection);
            level.setBlockAndUpdate(pos, state);
        }
    }

    //Something that will sort them into a map for sequential placement? an entity would need to do that sequential placement. Do that later.
    //placeOrdered creates an entity that will place the rest, passing in the map?


    public void add(BlockPos pos) {
        placementMap.putIfAbsent(pos, 1D);
    }
    public void add(Map.Entry<BlockPos, Double> input) {
        Double prev = placementMap.putIfAbsent(input.getKey(), input.getValue());
        LogUtils.getLogger().info((prev == null) ? "Added previously-unknown position, (" + input.getKey().toShortString() + "), to placement map." : "Position (\" + input.getKey().toShortString() + \") is already in placement map.");
    }
    public void add(BlockPos pos, Double val) {
        placementMap.putIfAbsent(pos, val);
    }

    public int blockCount() {
        return placementMap.keySet().size();
    }

}
