package us.mytheria.blobrp.director.command;

import me.anjoismysign.skeramidcommands.command.Command;
import me.anjoismysign.skeramidcommands.command.CommandBuilder;
import me.anjoismysign.skeramidcommands.commandtarget.BukkitCommandTarget;
import me.anjoismysign.skeramidcommands.server.bukkit.BukkitAdapter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.mytheria.bloblib.api.BlobLibMessageAPI;
import us.mytheria.bloblib.utilities.TextColor;
import us.mytheria.blobrp.entities.configuration.RoleplayWarpConfiguration;
import us.mytheria.blobrp.ui.RoleplayWarpsUI;

public class WarpCmd {

    private static WarpCmd instance;

    public static WarpCmd getInstance() {
        if (instance == null)
            instance = new WarpCmd();
        return instance;
    }

    public boolean isEnabled() {
        return RoleplayWarpConfiguration.getInstance().isEnabled();
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
}
