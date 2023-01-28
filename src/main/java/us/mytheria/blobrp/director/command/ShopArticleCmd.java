package us.mytheria.blobrp.director.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import us.mytheria.bloblib.BlobLibAssetAPI;
import us.mytheria.bloblib.entities.inventory.ObjectBuilder;
import us.mytheria.blobrp.BlobRP;
import us.mytheria.blobrp.entities.ShopArticle;

import java.util.ArrayList;
import java.util.List;

public class ShopArticleCmd implements CommandExecutor, TabCompleter {
    private final BlobRP main;

    public ShopArticleCmd() {
        this.main = BlobRP.getInstance();
        main.getCommand("shoparticle").setExecutor(this);
        main.getCommand("shoparticle").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("blobrp.admin")) {
            BlobLibAssetAPI.getMessage("System.No-Permission").toCommandSender(sender);
            return true;
        }
        if (args.length < 1) {
            debug(sender);
            return true;
        }
        String arg1 = args[0];
        if (arg1.equalsIgnoreCase("add")) {
            if (!(sender instanceof Player player)) {
                BlobLibAssetAPI.getMessage("System.Console-Not-Allowed-Command").toCommandSender(sender);
                return true;
            }
            ObjectBuilder<ShopArticle> builder = main.getManagerDirector().getShopArticleDirector()
                    .getBuilderManager().getOrDefault(player);
            builder.openInventory();
            return true;
        }
        return false;
    }

    public void debug(CommandSender sender) {
        if (sender.hasPermission("blobrp.debug")) {
            sender.sendMessage("/shoparticle add");
        }
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("shoparticle")) {
            if (sender.hasPermission("blobrp.admin")) {
                List<String> l = new ArrayList<>();
                if (args.length == 1) {
                    l.add("add");
                }
                return l;
            }
        }
        return null;
    }
}
