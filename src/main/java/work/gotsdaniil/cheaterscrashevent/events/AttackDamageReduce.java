package work.gotsdaniil.cheaterscrashevent.events;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import work.gotsdaniil.cheaterscrashevent.CheatersCrashEvent;
import work.gotsdaniil.cheaterscrashevent.ConfigManager;
import work.gotsdaniil.cheaterscrashevent.api.Placeholders;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AttackDamageReduce implements Listener {

    private final ConfigManager configManager;
    private final CheatersCrashEvent plugin;
    private final ConcurrentMap<Long, Long> timeBeforePunishmentMap;
    private int timeBeforePunishment;
    private int ModeDamageReduce;
    private double NumberValue;
    private String Punishment;

    public AttackDamageReduce(ConfigManager configManager, CheatersCrashEvent plugin) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.Punishment = configManager.AttackDamageReducePunishment();
        this.NumberValue = configManager.AttackDamageReduceNumberValue();
        this.ModeDamageReduce = configManager.AttackDamageReduceModeDamageReduce();
        this.timeBeforePunishmentMap = new ConcurrentHashMap<>();
        this.timeBeforePunishment = configManager.AttackDamageReduceTimeBeforePunishment();
    }


    public void setPlayerDamage(Player player, EntityDamageByEntityEvent event) {

        if (player == null) return;

        long playerId = player.getUniqueId().getMostSignificantBits();
        long time = timeBeforePunishmentMap.getOrDefault(playerId, 0L);

        if (time >= timeBeforePunishment) {
            executePunishment(player);
        }

        if (ModeDamageReduce == 0) {
            event.setDamage(calculatePlayerDamage(player) / NumberValue);
            return;
        }

        if (ModeDamageReduce == 1) {
            event.setDamage(calculatePlayerDamage(player) - NumberValue);
            return;
        }

        if (ModeDamageReduce == 2) {
            event.setCancelled(true);
        }
    }

    private double calculatePlayerDamage(Player player) {
        AttributeInstance attackDamageAttribute = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if (attackDamageAttribute != null) {
            return attackDamageAttribute.getValue();
        }
        return 1.0;
    }


    public void reloadConfig() {
        this.timeBeforePunishment = configManager.AttackDamageReduceTimeBeforePunishment();
        this.ModeDamageReduce = configManager.AttackDamageReduceModeDamageReduce();
        this.NumberValue = configManager.AttackDamageReduceNumberValue();
        this.Punishment = configManager.AttackDamageReducePunishment();
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