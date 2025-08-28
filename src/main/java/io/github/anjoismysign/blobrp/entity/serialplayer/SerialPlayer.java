package io.github.anjoismysign.blobrp.entity.serialplayer;

import io.github.anjoismysign.blobrp.entity.configuration.RoleplayConfiguration;
import io.github.anjoismysign.psa.crud.Crudable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class SerialPlayer implements Crudable {
    private final @NotNull String identification;
    private final @NotNull List<SerialProfile> profiles;
    private int selectedProfile = 0;

    public SerialPlayer(@NotNull String identification){
        this.identification = identification;
        this.profiles = new ArrayList<>();
        int defaultSlots = RoleplayConfiguration.getInstance().getAlternativeSavingConfiguration().getDefaultSlots();
        for (int index = 0; index < defaultSlots; index++) {
            this.profiles.add(new SerialProfile(RoleplayConfiguration.getInstance()
                    .getAlternativeSavingConfiguration().getRandomProfileName(profiles.stream().map(profile->profile.getProfileName()).toList()),
                    ""));
        }
    }

    public SerialPlayer(@NotNull String identification, @NotNull List<SerialProfile> profiles) {
        this.identification = identification;
        this.profiles = profiles;
    }

    @Override
    public @NotNull String getIdentification() {
        return identification;
    }

    public @NotNull List<SerialProfile> getProfiles() {
        return profiles;
    }

    public int getSelectedProfile() {
        return selectedProfile;
    }

    public void setSelectedProfile(int selectedProfile) {
        this.selectedProfile = selectedProfile;
    }

    public int getProfilesSize(){
        return profiles.size();
    }

    public void saveCurrentProfile(@NotNull Player player,
                                   boolean hasPlayedBefore){
        int size = profiles.size();
        if (size <= selectedProfile){
            throw new RuntimeException("Selected profile is '"+selectedProfile+"' but there are only "+size+" profiles");
        }
        profiles.get(selectedProfile).json = PlayerProfile.fromPlayer(player,hasPlayedBefore).toJson();
    }

    public void loadProfile(@NotNull Player player,
                            int selectedProfile){
        saveCurrentProfile(player, true);
        int size = profiles.size();
        if (size <= selectedProfile){
            throw new RuntimeException("Selected profile is '"+selectedProfile+"' but there are only "+size+" profiles");
        }
        SerialProfile profile = profiles.get(selectedProfile);
        PlayerProfile.fromJson(profile.json).toPlayer(player);
        this.selectedProfile = selectedProfile;
    }

    public void loadNewProfile(@NotNull Player player){
        saveCurrentProfile(player, true);
        SerialProfile profile = new SerialProfile(RoleplayConfiguration.getInstance()
                .getAlternativeSavingConfiguration().getRandomProfileName(profiles.stream().map(SerialProfile::getProfileName).toList()),
                "");
        profiles.add(profile);
        selectedProfile = profiles.indexOf(profile);
    }

    @Nullable
    public Player getPlayer(){
        UUID uuid = UUID.fromString(getIdentification());
        return Bukkit.getPlayer(uuid);
    }
}
