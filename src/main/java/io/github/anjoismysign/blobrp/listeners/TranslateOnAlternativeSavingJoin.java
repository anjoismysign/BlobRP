package io.github.anjoismysign.blobrp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import io.github.anjoismysign.bloblib.entities.translatable.TranslatableItem;
import io.github.anjoismysign.blobrp.director.manager.ConfigManager;
import io.github.anjoismysign.blobrp.events.CloudInventoryDeserializeEvent;

import java.util.UUID;

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
            UUID uuid = player.getUniqueId();
            String locale = player.getLocale();
            for (ItemStack stack : player.getInventory().getContents()) {
                TranslatableItem.localize(stack, locale);
            }
            Bukkit.getScheduler().runTask(getConfigManager().getPlugin(), () -> {
                if (player != Bukkit.getPlayer(uuid))
                    return;
                player.updateInventory();
            });
        });
    }
}
