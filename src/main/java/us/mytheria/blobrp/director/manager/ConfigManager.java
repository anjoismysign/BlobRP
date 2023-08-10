package us.mytheria.blobrp.director.manager;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import us.mytheria.bloblib.entities.ComplexEventListener;
import us.mytheria.bloblib.entities.SimpleEventListener;
import us.mytheria.bloblib.entities.TinyEventListener;
import us.mytheria.bloblib.managers.BlobPlugin;
import us.mytheria.blobrp.BlobRP;
import us.mytheria.blobrp.director.RPManager;
import us.mytheria.blobrp.director.RPManagerDirector;

import java.util.List;

public class ConfigManager extends RPManager {
    private final BlobPlugin main;
    private FileConfiguration configuration;

    private TinyEventListener dropNonSoulOnDeath;
    private TinyEventListener playerKeepExperienceOnDeath;
    private SimpleEventListener<Integer> playerDropExperienceOnDeath;
    private SimpleEventListener<Integer> entitiesDropExperienceOnDeath;
    private TinyEventListener entitiesClearDropsOnDeath;
    private TinyEventListener entityDropItem;
    private SimpleEventListener<Double> sellArticlesEvent;
    private SimpleEventListener<Boolean> sellArticlesListener;
    private SimpleEventListener<String> merchants;
    private SimpleEventListener<List<String>> merchantsView;
    private SimpleEventListener<String> welcomePlayers;
    private SimpleEventListener<Boolean> playerHunger;
    private SimpleEventListener<List<String>> iceFormation;
    private SimpleEventListener<String> forceGamemode;
    private SimpleEventListener<Integer> globalSlowDigging;
    private SimpleEventListener<String> onJoinMessage;
    private SimpleEventListener<String> onQuitMessage;
    private SimpleEventListener<String> killWeaponMessage;
    private SimpleEventListener<String> playerDeathMessage;

    private ComplexEventListener playerSpectateOnDeath;
    private ComplexEventListener alternativeSaving;
    private ComplexEventListener discordCmd;

    public ConfigManager(RPManagerDirector managerDirector) {
        super(managerDirector);
        main = BlobRP.getInstance();
        reload();
    }

    @Override
    public void reload() {
        main.reloadConfig();
        main.saveDefaultConfig();
        main.getConfig().options().copyDefaults(true);
        main.saveConfig();
        configuration = main.getConfig();
        ConfigurationSection listeners = configuration.getConfigurationSection("Listeners");
        ConfigurationSection complexListeners = configuration.getConfigurationSection("ComplexListeners");
        dropNonSoulOnDeath = TinyEventListener.READ(listeners, "Drop-Non-Soul-On-Death");
        playerKeepExperienceOnDeath = TinyEventListener.READ(listeners, "Player-Keep-Experience-On-Death");
        playerDropExperienceOnDeath = SimpleEventListener.INTEGER(listeners.getConfigurationSection("Player-Drop-Experience-On-Death"), "Amount");
        entitiesDropExperienceOnDeath = SimpleEventListener.INTEGER(listeners.getConfigurationSection("EntitiesDropExperienceOnDeath"), "Amount");
        entitiesClearDropsOnDeath = TinyEventListener.READ(listeners, "Entities-Clear-Drops-On-Death");
        entityDropItem = TinyEventListener.READ(listeners, "Entity-Drop-Item");
        sellArticlesEvent = SimpleEventListener.DOUBLE(listeners.getConfigurationSection("Sell-Articles-Event"), "Default-Price");
        sellArticlesListener = SimpleEventListener.BOOLEAN(listeners.getConfigurationSection("Manage-Sell-Articles"), "Permission-Multiplier");
        merchants = SimpleEventListener.STRING(listeners.getConfigurationSection("Merchants"), "Bought-Message");
        merchantsView = SimpleEventListener.STRING_LIST(listeners.getConfigurationSection("Merchants-View"), "Add");
        welcomePlayers = SimpleEventListener.STRING(listeners.getConfigurationSection("Welcome-Players"), "Message");
        playerHunger = SimpleEventListener.BOOLEAN(listeners.getConfigurationSection("Remove-Player-Hunger"), "Requires-Permission");
        iceFormation = SimpleEventListener.STRING_LIST(listeners.getConfigurationSection("Prevent-Ice-Formation"), "World-Whitelist");
        playerSpectateOnDeath = new ComplexEventListener(complexListeners.getConfigurationSection("Player-Spectate-On-Death"));
        forceGamemode = SimpleEventListener.STRING(listeners.getConfigurationSection("Force-Gamemode"), "Gamemode");
        alternativeSaving = new ComplexEventListener(complexListeners.getConfigurationSection("Alternative-Saving"));
        globalSlowDigging = SimpleEventListener.INTEGER(listeners.getConfigurationSection("Global-Slow-Digging"), "Level");
        onJoinMessage = SimpleEventListener.STRING(listeners.getConfigurationSection("On-Join-Message"), "Message");
        onQuitMessage = SimpleEventListener.STRING(listeners.getConfigurationSection("On-Quit-Message"), "Message");
        killWeaponMessage = SimpleEventListener.STRING(listeners.getConfigurationSection("Kill-Message-Weapon"), "Message");
        playerDeathMessage = SimpleEventListener.STRING(listeners.getConfigurationSection("Player-Death-Message"), "Message");
        discordCmd = new ComplexEventListener(complexListeners.getConfigurationSection("Discord-Cmd"));
    }

    public FileConfiguration getConfiguration() {
        return configuration;
    }

    public TinyEventListener dropNonSoulOnDeath() {
        return dropNonSoulOnDeath;
    }

    public TinyEventListener playerKeepExperienceOnDeath() {
        return playerKeepExperienceOnDeath;
    }

    public SimpleEventListener<Integer> playerDropExperienceOnDeath() {
        return playerDropExperienceOnDeath;
    }

    public SimpleEventListener<Integer> entitiesDropExperienceOnDeath() {
        return entitiesDropExperienceOnDeath;
    }

    public TinyEventListener entitiesClearDropsOnDeath() {
        return entitiesClearDropsOnDeath;
    }

    public TinyEventListener entityDropItem() {
        return entityDropItem;
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

    public SimpleEventListener<List<String>> merchantsView() {
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

    public ComplexEventListener playerSpectateOnDeath() {
        return playerSpectateOnDeath;
    }

    public SimpleEventListener<String> forceGamemode() {
        return forceGamemode;
    }

    public ComplexEventListener alternativeSaving() {
        return alternativeSaving;
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

    public ComplexEventListener discordCmd() {
        return discordCmd;
    }
}
