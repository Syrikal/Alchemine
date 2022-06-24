package syric.alchemine.util;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;

public class ChatPrint {

    public static void chatPrint(String input, Entity entity) {
        if (entity instanceof Player player) {
            String[] outputSplit = input.split("\n");
            for (String i : outputSplit) {
                player.displayClientMessage(Component.literal(i), false);
            }
        }
    }

    public static void chatPrint(String input, Level level) {
        List<? extends Player> playerList = level.players();
        for (Player player : playerList) {
            chatPrint(input, player);
        }
    }

}
