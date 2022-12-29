package us.mytheria.blobrp.director.manager;

import org.bukkit.entity.EntityType;
import us.mytheria.blobrp.director.RPManager;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.trophy.Trophy;
import us.mytheria.blobrp.trophy.requirements.TrophyReader;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

public class TrophyManager extends RPManager {
    private HashMap<EntityType, Trophy> trophies;

    public TrophyManager(RPManagerDirector managerDirector) {
        super(managerDirector);
    }

    @Override
    public void loadInConstructor() {
        trophies = new HashMap<>();
        loadFiles(getManagerDirector().getFileManager().getShopArticles());
    }

    private void loadFiles(File path) {
        File[] listOfFiles = path.listFiles();
        for (File file : listOfFiles) {
            if (file.getName().equals(".DS_Store"))
                continue;
            if (file.isFile()) {
                addTrophy(TrophyReader.read(file));
            }
            if (file.isDirectory())
                loadFiles(file);
        }
    }

    public void addTrophy(Trophy trophy) {
        this.trophies.put(trophy.getType(), trophy);
    }

    @Override
    public void reload() {
        loadInConstructor();
    }

    /**
     * @param entityType The EntityType of the Trophy
     * @return The Trophy if found, null otherwise
     */
    public Trophy getTrophy(EntityType entityType) {
        return trophies.get(entityType);
    }

    public Collection<Trophy> values() {
        return trophies.values();
    }
}
