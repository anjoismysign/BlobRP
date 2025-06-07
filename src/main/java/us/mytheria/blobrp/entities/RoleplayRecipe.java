package us.mytheria.blobrp.entities;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import us.mytheria.bloblib.api.BlobLibInventoryAPI;
import us.mytheria.bloblib.api.BlobLibMessageAPI;
import us.mytheria.bloblib.entities.BlobObject;
import us.mytheria.bloblib.entities.inventory.InventoryBuilderCarrier;
import us.mytheria.bloblib.entities.inventory.InventoryButton;
import us.mytheria.bloblib.entities.inventory.InventoryDataRegistry;
import us.mytheria.bloblib.entities.inventory.SharableInventory;
import us.mytheria.bloblib.entities.message.BlobSound;
import us.mytheria.bloblib.entities.translatable.TranslatableItem;
import us.mytheria.bloblib.exception.ConfigurationFieldException;
import us.mytheria.bloblib.utilities.PlayerUtil;
import us.mytheria.blobrp.director.RPManagerDirector;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public record RoleplayRecipe(@NotNull String getKey,
                             @NotNull InventoryBuilderCarrier<InventoryButton> getCarrier,
                             @NotNull TranslatableItem getResult,
                             @NotNull Map<String, Integer> getIngredients,
                             @Nullable BlobSound getPickUpSound)
        implements BlobObject {

    public static RoleplayRecipe of(@NotNull RPManagerDirector director,
                                    @NotNull File file) {
        String key = file.getName().replace(".yml", "");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (!config.isString("BlobInventory"))
            throw new ConfigurationFieldException("'BlobInventory' is not set or valid");
        if (!config.isString("Result"))
            throw new ConfigurationFieldException("'Result' is not set or valid");
        String carrierKey = config.getString("BlobInventory");
        InventoryBuilderCarrier<InventoryButton> carrier = BlobLibInventoryAPI
                .getInstance().getInventoryBuilderCarrier(carrierKey);
        String resultKey = config.getString("Result");
        if (carrier == null)
            throw new ConfigurationFieldException("'BlobInventory' is doesn't point to a valid inventory");
        TranslatableItem translatableItem = TranslatableItem.by(resultKey);
        if (translatableItem == null) {
            director.getPlugin().getLogger().warning("'Result' doesn't point to a valid TranslatableItem. At: " + file.getPath());
            return null;
        }
        ConfigurationSection ingredientsSection = config.getConfigurationSection("Ingredients");
        if (ingredientsSection == null)
            throw new ConfigurationFieldException("'Ingredients' is not set or valid");
        Logger logger = director.getPlugin().getLogger();
        HashMap<String, Integer> ingredients = new HashMap<>();
        ingredientsSection.getKeys(false).forEach(ingredient -> {
            if (!ingredientsSection.isInt(ingredient))
                logger.severe("Invalid ingredient getAmount for " + ingredient + " in " + key + ".yml");
            int amount = ingredientsSection.getInt(ingredient);
            ingredients.put(ingredient, amount);
        });
        BlobSound sound = null;
        if (config.isString("PickUp-Sound")) {
            sound = BlobSound.by(config.getString("PickUp-Sound"));
            if (sound == null)
                logger.severe("Invalid 'PickUp-Sound' in " + key + ".yml");
        }
        RoleplayRecipe rolePlayRecipe = new RoleplayRecipe(
                key,
                carrier,
                translatableItem,
                ingredients,
                sound);
        NamespacedKey namespacedKey = director.createNamespacedKey("RoleplayRecipe");
        InventoryDataRegistry<InventoryButton> registry = BlobLibInventoryAPI.getInstance()
                .getInventoryDataRegistry(carrier.reference());
        registry.onClick("Crafting", inventoryClickEvent ->
                inventoryClickEvent.setCancelled(false));
        registry.onClose(namespacedKey.toString(), (event, inventory) -> {
            Player player = (Player) event.getPlayer();
            CraftResponse status = rolePlayRecipe.craft(player, inventory);
            if (status.getStatus() == CraftStatus.NOT_ENOUGH) {
                BlobLibMessageAPI.getInstance()
                        .getMessage("RoleplayRecipe.Not-Enough", player)
                        .handle(player);
                return;
            }
            if (!status.isSuccessful())
                return;
            ItemStack clone = translatableItem.localize(player).getClone();
            String name = clone.getType().name();
            if (clone.getItemMeta() != null
                    && clone.getItemMeta().hasDisplayName())
                name = clone.getItemMeta().getDisplayName();
            int amount = status.getAmount;
            clone.setAmount(amount);
            BlobLibMessageAPI.getInstance()
                    .getMessage("RoleplayRecipe.Crafted", player)
                    .modder()
                    .replace("%item%", name)
                    .get()
                    .handle(player);
            PlayerUtil.giveItemToInventoryOrDrop(player, clone);
            if (rolePlayRecipe.getPickUpSound != null)
                rolePlayRecipe.getPickUpSound.handle(player);
        });
        return rolePlayRecipe;
    }

    /**
     * Opens the inventory for the player.
     *
     * @param player The player to open the inventory for.
     */
    public void open(@NotNull Player player) {
        Objects.requireNonNull(player, "'player' cannot be null");
        BlobLibInventoryAPI.getInstance().trackInventory(player, getCarrier.reference())
                .getInventory().open(player);
    }

    private CraftResponse craft(@NotNull Player player,
                                @NotNull SharableInventory<?> inventory) {
        InventoryButton button = inventory.getButton("Crafting");
        if (button == null) {
            Bukkit.getLogger().severe(getCarrier.reference() + " is missing a 'Crafting' button.");
            return CraftResponse.ERROR();
        }
        List<ItemStack> itemStacks = button.getSlots()
                .stream()
                .map(slot -> inventory.getInventory().getItem(slot))
                .toList();
        // items to refund
        List<ItemStack> refund = new ArrayList<>();
        boolean isDirty = false;
        Map<String, Integer> remainingIngredients = new HashMap<>();
        Map<String, Integer> quotientIngredients = new HashMap<>();
        getIngredients.forEach((key, amount) -> {
            remainingIngredients.put(key, 0);
            quotientIngredients.put(key, 0);
        });
        Map<String, ItemStack> representations = new HashMap<>();
        for (ItemStack itemStack : itemStacks) {
            if (itemStack == null)
                continue;
            if (itemStack.getType() == Material.AIR)
                continue;
            TranslatableItem item = TranslatableItem.byItemStack(itemStack);
            String key = item != null ?
                    IngredientType.TRANSLATABLE_ITEM.getStartsWith() + item.identifier() :
                    IngredientType.MATERIAL.getStartsWith() + itemStack.getType().name();
            ItemStack clone = new ItemStack(itemStack);
            Integer requiredAmount = getIngredients.get(key);
            if (requiredAmount == null) {
                refund.add(clone);
                continue;
            }
            isDirty = true;

            int inputAmount = clone.getAmount();
            // the remainders of fitting the inputAmount into requiredAmount
            int remainder = inputAmount % requiredAmount;
            // how many times inputAmount fits into amount
            int quotient = inputAmount / requiredAmount;

            representations.put(key, clone);
            quotientIngredients.put(key, quotientIngredients.getOrDefault(key, 0) + quotient);
            remainingIngredients.put(key, remainingIngredients.getOrDefault(key, 0) + remainder);
        }
        remainingIngredients.forEach((key, amount) -> {
            if (amount < 1)
                return;
            Integer requiredAmount = getIngredients.get(key);
            int quotientAmount = quotientIngredients.getOrDefault(key, 0);
            int remainingAmount = amount % requiredAmount;
            quotientAmount += amount / requiredAmount;
            quotientIngredients.put(key, quotientAmount);
            if (remainingAmount > 0) {
                ItemStack clone = new ItemStack(representations.get(key));
                clone.setAmount(remainingAmount);
                refund.add(clone);
            }
        });
        Collection<Integer> quotientIngredientsAmount = quotientIngredients.values();
        int canCraft = quotientIngredientsAmount.isEmpty() ? 0 : Collections.min(quotientIngredientsAmount);
        // quotientIngredients are in units of requiredAmount
        // canCraft is in units of requiredAmount
        quotientIngredients.forEach((key, amount) -> {
            if (amount < 1)
                return;
            int requiredAmount = getIngredients.get(key);
            int refundTotal = (amount - canCraft) * requiredAmount;
            ItemStack clone = new ItemStack(representations.get(key));
            clone.setAmount(refundTotal);
            PlayerUtil.giveItemToInventoryOrDrop(player, clone);
            if (getPickUpSound != null)
                getPickUpSound.handle(player);
        });
        refund.forEach(itemStack -> {
            PlayerUtil.giveItemToInventoryOrDrop(player, itemStack);
            if (getPickUpSound != null)
                getPickUpSound.handle(player);
        });
        if (canCraft == 0)
            if (isDirty)
                return CraftResponse.NOT_ENOUGH();
            else
                return CraftResponse.NO_ACTION();
        return CraftResponse.SUCCESSFUL(canCraft);
    }

    public record CraftResponse(@NotNull CraftStatus getStatus,
                                int getAmount) {
        public boolean isSuccessful() {
            return getStatus == CraftStatus.SUCCESSFUL;
        }

        public static CraftResponse ERROR() {
            return new CraftResponse(CraftStatus.ERROR, 0);
        }

        public static CraftResponse NO_ACTION() {
            return new CraftResponse(CraftStatus.NO_ACTION, 0);
        }

        public static CraftResponse NOT_ENOUGH() {
            return new CraftResponse(CraftStatus.NOT_ENOUGH, 0);
        }

        public static CraftResponse SUCCESSFUL(int amount) {
            return new CraftResponse(CraftStatus.SUCCESSFUL, amount);
        }
    }

    public enum CraftStatus {
        SUCCESSFUL,
        NOT_ENOUGH,
        NO_ACTION,
        ERROR;
    }

    @Override
    public File saveToFile(File directory) {
        File file = instanceFile(directory);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("BlobInventory", getCarrier.reference());
        config.set("Result", getResult.identifier());
        ConfigurationSection ingredientsSection = config.createSection("Ingredients");
        getIngredients.forEach(ingredientsSection::set);
        if (getPickUpSound != null)
            config.set("PickUp-Sound", getPickUpSound.identifier());
        try {
            config.save(file);
        } catch ( Exception exception ) {
            exception.printStackTrace();
        }
        return file;
    }
}
