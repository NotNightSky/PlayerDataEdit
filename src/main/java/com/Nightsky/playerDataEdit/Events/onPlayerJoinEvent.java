package com.Nightsky.playerDataEdit.Events;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class onPlayerJoinEvent implements Listener {
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.getLogger().info("Player joined: " + event.getPlayer().getName());
    }
}
