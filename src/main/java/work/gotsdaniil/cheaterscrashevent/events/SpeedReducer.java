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

public class SpeedReducer implements Listener {

    private final ConfigManager configManager;
    private final CheatersCrashEvent plugin;
    private final ConcurrentMap<Long, Long> timeBeforePunishmentMap;
    private int timeBeforePunishment;
    private double NumberValue;
    private String Punishment;

    public SpeedReducer(ConfigManager configManager, CheatersCrashEvent plugin) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.Punishment = configManager.SpeedReducerPunishment();
        this.NumberValue = configManager.SpeedReducerNumberValue();
        this.timeBeforePunishmentMap = new ConcurrentHashMap<>();
        this.timeBeforePunishment = configManager.SpeedReducerTimeBeforePunishment();
    }

    public void setPlayerSpeed(Player player) {

        if (player == null) return;

        long playerId = player.getUniqueId().getMostSignificantBits();
        long time = timeBeforePunishmentMap.getOrDefault(playerId, 0L);

        if (time >= timeBeforePunishment) {
            executePunishment(player);
        }

        player.setWalkSpeed((float) NumberValue);

    }

    public void reloadConfig() {
        this.timeBeforePunishment = configManager.SpeedReducerTimeBeforePunishment();
        this.NumberValue = configManager.SpeedReducerNumberValue();
        this.Punishment = configManager.SpeedReducerPunishment();
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