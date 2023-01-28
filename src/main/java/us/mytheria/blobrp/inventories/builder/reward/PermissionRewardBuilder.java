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
import us.mytheria.blobrp.reward.PermissionReward;
import us.mytheria.blobrp.reward.Reward;

import java.util.Optional;
import java.util.UUID;

public class PermissionRewardBuilder extends RPObjectBuilder<PermissionReward> {
    private boolean runsAsynchronously;

    public static PermissionRewardBuilder build(UUID builderId) {
        return new PermissionRewardBuilder(BlobRPAPI.buildInventory("PermissionRewardBuilder"), builderId);
    }

    private PermissionRewardBuilder(BlobInventory blobInventory, UUID builderId) {
        super(blobInventory, builderId);
        addQuickStringButton("Key", 300)
                .addQuickMessageButton("Message", 300)
                .addQuickOptionalLongButton("Delay", 300)
                .addQuickMessageButton("PermissionValue", 300)
                .addQuickMessageButton("World", 300)
                .setFunction(builder -> {
                    PermissionReward build = builder.build();
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
    public PermissionReward build() {
        ObjectBuilderButton<String> keyButton = (ObjectBuilderButton<String>) getObjectBuilderButton("Key");
        ObjectBuilderButton<String> messageButton = (ObjectBuilderButton<String>) getObjectBuilderButton("Message");
        ObjectBuilderButton<Long> delayButton = (ObjectBuilderButton<Long>) getObjectBuilderButton("Delay");
        ObjectBuilderButton<String> permissionValueButton = (ObjectBuilderButton<String>) getObjectBuilderButton("PermissionValue");
        ObjectBuilderButton<String> worldButton = (ObjectBuilderButton<String>) getObjectBuilderButton("World");

        if (keyButton.get().isEmpty() || permissionValueButton.get().isEmpty())
            return null;

        String key = keyButton.get().get();
        Optional<String> message = messageButton.get();
        Optional<Long> delay = delayButton.get();
        String permission = permissionValueButton.get().get();
        Optional<String> world = worldButton.get();

        return PermissionReward.build(key, delay.isPresent(), permission, delay, runsAsynchronously,
                message.map(BlobLibAssetAPI::getMessage), world);
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