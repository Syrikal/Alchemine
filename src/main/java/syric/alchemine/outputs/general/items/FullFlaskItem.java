package syric.alchemine.outputs.general.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import syric.alchemine.outputs.general.effects.AlchemicalEffect;
import syric.alchemine.outputs.general.effects.registerEffects;
import syric.alchemine.setup.AlchemineItems;

import java.util.ArrayList;
import java.util.List;

import static syric.alchemine.util.ChatPrint.chatPrint;

public class FullFlaskItem extends Item {

public FullFlaskItem(Properties properties) {super(properties);}

    private static List<AlchemicalEffect> effects = new ArrayList<>();
    private static List<String> effectNames = new ArrayList<>();
    private int index = 0;
    private boolean initialized = false;

    public InteractionResult useOn(UseOnContext context) {
        if (!initialized) {
            init();
        }

        effects.get(index).detonate(context);

        Player player = context.getPlayer();
        if (player != null && !player.isCreative()) {
            context.getItemInHand().setCount(0);
            player.getInventory().add(new ItemStack(AlchemineItems.ALCHEMICAL_FLASK.get(), 1));
        }

        return InteractionResult.sidedSuccess(context.getLevel().isClientSide());
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!initialized) {
            init();
        }
        if (!level.isClientSide) {
            index++;
            if (index >= effects.size()) {
                index = 0;
            }
            String str = effectNames.get(index);
            chatPrint("Changing stored effect to " + str, level);
        }

        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }

    public void init() {
        add(registerEffects.CRASH_PAD_EFFECT, "Crash Pad");
        add(registerEffects.INSTANT_SHELTER_EFFECT, "Instant Shelter");
        add(registerEffects.FOAMSNARE_EFFECT, "Foamsnare");
        add(registerEffects.WEBSNARE_EFFECT, "Websnare");
        add(registerEffects.TAR_STICK_EFFECT, "Tar Stick");
        add(registerEffects.GLUE_STICK_EFFECT, "Glue Stick");
        add(registerEffects.STONE_BLOB_EFFECT, "Stone Blob");
        add(registerEffects.OIL_SLICK_EFFECT, "Oil Slick");
        add(registerEffects.WALL_SLIDE_EFFECT, "Wall Slide");
        initialized = true;
    }

    private void add(AlchemicalEffect effect, String name) {
        effects.add(effect);
        effectNames.add(name);
    }

}
