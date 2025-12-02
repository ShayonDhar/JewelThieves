package game.entity.npc;

import game.entity.Direction;
import game.entity.Entity;
import game.entity.EntityName;
import game.level.Level;
import javafx.scene.image.ImageView;

public abstract class NPC extends Entity {
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
     */
    protected NPC(EntityName entityName, int x, int y, Direction direction, boolean alive, boolean blocksMovement, Level level) {
        super(entityName, x, y, direction, alive, blocksMovement, level);
    }

    public ImageView getSprite() {

        // Get direction that player sprite is facing
        Direction facingDirection = getDirection();

        // Reset imageview orientation
        sprite.setRotate(0);

        // Rotate based on new direction
        switch (facingDirection) {
            case EAST:
                sprite.rotateProperty().set(90);
                break;
            case SOUTH:
                sprite.rotateProperty().set(180);
                break;
            case WEST:
                sprite.rotateProperty().set(270);
                break;
            default:
                break;
        }

        return sprite;
    }
}
