package us.mytheria.blobrp.listeners;

import me.anjoismysign.anjo.entities.Uber;
import net.milkbowl.vault.economy.IdentityEconomy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachmentInfo;
import us.mytheria.bloblib.BlobLibAPI;
import us.mytheria.bloblib.BlobLibAssetAPI;
import us.mytheria.bloblib.entities.SimpleEventListener;
import us.mytheria.bloblib.entities.inventory.BlobInventory;
import us.mytheria.blobrp.BlobRP;
import us.mytheria.blobrp.director.manager.ConfigManager;
import us.mytheria.blobrp.entities.ShopArticle;
import us.mytheria.blobrp.entities.ShopArticleTransaction;
import us.mytheria.blobrp.events.AsyncMultipleShopArticleSellEvent;
import us.mytheria.blobrp.events.TransactionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ShopArticleSell extends RPListener {
    private BlobInventory blobInventory;
    private SimpleEventListener<Double> sellArticlesEvent;
    private SimpleEventListener<Boolean> manageSellArticles;

    public ShopArticleSell(ConfigManager configManager) {
        super(configManager);
        reload();
    }

    public void reload() {
        blobInventory = BlobLibAssetAPI.getBlobInventory("Sell-Articles");
        if (blobInventory == null)
            throw new NullPointerException("Inventory not found");
        HandlerList.unregisterAll(this);
        if (getConfigManager().sellArticlesEvent().register() && getConfigManager().manageSellArticles().register()) {
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
            sellArticlesEvent = getConfigManager().sellArticlesEvent();
            manageSellArticles = getConfigManager().manageSellArticles();
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().equals(blobInventory.getTitle()))
            return;
        Inventory inventory = e.getClickedInventory();
        if (inventory == null)
            return;
        Set<Integer> slots = blobInventory.getSlots("Available");
        if (slots == null)
            throw new NullPointerException("'Available' slots not found");
        if (slots.contains(e.getRawSlot()) || inventory.getType() == InventoryType.PLAYER)
            return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!event.getView().getTitle().equals(blobInventory.getTitle()))
            return;
        Player player = (Player) event.getPlayer();
        Bukkit.getScheduler().runTaskAsynchronously(BlobRP.getInstance(), () -> {
            if (!player.isOnline())
                return;
            Set<Integer> slots = blobInventory.getSlots("Available");
            if (slots == null)
                throw new NullPointerException("'Available' slots not found");
            List<ShopArticleTransaction> transactionList = new ArrayList<>();
            for (int slot : slots) {
                ItemStack itemStack = event.getInventory().getItem(slot);
                if (itemStack == null)
                    continue;
                int amount = itemStack.getAmount();
                boolean matches = false;
                for (ShopArticle shopArticle : getConfigManager().getManagerDirector().getShopArticleDirector()
                        .getObjectManager().values()) {
                    if (!shopArticle.matches(itemStack)) {
                        continue;
                    }
                    transactionList.add(new ShopArticleTransaction(shopArticle, amount));
                    matches = true;
                    break;
                }
                if (!matches)
                    transactionList.add(new ShopArticleTransaction(ShopArticle
                            .fromItemStack(itemStack, sellArticlesEvent.value(),
                                    "null",
                                    sellArticlesEvent.value(), true,
                                    null, null), amount));
            }
            AsyncMultipleShopArticleSellEvent sellEvent = new AsyncMultipleShopArticleSellEvent(
                    transactionList, player, TransactionType.SELL);
            Bukkit.getPluginManager().callEvent(sellEvent);
            if (sellEvent.isCancelled())
                return;
            if (!manageSellArticles.register())
                return;
            Uber<Integer> interval = new Uber<>(1);
            transactionList.forEach(transaction -> {
                ShopArticle shopArticle = transaction.shopArticle();
                int amount = transaction.amount();
                final Uber<Double> sellPrice = new Uber<>(shopArticle.getSellPrice());
                if (manageSellArticles.value()) {
                    Uber<Double> multiplier = new Uber<>(1d);
                    player.getEffectivePermissions().stream()
                            .map(PermissionAttachmentInfo::getPermission)
                            .filter(permission -> permission.startsWith("blobrp.shoparticle.multiplier."))
                            .map(s -> s.split("\\."))
                            .filter(split -> split.length == 4)
                            .map(split -> split[3])
                            .mapToDouble(Double::parseDouble)
                            .forEach(floatingNumber -> multiplier.talk(
                                    multiplier.thanks() + floatingNumber));
                    sellPrice.talk(sellPrice.thanks() * multiplier.thanks());
                }
                double money = sellPrice.thanks() * amount;
                IdentityEconomy economy = BlobLibAPI.getElasticEconomy()
                        .map(shopArticle.getSellingCurrency());
                economy.deposit(player.getUniqueId(), money);
                Bukkit.getScheduler().runTaskLaterAsynchronously(BlobRP.getInstance(), () -> {
                    if (!player.isOnline())
                        return;
                    BlobLibAssetAPI.getSound("Reward.Cash").play(player);
                }, interval.thanks());
                interval.talk(interval.thanks() + 1);
            });
        });
    }
}
