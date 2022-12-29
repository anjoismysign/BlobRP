package us.mytheria.blobrp.director;

import us.mytheria.bloblib.entities.manager.ManagerDirector;
import us.mytheria.blobrp.BlobRP;
import us.mytheria.blobrp.director.manager.*;

public class RPManagerDirector extends ManagerDirector {
    private final BlobRP main;
    private final FileManager fileManager;
    private final BuilderManager builderManager;

    public RPManagerDirector() {
        super();
        main = BlobRP.getInstance();
        addManager("CommandManager", new CommandManager(this));
        addManager("ConfigManager", new ConfigManager(this));
        fileManager = new FileManager(this);
        addManager("FileManager", fileManager);
        addManager("ShopArticleManager", new ShopArticleManager(this));
        addManager("RewardManager", new RewardManager(this));
        addManager("TrophyRequirementManager", new TrophyRequirementManager(this));
        addManager("ListenerManager", new ListenerManager(this));
        builderManager = new BuilderManager(this);
        addManager("BuilderManager", builderManager);
    }

    @Override
    public void reload() {
        /*
        From top to bottom, follow the order.
         */
        getDropsManager().reload();
    }

    @Override
    public void unload() {
    }

    @Override
    public void postWorld() {
    }

    public ConfigManager getConfigManager() {
        return (ConfigManager) getManager("ConfigManager");
    }

    public FileManager getFileManager() {
        return fileManager;
    }


    public ListenerManager getListenerManager() {
        return (ListenerManager) getManager("ListenerManager");
    }

    public BuilderManager getBuilderManager() {
        return builderManager;
    }

    public ShopArticleManager getDropsManager() {
        return (ShopArticleManager) getManager("ShopArticleManager");
    }

    public RewardManager getRewardManager() {
        return (RewardManager) getManager("RewardManager");
    }

    public TrophyRequirementManager getTrophyRequirementManager() {
        return (TrophyRequirementManager) getManager("TrophyRequirementManager");
    }

    public CommandManager getCommandManager() {
        return (CommandManager) getManager("CommandManager");
    }

    public BlobRP getJavaPlugin() {
        return main;
    }
}
