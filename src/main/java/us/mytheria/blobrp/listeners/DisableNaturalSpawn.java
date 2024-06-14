package us.mytheria.blobrp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.CreatureSpawnEvent;
import us.mytheria.blobrp.director.manager.ConfigManager;

import java.util.Set;
import java.util.stream.Collectors;

public class DisableNaturalSpawn extends RPListener {

    public DisableNaturalSpawn(ConfigManager configManager) {
        super(configManager);
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        if (getConfigManager().disableNaturalSpawn().register()) {
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
        }
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        Set<EntityType> disabled = getConfigManager().disableNaturalSpawn().value()
                .stream()
                .map(EntityType::valueOf)
                .collect(Collectors.toSet());
        if (!disabled.contains(event.getEntityType()))
            return;
        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL)
            return;
        event.setCancelled(true);
    }
}
