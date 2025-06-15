package io.github.anjoismysign.blobrp.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import io.github.anjoismysign.blobrp.entities.ShopArticle;

public class ShopArticleSaleFailEvent extends Event {
    @NotNull
    private final ShopArticle shopArticle;
    @NotNull
    private final Player player;
    @Nullable
    private final String currency;
    private final double amount;
    private boolean fix;

    public ShopArticleSaleFailEvent(@NotNull ShopArticle shopArticle,
                                    @NotNull Player player,
                                    @Nullable String currency,
                                    double amount) {
        this.shopArticle = shopArticle;
        this.player = player;
        this.currency = currency;
        this.amount = amount;
        this.fix = false;
    }

    private static final HandlerList HANDLERS_LIST = new HandlerList();


    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    /**
     * The article that was attempted to be sold.
     *
     * @return The article.
     */
    public @NotNull ShopArticle getShopArticle() {
        return shopArticle;
    }

    /**
     * The player who attempted to sell the article.
     *
     * @return The player.
     */
    public @NotNull Player getPlayer() {
        return player;
    }

    /**
     * The currency with which the sale was attempted.
     *
     * @return The currency. Null if default currency.
     */
    public @Nullable String getCurrency() {
        return currency;
    }

    /**
     * The amount of currency that was attempted to be sold.
     *
     * @return The amount of currency.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Whether this sale is marked as fixed.
     * If sale is fixed, it means that BlobRP will attempt to re-sell the article one last time.
     *
     * @return true if the sale is fixed, false otherwise.
     */
    public boolean isFixed() {
        return fix;
    }

    /**
     * Mark this sale as fixed.
     *
     * @param fixed true if the sale is fixed, false otherwise.
     */
    public void setFixed(boolean fixed) {
        this.fix = fixed;
    }
}
