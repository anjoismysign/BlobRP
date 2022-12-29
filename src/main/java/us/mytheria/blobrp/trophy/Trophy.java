package us.mytheria.blobrp.trophy;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import us.mytheria.blobrp.reward.Reward;
import us.mytheria.blobrp.trophy.requirements.TrophyRequirement;

import java.util.List;

/**
 * The Trophy class represents a trophy that is hunted for money.
 */
public class Trophy {
    // The type of entity that is being hunted (e.g. lion, elephant)
    private final EntityType type;

    // The rewards that are offered for hunting the trophy
    private final List<Reward> rewards;

    // The requirements that must be met in order to receive the reward for hunting the trophy
    private final TrophyRequirement requirements;

    /**
     * Constructs a new Trophy with the given type, reward, and requirements.
     *
     * @param type         the type of entity that is being hunted
     * @param rewards      the rewards that are offered for hunting the trophy
     * @param requirements the requirements that must be met in order to receive the reward
     */
    public Trophy(EntityType type, List<Reward> rewards, TrophyRequirement requirements) {
        this.type = type;
        this.rewards = rewards;
        this.requirements = requirements;
    }

    /**
     * Returns the type of entity that is being hunted.
     *
     * @return the type of entity that is being hunted
     */
    public EntityType getType() {
        return type;
    }

    /**
     * Returns the reward that is offered for hunting the trophy.
     *
     * @return the reward that is offered for hunting the trophy
     */
    public List<Reward> getRewards() {
        return rewards;
    }

    /**
     * Returns the requirements that must be met in order to receive the reward for hunting the trophy.
     *
     * @return the requirements that must be met in order to receive the reward
     */
    public TrophyRequirement getRequirements() {
        return requirements;
    }

    /**
     * Will reward the player with the rewards that are offered for hunting the trophy.
     *
     * @param player the player that is being rewarded
     */
    public void reward(Player player) {
        rewards.forEach(reward -> reward.applyAndMessage(player));
    }

    /**
     * Will check if the given entity is the same type as the trophy.
     * If met the requirements, reward is given.
     *
     * @param player the player that is being rewarded
     * @param entity the entity that is being checked
     */
    public void checkAndReward(Player player, Entity entity) {
        if (!requirements.meetsRequirements(entity))
            return;
        reward(player);
    }
}