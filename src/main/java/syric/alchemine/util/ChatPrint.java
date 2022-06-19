package syric.alchemine.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import com.mojang.brigadier.Message;

public class ChatPrint {

    public static void chatPrint(String input, Entity entity) {
        if (entity instanceof Player player) {
            player.displayClientMessage(Component.literal(input), false);
        }
    }
}
