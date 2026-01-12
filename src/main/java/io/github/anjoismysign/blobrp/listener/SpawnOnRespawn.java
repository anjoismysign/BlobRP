package io.github.anjoismysign.blobrp.listener;

import io.github.anjoismysign.blobrp.BlobRPAPI;
import io.github.anjoismysign.blobrp.director.manager.ConfigManager;
import io.github.anjoismysign.blobrp.entity.Spectator;
import io.github.anjoismysign.blobrp.entity.configuration.RoleplayWarpConfiguration;
import io.github.anjoismysign.blobrp.event.SpectatorStartEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.jetbrains.annotations.Nullable;

public class SpawnOnRespawn extends RPListener {

    public SpawnOnRespawn(ConfigManager configManager) {
        super(configManager);
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        if (getConfigManager().getSpawnOnRespawn().register() && RoleplayWarpConfiguration.getInstance().isEnabled()) {
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        @Nullable var warpAsSpawn = RoleplayWarpConfiguration.getInstance().roleplayWarpAsSpawn();
        if (warpAsSpawn == null){
            return;
        }
        BlobRPAPI.getInstance().getWarp(warpAsSpawn).teleport(event.getPlayer());
    }
}
