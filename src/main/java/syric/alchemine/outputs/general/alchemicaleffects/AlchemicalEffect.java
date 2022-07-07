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

    //Detonation types:
    //Direct by player, midair
    //Direct by player, on block
    //Direct by player, on entity
    //Thrown, detonate midair
    //Thrown, detonate on entity
    //Thrown, detonate on block
    //From block: subtypes- always midair? depending on direction?

    //Important information:
    //Level, player, direction, block hit, entity hit.
    //Type of capsule or flask or whatever used, just in case.


    //Allow transferral of effects. an interface for effectHoldingItem?

}
