package com.lifedestroyed.alo.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.lifedestroyed.alo.Main;

public class ALOCommand implements CommandExecutor {
    private final Main plugin;

    public ALOCommand(Main plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§aALO Commands: /alo reload, /alo config");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            sender.sendMessage("§aКонфиг перезагружен!");
            return true;
        }
        return false;
    }
}