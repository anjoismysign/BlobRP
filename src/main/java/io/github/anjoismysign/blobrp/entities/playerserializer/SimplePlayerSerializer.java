package io.github.anjoismysign.blobrp.entities.playerserializer;

import io.github.anjoismysign.bloblib.entities.BlobCrudable;
import io.github.anjoismysign.bloblib.utilities.ItemStackUtil;
import io.github.anjoismysign.blobrp.BlobRP;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;
import java.util.function.Consumer;

public class SimplePlayerSerializer implements PlayerSerializer {
    @Override
    public BlobCrudable serialize(Player player) {
        Location location = player.getLocation();
        BlobCrudable crudable = new BlobCrudable(player.getUniqueId().toString());
        Document document = crudable.getDocument();
        document.put("Name", player.getName());
        document.put("UniqueId", player.getUniqueId().toString());
        document.put("Health", player.getHealth());
        document.put("MaxHealth", player.getAttribute(Attribute.MAX_HEALTH).getValue());
        document.put("FoodLevel", player.getFoodLevel());
        document.put("Saturation", player.getSaturation());
        document.put("Exhaustion", player.getExhaustion());
        document.put("Level", player.getLevel());
        document.put("Exp", player.getExp());
        document.put("TotalExperience", player.getTotalExperience());
        document.put("FallDistance", player.getFallDistance());
        document.put("FireTicks", player.getFireTicks());
        document.put("RemainingAir", player.getRemainingAir());
        document.put("MaximumAir", player.getMaximumAir());
        document.put("AllowFlight", player.getAllowFlight());
        document.put("Flying", player.isFlying());
        document.put("Gamemode", player.getGameMode().name());
        document.put("Location", location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getYaw() + "," + location.getPitch());
        document.put("Inventory", ItemStackUtil.itemStackArrayToBase64(player.getInventory().getContents()));
        document.put("Armor", ItemStackUtil.itemStackArrayToBase64(player.getInventory().getArmorContents()));
        document.put("HeldItemSlot", player.getInventory().getHeldItemSlot());
        document.put("HasPlayedBefore", true);
        return crudable;
    }

    @Override
    public void deserialize(Player player, BlobCrudable crudable, Consumer<Player> consumer) {
        double maxHealth = crudable.hasDouble("MaxHealth").orElse(20.0);
        double health = crudable.hasDouble("Health").orElse(20.0);
        int foodLevel = crudable.hasInteger("FoodLevel").orElse(20);
        float saturation = crudable.hasFloat("Saturation").orElse(player.getSaturation());
        float exhaustion = crudable.hasFloat("Exhaustion").orElse(player.getExhaustion());
        int level = crudable.hasInteger("Level").orElse(0);
        float exp = crudable.hasFloat("Exp").orElse(0.0f);
        int totalExperience = crudable.hasInteger("TotalExperience").orElse(0);
        float fallDistance = crudable.hasFloat("FallDistance").orElse(0.0f);
        int fireTicks = crudable.hasInteger("FireTicks").orElse(0);
        int remainingAir = crudable.hasInteger("RemainingAir").orElse(player.getRemainingAir());
        int maximumAir = crudable.hasInteger("MaximumAir").orElse(player.getMaximumAir());
        boolean allowFlight = crudable.hasBoolean("AllowFlight").orElse(false);
        boolean flying = crudable.hasBoolean("Flying").orElse(false);
        GameMode gamemode = crudable.hasString("Gamemode").map(GameMode::valueOf).orElse(player.getGameMode());
        Location location = crudable.hasString("Location").map(serialLocation -> {
            String[] split = serialLocation.split(",");
            if (split.length != 6)
                throw new RuntimeException("Does world name contains a comma? '" + serialLocation + "'");
            return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), Float.parseFloat(split[4]), Float.parseFloat(split[5]));
        }).orElse(null);
        ItemStack[] inventory = crudable.hasString("Inventory").map(ItemStackUtil::itemStackArrayFromBase64).orElse(null);
        ItemStack[] armor = crudable.hasString("Armor").map(ItemStackUtil::itemStackArrayFromBase64).orElse(null);
        int heldItemSlot = crudable.hasInteger("HeldItemSlot").orElse(player.getInventory().getHeldItemSlot());
        UUID uuid = player.getUniqueId();
        Bukkit.getScheduler().runTask(BlobRP.getInstance(), () -> {
            if (player != Bukkit.getPlayer(uuid))
                return;
            player.getAttribute(Attribute.MAX_HEALTH).setBaseValue(maxHealth);
            player.setHealth(health);
            player.setFoodLevel(foodLevel);
            player.setSaturation(saturation);
            player.setExhaustion(exhaustion);
            player.setLevel(level);
            player.setExp(exp);
            player.setTotalExperience(totalExperience);
            player.setFallDistance(fallDistance);
            player.setFireTicks(fireTicks);
            player.setRemainingAir(remainingAir);
            player.setMaximumAir(maximumAir);
            player.setAllowFlight(allowFlight);
            player.setFlying(flying);
            player.setGameMode(gamemode);
            if (location != null && location.getWorld() != null) {
                player.teleport(location);
            }
            PlayerInventory playerInventory = player.getInventory();
            if (inventory != null) {
                playerInventory.setContents(inventory);
            }
            if (armor != null) {
                playerInventory.setArmorContents(armor);
            }
            playerInventory.setHeldItemSlot(heldItemSlot);
            if (consumer != null)
                consumer.accept(player);
        });
    }
}