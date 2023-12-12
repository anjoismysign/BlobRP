package us.mytheria.blobrp.inventories;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.mytheria.bloblib.api.BlobLibInventoryAPI;
import us.mytheria.bloblib.api.BlobLibSoundAPI;
import us.mytheria.bloblib.entities.ObjectDirector;
import us.mytheria.bloblib.entities.inventory.BlobInventory;
import us.mytheria.bloblib.entities.inventory.ObjectBuilderButton;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.reward.CashReward;

import java.util.Optional;
import java.util.UUID;

public class CashRewardBuilder extends RPObjectBuilder<CashReward> {
    private boolean runsAsynchronously;

    public static CashRewardBuilder build(UUID builderId,
                                          ObjectDirector<CashReward> objectDirector,
                                          RPManagerDirector managerDirector) {
        Player player = Bukkit.getPlayer(builderId);
        return new CashRewardBuilder(
                BlobLibInventoryAPI.getInstance().buildInventory("CashRewardBuilder", player.getLocale()),
                builderId, objectDirector,
                managerDirector);
    }

    private CashRewardBuilder(BlobInventory blobInventory, UUID builderId,
                              ObjectDirector<CashReward> objectDirector,
                              RPManagerDirector managerDirector) {
        super(blobInventory, builderId, objectDirector, managerDirector);
        try {
            addQuickStringButton("Key", 300)
                    .addQuickStringButton("Message", 300)
                    .addQuickStringButton("Currency", 300)
                    .addQuickLongButton("Delay", 300)
                    .addQuickDoubleButton("CashValue", 300)
                    .setFunction(builder -> {
                        CashReward build = builder.construct();
                        if (build == null)
                            return null;
                        Player player = getPlayer();
                        BlobLibSoundAPI.getInstance().getSound("Builder.Build-Complete").handle(player);
                        player.closeInventory();
                        ObjectDirector<CashReward> director = managerDirector
                                .getCashRewardDirector();
                        build.saveToFile(director.getObjectManager().getLoadFilesDirectory());
                        director.getObjectManager().addObject(build.getKey(), build);
                        director.getBuilderManager().removeBuilder(player);
                        return build;
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public CashReward construct() {
        ObjectBuilderButton<String> keyButton = (ObjectBuilderButton<String>) getObjectBuilderButton("Key");
        ObjectBuilderButton<String> currencyButton = (ObjectBuilderButton<String>) getObjectBuilderButton("Currency");
        ObjectBuilderButton<String> messageButton = (ObjectBuilderButton<String>) getObjectBuilderButton("Message");
        ObjectBuilderButton<Long> delayButton = (ObjectBuilderButton<Long>) getObjectBuilderButton("Delay");
        ObjectBuilderButton<Double> cashValueButton = (ObjectBuilderButton<Double>) getObjectBuilderButton("CashValue");

        if (keyButton.get().isEmpty() || cashValueButton.get().isEmpty())
            return null;

        String key = keyButton.get().get();
        Optional<String> message = messageButton.get();
        Optional<Long> delay = delayButton.get();
        boolean shouldDelay = delay.isPresent();
        Double cashValue = cashValueButton.get().get();
        return CashReward.build(key, shouldDelay, cashValue,
                delay, runsAsynchronously, message, currencyButton.get());
    }

    public boolean runsAsynchronously() {
        return runsAsynchronously;
    }

    public void setRunsAsynchronously(boolean runsAsynchronously) {
        this.runsAsynchronously = runsAsynchronously;
        updateDefaultButton("RunsAsynchronously", "%runsAsynchronously%", runsAsynchronously() ? "Yes" : "No");
        openInventory();
    }
}