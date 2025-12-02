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
     * @param level The current level object the NPC is
     */

    protected NPC(EntityName entityName, int x, int y, Direction direction,
                  boolean alive, boolean blocksMovement, Level level) {
        super(entityName, x, y, direction, alive, blocksMovement, level);
    }

    public ImageView getSprite() {
        return sprite;
    }
}
