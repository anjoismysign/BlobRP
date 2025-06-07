package us.mytheria.blobrp.phatloots;

import com.codisimus.plugins.phatloots.events.ChestOpenEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import us.mytheria.bloblib.entities.translatable.TranslatableItem;
import us.mytheria.blobrp.director.manager.ConfigManager;
import us.mytheria.blobrp.listeners.RPListener;

public class TranslateOnPhatLoot extends RPListener {

    public TranslateOnPhatLoot(ConfigManager configManager) {
        super(configManager);
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        if (getConfigManager().translateOnPhatLoot().register())
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
    }

    @EventHandler
    public void onUnrestock(ChestOpenEvent event) {
        Player player = event.getPlayer();
        Inventory inventory = event.getInventory();
        ItemStack[] contents = inventory.getContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack itemStack = contents[i];
            TranslatableItem translatableItem = TranslatableItem.byItemStack(itemStack);
            if (translatableItem == null)
                continue;
            translatableItem = translatableItem.localize(player);
            ItemStack clone = translatableItem.getClone();
            clone.setAmount(itemStack.getAmount());
            inventory.setItem(i, clone);
        }
    }
}
