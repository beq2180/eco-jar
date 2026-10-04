package com.example.aioeconomy;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public final class TradeHolder implements InventoryHolder {
    private final Player a, b;
    private final Inventory inv;

    public TradeHolder(Player a, Player b) {
        this.a = a; this.b = b;
        this.inv = Bukkit.createInventory(this, 54, "Trade: " + a.getName() + " ↔ " + b.getName());
    }

    public Player a() { return a; }
    public Player b() { return b; }
    public Inventory inventory() { return inv; }
    @Override public Inventory getInventory() { return inv; }
}
