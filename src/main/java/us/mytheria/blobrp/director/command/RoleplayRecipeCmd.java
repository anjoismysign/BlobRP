package us.mytheria.blobrp.director.command;

import me.anjoismysign.skeramidcommands.command.Command;
import me.anjoismysign.skeramidcommands.command.CommandBuilder;
import me.anjoismysign.skeramidcommands.command.CommandTarget;
import me.anjoismysign.skeramidcommands.commandtarget.BukkitCommandTarget;
import me.anjoismysign.skeramidcommands.server.bukkit.BukkitAdapter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.mytheria.bloblib.api.BlobLibMessageAPI;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.entities.RoleplayRecipe;

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
