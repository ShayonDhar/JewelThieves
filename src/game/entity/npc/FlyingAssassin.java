package game.entity.npc;

import game.entity.Direction;

/**
 * Represents the Flying Assassin NPC and its unique abilities
 *
 * @author Shayon Dhar
 * @version 1.0.0
 */
public class FlyingAssassin extends NPC {

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
    protected FlyingAssassin(String entityName, int y, int x, Direction direction, boolean alive, boolean blocksMovement) {
        super(entityName, y, x, direction, alive, blocksMovement);
    }

    @Override
    public void move() {
        //TODO: Flying Assassin movement implementation requires tiles
    }
}
