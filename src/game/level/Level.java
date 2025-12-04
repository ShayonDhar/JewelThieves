package game.level;

import game.GameController;
import game.TilePosition;
import game.entity.Direction;
import game.entity.Entity;
import game.entity.Player;
import game.entity.npc.FloorFollowingThief;
import game.entity.npc.FlyingAssassin;
import game.entity.npc.SmartThief;
import game.item.*;
import java.util.*;
import java.util.List;
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
    private int remainingTime;
    private boolean levelComplete;
    private boolean levelFailed;
    private List<Bomb> activeBombs;
    private List<Tile> exitTiles;
    private List<Item> items = new ArrayList<>();
    private Item[][] itemsGrid;
    private GameController controller;
    private Random smartThiefRandomMove = new Random();

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
     *
     * @param currentTile the current tile
     * @param nextTile    the tile we are moving to
     * @return whether they share a colour or not
     */
    private boolean sharesColour(Tile currentTile, Tile nextTile) {
        return Arrays.stream(currentTile.getColours())
                .anyMatch(colour -> nextTile.getColoursAsList().contains(colour));
    }

    //  Need a method that checks whether a tile contains FloorFollowingThief's SPECIFIC following colour

    /**
     * Auxillary method which checks whether a
     * tile contains the specific colour that Floor Following Thief's is following.
     *
     * @param tile            the tile we inspect.
     * @param followingcolour the specific colour the floor following thief follows.
     * @return whether the colour matches or not
     */
    private boolean tileSharesFollowingColour(Tile tile, Colour followingcolour) {

        //  If either tile or required colour is missing, can't be valid
        if (tile == null || followingcolour == null) {
            return false;
        }

        //  Tiles store their colours as JavaFX Color objects, so we have to convert the Enum into
        //  that too before comparing
        Color[] followingColours = tile.getColours();
        if (followingColours == null) {
            return false;
        }
        //  Convert Colour Enum to JavaFX Color equivalent
        Color target = followingcolour.getFXColor();

        //  Check every colour the tile contains
        //  If any of them match the thief's follow colour, then the tile is valid
        for (Color c : followingColours) {
            if (c.equals(target)) { //  The javaFX colour that matches the thief's Enum colour
                return true;
            }
        }
        //  No match found, so tile doesn't contain colour
        return false;
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
                Tile tile = levelGrid[y][x];
                if (tile != null) {
                    Item item = tile.getItem();
                    if (item instanceof Loot || item instanceof Lever) {
                        return false;
                    }
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
     * A tile is blocked if it contains another blocking entity,
     *
     * @param mover  the entity that's attempting to move to a new tile.
     * @param target the tile the mover wants to step onto.
     * @return returns true if movement onto the tile is blocked, otherwise false.
     */
    private boolean blocksMovement(Entity mover, Tile target) {
        if (target == null) {
            return true; //  So they can't move to "nowhere"
        }

        int x = target.getX();
        int y = target.getY();

        if (tileHasEntity(target)) {
            for (Entity e : entities) {
                if (e == mover) {
                    continue; //  Doesn't block itself from moving
                }
                if (!e.isAlive()) {
                    continue; // Dead entities don't block (not sure if them dying removes from tile.)
                }
                if (!e.isBlocksMovement()) {
                    continue; // Basically just Flying Assassin
                }

                if (e.getX() == x && e.getY() == y) {
                    return true; // Confirmed to block
                }
            }
        }

        // Whether x item blocks movement
        // Gates
        Item item = itemsGrid[y][x];
        if (item instanceof Gate) {
            return true;
        }
        // Bombs, can't step onto unless already exploded
        if (item instanceof Bomb bomb) {
            return bomb.getState() != BombState.EXPLODED;
        }
        // Doors, Loot, Lever, Clock etc do not block movement
        return false;
    }

    /**
     * Determines the next tile that an NPC should move to based on that NPCs
     * movement rules.
     * @param npc the NPC requesting its next tile
     * @return the tile the NPC should move to, or null if no valid move exists
     */
    public Tile getNextTileForNpc(Entity npc){

        Tile current = getTile(npc.getY(), npc.getX());
        if (current == null) {
            return null;
        }

        // Flying Assassin
        if (npc instanceof FlyingAssassin flyingAssassin) {

            Direction flyingDirection = flyingAssassin.getDirection();
            int dx = getOffsetX(flyingDirection);
            int dy = getOffsetY(flyingDirection);

            int nextX = current.getX() + dx;
            int nextY = current.getY() + dy;

            // If the Flying Assassin is about to leave the bounds, turn around
            if (!isInBounds(nextX, nextY)) {
                flyingDirection = flyingDirection.opposite(); // Uses the updated Direction Enum helper, far less duplication now
                flyingAssassin.setDirection(flyingDirection);

                dx = getOffsetX(flyingDirection);
                dy = getOffsetY(flyingDirection);

                nextX = current.getX() + dx;
                nextY = current.getY() + dy;

                if (!isInBounds(nextX, nextY)) {
                    return null; // If it can't move, just don't
                }
            }

            Tile flyingTarget = getTile(nextY, nextX);
            if (flyingTarget == null) {
                return null;
            }

            // Ignores all colours, gates, items etc. Only respects level bounds as functional spec says!
            return flyingTarget;
        }



        // Floor following thief
        if (npc instanceof FloorFollowingThief floorThief) {
            // Get tile the thief is currently standing on
            Tile currentTile = getTile(floorThief.getY(), floorThief.getX());
            if (currentTile == null) {
                return null;
            }

            Colour followingColour = floorThief.getFollowingColour();

            Direction[] directionPriority = floorThief.getDirectionPriority();
            for (Direction floorDirection : directionPriority) {
                Tile candidateTile = findNextValidTile(currentTile, floorDirection);
                if (candidateTile == null) {
                    continue; // "Nothing valid in this direction so try the next one"
                }

                // Make sure both current and candidate tiles contain the thief's follow colour
                if (!tileSharesFollowingColour(currentTile, followingColour)
                    || !tileSharesFollowingColour(candidateTile, followingColour)) {
                    continue;
                }

                // Respect blocking rules
                if (blocksMovement(floorThief, candidateTile)) {
                    continue;
                }

                // Found valid tile following colour and left hand rule
                floorThief.setDirection(floorDirection);
                return candidateTile;
            }
            // No valid direction found this tick
            return null;
        }

        // Smart Thief
        if (npc instanceof SmartThief smartThief) {
            // Tile where SmartThief currently is
            Tile currentTile = getTile(smartThief.getY(), smartThief.getX());
            if (currentTile == null) {
                return null;
            }

            Tile nextTileMovingTo = findShortestPathTarget(currentTile);
            if (nextTileMovingTo != null && !blocksMovement(smartThief, nextTileMovingTo)) {
                // Determine direction towards said next step then update facing
                Direction direction = getDirectionBetween(currentTile, nextTileMovingTo);
                if (direction != null) {
                    smartThief.setDirection(direction);
                }
                return nextTileMovingTo;
            }

            // If no reachable target or the step is blocked now, pick a random yet valid tile
            Tile randomlyMovingTo = getRandomButValidMove(currentTile, smartThief);
            if (randomlyMovingTo != null) {
                Direction direction = getDirectionBetween(currentTile, randomlyMovingTo);
                if (direction != null) {
                    smartThief.setDirection(direction);
                }
                return randomlyMovingTo;
            }
        }

        return null;
    }

    /**
     * Returns a "random but valid" tile that Smart Thief could move to
     * as part of it's movement from the given tile, or null if no such move exists.
     */
    private Tile getRandomButValidMove(Tile smartCurrentTile, Entity mover) {
        List<Direction> smartDirections = new ArrayList<>(Arrays.asList(Direction.values()));
        Collections.shuffle(smartDirections, smartThiefRandomMove);

        for (Direction direction : smartDirections) {
            Tile candidateTile = findNextValidTile(smartCurrentTile, direction);
            if (candidateTile == null) {
                continue;
            }
            if (blocksMovement(mover, candidateTile)) {
                continue;
            }
            return candidateTile;
        }
        return null;
    }

    /**
     * Works out direction smart thief would travel from one tile to another.
     * Assumes both tiles are on the same row/column (as they should)
     */
    private Direction getDirectionBetween(Tile from, Tile to) {
        int directionX = to.getX() - from.getX();
        int directionY = to.getY() - from.getY();

        if (directionX > 0) {
            return Direction.EAST;
        }
        if (directionX < 0) {
            return Direction.WEST;
        }
        if (directionY > 0) {
            return Direction.SOUTH;
        }
        if (directionY < 0) {
            return Direction.NORTH;
        }

        return null;
    }

    /**
     * Finds the shortest path between loot, lever and exit tile.
     * Typically used for smart thieves.
     * @param source the starting tile/ where smart thief currently is
     * @return the target tile that lies on the shortest valid path, or null if no reachable target exists
     */
    public Tile findShortestPathTarget(Tile source) {
        if (source == null) {
            return null;
        }

        int startX = source.getX();
        int startY = source.getY();

        // Determine which tiles are targets,
        // Will go for loot/levers first, then go for exits.
        boolean[][] isTarget = new boolean[levelHeight][levelWidth];
        boolean hasLootOrLever = false;

        // Mark loot/lever tiles as targets
        for (int y = 0; y < levelHeight; y++) {
            for (int x = 0; x < levelWidth; x++) {
                Item item = itemsGrid[y][x];
                if (item instanceof Loot || item instanceof Lever) {
                    hasLootOrLever = true;
                    isTarget[y][x] = true;
                    }
                }
            }

        // If no loot or levers, use exit tiles instead
        if (!hasLootOrLever) {
            if (exitTiles == null || exitTiles.isEmpty()) {
                return null; // Nothing to pathfind itself to
            }
            for (Tile exit : exitTiles) {
                isTarget[exit.getY()][exit.getX()] = true;
            }
        }

        // Breadth first search setup
        // previousTileX[y][x] X of the tile which we came FROM when first reaching (x,y)
        // previousTileY, Y of the above.
        boolean[][] visited = new boolean[levelHeight][levelWidth];
        int[][] previousTileX = new int[levelHeight][levelWidth];
        int[][] previousTileY = new int[levelHeight][levelWidth];

        // Initialise previous array to having no parent
        for (int y = 0; y < levelHeight; y++) {
            for (int x = 0; x < levelWidth; x++) {
                previousTileX[y][x] = -1;
                previousTileY[y][x] = -1;
            }
        }

        ArrayDeque<int[]> queue = new ArrayDeque<>();
        visited[startY][startX] = true;
        queue.add(new int[]{startX, startY});

        int goalX = -1;
        int goalY = -1;
        boolean foundGoal = false;

        // Breadth first search
        while (!queue.isEmpty()) {
            int[] position = queue.removeFirst();
            int currentX = position[0];
            int currentY = position[1];

            // Make sure source tile isn't a goal, if it is a target and not the starting tile
            // then the nearest target has been found
            if (!(currentX == startX && currentY == startY) && isTarget[currentY][currentX]) {
                goalX = currentX;
                goalY = currentY;
                foundGoal = true;
                break; // Because first it hits any target = nearest target
            }

            Tile smartCurrentTile = levelGrid[currentY][currentX];
            // Explorer neighbours to current tile in all 4 directions
            for (Direction smartDirection : Direction.values()) {
                Tile neighbourTile = findNextValidTile(smartCurrentTile, smartDirection);
                if (neighbourTile == null) {
                    continue;
                }

                int nextX = neighbourTile.getX();
                int nextY = neighbourTile.getY();

                if (!isInBounds(nextX, nextY)) {
                    continue;
                }
                if (visited[nextY][nextX]) {
                    continue;
                }

                // Use blocksMovement to make sure BFS doesn't make paths through impassible tiles
                // Null is passed as athe mover because BFS checks for generic passability, so with
                // mover as null, any blocking entity/item will cause the tile to be marked an obstacle.
                if (blocksMovement(null, neighbourTile)) {
                    continue;
                }

                visited[nextY][nextX] = true;
                // Record how nextX nd nextY were gotten to from currentX and currentY.
                previousTileX[nextY][nextX] = currentX;
                previousTileY[nextY][nextX] = currentY;

                queue.addLast(new int[]{nextX, nextY});
            }
        }

        // If no reachable target has been found
        if (!foundGoal) {
            return null;
        }

        // Reconstruct the next step from source to goal
        int stepToNextX = goalX;
        int stepToNextY = goalY;

        // Walk backwards until previous tile of stepToNextX and stepToNextY are the source

        /*
        Example: For tiles(x,y)
                 Start: (1,1)
                 Next: (2,1)
                 Next: (3,1)
                 Goal: (4,1)
        Start from (4,1) and walk backwards alk backwards: step = (4,1) so previous = (3,1)
                        step - (3,1) so previous = (2,1)
                        step = (2, 1) so previous - (1,1)/the start tile!
         */

        while (!(previousTileX[stepToNextY][stepToNextX] == startX &&
                previousTileY[stepToNextY][stepToNextX] == startY)) {
            int previousToSourceX = previousTileX[stepToNextY][stepToNextX];
            int previousToSourceY = previousTileY[stepToNextY][stepToNextX];

            // If parent chain is somehow lost then return null/abort
            if (previousToSourceX == -1 && previousToSourceY == -1) {
                return null;
            }

            stepToNextX = previousToSourceX;
            stepToNextY = previousToSourceY;
        }
        // stepToNext X and stepToNextY are now the tile immediately after the source
        return getTile(stepToNextY, stepToNextX);
    }

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
            if (bomb.getState() == BombState.WAITING ||
                    bomb.getState() == BombState.COUNTING) {
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
     * Handles the explosion of a bomb
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
    }
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
     * helper that notifies the game controller to display an explosion at a specific location.
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
     * Updates the state of the level by the specified time step.
     * This includes decreasing remaining time, updating NPC movement,
     * processing bomb countdowns and explosions, and checking win or loss conditions.
     * @param time the time step (in seconds or ticks) to advance the level state by
     */
    public void updateLevel(int time) {
        /* TODO:
        1. Reduce Remaining Time
        2. update NPCs
        4. check win/loss
         */

        // Added handling and movement of NPCs - Keyan
        if (entities != null) {
            // This makes a seperate copy of the current entities before looping. I feel like
            // removing elements from a list WHILE it's iterating will cause issues somewhere
            // so this just makes sure the list of entities is stable during the loop
            // THEN apply all the changes to the actual entities list
            List<Entity> seperateCopy = new ArrayList<>(entities);
            // Adds now dead Entities to a list that will run after the loop is done, just for robustness
            List<Entity> toRemove = new ArrayList<>();

            for (Entity entity : seperateCopy) {
                if (!(entity instanceof game.entity.npc.NPC npc)) {
                    continue;
                }
                if (!npc.isAlive()) {
                    continue;
                }

                Tile currentTile = getTile(npc.getY(), npc.getX());
                if (currentTile == null) {
                    continue;
                }
                Tile targetTile = getNextTileForNpc(npc);
                if (targetTile == null) {
                    continue;
                }

                int targetX = targetTile.getX();
                int targetY = targetTile.getY();

                // Handle collisions depending on NPCs type (Flying Assassin merks other NPCs and players)
                if (npc instanceof FlyingAssassin flyingAssassin) {
                    // Assassin KILLS player on contact
                    if (player != null && player.isAlive() && player.getX() == targetX
                        && player.getY() == targetY) {
                        player.die(false);
                        flyingAssassin.setPosition(targetX, targetY);
                        GameController.gameOver();
                        return;
                    }

                    // Assassin kills/removes other NPCs that it runs into
                    for (Entity otherNPC : seperateCopy) {
                        if (otherNPC == flyingAssassin || otherNPC == player || !otherNPC.isAlive()) {
                            continue;
                        }
                        if (otherNPC.getX() == targetX && otherNPC.getY() == targetY) {
                            otherNPC.die(false);
                            toRemove.add(otherNPC);
                        }
                    }
                    flyingAssassin.setPosition(targetX, targetY);
                } else if (npc instanceof SmartThief smartThief) {
                    smartThief.setPosition(targetX, targetY);
                }  else if (npc instanceof FloorFollowingThief floorFollowingThief) {
                    floorFollowingThief.setPosition(targetX, targetY);
                }
            }
            entities.removeAll(toRemove);
        }
        if (activeBombs != null) {
            for (Bomb bomb : new ArrayList<>(activeBombs)) {

                bomb.updateBombState(this);
            }
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


    public List<Item> getAllItems() {
        return items;
    }

    public void removeItem(Item item) {
        items.remove(item);
    }
}
