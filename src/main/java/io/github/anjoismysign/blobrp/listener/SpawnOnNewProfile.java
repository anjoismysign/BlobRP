package io.github.anjoismysign.blobrp.listener;

import io.github.anjoismysign.bloblib.api.BlobLibProfileAPI;
import io.github.anjoismysign.bloblib.listener.SmartTinyListener;
import io.github.anjoismysign.blobrp.BlobRPAPI;
import io.github.anjoismysign.blobrp.entity.configuration.RoleplayWarpConfiguration;
import net.milkbowl.vault.profile.ProfileLoadEvent;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.Nullable;

public class SpawnOnNewProfile implements SmartTinyListener {

    @EventHandler
    public void onLoad(ProfileLoadEvent event) {
        if (!RoleplayWarpConfiguration.getInstance().isEnabled()){
            return;
        }
        var player = event.getPlayer();
        var provider = BlobLibProfileAPI.getInstance().getProvider();
        if (provider.hasProfilePlayedBefore(player, provider.getCurrentProfileIndex(player))){
            return;
        }
        @Nullable var warpAsSpawn = RoleplayWarpConfiguration.getInstance().roleplayWarpAsSpawn();
        if (warpAsSpawn == null){
            return;
        }
        BlobRPAPI.getInstance().getWarp(warpAsSpawn).teleport(event.getPlayer());
    }
}
