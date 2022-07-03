package syric.alchemine.outputs.sticky.effects;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import syric.alchemine.outputs.general.alchemicaleffects.AlchemicalEffect;
import syric.alchemine.outputs.general.alchemicaleffects.effectsUtil;
import syric.alchemine.setup.AlchemineBlocks;

public class FoamsnareEffect implements AlchemicalEffect {

    @Override
    public void detonate(UseOnContext context) {
        BlockPos pos = AlchemicalEffect.getOrigin(context);
        Level level = context.getLevel();

        effectsUtil.placeSwellInstantaneous(level, pos, 80, AlchemineBlocks.FOAM_SNARE, effectsUtil.BLOCK_REPLACEABLE);
    }

    @Override
    public String toString() { return "Foamsnare"; }


}
