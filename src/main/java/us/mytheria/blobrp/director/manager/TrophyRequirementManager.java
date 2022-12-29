package us.mytheria.blobrp.director.manager;

import us.mytheria.blobrp.director.RPManager;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.trophy.requirements.TrophyRequirement;
import us.mytheria.blobrp.trophy.requirements.TrophyRequirementReader;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

public class TrophyRequirementManager extends RPManager {
    private HashMap<String, TrophyRequirement> requirements;

    public TrophyRequirementManager(RPManagerDirector managerDirector) {
        super(managerDirector);
    }

    @Override
    public void loadInConstructor() {
        requirements = new HashMap<>();
        loadFiles(getManagerDirector().getFileManager().getShopArticles());
    }

    private void loadFiles(File path) {
        File[] listOfFiles = path.listFiles();
        for (File file : listOfFiles) {
            if (file.getName().equals(".DS_Store"))
                continue;
            if (file.isFile()) {
                addTrophyRequirements(TrophyRequirementReader.read(file).build());
            }
            if (file.isDirectory())
                loadFiles(file);
        }
    }

    public void addTrophyRequirements(TrophyRequirement requirement) {
        this.requirements.put(requirement.getKey(), requirement);
    }

    @Override
    public void reload() {
        loadInConstructor();
    }

    /**
     * @param key The key/fileName of the TrophyRequirement
     * @return The TrophyRequirement if found, null otherwise
     */
    public TrophyRequirement getTrophyRequirement(String key) {
        return requirements.get(key);
    }

    public Collection<TrophyRequirement> values() {
        return requirements.values();
    }
}
