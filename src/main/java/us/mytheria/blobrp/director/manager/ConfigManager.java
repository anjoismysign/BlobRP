package us.mytheria.blobrp.director.manager;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import us.mytheria.bloblib.entities.ComplexEventListener;
import us.mytheria.bloblib.entities.SimpleEventListener;
import us.mytheria.bloblib.managers.BlobPlugin;
import us.mytheria.blobrp.BlobRP;
import us.mytheria.blobrp.director.RPManager;
import us.mytheria.blobrp.director.RPManagerDirector;

import java.util.List;

public class ConfigManager extends RPManager {
    private final BlobPlugin main;
    private FileConfiguration configuration;

    private SimpleEventListener<Boolean> dropNonSoulOnDeath;
    private SimpleEventListener<Boolean> playerKeepExperienceOnDeath;
    private SimpleEventListener<Integer> playerDropExperienceOnDeath;
    private SimpleEventListener<Integer> entitiesDropExperienceOnDeath;
    private SimpleEventListener<Boolean> entitiesClearDropsOnDeath;
    private SimpleEventListener<Boolean> entityDropItem;
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
        dropNonSoulOnDeath = SimpleEventListener.BOOLEAN(listeners.getConfigurationSection("DropNonSoulOnDeath"), "Drop");
        playerKeepExperienceOnDeath = SimpleEventListener.BOOLEAN(listeners.getConfigurationSection("PlayerKeepExperienceOnDeath"), "Keep");
        playerDropExperienceOnDeath = SimpleEventListener.INTEGER(listeners.getConfigurationSection("PlayerDropExperienceOnDeath"), "Amount");
        entitiesDropExperienceOnDeath = SimpleEventListener.INTEGER(listeners.getConfigurationSection("EntitiesDropExperienceOnDeath"), "Amount");
        entitiesClearDropsOnDeath = SimpleEventListener.BOOLEAN(listeners.getConfigurationSection("EntitiesClearDropsOnDeath"), "Clear");
        entityDropItem = SimpleEventListener.BOOLEAN(listeners.getConfigurationSection("EntityDropItem"), "Cancel");
        sellArticlesEvent = SimpleEventListener.DOUBLE(listeners.getConfigurationSection("SellArticlesEvent"), "DefaultPrice");
        sellArticlesListener = SimpleEventListener.BOOLEAN(listeners.getConfigurationSection("ManageSellArticles"), "PermissionMultiplier");
        merchants = SimpleEventListener.STRING(listeners.getConfigurationSection("Merchants"), "BoughtMessage");
        merchantsView = SimpleEventListener.STRING_LIST(listeners.getConfigurationSection("MerchantsView"), "Add");
        welcomePlayers = SimpleEventListener.STRING(listeners.getConfigurationSection("WelcomePlayers"), "Message");
        playerHunger = SimpleEventListener.BOOLEAN(listeners.getConfigurationSection("PlayerHunger"), "RequiresPermission");
        iceFormation = SimpleEventListener.STRING_LIST(listeners.getConfigurationSection("IceFormation"), "WorldWhitelist");
        playerSpectateOnDeath = new ComplexEventListener(complexListeners.getConfigurationSection("PlayerSpectateOnDeath"));
        forceGamemode = SimpleEventListener.STRING(listeners.getConfigurationSection("ForceGamemode"), "Gamemode");
        alternativeSaving = new ComplexEventListener(complexListeners.getConfigurationSection("AlternativeSaving"));
        globalSlowDigging = SimpleEventListener.INTEGER(listeners.getConfigurationSection("Global-Slow-Digging"), "Level");
        onJoinMessage = SimpleEventListener.STRING(listeners.getConfigurationSection("OnJoinMessage"), "Message");
        onQuitMessage = SimpleEventListener.STRING(listeners.getConfigurationSection("OnQuitMessage"), "Message");
        killWeaponMessage = SimpleEventListener.STRING(listeners.getConfigurationSection("KillMessageWeapon"), "Message");
        playerDeathMessage = SimpleEventListener.STRING(listeners.getConfigurationSection("PlayerDeathMessage"), "Message");
        discordCmd = new ComplexEventListener(complexListeners.getConfigurationSection("DiscordCmd"));
    }

    public FileConfiguration getConfiguration() {
        return configuration;
    }

    public SimpleEventListener<Boolean> dropNonSoulOnDeath() {
        return dropNonSoulOnDeath;
    }

    public SimpleEventListener<Boolean> playerKeepExperienceOnDeath() {
        return playerKeepExperienceOnDeath;
    }

    public SimpleEventListener<Integer> playerDropExperienceOnDeath() {
        return playerDropExperienceOnDeath;
    }

    public SimpleEventListener<Integer> entitiesDropExperienceOnDeath() {
        return entitiesDropExperienceOnDeath;
    }

    public SimpleEventListener<Boolean> entitiesClearDropsOnDeath() {
        return entitiesClearDropsOnDeath;
    }

    public SimpleEventListener<Boolean> entityDropItem() {
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
