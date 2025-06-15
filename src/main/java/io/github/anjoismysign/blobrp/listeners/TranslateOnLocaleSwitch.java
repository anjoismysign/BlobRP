package io.github.anjoismysign.blobrp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerLocaleChangeEvent;
import org.bukkit.inventory.ItemStack;
import io.github.anjoismysign.bloblib.entities.translatable.TranslatableItem;
import io.github.anjoismysign.blobrp.director.manager.ConfigManager;

public class TranslateOnLocaleSwitch extends RPListener {

    public TranslateOnLocaleSwitch(ConfigManager configManager) {
        super(configManager);
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        if (getConfigManager().translateOnLocaleSwitch().register()) {
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
        }
    }

    @EventHandler
    public void onSwitch(PlayerLocaleChangeEvent event) {
        Player player = event.getPlayer();
        String locale = event.getLocale();
        for (ItemStack stack : player.getInventory().getContents()) {
            TranslatableItem.localize(stack, locale);
        }
    }
}
