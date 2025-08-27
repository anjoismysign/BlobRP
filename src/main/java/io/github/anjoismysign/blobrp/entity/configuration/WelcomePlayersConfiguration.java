package io.github.anjoismysign.blobrp.entity.configuration;

public class WelcomePlayersConfiguration {

    private boolean enabled;
    private String message;
    private WelcomeInventoryConfiguration inventory;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public WelcomeInventoryConfiguration getInventory() {
        return inventory;
    }

    public void setInventory(WelcomeInventoryConfiguration inventory) {
        this.inventory = inventory;
    }
}
