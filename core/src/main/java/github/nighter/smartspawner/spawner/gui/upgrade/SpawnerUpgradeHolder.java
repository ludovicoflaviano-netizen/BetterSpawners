package github.nighter.smartspawner.spawner.gui.upgrade;

import github.nighter.smartspawner.spawner.gui.SpawnerHolder;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import lombok.Getter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

@Getter
public final class SpawnerUpgradeHolder implements InventoryHolder, SpawnerHolder {
    private final SpawnerData spawnerData;
    public SpawnerUpgradeHolder(SpawnerData spawnerData) { this.spawnerData = spawnerData; }
    @Override public Inventory getInventory() { return null; }
}
