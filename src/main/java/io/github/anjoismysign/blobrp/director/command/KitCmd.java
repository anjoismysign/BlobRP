package io.github.anjoismysign.blobrp.director.command;

import io.github.anjoismysign.bloblib.api.BlobLibMessageAPI;
import io.github.anjoismysign.blobrp.BlobRP;
import io.github.anjoismysign.blobrp.entity.RoleplayKit;
import io.github.anjoismysign.blobrp.entity.configuration.RoleplayWarpConfiguration;
import io.github.anjoismysign.holoworld.asset.IdentityGeneration;
import io.github.anjoismysign.skeramidcommands.command.Command;
import io.github.anjoismysign.skeramidcommands.commandtarget.BukkitCommandTarget;
import io.github.anjoismysign.skeramidcommands.server.bukkit.BukkitAdapter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public enum KitCmd {
    INSTANCE;

    public void initialize() {
        var players = BukkitCommandTarget.ONLINE_PLAYERS();
        var kitManager = BlobRP.getInstance().getKitManager();

        Command kit = BukkitAdapter.getInstance().ofBukkitCommand("kit");
        Command example = kit.child("example");
        example.onExecute((permissionMessenger, args) -> {
            CommandSender commandSender = BukkitAdapter.getInstance().of(permissionMessenger);
            var info = new RoleplayKit.Info();
            var item = new RoleplayKit.Item();
            item.setIndex(0);
            item.setIdentifier("TranslatableArea.Wand");
            item.setAmount(1);
            info.setEquipment(Set.of(item));
            info.setPriority(Integer.MIN_VALUE);
            kitManager.add(new IdentityGeneration<>("example", info));
            commandSender.sendMessage("Generated example kit");
        });
        Command give = kit.child("give");
        give.setParameters(players,kitManager);
        give.onExecute((permissionMessenger, args) -> {
            CommandSender commandSender = BukkitAdapter.getInstance().of(permissionMessenger);
            Player target = players.parse(args[0]);
            if (target == null) {
                BlobLibMessageAPI.getInstance()
                        .getMessage("Player.Not-Found", commandSender)
                        .toCommandSender(commandSender);
                return;
            }
            var roleplayKit = kitManager.parse(args[1]);
            roleplayKit.apply(target);
        });
    }

    public boolean isEnabled() {
        return RoleplayWarpConfiguration.getInstance().isEnabled();
    }
}
