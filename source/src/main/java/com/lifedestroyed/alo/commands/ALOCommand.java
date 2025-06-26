package com.lifedestroyed.alo.commands;

import com.lifedestroyed.alo.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ALOCommand implements CommandExecutor, TabCompleter {
    private final Main plugin;
    private final ConfigCommand configCommand;

    public ALOCommand(Main plugin) {
        this.plugin = plugin;
        this.configCommand = new ConfigCommand(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                plugin.reloadConfig();
                sender.sendMessage("§aConfiguration reloaded!");
                return true;
            case "config":
                return configCommand.onCommand(sender, cmd, label, args);
            case "info":
                sendPluginInfo(sender);
                return true;
            default:
                sendHelp(sender);
                return true;
        }
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§6AntiLogOff Commands:");
        sender.sendMessage("§e/alo reload §7- Reload configuration");
        sender.sendMessage("§e/alo config §7- Show current settings");
        sender.sendMessage("§e/alo info §7- Show plugin information");
    }

    private void sendPluginInfo(CommandSender sender) {
        sender.sendMessage("§6===== AntiLogOff Info =====");
        sender.sendMessage("§eVersion: §f" + plugin.getDescription().getVersion());
        sender.sendMessage("§eAuthor: §f" + plugin.getDescription().getAuthors());
        sender.sendMessage("§eDescription: §fPrevents combat logging with customizable punishments");
        sender.sendMessage("§eWebsite: §fhttps://github.com/lifedestroyed/AntiLogOff");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();

        if (args.length == 1) {
            commands.add("reload");
            commands.add("config");
            commands.add("info");
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }

        Collections.sort(completions);
        return completions;
    }
}