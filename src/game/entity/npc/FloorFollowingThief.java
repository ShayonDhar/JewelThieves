package game.entity.npc;

import game.entity.Direction;
import game.entity.EntityName;
import game.level.Colour;
import game.level.Level;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.canvas.GraphicsContext;

/**
 * FloorFollowingThief NPC implementation.
 *
 * @author Keyan Jaf
 * @version 1.0.0
 */
public class FloorFollowingThief extends NPC {

    private static final int SPRITE_SIZE = 35;
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
    public FloorFollowingThief(int x, int y, Direction direction, boolean alive, boolean blocksMovement, Level level, Colour followingColour) {
        super(EntityName.FLOOR_FOLLOWING_THIEF, x, y, direction, alive, blocksMovement, level);
        this.followingColour = followingColour;

        sprite = new ImageView(
                new Image(game.entity.Player.class.getResource("/game/resources/floorfollowingthief.png").toExternalForm())
        );
        sprite.setFitWidth(SPRITE_SIZE);
        sprite.setFitHeight(SPRITE_SIZE);
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

        Direction left = current.left();
        Direction forward = current;
        Direction right = current.right();
        Direction backwards = current.opposite();

        return new Direction[]{left, forward, right, backwards}; //The priority
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
