package syric.alchemine.outputs.sticky.effects;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import syric.alchemine.outputs.general.alchemicaleffects.AlchemicalEffect;
import syric.alchemine.outputs.general.alchemicaleffects.PlacementSet;
import syric.alchemine.outputs.general.alchemicaleffects.effectsUtil;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.PlacementPattern;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.SpreadPattern;
import syric.alchemine.setup.AlchemineBlocks;

public class FoamsnareEffect implements AlchemicalEffect {

    @Override
    public void detonate(UseOnContext context) {
        BlockPos pos = AlchemicalEffect.getOrigin(context);
        Level level = context.getLevel();

        PlacementPattern pattern = new SpreadPattern(level, pos, 80, effectsUtil.BLOCK_REPLACEABLE);
        new PlacementSet(level).addPattern(pattern).cull(effectsUtil.BLOCK_REPLACEABLE).placeImmediate(AlchemineBlocks.FOAM_SNARE, true);

    }

    @Override
    public String toString() { return "Foamsnare"; }


}
