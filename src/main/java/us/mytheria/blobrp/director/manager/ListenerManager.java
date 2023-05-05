package us.mytheria.blobrp.director.manager;

import us.mytheria.blobrp.director.RPManager;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.listeners.*;
import us.mytheria.blobrp.merchant.MerchantListener;

public class ListenerManager extends RPManager {
    private final DropNonSoulOnDeath dropNonSoulOnDeath;
    private final EntitiesClearDropsOnDeath entitiesClearDropsOnDeath;
    private final EntitiesDropExperienceOnDeath entitiesDropExperienceOnDeath;
    private final EntityDropItem entityDropItem;
    private final KeepExperienceOnDeath keepExperienceOnDeath;
    private final PlayerDropExperienceOnDeath playerDropExperienceOnDeath;
    private final ShopArticleSell shopArticleSell;
    private final MerchantListener merchantListener;
    private final WelcomePlayer welcomePlayer;
    private final PlayerHunger playerHunger;
    private final IceFormation iceFormation;
    private final PlayerSpectateOnDeath playerSpectateOnDeath;
    private final ForceGamemodeOnJoin forceGamemodeOnJoin;
    private final GlobalSlowDigging globalSlowDigging;
    private final OnJoinMessage onJoinMessage;
    private final OnQuitMessage onQuitMessage;
    private final DiscordCmd discordCmd;
    private final KillMessageWeapon killMessageWeapon;
    private final PlayerDeathMessage playerDeathMessage;

    public ListenerManager(RPManagerDirector managerDirector) {
        super(managerDirector);
        ConfigManager configManager = managerDirector.getConfigManager();
        dropNonSoulOnDeath = new DropNonSoulOnDeath(configManager);
        entitiesClearDropsOnDeath = new EntitiesClearDropsOnDeath(configManager);
        entitiesDropExperienceOnDeath = new EntitiesDropExperienceOnDeath(configManager);
        entityDropItem = new EntityDropItem(configManager);
        keepExperienceOnDeath = new KeepExperienceOnDeath(configManager);
        playerDropExperienceOnDeath = new PlayerDropExperienceOnDeath(configManager);
        shopArticleSell = new ShopArticleSell(configManager);
        merchantListener = new MerchantListener(configManager);
        welcomePlayer = new WelcomePlayer(configManager);
        playerHunger = new PlayerHunger(configManager);
        iceFormation = new IceFormation(configManager);
        playerSpectateOnDeath = new PlayerSpectateOnDeath(configManager);
        forceGamemodeOnJoin = new ForceGamemodeOnJoin(configManager);
        globalSlowDigging = new GlobalSlowDigging(configManager);
        onJoinMessage = new OnJoinMessage(configManager);
        onQuitMessage = new OnQuitMessage(configManager);
        discordCmd = new DiscordCmd(configManager);
        killMessageWeapon = new KillMessageWeapon(configManager);
        playerDeathMessage = new PlayerDeathMessage(configManager);
        reload();
    }

    @Override
    public void reload() {
        dropNonSoulOnDeath.reload();
        entitiesClearDropsOnDeath.reload();
        entitiesDropExperienceOnDeath.reload();
        entityDropItem.reload();
        keepExperienceOnDeath.reload();
        playerDropExperienceOnDeath.reload();
        shopArticleSell.reload();
        merchantListener.reload();
        welcomePlayer.reload();
        playerHunger.reload();
        iceFormation.reload();
        playerSpectateOnDeath.reload();
        forceGamemodeOnJoin.reload();
        globalSlowDigging.reload();
        onJoinMessage.reload();
        onQuitMessage.reload();
        discordCmd.reload();
        killMessageWeapon.reload();
        playerDeathMessage.reload();
    }
}
