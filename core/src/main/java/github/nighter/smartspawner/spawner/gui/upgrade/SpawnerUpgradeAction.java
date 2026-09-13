package github.nighter.smartspawner.spawner.gui.upgrade;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.hooks.economy.currency.CurrencyManager;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public final class SpawnerUpgradeAction implements Listener {
    private final SmartSpawner plugin; private final SpawnerUpgradeUI ui;
    public SpawnerUpgradeAction(SmartSpawner plugin,SpawnerUpgradeUI ui){this.plugin=plugin;this.ui=ui;}
    @EventHandler public void onClick(InventoryClickEvent e){
        if(!(e.getWhoClicked() instanceof Player p))return;
        if(!(e.getInventory().getHolder(false) instanceof SpawnerUpgradeHolder h))return;
        e.setCancelled(true); if(e.getClickedInventory()==null||e.getRawSlot()<0||e.getRawSlot()>=e.getInventory().getSize())return;
        if(e.getRawSlot()==22){p.closeInventory();return;} if(e.getRawSlot()==13)purchase(p,h.getSpawnerData());
    }
    private void purchase(Player p,SpawnerData s){
        int level=s.getUpgradeLevel(); if(level>=SpawnerUpgradeUI.MAX_LEVEL){p.sendMessage("§cThis spawner is already fully upgraded.");return;}
        CurrencyManager currency=plugin.getItemPriceManager()==null?null:plugin.getItemPriceManager().getCurrencyManager();
        if(currency==null||!currency.isCurrencyAvailable()){p.sendMessage("§cVault 2.0 economy is not available. Upgrade purchases are disabled.");return;}
        double cost=ui.getUpgradeCost(level);
        if(!currency.withdraw(cost,p)){p.sendMessage("§cYou need §a$"+plugin.getLanguageManager().formatNumber(cost)+"§c to buy this upgrade.");p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_BASS,.8f,.7f);return;}
        s.setUpgradeLevel(level+1); plugin.getSpawnerStorage().markSpawnerModified(s.getSpawnerId());
        p.sendMessage("§aSpawner upgraded to level §e"+(level+1)+"§a for §2$"+plugin.getLanguageManager().formatNumber(cost)+"§a.");
        p.playSound(p.getLocation(),Sound.ENTITY_PLAYER_LEVELUP,.8f,1.2f); ui.refresh(p,s);
    }
}
