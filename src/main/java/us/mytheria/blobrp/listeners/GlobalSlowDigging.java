package us.mytheria.blobrp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import us.mytheria.blobrp.director.manager.ConfigManager;
import us.mytheria.blobrp.events.SpectatorEndEvent;

public class GlobalSlowDigging extends RPListener {
    private int level;
    private boolean isSpectatorRegistered;
    private final SpectatorRegistered registered;
    private final SpectatorNotRegistered notRegistered;

    public GlobalSlowDigging(ConfigManager configManager) {
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
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE,
                level - 1, false, false, false));
    }

    class SpectatorNotRegistered implements Listener {
        @EventHandler
        public void onRespawn(PlayerRespawnEvent event) {
            Player player = event.getPlayer();
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE,
                    level - 1, false, false, false));
        }
    }

    class SpectatorRegistered implements Listener {
        @EventHandler
        public void onRespawn(SpectatorEndEvent event) {
            Player player = event.getPlayer();
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE,
                    level - 1, false, false, false));
        }
    }
}
