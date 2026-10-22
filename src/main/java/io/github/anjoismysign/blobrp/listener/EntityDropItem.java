package io.github.anjoismysign.blobrp.listener;

import io.github.anjoismysign.bloblib.listener.SmartTinyListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDropItemEvent;

public class EntityDropItem implements SmartTinyListener {

    @EventHandler
    public void onSpawn(EntityDropItemEvent event) {
        event.setCancelled(true);
    }
}
