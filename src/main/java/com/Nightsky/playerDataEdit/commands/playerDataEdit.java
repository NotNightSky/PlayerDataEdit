package com.Nightsky.playerDataEdit.commands;

import com.Nightsky.playerDataEdit.menus.playerActionMenu;
import com.Nightsky.playerDataEdit.menus.playerDataEditMenu;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class playerDataEdit implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("playerdataedit.menu")) {
            sender.sendMessage("You do not have permission to use this command.");
            return true;
        } else if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            playerDataEditMenu.open(player);
            return true;
        }

        String name = args[0];
        Player online = Bukkit.getPlayerExact(name);
        UUID targetId;
        String resolvedName;
        if (online != null) {
            targetId = online.getUniqueId();
            resolvedName = online.getName();
        } else {
            OfflinePlayer offline = Bukkit.getOfflinePlayer(name);
            if (!offline.hasPlayedBefore()) {
                player.sendMessage("Player not found: " + name);
                return true;
            }
            targetId = offline.getUniqueId();
            resolvedName = offline.getName();
        }

        playerActionMenu.open(player, targetId, resolvedName, 0);
        return true;
    }
}
