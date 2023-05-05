package us.mytheria.blobrp.listeners;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import us.mytheria.bloblib.entities.SimpleEventListener;
import us.mytheria.bloblib.utilities.ItemStackUtil;
import us.mytheria.bloblib.utilities.TextColor;
import us.mytheria.blobrp.director.manager.ConfigManager;

import java.util.List;

public class KillMessageWeapon extends RPListener {
    private SimpleEventListener<String> killMessageWeapon;
    private String message;

    public KillMessageWeapon(ConfigManager configManager) {
        super(configManager);
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        if (getConfigManager().killWeaponMessage().register()) {
            killMessageWeapon = getConfigManager().killWeaponMessage();
            message = killMessageWeapon.value();
            if (message.length() == 0)
                throw new IllegalArgumentException("Message cannot be empty!");
            message = TextColor.PARSE(message);

            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
        }
    }


    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onKill(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (entity.getType() != EntityType.PLAYER)
            return;
        Player victim = (Player) entity;
        if (event.getFinalDamage() < victim.getHealth())
            return;
        Entity damager = event.getDamager();
        if (damager.getType() != EntityType.PLAYER)
            return;
        Player attacker = (Player) damager;
        ItemStack activeItem = attacker.getInventory().getItemInMainHand();
        String message = this.message.replace("%attacker%", attacker.getName()).replace("%victim%", victim.getName());
        TextComponent msg = new TextComponent(message);
        if (activeItem == null || activeItem.getType().isAir()) {
            Bukkit.getOnlinePlayers().forEach(player ->
                    player.spigot().sendMessage(msg));
            return;
        }
        String displayName = ItemStackUtil.display(activeItem);
        ComponentBuilder componentBuilder = new ComponentBuilder(displayName);
        ItemMeta itemMeta = activeItem.getItemMeta();
        if (itemMeta != null && itemMeta.hasLore()) {
            List<String> lore = itemMeta.getLore();
            lore.forEach(line -> componentBuilder.append("\n" + line));
        }
        msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                componentBuilder.create()));
        Bukkit.getOnlinePlayers().forEach(player ->
                player.spigot().sendMessage(msg));
    }
}
