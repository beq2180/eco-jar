package com.example.aioeconomy;

import org.bukkit.entity.Player;
import java.util.*;

public final class TradeManager {
    private final AIOEconomyPlugin plugin;
    private final EconomyManager economy;
    private final Map<UUID, UUID> requests = new HashMap<>();

    public TradeManager(AIOEconomyPlugin plugin, EconomyManager economy) {
        this.plugin = plugin; this.economy = economy;
    }

    public void request(Player from, Player to) {
        requests.put(to.getUniqueId(), from.getUniqueId());
        to.sendMessage("§e" + from.getName() + " wants to trade with you.");
        to.sendMessage("§7Use §f/trade " + from.getName() + " §7to accept.");
    }

    public boolean hasRequest(Player receiver, Player sender) {
        return Objects.equals(requests.get(receiver.getUniqueId()), sender.getUniqueId());
    }

    public void clear(Player p) { requests.remove(p.getUniqueId()); }

    public void open(Player a, Player b) {
        TradeHolder holder = new TradeHolder(a, b);
        // Prototype UI: shared inventory. Full per-player trade states are planned next.
        a.openInventory(holder.inventory());
        b.openInventory(holder.inventory());
    }
}
