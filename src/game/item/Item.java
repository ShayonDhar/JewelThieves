package game.item;

import game.entity.Entity;
import game.level.Level;
import javafx.scene.canvas.GraphicsContext;

/**
 * Item Class
 * Contains all the items that the player and NPCs can interact with
 * in the game.
 * It is an abstract class
 * Contains all the abilities of the items.
 * All the items in the game include: Loot, Bomb, Clock, Lever, Gate & Door
 * Possible Enumerated classes required to hold the states and properties of the items
 *
 * @author Elijah Ogunjebe
 * @author Talal Alshammari
 * @version 1.0.0
 */
public abstract class Item {

    /**
     * All the attributes that all the items have in common
     */
    protected String itemName;
    protected int itemID;
    protected int x;
    protected int y;
    protected boolean isOn;

    /**
     * Constructor that all the items will use.
     * It has all the properties they have in common
     * But each item will have different String, int or boolean for each attribute
     * @param itemName is the name of the item, e.g. Gate
     * @param itemID is the ID of the item, e.g. if there were 5 bombs, one ID would be 4
     * @param x is the x coordinate of the location of the item on the map
     * @param y is the y coordinate of the location of the item on the map
     * @param isOn is a boolean that will either be true or false. It tells us
     *             whether the item has been claimed or triggered.
     */
    public Item(String itemName, int itemID, int x, int y, boolean isOn) {
        this.itemName = itemName;
        this.itemID = itemID;
        this.x = x;
        this.y = y;
        this.isOn = isOn;
    }
    public String getItemName() {
        return itemName;
    }
    public int getItemID() {
        return itemID;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public abstract void draw(GraphicsContext gc);

    public abstract void collectItem(Entity entityName, Level level);
}
