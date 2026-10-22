package io.github.anjoismysign.blobrp.listener;

import io.github.anjoismysign.bloblib.api.BlobLibProfileAPI;
import io.github.anjoismysign.bloblib.listener.SmartTinyListener;
import io.github.anjoismysign.blobrp.BlobRP;
import io.github.anjoismysign.blobrp.entity.RoleplayKit;
import net.milkbowl.vault.profile.ProfileLoadEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

public class DefaultKitOnNewProfile implements SmartTinyListener {

    @EventHandler
    public void onLoad(ProfileLoadEvent event) {
        Player player = event.getPlayer();
        var provider = BlobLibProfileAPI.getInstance().getProvider();
        if (provider.hasProfilePlayedBefore(player, provider.getCurrentProfileIndex(player))) {
            return;
        }
        var kitManager = BlobRP.getInstance().getKitManager();
        Bukkit.getScheduler().runTaskLater(BlobRP.getInstance(), ()->{
            if (!player.isConnected()){
                return;
            }
            @Nullable var kit = kitManager
                    .stream()
                    .filter(roleplayKit -> player.hasPermission(roleplayKit.permission()))
                    .max(Comparator.comparingInt(RoleplayKit::priority))
                    .orElse(null);
            if (kit == null) {
                return;
            }
            kit.apply(player);
        }, 40);

    }
}
