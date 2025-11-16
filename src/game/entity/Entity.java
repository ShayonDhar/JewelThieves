package game.entity;

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
    private int entityID;
    private int x;
    private int y;
    private Direction direction;
    private boolean alive;

    /**
     * Constructor to create an Entity object.
     *
     * @param entityID unique ID of the entity
     * @param entityName name of the entity
     * @param y y coordinate of the entity
     * @param x x coordinate of the entity
     * @param direction direction the entity is facing
     * @param alive the alive state of the entity
     * @param blocksMovement whether the entity blocks movement of other entities
     */
    public Entity(int entityID, String entityName, int y, int x, Direction direction, boolean alive, boolean blocksMovement) {
        this.entityID = entityID;
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
     * Gets the unique ID of the entity.
     *
     * @return entity ID
     */
    public int getEntityID() {
        return entityID;
    }

    /**
     * Sets the unique ID of the entity.
     *
     * @param entityID new entity ID
     */
    public void setEntityID(int entityID) {
        this.entityID = entityID;
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
     * Turns the entity to the left (useful for floor following thief)
     *
     */
    public void turnLeft() {
        switch (direction) {
            case NORTH -> direction = Direction.NORTH;
            case EAST -> direction = Direction.NORTH;
            case SOUTH -> direction = Direction.EAST;
            case WEST -> direction = Direction.SOUTH;
        }
    }

    /**
     * Turns the entity to the right (useful for floor following thief)
     *
     */
    public void turnRight() {
        switch (direction) {
            case NORTH -> direction = Direction.EAST;
            case EAST -> direction = Direction.SOUTH;
            case SOUTH -> direction = Direction.WEST;
            case WEST -> direction = Direction.NORTH;
        }
    }

    /**
     * Turns the entity 180 degrees. Essentially flying assassin's movement pattern.
     */
    public void turnAround() {
        switch (direction) {
            case NORTH -> direction = Direction.SOUTH;
            case EAST -> direction = Direction.WEST;
            case SOUTH -> direction = Direction.NORTH;
            case WEST -> direction = Direction.EAST;
        }

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
     * Compares this entity to another object for equality.
     * Entities are considered equal if they share the same ID.
     *
     * @param o the object to compare
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity other)) return false;
        return this.entityID == other.entityID;
    }

    /**
     * Generates a hash code for the entity based on its ID.
     *
     * @return hash code of the entity
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(entityID);
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
                ", entityID=" + entityID +
                ", xCoordinate=" + x +
                ", yCoordinate=" + y +
                ", direction=" + direction +
                ", alive=" + alive +
                '}';
    }

}
