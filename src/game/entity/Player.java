package game.entity;

/**
 * Represents the player entity in the game.
 * The Player class extends the generic Entity class and provides
 * a concrete implementation for the player character. It sets the entity
 * name to "Player" and inherits all shared state such as position, direction,
 * and movement blocking.
 *
 * @author Shayon Dhar
 * @version 1.0.0
 */
public class Player extends Entity {

    private static final String ENTITY_NAME = "Player";

    /**
     * Constructs a new Player entity.
     *
     * @param entityID       unique ID of the player
     * @param y              y coordinate of the player
     * @param x              x coordinate of the player
     * @param direction      direction the player is facing
     * @param alive          the alive state of the player
     * @param blocksMovement whether the player blocks movement of other entities
     */
    public Player(int entityID, int y, int x, Direction direction, boolean alive, boolean blocksMovement) {
        super(entityID, ENTITY_NAME, y, x, direction, alive, blocksMovement);
    }

    @Override
    public void move() {
        //TODO: implementation
    }
}

