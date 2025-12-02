package game.item;

/**
 * Defines the loot type and their value.
 *
 * @author Alex Samuel
 * @version 1.0
 */
public enum LootType {
    CENT(25),
    DOLLAR(100),
    RUBY(250),
    DIAMOND(400);

    private final int value;

    LootType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
