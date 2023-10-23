package us.mytheria.blobrp.director.command;

import me.anjoismysign.anjo.entities.Result;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.mytheria.bloblib.api.BlobLibMessageAPI;
import us.mytheria.bloblib.entities.BlobChildCommand;
import us.mytheria.bloblib.entities.BlobExecutor;
import us.mytheria.bloblib.entities.ExecutorData;
import us.mytheria.blobrp.entities.ShopArticle;

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
