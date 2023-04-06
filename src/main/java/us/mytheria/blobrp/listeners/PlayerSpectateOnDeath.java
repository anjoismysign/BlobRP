package us.mytheria.blobrp.listeners;

import me.anjoismysign.anjo.entities.Uber;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import us.mytheria.bloblib.BlobLibAssetAPI;
import us.mytheria.bloblib.entities.logger.BlobPluginLogger;
import us.mytheria.bloblib.entities.message.BlobMessage;
import us.mytheria.blobrp.director.manager.ConfigManager;
import us.mytheria.blobrp.entities.Spectator;

import java.util.HashMap;
import java.util.Random;

public class PlayerSpectateOnDeath extends RPListener {
    private int length;
    private HashMap<Short, BlobMessage> repository;

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
            complexEventListener.getStringList("BlobMessages").forEach(key -> {
                BlobMessage message = BlobLibAssetAPI.getMessage(key);
                if (message == null) {
                    logger.error("Message " + key + " not found.");
                    return;
                }
                short current = uber.thanks();
                repository.put(current, message);
                current++;
                uber.talk(current);
            });
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent event) {
        Bukkit.getScheduler().runTask(getConfigManager().getPlugin(), () ->
                event.getEntity().spigot().respawn());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        event.setRespawnLocation(player.getLocation());
        new Spectator(player, length);
        randomMessage().handle(player);
    }

    private BlobMessage randomMessage() {
        short random = (short) new Random().nextInt(repository.size());
        return repository.get(random);
    }
}
