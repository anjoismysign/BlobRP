package io.github.anjoismysign.blobrp.director.command;

import io.github.anjoismysign.anjo.entities.Result;
import io.github.anjoismysign.bloblib.api.BlobLibMessageAPI;
import io.github.anjoismysign.bloblib.entities.BlobChildCommand;
import io.github.anjoismysign.bloblib.entities.BlobExecutor;
import io.github.anjoismysign.bloblib.entities.ExecutorData;
import io.github.anjoismysign.blobrp.entities.ShopArticle;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenSellInventory {

    public static boolean command(ExecutorData data) {
        String[] args = data.args();
        BlobExecutor executor = data.executor();
        CommandSender sender = data.sender();
        Result<BlobChildCommand> result = executor
                .isChildCommand("opensellinventory", args);
        if (result.isValid()) {
            switch (args.length) {
                case 1 -> {
                    return executor.ifInstanceOfPlayer(sender, ShopArticle::openSellInventory);
                }
                case 2 -> {
                    String playerName = args[1];
                    Player input = Bukkit.getPlayer(playerName);
                    if (input == null) {
                        BlobLibMessageAPI.getInstance()
                                .getMessage("Player.Not-Found", sender)
                                .toCommandSender(sender);
                        return true;
                    }
                    ShopArticle.openSellInventory(input);
                    return true;
                }
                default -> {
                    return false;
                }
            }

        }
        return false;
    }
}
