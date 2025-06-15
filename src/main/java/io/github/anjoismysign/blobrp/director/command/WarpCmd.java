package io.github.anjoismysign.blobrp.director.command;

import io.github.anjoismysign.bloblib.api.BlobLibMessageAPI;
import io.github.anjoismysign.bloblib.utilities.TextColor;
import io.github.anjoismysign.blobrp.entities.configuration.RoleplayWarpConfiguration;
import io.github.anjoismysign.blobrp.ui.RoleplayWarpsUI;
import io.github.anjoismysign.skeramidcommands.command.Command;
import io.github.anjoismysign.skeramidcommands.command.CommandBuilder;
import io.github.anjoismysign.skeramidcommands.commandtarget.BukkitCommandTarget;
import io.github.anjoismysign.skeramidcommands.server.bukkit.BukkitAdapter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCmd {

    private static WarpCmd instance;

    public static WarpCmd getInstance() {
        if (instance == null)
            instance = new WarpCmd();
        return instance;
    }

    private WarpCmd() {
        Command warpsCommand = CommandBuilder.of("roleplaywarps")
                .build();
        Command open = warpsCommand.child("open");
        open.setParameters(BukkitCommandTarget.ONLINE_PLAYERS());
        open.onExecute((permissionMessenger, args) -> {
            CommandSender commandSender = BukkitAdapter.getInstance().of(permissionMessenger);
            Player target = BukkitCommandTarget.ONLINE_PLAYERS().parse(args[0]);
            if (target == null) {
                BlobLibMessageAPI.getInstance()
                        .getMessage("Player.Not-Found", commandSender)
                        .toCommandSender(commandSender);
                return;
            }
            if (!isEnabled()) {
                target.sendMessage(TextColor.PARSE("&cFeature disabled. Contact admin"));
                return;
            }
            RoleplayWarpsUI.getInstance().open(target);
        });
    }

    public boolean isEnabled() {
        return RoleplayWarpConfiguration.getInstance().isEnabled();
    }
}
