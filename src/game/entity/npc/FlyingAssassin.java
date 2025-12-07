package game.entity.npc;

import game.entity.Direction;
import game.entity.EntityName;
import game.entity.Player;
import game.level.Level;
import java.util.Objects;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents the Flying Assassin NPC and its unique abilities.
 *
 * @author Shayon Dhar
 * @version 1.0.0
 */
public class FlyingAssassin extends NPC {
    private static final int SPRITE_SIZE = 35;
    /**
     * Constructor to create an Entity object.
     *
     * @param x              x coordinate of the entity
     * @param y              y coordinate of the entity
     * @param direction      direction the entity is facing
     * @param alive          the alive state of the entity
     * @param blocksMovement whether the entity blocks movement of other entities
     * @param level The level the entity is currently on
     */

    public FlyingAssassin(int x, int y, Direction direction, boolean alive, boolean blocksMovement, Level level) {
        super(EntityName.FLYING_ASSASSIN, x, y, direction, alive, blocksMovement, level);

        sprite = new ImageView(
                new Image(Objects.requireNonNull(Player.class.getResource(
                        "/game/resources/flyingassassin.png")).toExternalForm())
        );
        sprite.setFitWidth(SPRITE_SIZE);
        sprite.setFitHeight(SPRITE_SIZE);
    }

    @Override
    public void move() {
        // Implemented in Level!
    }

    /* TODO: Check tile/level boundariesH
       (example/framework code)
       boolean canMove = Level.canMoveTo(nextX, nextY)
       if (!canMove) {
            turnAround();
            return;
       }
     */

    /* TODO: Detect collisions with players or NPCs

     */

}
