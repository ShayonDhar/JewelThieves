package game.entity;

/**
 * Represents the possible directions an entity can face or move in the game.
 *
 * @author Shayon Dhar
 * @version 1.0.0
 */
public enum Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    private Direction currentDirection;

    public void turnLeft() {
        switch (currentDirection) {
            case NORTH -> currentDirection = Direction.WEST;
            case EAST -> currentDirection = Direction.NORTH;
            case SOUTH -> currentDirection = Direction.EAST;
            case WEST -> currentDirection = Direction.SOUTH;
        }
    }

    public void turnRight() {
        switch (currentDirection) {
            case NORTH -> currentDirection = Direction.EAST;
            case EAST -> currentDirection = Direction.SOUTH;
            case SOUTH -> currentDirection = Direction.WEST;
            case WEST -> currentDirection = Direction.NORTH;
        }
    }

    public void turnAround() {
        switch (currentDirection) {
            case NORTH -> currentDirection = Direction.SOUTH;
            case EAST -> currentDirection = Direction.WEST;
            case SOUTH -> currentDirection = Direction.NORTH;
            case WEST -> currentDirection = Direction.EAST;
        }
    }
}
