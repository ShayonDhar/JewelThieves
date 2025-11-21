package game.entity;

/**
 * Represents the possible names of an entity.
 *
 * @author Shayon Dhar
 * @version 1.0.0
 */
public enum EntityName {
    PLAYER("Player"),
    FLYING_ASSASSIN("Flying Assassin"),
    FLOOR_FOLLOWING_THIEF("Floor Following Thief"),
    SMART_THIEF("Smart Thief");

    private final String displayName;

    /**
     * Creates an entity name constant with a display name.
     *
     * @param displayName the user-friendly name of the entity
     */
    EntityName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the display name of this entity.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Converts a string into the matching Entity name
     *
     * @param name the string to convert
     * @return the matching {@code EntityName}
     * @throws IllegalArgumentException if no constant matches the given string
     */
    public static EntityName fromString(String name) {
        for (EntityName e : values()) {
            if (e.displayName.equalsIgnoreCase(name)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Unknown entity name: " + name);
    }

    /**
     * Returns the display name of this entity.
     *
     * @return the display name
     */
    @Override
    public String toString() {
        return displayName;
    }
}
