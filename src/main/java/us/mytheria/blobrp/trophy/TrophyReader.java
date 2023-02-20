package us.mytheria.blobrp.trophy;

import global.warming.commons.io.FilenameUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import us.mytheria.blobrp.BlobRP;
import us.mytheria.blobrp.reward.Reward;
import us.mytheria.blobrp.trophy.requirements.TrophyRequirement;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TrophyReader {
    public static Trophy read(File file) {
        YamlConfiguration section = YamlConfiguration.loadConfiguration(file);
        if (!section.contains("EntityType"))
            throw new IllegalArgumentException("'EntityType' is required. Missing at: " + file.getPath());
        EntityType type = EntityType.valueOf(section.getString("EntityType"));
        if (!section.contains("Rewards"))
            throw new IllegalArgumentException("'Rewards' is required. Missing at: " + file.getPath());
        List<String> rewardKeys = section.getStringList("Rewards");
        List<Reward> rewards = new ArrayList<>();
        rewardKeys.forEach(key -> {
            Reward.ifReward(key, rewards::add);
        });
        if (!section.contains("Requirements"))
            throw new IllegalArgumentException("'Requirements' is required. Missing at: " + file.getPath());
        String trophyRequirementKey = section.getString("Requirements");
        TrophyRequirement trophyRequirement = BlobRP.getInstance().getManagerDirector().getTrophyRequirementDirector().getObjectManager().getObject(trophyRequirementKey);
        if (trophyRequirement == null)
            throw new IllegalArgumentException("TrophyRequirement not found: " + trophyRequirementKey);
        return new Trophy(type, rewards, trophyRequirement, FilenameUtils.removeExtension(file.getName()));
    }
}