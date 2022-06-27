package syric.alchemine.outputs.bouncy.effects;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import syric.alchemine.outputs.general.effects.AlchemicalEffect;
import syric.alchemine.outputs.general.effects.effectsUtil;
import syric.alchemine.outputs.general.effects.placementpatterns.PlacementPattern;
import syric.alchemine.outputs.general.effects.placementpatterns.BubblePattern;
import syric.alchemine.setup.AlchemineBlocks;

public class InstantShelterEffect implements AlchemicalEffect {

    public InstantShelterEffect(){};

    public void detonate(UseOnContext context) {
        BlockPos pos = AlchemicalEffect.getOrigin(context);
        Level level = context.getLevel();

        PlacementPattern pattern = new BubblePattern(pos, 1.75F, 2.65F);

        effectsUtil.placeAbsolute(level, pattern, AlchemineBlocks.SHELL_SLIME, effectsUtil.BLOCK_REPLACEABLE);

    }


}
