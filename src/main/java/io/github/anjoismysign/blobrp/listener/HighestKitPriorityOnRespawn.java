package io.github.anjoismysign.blobrp.listener;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import io.github.anjoismysign.blobrp.director.manager.ConfigManager;
import io.github.anjoismysign.blobrp.entity.RoleplayKit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

public class HighestKitPriorityOnRespawn extends RPListener {

    public HighestKitPriorityOnRespawn(ConfigManager configManager) {
        super(configManager);
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        if (getConfigManager().getHighestKitPriorityOnRespawn().register()) {
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerPostRespawnEvent event) {
        Player player = event.getPlayer();
        var kitManager = getManagerDirector().getPlugin().getKitManager();
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
