package us.mytheria.blobrp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import us.mytheria.bloblib.entities.translatable.TranslatableItem;
import us.mytheria.blobrp.director.manager.ConfigManager;
import us.mytheria.blobrp.events.CloudInventoryDeserializeEvent;

public class TranslateOnAlternativeSavingJoin extends RPListener {

    public TranslateOnAlternativeSavingJoin(ConfigManager configManager) {
        super(configManager);
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        if (getConfigManager().translateOnAlternativeSavingJoin().register()) {
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
        }
    }

    @EventHandler
    public void onDeserialize(CloudInventoryDeserializeEvent event) {
        event.ifStillOnline(player -> {
            String locale = player.getLocale();
            for (ItemStack stack : player.getInventory().getArmorContents()) {
                TranslatableItem translatableItem = TranslatableItem.isInstance(stack);
                if (translatableItem == null)
                    continue;
                ItemStack to = translatableItem.localize(locale).getClone();
                stack.setType(to.getType());
                stack.setItemMeta(to.getItemMeta());
            }
            for (ItemStack stack : player.getInventory().getContents()) {
                TranslatableItem translatableItem = TranslatableItem.isInstance(stack);
                if (translatableItem == null)
                    continue;
                ItemStack to = translatableItem.localize(locale).getClone();
                stack.setType(to.getType());
                stack.setItemMeta(to.getItemMeta());
            }
            Bukkit.getScheduler().runTask(getConfigManager().getPlugin(), () -> {
                if (!player.isValid() || !player.isOnline())
                    return;
                player.updateInventory();
            });
        });
    }
}
