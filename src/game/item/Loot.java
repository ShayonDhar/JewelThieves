package game.item;

/**
 * Loot class which holds all the loot functions
 * for the different loot functions and also allows loot
 * objects to be created.
 *
 * @author Elijah
 * @version 1.0.0
 */
public class Loot extends Item {
    private final LootType lootType;
    /**
     * Constructor that all the items will use.
     * It has all the properties they have in common
     * But each item will have different String, int or boolean for each attribute
     *
     * @param itemName is the name of the item, e.g. Gate
     * @param itemID   is the ID of the item, e.g. if there were 5 bombs, one ID would be 4
     * @param x        is the x coordinate of the location of the item on the map
     * @param y        is the y coordinate of the location of the item on the map
     * @param isOn     is a boolean that will either be true or false. It tells us
     *                 whether the item has been claimed or triggered.
     * @param lootType the loot type of the object being created
     */

    public Loot(String itemName, int itemID, int x, int y, boolean isOn, LootType lootType) {
        super(itemName, itemID, x, y, isOn, ItemType.LOOT);
        this.lootType = lootType;
    }

    /**
     * Gets the loot type.
     *
     * @return loot type
     */
    public LootType getLootType() {
        return lootType;
    }
}
