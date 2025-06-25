package com.lifedestroyed.alo;

import com.lifedestroyed.alo.commands.ALOCommand;
import com.lifedestroyed.alo.commands.ConfigCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private PvPManager pvpManager;
    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        this.pvpManager = new PvPManager(this);

        // Регистрация команд
        getCommand("alo").setExecutor(new ALOCommand(this));
        getCommand("aloconfig").setExecutor(new ConfigCommand(this));

        getLogger().info("AntiLogOff включен!");
    }

    public PvPManager getPvPManager() { return pvpManager; }
    public static Main getInstance() { return instance; }
}