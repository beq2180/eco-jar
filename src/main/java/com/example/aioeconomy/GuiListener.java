package com.example.aioeconomy;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class GuiListener implements Listener {
    private final AIOEconomyPlugin plugin;
    public GuiListener(AIOEconomyPlugin plugin) { this.plugin = plugin; }

    @EventHandler
    public void click(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player p)) return;
        String title = e.getView().getTitle();

        if (title.equals("Sell GUI")) {
            if (e.getClickedInventory() == e.getView().getTopInventory() && e.getCurrentItem() != null) {
                e.setCancelled(true);
                ItemStack item = e.getCurrentItem();
                if (item.getType().isAir()) return;
                double price = sellPrice(item.getType());
                if (price <= 0) { p.sendMessage("§cNo sell price configured for " + item.getType()); return; }
                int amount = item.getAmount();
                p.getInventory().removeItem(item);
                plugin.economy().deposit(p.getUniqueId(), price * amount);
                p.sendMessage("§aSold " + amount + "x " + item.getType() + " for $" + money(price * amount));
                p.closeInventory();
            }
            return;
        }

        if (title.startsWith("Auction House")) {
            if (e.getClickedInventory() != e.getView().getTopInventory()) return;
            e.setCancelled(true);
            ItemStack item = e.getCurrentItem();
            if (item == null || item.getType().isAir()) return;
            ItemMeta meta = item.getItemMeta();
            if (meta == null || meta.getLore() == null) return;
            for (String line : meta.getLore()) {
                if (line.startsWith("§0ID:")) {
                    try {
                        var id = java.util.UUID.fromString(line.substring(5));
                        var listing = plugin.auction().get(id);
                        if (listing != null && plugin.auction().buy(p.getUniqueId(), id)) {
                            p.getInventory().addItem(listing.item());
                            p.sendMessage("§aPurchased the item for $" + money(listing.price()));
                            p.closeInventory();
                        } else p.sendMessage("§cYou cannot afford this listing or it no longer exists.");
                    } catch (Exception ignored) {}
                    break;
                }
            }
        }
    }

    private double sellPrice(Material m) {
        for (String section : plugin.shop().sections())
            for (var item : plugin.shop().items(section))
                if (item.material() == m) return item.sell();
        return 0;
    }

    static String money(double n) { return String.format("%.2f", n); }
}
