package io.github.anjoismysign.blobrp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerRespawnEvent;
import io.github.anjoismysign.bloblib.api.BlobLibInventoryAPI;
import io.github.anjoismysign.bloblib.entities.SimpleEventListener;
import io.github.anjoismysign.bloblib.entities.inventory.InventoryBuilderCarrier;
import io.github.anjoismysign.bloblib.entities.inventory.MetaBlobPlayerInventoryBuilder;
import io.github.anjoismysign.bloblib.entities.inventory.MetaInventoryButton;
import io.github.anjoismysign.blobrp.director.manager.ConfigManager;

public class RespawnInventory extends RPListener {
    private SimpleEventListener<String> respawnInventory;

    public RespawnInventory(ConfigManager configManager) {
        super(configManager);
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        if (getConfigManager().respawnInventory().register()) {
            respawnInventory = getConfigManager().respawnInventory();
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if (event.getRespawnReason() != PlayerRespawnEvent.RespawnReason.DEATH)
            return;
        Player player = event.getPlayer();
        InventoryBuilderCarrier<MetaInventoryButton> carrier =
                BlobLibInventoryAPI.getInstance()
                        .getMetaInventoryBuilderCarrier(respawnInventory.value(), player.getLocale());
        MetaBlobPlayerInventoryBuilder.fromInventoryBuilderCarrier
                (carrier, player.getUniqueId());
    }
}
