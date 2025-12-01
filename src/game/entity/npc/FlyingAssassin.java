package game.entity.npc;

import game.entity.Direction;
import game.entity.EntityName;
import game.level.Level;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.canvas.GraphicsContext;

/**
 * Represents the Flying Assassin NPC and its unique abilities
 *
 * @author Shayon Dhar
 * @version 1.0.0
 */
public class FlyingAssassin extends NPC {
    private static final int SPRITE_SIZE = 25;
    /**
     * Constructor to create an Entity object.
     * @param x              x coordinate of the entity
     * @param y              y coordinate of the entity
     * @param direction      direction the entity is facing
     * @param alive          the alive state of the entity
     * @param blocksMovement whether the entity blocks movement of other entities
     */
    public FlyingAssassin(int x, int y, Direction direction, boolean alive, boolean blocksMovement, Level level) {
        super(EntityName.FLYING_ASSASSIN, x, y, direction, alive, blocksMovement, level);

        sprite = new ImageView(
                new Image(game.entity.Player.class.getResource("/game/resources/flyingassassin.png").toExternalForm())
        );
        sprite.setFitWidth(SPRITE_SIZE);
        sprite.setFitHeight(SPRITE_SIZE);
    }



    @Override
    public void move() {
        //Implemented in Level!
    }


    @Override
    public void addToHighscore(int value) {
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
