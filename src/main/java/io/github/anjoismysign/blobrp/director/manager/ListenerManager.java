package io.github.anjoismysign.blobrp.director.manager;

import io.github.anjoismysign.blobrp.director.RPManager;
import io.github.anjoismysign.blobrp.director.RPManagerDirector;
import io.github.anjoismysign.blobrp.listener.BlobDesignCustomMining;
import io.github.anjoismysign.blobrp.listener.BlockFade;
import io.github.anjoismysign.blobrp.listener.DefaultKitOnNewProfile;
import io.github.anjoismysign.blobrp.listener.DisableNaturalSpawn;
import io.github.anjoismysign.blobrp.listener.DiscordCmd;
import io.github.anjoismysign.blobrp.listener.DropNonSoulOnDeath;
import io.github.anjoismysign.blobrp.listener.EntitiesClearDropsOnDeath;
import io.github.anjoismysign.blobrp.listener.EntitiesDropExperienceOnDeath;
import io.github.anjoismysign.blobrp.listener.EntityDropItem;
import io.github.anjoismysign.blobrp.listener.ForceGamemodeOnJoin;
import io.github.anjoismysign.blobrp.listener.GlobalMiningFatigue;
import io.github.anjoismysign.blobrp.listener.HighestKitPriorityOnRespawn;
import io.github.anjoismysign.blobrp.listener.IceFormation;
import io.github.anjoismysign.blobrp.listener.KeepExperienceOnDeath;
import io.github.anjoismysign.blobrp.listener.KillMessageWeapon;
import io.github.anjoismysign.blobrp.listener.OnJoinMessage;
import io.github.anjoismysign.blobrp.listener.OnQuitMessage;
import io.github.anjoismysign.blobrp.listener.PlayerDeathMessage;
import io.github.anjoismysign.blobrp.listener.PlayerDropExperienceOnDeath;
import io.github.anjoismysign.blobrp.listener.PlayerHunger;
import io.github.anjoismysign.blobrp.listener.PlayerSpectateOnDeath;
import io.github.anjoismysign.blobrp.listener.RPListener;
import io.github.anjoismysign.blobrp.listener.RemoveJunk;
import io.github.anjoismysign.blobrp.listener.RespawnInventory;
import io.github.anjoismysign.blobrp.listener.ShopArticleSell;
import io.github.anjoismysign.blobrp.listener.SpawnOnNewProfile;
import io.github.anjoismysign.blobrp.listener.SpawnOnRespawn;
import io.github.anjoismysign.blobrp.listener.TranslateOnJoin;
import io.github.anjoismysign.blobrp.listener.TranslateOnLocaleSwitch;
import io.github.anjoismysign.blobrp.listener.TranslateOnPickup;
import io.github.anjoismysign.blobrp.listener.WelcomePlayer;
import io.github.anjoismysign.blobrp.phatloots.PhatLootChestSmoothBreakAnimation;
import io.github.anjoismysign.blobrp.phatloots.PhatLootsHolograms;
import io.github.anjoismysign.blobrp.phatloots.TranslateOnPhatLoot;
import io.github.anjoismysign.blobrp.weaponmechanics.ApplyTranslatableItemsToWeaponMechanics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.List;

public class ListenerManager extends RPManager {
    private final List<RPListener> listeners;

    public ListenerManager(RPManagerDirector managerDirector) {
        super(managerDirector);
        listeners = new ArrayList<>();
        ConfigManager configManager = managerDirector.getConfigManager();
        PluginManager pluginManager = Bukkit.getPluginManager();
        if (pluginManager.isPluginEnabled("BlobDesign")) {
            listeners.add(new BlobDesignCustomMining(configManager));
        }
        listeners.add(new DefaultKitOnNewProfile(configManager));
        listeners.add(new HighestKitPriorityOnRespawn(configManager));
        listeners.add(new SpawnOnNewProfile(configManager));
        listeners.add(new SpawnOnRespawn(configManager));
        listeners.add(new DropNonSoulOnDeath(configManager));
        listeners.add(new EntitiesClearDropsOnDeath(configManager));
        listeners.add(new EntitiesDropExperienceOnDeath(configManager));
        listeners.add(new EntityDropItem(configManager));
        listeners.add(new BlockFade(configManager));
        listeners.add(new TranslateOnPickup(configManager));
        listeners.add(new TranslateOnLocaleSwitch(configManager));
        listeners.add(new TranslateOnJoin(configManager));
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
