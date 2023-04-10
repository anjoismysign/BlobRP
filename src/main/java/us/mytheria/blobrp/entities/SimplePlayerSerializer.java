package us.mytheria.blobrp.entities;

import org.bson.Document;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import us.mytheria.bloblib.entities.BlobCrudable;
import us.mytheria.bloblib.utilities.ItemStackUtil;
import us.mytheria.bloblib.utilities.SerializationLib;

public class SimplePlayerSerializer implements PlayerSerializer {
    @Override
    public BlobCrudable serialize(Player player) {
        BlobCrudable crudable = new BlobCrudable(player.getUniqueId().toString());
        Document document = crudable.getDocument();
        document.put("Name", player.getName());
        document.put("UniqueId", player.getUniqueId().toString());
        document.put("Health", player.getHealth());
        document.put("MaxHealth", player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
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
        document.put("Location", SerializationLib.serialize(player.getLocation()));
        document.put("Inventory", ItemStackUtil.itemStackArrayToBase64(player.getInventory().getContents()));
        document.put("Armor", ItemStackUtil.itemStackArrayToBase64(player.getInventory().getArmorContents()));
        return crudable;
    }

    @Override
    public void deserialize(Player player, BlobCrudable crudable) {
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(crudable.hasDouble("MaxHealth").orElse(20.0));
        player.setHealth(crudable.hasDouble("Health").orElse(20.0));
        player.setFoodLevel(crudable.hasInteger("FoodLevel").orElse(20));
        player.setSaturation(crudable.hasFloat("Saturation").orElse(5.0f));
        player.setExhaustion(crudable.hasFloat("Exhaustion").orElse(0.0f));
        player.setLevel(crudable.hasInteger("Level").orElse(0));
        player.setExp(crudable.hasFloat("Exp").orElse(0.0f));
        player.setTotalExperience(crudable.hasInteger("TotalExperience").orElse(0));
        player.setFallDistance(crudable.hasFloat("FallDistance").orElse(0.0f));
        player.setFireTicks(crudable.hasInteger("FireTicks").orElse(0));
        player.setRemainingAir(crudable.hasInteger("RemainingAir").orElse(20));
        player.setMaximumAir(crudable.hasInteger("MaximumAir").orElse(20));
        player.setAllowFlight(crudable.hasBoolean("AllowFlight").orElse(false));
        player.setFlying(crudable.hasBoolean("Flying").orElse(false));
        crudable.hasString("Gamemode").ifPresent(gamemode -> player.setGameMode(GameMode.valueOf(gamemode)));
        crudable.hasString("Location").ifPresent(location -> player.teleport(SerializationLib.deserializeLocation(location)));
        crudable.hasString("Inventory").ifPresent(inventory -> player.getInventory().setContents(ItemStackUtil.itemStackArrayFromBase64(inventory)));
        crudable.hasString("Armor").ifPresent(armor -> player.getInventory().setArmorContents(ItemStackUtil.itemStackArrayFromBase64(armor)));
    }
}