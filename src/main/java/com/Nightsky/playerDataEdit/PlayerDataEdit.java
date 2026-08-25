package com.Nightsky.playerDataEdit;

import com.Nightsky.playerDataEdit.Events.onPlayerJoinEvent;
import com.Nightsky.playerDataEdit.commands.recheckDirectory;
import com.Nightsky.playerDataEdit.menus.playerDataEditMenu;
import com.Nightsky.playerDataEdit.utils.FindNBTDirectory;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;
import java.util.Objects;

public final class PlayerDataEdit extends JavaPlugin {

    private static PlayerDataEdit instance;

    public void onEnable() {
        instance = this;
        Bukkit.getLogger().info("Loading PlayerDataEdit plugin...");
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new onPlayerJoinEvent(), this);
        getServer().getPluginManager().registerEvents(new playerDataEditMenu(), this);
        Objects.requireNonNull(getCommand("recheckdirectory")).setExecutor(new recheckDirectory());

        if (getConfig().getBoolean("firstLaunch", true)) {
            Bukkit.getLogger().info("This is the first launch of the plugin. Performing initial setup...");
            Bukkit.getLogger().info(Bukkit.getWorldContainer().getAbsolutePath());
            Path nbtDirectory = FindNBTDirectory.find(Bukkit.getWorldContainer().toPath().normalize());
            if (nbtDirectory != null) {
                getConfig().set("NBTDirectory", nbtDirectory.toString());
                getConfig().set("firstLaunch", false);
            } else {
                Bukkit.getLogger().warning("Could not locate player data directory. Retrying on next startup.");
            }
            saveConfig();
        }
        Bukkit.getLogger().info("PlayerDataEdit plugin loaded.");
    }

    public void onDisable() {
        Bukkit.getLogger().info("Unloading PlayerDataEdit plugin...");
    }

    public static PlayerDataEdit getInstance() {
        return instance;
    }
}