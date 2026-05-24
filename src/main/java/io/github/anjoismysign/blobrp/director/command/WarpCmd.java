package io.github.anjoismysign.blobrp.director.command;

import io.github.anjoismysign.bloblib.api.BlobLibMessageAPI;
import io.github.anjoismysign.bloblib.utilities.TextColor;
import io.github.anjoismysign.blobrp.BlobRPAPI;
import io.github.anjoismysign.blobrp.entity.RoleplayWarp;
import io.github.anjoismysign.blobrp.entity.configuration.RoleplayWarpConfiguration;
import io.github.anjoismysign.blobrp.ui.RoleplayWarpsUI;
import io.github.anjoismysign.skeramidcommands.command.Command;
import io.github.anjoismysign.skeramidcommands.command.CommandBuilder;
import io.github.anjoismysign.skeramidcommands.command.CommandTarget;
import io.github.anjoismysign.skeramidcommands.commandtarget.BukkitCommandTarget;
import io.github.anjoismysign.skeramidcommands.commandtarget.CommandTargetBuilder;
import io.github.anjoismysign.skeramidcommands.server.bukkit.BukkitAdapter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public enum WarpCmd {
    INSTANCE;

    public void initialize() {
        Command warpsCommand = CommandBuilder.of("roleplaywarps")
                .build();
        CommandTarget<RoleplayWarp> warps = CommandTargetBuilder.fromMap(()->BlobRPAPI.getInstance().mapWarps());

        Command teleport = warpsCommand.child("teleport");
        teleport.setParameters(BukkitCommandTarget.ONLINE_PLAYERS(), warps);
        teleport.onExecute((permissionMessenger, args) -> {
            CommandSender commandSender = BukkitAdapter.getInstance().of(permissionMessenger);
            if (args.length < 2){
                commandSender.sendMessage(TextColor.PARSE("&c/roleplaywarps teleport <player> <warp>"));
                return;
            }
            @Nullable Player target = BukkitCommandTarget.ONLINE_PLAYERS().parse(args[0]);
            if (target == null) {
                BlobLibMessageAPI.getInstance()
                        .getMessage("Player.Not-Found", commandSender)
                        .toCommandSender(commandSender);
                return;
            }
            String selected = args[1];
            @Nullable RoleplayWarp warp = warps.parse(selected);
            if (warp == null){
                commandSender.sendMessage(TextColor.PARSE("&cNot a warp: "+selected));
                return;
            }
            if (!isEnabled()) {
                target.sendMessage(TextColor.PARSE("&cFeature disabled. Contact admin"));
                return;
            }
            target.teleport(warp.getPositionable().get().toLocation());
        });

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
