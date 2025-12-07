package game.item;

import game.entity.Player;
import game.level.Colour;
import javafx.scene.Node;
import javafx.scene.image.ImageView;

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

    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    public Lever(String itemName, int itemID, int x, int y, boolean isOn, Colour colour) {
        super(itemName, itemID, x, y, isOn, ItemType.LEVER);
        this.colour = colour;
    }

    public Colour getColour() {
        return colour;
    }

    /**
     * Gets the item image to be displayed on the level.
     *
     * @return the item image
     */
    @Override
    public Node getSprite() {
        if (sprite == null) {
            sprite = new ImageView(Player.class.getResource("/game/resources/"
                    + itemName.toLowerCase() + ".png").toExternalForm());
            sprite.getStyleClass().add("image-tint-" + colour.name());
            sprite.setFitWidth(SPRITE_WIDTH_HEIGHT);
            sprite.setFitHeight(SPRITE_WIDTH_HEIGHT);
        }
        return sprite;
    }
}
