package io.github.anjoismysign.blobrp.director.manager;

import io.github.anjoismysign.blobrp.director.RPManager;
import io.github.anjoismysign.blobrp.director.RPManagerDirector;
import io.github.anjoismysign.blobrp.listeners.BlobDesignCustomMining;
import io.github.anjoismysign.blobrp.listeners.BlockFade;
import io.github.anjoismysign.blobrp.listeners.DisableNaturalSpawn;
import io.github.anjoismysign.blobrp.listeners.DiscordCmd;
import io.github.anjoismysign.blobrp.listeners.DropNonSoulOnDeath;
import io.github.anjoismysign.blobrp.listeners.EntitiesClearDropsOnDeath;
import io.github.anjoismysign.blobrp.listeners.EntitiesDropExperienceOnDeath;
import io.github.anjoismysign.blobrp.listeners.EntityDropItem;
import io.github.anjoismysign.blobrp.listeners.ForceGamemodeOnJoin;
import io.github.anjoismysign.blobrp.listeners.GlobalMiningFatigue;
import io.github.anjoismysign.blobrp.listeners.IceFormation;
import io.github.anjoismysign.blobrp.listeners.KeepExperienceOnDeath;
import io.github.anjoismysign.blobrp.listeners.KillMessageWeapon;
import io.github.anjoismysign.blobrp.listeners.OnJoinMessage;
import io.github.anjoismysign.blobrp.listeners.OnQuitMessage;
import io.github.anjoismysign.blobrp.listeners.PlayerDeathMessage;
import io.github.anjoismysign.blobrp.listeners.PlayerDropExperienceOnDeath;
import io.github.anjoismysign.blobrp.listeners.PlayerHunger;
import io.github.anjoismysign.blobrp.listeners.PlayerSpectateOnDeath;
import io.github.anjoismysign.blobrp.listeners.RPListener;
import io.github.anjoismysign.blobrp.listeners.RemoveJunk;
import io.github.anjoismysign.blobrp.listeners.RespawnInventory;
import io.github.anjoismysign.blobrp.listeners.ShopArticleSell;
import io.github.anjoismysign.blobrp.listeners.TranslateOnAlternativeSavingJoin;
import io.github.anjoismysign.blobrp.listeners.TranslateOnJoin;
import io.github.anjoismysign.blobrp.listeners.TranslateOnLocaleSwitch;
import io.github.anjoismysign.blobrp.listeners.TranslateOnPickup;
import io.github.anjoismysign.blobrp.listeners.WelcomePlayer;
import io.github.anjoismysign.blobrp.phatloots.PhatLootChestSmoothBreakAnimation;
import io.github.anjoismysign.blobrp.phatloots.PhatLootsHolograms;
import io.github.anjoismysign.blobrp.phatloots.TranslateOnPhatLoot;
import io.github.anjoismysign.blobrp.weaponmechanics.ApplyTranslatableItemsToWeaponMechanics;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class ListenerManager extends RPManager {
    private final List<RPListener> listeners;

    public ListenerManager(RPManagerDirector managerDirector) {
        super(managerDirector);
        listeners = new ArrayList<>();
        ConfigManager configManager = managerDirector.getConfigManager();
        if (Bukkit.getPluginManager().isPluginEnabled("BlobDesign"))
            listeners.add(new BlobDesignCustomMining(configManager));
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
        listeners.add(new GlobalMiningFatigue(configManager));
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
