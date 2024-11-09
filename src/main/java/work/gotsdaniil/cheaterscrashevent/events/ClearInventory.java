package work.gotsdaniil.cheaterscrashevent.events;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import work.gotsdaniil.cheaterscrashevent.CheatersCrashEvent;
import work.gotsdaniil.cheaterscrashevent.ConfigManager;
import work.gotsdaniil.cheaterscrashevent.api.Placeholders;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ClearInventory implements Listener {

    private final ConfigManager configManager;
    private final CheatersCrashEvent plugin;
    private final ConcurrentMap<Long, Long> timeBeforePunishmentMap;
    private int timeBeforePunishment;
    private String Punishment;

    public ClearInventory(ConfigManager configManager, CheatersCrashEvent plugin) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.Punishment = configManager.ClearInventoryPunishment();
        this.timeBeforePunishmentMap = new ConcurrentHashMap<>();
        this.timeBeforePunishment = configManager.ClearInventoryTimeBeforePunishment();
    }

    public void ClearInventoryPlayer(Player player) {

        if (player == null) return;

        long playerId = player.getUniqueId().getMostSignificantBits();
        long time = timeBeforePunishmentMap.getOrDefault(playerId, 0L);

        if (time >= timeBeforePunishment) {
            executePunishment(player);
        }

        player.getInventory().clear();
    }

    public void reloadConfig() {
        this.timeBeforePunishment = configManager.ClearInventoryTimeBeforePunishment();
        this.Punishment = configManager.ClearInventoryPunishment();
    }

    private void executePunishment(Player player) {
        Bukkit.getScheduler().runTask(plugin, () -> {

            if (Punishment.isEmpty() || Punishment.trim().isEmpty()) return;

            if (player != null) {
                Punishment = Placeholders.replacePlaceholderPlayer(player, Punishment);

                ConsoleCommandSender consoleSender = Bukkit.getServer().getConsoleSender();
                Bukkit.dispatchCommand(consoleSender, Punishment);
            }
        });
    }
}

