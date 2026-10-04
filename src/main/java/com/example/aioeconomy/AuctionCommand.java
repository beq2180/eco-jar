package com.example.aioeconomy;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.*;

public final class AuctionCommand implements CommandExecutor {
    private final AIOEconomyPlugin p;
    public AuctionCommand(AIOEconomyPlugin p){this.p=p;}

    public boolean onCommand(CommandSender s, Command c, String l, String[] a){
        if(!(s instanceof Player pl))return true;
        if(a.length>=1&&a[0].equalsIgnoreCase("sell")){
            ItemStack hand=pl.getInventory().getItemInMainHand();
            if(hand.getType().isAir()){pl.sendMessage("§cHold an item.");return true;}
            double price;
            try{price=a.length>=2?Double.parseDouble(a[1]):p.getConfig().getDouble("auction.default-price",100);}catch(Exception e){pl.sendMessage("§cInvalid price.");return true;}
            if(price<=0){pl.sendMessage("§cPrice must be positive.");return true;}
            p.auction().create(pl.getUniqueId(),hand,price);
            pl.getInventory().setItemInMainHand(null);
            pl.sendMessage("§aListed your item for $"+GuiListener.money(price)+".");
            return true;
        }
        String search=a.length>=2&&a[0].equalsIgnoreCase("search")?a[1]:"";
        Inventory inv=Bukkit.createInventory(null,54,"Auction House"+(search.isEmpty()?"":" — "+search));
        int slot=0;
        for(var x:p.auction().listings()){
            if(slot>=54)break;
            if(!search.isEmpty()&&!search.equalsIgnoreCase("hand")&&!x.item().getType().name().toLowerCase().contains(search.toLowerCase()))continue;
            if(search.equalsIgnoreCase("hand")&&!x.item().getType().equals(pl.getInventory().getItemInMainHand().getType()))continue;
            ItemStack display=x.item().clone();
            ItemMeta meta=display.getItemMeta();
            List<String> lore=meta.hasLore()?new ArrayList<>(meta.getLore()):new ArrayList<>();
            lore.add("§aPrice: $"+GuiListener.money(x.price()));
            lore.add("§7Seller: "+Bukkit.getOfflinePlayer(x.seller()).getName());
            lore.add("§0ID:"+x.id());
            meta.setLore(lore); display.setItemMeta(meta);
            inv.setItem(slot++,display);
        }
        pl.openInventory(inv); return true;
    }
}
