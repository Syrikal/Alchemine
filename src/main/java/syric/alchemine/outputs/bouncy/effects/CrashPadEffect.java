package syric.alchemine.outputs.bouncy.effects;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import syric.alchemine.outputs.general.effects.AlchemicalEffect;
import syric.alchemine.outputs.general.effects.effectsUtil;
import syric.alchemine.outputs.general.effects.placementpatterns.FlatDiscPattern;
import syric.alchemine.outputs.general.effects.placementpatterns.PlacementPattern;
import syric.alchemine.setup.AlchemineBlocks;

public class CrashPadEffect implements AlchemicalEffect {

    public CrashPadEffect(){};

    public void detonate(UseOnContext context) {
        BlockPos pos = AlchemicalEffect.getOrigin(context);
        Level level = context.getLevel();

        PlacementPattern place = new FlatDiscPattern(pos, 2.5F);

        effectsUtil.placeAbsolute(level, place, AlchemineBlocks.CRASH_PAD, effectsUtil.BLOCK_REPLACEABLE);

    }

}