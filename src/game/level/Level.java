package game.level;

import game.GameController;
import game.TilePosition;
import game.entity.Direction;
import game.entity.Entity;
import game.entity.Player;
import game.entity.npc.FlyingAssassin;
import game.entity.npc.NPC;
import game.item.*;
import java.util.*;
import javafx.scene.paint.Color;

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
    private int levelNumber; // Added for high score tracking
    private int remainingTime;
    private boolean levelComplete;
    private boolean levelFailed;
    private List<Bomb> activeBombs;
    private List<Tile> exitTiles;
    private List<Item> items = new ArrayList<>();
    private List<Gate> gates = new ArrayList<>();
    private Item[][] itemsGrid;
    private GameController controller;
    private NPCManager npcManager;

    /**
     * Constructor which loads the level from the level loader.
     */
    public Level(GameController controller) {
        this.controller = controller;
        this.npcManager = new NPCManager(this);
    }

    /**
     * Finds the next valid tile in the given direction based on movement rules.
     * Movement is determined by checking the next tile which has a common
     * colour with the current tile.
     *
     * @param currentTile the tile the entity is currently standing on.
     * @param direction   the direction in which movement is attempted.
     * @return the next valid tile in that direction, or null if no valid tile exists.
     */
    public Tile findNextValidTile(Tile currentTile, Direction direction) {
        int dx = getOffsetX(direction);
        int dy = getOffsetY(direction);

        if (dx == 0 && dy == 0) return null; //  invalid direction

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
            case EAST -> 1;
            case WEST -> -1;
            default -> 0;
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
            default -> 0;
        };
    }

    /**
     * Checks whether the given (x, y) coordinates are within the bounds of the level grid.
     *
     * @param x the x-coordinate to check
     * @param y the y-coordinate to check
     * @return true if the coordinates are inside the grid, otherwise false
     */
    public boolean isInBounds(int x, int y) {
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
     *
     * @param currentTile the current tile
     * @param nextTile    the tile we are moving to
     * @return whether they share a colour or not
     */
    private boolean sharesColour(Tile currentTile, Tile nextTile) {
        return Arrays.stream(currentTile.getColours())
                .anyMatch(colour -> nextTile.getColoursAsList().contains(colour));
    }

    /**
     * Auxiliary method which checks whether a
     * tile contains the specific colour that Floor Following Thief's is following.
     *
     * @param tile            the tile we inspect.
     * @param followingColour the specific colour the floor following thief follows.
     * @return whether the colour matches or not
     */
    public boolean tileSharesFollowingColour(Tile tile, Colour followingColour) {

        //  If either tile or required colour is missing, can't be valid
        if (tile == null || followingColour == null) {
            return true;
        }

        //  Tiles store their colours as JavaFX Color objects, so we have to convert the Enum into
        //  that too before comparing
        Color[] followingColours = tile.getColours();
        if (followingColours == null) {
            return true;
        }
        //  Convert Colour Enum to JavaFX Color equivalent
        Color target = followingColour.getFXColor();

        //  Check every colour the tile contains
        //  If any of them match the thief's follow colour, then the tile is valid
        for (Color c : followingColours) {
            if (c.equals(target)) { //  The javaFX colour that matches the thief's Enum colour
                return false;
            }
        }
        //  No match found, so tile doesn't contain colour
        return true;
    }

    /**
     * Returns tile at the current grid coordinates.
     *
     * @param y y-coordinate.
     * @param x x-coordinate.
     * @return the tile.
     */
    public Tile getTile(int y, int x) {
        if (y < 0 || y >= levelHeight || x < 0 || x >= levelWidth) {
            return null;
        }
        return levelGrid[y][x];
    }

    /**
     * Gets the item at a specific coordinate
     * on the level.
     *
     * @param y the y-coordinate
     * @param x the x-coordinate
     * @return gets the item on at that grid
     */
    public Item getItemAt(int y, int x) {
        return itemsGrid[y][x];
    }

    /**
     * Sets the item at a specific coordinate.
     *
     * @param y    the y-coordinate of the tile
     * @param x    the x-coordinate of the tile
     * @param item the item being added
     */
    public void setItemAt(int y, int x, Item item) {
        itemsGrid[y][x] = item;
    }

    /**
     * Returns the four orthogonally adjacent neighbour tiles of the given tile.
     * Tiles that are outside the level boundaries are not included in return list.
     *
     * @param tile the tile whose neighbours are requested.
     * @return a list of neighbouring tiles.
     */
    public List<Tile> getNeighbourTiles(Tile tile) {
        List<Tile> list = new ArrayList<>();

        int y = tile.getY();
        int x = tile.getX();

        Tile up = getTile(y + 1, x);
        if (up != null) {
            list.add(up);
        }

        Tile down = getTile(y - 1, x);

        if (down != null) {
            list.add(down);
        }

        Tile left = getTile(y, x - 1);
        if (left != null) {
            list.add(left);
        }

        Tile right = getTile(y, x + 1);
        if (right != null) {
            list.add(right);
        }

        return list;
    }

    /**
     * Opens (removes) all gates of the specified colour from the level.
     * This is typically called after the player or a thief activates a lever
     * that corresponds to the same colour.
     *
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
     *
     * @return true if no loot or levers remain in the level, false otherwise
     */
    public boolean allLootAndLeversCollected() {
        for (int y = 0; y < levelHeight; y++) {
            for (int x = 0; x < levelWidth; x++) {
                Item item = itemsGrid[y][x];
                if (item instanceof Loot || item instanceof Lever) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks whether a tile has an entity.
     *
     * @param t the tile object
     * @return does the tile contain an entity
     */
    private boolean tileHasEntity(Tile t) {
        for (Entity e : entities) {
            if (e.getX() == t.getX() && e.getY() == t.getY()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines whether a given tile is blocked for the Entity trying to move onto it.
     * A tile is blocked if it contains another blocking entity or a blocking item.
     *
     * @param mover  the entity that's attempting to move to a new tile.
     * @param target the tile the mover wants to step onto.
     * @return true if movement onto the tile is blocked, false otherwise.
     */
    public boolean blocksMovement(Entity mover, Tile target) {
        if (target == null) {
            return true;
        }

        if (isBlockedByEntity(mover, target)) {
            return true;
        }

        return isBlockedByItem(target);
    }

    /**
     * Checks if the target tile is blocked by another entity.
     *
     * @param mover  the entity attempting to move
     * @param target the tile being checked
     * @return true if blocked by an entity, false otherwise
     */
    private boolean isBlockedByEntity(Entity mover, Tile target) {
        if (!tileHasEntity(target)) {
            return false;
        }

        int x = target.getX();
        int y = target.getY();

        for (Entity e : entities) {
            if (shouldEntityBlock(e, mover, x, y)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if a specific entity should block movement.
     *
     * @param entity the entity to check
     * @param mover  the entity trying to move
     * @param x      target X coordinate
     * @param y      target Y coordinate
     * @return true if this entity blocks movement, false otherwise
     */
    private boolean shouldEntityBlock(Entity entity, Entity mover, int x, int y) {
        if (entity == mover) {
            return false; // Entity doesn't block itself
        }

        if (!entity.isAlive()) {
            return false; // Dead entities don't block
        }

        if (!entity.isBlocksMovement()) {
            return false; // Non-blocking entities (e.g., Flying Assassin)
        }

        return entity.getX() == x && entity.getY() == y;
    }

    /**
     * Checks if the target tile is blocked by an item.
     *
     * @param target the tile being checked
     * @return true if blocked by an item, false otherwise
     */
    private boolean isBlockedByItem(Tile target) {
        int x = target.getX();
        int y = target.getY();
        Item item = itemsGrid[y][x];

        if (item instanceof Gate) {
            return true;
        }

        if (item instanceof Bomb bomb) {
            return bomb.getState() != BombState.EXPLODED;
        }

        return false;
    }

    /**
     * Update time will add or subtract the time provided by the clock
     * based on whether the player or thieves collected it.
     *
     * @param time remaining time for game
     */
    public void update(int time) {
        remainingTime += time;
    }

    /**
     * Removes an item from the grid.
     *
     * @param y the y-coordinate of the tile
     * @param x the x-coordinate of the tile
     */
    public void removeItemFromGrid(int y, int x) {
        itemsGrid[y][x] = null;

        //  Also remove from the Tile object itself
        Tile tile = getTile(y, x);
        if (tile != null) {
            tile.removeItem();
        }
    }

    /**
     * Destroys the item located at the specified tile coordinates
     * as part of a bomb explosion.
     *
     * @param x the x-coordinate of the tile
     * @param y the y-coordinate of the tile
     */
    public void destroyTileContent(int x, int y) {
        Item item = itemsGrid[y][x];
        if (item == null) {
            return;
        }

        //  Bombs
        if (item instanceof Bomb bomb) {
            if (bomb.getState() == BombState.WAITING
                    || bomb.getState() == BombState.COUNTING) {
                bomb.trigger();
            }
            return;
        }

        //  Gates and Doors survive
        if (item instanceof Gate || item instanceof Door) {
            return;
        }

        //  Everything else gets destroyed
        removeItemFromGrid(y, x);
    }

    /**
     * Handles the explosion of a bomb.
     *
     * @param x x-coordinate of the tile
     * @param y y-coordinate of the tile.
     */

    public void handleExplosion(int x, int y) {
        controller.showExplosionAtTiles(getExplosionTiles(x, y));

        //  horizontal blast
        for (int cx = 0; cx < levelWidth; cx++) {
            destroyTileContent(cx, y);
        }
        //  vertical blast
        for (int cy = 0; cy < levelHeight; cy++) {
            destroyTileContent(x, cy);
        }
        // Murder the player
        for (TilePosition position : getExplosionTiles(x, y)) {
            int targetX = position.x();
            int targetY = position.y();

            // Kill player
            // If the player is standing in the blast zone:
            if (player != null && player.isAlive() && player.getX() == targetX && player.getY() == targetY) {
                player.die(false);
                GameController.gameOver();
            }
        }
    }

    /**
     * Checks that when the PLAYER moves, if it's occupying the same tile as a flying assassin.
     *
     * @return whether the player died or not.
     */
    public boolean checkPlayerShouldDieOnAssassin() {
        int playerX = player.getX();
        int playerY = player.getY();

        for (Entity flyingAssassin : entities) {
            if (flyingAssassin instanceof FlyingAssassin flyingAssassinButBetter) {
                if (flyingAssassinButBetter.getX() == playerX && flyingAssassinButBetter.getY() == playerY) {
                    player.die(false);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method to return the tiles to-be exploded by a bomb.
     *
     * @param x x-coordinate of the tile
     * @param y y-coordinate of the tile
     * @return a list of tiles to be exploded
     */
    public List<TilePosition> getExplosionTiles(int x, int y) {
        List<TilePosition> tiles = new ArrayList<>();

        // horizontal
        for (int cx = 0; cx < levelWidth; cx++) {
            tiles.add(new TilePosition(cx, y));
        }

        // vertical
        for (int cy = 0; cy < levelHeight; cy++) {
            tiles.add(new TilePosition(x, cy));
        }

        return tiles;
    }

    /**
     * Helper that notifies the game controller to display an explosion at a specific location.
     *
     * @param x the x coordinate of the location
     * @param y the y coordinate of the location
     */

    public void notifyExplosion(int x, int y) {
        if (controller == null) {
            return;
        }
        controller.showExplosionAtTiles(getExplosionTiles(x, y));
    }

    /**
     * Method that will unlock all gates of a given colour and will remove them from the
     * arraylist of gates and from the map.
     *
     * @param colour colour of the lever
     */
    public void unlockGates(Colour colour) {
        for (int y = 0; y < levelHeight; y++) {
            for (int x = 0; x < levelWidth; x++) {
                if (itemsGrid[y][x] instanceof Gate gate && gate.getColour().equals(colour)) {
                    gate.isOn = false;
                    removeItemFromGrid(gate.getY(), gate.getX());
                }

            }
        }
    }

    /**
     * Checks if any thief (non-Flying Assassin NPC) is standing on a door
     * after all loot and levers are collected.
     *
     * @return true if a thief has reached the door and won
     */
    public boolean hasThiefWon() {
        if (!allLootAndLeversCollected()) {
            return false;
        }

        for (Entity entity : entities) {
            if (entity instanceof NPC && entity.isAlive()) {
                int x = entity.getX();
                int y = entity.getY();
                Item item = itemsGrid[y][x];

                if (item instanceof Door) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Updates the state of the level by the specified time step.
     * This includes decreasing remaining time, updating NPC movement,
     * processing bomb countdowns and explosions, and checking win or loss conditions.
     *
     * @param time the time step (in seconds or ticks) to advance the level state by
     */
    public void updateLevel(int time) {
        if (entities == null) {
            return;
        }

        npcManager.updateAllNPCs();

        if (hasThiefWon()) {
            setLevelFailed(true);
            GameController.gameOver();
        }
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

    public int getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
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

    public void setEntities(List<Entity> list) {
        this.entities = (ArrayList<Entity>) list;
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

    public void setLevelFailed(boolean failed) {
        this.levelFailed = failed;
    }

    public boolean isLevelFailed() {
        return levelFailed;
    }

    public void setLevelComplete(boolean complete) {
        this.levelComplete = complete;
    }

    public boolean isLevelComplete() {
        return levelComplete;
    }
}