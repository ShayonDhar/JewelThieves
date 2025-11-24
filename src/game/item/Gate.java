package game.item;

import javafx.scene.canvas.GraphicsContext;

public class Gate extends Item{
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
     */
    public Gate(String itemName, int itemID, int x, int y, boolean isOn, ItemType itemType) {
        super(itemName, itemID, x, y, isOn, ItemType.GATE);
    }

    @Override
    public void draw(GraphicsContext gc) {

    }

    @Override
    public void collectItem(Entity entityName) {
        
    }
}
