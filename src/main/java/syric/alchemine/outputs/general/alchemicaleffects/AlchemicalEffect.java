package syric.alchemine.outputs.general.alchemicaleffects;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.UseOnContext;

public interface AlchemicalEffect {

    void detonate(UseOnContext context);

    static BlockPos getOrigin(UseOnContext context) {
        Direction direction = context.getClickedFace();
        BlockPos blockpos = context.getClickedPos();
        return blockpos.relative(direction);
    }

}
