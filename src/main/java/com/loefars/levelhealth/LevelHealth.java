package com.loefars.levelhealth;

import com.sun.jdi.CharType;
import net.md_5.bungee.api.chat.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.processing.SupportedSourceVersion;

public final class LevelHealth extends JavaPlugin implements Listener {


    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("LevelHealth has been loaded!");
        this.getServer().getPluginManager().registerEvents(this, this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("LevelHealth has been unloaded!");
    }

    @EventHandler
    public void onPlayerXPLevelChange(final PlayerLevelChangeEvent event) {
        final Player player = event.getPlayer();
        this.getServer().getScheduler().runTaskLater(this, new Runnable() {
            @Override
            public void run() {
                LevelHealth.this.scaleHealth(player);
            }
        }, 1L);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        player.sendMessage(ChatColor.GREEN + "This plugin was created by Loefars!" + ChatColor.GREEN + ChatColor.BOLD);
        TextComponent message = new TextComponent("Check her out here!");
        message.setColor(net.md_5.bungee.api.ChatColor.GREEN);
        message.setUnderlined(true);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://socials.loefars.com"));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here!").create()));
        player.spigot().sendMessage(message);
        this.scaleHealth(event.getPlayer());
        final int joinXP = player.getLevel();
        if (joinXP == 0) {
            this.getServer().getScheduler().runTaskLater(this, new Runnable() {
                @Override
                public void run() {
                    player.setLevel(5);
                    LevelHealth.this.scaleHealth(player);
                }

            }, 1L);
            }
        }



    private void scaleHealth(final Player player) {
        final int lvl = player.getLevel();
        if (lvl <= 0) {
            player.setMaxHealth(1);
            return;
        }
        player.setMaxHealth(lvl);
        player.setHealth(lvl);
    }

    private void scaleXP(final Player player, final double dmg) {
        int lvl = player.getLevel();
        int lvlChange = (int) (lvl - dmg);
        if (lvlChange < 0) {
            lvlChange = 0;
        }
        player.setLevel(lvlChange);
        this.getServer().getScheduler().runTaskLater(this, new Runnable() {
            @Override
            public void run() {
                LevelHealth.this.scaleHealth(player);
            }
        }, 1L);

    }

    @EventHandler
    public void onPlayerDamage(final EntityDamageEvent event) {
        final EntityType entityType = event.getEntityType();
        if (entityType == EntityType.PLAYER) {
            final Player player = (Player) event.getEntity();
            final double dmg = event.getFinalDamage();
            this.getServer().getScheduler().runTaskLater(this, new Runnable() {
                @Override
                public void run() {
                    LevelHealth.this.scaleXP(player, dmg);
                }
            }, 1L);
        }

    }

    @EventHandler
    public void onPlayerRespawn(final PlayerRespawnEvent event) {
        final Player player = event.getPlayer();
        this.getServer().getScheduler().runTaskLater(this, new Runnable() {
            @Override
            public void run() {
                player.setLevel(5);
                LevelHealth.this.scaleHealth(player);
            }
        }, 1L);
    }
}




