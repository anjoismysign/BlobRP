package us.mytheria.blobrp.entities.customblock;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import us.mytheria.bloblib.entities.tag.TagSet;
import us.mytheria.bloblib.entities.translatable.TranslatableItem;
import us.mytheria.bloblib.exception.ConfigurationFieldException;

import java.util.Objects;

public record CustomBlock(float getHardness,
                          @Nullable String requiresToolKey,
                          @Nullable String preferredToolKey) {

    /**
     * Reads a CustomBlock from a ConfigurationSection
     * It fails fast if the 'Hardness' field is missing or not a Double
     *
     * @param section the ConfigurationSection to read from
     * @return the CustomBlock
     */
    @NotNull
    public static CustomBlock READ(@NotNull ConfigurationSection section) {
        Objects.requireNonNull(section, "'section' cannot be null");
        if (!section.isDouble("Hardness"))
            throw new ConfigurationFieldException("'Hardness' field is missing or not a Double");
        float hardness = (float) section.getDouble("Hardness");
        String requiresToolKey = null;
        if (section.isString("Requires-Tool-TagSet"))
            requiresToolKey = section.getString("Requires-Tool-TagSet");
        String preferredToolKey = null;
        if (section.isString("Preferred-Tool-TagSet"))
            preferredToolKey = section.getString("Preferred-Tool-TagSet");
        return new CustomBlock(hardness, requiresToolKey, preferredToolKey);
    }

    /**
     * Gets the RequiresTool TagSet
     *
     * @return the RequiresTool TagSet
     */
    @Nullable
    public TagSet getRequiresTool() {
        if (requiresToolKey == null)
            return null;
        return TagSet.by(requiresToolKey);
    }

    /**
     * Gets the PreferredTool TagSet
     *
     * @return the PreferredTool TagSet
     */
    @Nullable
    public TagSet getPreferredTool() {
        if (preferredToolKey == null)
            return null;
        return TagSet.by(preferredToolKey);
    }

    /**
     * Checks if the given ItemStack can harvest this CustomBlock
     *
     * @param itemStack the ItemStack to check
     * @return true if the ItemStack can harvest this CustomBlock
     */
    public boolean canHarvest(@NotNull ItemStack itemStack) {
        if (requiresToolKey == null)
            return true;
        Objects.requireNonNull(itemStack, "'itemStack' cannot be null");
        TranslatableItem translatableItem = TranslatableItem.byItemStack(itemStack);
        if (translatableItem == null)
            return getRequiresTool().contains(itemStack.getType().toString());
        return getRequiresTool().contains(translatableItem.identifier());
    }

    /**
     * Checks if the given ItemStack is a preferred tool for this CustomBlock
     *
     * @param itemStack the ItemStack to check
     * @return true if the ItemStack is a preferred tool for this CustomBlock
     */
    public boolean isPreferredTool(@NotNull ItemStack itemStack) {
        if (preferredToolKey == null)
            return false;
        Objects.requireNonNull(itemStack, "'itemStack' cannot be null");
        TranslatableItem translatableItem = TranslatableItem.byItemStack(itemStack);
        if (translatableItem == null)
            return getPreferredTool().contains(itemStack.getType().toString());
        return getPreferredTool().contains(translatableItem.identifier());
    }
}
