package us.mytheria.blobrp.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Nullable;
import us.mytheria.bloblib.entities.message.BlobMessage;
import us.mytheria.bloblib.entities.message.ReferenceBlobMessage;
import us.mytheria.blobrp.entities.ShopArticleTransaction;

public class ShopArticleSellEvent extends ShopArticleTransactionEvent {
    private final ReferenceBlobMessage referenceSuccessMessage;
    private final ReferenceBlobMessage referenceNotEnoughMessage;
    private BlobMessage notEnoughMessage;
    private BlobMessage successMessage;
    private final double price;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public ShopArticleSellEvent(ShopArticleTransaction transaction, Player player,
                                TransactionType transactionType,
                                TransactionStatus transactionStatus,
                                boolean isAsync,
                                ReferenceBlobMessage referenceSuccessMessage,
                                ReferenceBlobMessage referenceNotEnoughMessage,
                                BlobMessage notEnoughMessage,
                                BlobMessage successMessage,
                                double price) {
        super(transaction, player, transactionType, transactionStatus, isAsync);
        this.notEnoughMessage = notEnoughMessage;
        this.successMessage = successMessage;
        this.price = price;
        this.referenceSuccessMessage = referenceSuccessMessage;
        this.referenceNotEnoughMessage = referenceNotEnoughMessage;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    /**
     * @return message to send to player if he doesn't have enough money
     */
    @Nullable
    public BlobMessage getNotEnoughMessage() {
        return notEnoughMessage;
    }

    /**
     * Will set not enough message money message.
     * If provided null, no message will be sent to player.
     *
     * @param notEnoughMessage message to send to player
     */
    public void setNotEnoughMessage(@Nullable BlobMessage notEnoughMessage) {
        this.notEnoughMessage = notEnoughMessage;
    }

    /**
     * @return message to send to player if transaction was successful
     */
    @Nullable
    public BlobMessage getSuccessMessage() {
        return successMessage;
    }

    /**
     * Will set success message.
     * If provided null, no message will be sent to player.
     *
     * @param successMessage message to send to player
     */
    public void setSuccessMessage(@Nullable BlobMessage successMessage) {
        this.successMessage = successMessage;
    }

    /**
     * @return price of the transaction
     */
    public double getPrice() {
        return price;
    }

    /**
     * @return message that's set inside config.yml to be used as default success message.
     */
    public ReferenceBlobMessage getReferenceSuccessMessage() {
        return referenceSuccessMessage;
    }

    /**
     * @return the default message to be used as not enough money message.
     */
    public ReferenceBlobMessage getReferenceNotEnoughMessage() {
        return referenceNotEnoughMessage;
    }
}
