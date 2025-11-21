package game.level;

import game.entity.Direction;
import game.entity.Entity;
import game.entity.Player;
import game.item.Bomb;
import game.item.Item;
import game.item.Gate;
import game.item.BombState;
import game.item.Door;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents a single playable level in the game. A Level stores the complete
 * world state, including tiles, items, NPCs, the player, time limits, and all
 * rules governing movement and interactions as described in the functional
 * specification.
 * The Level class is responsible for:
 * - Loading and initializing level data from a file.
 * - Managing tile-based movement for the player and NPCs.
 * - Handling interactions with items such as loot, clocks, gates, levers, and bombs.
 * - Tracking remaining time and determining win or loss conditions.
 * - Updating the state of NPCs, bombs, and other time-dependent gameplay events.
 * A Level does not handle rendering or user input.
 *
 * @author Alex Samuel
 * @version 1.0.0
 */


public class Level {
    private static final int INITIAL_TIME = 0;
    private static final int MAX_TIME = 240;

    private Tile[][] levelGrid;
    private List<Entity> entities;
    private Player player;
    private int levelWidth; // TODO: Note from Anton, levelWidth cannot exceed 650
    private int levelHeight; // TODO: Note from Anton, levelHeight cannot exceed 500
    private int remainingTime;
    private boolean levelComplete;
    private boolean levelFailed;
    private List<Bomb> activeBombs;
    private List<Tile> exitTiles;
    private Item[][] itemsGrid;

    private static final double CANVAS_WIDTH = 650;
    private static final double CANVAS_HEIGHT = 650;

    /* TODO: Consider the following when designing the Tile[][] implementation (love Anton x)

    // The width and height (in pixels) of each cell that makes up the game.
	private static final int GRID_CELL_WIDTH = 50;
	private static final int GRID_CELL_HEIGHT = 50;

	// The width of the grid in number of cells.
	private static final int GRID_WIDTH = 12;
     */

    /**
     * Constructor which loads the level from the level file.
     * @param LevelFile The file which stores the level data.
     */
    public Level(String LevelFile) {
        loadFromFile(LevelFile);
    }

    /**
     * Finds the next valid tile in the given direction based on movement rules.
     * Movement is determined by checking the next tile which has a common
     * colour with the current tile.
     * @param currentTile     the tile the entity is currently standing on.
     * @param direction  the direction in which movement is attempted.
     * @return the next valid tile in that direction, or null if no valid tile exists.
     */
    public Tile findNextValidTile(Tile currentTile, Direction direction) {
        int currentXCoordinate = currentTile.getX();
        int currentYCoordinate = currentTile.getY();

        int nextYCoordinate = 0;
        int nextXCoordinate = 0;

        if (direction == Direction.NORTH) {
            nextYCoordinate = currentYCoordinate - 1;
        } else if (direction == Direction.SOUTH) {
            nextYCoordinate = currentYCoordinate + 1;
        } else if (direction == Direction.EAST) {
            nextXCoordinate = currentXCoordinate + 1;
        } else if (direction == Direction.WEST) {
            nextXCoordinate = currentXCoordinate - 1;
        } else {
            return null;
        }
        while (nextXCoordinate >= 0 && nextYCoordinate >= 0
                && nextXCoordinate < levelWidth
                && nextYCoordinate < levelHeight) {
            Tile next = levelGrid[nextYCoordinate][nextXCoordinate];

            if (next != null && sharesColour(currentTile, next)) {
                return next;
            }
            nextXCoordinate += currentXCoordinate;
            nextYCoordinate += currentYCoordinate;

        }


        }

    /**
     * Auxiliary method to check whether the
     * 2 tiles we are checking share a common colour.
     * @param currentTile the current tile
     * @param nextTile the tile we are moving to
     * @return whether they share a colour or not
     */
    private boolean sharesColour(Tile currentTile,
                                 Tile nextTile) {
        for (Colour c : currentTile.getColours()) {
            return nextTile.getColours().contains(c);
        }
        return false;
    }

    /**
     * Returns tile at the current grid coordinates.
     * @param y y-coordinate.
     * @param x x-coordinate.
     * @return the tile.
     */
    public Tile getTile(int y, int x){
        return levelGrid[y][x];
    }

    /**
     * Gets the item at a specific coordinate
     * on the level.
     * @param y the y-coordinate
     * @param x the x-coordinate
     * @return gets the item on at that grid
     */
    public Item getItemAt(int y, int x){
        return itemsGrid[y][x];
    }

    /**
     * Sets the item at a specific coordinate
     * @param y the y-coordinate of the tile
     * @param x the x-coordinate of the tile
     * @param item the item being added 
     */
    private void setItemAt(int y, int x,Item item){
        itemsGrid[y][x] = item;
    }

    /**
     * Loads all the level data from the level file.
     * @param filename name of the load file.
     */
    public void loadFromFile(String filename) {
    }

    /**
     * Returns the four orthogonally adjacent neighbour tiles of the given tile.
     * Tiles that are outside the level boundaries are not included in return list.
     * @param tile the tile whose neighbours are requested.
     * @return a list of neighbouring tiles.
     */
    public List<Tile> getNeighbourTiles(Tile tile) {
        List<Tile> list = new ArrayList<>();

        Tile up    = getTile(tile.getX(), tile.getY() - 1);
        Tile down  = getTile(tile.getX(), tile.getY() + 1);
        Tile left  = getTile(tile.getX() - 1, tile.getY());
        Tile right = getTile(tile.getX() + 1, tile.getY());

        if (up != null) list.add(up);
        if (down != null) list.add(down);
        if (left != null) list.add(left);
        if (right != null) list.add(right);

        return list;
    }

    /**
     * Opens (removes) all gates of the specified colour from the level.
     * This is typically called after the player or a thief activates a lever
     * that corresponds to the same colour.
     * @param c the colour of gates to open
     */
    public void openGatesOfColour(Colour c){
        //TODO: Open gate logic
    }
    /**
     * Checks whether all loot and levers present in the level have been collected.
     * Used to determine whether the player or thieves may activate the exit tile.
     * @return true if no loot or levers remain in the level, false otherwise
     */
    public boolean allLootAndLeversCollected(){
        //TODO: Loot and Levers collected check logic
        return false;
    }

    /**
     * Checks whether a tile has an entity.
     * @param t the tile object
     * @return does the tile contain an entity
     */
    private boolean tileHasEntity(Tile t) {
        for (Entity e : entities) {
            if (e.getX() == t.getX() && e.getY() == t.getY()) return true;
        }
        return false;
    }
    /**
     * Triggers the specified bomb.
     * Uses the getNeighbourTiles to check whether the bomb
     * should be triggered.
     * @param bomb the bomb to trigger
     */
    public void triggerBomb(Bomb bomb){
        Tile bombTile = getTile(bomb.getX(), bomb.getY());
        List<Tile> neighbours = getNeighbourTiles(bombTile);

        boolean shouldTrigger = false;

        for (Tile t : neighbours) {

            Item item = itemsGrid[t.getY()][t.getX()];
            if (item != null) {
                shouldTrigger = true;
            }

            if (tileHasEntity(t)) {
                shouldTrigger = true;
            }
        }

        if (shouldTrigger) {
            bomb.trigger();
        }
    }

    /**
     * Determines the next tile that an NPC should move to based on that NPC's
     * movement rules.
     * @param npc the NPC requesting its next tile
     * @return the tile the NPC should move to, or null if no valid move exists
     */
    public Tile getNextTileForNpc(Entity npc){
        //TODO: NPC Logic
        return null;
    }
    /**
     * Finds the shortest path between loot, lever and exit tile.
     * Typically used for smart thieves.
     * @param source the starting tile
     * @return the target tile that lies on the shortest valid path, or null if no reachable target exists
     */
    public Tile findShortestPathTarget(Tile source){
        //TODO: pathfinding for smart thief
        return null;
    }

    // TODO: Note from Anton, would this be here or updated via draw() method in the Item class?
    public void update(int time){
    }
    /**
     * Removes an item from the grid.
     * @param x the x-coordinate of the tile
     * @param y the y-coordinate of the tile
     */
    private void removeItemFromGrid(int x, int y) {
        itemsGrid[x][y] = null;
    }

    /**
     * Destroys the item located at the specified tile coordinates
     * as part of a bomb explosion.
     * @param x the x-coordinate of the tile
     * @param y the y-coordinate of the tile
     */
    public void destroyTileContent(int x, int y) {

        Item item = itemsGrid[x][y];
        if (item == null) return;

        // Bombs
        if (item instanceof Bomb bomb) {
            if (bomb.getState() == BombState.WAITING ||
                    bomb.getState() == BombState.COUNTING) {
                bomb.trigger();
            }
            return;
        }

        // Gates and Doors survive
        if (item instanceof Gate || item instanceof Door) {
            return;
        }

        // Everything else gets destroyed
        removeItemFromGrid(x, y);
    }

    /**
     * Handles the explosion of a bomb
     * @param x x-coordinate of the tile
     * @param y y-coordinate of the tile.
     */

    public void handleExplosion(int x, int y) {
        // horizontal blast
        for (int cx = 0; cx < levelWidth; cx++) {
            destroyTileContent(cx, y);
        }

        // vertical blast
        for (int cy = 0; cy < levelHeight; cy++) {
            destroyTileContent(x, cy);
        }
    }
    /**
     * Updates the state of the level by the specified time step.
     * This includes decreasing remaining time, updating NPC movement,
     * processing bomb countdowns and explosions, and checking win or loss conditions.
     * @param time the time step (in seconds or ticks) to advance the level state by
     */
    public void updateLevel(int time) {
        /* TODO:
        1. Reduce Remaining Time
        2. update NPCs
        3. tick bombs
        4. check win/loss
         */
    }

    /**
     * Renders the level onto the JavaFX application.
     *
     * @author Antoni Wachowiak
     * @param gc The graphics context used within the JavaFX application
     */
    public void draw(GraphicsContext gc) {
        // TODO: Temp code setting the levelWidth and levelHeight
        levelWidth = 300;
        levelHeight = 400;

        // Calculating where the level background should appear within the canvas
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();
        double x = (canvasWidth - levelWidth) / 2;
        double y = (canvasHeight - levelHeight) / 2;

        // Drawing the level background
        gc.setFill(Color.GRAY);
        gc.fillRect(x, y, levelWidth, levelHeight);
    }
}
