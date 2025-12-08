package game.level;

import game.entity.Direction;
import game.entity.Entity;
import game.item.*;
import java.util.ArrayDeque;

/**
 * Handles pathfinding logic for SmartThief NPCs in the game level.
 * This class encapsulates the breadth-first search (BFS) algorithm to find
 * the shortest path to targets such as loot, levers, or exit tiles.
 * It uses data from the associated Level instance to perform calculations.
 *
 * @author Shayon Dhar
 * @version 1.0.0
 */
public class SmartThiefPathfinder {

    private final Level level;

    /**
     * Constructs a SmartThiefPathfinder instance associated with the given Level.
     *
     * @param level the Level instance providing grid and entity data
     */
    public SmartThiefPathfinder(Level level) {
        this.level = level;
    }

    /**
     * Finds the shortest path between loot, lever and exit tile.
     * Uses breadth-first search to find the nearest reachable target.
     *
     * @param source the starting tile where smart thief currently is
     * @return the target tile that lies on the shortest valid path, or null if no reachable target exists
     */
    public Tile findShortestPathTarget(Tile source) {
        if (source == null) {
            return null;
        }

        boolean[][] isTarget = markTargetTiles();

        // Arrays to track the path
        int[][] previousTileX = new int[level.getLevelHeight()][level.getLevelWidth()];
        int[][] previousTileY = new int[level.getLevelHeight()][level.getLevelWidth()];
        initializePreviousTileArrays(previousTileX, previousTileY);

        // Perform BFS and get goal coordinates
        int[] goalCoords = performBreadthFirstSearch(source, isTarget, previousTileX, previousTileY);

        return reconstructFirstStep(source, goalCoords[0], goalCoords[1], previousTileX, previousTileY);
    }

    /**
     * Marks all target tiles for pathfinding (loot, levers, or exits).
     *
     * @return a 2D boolean array marking target tiles, or null if no targets exist
     */
    private boolean[][] markTargetTiles() {
        boolean[][] isTarget = new boolean[level.getLevelHeight()][level.getLevelWidth()];
        boolean hasLootOrLever = false;

        // Mark loot/lever tiles as targets
        for (int y = 0; y < level.getLevelHeight(); y++) {
            for (int x = 0; x < level.getLevelWidth(); x++) {
                Item item = level.getItemsGrid()[y][x];
                if (item instanceof Loot || item instanceof Lever) {
                    hasLootOrLever = true;
                    isTarget[y][x] = true;
                }
            }
        }

        // If no loot or levers, use exit tiles instead
        if (!hasLootOrLever) {
            if (level.getExitTiles() == null || level.getExitTiles().isEmpty()) {
                return new boolean[0][0];
            }
            for (Tile exit : level.getExitTiles()) {
                isTarget[exit.getY()][exit.getX()] = true;
            }
        }

        return isTarget;
    }

    /**
     * Performs a breadth-first search to find the nearest target tile.
     *
     * @param source the starting tile
     * @param isTarget 2D array marking which tiles are targets
     * @param previousTileX array to track X coordinates of previous tiles in path
     * @param previousTileY array to track Y coordinates of previous tiles in path
     * @return int array [goalX, goalY] if goal found, null otherwise
     */
    private int[] performBreadthFirstSearch(Tile source, boolean[][] isTarget,
                                            int[][] previousTileX, int[][] previousTileY) {
        int startX = source.getX();
        int startY = source.getY();

        boolean[][] visited = new boolean[level.getLevelHeight()][level.getLevelWidth()];

        ArrayDeque<int[]> queue = new ArrayDeque<>();
        visited[startY][startX] = true;
        queue.add(new int[]{startX, startY});

        while (!queue.isEmpty()) {
            int[] position = queue.removeFirst();
            int currentX = position[0];
            int currentY = position[1];

            // Check if we've reached a target (but not the starting tile)
            if (!(currentX == startX && currentY == startY) && isTarget[currentY][currentX]) {
                return new int[]{currentX, currentY}; // Return goal coordinates
            }

            exploreNeighbours(currentX, currentY, visited, previousTileX, previousTileY, queue);
        }

        return new int[0]; // No goal found
    }

    /**
     * Initializes the previous tile tracking arrays with -1 (no parent).
     *
     * @param previousTileX X-coordinate tracking array
     * @param previousTileY Y-coordinate tracking array
     */
    private void initializePreviousTileArrays(int[][] previousTileX, int[][] previousTileY) {
        for (int y = 0; y < level.getLevelHeight(); y++) {
            for (int x = 0; x < level.getLevelWidth(); x++) {
                previousTileX[y][x] = -1;
                previousTileY[y][x] = -1;
            }
        }
    }

    /**
     * Explores all valid neighbouring tiles during BFS.
     *
     * @param currentX current X coordinate
     * @param currentY current Y coordinate
     * @param visited tracking array for visited tiles
     * @param previousTileX tracking array for previous X coordinates
     * @param previousTileY tracking array for previous Y coordinates
     * @param queue BFS queue to add new tiles to
     */
    private void exploreNeighbours(int currentX, int currentY, boolean[][] visited,
                                   int[][] previousTileX, int[][] previousTileY,
                                   ArrayDeque<int[]> queue) {
        Tile currentTile = level.getLevelGrid()[currentY][currentX];

        for (Direction direction : Direction.values()) {
            Tile neighbourTile = level.findNextValidTile(currentTile, direction);
            if (neighbourTile != null) {
                int nextX = neighbourTile.getX();
                int nextY = neighbourTile.getY();

                if (nextX >= 0 && nextX < level.getLevelWidth() && nextY >= 0
                        && nextY < level.getLevelHeight()
                        && !visited[nextY][nextX]
                        && !isTileBlocked(nextX, nextY)) {

                    visited[nextY][nextX] = true;
                    previousTileX[nextY][nextX] = currentX;
                    previousTileY[nextY][nextX] = currentY;
                    queue.addLast(new int[]{nextX, nextY});
                }
            }
        }
    }

    /**
     * Checks if a tile is blocked by entities or items.
     *
     * @param x X coordinate of tile
     * @param y Y coordinate of tile
     * @return true if the tile is blocked, false otherwise
     */
    private boolean isTileBlocked(int x, int y) {
        // Check for blocking entities
        for (Entity e : level.getEntities()) {
            if (e.getX() == x && e.getY() == y && e.isAlive() && e.isBlocksMovement()) {
                return true;
            }
        }

        // Check for blocking items
        Item tileItem = level.getItemsGrid()[y][x];
        if (tileItem instanceof Gate) {
            return true;
        }
        return tileItem instanceof Bomb bomb && bomb.getState() != BombState.EXPLODED;
    }

    /**
     * Reconstructs the first step from source to goal by backtracking.
     *
     * @param source the starting tile
     * @param goalX the X coordinate of the goal
     * @param goalY the Y coordinate of the goal
     * @param previousTileX array tracking X coordinates of previous tiles
     * @param previousTileY array tracking Y coordinates of previous tiles
     * @return the tile immediately after the source on the path to goal
     */
    private Tile reconstructFirstStep(Tile source, int goalX, int goalY,
                                      int[][] previousTileX, int[][] previousTileY) {
        int startX = source.getX();
        int startY = source.getY();

        int stepX = goalX;
        int stepY = goalY;

        // Walk backwards until we reach the tile immediately after source
        while (!(previousTileX[stepY][stepX] == startX
                && previousTileY[stepY][stepX] == startY)) {
            int prevX = previousTileX[stepY][stepX];
            int prevY = previousTileY[stepY][stepX];

            // If parent chain is lost, abort
            if (prevX == -1 && prevY == -1) {
                return null;
            }

            stepX = prevX;
            stepY = prevY;
        }

        return level.getTile(stepY, stepX);
    }
}