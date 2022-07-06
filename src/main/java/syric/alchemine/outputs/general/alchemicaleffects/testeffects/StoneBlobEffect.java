package syric.alchemine.outputs.general.alchemicaleffects.testeffects;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import syric.alchemine.outputs.general.alchemicaleffects.AlchemicalEffect;
import syric.alchemine.outputs.general.alchemicaleffects.PlacementSet;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.PlacementPattern;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.SpreadPattern;

public class StoneBlobEffect implements AlchemicalEffect {

    @Override
    public void detonate(UseOnContext context) {
        BlockPos pos = AlchemicalEffect.getOrigin(context);
        Level level = context.getLevel();


        PlacementPattern pattern = new SpreadPattern(level, pos, 60, PlacementSet.BREAK_ON_PUSH);
        new PlacementSet(level).addPattern(pattern).cull(PlacementSet.BREAK_ON_PUSH).placeImmediate(Blocks.STONE.defaultBlockState(), true);

    }

    @Override
    public String toString() { return "Stone Blob"; }

}
