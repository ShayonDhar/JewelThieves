package game.item;

/**
 * Defines the loot type and their value.
 *
 * @author Alex Samuel
 * @version 1.0.0
 */
public enum LootType {
    CENT(25),
    DOLLAR(100),
    RUBY(250),
    DIAMOND(400);

    private final int value;

    /**
     * Constructor to make loot type, must have value.
     *
     * @param value of the loot
     */
    LootType(int value) {
        this.value = value;
    }

    /**
     * Gets the value of the loot type.
     *
     * @return value of loot type.
     */
    public int getValue() {
        return value;
    }
}
