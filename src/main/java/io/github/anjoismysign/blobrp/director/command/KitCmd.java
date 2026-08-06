package io.github.anjoismysign.blobrp.director.command;

import io.github.anjoismysign.bloblib.api.BlobLibMessageAPI;
import io.github.anjoismysign.bloblib.translatable.TranslatableItem;
import io.github.anjoismysign.blobrp.BlobRP;
import io.github.anjoismysign.blobrp.entity.RoleplayKit;
import io.github.anjoismysign.blobrp.entity.configuration.RoleplayWarpConfiguration;
import io.github.anjoismysign.holoworld.asset.IdentityGeneration;
import io.github.anjoismysign.skeramidcommands.command.Command;
import io.github.anjoismysign.skeramidcommands.command.CommandTarget;
import io.github.anjoismysign.skeramidcommands.commandtarget.BukkitCommandTarget;
import io.github.anjoismysign.skeramidcommands.server.bukkit.BukkitAdapter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum KitCmd {
    INSTANCE;

    public void initialize() {
        var players = BukkitCommandTarget.ONLINE_PLAYERS();
        var kitManager = BlobRP.getInstance().getKitManager();

        Command kit = BukkitAdapter.getInstance().ofBukkitCommand("roleplaykit");
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
            if (args.length < 2){
                return;
            }
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
        Command get = kit.child("get");
        get.setParameters(kitManager);
        get.onExecute((permissionMessenger, args) -> {
            CommandSender commandSender = BukkitAdapter.getInstance().of(permissionMessenger);
            if (args.length < 1){
                return;
            }
            if (!(commandSender instanceof Player target)) {
                BlobLibMessageAPI.getInstance()
                        .getMessage("System.Console-Not-Allowed-Command", commandSender)
                        .toCommandSender(commandSender);
                return;
            }
            var roleplayKit = kitManager.parse(args[0]);
            roleplayKit.apply(target);
        });
        Command create = kit.child("create");
        create.setParameters(new CommandTarget<String>() {
            @Override
            public List<String> get() {
                return List.of("Type-the-kit-name-here");
            }

            @Override
            public @Nullable String parse(String s) {
                return s;
            }
        });
        create.onExecute((permissionMessenger, args) -> {
            CommandSender commandSender = BukkitAdapter.getInstance().of(permissionMessenger);
            if (args.length < 1){
                return;
            }
            if (!(commandSender instanceof Player player)) {
                BlobLibMessageAPI.getInstance()
                        .getMessage("System.Console-Not-Allowed-Command", commandSender)
                        .toCommandSender(commandSender);
                return;
            }
            var kitName = args[0];
            var inventory = player.getInventory();
            var equipment = new HashSet<RoleplayKit.Item>();
            for (int slot = 0; slot <= 40; slot++) {
                var stack = inventory.getItem(slot);
                @Nullable var translatable = TranslatableItem.byItemStack(stack);
                if (translatable == null){
                    continue;
                }
                var item = new RoleplayKit.Item();
                item.setIndex(slot);
                item.setAmount(stack.getAmount());
                item.setIdentifier(translatable.identifier());
                equipment.add(item);
            }
            var kitInfo = new RoleplayKit.Info();
            kitInfo.setEquipment(equipment);
            kitInfo.setPriority(0);
            BlobRP.getInstance().getKitManager().add(new IdentityGeneration<>(kitName, kitInfo));
            commandSender.sendMessage("Kit '"+kitName+"' created with '0' priority");
        });
    }

    public boolean isEnabled() {
        return RoleplayWarpConfiguration.getInstance().isEnabled();
    }
}
