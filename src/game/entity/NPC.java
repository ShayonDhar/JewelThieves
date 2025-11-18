package game.entity;

public abstract class NPC extends Entity {
    /**
     * Constructor to create an Entity object.
     *
     * @param entityID       unique ID of the entity
     * @param entityName     name of the entity
     * @param y              y coordinate of the entity
     * @param x              x coordinate of the entity
     * @param direction      direction the entity is facing
     * @param alive          the alive state of the entity
     * @param blocksMovement whether the entity blocks movement of other entities
     */
    protected NPC(int entityID, String entityName, int y, int x, Direction direction, boolean alive, boolean blocksMovement) {
        super(entityID, entityName, y, x, direction, alive, blocksMovement);
    }
}
