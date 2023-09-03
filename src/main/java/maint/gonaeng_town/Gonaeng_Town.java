package maint.gonaeng_town;

import maint.gonaeng_town.Town_Event.Event_Create;
import maint.gonaeng_town.Town_Event.Event_Enter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

import static maint.gonaeng_town.Town_DB.DB_TownCreate.getTownRS;
import static maint.gonaeng_town.Town_Manager.Manager_Location.TownAriaUpdate;
import static maint.gonaeng_town.Town_Manager.Manager_Location.getAllLoc;

public final class Gonaeng_Town extends JavaPlugin {
    private static Gonaeng_Town INSTANCE;
    public static Gonaeng_Town getInstance() {
        return INSTANCE;
    }
    public static Connection connection;
    FileConfiguration config = this.getConfig();
    public static HashMap<String, ArrayList<ArrayList<Integer>>> TownAria = new HashMap<>();
    public static HashMap<String, String> TownName = new HashMap<>();
    @Override
    public void onEnable() {
        INSTANCE = this;
        setConfig();
        DBConnect();
        getServer().getPluginManager().registerEvents(new Event_Create(), this);
        getServer().getPluginManager().registerEvents(new Event_Enter(), this);
        getCommand("마을").setExecutor(new Town_Command());
        LoadTown();
    }
    void LoadTown(){
        ResultSet rs = getTownRS();
        while (true){
            try {
                if (!Objects.requireNonNull(rs).next()) break;
                TownAriaUpdate(rs.getInt("LLocX"),rs.getInt("RLocX"),rs.getInt("LLocZ"),rs.getInt("RLocZ"),rs.getString("CreateUserID"));
                TownName.put(rs.getString("CreateUserID"),rs.getString("TownName"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
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
