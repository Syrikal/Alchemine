package syric.alchemine.outputs.fire.effects;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import syric.alchemine.outputs.general.alchemicaleffects.AlchemicalEffect;
import syric.alchemine.outputs.general.alchemicaleffects.effectsUtil;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.PlacementPattern;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.SparseSpherePattern;
import syric.alchemine.setup.AlchemineBlocks;

public class StonefireEffect implements AlchemicalEffect{

    @Override
    public void detonate(UseOnContext context) {
        BlockPos pos = AlchemicalEffect.getOrigin(context);
        Level level = context.getLevel();

        PlacementPattern pattern = new SparseSpherePattern(pos, 4F, 0.05);
        effectsUtil.placeAbsolute(level, pattern, AlchemineBlocks.STONE_FIRE, effectsUtil.AIR_ONLY);

    }

    @Override
    public String toString() { return "Stonefire"; }

}
