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

public class BigHunger implements Listener {

    private final ConfigManager configManager;
    private final CheatersCrashEvent plugin;
    private final ConcurrentMap<Long, Long> timeBeforePunishmentMap;
    private int timeBeforePunishment;
    private int timeToSetHunger;
    private int NumberValue;
    private String Punishment;

    public BigHunger(ConfigManager configManager, CheatersCrashEvent plugin) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.Punishment = configManager.BigHungerPunishment();
        this.NumberValue = configManager.BigHungerNumberValue();
        this.timeToSetHunger = configManager.BigHungerTimeToSetHunger();
        this.timeBeforePunishmentMap = new ConcurrentHashMap<>();
        this.timeBeforePunishment = configManager.BigHungerTimeBeforePunishment();
    }

    public void setHungerTime(Player player) {

        if (player == null) return;

        long playerId = player.getUniqueId().getMostSignificantBits();
        long time = timeBeforePunishmentMap.getOrDefault(playerId, 0L);

        if (time >= timeBeforePunishment) {
            executePunishment(player);
        }

        if (time >= timeToSetHunger) {
            player.setFoodLevel(player.getFoodLevel() - NumberValue);
        }
    }

    public void reloadConfig() {
        this.timeBeforePunishment = configManager.BigHungerTimeBeforePunishment();
        this.timeToSetHunger = configManager.BigHungerTimeToSetHunger();
        this.NumberValue = configManager.BigHungerNumberValue();
        this.Punishment = configManager.BigHungerPunishment();
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
