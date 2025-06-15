package io.github.anjoismysign.blobrp.entities.regenable;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import io.github.anjoismysign.bloblib.exception.ConfigurationFieldException;

import java.util.Objects;
import java.util.Random;

public record RandomInterval(
        long getStart,
        long getEnd
) {

    @NotNull
    public static RandomInterval READ(
            @NotNull ConfigurationSection section,
            boolean isDefault
    ) {
        Objects.requireNonNull(section, "'section' cannot be null");
        if (isDefault) {
            ConfigurationSection intervalSection = section.getConfigurationSection("Interval");
            if (intervalSection == null)
                throw new ConfigurationFieldException("'Interval' is not set or valid");
            return READ(intervalSection, false);
        }
        long start = section.getLong("Start");
        if (start == 0)
            throw new ConfigurationFieldException("'Start' is not valid or set");
        long end = section.getLong("End");
        if (end == 0)
            throw new ConfigurationFieldException("'End' is not valid or set");
        return new RandomInterval(start, end);
    }

    public long next() {
        if (getStart == getEnd)
            return getStart;
        Random random = new Random();
        return random.nextLong(getStart, getEnd + 1);
    }

    public void serialize(
            @NotNull ConfigurationSection section,
            boolean create) {
        Objects.requireNonNull(section, "'section' cannot be null");
        if (create) {
            ConfigurationSection regenableIntervalSection = section.createSection("Interval");
            serialize(regenableIntervalSection, false);
            return;
        }
        section.set("Start", getStart);
        section.set("End", getEnd);
    }
}
