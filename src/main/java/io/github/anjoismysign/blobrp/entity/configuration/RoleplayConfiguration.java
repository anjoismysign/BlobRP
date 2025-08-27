package io.github.anjoismysign.blobrp.entity.configuration;

import io.github.anjoismysign.blobrp.BlobRP;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class RoleplayConfiguration {
    private static RoleplayConfiguration instance;

    public static RoleplayConfiguration getInstance() {
        if (instance == null)
            instance = new RoleplayConfiguration();
        return instance;
    }

    private RoleplayWarpConfiguration roleplayWarpConfiguration;
    private PressureConfiguration pressureConfiguration;
    private AlternativeSavingConfiguration alternativeSavingConfiguration;

    private RoleplayConfiguration() {
    }

    public void reload(@NotNull ConfigurationSection settingsSection) {
        BlobRP plugin = BlobRP.getInstance();
        File dataFolder = plugin.getDataFolder();
        File configurationFile = new File(dataFolder, "alternative-saving.yml");
        plugin.saveResource("alternative-saving.yml", false);

        Constructor constructor = new Constructor(AlternativeSavingConfiguration.class, new LoaderOptions());
        Yaml yaml = new Yaml(constructor);
        try (FileInputStream inputStream = new FileInputStream(configurationFile)) {
            alternativeSavingConfiguration = yaml.load(inputStream);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        roleplayWarpConfiguration = RoleplayWarpConfiguration.READ(settingsSection);
        pressureConfiguration = PressureConfiguration.READ(settingsSection);
    }

    @NotNull
    public RoleplayWarpConfiguration getRoleplayWarpConfiguration() {
        return roleplayWarpConfiguration;
    }

    @NotNull
    public PressureConfiguration getPressureConfiguration() {
        return pressureConfiguration;
    }

    public AlternativeSavingConfiguration getAlternativeSavingConfiguration() {
        return alternativeSavingConfiguration;
    }
}
