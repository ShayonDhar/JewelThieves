package game.entity.npc;

import game.entity.Direction;
import game.entity.Entity;

public abstract class NPC extends Entity {
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
    protected NPC(String entityName, int y, int x, Direction direction, boolean alive, boolean blocksMovement) {
        super(entityName, y, x, direction, alive, blocksMovement);
    }
}
