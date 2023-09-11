package com.loefars.levelhealth;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.plugin.Plugin;
import java.util.Iterator;

import java.util.logging.Level;

import static org.bukkit.Bukkit.broadcastMessage;
import static org.bukkit.Bukkit.getServer;

public class GetLevel implements Listener {
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
            EntityType EntVar = e.getEntityType();
        Bukkit.broadcastMessage(String.valueOf(EntVar));

    }

    @EventHandler
    public void onPlayerXPLevelChange(final PlayerLevelChangeEvent event) {
        final Player player = event.getPlayer();
        getServer().getScheduler().runTaskLater((Plugin) this, new Runnable(){
            @Override
            public void run(){
                GetLevel.this.scaleHealth(player);
            }
        }, 1L);
    }
    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        this.scaleHealth(event.getPlayer());
    }

    private void scaleHealth(final Player player) {
        double maxHP = player.getLevel();
        broadcastMessage("Max HP" + maxHP);
        player.setMaxHealth(maxHP);
        double healthSet = player.getMaxHealth();
        broadcastMessage("Set Player HP to " + healthSet);
        }
    }
