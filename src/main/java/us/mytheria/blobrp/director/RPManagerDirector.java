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
        addManager("LangManager", new LangManager(this));
        addManager("DropsManager", new ShopArticleManager(this));
        addManager("ListenerManager", new ListenerManager(this));
        builderManager = new BuilderManager(this);
        addManager("BuilderManager", builderManager);
    }

    @Override
    public void reload() {
        /*
        From top to bottom, follow the order.
         */
        getLangManager().reload();
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

    public LangManager getLangManager() {
        return (LangManager) getManager("LangManager");
    }


    public ListenerManager getListenerManager() {
        return (ListenerManager) getManager("ListenerManager");
    }

    public BuilderManager getBuilderManager() {
        return builderManager;
    }

    public ShopArticleManager getDropsManager() {
        return (ShopArticleManager) getManager("DropsManager");
    }

    public CommandManager getCommandManager() {
        return (CommandManager) getManager("CommandManager");
    }

    public BlobRP getJavaPlugin() {
        return main;
    }
}
