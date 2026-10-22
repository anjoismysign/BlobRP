package io.github.anjoismysign.blobrp.listener;

import io.github.anjoismysign.bloblib.listener.SmartTinyListener;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntitiesClearDropsOnDeath implements SmartTinyListener {

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (event.getEntity().getType() == EntityType.PLAYER)
            return;
        event.getDrops().clear();
    }
}
