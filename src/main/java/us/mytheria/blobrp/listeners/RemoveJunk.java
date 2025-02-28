package us.mytheria.blobrp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;
import us.mytheria.bloblib.entities.SimpleEventListener;
import us.mytheria.bloblib.entities.tag.TagSet;
import us.mytheria.bloblib.entities.translatable.TranslatableItem;
import us.mytheria.blobrp.director.manager.ConfigManager;

public class RemoveJunk extends RPListener {
    private SimpleEventListener<String> removeJunk;

    public RemoveJunk(ConfigManager configManager) {
        super(configManager);
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        if (getConfigManager().removeJunk().register()) {
            removeJunk = getConfigManager().removeJunk();
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
        }
    }

    @EventHandler
    public void onSpawn(ItemSpawnEvent event) {
        ItemStack stack = event.getEntity().getItemStack();
        TranslatableItem translatableItem = TranslatableItem.isInstance(stack);
        if (translatableItem == null)
            return;
        String key = translatableItem.identifier();
        TagSet tagSet = TagSet.by(removeJunk.value());
        if (!tagSet.contains(key))
            return;
        event.setCancelled(true);
    }
}
