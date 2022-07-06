package syric.alchemine.outputs.sticky.effects;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import syric.alchemine.outputs.general.alchemicaleffects.AlchemicalEffect;
import syric.alchemine.outputs.general.alchemicaleffects.PlacementSet;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.PlacementPattern;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.SpherePattern;
import syric.alchemine.setup.AlchemineBlocks;

public class WebsnareEffect implements AlchemicalEffect {

    @Override
    public void detonate(UseOnContext context) {
        BlockPos pos = AlchemicalEffect.getOrigin(context);
        Level level = context.getLevel();

        PlacementPattern pattern = new SpherePattern(pos, 1F);
        PlacementPattern pattern2 = new SpherePattern(pos.above(), 1F);
        new PlacementSet(level).addPattern(pattern).addPattern(pattern2).cull(PlacementSet.BLOCK_REPLACEABLE).placeImmediate(AlchemineBlocks.WEB_SNARE, true);

    }

    @Override
    public String toString() { return "Websnare"; }

}
