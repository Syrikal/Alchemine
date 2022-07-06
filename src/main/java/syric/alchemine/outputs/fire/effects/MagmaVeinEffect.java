package syric.alchemine.outputs.fire.effects;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import syric.alchemine.outputs.general.alchemicaleffects.AlchemicalEffect;
import syric.alchemine.outputs.general.alchemicaleffects.PlacementSet;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.PlacementPattern;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.SpreadPattern;

public class MagmaVeinEffect implements AlchemicalEffect {

    @Override
    public void detonate(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();

        PlacementPattern pattern = new SpreadPattern(level, pos, 40, PlacementSet.MAGMA_VEIN);
        new PlacementSet(level).addPattern(pattern).cull(PlacementSet.MAGMA_VEIN).placeImmediate(Blocks.MAGMA_BLOCK, false);

    }

    @Override
    public String toString() { return "Magma Vein"; }

}
