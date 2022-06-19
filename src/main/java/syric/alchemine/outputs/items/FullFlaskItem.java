package syric.alchemine.outputs.items;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import syric.alchemine.outputs.effects.AlchemicalEffect;
import syric.alchemine.outputs.effects.registerEffects;
import syric.alchemine.setup.alchemineItems;

public class FullFlaskItem extends Item {

public FullFlaskItem(Properties properties) {super(properties);}

    AlchemicalEffect storedEffect = registerEffects.CRASH_PAD_EFFECT;

    public InteractionResult useOn(UseOnContext context) {
        storedEffect.detonate(context);

        Player player = context.getPlayer();
        if (player != null && !player.isCreative()) {
            context.getItemInHand().setCount(0);
            player.getInventory().add(new ItemStack(alchemineItems.ALCHEMICAL_FLASK.get(), 1));
        }

        return InteractionResult.sidedSuccess(context.getLevel().isClientSide());
    }

}
