package us.mytheria.blobrp.inventories;

import org.bukkit.entity.Player;
import us.mytheria.bloblib.BlobLibAssetAPI;
import us.mytheria.bloblib.entities.ObjectDirector;
import us.mytheria.bloblib.entities.inventory.BlobInventory;
import us.mytheria.bloblib.entities.inventory.ObjectBuilderButton;
import us.mytheria.bloblib.entities.message.BlobSound;
import us.mytheria.bloblib.entities.message.ReferenceBlobMessage;
import us.mytheria.blobrp.RPShortcut;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.reward.PermissionReward;

import java.util.Optional;
import java.util.UUID;

public class PermissionRewardBuilder extends RPObjectBuilder<PermissionReward> {
    private boolean runsAsynchronously;

    public static PermissionRewardBuilder build(UUID builderId,
                                                ObjectDirector<PermissionReward> objectDirector) {
        return new PermissionRewardBuilder(
                RPShortcut.buildInventory("PermissionRewardBuilder"),
                builderId, objectDirector);
    }

    private PermissionRewardBuilder(BlobInventory blobInventory, UUID builderId,
                                    ObjectDirector<PermissionReward> objectDirector) {
        super(blobInventory, builderId, objectDirector);
        addQuickStringButton("Key", 300)
                .addQuickMessageButton("Message", 300)
                .addQuickLongButton("Delay", 300)
                .addQuickStringButton("PermissionValue", 300)
                .addQuickStringButton("World", 300)
                .setFunction(builder -> {
                    PermissionReward build = builder.construct();
                    if (build == null)
                        return null;
                    Player player = getPlayer();
                    BlobSound sound = BlobLibAssetAPI.getSound("Builder.Build-Complete");
                    sound.play(player);
                    player.closeInventory();
                    ObjectDirector<PermissionReward> director = RPManagerDirector
                            .getInstance().getPermissionRewardDirector();
                    build.saveToFile(director.getObjectManager().getLoadFilesDirectory());
                    director.getObjectManager().addObject(build.getKey(), build);
                    director.getBuilderManager().removeBuilder(player);
                    return build;
                });
    }

    @SuppressWarnings("unchecked")
    @Override
    public PermissionReward construct() {
        ObjectBuilderButton<String> keyButton = (ObjectBuilderButton<String>) getObjectBuilderButton("Key");
        ObjectBuilderButton<ReferenceBlobMessage> messageButton = (ObjectBuilderButton<ReferenceBlobMessage>) getObjectBuilderButton("Message");
        ObjectBuilderButton<Long> delayButton = (ObjectBuilderButton<Long>) getObjectBuilderButton("Delay");
        ObjectBuilderButton<String> permissionValueButton = (ObjectBuilderButton<String>) getObjectBuilderButton("PermissionValue");
        ObjectBuilderButton<String> worldButton = (ObjectBuilderButton<String>) getObjectBuilderButton("World");

        if (keyButton.get().isEmpty() || permissionValueButton.get().isEmpty())
            return null;

        String key = keyButton.get().get();
        Optional<ReferenceBlobMessage> message = messageButton.get();
        Optional<Long> delay = delayButton.get();
        String permission = permissionValueButton.get().get();
        Optional<String> world = worldButton.get();

        return PermissionReward.build(key, delay.isPresent(), permission, delay, runsAsynchronously,
                message, world);
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