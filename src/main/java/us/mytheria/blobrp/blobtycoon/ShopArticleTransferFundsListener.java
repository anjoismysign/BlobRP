package us.mytheria.blobrp.blobtycoon;

import net.milkbowl.vault.economy.IdentityEconomy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import us.mytheria.bloblib.api.BlobLibEconomyAPI;
import us.mytheria.blobrp.director.manager.ConfigManager;
import us.mytheria.blobrp.events.ShopArticleSaleFailEvent;
import us.mytheria.blobrp.listeners.RPListener;
import us.mytheria.blobtycoon.BlobTycoonInternalAPI;
import us.mytheria.blobtycoon.entity.PlotProfile;
import us.mytheria.blobtycoon.entity.TycoonPlayer;

import java.util.Optional;

public class ShopArticleTransferFundsListener extends RPListener {

    public ShopArticleTransferFundsListener(ConfigManager configManager) {
        super(configManager);
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        if (getConfigManager().blobTycoonShopArticleTransferFunds().register())
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
    }

    @EventHandler
    public void onSaleFail(ShopArticleSaleFailEvent event) {
        if (event.isFixed())
            return;
        Player player = event.getPlayer();
        TycoonPlayer tycoonPlayer = BlobTycoonInternalAPI.getInstance().getTycoonPlayer(player);
        if (tycoonPlayer == null || tycoonPlayer.getProfile() == null)
            return;
        PlotProfile plotProfile = tycoonPlayer.getProfile().getPlotProfile();
        String currency = event.getCurrency() == null ? "default" : event.getCurrency();
        double amount = event.getAmount();
        if (!plotProfile.withdrawValuable(currency, amount))
            return;
        IdentityEconomy economy = BlobLibEconomyAPI.getInstance().getElasticEconomy().map(Optional.ofNullable(currency));
        economy.depositPlayer(player, amount);
        event.setFixed(true);
    }
}
