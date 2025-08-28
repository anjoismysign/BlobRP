package io.github.anjoismysign.blobrp.command;

import io.github.anjoismysign.bloblib.api.BlobLibInventoryAPI;
import io.github.anjoismysign.bloblib.api.BlobLibMessageAPI;
import io.github.anjoismysign.bloblib.entities.translatable.TranslatableItem;
import io.github.anjoismysign.blobrp.BlobRP;
import io.github.anjoismysign.blobrp.director.manager.AlternativeSaving;
import io.github.anjoismysign.blobrp.entity.serialplayer.SerialPlayer;
import io.github.anjoismysign.blobrp.entity.serialplayer.SerialProfile;
import io.github.anjoismysign.skeramidcommands.command.Command;
import io.github.anjoismysign.skeramidcommands.command.CommandBuilder;
import io.github.anjoismysign.skeramidcommands.commandtarget.BukkitCommandTarget;
import io.github.anjoismysign.skeramidcommands.server.bukkit.BukkitAdapter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public enum RoleplayCommand {
    INSTANCE;

    public void load(){
        Command rprofile = CommandBuilder.of("rprofile").build();
        switchProfile(rprofile);
    }

    public void switchProfile(Command rprofile){
        Command switchCommand = rprofile.child("switch");
        switchCommand.setParameters(BukkitCommandTarget.ONLINE_PLAYERS());
        switchCommand.onExecute((permissionMessenger, args) -> {
            CommandSender sender = BukkitAdapter.getInstance().of(permissionMessenger);
            Player target = BukkitCommandTarget.ONLINE_PLAYERS().parse(args[0]);
            if (target == null) {
                BlobLibMessageAPI.getInstance()
                        .getMessage("Player.Not-Found", sender)
                        .toCommandSender(sender);
                return;
            }
            @Nullable SerialPlayer serialPlayer = AlternativeSaving.getSerialPlayer(target);
            if (serialPlayer == null){
                BlobLibMessageAPI.getInstance()
                    .getMessage("Player.Not-Inside-Plugin-Cache", target)
                    .handle(target);
                return;
            }
            UUID uuid = target.getUniqueId();
            Bukkit.getScheduler().runTask(BlobRP.getInstance(), () -> {
                if (target != Bukkit.getPlayer(uuid)) {
                    return;
                }
                List<SerialProfile> profiles = serialPlayer.getProfiles();
                BlobLibInventoryAPI.getInstance()
                        .customSelector(
                                "BlobRP-Switch-Profile",
                                target,
                                "Profiles",
                                "Profile",
                                ()->profiles,
                                profile -> {
                                    target.closeInventory();
                                    int index = profiles.indexOf(profile);
                                    if (index == -1){
                                        throw new RuntimeException("Profile does not belong to SerialPlayer");
                                    }
                                    serialPlayer.loadProfile(target, index);
                                },
                                profile -> TranslatableItem.by("BlobRP.Switch-Profile-Element")
                                        .localize(target)
                                        .modder()
                                        .replace("%profile%", profile.getProfileName())
                                        .get()
                                        .get(),
                                null,
                                null,
                                null);
            });
        });
    }
}
