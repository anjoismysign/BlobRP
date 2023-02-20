package us.mytheria.blobrp.trophy;

import org.bukkit.configuration.file.YamlConfiguration;
import us.mytheria.blobrp.reward.Reward;

import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

public class TrophyWriter {
    private final Trophy trophy;

    public static TrophyWriter from(Trophy trophy) {
        return new TrophyWriter(trophy);
    }

    private TrophyWriter(Trophy trophy) {
        this.trophy = trophy;
    }

    public File saveToFile(File directory) {
        File file = new File(directory
                + "/" + trophy.getKey() + ".yml");
        YamlConfiguration section = YamlConfiguration.loadConfiguration(file);
        section.set("EntityType", trophy.getType().name());
        section.set("Rewards", trophy.getRewards().stream().map(Reward::getKey)
                .collect(Collectors.toList()));
        section.set("Requirements", trophy.getRequirements().getKey());
        try {
            section.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
