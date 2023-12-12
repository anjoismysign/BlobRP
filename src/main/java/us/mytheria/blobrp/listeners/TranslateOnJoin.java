package us.mytheria.blobrp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import us.mytheria.bloblib.entities.translatable.TranslatableItem;
import us.mytheria.blobrp.director.manager.ConfigManager;

public class TranslateOnJoin extends RPListener {

    public TranslateOnJoin(ConfigManager configManager) {
        super(configManager);
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        if (getConfigManager().translateOnJoin().register()) {
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String locale = player.getLocale();
        for (ItemStack stack : player.getInventory().getArmorContents()) {
            TranslatableItem translatableItem = TranslatableItem.isInstance(stack);
            if (translatableItem == null)
                continue;
            ItemStack to = translatableItem.localize(locale).getClone();
            stack.setType(to.getType());
            stack.setItemMeta(to.getItemMeta());
        }
        for (ItemStack stack : player.getInventory().getContents()) {
            TranslatableItem translatableItem = TranslatableItem.isInstance(stack);
            if (translatableItem == null)
                continue;
            ItemStack to = translatableItem.localize(locale).getClone();
            stack.setType(to.getType());
            stack.setItemMeta(to.getItemMeta());
        }
    }
}
