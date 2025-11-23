package game.entity.npc;

import game.entity.Direction;
import game.entity.EntityName;
import javafx.scene.canvas.GraphicsContext;

/**
 * Represents the Flying Assassin NPC and its unique abilities
 *
 * @author Shayon Dhar
 * @version 1.0.0
 */
public class FlyingAssassin extends NPC {

    /**
     * Constructor to create an Entity object.
     * @param x              x coordinate of the entity
     * @param y              y coordinate of the entity
     * @param direction      direction the entity is facing
     * @param alive          the alive state of the entity
     * @param blocksMovement whether the entity blocks movement of other entities
     */
    protected FlyingAssassin(int x, int y, Direction direction, boolean alive, boolean blocksMovement) {
        super(EntityName.FLYING_ASSASSIN, x, y, direction, alive, blocksMovement);
    }

    @Override
    public void move() {
        //TODO: Flying Assassin movement implementation requires tiles

        /*
        Once again depends on Level/Tile
        - Move in a straight line (horizontal/veritcal)
          IGNORES floor colour rules

        - When the next step would leave the level bounds, it simply uses turnAround()
         */

    }

    @Override
    public void draw(GraphicsContext gc) {

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
