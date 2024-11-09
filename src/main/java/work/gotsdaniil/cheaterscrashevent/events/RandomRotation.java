package work.gotsdaniil.cheaterscrashevent.events;


import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import work.gotsdaniil.cheaterscrashevent.CheatersCrashEvent;
import work.gotsdaniil.cheaterscrashevent.ConfigManager;
import work.gotsdaniil.cheaterscrashevent.api.Placeholders;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RandomRotation implements Listener {

    private final ConfigManager configManager;
    private final CheatersCrashEvent plugin;
    private final ConcurrentMap<Long, Long> timeBeforePunishmentMap;
    private int timeBeforePunishment;
    private String Punishment;

    public RandomRotation(ConfigManager configManager, CheatersCrashEvent plugin) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.Punishment = configManager.RandomRotationPunishment();
        this.timeBeforePunishmentMap = new ConcurrentHashMap<>();
        this.timeBeforePunishment = configManager.RandomRotationTimeBeforePunishment();
    }

    public void PlayerRotation(Player player) {

        if (player == null) return;

        long playerId = player.getUniqueId().getMostSignificantBits();
        long time = timeBeforePunishmentMap.getOrDefault(playerId, 0L);

        if (time >= timeBeforePunishment) {
            executePunishment(player);
        }

        Random random = new Random();
        int RandomRotV = random.nextInt(461) - 360;
        int RandomRotV1 = random.nextInt(281) - 180;

        player.setRotation(RandomRotV, RandomRotV1);
    }

    public void reloadConfig() {
        this.timeBeforePunishment = configManager.RandomRotationTimeBeforePunishment();
        this.Punishment = configManager.RandomRotationPunishment();
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


