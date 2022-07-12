package syric.alchemine.outputs.fire.effects;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import syric.alchemine.outputs.general.alchemicaleffects.AlchemicalEffect;
import syric.alchemine.outputs.general.alchemicaleffects.PlacementSet;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.PlacementPattern;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.SpreadPattern;
import syric.alchemine.setup.AlchemineBlocks;

public class SmokeCloudEffect implements AlchemicalEffect {

    @Override
    public void detonate(UseOnContext context) {
        BlockPos pos = AlchemicalEffect.getOrigin(context);
        Level level = context.getLevel();

        //Play a sound?

        //Spread pattern should be flatter

        PlacementPattern pattern = new SpreadPattern(level, pos, 80, PlacementSet.AIR_ONLY);
        new PlacementSet(level).addPattern(pattern).cull(PlacementSet.AIR_ONLY).placeImmediate(AlchemineBlocks.SMOKE_CLOUD, true);

        //Turn the player invisible if they're in that space


    }

    @Override
    public String toString() { return "Smoke Cloud"; }


}
