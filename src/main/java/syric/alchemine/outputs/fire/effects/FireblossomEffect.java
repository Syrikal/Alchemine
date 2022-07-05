package syric.alchemine.outputs.fire.effects;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import syric.alchemine.outputs.general.alchemicaleffects.AlchemicalEffect;
import syric.alchemine.outputs.general.alchemicaleffects.effectsUtil;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.PlacementPattern;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.SparseSpherePattern;
import syric.alchemine.setup.AlchemineBlocks;

public class FireblossomEffect implements AlchemicalEffect{


    @Override
    public void detonate(UseOnContext context) {
        BlockPos pos = AlchemicalEffect.getOrigin(context);
        Level level = context.getLevel();

        PlacementPattern pattern = new SparseSpherePattern(pos, 4F, 0.1);
        effectsUtil.placeAbsolute(level, pattern, Blocks.FIRE, effectsUtil.AIR_ONLY);

        //Damage entities


    }

    @Override
    public String toString() { return "Fireblossom"; }

}
