package work.gotsdaniil.cheaterscrashevent;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import work.gotsdaniil.cheaterscrashevent.events.*;

import java.util.Objects;

public class CheatersCrashEvent extends JavaPlugin {

    @Override
    public void onEnable() {
        ConfigManager configManager = new ConfigManager(this);

        new AttackDamageReduce(configManager, this);
        new RandomRotation(configManager, this);
        new ClearInventory(configManager, this);
        new SpeedReducer(configManager, this);
        new BigHunger(configManager, this);

        CheatersCrashEventCommands cheatersCrashEventCommands = new CheatersCrashEventCommands(configManager, this);
        Bukkit.getPluginManager().registerEvents(cheatersCrashEventCommands, this);

        Objects.requireNonNull(this.getCommand("cheaterscrashevent")).setExecutor(cheatersCrashEventCommands);
    }
}