package game.item;
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
