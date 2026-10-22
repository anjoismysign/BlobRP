package io.github.anjoismysign.blobrp.listener;

import io.github.anjoismysign.bloblib.listener.SmartTinyListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

public class KeepExperienceOnDeath implements SmartTinyListener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setKeepLevel(true);
    }
}
