package us.mytheria.blobrp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;
import us.mytheria.bloblib.api.BlobLibMessageAPI;
import us.mytheria.bloblib.entities.message.BlobMessage;
import us.mytheria.blobrp.director.manager.ConfigManager;

import javax.annotation.Nullable;
import java.util.Objects;

public class PlayerDeathMessage extends RPListener {
    private @Nullable BlobMessage message;

    public PlayerDeathMessage(ConfigManager configManager) {
        super(configManager);
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        if (getConfigManager().playerDeathMessage().register()) {
            String value = getConfigManager().playerDeathMessage().value();
            if (value.isEmpty())
                message = null;
            else
                message = Objects.requireNonNull(BlobLibMessageAPI.getInstance().getMessage(value), "Message not found: " + value);
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
        }
    }


    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (message == null)
            event.setDeathMessage(null);
        else
            message.modder()
                    .replace("%victim%", event.getEntity().getName())
                    .get()
                    .broadcast();
    }
}
