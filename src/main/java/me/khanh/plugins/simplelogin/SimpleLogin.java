package me.khanh.plugins.simplelogin;

import me.khanh.plugins.simplelogin.commands.SimpleLoginCommand;
import me.khanh.plugins.simplelogin.listener.PlayerListener;
import me.khanh.plugins.simplelogin.storage.MySQL;
import me.khanh.plugins.simplelogin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class SimpleLogin extends JavaPlugin implements Utils {

    private static SimpleLogin instance;
    private MySQL mySQL;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        connectMySQL();
        Objects.requireNonNull(getCommand("simplelogin")).setExecutor(new SimpleLoginCommand());
        registerEvents();
    }

    @Override
    public void onDisable() {
        mySQL.disconnect();
        Bukkit.getScheduler().cancelTasks(this);
        instance = null;
    }

    public void connectMySQL(){
        info("Connecting to Database...");
        mySQL = new MySQL();
        mySQL.connect();
        if (mySQL.isConnected()){
            info("&aSuccessful connection to the Database");
            mySQL.createTables();
        }
    }

    public void reload(){
        mySQL.disconnect();
        saveDefaultConfig();
        connectMySQL();
    }

    public void disable(){
        getServer().getPluginManager().disablePlugin(this);
    }

    public void registerEvents(){
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

    public static SimpleLogin getInstance() {
        return instance;
    }

    public MySQL getMySQL() {
        return mySQL;
    }
}
