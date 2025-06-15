package io.github.anjoismysign.blobrp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockFormEvent;
import io.github.anjoismysign.blobrp.director.manager.ConfigManager;

import java.util.HashSet;
import java.util.Set;

public class IceFormation extends RPListener {
    private Set<String> worldWhitelist;

    public IceFormation(ConfigManager configManager) {
        super(configManager);
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        if (getConfigManager().iceFormation().register()) {
            worldWhitelist = new HashSet<>(getConfigManager().iceFormation().value());
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
        }
    }

    @EventHandler
    public void onIceFormation(BlockFormEvent event) {
        String reference = event.getBlock().getWorld().getName();
        if (!worldWhitelist.contains(reference))
            return;
        Material type = event.getNewState().getType();
        if (type != Material.ICE)
            return;
        event.setCancelled(true);
    }
}
