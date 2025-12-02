package game.item;

import game.entity.Entity;
import game.entity.Player;
import game.level.Level;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
 * @version 1.0.0
 */
public abstract class Item {
    /**
     * All the attributes that all the items have in common.
     */
    private static final int SPRITE_WIDTH_HEIGHT = 32;
    public boolean isOn;
    protected String itemName;
    protected int itemID;
    protected int x;
    protected int y;
    protected ItemType itemType;
    protected ImageView sprite;

    /**
     * Constructor that all the items will use.
     * It has all the properties they have in common
     * But each item will have different String, int or boolean for each attribute
     *
     * @param itemName name of the item for example gate.
     * @param itemID is the ID of the item, e.g. if there were 5 bombs, one ID would be 4
     * @param x is the x coordinate of the location of the item on the map
     * @param y is the y coordinate of the location of the item on the map
     * @param isOn is a boolean that will either be true or false. It tells us
     *             whether the item has been claimed or triggered.
     * @param itemType The type of item e.g. Gate
     */
    public Item(String itemName, int itemID, int x, int y, boolean isOn, ItemType itemType) {
        this.itemName = itemName;
        this.itemID = itemID;
        this.x = x;
        this.y = y;
        this.isOn = isOn;
        this.itemType = itemType;
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

    public ItemType getItemType() {
        return itemType;
    }

    /**
     * Gets the item image to be displayed on the level.
     *
     * @return the item image
     */

    public Node getSprite() {
        if (sprite == null) {
            sprite = new ImageView(Player.class.getResource("/game/resources/"
                    + itemName.toLowerCase() + ".png").toExternalForm());
            sprite.setFitWidth(SPRITE_WIDTH_HEIGHT);
            sprite.setFitHeight(SPRITE_WIDTH_HEIGHT);
        }
        return sprite;
    }

    /**
     * Method which allows the item to be displayed on javaFX.
     *
     * @param gc The game controller
     */

    public abstract void draw(GraphicsContext gc);

    /**
     * Abstract method which Collects the item when interacted with player/NPC.
     *
     * @param entityName The name of the entity
     * @param level the level the item was collected on
     */

    public abstract void collectItem(Entity entityName, Level level);
}
