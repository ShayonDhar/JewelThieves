package game.level;

import game.GameController;
import game.entity.Direction;
import game.entity.Entity;
import game.entity.Player;
import game.entity.npc.FloorFollowingThief;
import game.entity.npc.FlyingAssassin;
import game.entity.npc.SmartThief;
import game.item.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.*;
import java.io.File;


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
    private ArrayList<Entity> entities;
    private Player player;
    private int levelWidth;
    private int levelHeight;
    private int remainingTime;
    private boolean levelComplete;
    private boolean levelFailed;
    private List<Bomb> activeBombs;
    private List<Tile> exitTiles;
    private Item[][] itemsGrid;
    private GameController controller;

    /**
     * Constructor which loads the level from the level loader.
     */
    public Level(GameController controller) {
        this.controller = controller;
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
        int dx = getOffsetX(direction);
        int dy = getOffsetY(direction);

        if (dx == 0 && dy == 0) return null; // invalid direction

        int nextX = currentTile.getX() + dx;
        int nextY = currentTile.getY() + dy;

        while (isInBounds(nextX, nextY)) {
            Tile next = levelGrid[nextY][nextX];

            if (isValidNextTile(currentTile, next)) {
                return next;
            }

            nextX += dx;
            nextY += dy;
        }

        return null;
    }

    /**
     * Computes the horizontal movement offset for a given direction.
     *
     * @param direction the direction to translate
     * @return +1 for EAST, -1 for WEST, or 0 for NORTH/SOUTH/other
     */
    private int getOffsetX(Direction direction) {
        return switch (direction) {
            case EAST  -> 1;
            case WEST  -> -1;
            default    -> 0;
        };
    }

    /**
     * Computes the vertical movement offset for a given direction.
     *
     * @param direction the direction to translate
     * @return +1 for SOUTH, -1 for NORTH, or 0 for EAST/WEST/other
     */
    private int getOffsetY(Direction direction) {
        return switch (direction) {
            case SOUTH -> 1;
            case NORTH -> -1;
            default    -> 0;
        };
    }

    /**
     * Checks whether the given (x, y) coordinates are within the bounds of the level grid.
     *
     * @param x the x-coordinate to check
     * @param y the y-coordinate to check
     * @return true if the coordinates are inside the grid, otherwise false
     */
    private boolean isInBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < levelWidth && y < levelHeight;
    }

    /**
     * Determines whether the specified next tile is a valid match for movement.
     * A tile is valid if it exists (is not null) and shares the same colour
     * properties as the current tile.
     *
     * @param current the tile from which movement started
     * @param next    the tile being evaluated
     * @return true if the next tile is not null and matches the colour
     */
    private boolean isValidNextTile(Tile current, Tile next) {
        return next != null && sharesColour(current, next);
    }


    /**
     * Auxiliary method to check whether the
     * 2 tiles we are checking share a common colour.
     * @param currentTile the current tile
     * @param nextTile the tile we are moving to
     * @return whether they share a colour or not
     */
    private boolean sharesColour(Tile currentTile, Tile nextTile) {
        return Arrays.stream(currentTile.getColours())
                .anyMatch(colour -> nextTile.getColoursAsList().contains(colour));
    }

    /**
     * Returns tile at the current grid coordinates.
     * @param y y-coordinate.
     * @param x x-coordinate.
     * @return the tile.
     */
    public Tile getTile(int y, int x){
        if (y < 0 || y >= levelHeight || x < 0 || x >= levelWidth) {
            return null;
        }
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
    public void setItemAt(int y, int x,Item item){
        itemsGrid[y][x] = item;
    }


    /**
     * Returns the four orthogonally adjacent neighbour tiles of the given tile.
     * Tiles that are outside the level boundaries are not included in return list.
     * @param tile the tile whose neighbours are requested.
     * @return a list of neighbouring tiles.
     */
    public List<Tile> getNeighbourTiles(Tile tile) {
        List<Tile> list = new ArrayList<>();

        int y = tile.getY();
        int x = tile.getX();

        Tile up    = getTile(y + 1, x);
        Tile down  = getTile(y - 1, x);
        Tile left  = getTile(y, x - 1);
        Tile right = getTile(y, x + 1);

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
    public void openGatesOfColour(Colour c) {
        for (int y = 0; y < levelHeight; y++) {
            for (int x = 0; x < levelWidth; x++) {
                Tile t = levelGrid[y][x];

                if (t != null && t.hasGate()) {
                    Gate gate = t.getGate();

                    if (gate.getColour() == c) {
                        t.removeItem();
                    }
                }
            }
        }
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
     * Determines the next tile that an NPC should move to based on that NPCs
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

    /**
     * Update time will add or subtract the time provided by the clock
     * based on whether the player or thieves collected it.
     * @param time
     */
    public void update(int time){
        remainingTime += time;
    }
    /**
     * Removes an item from the grid.
     * @param x the x-coordinate of the tile
     * @param y the y-coordinate of the tile
     */
    public void removeItemFromGrid(int x, int y) {
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

    public Tile[][] getLevelGrid() {
        return levelGrid;
    }

    public void setLevelGrid(Tile[][] grid) {
        this.levelGrid = grid;
    }

    public Item[][] getItemsGrid() {
        return itemsGrid;
    }

    public void setItemsGrid(Item[][] items) {
        this.itemsGrid = items;
    }
    public int getLevelWidth() {
        return levelWidth;
    }

    public void setLevelWidth(int width) {
        this.levelWidth = width;
    }

    public int getLevelHeight() {
        return levelHeight;
    }

    public void setLevelHeight(int height) {
        this.levelHeight = height;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int time) {
        this.remainingTime = time;
    }
    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(ArrayList<Entity> list) {
        this.entities = list;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    public List<Tile> getExitTiles() {
        return exitTiles;
    }

    public void setExitTiles(List<Tile> tiles) {
        this.exitTiles = tiles;
    }
    public List<Bomb> getActiveBombs() {
        return activeBombs;
    }

    public void setActiveBombs(List<Bomb> bombs) {
        this.activeBombs = bombs;
    }




}
