package io.github.anjoismysign.blobrp.pressure;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import io.github.anjoismysign.blobrp.director.RPManager;
import io.github.anjoismysign.blobrp.director.RPManagerDirector;
import io.github.anjoismysign.blobrp.entities.configuration.PressureConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PressureManager extends RPManager implements Listener {
    private final Map<UUID, PlayerPressure> pressures;

    public PressureManager(RPManagerDirector managerDirector) {
        super(managerDirector);
        pressures = new HashMap<>();
        reload();
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        if (!PressureConfiguration.getInstance().isEnabled())
            return;
        Bukkit.getPluginManager().registerEvents(this, getPlugin());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        pressures.put(player.getUniqueId(), new PlayerPressure(player));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        @Nullable PlayerPressure pressure = pressures.remove(player.getUniqueId());
        if (pressure != null)
            pressure.cancel();
    }

    @Nullable
    public PlayerPressure getPlayerPressure(@NotNull UUID uuid) {
        return pressures.get(uuid);
    }
}
