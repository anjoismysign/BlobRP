package us.mytheria.blobrp.phatloots;

import com.codisimus.plugins.phatloots.events.ChestOpenEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import us.mytheria.blobrp.director.manager.ConfigManager;
import us.mytheria.blobrp.listeners.RPListener;

public class PhatLootChestSmoothBreakAnimation extends RPListener {

    public PhatLootChestSmoothBreakAnimation(ConfigManager configManager) {
        super(configManager);
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        if (getConfigManager().phatLootChestSmoothBreakAnimation().register())
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
    }

    @EventHandler(ignoreCancelled = true)
    public void onOpen(ChestOpenEvent event) {
        event.setCancelled(true);
        Block block = event.getChest().getBlock();
        World world = block.getWorld();
        Location location = block.getLocation().clone().add(0.5, 0.5, 0.5);
        Inventory inventory = event.getInventory();
        ItemStack[] contents = inventory.getContents();
        int d = 0;
        for (int i = 0; i < contents.length; i++) {
            ItemStack itemStack = contents[i];
            if (itemStack == null)
                continue;
            d++;
            inventory.setItem(i, null);
            Bukkit.getScheduler().runTaskLater(getConfigManager().getPlugin(), () -> {
                world.dropItem(location, itemStack);
                world.playSound(location, Sound.ENTITY_ITEM_PICKUP, 1f, 0.75f);
            }, d + 5);
        }
    }
}
