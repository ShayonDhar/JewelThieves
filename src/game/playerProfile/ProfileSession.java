package game.playerProfile;

/**
 * A utility class for managing the currently active player profile in the game session.
 * Provides static methods to set and retrieve the active profile and its name.
 * This class is designed to hold the profile in memory for the duration of the application's runtime.
 *
 * @author Alex Samuel
 * @version 1.0
 */
public class ProfileSession {
    /**
     * The currently active player profile.
     */
    private static PlayerProfile current;

    /**
     * Sets the currently active player profile.
     *
     * @param player The PlayerProfile to set as the active profile.
     */
    public static void set(PlayerProfile player) {
        current = player;
    }

    /**
     * Retrieves the currently active player profile.
     *
     * @return The active PlayerProfile, or null if none has been set.
     */
    public static PlayerProfile getProfile() {
        return current;
    }

    /**
     * Retrieves the name of the currently active player profile.
     *
     * @return The name of the active player profile, or throws NullPointerException if no profile is set.
     */
    public static String getCurrentName() {
        return current.getName();
    }
}


