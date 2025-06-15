package io.github.anjoismysign.blobrp.director.command;

import io.github.anjoismysign.bloblib.api.BlobLibMessageAPI;
import io.github.anjoismysign.blobrp.director.RPManagerDirector;
import io.github.anjoismysign.blobrp.entities.RoleplayRecipe;
import io.github.anjoismysign.skeramidcommands.command.Command;
import io.github.anjoismysign.skeramidcommands.command.CommandBuilder;
import io.github.anjoismysign.skeramidcommands.command.CommandTarget;
import io.github.anjoismysign.skeramidcommands.commandtarget.BukkitCommandTarget;
import io.github.anjoismysign.skeramidcommands.server.bukkit.BukkitAdapter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class RoleplayRecipeCmd {

    public static RoleplayRecipeCmd of(@NotNull RPManagerDirector director) {
        Objects.requireNonNull(director, "'director' cannot be null");
        return new RoleplayRecipeCmd(director);
    }

    private RoleplayRecipeCmd(@NotNull RPManagerDirector director) {
        Command roleplayrecipe = CommandBuilder.of("roleplayrecipe")
                .build();
        Command open = roleplayrecipe.child("open");
        CommandTarget<RoleplayRecipe> roleplayTarget = director.getRoleplayRecipeDirector().getObjectManager();
        open.setParameters(BukkitCommandTarget.ONLINE_PLAYERS(), roleplayTarget);
        open.onExecute((permissionMessenger, args) -> {
            CommandSender sender = BukkitAdapter.getInstance().of(permissionMessenger);
            if (args.length < 2) {
                sender.sendMessage("Usage: '/roleplayrecipe open <player> <roleplayrecipe>'");
                return;
            }
            Player player = BukkitCommandTarget.ONLINE_PLAYERS().parse(args[0]);
            if (player == null) {
                BlobLibMessageAPI.getInstance()
                        .getMessage("Player.Not-Found", sender)
                        .toCommandSender(sender);
                return;
            }
            RoleplayRecipe roleplayRecipe = roleplayTarget.parse(args[1]);
            if (roleplayRecipe == null) {
                BlobLibMessageAPI.getInstance()
                        .getMessage("RoleplayRecipe.Not-Found", sender)
                        .toCommandSender(sender);
                return;
            }
            roleplayRecipe.open(player);
        });
    }
}
