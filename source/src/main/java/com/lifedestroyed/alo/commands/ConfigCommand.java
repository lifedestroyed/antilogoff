package com.lifedestroyed.alo.commands;

import com.lifedestroyed.alo.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ConfigCommand {
    private final Main plugin;

    public ConfigCommand(Main plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        sender.sendMessage("§6===== AntiLogOff Config =====");
        sender.sendMessage("§eTimer Duration: §f" + plugin.getConfig().getInt("settings.timer-duration") + " seconds");
        sender.sendMessage("§ePunishment: §f" + plugin.getConfig().getString("settings.punishment"));
        sender.sendMessage("§eBossBar Color: §f" + plugin.getConfig().getString("settings.bossbar-color"));
        return true;
    }
}