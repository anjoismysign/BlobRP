package io.github.anjoismysign.blobrp.listener;

import io.github.anjoismysign.bloblib.SoulAPI;
import io.github.anjoismysign.blobrp.director.manager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class DropNonSoulOnDeath extends RPListener {

    public DropNonSoulOnDeath(ConfigManager configManager) {
        super(configManager);
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        if (getConfigManager().dropNonSoulOnDeath().register())
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PlayerInventory inventory = player.getInventory();
        for (ItemStack stack : inventory.getContents()){
            if (stack == null){
                continue;
            }
            if (!stack.containsEnchantment(Enchantment.VANISHING_CURSE)){
                continue;
            }
            stack.setAmount(0);
        }
        for (ItemStack stack : inventory.getArmorContents()){
            if (stack == null){
                continue;
            }
            if (!stack.containsEnchantment(Enchantment.VANISHING_CURSE)){
                continue;
            }
            stack.setAmount(0);
        }
        SoulAPI.getInstance().dropAll(player);
    }
}
