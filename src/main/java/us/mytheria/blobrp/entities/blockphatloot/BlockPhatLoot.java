package us.mytheria.blobrp.entities.blockphatloot;

import com.codisimus.plugins.phatloots.PhatLoot;
import com.codisimus.plugins.phatloots.PhatLootsAPI;
import com.codisimus.plugins.phatloots.loot.LootBundle;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import us.mytheria.bloblib.entities.BlobObject;
import us.mytheria.blobrp.entities.blocktype.BlockType;

import java.io.File;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public record BlockPhatLoot(@NotNull String getKey,
                            @NotNull BlockType getBlockType,
                            @NotNull String getPhatLootName,
                            boolean shouldCancel,
                            @Nullable Set<String> applicableOn,
                            boolean isGlobal) implements BlobObject {

    @Override
    public File saveToFile(File directory) {
        File file = instanceFile(directory);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        getBlockType.serializeDefault(config);
        config.set("PhatLootName", getPhatLootName);
        config.set("Should-Cancel", shouldCancel);
        config.set("Applicable-On", applicableOn.stream().toList());
        config.set("Is-Global", isGlobal);
        try {
            config.save(file);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return file;
    }

    @Nullable
    public PhatLoot getPhatLoot() {
        return PhatLootsAPI.getPhatLoot(getPhatLootName);
    }

    @NotNull
    public LootBundle rollForLoot() {
        PhatLoot phatLoot = getPhatLoot();
        if (phatLoot == null)
            return new LootBundle();
        return phatLoot.rollForLoot();
    }

    @NotNull
    public Set<World> getApplicableWorlds() {
        if (applicableOn == null)
            return Set.of();
        return applicableOn.stream()
                .map(Bukkit::getWorld)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public boolean applies(@Nullable World world) {
        if (isGlobal)
            return true;
        if (world == null)
            return false;
        return getApplicableWorlds().contains(world);
    }
}
