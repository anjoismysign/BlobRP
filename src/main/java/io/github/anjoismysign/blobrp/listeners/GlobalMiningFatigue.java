package io.github.anjoismysign.blobrp.listeners;

import io.github.anjoismysign.blobrp.director.manager.ConfigManager;
import io.github.anjoismysign.blobrp.events.SpectatorEndEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GlobalMiningFatigue extends RPListener {
    private final SpectatorRegistered registered;
    private final SpectatorNotRegistered notRegistered;
    private int level;
    private boolean isSpectatorRegistered;

    public GlobalMiningFatigue(ConfigManager configManager) {
        super(configManager);
        registered = new SpectatorRegistered();
        notRegistered = new SpectatorNotRegistered();
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        HandlerList.unregisterAll(registered);
        HandlerList.unregisterAll(notRegistered);
        isSpectatorRegistered = false;
        getConfigManager().globalSlowDigging().ifRegister(integerSimpleEventListener -> {
            this.level = integerSimpleEventListener.value();
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
        });
        getConfigManager().playerSpectateOnDeath().ifRegister(simpleEventListener -> {
            isSpectatorRegistered = true;
        });
        if (isSpectatorRegistered)
            Bukkit.getPluginManager().registerEvents(registered, getConfigManager().getPlugin());
        else
            Bukkit.getPluginManager().registerEvents(notRegistered, getConfigManager().getPlugin());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, Integer.MAX_VALUE,
                level - 1, false, false, false));
    }

    class SpectatorNotRegistered implements Listener {
        @EventHandler
        public void onRespawn(PlayerRespawnEvent event) {
            Player player = event.getPlayer();
            player.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, Integer.MAX_VALUE,
                    level - 1, false, false, false));
        }
    }

    class SpectatorRegistered implements Listener {
        @EventHandler
        public void onRespawn(SpectatorEndEvent event) {
            Player player = event.getPlayer();
            player.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, Integer.MAX_VALUE,
                    level - 1, false, false, false));
        }
    }
}
