package us.mytheria.blobrp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;
import us.mytheria.bloblib.api.BlobLibMessageAPI;
import us.mytheria.blobrp.director.manager.ConfigManager;

import javax.annotation.Nullable;

public class PlayerDeathMessage extends RPListener {
    private @Nullable String message;
    private boolean isValid;

    public PlayerDeathMessage(ConfigManager configManager) {
        super(configManager);
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        if (getConfigManager().playerDeathMessage().register()) {
            String value = getConfigManager().playerDeathMessage().value();
            if (value.isEmpty())
                message = null;
            else {
                message = value;
                isValid = true;
                if (BlobLibMessageAPI.getInstance().getMessage(value, "en_us") == null) {
                    getConfigManager().getPlugin().getAnjoLogger().singleError("BlobMessage '" + value + "' doesn't exist");
                    isValid = false;
                }
            }
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
        }
    }


    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        if (isValid)
            BlobLibMessageAPI.getInstance().broadcast(message, modder ->
                    modder.replace("%victim%", event.getEntity().getName()));
    }
}
