package io.github.anjoismysign.blobrp.phatloots;

import com.codisimus.plugins.phatloots.PhatLoot;
import com.codisimus.plugins.phatloots.PhatLootChest;
import com.codisimus.plugins.phatloots.PhatLoots;
import com.codisimus.plugins.phatloots.events.PreLootEvent;
import com.codisimus.plugins.phatloots.events.PrePlayerLootEvent;
import com.codisimus.plugins.phatloots.loot.CommandLoot;
import com.codisimus.plugins.phatloots.loot.LootBundle;
import io.github.anjoismysign.bloblib.api.BlobLibEconomyAPI;
import io.github.anjoismysign.blobrp.director.manager.ConfigManager;
import io.github.anjoismysign.blobrp.listener.RPListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.LinkedList;
import java.util.List;

public class PhatLootChestSmoothBreakAnimation extends RPListener {

    private boolean decimals;

    public PhatLootChestSmoothBreakAnimation(ConfigManager configManager) {
        super(configManager);
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        if (!getConfigManager().phatLootChestSmoothBreakAnimation().register()) {
            return;
        }
        FileConfiguration config = PhatLoots.plugin.getConfig();
        decimals = config.getBoolean("DivideMoneyAmountBy100");
        Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onOpen(PlayerInteractEvent event){
        if (!event.hasBlock() || event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        boolean looted = simulateLoot(block, player);
        if (!looted) {
            return;
        }
        event.setCancelled(true);
    }

    private boolean simulateLoot(Block block, Player player){
        LinkedList<PhatLoot> phatLoots = PhatLoots.getPhatLoots(block, player);
        if (phatLoots.isEmpty()) {
            return false;
        }
        if (!PhatLoots.plugin.getPluginHookManager().getWorldGuardManager().isRegionLootableIfOwnerOrMembers(block)) {
            return false;
        }
        World world = block.getWorld();
        Location spawnLocation = block.getLocation().clone().add(0.5, 0.5, 0.5);
        PhatLootChest chest = PhatLootChest.getChest(block);
        boolean looted = false;
        for (PhatLoot phatLoot : phatLoots) {
            PreLootEvent prePlayerLootEvent = new PrePlayerLootEvent(player, phatLoot, chest, 0);
            Bukkit.getPluginManager().callEvent(prePlayerLootEvent);
            if (prePlayerLootEvent.isCancelled()) {
                continue;
            }
            LootBundle lootBundle = phatLoot.rollForLoot(prePlayerLootEvent.getLootingBonus());

            double money = lootBundle.getMoney();
            money = Math.abs(money);
            if (money >= 0) {
                if (decimals) {
                    money /= 100;
                }
                BlobLibEconomyAPI.getInstance().getElasticEconomy().getDefault().depositPlayer(player, money);
            }

            int experience = lootBundle.getExp();
            if (experience > 0) {
                spawnExperienceOrbs(world, spawnLocation, experience);
            }

            for (CommandLoot command : lootBundle.getCommandList()) {
                command.execute(player);
            }

            for (String message : lootBundle.getMessageList()) {
                player.sendMessage(message);
            }

            List<ItemStack> itemList = lootBundle.getItemList().stream().toList();

            int delay = 0;
            for (ItemStack itemStack : itemList) {
                if (itemStack == null)
                    continue;
                delay++;
                Bukkit.getScheduler().runTaskLater(getConfigManager().getPlugin(), () -> {
                    Item item = player.getWorld().dropItemNaturally(spawnLocation, itemStack);
                    item.setVelocity(randomPopVelocity());
                    world.playSound(spawnLocation, Sound.ENTITY_ITEM_PICKUP, 1f, 0.75f);
                }, delay + 3);
            }

            looted = true;
            phatLoot.setTime(player, chest);
        }
        return looted;
    }

    private void spawnExperienceOrbs(World world, Location location, int experience) {
        int remaining = experience;
        while (remaining > 0) {
            int orbValue = getOrbValue(remaining);
            world.spawn(location, ExperienceOrb.class, orb -> orb.setExperience(orbValue));
            remaining -= orbValue;
        }
    }

    private int getOrbValue(int amount) {
        if (amount >= 2477) return 2477;
        if (amount >= 1237) return 1237;
        if (amount >= 617) return 617;
        if (amount >= 307) return 307;
        if (amount >= 149) return 149;
        if (amount >= 73) return 73;
        if (amount >= 37) return 37;
        if (amount >= 17) return 17;
        if (amount >= 7) return 7;
        if (amount >= 3) return 3;
        return 1;
    }

    private Vector randomPopVelocity() {
        double x = (Math.random() - 0.5) * 0.4;
        double z = (Math.random() - 0.5) * 0.4;
        double y = 0.2 + Math.random() * 0.2;
        return new Vector(x, y, z);
    }

}
