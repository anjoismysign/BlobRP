package io.github.anjoismysign.blobrp.listeners;

import io.github.anjoismysign.anjo.entities.Uber;
import io.github.anjoismysign.bloblib.api.BlobLibMessageAPI;
import io.github.anjoismysign.bloblib.entities.logger.BlobPluginLogger;
import io.github.anjoismysign.bloblib.entities.message.BlobMessage;
import io.github.anjoismysign.blobrp.director.manager.ConfigManager;
import io.github.anjoismysign.blobrp.entities.Spectator;
import io.github.anjoismysign.blobrp.events.SpectatorStartEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.HashMap;
import java.util.Random;

public class PlayerSpectateOnDeath extends RPListener {
    private int length;
    private HashMap<Short, String> repository;

    public PlayerSpectateOnDeath(ConfigManager configManager) {
        super(configManager);
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        repository = new HashMap<>();
        BlobPluginLogger logger = getConfigManager().getPlugin().getAnjoLogger();
        Uber<Short> uber = Uber.drive((short) 0);
        getConfigManager().playerSpectateOnDeath().ifRegister(complexEventListener -> {
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
            PlayerSpectateOnDeath.this.length =
                    complexEventListener.getInt("Length");
            complexEventListener.getStringList("Blob-Messages").forEach(key -> {
                BlobMessage message = BlobLibMessageAPI.getInstance().getMessage(key);
                if (message == null) {
                    logger.error("Message " + key + " not found.");
                    return;
                }
                short current = uber.thanks();
                repository.put(current, key);
                current++;
                uber.talk(current);
            });
        });
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        GameRule<Boolean> immediateRespawn = GameRule.DO_IMMEDIATE_RESPAWN;
        Player player = event.getEntity();
        if (Boolean.TRUE.equals(player.getWorld().getGameRuleValue(immediateRespawn)))
            return;
        Bukkit.getScheduler().runTask(getConfigManager().getPlugin(), () ->
                player.spigot().respawn());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Location respawnLocation = event.getRespawnLocation();
        event.setRespawnLocation(player.getLocation());
        SpectatorStartEvent spectatorStartEvent = new SpectatorStartEvent(player, false);
        Bukkit.getPluginManager().callEvent(spectatorStartEvent);
        if (spectatorStartEvent.isCancelled()) return;
        new Spectator(player, length, respawnLocation);
        randomMessage().localize(player.getLocale()).handle(player);
    }

    private BlobMessage randomMessage() {
        short random = (short) new Random().nextInt(repository.size());
        return BlobLibMessageAPI.getInstance().getMessage(repository.get(random));
    }
}
