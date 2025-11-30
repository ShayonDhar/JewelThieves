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

    public Direction left() {
        return switch (this) {
            case NORTH -> WEST;
            case EAST -> NORTH;
            case SOUTH -> EAST;
            case WEST -> SOUTH;
        };
    }

    public Direction right() {
        return switch (this) {
            case NORTH -> EAST;
            case EAST -> SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
        };
    }

    public Direction opposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case EAST -> WEST;
            case SOUTH -> NORTH;
            case WEST -> EAST;
        };
    }
}
