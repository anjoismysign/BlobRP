package us.mytheria.blobrp.entities;

import com.codisimus.plugins.phatloots.PhatLoot;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import us.mytheria.bloblib.entities.tag.TagSet;
import us.mytheria.bloblib.entities.translatable.TranslatableItem;
import us.mytheria.bloblib.exception.ConfigurationFieldException;

import java.util.Objects;

public record LockedPhatLootChest(
        @NotNull String getPhatLoot,
        @NotNull TagSet getAllowedKeys
) {

    public static LockedPhatLootChest READ(@NotNull ConfigurationSection parent) {
        Objects.requireNonNull(parent, "'parent' cannot be null");
        if (!parent.isString("PhatLoot"))
            throw new ConfigurationFieldException("'PhatLoot' is not set or valid");
        String phatLoot = parent.getString("PhatLoot");
        if (!parent.isString("Allowed-Keys"))
            throw new ConfigurationFieldException("'Allowed-Keys' is not set or valid");
        String allowedKeysTagSet = parent.getString("Allowed-Keys");
        TagSet allowedKeys = TagSet.by(allowedKeysTagSet);
        if (allowedKeys == null)
            throw new ConfigurationFieldException("'Allowed-Keys' doesn't point to a valid TagSet");
        return new LockedPhatLootChest(phatLoot, allowedKeys);
    }

    public boolean isKey(@NotNull TranslatableItem translatableItem,
                         @NotNull PhatLoot phatLoot) {
        Objects.requireNonNull(translatableItem, "'translatableItem' cannot be null");
        Objects.requireNonNull(translatableItem, "'translatableItem' cannot be null");
        String name = phatLoot.getName();
        String key = translatableItem.identifier();
        return getAllowedKeys.contains(key);
    }
}
