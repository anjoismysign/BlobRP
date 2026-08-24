package io.github.anjoismysign.blobrp.listener;

import io.github.anjoismysign.blobrp.director.manager.ConfigManager;
import io.papermc.paper.event.entity.TameableDeathMessageEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

public class CancelTameableDeathMessageEvent extends RPListener {

    public CancelTameableDeathMessageEvent(ConfigManager configManager) {
        super(configManager);
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        if (getConfigManager().getCancelTameableDeathMessageEvent().register()) {
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
        }
    }

    @EventHandler
    public void onMessage(TameableDeathMessageEvent event){
        event.setCancelled(true);
    }
}
