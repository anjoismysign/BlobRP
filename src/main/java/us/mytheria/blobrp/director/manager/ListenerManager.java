package us.mytheria.blobrp.director.manager;

import org.bukkit.Bukkit;
import us.mytheria.blobrp.director.RPManager;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.listeners.*;
import us.mytheria.blobrp.phatloots.PhatLootChestSmoothBreakAnimation;
import us.mytheria.blobrp.phatloots.PhatLootsHolograms;
import us.mytheria.blobrp.phatloots.TranslateOnPhatLoot;
import us.mytheria.blobrp.weaponmechanics.ApplyTranslatableItemsToWeaponMechanics;

import java.util.ArrayList;
import java.util.List;

public class ListenerManager extends RPManager {
    private final List<RPListener> listeners;

    public ListenerManager(RPManagerDirector managerDirector) {
        super(managerDirector);
        listeners = new ArrayList<>();
        ConfigManager configManager = managerDirector.getConfigManager();
        listeners.add(new DropNonSoulOnDeath(configManager));
        listeners.add(new EntitiesClearDropsOnDeath(configManager));
        listeners.add(new EntitiesDropExperienceOnDeath(configManager));
        listeners.add(new EntityDropItem(configManager));
        listeners.add(new BlockFade(configManager));
        listeners.add(new TranslateOnPickup(configManager));
        listeners.add(new TranslateOnLocaleSwitch(configManager));
        listeners.add(new TranslateOnJoin(configManager));
        listeners.add(new TranslateOnAlternativeSavingJoin(configManager));
        listeners.add(new KeepExperienceOnDeath(configManager));
        listeners.add(new PlayerDropExperienceOnDeath(configManager));
        listeners.add(new ShopArticleSell(configManager));
        listeners.add(new WelcomePlayer(configManager));
        listeners.add(new PlayerHunger(configManager));
        listeners.add(new IceFormation(configManager));
        listeners.add(new DisableNaturalSpawn(configManager));
        listeners.add(new PlayerSpectateOnDeath(configManager));
        listeners.add(new ForceGamemodeOnJoin(configManager));
        listeners.add(new GlobalSlowDigging(configManager));
        listeners.add(new OnJoinMessage(configManager));
        listeners.add(new OnQuitMessage(configManager));
        listeners.add(new DiscordCmd(configManager));
        listeners.add(new KillMessageWeapon(configManager));
        listeners.add(new PlayerDeathMessage(configManager));
        listeners.add(new RemoveJunk(configManager));
        listeners.add(new RespawnInventory(configManager));
        if (Bukkit.getPluginManager().isPluginEnabled("WeaponMechanics")) {
            listeners.add(new ApplyTranslatableItemsToWeaponMechanics(configManager));
        }
        if (Bukkit.getPluginManager().isPluginEnabled("PhatLoots")) {
            listeners.add(new PhatLootsHolograms(configManager));
            listeners.add(new TranslateOnPhatLoot(configManager));
            listeners.add(new PhatLootChestSmoothBreakAnimation(configManager));
        }
        reload();
    }

    @Override
    public void reload() {
        listeners.forEach(RPListener::reload);
    }
}
