package us.mytheria.blobrp.inventories.builder.reward;

import org.bukkit.entity.Player;
import us.mytheria.bloblib.BlobLibAPI;
import us.mytheria.bloblib.entities.ObjectDirector;
import us.mytheria.bloblib.entities.inventory.BlobInventory;
import us.mytheria.bloblib.entities.inventory.ObjectBuilderButton;
import us.mytheria.bloblib.entities.inventory.ObjectBuilderButtonBuilder;
import us.mytheria.bloblib.entities.message.BlobSound;
import us.mytheria.bloblib.entities.message.ReferenceBlobMessage;
import us.mytheria.blobrp.BlobRPAPI;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.inventories.builder.RPObjectBuilder;
import us.mytheria.blobrp.reward.PermissionReward;
import us.mytheria.blobrp.reward.Reward;

import java.util.UUID;

public class PermissionRewardBuilder extends RPObjectBuilder<PermissionReward> {
    private boolean shouldDelay;
    private boolean runsAsynchronously;
    private boolean containsMessage;
    private boolean useWorld;
    private boolean useCurrentWorld;

    public static PermissionRewardBuilder build(UUID builderId) {
        return new PermissionRewardBuilder(BlobRPAPI.buildInventory("CashRewardBuilder"), builderId);
    }

    private PermissionRewardBuilder(BlobInventory blobInventory, UUID builderId) {
        super(blobInventory, builderId);
        ObjectBuilderButton<String> keyButton = ObjectBuilderButtonBuilder.STRING("Key",
                300, "Builder.Key-Timeout",
                "Builder.Key", string -> {
                    updateDefaultButton("Key", "%key%",
                            string == null ? "N/A" : string);
                    openInventory();
                    return true;
                });
        ObjectBuilderButton<ReferenceBlobMessage> messageButton =
                ObjectBuilderButtonBuilder.MESSAGE(
                        "Message",
                        300,
                        "Builder.Message-Timeout",
                        "Builder.Message",
                        message -> {
                            updateDefaultButton("Message", "%message%",
                                    message == null ? "N/A" : message.getReference());
                            openInventory();
                            return true;
                        });
        ObjectBuilderButton<Long> delay = ObjectBuilderButtonBuilder.LONG("CustomModelData",
                300, "Builder.CustomModelData-Timeout",
                "Builder.CustomModelData", integer -> {
                    updateDefaultButton("CustomModelData", "%customModelData%",
                            "" + integer);
                    openInventory();
                    return true;
                });
        ObjectBuilderButton<String> worldNameButton = ObjectBuilderButtonBuilder.STRING(
                "CashValue",
                300, "Builder.CashValue-Timeout",
                "Builder.CashValue", value -> {
                    updateDefaultButton("CashValue", "%cash%",
                            value == null ? "N/A" : value + value);
                    openInventory();
                    return true;
                });
        addObjectBuilderButton(keyButton).addObjectBuilderButton(messageButton)
                .addObjectBuilderButton(delay).addObjectBuilderButton(worldNameButton)
                .setFunction(builder -> {
                    PermissionReward build = builder.build();
                    if (build == null)
                        return null;
                    Player player = getPlayer();
                    BlobSound sound = BlobLibAPI.getSound("Builder.Build-Complete");
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

    public boolean shouldDelay() {
        return shouldDelay;
    }

    public void setShouldDelay(boolean shouldDelay) {
        this.shouldDelay = shouldDelay;
        updateDefaultButton("ShouldDelay", "%shouldDelay%", shouldDelay() ? "Yes" : "No");
        openInventory();
    }

    public boolean runsAsynchronously() {
        return runsAsynchronously;
    }

    public void setRunsAsynchronously(boolean runsAsynchronously) {
        this.runsAsynchronously = runsAsynchronously;
        updateDefaultButton("RunsAsynchronously", "%runsAsynchronously%", runsAsynchronously() ? "Yes" : "No");
        openInventory();
    }

    public boolean containsMessage() {
        return containsMessage;
    }

    public void setContainsMessage(boolean containsMessage) {
        this.containsMessage = containsMessage;
        updateDefaultButton("ContainsMessage", "%containsMessage%", containsMessage() ? "Yes" : "No");
        openInventory();
    }
}