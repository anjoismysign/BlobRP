package us.mytheria.blobrp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import us.mytheria.bloblib.BlobLibAssetAPI;
import us.mytheria.bloblib.entities.inventory.BlobInventory;
import us.mytheria.blobrp.BlobRP;
import us.mytheria.blobrp.entities.ShopArticle;
import us.mytheria.blobrp.events.AsyncShopArticleSoldEvent;

import java.util.Set;

public class ArticleSell implements Listener {
    private final BlobRP main;
    private BlobInventory blobInventory;
    private final double defaultBuyPrice;

    public ArticleSell(double defaultBuyPrice) {
        this.main = BlobRP.getInstance();
        Bukkit.getPluginManager().registerEvents(this, main);
        reload();
        this.defaultBuyPrice = defaultBuyPrice;
    }

    public void reload() {
        blobInventory = BlobLibAssetAPI.getBlobInventory("Sell-Articles");
        if (blobInventory == null)
            throw new NullPointerException("Inventory not found");
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().equals(blobInventory.getTitle()))
            return;
        Inventory inventory = e.getClickedInventory();
        if (inventory == null)
            return;
        Set<Integer> slots = blobInventory.getSlots("Available");
        if (slots.contains(e.getRawSlot()) || inventory.getType() == InventoryType.PLAYER)
            return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (!e.getView().getTitle().equals(blobInventory.getTitle()))
            return;
        Player player = (Player) e.getPlayer();
        Bukkit.getScheduler().runTaskAsynchronously(BlobRP.getInstance(), () -> {
            if (!player.isOnline())
                return;
            Set<Integer> slots = blobInventory.getSlots("Available");
            if (slots == null)
                throw new NullPointerException("'Available' slots not found");
            for (int slot : slots) {
                ItemStack itemStack = e.getInventory().getItem(slot);
                if (itemStack == null)
                    continue;
                for (ShopArticle shopArticle : main.getManagerDirector().getShopArticleDirector()
                        .getObjectManager().values()) {
                    if (!shopArticle.matches(itemStack)) {
                        AsyncShopArticleSoldEvent event = new AsyncShopArticleSoldEvent(
                                ShopArticle.fromItemStack(defaultBuyPrice, itemStack));
                        if (event.isCancelled())
                            continue;
                        Bukkit.getPluginManager().callEvent(event);
                        continue;
                    }
                    AsyncShopArticleSoldEvent event = new AsyncShopArticleSoldEvent(shopArticle);
                    if (event.isCancelled())
                        continue;
                    Bukkit.getPluginManager().callEvent(event);
                }
            }
        });
    }
}
