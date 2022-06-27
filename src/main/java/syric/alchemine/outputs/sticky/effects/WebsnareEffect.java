package syric.alchemine.outputs.sticky.effects;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import syric.alchemine.outputs.general.effects.AlchemicalEffect;
import syric.alchemine.outputs.general.effects.effectsUtil;
import syric.alchemine.outputs.general.effects.placementpatterns.PlacementPattern;
import syric.alchemine.outputs.general.effects.placementpatterns.SpherePattern;
import syric.alchemine.setup.AlchemineBlocks;

public class WebsnareEffect implements AlchemicalEffect {

    @Override
    public void detonate(UseOnContext context) {
        BlockPos pos = AlchemicalEffect.getOrigin(context);
        Level level = context.getLevel();

        PlacementPattern pattern = new SpherePattern(pos, 1F);
        PlacementPattern pattern2 = new SpherePattern(pos.above(), 1F);
        effectsUtil.placeAbsolute(level, pattern, AlchemineBlocks.WEB_SNARE, effectsUtil.BLOCK_REPLACEABLE);
        effectsUtil.placeAbsolute(level, pattern2, AlchemineBlocks.WEB_SNARE, effectsUtil.BLOCK_REPLACEABLE);

    }

}
