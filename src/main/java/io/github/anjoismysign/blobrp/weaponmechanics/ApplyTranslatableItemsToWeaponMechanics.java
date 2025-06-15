package io.github.anjoismysign.blobrp.weaponmechanics;

import me.deecaad.weaponmechanics.weapon.weaponevents.WeaponEquipEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import io.github.anjoismysign.bloblib.entities.translatable.TranslatableItem;
import io.github.anjoismysign.blobrp.director.manager.ConfigManager;
import io.github.anjoismysign.blobrp.listeners.RPListener;

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
        if (TranslatableItem.byItemStack(hand) != null)
            return;
        TranslatableItem translatableItem = TranslatableItem
                .byItemStack(hand);
        if (translatableItem == null)
            return;
        Player player = (Player) shooter;
        translatableItem.apply(hand, player);
    }
}
