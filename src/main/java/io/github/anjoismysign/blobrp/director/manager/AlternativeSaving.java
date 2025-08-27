package io.github.anjoismysign.blobrp.director.manager;

import io.github.anjoismysign.bloblib.SoulAPI;
import io.github.anjoismysign.bloblib.api.BlobLibInventoryAPI;
import io.github.anjoismysign.bloblib.api.BlobLibMessageAPI;
import io.github.anjoismysign.bloblib.entities.inventory.InventoryBuilderCarrier;
import io.github.anjoismysign.bloblib.entities.inventory.MetaBlobPlayerInventoryBuilder;
import io.github.anjoismysign.bloblib.entities.inventory.MetaInventoryButton;
import io.github.anjoismysign.bloblib.entities.translatable.TranslatableItem;
import io.github.anjoismysign.bloblib.managers.cruder.BukkitCruder;
import io.github.anjoismysign.bloblib.managers.cruder.BukkitCruderBuilder;
import io.github.anjoismysign.blobrp.BlobRP;
import io.github.anjoismysign.blobrp.director.RPManager;
import io.github.anjoismysign.blobrp.director.RPManagerDirector;
import io.github.anjoismysign.blobrp.entity.configuration.AlternativeSavingConfiguration;
import io.github.anjoismysign.blobrp.entity.configuration.RoleplayConfiguration;
import io.github.anjoismysign.blobrp.entity.configuration.WelcomeInventoryConfiguration;
import io.github.anjoismysign.blobrp.entity.configuration.WelcomePlayersConfiguration;
import io.github.anjoismysign.blobrp.entity.serialplayer.SerialPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLocaleChangeEvent;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class AlternativeSaving extends RPManager implements Listener {
    private final String reference = "WelcomeInventory";
    private final BukkitCruder<SerialPlayer> serialPlayerCruder;

    public AlternativeSaving(RPManagerDirector director) {
        super(director);
        serialPlayerCruder = new BukkitCruderBuilder<SerialPlayer>()
                .crudableClass(SerialPlayer.class)
                .plugin(BlobRP.getInstance())
                .onJoin(serialPlayer -> {
                    Bukkit.getScheduler().runTask(BlobRP.getInstance(), ()->{
                        @Nullable Player player = serialPlayer.getPlayer();
                        if (player == null){
                            return;
                        }
                        WelcomePlayersConfiguration configuration = RoleplayConfiguration.getInstance().getAlternativeSavingConfiguration().getWelcomePlayers();
                        serialPlayer.loadProfile(player, serialPlayer.getSelectedProfile());
                        if (configuration.isEnabled()) {
                            BlobLibMessageAPI.getInstance()
                                    .getMessage(configuration.getMessage(), player)
                                    .modder()
                                    .replace("%player%", player.getName())
                                    .get()
                                    .handle(player);
                        }
                        WelcomeInventoryConfiguration inventoryConfiguration = configuration.getInventory();
                        if (inventoryConfiguration.isEnabled()) {
                            InventoryBuilderCarrier<MetaInventoryButton> carrier = BlobLibInventoryAPI
                                    .getInstance().getMetaInventoryBuilderCarrier(reference, player.getLocale());
                            Objects.requireNonNull(carrier, "'" + reference + "' cannot be null");
                            MetaBlobPlayerInventoryBuilder.fromInventoryBuilderCarrier
                                    (carrier, player.getUniqueId());
                            if (inventoryConfiguration.isSoul()){
                                SoulAPI.getInstance().set(player);
                            }
                        }
                    });
                })
                .onAutoSave(serialPlayer -> {
                    @Nullable Player player = serialPlayer.getPlayer();
                    if (player == null){
                        return;
                    }
                    serialPlayer.saveCurrentProfile(player, true);
                })
                .onQuit(serialPlayer -> {
                    @Nullable Player player = serialPlayer.getPlayer();
                    if (player == null){
                        return;
                    }
                    serialPlayer.saveCurrentProfile(player, true);
                })
                .build();
        reload();
    }

    @Override
    public void reload() {
        HandlerList.unregisterAll(this);
        AlternativeSavingConfiguration configuration = RoleplayConfiguration.getInstance().getAlternativeSavingConfiguration();
        if (configuration.isEnabled()){
            Bukkit.getPluginManager().registerEvents(this, getPlugin());
        }
    }

    @Override
    public void unload() {
        serialPlayerCruder.saveAll();
    }

    @EventHandler
    public void onLocale(PlayerLocaleChangeEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        for (int index = 0; index < inventory.getSize(); index++) {
            TranslatableItem.localize(inventory.getItem(index), player.getLocale());
        }
    }
}
