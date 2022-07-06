package syric.alchemine.outputs.general.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import syric.alchemine.outputs.general.alchemicaleffects.AlchemicalEffects;
import syric.alchemine.setup.AlchemineItems;

import static syric.alchemine.util.ChatPrint.chatPrint;

public class FullFlaskItem extends Item {

public FullFlaskItem(Properties properties) {super(properties);}

    private int index = 0;

    public InteractionResult useOn(UseOnContext context) {

        if (!context.getLevel().isClientSide()) {
            AlchemicalEffects.EFFECT_LIST.get(index).detonate(context);

            Player player = context.getPlayer();
            if (player != null && !player.isCreative()) {
                context.getItemInHand().setCount(0);
                player.getInventory().add(new ItemStack(AlchemineItems.ALCHEMICAL_FLASK.get(), 1));
            }
        }

        return InteractionResult.sidedSuccess(context.getLevel().isClientSide());
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            if (player.isShiftKeyDown()) {
                index--;
                if (index < 0) {
                    index = AlchemicalEffects.EFFECT_LIST.size() - 1;
                }
            } else {
                index++;
                if (index >= AlchemicalEffects.EFFECT_LIST.size()) {
                    index = 0;
                }
            }
            String str = AlchemicalEffects.EFFECT_LIST.get(index).toString();
            chatPrint("Changing stored effect to " + str, level);
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }
}
