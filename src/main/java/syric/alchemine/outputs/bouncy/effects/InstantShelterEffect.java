package syric.alchemine.outputs.bouncy.effects;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import syric.alchemine.outputs.general.alchemicaleffects.AlchemicalEffect;
import syric.alchemine.outputs.general.alchemicaleffects.PlacementSet;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.PlacementPattern;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.BubblePattern;
import syric.alchemine.setup.AlchemineBlocks;

public class InstantShelterEffect implements AlchemicalEffect {

    public InstantShelterEffect(){};

    public void detonate(UseOnContext context) {
        BlockPos pos = AlchemicalEffect.getOrigin(context);
        Level level = context.getLevel();

        PlacementPattern pattern = new BubblePattern(pos, 1.75F, 2.65F);
        new PlacementSet(level).addPattern(pattern).cull(PlacementSet.BLOCK_REPLACEABLE).placeImmediate(AlchemineBlocks.SHELL_SLIME, false);

    }

    @Override
    public String toString() { return "Instant Shelter"; }

}
