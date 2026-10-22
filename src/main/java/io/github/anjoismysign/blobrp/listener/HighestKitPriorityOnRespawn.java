package io.github.anjoismysign.blobrp.listener;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import io.github.anjoismysign.bloblib.listener.SmartTinyListener;
import io.github.anjoismysign.blobrp.BlobRP;
import io.github.anjoismysign.blobrp.entity.RoleplayKit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

public class HighestKitPriorityOnRespawn implements SmartTinyListener {

    @EventHandler
    public void onPlayerRespawn(PlayerPostRespawnEvent event) {
        Player player = event.getPlayer();
        var kitManager = BlobRP.getInstance().getKitManager();
        @Nullable var kit = kitManager
                .stream()
                .filter(roleplayKit -> player.hasPermission(roleplayKit.permission()))
                .max(Comparator.comparingInt(RoleplayKit::priority))
                .orElse(null);
        if (kit == null){
            return;
        }
        kit.apply(player);
    }
}
