package io.github.anjoismysign.blobrp.phatloots;

import com.codisimus.plugins.phatloots.events.PrePlayerLootEvent;
import io.github.anjoismysign.blobrp.director.manager.ConfigManager;
import io.github.anjoismysign.blobrp.listener.RPListener;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

public class PhatLootChestRequiresKey extends RPListener {

    public PhatLootChestRequiresKey(ConfigManager configManager) {
        super(configManager);
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        if (getConfigManager().phatLootChestSmoothBreakAnimation().register())
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
    }

    @EventHandler
    public void onOpen(PrePlayerLootEvent event) {
//        Ph event.getPhatLoot();
    }
}
