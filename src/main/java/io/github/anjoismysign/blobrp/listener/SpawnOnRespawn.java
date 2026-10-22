package io.github.anjoismysign.blobrp.listener;

import io.github.anjoismysign.bloblib.listener.SmartTinyListener;
import io.github.anjoismysign.blobrp.BlobRPAPI;
import io.github.anjoismysign.blobrp.entity.configuration.RoleplayWarpConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.jetbrains.annotations.Nullable;

public class SpawnOnRespawn implements SmartTinyListener {

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (!RoleplayWarpConfiguration.getInstance().isEnabled()){
            return;
        }
        @Nullable var warpAsSpawn = RoleplayWarpConfiguration.getInstance().roleplayWarpAsSpawn();
        if (warpAsSpawn == null){
            return;
        }
        BlobRPAPI.getInstance().getWarp(warpAsSpawn).teleport(event.getPlayer());
    }
}
