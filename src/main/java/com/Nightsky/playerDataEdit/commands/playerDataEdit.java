package com.Nightsky.playerDataEdit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class playerDataEdit implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("playerdataedit.menu")) {
            sender.sendMessage("You do not have permission to use this command.");
            return true;
        }

        // TODO: Implement the command logic here.

        return true;
    }
}
