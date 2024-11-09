package work.gotsdaniil.cheaterscrashevent;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
    private final CheatersCrashEvent plugin;
    private FileConfiguration config;

    public ConfigManager(CheatersCrashEvent plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    private File getDataFolder() {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        return dataFolder;
    }

    private void loadConfig() {
        File configFile = new File(getDataFolder(), "config.yml");

        // Проверяем, существует ли файл
        if (!configFile.exists()) {
            try {
                // Копируем файл из ресурсов в папку плагина
                InputStream inputStream = plugin.getResource("config.yml");
                if (inputStream == null) {
                    plugin.getLogger().severe("Файл config.yml не найден в ресурсах плагина!");
                    return;
                }
                Path configFilePath = configFile.toPath();
                if (configFilePath == null) {
                    plugin.getLogger().severe("Не удалось получить путь к файлу config.yml!");
                    return;
                }
                Files.copy(inputStream, configFilePath);
            } catch (IOException e) {
                plugin.getLogger().severe("Не удалось скопировать файл config.yml из ресурсов!");
                e.printStackTrace();
                return;
            }
        }

        // Загружаем конфигурацию
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public String AttackDamageReducePunishment() {
        return config.getString("AttackDamageReduce.Punishment");
    }

    public int AttackDamageReduceTimeBeforePunishment() {
        return config.getInt("AttackDamageReduce.Time");
    }

    public int AttackDamageReduceModeDamageReduce() {
        return config.getInt("AttackDamageReduce.Mode");
    }

    public double AttackDamageReduceNumberValue() {
        return config.getInt("AttackDamageReduce.Value");
    }

    public String SpeedReducerPunishment() {
        return config.getString("SpeedReducer.Punishment");
    }

    public int SpeedReducerTimeBeforePunishment() {
        return config.getInt("SpeedReducer.Time");
    }

    public double SpeedReducerNumberValue() {
        return config.getDouble("SpeedReducer.Value");
    }

    public String BigHungerPunishment() {
        return config.getString("BigHunger.Punishment");
    }

    public int BigHungerTimeBeforePunishment() {
        return config.getInt("BigHunger.Time");
    }

    public int BigHungerNumberValue() {
        return config.getInt("BigHunger.Value");
    }

    public int BigHungerTimeToSetHunger() {
        return config.getInt("BigHunger.timeToSetHunger");
    }

    public String ClearInventoryPunishment() {
        return config.getString("ClearInventory.Punishment");
    }

    public int ClearInventoryTimeBeforePunishment() {
        return config.getInt("ClearInventory.Time");
    }

    public String RandomRotationPunishment() {
        return config.getString("RandomRotation.Punishment");
    }

    public int RandomRotationTimeBeforePunishment() {
        return config.getInt("RandomRotation.Time");
    }

    public void reloadConfig() {
        loadConfig();
        plugin.saveDefaultConfig();
    }
}