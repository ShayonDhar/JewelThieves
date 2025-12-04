package game.playerProfile;

/**
 * PlayerProfile which allows playerProfiles to be created.
 *
 * @author Alex Samuel
 * @version 1.0
 */

public class PlayerProfile {
    protected String name;
    protected int maxLevelUnlocked;

    /**
     * Player profile constructor
     * The player is automatically assigned with level 1 unlocked.
     *
     * @param name the name of the player
     */
    public PlayerProfile(String name) {
        this.name = name;
        this.maxLevelUnlocked = 1;
    }

    public String getName() {
        return name;
    }

    public int getMaxLevelUnlocked() {
        return maxLevelUnlocked;
    }
}
