package us.mytheria.blobrp.director.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import us.mytheria.blobrp.BlobRP;
import us.mytheria.blobrp.inventories.builder.ShopArticleBuilder;

import java.util.ArrayList;
import java.util.List;

public class ShopArticleCmd implements CommandExecutor, TabCompleter {
    private BlobRP main;

    public ShopArticleCmd() {
        this.main = BlobRP.getInstance();
        main.getCommand("shoparticle").setExecutor(this);
        main.getCommand("shoparticle").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("blobrp.admin")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou don't have permission to do that!"));
            return true;
        }
        if (args.length < 1) {
            debug(sender);
            return true;
        }
        String arg1 = args[0];
        if (arg1.equalsIgnoreCase("add")) {
            if (!(sender instanceof Player player)) {
                Bukkit.getLogger().info("You must be a player to use this command");
                return true;
            }
            ShopArticleBuilder builder = main.getDirector()
                    .getBuilderManager().getShopArticleBuilderManager().getOrDefault(player);
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