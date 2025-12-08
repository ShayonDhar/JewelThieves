package game.playerProfile;

/**
 * Represents a player profile containing the player's name
 * and their highest unlocked level.
 * A profile always has at least level 1 unlocked, and the
 * name is stored in trimmed form.
 *
 * @author Alex Samuel
 * @version 1.0
 */
public class PlayerProfile {

    private final String name;
    private int maxUnlockedLevel;

    /**
     * Creates a new profile with the given name and defaults
     * the maximum unlocked level to 1.
     *
     * @param name the player's name, not null
     */
    public PlayerProfile(String name) {
        this(name, 1);
    }

    /**
     * Creates a new profile with the given name and the specified
     * maximum unlocked level. The level is clamped to a minimum of 1.
     *
     * @param name              the player's name, not null
     * @param maxUnlockedLevel  initial unlocked level, at least 1
     */
    public PlayerProfile(String name, int maxUnlockedLevel) {
        this.name = name.trim();
        this.maxUnlockedLevel = Math.max(1, maxUnlockedLevel);
    }

    /**
     * Returns the player's name.
     *
     * @return the name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the highest unlocked level for this player.
     *
     * @return the maximum unlocked level, at least 1
     */
    public int getMaxUnlockedLevel() {
        return maxUnlockedLevel;
    }

    /**
     * Updates the highest unlocked level for this player.
     * The level is clamped to a minimum of 1.
     *
     * @param level the new unlocked level
     */
    public void setMaxUnlockedLevel(int level) {
        this.maxUnlockedLevel = Math.max(1, level);
    }

    /**
     * Returns the player's name as the string representation.
     *
     * @return the player's name
     */
    @Override
    public String toString() {
        return name;
    }
}
