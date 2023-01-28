package us.mytheria.blobrp.inventories.builder.reward;

import org.bukkit.entity.Player;
import us.mytheria.bloblib.BlobLibAssetAPI;
import us.mytheria.bloblib.entities.ObjectDirector;
import us.mytheria.bloblib.entities.inventory.BlobInventory;
import us.mytheria.bloblib.entities.inventory.ObjectBuilderButton;
import us.mytheria.bloblib.entities.message.BlobSound;
import us.mytheria.blobrp.BlobRPAPI;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.inventories.builder.RPObjectBuilder;
import us.mytheria.blobrp.reward.CashReward;
import us.mytheria.blobrp.reward.Reward;

import java.util.Optional;
import java.util.UUID;

public class CashRewardBuilder extends RPObjectBuilder<CashReward> {
    private boolean runsAsynchronously;

    public static CashRewardBuilder build(UUID builderId) {
        return new CashRewardBuilder(BlobRPAPI.buildInventory("CashRewardBuilder"), builderId);
    }

    private CashRewardBuilder(BlobInventory blobInventory, UUID builderId) {
        super(blobInventory, builderId);
        addQuickStringButton("Key", 300).addQuickMessageButton(
                        "Message", 3000).addQuickOptionalLongButton("Delay", 300)
                .addQuickOptionalDoubleButton("CashValue", 300)
                .setFunction(builder -> {
                    CashReward build = builder.build();
                    if (build == null)
                        return null;
                    Player player = getPlayer();
                    BlobSound sound = BlobLibAssetAPI.getSound("Builder.Build-Complete");
                    sound.play(player);
                    player.closeInventory();
                    build.saveToFile();
                    ObjectDirector<Reward> director = RPManagerDirector
                            .getInstance().getRewardDirector();
                    director.getObjectManager().addObject(build.getKey(), build);
                    director.getBuilderManager().removeBuilder(player);
                    return build;
                });
    }

    @SuppressWarnings("unchecked")
    @Override
    public CashReward build() {
        ObjectBuilderButton<String> keyButton = (ObjectBuilderButton<String>) getObjectBuilderButton("Key");
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
                delay, runsAsynchronously, message.map(BlobLibAssetAPI::getMessage));
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