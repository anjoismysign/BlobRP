package us.mytheria.blobrp.weaponmechanics;

import me.deecaad.weaponmechanics.weapon.weaponevents.WeaponEquipEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import us.mytheria.bloblib.entities.translatable.TranslatableItem;
import us.mytheria.blobrp.director.manager.ConfigManager;
import us.mytheria.blobrp.listeners.RPListener;

public class ApplyTranslatableItemsToWeaponMechanics extends RPListener {

    public ApplyTranslatableItemsToWeaponMechanics(ConfigManager configManager) {
        super(configManager);
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        if (getConfigManager().applyTranslatableItemsToWeaponMechanics().register())
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
    }

    @EventHandler
    public void onEquip(WeaponEquipEvent event) {
        LivingEntity shooter = event.getShooter();
        if (shooter.getType() != EntityType.PLAYER)
            return;
        ItemStack hand = event.getWeaponStack();
        if (TranslatableItem.isInstance(hand) != null)
            return;
        TranslatableItem translatableItem = TranslatableItem
                .byMaterialAndCustomModelData(hand, "en_us");
        if (translatableItem == null)
            return;
        Player player = (Player) shooter;
        translatableItem.apply(hand, player);
    }
}
