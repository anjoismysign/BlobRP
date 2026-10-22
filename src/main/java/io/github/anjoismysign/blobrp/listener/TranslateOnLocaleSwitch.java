package io.github.anjoismysign.blobrp.listener;

import io.github.anjoismysign.bloblib.listener.SmartTinyListener;
import io.github.anjoismysign.bloblib.translatable.TranslatableItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerLocaleChangeEvent;
import org.bukkit.inventory.ItemStack;

public class TranslateOnLocaleSwitch implements SmartTinyListener {

    @EventHandler
    public void onSwitch(PlayerLocaleChangeEvent event) {
        Player player = event.getPlayer();
        String locale = event.getLocale();
        for (ItemStack stack : player.getInventory().getContents()) {
            TranslatableItem.localize(stack, locale);
        }
    }
}
