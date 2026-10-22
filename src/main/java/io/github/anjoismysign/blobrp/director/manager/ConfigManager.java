package io.github.anjoismysign.blobrp.director.manager;

import io.github.anjoismysign.bloblib.domain.ConfigDecorator;
import io.github.anjoismysign.bloblib.listener.ComplexEventListener;
import io.github.anjoismysign.bloblib.listener.ListenersSection;
import io.github.anjoismysign.bloblib.listener.SimpleEventListener;
import io.github.anjoismysign.bloblib.listener.TinyEventListener;
import io.github.anjoismysign.bloblib.manager.BlobPlugin;
import io.github.anjoismysign.blobrp.BlobRP;
import io.github.anjoismysign.blobrp.director.RPManager;
import io.github.anjoismysign.blobrp.director.RPManagerDirector;
import io.github.anjoismysign.blobrp.entity.configuration.RoleplayConfiguration;
import io.github.anjoismysign.blobrp.ui.RoleplayUI;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class ConfigManager extends RPManager {
    private final BlobPlugin main;
    private final RoleplayConfiguration configuration;

    private TinyEventListener applyTranslatableItemsToWeaponMechanics;
    private TinyEventListener phatLootChestSmoothBreakAnimation;
    private TinyEventListener translateOnPhatLoot;

    private SimpleEventListener<Integer> playerDropExperienceOnDeath;
    private SimpleEventListener<Integer> entitiesDropExperienceOnDeath;
    private SimpleEventListener<Double> sellArticlesEvent;
    private SimpleEventListener<Boolean> sellArticlesListener;
    private SimpleEventListener<String> merchants;
    private SimpleEventListener<String> merchantsView;
    private SimpleEventListener<String> welcomePlayers;
    private SimpleEventListener<Boolean> playerHunger;
    private SimpleEventListener<List<String>> iceFormation;
    private SimpleEventListener<String> forceGamemode;
    private SimpleEventListener<Integer> globalSlowDigging;
    private SimpleEventListener<String> onJoinMessage;
    private SimpleEventListener<String> onQuitMessage;
    private SimpleEventListener<String> killWeaponMessage;
    private SimpleEventListener<String> playerDeathMessage;
    private SimpleEventListener<String> removeJunk;
    private SimpleEventListener<String> respawnInventory;
    private SimpleEventListener<List<String>> disableNaturalSpawn;

    private ComplexEventListener blobDesignCustomMining;
    private ComplexEventListener playerSpectateOnDeath;
    private ComplexEventListener discordCmd;
    private ComplexEventListener phatLootsHolograms;

    public ConfigManager(RPManagerDirector managerDirector) {
        super(managerDirector);
        main = BlobRP.getInstance();
        configuration = RoleplayConfiguration.getInstance();
        reload();
    }

    @Override
    public void reload() {
        ConfigDecorator configDecorator = main.getConfigDecorator();
        ListenersSection listenersSection = configDecorator.reloadAndGetListeners();
        ConfigurationSection settingsSection = configDecorator.reloadAndGetSection("Settings");
        configuration.reload(settingsSection);
        RoleplayUI.getInstance().reload();
        applyTranslatableItemsToWeaponMechanics = listenersSection.tinyEventListener("Apply-TranslatableItems-To-WeaponMechanics");
        phatLootChestSmoothBreakAnimation = listenersSection.tinyEventListener("PhatLootChest-Smooth-Break-Animation");
        translateOnPhatLoot = listenersSection.tinyEventListener("Translate-On-PhatLoot");

        playerDropExperienceOnDeath = listenersSection.simpleEventListenerInteger("Player-Drop-Experience-On-Death", "Amount");
        entitiesDropExperienceOnDeath = listenersSection.simpleEventListenerInteger("Entities-Drop-Experience-On-Death", "Amount");
        sellArticlesEvent = listenersSection.simpleEventListenerDouble("Sell-Articles-Event", "Default-Price");
        sellArticlesListener = listenersSection.simpleEventListenerBoolean("Manage-Sell-Articles", "Permission-Multiplier");
        merchants = listenersSection.simpleEventListenerString("Merchants", "Bought-Message");
        merchantsView = listenersSection.simpleEventListenerString("Merchants-View", "Add");
        welcomePlayers = listenersSection.simpleEventListenerString("Welcome-Players", "Message");
        playerHunger = listenersSection.simpleEventListenerBoolean("Remove-Player-Hunger", "Requires-Permission");
        iceFormation = listenersSection.simpleEventListenerStringList("Prevent-Ice-Formation", "World-Whitelist");
        forceGamemode = listenersSection.simpleEventListenerString("Force-Gamemode", "Gamemode");
        globalSlowDigging = listenersSection.simpleEventListenerInteger("Global-Slow-Digging", "Level");
        onJoinMessage = listenersSection.simpleEventListenerString("On-Join-Message", "Message");
        onQuitMessage = listenersSection.simpleEventListenerString("On-Quit-Message", "Message");
        killWeaponMessage = listenersSection.simpleEventListenerString("Kill-Message-Weapon", "Message");
        playerDeathMessage = listenersSection.simpleEventListenerString("Player-Death-Message", "Message");
        removeJunk = listenersSection.simpleEventListenerString("Remove-Junk", "TagSet");
        respawnInventory = listenersSection.simpleEventListenerString("Respawn-Inventory", "MetaBlobInventory");
        disableNaturalSpawn = listenersSection.simpleEventListenerStringList("Disable-Natural-Spawn", "Mobs");

        blobDesignCustomMining = listenersSection.complexEventListener("BlobDesign-Custom-Mining");
        playerSpectateOnDeath = listenersSection.complexEventListener("Player-Spectate-On-Death");
        discordCmd = listenersSection.complexEventListener("Discord-Cmd");
        phatLootsHolograms = listenersSection.complexEventListener("PhatLoots-Holograms");
    }

    public TinyEventListener applyTranslatableItemsToWeaponMechanics() {
        return applyTranslatableItemsToWeaponMechanics;
    }

    public TinyEventListener phatLootChestSmoothBreakAnimation() {
        return phatLootChestSmoothBreakAnimation;
    }

    public TinyEventListener translateOnPhatLoot() {
        return translateOnPhatLoot;
    }

    public SimpleEventListener<Integer> playerDropExperienceOnDeath() {
        return playerDropExperienceOnDeath;
    }

    public SimpleEventListener<Integer> entitiesDropExperienceOnDeath() {
        return entitiesDropExperienceOnDeath;
    }

    public SimpleEventListener<Double> sellArticlesEvent() {
        return sellArticlesEvent;
    }

    public SimpleEventListener<Boolean> manageSellArticles() {
        return sellArticlesListener;
    }

    public SimpleEventListener<String> merchants() {
        return merchants;
    }

    public SimpleEventListener<String> merchantsView() {
        return merchantsView;
    }

    public SimpleEventListener<String> welcomePlayers() {
        return welcomePlayers;
    }

    public SimpleEventListener<Boolean> playerHunger() {
        return playerHunger;
    }

    public SimpleEventListener<List<String>> iceFormation() {
        return iceFormation;
    }

    public SimpleEventListener<List<String>> disableNaturalSpawn() {
        return disableNaturalSpawn;
    }

    public SimpleEventListener<String> removeJunk() {
        return removeJunk;
    }

    public SimpleEventListener<String> respawnInventory() {
        return respawnInventory;
    }

    public SimpleEventListener<String> forceGamemode() {
        return forceGamemode;
    }

    public SimpleEventListener<Integer> globalSlowDigging() {
        return globalSlowDigging;
    }

    public SimpleEventListener<String> onJoinMessage() {
        return onJoinMessage;
    }

    public SimpleEventListener<String> onQuitMessage() {
        return onQuitMessage;
    }

    public SimpleEventListener<String> killWeaponMessage() {
        return killWeaponMessage;
    }

    public SimpleEventListener<String> playerDeathMessage() {
        return playerDeathMessage;
    }

    public ComplexEventListener blobDesignCustomMining() {
        return blobDesignCustomMining;
    }

    public ComplexEventListener playerSpectateOnDeath() {
        return playerSpectateOnDeath;
    }

    public ComplexEventListener discordCmd() {
        return discordCmd;
    }

    public ComplexEventListener phatLootsHolograms() {
        return phatLootsHolograms;
    }
}
