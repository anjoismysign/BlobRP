package io.github.anjoismysign.blobrp.listener;

import io.github.anjoismysign.bloblib.listener.SmartTinyListener;
import io.github.anjoismysign.bloblib.translatable.TranslatableItem;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class TranslateOnPickup implements SmartTinyListener {

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) {
            return;
        }
        Player player = (Player) event.getEntity();
        Item item = event.getItem();
        ItemStack stack = item.getItemStack();
        TranslatableItem.localize(stack, player.getLocale());
    }
}
