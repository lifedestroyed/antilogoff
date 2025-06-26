package com.lifedestroyed.alo;

import com.lifedestroyed.alo.commands.ALOCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private PvPManager pvpManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.pvpManager = new PvPManager(this);

        ALOCommand aloCommand = new ALOCommand(this);
        getCommand("alo").setExecutor(aloCommand);
        getCommand("alo").setTabCompleter(aloCommand);

        getLogger().info("AntiLogOff v" + getDescription().getVersion() + " enabled!");
    }

    public PvPManager getPvPManager() {
        return pvpManager;
    }
}