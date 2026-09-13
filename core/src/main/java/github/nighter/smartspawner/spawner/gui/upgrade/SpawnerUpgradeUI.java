package github.nighter.smartspawner.spawner.gui.upgrade;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.language.LanguageManager;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;
import java.util.Map;

public final class SpawnerUpgradeUI {
    public static final int MAX_LEVEL = 10;
    private static final double BASE_COST = 5_000_000D;
    private final SmartSpawner plugin;
    private final LanguageManager languageManager;
    public SpawnerUpgradeUI(SmartSpawner plugin) { this.plugin = plugin; this.languageManager = plugin.getLanguageManager(); }
    public double getUpgradeCost(int level) { return level >= MAX_LEVEL ? 0D : BASE_COST * Math.pow(4D, Math.max(0, level)); }
    public void open(Player player, SpawnerData spawner) {
        Inventory inv=Bukkit.createInventory(new SpawnerUpgradeHolder(spawner),27,"§8Spawner Upgrades");
        populate(inv,spawner); player.openInventory(inv);
    }
    public void refresh(Player player, SpawnerData spawner) {
        Inventory inv=player.getOpenInventory().getTopInventory();
        if (!(inv.getHolder(false) instanceof SpawnerUpgradeHolder h) || h.getSpawnerData()!=spawner) return;
        populate(inv,spawner);
    }
    private void populate(Inventory inv, SpawnerData spawner) {
        inv.clear(); int level=spawner.getUpgradeLevel(); int next=Math.min(MAX_LEVEL,level+1);
        inv.setItem(11,item(Material.NETHER_STAR,"spawner_upgrade_info",Map.of("level",String.valueOf(level),"max_level",String.valueOf(MAX_LEVEL))));
        inv.setItem(13,item(level>=MAX_LEVEL?Material.BEDROCK:Material.EMERALD,"spawner_upgrade_button",Map.of("level",String.valueOf(level),"next_level",String.valueOf(next),"cost",level>=MAX_LEVEL?"MAX":languageManager.formatNumber(getUpgradeCost(level)))));
        inv.setItem(15,stats(spawner,next));
        ItemStack close=new ItemStack(Material.BARRIER); close.editMeta(m->m.setDisplayName("§cClose")); inv.setItem(22,close);
    }
    private ItemStack item(Material material,String key,Map<String,String> p){
        ItemStack item=new ItemStack(material); ItemMeta meta=item.getItemMeta(); meta.setDisplayName(languageManager.getGuiItemName(key+".name",p)); meta.lore(languageManager.buildGuiLoreAsComponents(key+".lore",p,List.of(),null)); item.setItemMeta(meta); return item;
    }
    private ItemStack stats(SpawnerData s,int next){
        Map<String,String> p=Map.of("level",String.valueOf(s.getUpgradeLevel()),"next_level",String.valueOf(next),"mobs",String.valueOf(s.getMaxMobs()),"next_mobs",String.valueOf(projectMaxMobs(s,next)),"storage",languageManager.formatNumber(s.getMaxSpawnerLootSlots()),"next_storage",languageManager.formatNumber(projectStorage(s,next)),"delay",formatDelay(s.getSpawnDelay()),"next_delay",formatDelay(projectDelay(s,next)),"range",String.valueOf(s.getSpawnerRange()),"next_range",String.valueOf(projectRange(s,next)));
        return item(Material.DIAMOND,"spawner_upgrade_stats",p);
    }
    public int projectMaxMobs(SpawnerData s,int level){double r=(1+level*.15)/(1+s.getUpgradeLevel()*.15);return (int)Math.ceil(s.getMaxMobs()*r);}
    public int projectStorage(SpawnerData s,int level){double r=(1+level*.25)/(1+s.getUpgradeLevel()*.25);return (int)Math.floor(s.getMaxSpawnerLootSlots()*r);}
    public long projectDelay(SpawnerData s,int level){double c=Math.max(.5,1-s.getUpgradeLevel()*.05),n=Math.max(.5,1-level*.05);return Math.max(1,Math.round(s.getSpawnDelay()*n/c));}
    public int projectRange(SpawnerData s,int level){return Math.min(64,s.getSpawnerRange()+Math.max(0,level/2-s.getUpgradeLevel()/2));}
    private String formatDelay(long ticks){return String.format("%.1fs",ticks/20D);}
}
