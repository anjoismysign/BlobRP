package us.mytheria.blobrp.director.manager;

import us.mytheria.blobrp.director.RPManager;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.reward.Reward;
import us.mytheria.blobrp.reward.RewardReader;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

public class RewardManager extends RPManager {
    private HashMap<String, Reward> rewards;

    public RewardManager(RPManagerDirector managerDirector) {
        super(managerDirector);
    }

    @Override
    public void loadInConstructor() {
        rewards = new HashMap<>();
        loadFiles(getManagerDirector().getFileManager().getShopArticles());
    }

    private void loadFiles(File path) {
        File[] listOfFiles = path.listFiles();
        for (File file : listOfFiles) {
            if (file.getName().equals(".DS_Store"))
                continue;
            if (file.isFile()) {
                addReward(RewardReader.read(file));
            }
            if (file.isDirectory())
                loadFiles(file);
        }
    }

    public void addReward(Reward<?> reward) {
        this.rewards.put(reward.getKey(), reward);
    }

    @Override
    public void reload() {
        loadInConstructor();
    }

    /**
     * @param key The key/fileName of the Reward
     * @return The Reward if found, null otherwise
     */
    public Reward getReward(String key) {
        return rewards.get(key);
    }

    public Collection<Reward> values() {
        return rewards.values();
    }
}
