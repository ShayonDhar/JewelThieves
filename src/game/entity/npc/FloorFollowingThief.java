package game.entity.npc;

import game.entity.Direction;
import game.entity.EntityName;
import game.level.Colour;
import javafx.scene.canvas.GraphicsContext;

/**
 * FloorFollowingThief NPC implementation.
 *
 * @author Keyan Jaf
 * @version 1.0.0
 */
public class FloorFollowingThief extends NPC {

    /**
     * The colour the floor following thief shall follow.
     */
    private final Colour followingColour;

    /**
     * Constructor for the Floor Following Thief at a position on the grid.
     *
     * @param x              x coordinate of the Floor Following Thief
     * @param y              y coordinate of the Floor Following Thief
     * @param direction      direction the Floor Following Thief is facing
     * @param alive          the alive state of the Floor Following Thief
     * @param blocksMovement whether the Floor Following Thief blocks movement of other entities
     */
    public FloorFollowingThief(int x, int y, Direction direction, boolean alive, boolean blocksMovement, Colour followingColour) {
        super(EntityName.FLOOR_FOLLOWING_THIEF, x, y, direction, alive, blocksMovement);
        this.followingColour = followingColour;
    }

    /**
     * Returns colour the thief is following.
     *
     * @return asigned floor colour
     */
    public Colour getFollowingColour() {
        return followingColour;
    }


    /**
     * Returns the order in which floor following thief will attempt to move,
     * with it's "left hand" priority.
     * @return an array of directions in a priority order.
     */
    public Direction[] getDirectionPriority() {
        Direction current = getDirection();

        Direction left = leftOf(current);
        Direction right = rightOf(current);
        Direction forward = current;
        Direction backwards = oppositeOf(current);

        return new Direction[]{left, forward, right, backwards}; //The priority
    }

    /**
     * Gives the direction to the left of the returned/current direction,
     * without actually changing it yet.
     * @param current the current direction.
     * @return returns the direction to the left.
     */

    private Direction leftOf(Direction current) {
        return switch (current) {
            case NORTH -> Direction.WEST;
            case WEST -> Direction.SOUTH;
            case SOUTH -> Direction.EAST;
            case EAST -> Direction.NORTH;
        };
    }

    /**
     * Gives the direction to the right of the returned/current direction,
     * without actually changing it yet.
     * @param current the current direction.
     * @return returns the direction to the right.
     */
    private Direction rightOf(Direction current) {
        return switch (current) {
            case NORTH -> Direction.EAST;
            case EAST -> Direction.SOUTH;
            case SOUTH -> Direction.WEST;
            case WEST -> Direction.NORTH;
        };
    }

    /**
     * Computes the direction opposite the current/given direction.
     * @param current the current direction/
     * @return returns the opposite direction.
     */
    private Direction oppositeOf(Direction current) {
        return switch (current) {
            case NORTH -> Direction.SOUTH;
            case SOUTH -> Direction.NORTH;
            case EAST -> Direction.WEST;
            case WEST -> Direction.EAST;
        };
    }

    @Override
    public void addToHighscore(int value) {
    }

    @Override
    public void move() {
        /*TODO: Implement the left hand wall following once Tile is fully available.
        FloorFollowingThief movement is based on the left hand rule and the
        colour of adjacent tiles ad described in the functional spec.

        Full implementation (at least I (Keyan) think) requires:
        - Access to the current Tile plus neighbouring tiles.
        - Ability to check which edges/tiles share this thiefs followingColour
        - Collision and blocking checks as with every other NPC/the player

        Intended movement method:
        1. Get the thief's current tile
        2. Get the preferred directions in order
           Direction[] prefs = getDirectionPriority();
        3. For each direction in prefs look at the neighbouring tile,
        see if the movement between that tile an the current follows
        the thief's followingColour
       - Check that the neighbour isn't blocked by a closed gate/bomb/entity etc.

       Finally, if a valid move, set the direction and move the thief to that tile via
       setPosition(newX, newY). Repeat on new tick!


         */
    }
}
