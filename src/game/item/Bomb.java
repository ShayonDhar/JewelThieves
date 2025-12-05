package game.item;

import game.GameController;
import game.entity.Entity;
import game.entity.Player;
import game.level.Level;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * The bomb class which implements all the bomb
 * functions.
 *
 * @author Alex Samuel
 * @version 1.0.0
 */
public class Bomb extends Item {
    private static final int BOMB_COUNTDOWN = 4; // Not 3 to account for trigger time
    private static final int TRIGGERED_TIME = 3;
    private BombState state;
    private int countdown = BOMB_COUNTDOWN;

    /**
     * Constructor that all the items will use.
     * It has all the properties they have in common
     * But each item will have different String, int
     * or boolean for each attribute.
     *
     * @param itemName is the name of the item, e.g. Gate
     * @param itemID   is the ID of the item,
     *                 e.g. if there were 5 bombs, one ID would be 4
     * @param x        is the x coordinate of the location of the item on the map
     * @param y        is the y coordinate of the location of the item on the map
     * @param isOn     is a boolean that will either be true or false. It tells us
     *                 whether the item has been claimed or triggered.
     */
    public Bomb(String itemName, int itemID, int x, int y, boolean isOn) {
        super(itemName, itemID, x, y, isOn, ItemType.BOMB);
        this.state = BombState.WAITING;
    }

    /**
     * The method which collects the item.
     *
     * @param entityName The name of the entity being collected
     * @param level The current active level
     */

    @Override
    public void collectItem(Entity entityName, Level level) {

        if (!isOn) {
            return;
        }

        if (entityName.getX() == x && entityName.getY() == y) {
            trigger();
            updateBombState(level);
            entityName.getLevel().destroyTileContent(entityName.getX(), entityName.getY());
        }

        isOn = false;
    }

    public BombState getState() {
        return state;
    }

    public void setState(BombState state) {
        this.state = state;
    }

    /**
     * The method which updates the bombs state.
     *
     * @param level the level the bomb is current on
     */

    public void updateBombState(Level level) {
        switch (state) {
            case WAITING, EXPLODED:
                break;
            case COUNTING:
                countdown--;
                if (countdown <= 0) {
                    explode(level);
                }
                break;
            default:
                System.out.println("Game error on updateBombState()");
                break;
        }


    }

    /**
     * Method which triggers the bomb.
     */
    public void trigger() {
        if (state == BombState.WAITING) {
            state = BombState.COUNTING;
        }
    }

    /**
     * The method which uses the trigger method
     * to cause the bomb to explode.
     *
     * @param level the level object the bomb is currently on
     */
    public void explode(Level level) {
        if (this.state == BombState.EXPLODED) {
            return;
        }
        this.state = BombState.EXPLODED;

        int bombX = this.getX();
        int bombY  = this.getY();

        List<Item> itemsToExplode = new ArrayList<>(level.getAllItems());

        level.handleExplosion(bombX, bombY);
        level.notifyExplosion(bombX, bombY);

        level.removeItemFromGrid(this.getY(), this.getX());

    }

    /**
     * Gets the bomb image and countdown to be displayed on the level.
     *
     * @return the item image
     */
    @Override
    public Node getSprite() {
        if (sprite == null) {
            sprite = new ImageView(Player.class.getResource("/game/resources/"
                    + itemName.toLowerCase() + ".png").toExternalForm());
            sprite.setFitWidth(SPRITE_WIDTH_HEIGHT);
            sprite.setFitHeight(SPRITE_WIDTH_HEIGHT);
        }

        // Countdown text
        Text countdownText = new Text(String.valueOf(countdown));
        countdownText.setFill(Color.ORANGE);
        countdownText.setFont(Font.font("Arial", FontWeight.BOLD, 35));
        countdownText.setStroke(Color.BLACK);   // outline for visibility
        countdownText.setStrokeWidth(1);

        // Hiding the countdown until bomb is triggered
        if (countdown <= TRIGGERED_TIME) {
            countdownText.setText(String.valueOf(countdown));
        } else {
            countdownText.setText("");
        }

        // Center the text on top of the image

        return new StackPane(sprite, countdownText);
    }
}
