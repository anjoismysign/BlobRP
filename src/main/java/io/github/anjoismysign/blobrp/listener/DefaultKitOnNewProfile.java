package io.github.anjoismysign.blobrp.listener;

import io.github.anjoismysign.bloblib.events.ProfileLoadEvent;
import io.github.anjoismysign.blobrp.BlobRP;
import io.github.anjoismysign.blobrp.director.manager.ConfigManager;
import io.github.anjoismysign.blobrp.entity.RoleplayKit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

public class DefaultKitOnNewProfile extends RPListener {

    public DefaultKitOnNewProfile(ConfigManager configManager) {
        super(configManager);
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        if (getConfigManager().getDefaultKitOnNewProfile().register()) {
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
        }
    }

    @EventHandler
    public void onLoad(ProfileLoadEvent event) {
        var profile = event.getProfile();
        if (profile.hasPlayedBefore()) {
            return;
        }
        Player player = event.getPlayer();
        var kitManager = getManagerDirector().getPlugin().getKitManager();
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
