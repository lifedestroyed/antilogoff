package com.lifedestroyed.alo;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;
import java.util.HashMap;
import java.util.Map;

public class PvPManager implements Listener {
    private final Main plugin;
    private final Map<Player, BukkitTask> timers = new HashMap<>();

    public PvPManager(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player victim) || !(e.getDamager() instanceof Player attacker)) return;
        startTimer(victim);
        startTimer(attacker);
    }

    private void startTimer(Player player) {
        cancelTimer(player);

        // Финализируем переменную для лямбды
        final int[] duration = {plugin.getConfig().getInt("settings.timer-duration")};
        String startMsg = plugin.getConfig().getString("messages.timer-start")
                .replace("%time%", String.valueOf(duration[0]));
        player.sendMessage(startMsg);

        // Используем BukkitRunnable вместо лямбды для совместимости
        BukkitTask task = new org.bukkit.scheduler.BukkitRunnable() {
            @Override
            public void run() {
                if (duration[0] <= 0) {
                    player.sendMessage(plugin.getConfig().getString("messages.timer-end"));
                    cancelTimer(player);
                    this.cancel();
                    return;
                }

                // Альтернатива sendActionBar (если метод недоступен)
                String actionBar = plugin.getConfig().getString("messages.actionbar-text")
                        .replace("%time%", String.valueOf(duration[0]));
                player.sendTitle("", actionBar, 0, 20, 0); // Имитация ActionBar
                duration[0]--;
            }
        }.runTaskTimer(plugin, 0L, 20L);

        timers.put(player, task);
    }

    private void cancelTimer(Player player) {
        if (timers.containsKey(player)) {
            timers.get(player).cancel();
            timers.remove(player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (timers.containsKey(player) && plugin.getConfig().getBoolean("settings.punish-on-logoff")) {
            punishPlayer(player);
        }
    }

    private void punishPlayer(Player player) {
        String punishment = plugin.getConfig().getString("settings.punishment");
        switch (punishment) {
            case "kill" -> player.setHealth(0);
            case "damage" -> player.damage(10.0);
            case "ban" -> Bukkit.getBanList(org.bukkit.BanList.Type.NAME)
                    .addBan(player.getName(), "Выход в PvP", null, "ALO");
            default -> player.sendMessage(plugin.getConfig().getString("messages.punished-message"));
        }
    }
}