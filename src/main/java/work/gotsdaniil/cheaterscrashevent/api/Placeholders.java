package work.gotsdaniil.cheaterscrashevent.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;


public class Placeholders implements Listener {

    public static String replacePlaceholderPlayer(Player player, String message) {

        if (player == null) return message;

        message = message.replace("%player%", player.getName());

        return message;
    }

}
