package us.mytheria.blobrp.inventories;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.mytheria.bloblib.BlobLibAssetAPI;
import us.mytheria.bloblib.entities.ObjectDirector;
import us.mytheria.bloblib.entities.inventory.BlobInventory;
import us.mytheria.bloblib.entities.inventory.ObjectBuilderButton;
import us.mytheria.bloblib.entities.message.BlobSound;
import us.mytheria.bloblib.entities.message.ReferenceBlobMessage;
import us.mytheria.blobrp.RPShortcut;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.reward.ItemStackReward;

import java.util.Optional;
import java.util.UUID;

public class ItemStackRewardBuilder extends RPObjectBuilder<ItemStackReward> {
    private boolean runsAsynchronously;

    public static ItemStackRewardBuilder build(UUID builderId,
                                               ObjectDirector<ItemStackReward> objectDirector) {
        return new ItemStackRewardBuilder(
                RPShortcut.buildInventory("ItemStackRewardBuilder"),
                builderId, objectDirector);
    }

    private ItemStackRewardBuilder(BlobInventory blobInventory, UUID builderId,
                                   ObjectDirector<ItemStackReward> objectDirector) {
        super(blobInventory, builderId, objectDirector);
        addQuickStringButton("Key", 300)
                .addQuickMessageButton("Message", 300)
                .addQuickLongButton("Delay", 300)
                .addQuickItemButton("ItemStack")
                .setFunction(builder -> {
                    ItemStackReward build = builder.construct();
                    if (build == null)
                        return null;
                    Player player = getPlayer();
                    BlobSound sound = BlobLibAssetAPI.getSound("Builder.Build-Complete");
                    sound.play(player);
                    player.closeInventory();
                    ObjectDirector<ItemStackReward> director = RPManagerDirector
                            .getInstance().getItemStackRewardDirector();
                    build.saveToFile(director.getObjectManager().getLoadFilesDirectory());
                    director.getObjectManager().addObject(build.getKey(), build);
                    director.getBuilderManager().removeBuilder(player);
                    return build;
                });
    }

    @SuppressWarnings("unchecked")
    @Override
    public ItemStackReward construct() {
        ObjectBuilderButton<String> keyButton = (ObjectBuilderButton<String>) getObjectBuilderButton("Key");
        ObjectBuilderButton<ReferenceBlobMessage> messageButton = (ObjectBuilderButton<ReferenceBlobMessage>) getObjectBuilderButton("Message");
        ObjectBuilderButton<Long> delayButton = (ObjectBuilderButton<Long>) getObjectBuilderButton("Delay");
        ObjectBuilderButton<ItemStack> itemStackButton =
                (ObjectBuilderButton<ItemStack>) getObjectBuilderButton("ItemStack");

        if (keyButton.get().isEmpty() || itemStackButton.get().isEmpty())
            return null;

        String key = keyButton.get().get();
        Optional<ReferenceBlobMessage> message = messageButton.get();
        Optional<Long> delay = delayButton.get();
        ItemStack itemStack = itemStackButton.get().get();

        return ItemStackReward.build(key, delay.isPresent(),
                itemStack, delay, runsAsynchronously, message);
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