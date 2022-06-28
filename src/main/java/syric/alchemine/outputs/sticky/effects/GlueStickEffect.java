package syric.alchemine.outputs.sticky.effects;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import syric.alchemine.outputs.general.effects.AlchemicalEffect;
import syric.alchemine.outputs.general.effects.effectsUtil;
import syric.alchemine.outputs.general.effects.placementpatterns.FlatDiscPattern;
import syric.alchemine.outputs.general.effects.placementpatterns.PlacementPattern;
import syric.alchemine.setup.AlchemineBlocks;

public class GlueStickEffect implements AlchemicalEffect {

    @Override
    public void detonate(UseOnContext context) {
        BlockPos pos = AlchemicalEffect.getOrigin(context);
        Level level = context.getLevel();

        PlacementPattern place = new FlatDiscPattern(pos, 1.5F);

        effectsUtil.placeAbsolute(level, place, AlchemineBlocks.GLUE_STICK, effectsUtil.BLOCK_REPLACEABLE);
    }


}