package maint.gonaeng_town;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Objects;
import java.util.logging.Level;

public final class Gonaeng_Town extends JavaPlugin {
    private static Gonaeng_Town INSTANCE;
    public static Gonaeng_Town getInstance() {
        return INSTANCE;
    }
    public static Connection connection;
    FileConfiguration config = this.getConfig();
    @Override
    public void onEnable() {
        INSTANCE = this;
        setConfig();
        DBConnect();
        getServer().getPluginManager().registerEvents(new Create_Event(), this);
        getCommand("마을").setExecutor(new Town_Command());
    }
    void DBConnect(){
        try {
            connection = DriverManager.getConnection(Objects.requireNonNull(config.getString("DB.URL")), config.getString("DB.ID"), config.getString("DB.PW"));
        } catch (Exception ex) {
            getLogger().log(Level.SEVERE, "DB오류 : "+ex);
        }
    }
    void setConfig() {
        File ConfigFile = new File(getDataFolder(), "config.yml");
        if (!ConfigFile.isFile()) {
            //Lucy_DB
            config.addDefault("DB.ID", "root");
            config.addDefault("DB.PW", "INTY");
            config.addDefault("DB.URL", "jdbc:mysql://127.0.0.1:3307/town?autoReconnect=true");
            config.options().copyDefaults(true);
            saveConfig();
        }
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
