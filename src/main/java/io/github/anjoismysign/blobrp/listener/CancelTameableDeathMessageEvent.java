package io.github.anjoismysign.blobrp.listener;

import io.github.anjoismysign.bloblib.listener.SmartTinyListener;
import io.papermc.paper.event.entity.TameableDeathMessageEvent;
import org.bukkit.event.EventHandler;

public class CancelTameableDeathMessageEvent implements SmartTinyListener {

    @EventHandler
    public void onMessage(TameableDeathMessageEvent event){
        event.setCancelled(true);
    }
}
