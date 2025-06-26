package com.lifedestroyed.alo;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PvPManager implements Listener {
    private final Main plugin;
    private final Map<UUID, BukkitTask> timers = new HashMap<>();
    private final Map<UUID, BossBar> bossBars = new HashMap<>();
    private final Map<UUID, Boolean> inCombat = new HashMap<>();

    public PvPManager(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player victim) || !(e.getDamager() instanceof Player attacker)) {
            return;
        }

        handleCombatStart(victim);
        handleCombatStart(attacker);
    }

    private void handleCombatStart(Player player) {
        UUID uuid = player.getUniqueId();

        if (!inCombat.containsKey(uuid)) {
            String message = plugin.getConfig().getString("messages.combat-start")
                    .replace("%time%", String.valueOf(plugin.getConfig().getInt("settings.timer-duration")));
            player.sendMessage(message);
            inCombat.put(uuid, true);
        }

        startTimer(player);
    }

    private void startTimer(Player player) {
        cancelTimer(player);

        final int[] duration = {plugin.getConfig().getInt("settings.timer-duration")};
        BossBar bossBar = createBossBar(player, duration[0]);

        BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (duration[0] <= 0) {
                player.sendMessage(plugin.getConfig().getString("messages.combat-end"));
                cancelTimer(player);
                inCombat.remove(player.getUniqueId());
                return;
            }

            String text = plugin.getConfig().getString("messages.bossbar-text")
                    .replace("%time%", String.valueOf(duration[0]));
            bossBar.setTitle(text);
            bossBar.setProgress((double) duration[0] / plugin.getConfig().getInt("settings.timer-duration"));

            duration[0]--;
        }, 0L, 20L);

        timers.put(player.getUniqueId(), task);
    }

    private BossBar createBossBar(Player player, int duration) {
        cancelBossBar(player);

        String text = plugin.getConfig().getString("messages.bossbar-text")
                .replace("%time%", String.valueOf(duration));

        BarColor color = getBossBarColor();
        BossBar bossBar = Bukkit.createBossBar(text, color, BarStyle.SOLID);
        bossBar.setProgress(1.0);
        bossBar.addPlayer(player);
        bossBars.put(player.getUniqueId(), bossBar);

        return bossBar;
    }

    private BarColor getBossBarColor() {
        String colorStr = plugin.getConfig().getString("settings.bossbar-color");

        try {
            return BarColor.valueOf(colorStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return BarColor.RED;
        }
    }

    private void cancelBossBar(Player player) {
        BossBar bossBar = bossBars.get(player.getUniqueId());
        if (bossBar != null) {
            bossBar.removeAll();
            bossBars.remove(player.getUniqueId());
        }
    }

    private void cancelTimer(Player player) {
        BukkitTask task = timers.get(player.getUniqueId());
        if (task != null) {
            task.cancel();
            timers.remove(player.getUniqueId());
        }
        cancelBossBar(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (timers.containsKey(player.getUniqueId())) {
            if (plugin.getConfig().getBoolean("settings.punish-on-logoff")) {
                punishPlayer(player);
            }
            cancelTimer(player);
        }
    }

    private void punishPlayer(Player player) {
        String punishment = plugin.getConfig().getString("settings.punishment").toLowerCase();
        switch (punishment) {
            case "kill":
                player.setHealth(0);
                break;
            case "damage":
                player.damage(10.0);
                break;
            case "ban":
                Bukkit.getBanList(org.bukkit.BanList.Type.NAME)
                        .addBan(player.getName(), "Combat Logout", null, "AntiLogOff");
                break;
        }
        player.sendMessage(plugin.getConfig().getString("messages.punished-message"));
    }
}