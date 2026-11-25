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
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class playerDataEditMenu implements Listener {

    private static final String MENU_TITLE = ChatColor.DARK_GRAY + "Player Data Edit";
    private static final int MENU_SIZE = 54;
    private static final int PLAYER_SLOTS_PER_PAGE = MENU_SIZE - 9;
    private static final int PREV_PAGE_SLOT = MENU_SIZE - 9 + 3;
    private static final int NEXT_PAGE_SLOT = MENU_SIZE - 9 + 5;

    public static void open(Player player) {
        open(player, 0);
    }

    static void open(Player player, int page) {
        List<Player> onlinePlayers = new ArrayList<Player>(Bukkit.getOnlinePlayers());

        int lastPage = Math.max(0, (onlinePlayers.size() - 1) / PLAYER_SLOTS_PER_PAGE);
        page = Math.max(0, Math.min(page, lastPage));

        MenuHolder holder = new MenuHolder(page);
        Inventory menu = Bukkit.createInventory(holder, MENU_SIZE, MENU_TITLE);
        holder.setMenu(menu);

        int start = page * PLAYER_SLOTS_PER_PAGE;
        int end = Math.min(start + PLAYER_SLOTS_PER_PAGE, onlinePlayers.size());
        for (int slot = 0; start + slot < end; slot++) {
            Player target = onlinePlayers.get(start + slot);
            holder.setTarget(slot, target.getUniqueId());
            menu.setItem(slot, createPlayerHead(target.getName()));
        }

        boolean hasPrevPage = page > 0;
        boolean hasNextPage = page < lastPage;
        menu.setItem(PREV_PAGE_SLOT, createNavButton("Previous Page", hasPrevPage));
        menu.setItem(NEXT_PAGE_SLOT, createNavButton("Next Page", hasNextPage));

        player.openInventory(menu);
    }

    static ItemStack createPlayerHead(String targetName) {
        ItemStack head = CompatItems.create("PLAYER_HEAD", "SKULL_ITEM", (short) 3);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwner(targetName);
        meta.setDisplayName(ChatColor.GREEN + targetName);
        head.setItemMeta(meta);
        return head;
    }

    private static ItemStack createNavButton(String name, boolean available) {
        ItemStack dye = CompatItems.createStatusDye(available);
        ItemMeta meta = dye.getItemMeta();
        meta.setDisplayName(available ? ChatColor.GREEN + name : ChatColor.GRAY + name);
        dye.setItemMeta(meta);
        return dye;
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
        Player player = (Player) event.getWhoClicked();

        if (event.getRawSlot() < PLAYER_SLOTS_PER_PAGE) {
            UUID targetId = holder.targetAt(event.getRawSlot());
            Player target = targetId != null ? Bukkit.getPlayer(targetId) : null;
            if (target != null) {
                playerActionMenu.open(player, targetId, target.getName(), holder.page);
            } else {
                open(player, holder.page);
            }
        } else if (event.getRawSlot() == PREV_PAGE_SLOT) {
            open(player, holder.page - 1);
        } else if (event.getRawSlot() == NEXT_PAGE_SLOT) {
            open(player, holder.page + 1);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (MenuHolder.from(event.getInventory()) != null) {
            event.setCancelled(true);
        }
    }

    private static class MenuHolder implements InventoryHolder {

        private final int page;
        private final UUID[] targets = new UUID[PLAYER_SLOTS_PER_PAGE];
        private Inventory menu;

        private MenuHolder(int page) {
            this.page = page;
        }

        private static MenuHolder from(Inventory inventory) {
            return inventory != null && inventory.getHolder() instanceof MenuHolder
                    ? (MenuHolder) inventory.getHolder()
                    : null;
        }

        private void setMenu(Inventory menu) {
            this.menu = menu;
        }

        private void setTarget(int slot, UUID targetId) {
            targets[slot] = targetId;
        }

        private UUID targetAt(int slot) {
            return slot >= 0 && slot < targets.length ? targets[slot] : null;
        }

        @Override
        public Inventory getInventory() {
            return menu;
        }
    }
}
