package com.Nightsky.playerDataEdit;

import com.Nightsky.playerDataEdit.Events.onPlayerJoinEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerDataEdit extends JavaPlugin {
    public void onEnable() {
        Bukkit.getLogger().info("Loading PlayerDataEdit plugin...");
        getServer().getPluginManager().registerEvents(new onPlayerJoinEvent(), this);
        Bukkit.getLogger().info("PlayerDataEdit plugin loaded.");
    }

    public void onDisable() {
        Bukkit.getLogger().info("Unloading PlayerDataEdit plugin...");
    }
}