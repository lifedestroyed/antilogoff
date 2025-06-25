package com.lifedestroyed.alo.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.lifedestroyed.alo.Main;

public class ConfigCommand implements CommandExecutor {
    private final Main plugin;

    public ConfigCommand(Main plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        sender.sendMessage("§6===== ALO Config =====");
        sender.sendMessage("§eTimer: §f" + plugin.getConfig().getInt("settings.timer-duration") + " сек");
        sender.sendMessage("§ePunishment: §f" + plugin.getConfig().getString("settings.punishment"));
        sender.sendMessage("§eActionBar Text: §f" + plugin.getConfig().getString("messages.actionbar-text"));
        return true;
    }
}