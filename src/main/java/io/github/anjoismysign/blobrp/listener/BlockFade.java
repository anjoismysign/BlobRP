package io.github.anjoismysign.blobrp.listener;

import io.github.anjoismysign.bloblib.listener.SmartTinyListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockFadeEvent;

public class BlockFade implements SmartTinyListener {

    @EventHandler
    public void onFade(BlockFadeEvent event) {
        event.setCancelled(true);
    }
}
