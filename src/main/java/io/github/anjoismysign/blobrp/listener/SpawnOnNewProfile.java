package io.github.anjoismysign.blobrp.listener;

import io.github.anjoismysign.bloblib.events.ProfileLoadEvent;
import io.github.anjoismysign.blobrp.BlobRPAPI;
import io.github.anjoismysign.blobrp.director.manager.ConfigManager;
import io.github.anjoismysign.blobrp.entity.configuration.RoleplayWarpConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Nullable;

public class SpawnOnNewProfile extends RPListener {

    public SpawnOnNewProfile(ConfigManager configManager) {
        super(configManager);
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        if (getConfigManager().getSpawnOnNewProfile().register() && RoleplayWarpConfiguration.getInstance().isEnabled()) {
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
        }
    }

    @EventHandler
    public void onLoad(ProfileLoadEvent event) {
        var profile = event.getProfile();
        if (profile.hasPlayedBefore()){
            return;
        }
        @Nullable var warpAsSpawn = RoleplayWarpConfiguration.getInstance().roleplayWarpAsSpawn();
        if (warpAsSpawn == null){
            return;
        }
        BlobRPAPI.getInstance().getWarp(warpAsSpawn).teleport(event.getPlayer());
    }
}
