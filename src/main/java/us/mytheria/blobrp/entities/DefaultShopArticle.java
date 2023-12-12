package us.mytheria.blobrp.entities;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import us.mytheria.bloblib.entities.DataAssetType;
import us.mytheria.bloblib.entities.translatable.Translatable;
import us.mytheria.bloblib.entities.translatable.TranslatableItem;

import java.util.Objects;
import java.util.function.Function;

public class DefaultShopArticle implements TranslatableItem {
    private final ItemStack itemStack;

    public static DefaultShopArticle of(@NotNull ItemStack itemStack) {
        Objects.requireNonNull(itemStack);
        return new DefaultShopArticle(itemStack);
    }

    private DefaultShopArticle(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public @NotNull ItemStack get() {
        return itemStack;
    }

    public @NotNull Translatable<ItemStack> modify(Function<String, String> function) {
        throw new UnsupportedOperationException("DefaultShopArticle does not support modification");
    }

    public String getReference() {
        throw new UnsupportedOperationException("DefaultShopArticle does not have a reference");
    }

    public DataAssetType getType() {
        return DataAssetType.TRANSLATABLE_ITEM;
    }

    @NotNull
    public String getLocale() {
        return "en_us";
    }
}
