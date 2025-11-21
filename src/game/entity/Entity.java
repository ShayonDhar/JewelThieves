package game.entity;

import javafx.scene.canvas.GraphicsContext;

/**
 * This class represents a generic entity in the game.
 * It provides shared state such as position, direction, and movement blocking.
 * Subclasses must implement specific behavior such as movement logic.
 *
 * @author Shayon Dhar, Keyan Jaf
 * @version 1.0.0
 */
public abstract class Entity {

    private final boolean blocksMovement;
    private String entityName;
    private int x;
    private int y;
    private Direction direction;
    private boolean alive;

    /**
     * Constructor to create an Entity object.
     *
     * @param entityName name of the entity
     * @param y y coordinate of the entity
     * @param x x coordinate of the entity
     * @param direction direction the entity is facing
     * @param alive the alive state of the entity
     * @param blocksMovement whether the entity blocks movement of other entities
     */
    protected Entity(String entityName, int y, int x, Direction direction, boolean alive, boolean blocksMovement) {
        this.entityName = entityName;
        this.y = y;
        this.x = x;
        this.direction = direction;
        this.alive = alive;
        this.blocksMovement = blocksMovement;
    }

    /**
     * Abstract method to define how the entity moves.
     * Must be implemented by subclasses.
     */
    public abstract void move();

    /**
     * Gets the y coordinate of the entity.
     *
     * @return current y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y coordinate of the entity.
     *
     * @param y new y coordinate
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Gets the x coordinate of the entity.
     *
     * @return current x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the x coordinate of the entity.
     *
     * @param x new x coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the position of the entity. (Convenient to do in one call)
     * @param x new x coordinate
     * @param y new y coordinate
     */

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Checks if the entity blocks movement of other entities.
     *
     * @return true if the entity blocks movement, false otherwise
     */
    public boolean isBlocksMovement() {
        return blocksMovement;
    }

    /**
     * Gets the name of the entity.
     *
     * @return entity name
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * Sets the name of the entity.
     *
     * @param entityName new entity name
     */
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    /**
     * Gets the direction the entity is facing.
     *
     * @return current direction
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Sets the direction the entity is facing.
     *
     * @param direction new direction
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Checks if the entity is alive.
     *
     * @return true if alive, false otherwise
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Sets the alive state of the entity.
     *
     * @param aliveState new alive state
     */
    public void die(boolean aliveState) {
        this.alive = aliveState;
    }

    /**
     * Returns a string representation of the entity.
     *
     * @return string containing entity details
     */
    @Override
    public String toString() {
        return "Entity{" +
                "blocksMovement=" + blocksMovement +
                ", entityName='" + entityName + '\'' +
                ", xCoordinate=" + x +
                ", yCoordinate=" + y +
                ", direction=" + direction +
                ", alive=" + alive +
                '}';
    }

    /**
     * Renders the entity onto the JavaFX application.
     *
     * @author Antoni Wachowiak
     * @param gc The graphics context used within the JavaFX application
     */
    public abstract void draw(GraphicsContext gc);

}
