package work.gotsdaniil.cheaterscrashevent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;
import work.gotsdaniil.cheaterscrashevent.events.*;

public class CheatersCrashEventCommands implements CommandExecutor, @NotNull Listener {

    private final CheatersCrashEvent plugin;
    private final ConfigManager configManager;
    private final AttackDamageReduce attackDamageReduce;
    private final RandomRotation randomRotation;
    private final ClearInventory clearInventory;
    private final SpeedReducer speedReducer;
    private final BigHunger bigHunger;

    public CheatersCrashEventCommands(ConfigManager configManager, CheatersCrashEvent plugin) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.attackDamageReduce = new AttackDamageReduce(configManager, plugin);
        this.randomRotation = new RandomRotation(configManager, plugin);
        this.clearInventory = new ClearInventory(configManager, plugin);
        this.speedReducer = new SpeedReducer(configManager, plugin);
        this.bigHunger = new BigHunger(configManager, plugin);
        Bukkit.getPluginManager().registerEvents(attackDamageReduce, plugin);
        Bukkit.getPluginManager().registerEvents(randomRotation, plugin);
        Bukkit.getPluginManager().registerEvents(speedReducer, plugin);
        Bukkit.getPluginManager().registerEvents(bigHunger, plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender.hasPermission("cheaterscrashevent.use") || sender instanceof ConsoleCommandSender) {

            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {

                attackDamageReduce.reloadConfig();
                randomRotation.reloadConfig();
                clearInventory.reloadConfig();
                speedReducer.reloadConfig();
                bigHunger.reloadConfig();
                configManager.reloadConfig();
                sender.sendMessage(ChatColor.GREEN + "CheatersCrashEvent успешно перезагружен");

                return true;
            }

            if (args.length != 2) {
                sender.sendMessage("Использование: /cce <тип-пытки> <ник-игрока>");
                return true;
            }

            String playerName = args[1];
            Player targetPlayer = Bukkit.getPlayer(playerName);

            if (targetPlayer == null) {
                sender.sendMessage("Игрок с ником " + playerName + " не найден.");
                return true;
            }

            if (args[0].equalsIgnoreCase("help")) {

                String message = "&6&l&m========&e&lCheatersCrashEvent&6&l&m========\n" +
                        "&a/cce reload &e- команда перезагрузки плагина\n" +
                        "&aAttackDamageReduce &e- пытка уреза урона игрока\n" +
                        "&RandomRotation &e- пытка рандомной ротации прицела\n" +
                        "&aSpeedReducer &e- пытка уреза скорости игрока\n" +
                        "&aBigHunger &e- пытка увеличения голода игрока\n" +
                        "&ClearInventory &e- полная отчистка инвентаря игрока\n" +
                        "&6&l&m==================================";
                sender.sendMessage(message);

                return true;
            }

            if (args[0].equalsIgnoreCase("AttackDamageReduce")) {
                EntityDamageByEntityEvent DamageEvent = new EntityDamageByEntityEvent(targetPlayer, targetPlayer, EntityDamageByEntityEvent.DamageCause.ENTITY_ATTACK, 1.0);
                attackDamageReduce.setPlayerDamage(targetPlayer, DamageEvent);
            } else if (args[0].equalsIgnoreCase("SpeedReducer")) {
                speedReducer.setPlayerSpeed(targetPlayer);
            } else if (args[0].equalsIgnoreCase("BigHunger")) {
                bigHunger.setHungerTime(targetPlayer);
            } else if (args[0].equalsIgnoreCase("ClearInventory")) {
                clearInventory.ClearInventoryPlayer(targetPlayer);
            } else if (args[0].equalsIgnoreCase("RandomRotation")) {
                randomRotation.PlayerRotation(targetPlayer);
            } else {
                sender.sendMessage("Неизвестный тип пытки: " + args[0]);
            }

            return true;
        }
        return false;
    }
}