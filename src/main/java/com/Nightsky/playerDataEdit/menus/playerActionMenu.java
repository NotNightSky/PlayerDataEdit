package com.Nightsky.playerDataEdit.menus;

import com.Nightsky.playerDataEdit.utils.CompatItems;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

public class playerActionMenu implements Listener {

    private static final int MENU_SIZE = 18;
    private static final int HEAD_SLOT = 4;
    private static final int EDIT_NBT_SLOT = 11;
    private static final int STATUS_SLOT = 13;
    private static final int BACK_SLOT = 15;

    public static void open(Player viewer, UUID targetId, String targetName, int returnPage) {
        MenuHolder holder = new MenuHolder(returnPage);
        Inventory menu = Bukkit.createInventory(holder, MENU_SIZE, ChatColor.DARK_GRAY + "Edit: " + targetName);
        holder.setMenu(menu);

        menu.setItem(HEAD_SLOT, playerDataEditMenu.createPlayerHead(targetName));

        ItemStack editNbt = CompatItems.create("BOOK", "BOOK");
        ItemMeta editNbtMeta = editNbt.getItemMeta();
        editNbtMeta.setDisplayName(ChatColor.AQUA + "Edit NBT");
        editNbtMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "Edits this player's saved data.",
                ChatColor.GRAY + "Available while the player is online or offline."));
        editNbt.setItemMeta(editNbtMeta);
        menu.setItem(EDIT_NBT_SLOT, editNbt);

        boolean online = Bukkit.getPlayer(targetId) != null;
        ItemStack status = CompatItems.createStatusDye(online);
        ItemMeta statusMeta = status.getItemMeta();
        statusMeta.setDisplayName(online ? ChatColor.GREEN + "Online" : ChatColor.GRAY + "Offline");
        statusMeta.setLore(Collections.singletonList(ChatColor.GRAY + (online
                ? "This player is currently on the server."
                : "This player's data is stored on disk.")));
        status.setItemMeta(statusMeta);
        menu.setItem(STATUS_SLOT, status);

        ItemStack back = CompatItems.create("ARROW", "ARROW");
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(ChatColor.GREEN + "Back");
        backMeta.setLore(Collections.singletonList(ChatColor.GRAY + "Returns to the player list."));
        back.setItemMeta(backMeta);
        menu.setItem(BACK_SLOT, back);

        viewer.openInventory(menu);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        MenuHolder holder = MenuHolder.from(event.getInventory());
        if (holder == null) {
            return;
        }

        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player viewer = (Player) event.getWhoClicked();

        if (event.getRawSlot() == EDIT_NBT_SLOT) {
            viewer.sendMessage(ChatColor.YELLOW + "The NBT editor is not implemented yet.");
        } else if (event.getRawSlot() == BACK_SLOT) {
            playerDataEditMenu.open(viewer, holder.returnPage);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (MenuHolder.from(event.getInventory()) != null) {
            event.setCancelled(true);
        }
    }

    private static class MenuHolder implements InventoryHolder {

        private final int returnPage;
        private Inventory menu;

        private MenuHolder(int returnPage) {
            this.returnPage = returnPage;
        }

        private static MenuHolder from(Inventory inventory) {
            return inventory != null && inventory.getHolder() instanceof MenuHolder
                    ? (MenuHolder) inventory.getHolder()
                    : null;
        }

        private void setMenu(Inventory menu) {
            this.menu = menu;
        }

        @Override
        public Inventory getInventory() {
            return menu;
        }
    }
}
