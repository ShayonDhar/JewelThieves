package game.entity.npc;

import game.entity.Direction;
import game.entity.Entity;
import game.entity.EntityName;
import game.level.Level;
import javafx.scene.image.ImageView;

/**
 * Abstract NPC class which defines a base type
 * for all NPCs, no movement/implementation here.
 *
 * @author Keyan Jaf
 * @version 1.0
 */

@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public abstract class NPC extends Entity {

    private static final int ROTATE_TO_EAST = 90;
    private static final int ROTATE_TO_SOUTH = 180;
    private static final int ROTATE_TO_WEST = 270;
    protected ImageView sprite;

    /**
     * Constructor to create an Entity object.
     *
     * @param entityName     name of the entity
     * @param y              y coordinate of the entity
     * @param x              x coordinate of the entity
     * @param direction      direction the entity is facing
     * @param alive          the alive state of the entity
     * @param blocksMovement whether the entity blocks movement of other entities
     * @param level The current level object the NPC is
     */

    protected NPC(EntityName entityName, int x, int y, Direction direction,
                  boolean alive, boolean blocksMovement, Level level) {
        super(entityName, x, y, direction, alive, blocksMovement, level);
    }

    /**
     * Method to get the sprite of an NPC.
     * Rotates the sprite based on movement direction.
     *
     * @return NPC png file
     */
    public ImageView getSprite() {

        // Get direction that player sprite is facing
        Direction facingDirection = getDirection();

        // Reset imageview orientation
        sprite.setRotate(0);

        // Rotate based on new direction
        switch (facingDirection) {
            case EAST:
                sprite.rotateProperty().set(ROTATE_TO_EAST);
                break;
            case SOUTH:
                sprite.rotateProperty().set(ROTATE_TO_SOUTH);
                break;
            case WEST:
                sprite.rotateProperty().set(ROTATE_TO_WEST);
                break;
            default:
                break;
        }

        return sprite;
    }
}
