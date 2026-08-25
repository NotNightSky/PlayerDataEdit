package com.Nightsky.playerDataEdit.commands;

import com.Nightsky.playerDataEdit.PlayerDataEdit;
import com.Nightsky.playerDataEdit.utils.FindNBTDirectory;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.nio.file.Path;

public class recheckDirectory implements CommandExecutor {
    private final PlayerDataEdit plugin = PlayerDataEdit.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("playerdataedit.recheckNBTDirectory")) {
            sender.sendMessage("You do not have permission to use this command.");
            return true;
        }

        Path serverRoot = Bukkit.getWorldContainer().toPath().normalize();
        String configured = plugin.getConfig().getString("NBTDirectory", "");
        Path currentDirectory = (configured == null || configured.isEmpty())
                ? null
                : serverRoot.resolve(configured).normalize();

        if (FindNBTDirectory.isValid(currentDirectory)) {
            sender.sendMessage("Current NBT directory is up to date: " + currentDirectory);
            return true;
        }

        Path found = FindNBTDirectory.find(serverRoot);
        if (found == null) {
            sender.sendMessage("Could not locate any player data directory under " + serverRoot + ".");
            return true;
        }

        plugin.getConfig().set("NBTDirectory", found.toString());
        plugin.saveConfig();
        sender.sendMessage(currentDirectory == null
                ? "NBT directory set to: " + found
                : "Stale NBT directory " + currentDirectory + " replaced with: " + found);
        return true;
    }
}
