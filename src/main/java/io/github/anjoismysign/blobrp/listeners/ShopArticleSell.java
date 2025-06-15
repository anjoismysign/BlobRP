package io.github.anjoismysign.blobrp.listeners;

import io.github.anjoismysign.anjo.entities.Uber;
import io.github.anjoismysign.bloblib.api.BlobLibEconomyAPI;
import io.github.anjoismysign.bloblib.api.BlobLibInventoryAPI;
import io.github.anjoismysign.bloblib.api.BlobLibSoundAPI;
import io.github.anjoismysign.bloblib.entities.SimpleEventListener;
import io.github.anjoismysign.bloblib.entities.inventory.InventoryButton;
import io.github.anjoismysign.bloblib.entities.inventory.InventoryDataRegistry;
import io.github.anjoismysign.bloblib.entities.translatable.TranslatableItem;
import io.github.anjoismysign.blobrp.director.manager.ConfigManager;
import io.github.anjoismysign.blobrp.entities.DefaultShopArticle;
import io.github.anjoismysign.blobrp.entities.ShopArticle;
import io.github.anjoismysign.blobrp.entities.ShopArticleTransaction;
import io.github.anjoismysign.blobrp.events.MultipleShopArticleSellEvent;
import io.github.anjoismysign.blobrp.events.TransactionType;
import net.milkbowl.vault.economy.IdentityEconomy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ShopArticleSell extends RPListener {
    private SimpleEventListener<Double> sellArticlesEvent;
    private SimpleEventListener<Boolean> manageSellArticles;

    public ShopArticleSell(ConfigManager configManager) {
        super(configManager);
        reload();
    }

    public void reload() {
        InventoryDataRegistry<InventoryButton> sellArticles = BlobLibInventoryAPI
                .getInstance().getInventoryDataRegistry("Sell-Articles");
        Objects.requireNonNull(sellArticles);
        sellArticles.onClose("BlobRP", (event, inventory) -> {
            Player player = (Player) event.getPlayer();
            Set<Integer> slots = inventory.getSlots("Available");
            Objects.requireNonNull(slots, "Slots button not found");
            List<ShopArticleTransaction> transactionList = new ArrayList<>();
            for (int slot : slots) {
                ItemStack itemStack = event.getInventory().getItem(slot);
                if (itemStack == null)
                    continue;
                int amount = itemStack.getAmount();
                boolean matches = false;
                for (ShopArticle shopArticle : getConfigManager().getManagerDirector().getShopArticleDirector()
                        .getObjectManager().values()) {
                    TranslatableItem display = shopArticle.getDisplay();
                    TranslatableItem instance = TranslatableItem.byItemStack(itemStack);
                    if (instance == null)
                        continue;
                    if (!display.identifier().equals(instance.identifier()))
                        continue;
                    transactionList.add(new ShopArticleTransaction(shopArticle, amount));
                    matches = true;
                    break;
                }
                if (!matches)
                    transactionList.add(new ShopArticleTransaction(ShopArticle
                            .fromTranslatableItem(DefaultShopArticle.of(itemStack),
                                    sellArticlesEvent.value(),
                                    "null",
                                    sellArticlesEvent.value(),
                                    true,
                                    null,
                                    null),
                            amount));
            }
            MultipleShopArticleSellEvent sellEvent = new MultipleShopArticleSellEvent(
                    transactionList, player, TransactionType.SELL);
            Bukkit.getPluginManager().callEvent(sellEvent);
            if (sellEvent.isCancelled())
                return;
            if (!manageSellArticles.register())
                return;
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
                IdentityEconomy economy = BlobLibEconomyAPI.getInstance().getElasticEconomy()
                        .map(shopArticle.getSellingCurrency());
                economy.deposit(player.getUniqueId(), money);
                BlobLibSoundAPI.getInstance().getSound("Reward.Cash").play(player);
            });
        });
        HandlerList.unregisterAll(this);
        if (getConfigManager().sellArticlesEvent().register() && getConfigManager().manageSellArticles().register()) {
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
            sellArticlesEvent = getConfigManager().sellArticlesEvent();
            manageSellArticles = getConfigManager().manageSellArticles();
        }
    }
}
