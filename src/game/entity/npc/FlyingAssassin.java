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

    private static final String ENTITY_NAME = EntityName.FLYING_ASSASSIN.getDisplayName();

    /**
     * Constructor to create an Entity object.
     * @param y              y coordinate of the entity
     * @param x              x coordinate of the entity
     * @param direction      direction the entity is facing
     * @param alive          the alive state of the entity
     * @param blocksMovement whether the entity blocks movement of other entities
     */
    protected FlyingAssassin(int y, int x, Direction direction, boolean alive, boolean blocksMovement) {
        super(ENTITY_NAME, y, x, direction, alive, blocksMovement);
    }

    @Override
    public void move() {
        //TODO: Flying Assassin movement implementation requires tiles
    }

    @Override
    public void draw(GraphicsContext gc) {

    }

    @Override
    public void addToHighscore(int value) {

    }
}
