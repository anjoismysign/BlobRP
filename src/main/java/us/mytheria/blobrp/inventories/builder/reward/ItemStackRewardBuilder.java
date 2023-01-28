package us.mytheria.blobrp.inventories.builder.reward;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.mytheria.bloblib.BlobLibAssetAPI;
import us.mytheria.bloblib.entities.ObjectDirector;
import us.mytheria.bloblib.entities.inventory.BlobInventory;
import us.mytheria.bloblib.entities.inventory.ObjectBuilderButton;
import us.mytheria.bloblib.entities.message.BlobSound;
import us.mytheria.blobrp.BlobRPAPI;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.inventories.builder.RPObjectBuilder;
import us.mytheria.blobrp.reward.ItemStackReward;
import us.mytheria.blobrp.reward.Reward;

import java.util.Optional;
import java.util.UUID;

public class ItemStackRewardBuilder extends RPObjectBuilder<ItemStackReward> {
    private boolean runsAsynchronously;

    public static ItemStackRewardBuilder build(UUID builderId) {
        return new ItemStackRewardBuilder(BlobRPAPI.buildInventory("ItemStackRewardBuilder"), builderId);
    }

    private ItemStackRewardBuilder(BlobInventory blobInventory, UUID builderId) {
        super(blobInventory, builderId);
        addQuickStringButton("Key", 300)
                .addQuickMessageButton("Message", 300)
                .addQuickOptionalLongButton("Delay", 300)
                .addQuickItemButton("ItemStackValue")
                .setFunction(builder -> {
                    ItemStackReward build = builder.build();
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
    public ItemStackReward build() {
        ObjectBuilderButton<String> keyButton = (ObjectBuilderButton<String>) getObjectBuilderButton("Key");
        ObjectBuilderButton<String> messageButton = (ObjectBuilderButton<String>) getObjectBuilderButton("Message");
        ObjectBuilderButton<Long> delayButton = (ObjectBuilderButton<Long>) getObjectBuilderButton("Delay");
        ObjectBuilderButton<ItemStack> itemStackButton =
                (ObjectBuilderButton<ItemStack>) getObjectBuilderButton("ItemStackValue");

        if (keyButton.get().isEmpty() || itemStackButton.get().isEmpty())
            return null;

        String key = keyButton.get().get();
        Optional<String> message = messageButton.get();
        Optional<Long> delay = delayButton.get();
        ItemStack itemStack = itemStackButton.get().get();

        return ItemStackReward.build(key, delay.isPresent(),
                itemStack, delay, runsAsynchronously, message.map(BlobLibAssetAPI::getMessage));
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