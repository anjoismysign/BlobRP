package io.github.anjoismysign.blobrp.listener;

import io.github.anjoismysign.bloblib.listener.SmartTinyListener;
import io.github.anjoismysign.bloblib.translatable.TranslatableItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class TranslateOnJoin implements SmartTinyListener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String locale = player.getLocale();
        for (ItemStack stack : player.getInventory().getContents()) {
            TranslatableItem.localize(stack, locale);
        }
    }
}
