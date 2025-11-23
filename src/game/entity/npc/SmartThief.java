package game.entity.npc;

import game.entity.Direction;
import game.entity.EntityName;
import javafx.scene.canvas.GraphicsContext;


/*
SmartThief implementation:
Most intelligent NPC, potentially hard to code.
- Collect as much loot as possible then attempt to reach
an exit before the player does.
*/

public class SmartThief extends NPC {

    /**
     * Constructor to create the Smart Thief.
     *
     * @param x              x coordinate of the Smart Thief
     * @param y              y coordinate of the Smart Thief
     * @param direction      direction the Smart Thief is facing
     * @param alive          the alive state of the Smart Thief
     * @param blocksMovement whether the SmartThief blocks movement of other entities
     */
    protected SmartThief(int x, int y, Direction direction, boolean alive, boolean blocksMovement) {
        super(EntityName.SMART_THIEF, x, y, direction, alive, blocksMovement);
    }

    @Override
    public void draw(GraphicsContext gc) {

    }

    @Override
    public void move()
    {
        /*
        SmartThief movements relies heavily on Level/Tile state and pathfinding, so
        full implementation of those will be needed.

        Intended behaviour at the very least:

        1. If any loot or levers remain on the level:
            1a. Determine whether it is at least reachable, following the movement rules.
            1b. If reachable loot/lever exists, compute the shortest path from the thief's
            current tile, to said nearest loot/lever.
             - Move one step along that shortest path, choose the next
             tile on the path, setDirection toward it, and update the thief's pos
             (setPosition(newX, newY)).
                   OTHERWISE:
                - Choose a "random but valid" direction and move there.
                 Valid = within bounds, obeyes rules and not blocked by bombs, players, other NPCs.

         2. If no loot or levers remain,
             2a. Determine whether there is at least on reachable exit.
             2b. If reachable exit exists, compute the shortest path to it and move one step along
             that path (just like above.)
                Otherwise: Move in a random but valid direction!
         */
    }

}


