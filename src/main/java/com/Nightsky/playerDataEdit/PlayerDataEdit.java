package com.Nightsky.playerDataEdit;

import com.Nightsky.playerDataEdit.commands.playerDataEdit;
import com.Nightsky.playerDataEdit.commands.recheckDirectory;
import com.Nightsky.playerDataEdit.menus.playerActionMenu;
import com.Nightsky.playerDataEdit.menus.playerDataEditMenu;
import com.Nightsky.playerDataEdit.utils.FindNBTDirectory;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;
import java.util.Objects;

public final class PlayerDataEdit extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Loading PlayerDataEdit plugin...");
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new playerDataEditMenu(), this);
        getServer().getPluginManager().registerEvents(new playerActionMenu(), this);
        Objects.requireNonNull(getCommand("recheckdirectory")).setExecutor(new recheckDirectory(this));
        Objects.requireNonNull(getCommand("playerdataedit")).setExecutor(new playerDataEdit());

        if (getConfig().getBoolean("firstLaunch", true)) {
            getLogger().info("Performing initial setup...");
            Path nbtDirectory = FindNBTDirectory.find(getServer().getWorldContainer().toPath().normalize());
            if (nbtDirectory != null) {
                getConfig().set("NBTDirectory", nbtDirectory.toString());
                getConfig().set("firstLaunch", false);
                saveConfig();
                getLogger().info("NBT directory set to: " + nbtDirectory);
            } else {
                getLogger().warning("Could not locate player data directory. Retrying on next startup.");
            }
        }
        getLogger().info("PlayerDataEdit plugin loaded.");
    }
}
