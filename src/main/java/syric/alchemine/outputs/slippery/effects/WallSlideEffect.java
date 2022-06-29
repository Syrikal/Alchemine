package syric.alchemine.outputs.slippery.effects;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import syric.alchemine.outputs.general.effects.AlchemicalEffect;
import syric.alchemine.outputs.general.effects.effectsUtil;
import syric.alchemine.outputs.general.effects.placementpatterns.FlatDiscPattern;
import syric.alchemine.outputs.general.effects.placementpatterns.PlacementPattern;
import syric.alchemine.outputs.general.effects.placementpatterns.SpherePattern;
import syric.alchemine.setup.AlchemineBlocks;

import static net.minecraft.world.level.block.DirectionalBlock.FACING;

public class WallSlideEffect implements AlchemicalEffect {

    public WallSlideEffect(){};

    public void detonate(UseOnContext context) {
        BlockPos pos = AlchemicalEffect.getOrigin(context);
        Level level = context.getLevel();
        Direction direction = context.getClickedFace();

        PlacementPattern place = new SpherePattern(pos, 4F);

        effectsUtil.placeAbsolute(level, place, AlchemineBlocks.WALL_SLIDE.get().defaultBlockState().setValue(FACING, direction), effectsUtil.BLOCK_REPLACEABLE);

    }

}
