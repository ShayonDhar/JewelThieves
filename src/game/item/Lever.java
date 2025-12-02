package game.item;

import game.entity.Entity;
import game.level.Colour;
import game.level.Level;
import javafx.scene.canvas.GraphicsContext;

/**
 * The lever class which implements all the lever functions
 * and allows the lever object to be created.
 *
 * @author Shayon Dar
 * @version 1.0
 */

public class Lever extends Item {
    private final Colour colour;
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
     * @param colour the colour of the lever
     */

    public Lever(String itemName, int itemID, int x, int y, boolean isOn, Colour colour) {
        super(itemName, itemID, x, y, isOn, ItemType.LEVER);
        this.colour = colour;
    }

    public Colour getColour() {
        return colour;
    }

    @Override
    public void collectItem(Entity entityName, Level level) {

    }
}
