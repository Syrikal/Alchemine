package syric.alchemine.outputs.fire.effects;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import syric.alchemine.outputs.general.alchemicaleffects.AlchemicalEffect;
import syric.alchemine.outputs.general.alchemicaleffects.PlacementSet;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.PlacementPattern;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.SpherePattern;

public class FireblossomEffect implements AlchemicalEffect{


    @Override
    public void detonate(UseOnContext context) {
        BlockPos pos = AlchemicalEffect.getOrigin(context);
        Level level = context.getLevel();

        PlacementPattern pattern = new SpherePattern(pos, 4F);
        new PlacementSet(level).addPattern(pattern).cull(PlacementSet.AIR_ONLY).cull(PlacementSet.randomFilter(0.4F)).placeImmediate(Blocks.FIRE, false);

        //Damage entities


    }

    @Override
    public String toString() { return "Fireblossom"; }

}
