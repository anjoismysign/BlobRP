package io.github.anjoismysign.blobrp.entity.configuration;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class AlternativeSavingConfiguration {

    private boolean enabled;

    private WelcomePlayersConfiguration welcomePlayers;

    private List<String> profileNames;

    private int defaultSlots;

    public WelcomePlayersConfiguration getWelcomePlayers() {
        return welcomePlayers;
    }

    public void setWelcomePlayers(WelcomePlayersConfiguration welcomePlayers) {
        this.welcomePlayers = welcomePlayers;
    }

    public List<String> getProfileNames() {
        return profileNames;
    }

    public void setProfileNames(List<String> profileNames) {
        this.profileNames = profileNames;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getRandomProfileName(@NotNull List<String> noRepeat) {
        Objects.requireNonNull(noRepeat, "'noRepeat' cannot be null");
        List<String> names = profileNames;
        if (names.isEmpty()) {
            names = List.of(
                    "Alpha", "Bravo", "Charlie", "Delta", "Echo", "Foxtrot",
                    "Golf", "Hotel", "India", "Juliett", "Kilo", "Lima",
                    "Mike", "November", "Oscar", "Papa", "Quebec", "Romeo",
                    "Sierra", "Tango", "Uniform", "Victor", "Whiskey",
                    "X-ray", "Yankee", "Zulu"
            );
        }
        String random = names.get((int) (Math.random() * names.size()));
        if (noRepeat.contains(random))
            return getRandomProfileName(noRepeat);
        return random;
    }

    public int getDefaultSlots() {
        return defaultSlots;
    }

    public void setDefaultSlots(int defaultSlots) {
        this.defaultSlots = defaultSlots;
    }
}
