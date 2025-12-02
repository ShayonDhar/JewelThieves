package game.level;

import game.entity.Direction;
import game.item.Door;
import game.item.Gate;
import game.item.Item;
import game.item.Lever;
import game.item.Loot;

import java.util.ArrayDeque;
import java.util.List;

/**
 * Service responsible for pathfinding operations within a level.
 * Handles breadth-first search and shortest path calculations for NPCs.
 *
 * @author Your Name
 * @version 1.0.0
 */
public class PathFindingService {

    private final Level level;

    /**
     * Constructs a PathfindingService for the given level.
     *
     * @param level the level to perform pathfinding in
     */
    public PathFindingService(Level level) {
        this.level = level;
    }

    /**
     * Finds the next tile on the shortest path from the source tile to the nearest
     * target (loot, lever, or exit).
     *
     * @param source the starting tile (where the NPC currently is)
     * @return the next tile to move to, or null if no reachable target exists
     */
    public Tile findShortestPathTarget(Tile source) {
        if (source == null) {
            return null;
        }

        int startX = source.getX();
        int startY = source.getY();

        // Determine which tiles are targets
        boolean[][] isTarget = new boolean[level.getLevelHeight()][level.getLevelWidth()];
        boolean hasLootOrLever = markLootAndLeverTargets(isTarget);

        // If no loot or levers, use exit tiles instead
        if (!hasLootOrLever) {
            if (!markExitTargets(isTarget)) {
                return null; // Nothing to pathfind to
            }
        }

        // Run BFS to find nearest target
        PathfindingResult result = breadthFirstSearch(startX, startY, isTarget);

        if (!result.foundGoal) {
            return null;
        }

        // Reconstruct the first step from source to goal
        return reconstructFirstStep(startX, startY, result);
    }

    /**
     * Marks all tiles containing loot or levers as targets.
     *
     * @param isTarget 2D array to mark target tiles
     * @return true if any loot or lever was found, false otherwise
     */
    private boolean markLootAndLeverTargets(boolean[][] isTarget) {
        boolean hasLootOrLever = false;
        Item[][] itemsGrid = level.getItemsGrid();

        for (int y = 0; y < level.getLevelHeight(); y++) {
            for (int x = 0; x < level.getLevelWidth(); x++) {
                Item item = itemsGrid[y][x];
                if (item instanceof Loot || item instanceof Lever) {
                    hasLootOrLever = true;
                    isTarget[y][x] = true;
                }
            }
        }

        return hasLootOrLever;
    }

    /**
     * Marks all exit tiles as targets.
     *
     * @param isTarget 2D array to mark target tiles
     * @return true if any exit tiles exist, false otherwise
     */
    private boolean markExitTargets(boolean[][] isTarget) {
        List<Tile> exitTiles = level.getExitTiles();

        if (exitTiles == null || exitTiles.isEmpty()) {
            return false;
        }

        for (Tile exit : exitTiles) {
            isTarget[exit.getY()][exit.getX()] = true;
        }

        return true;
    }

    /**
     * Performs breadth-first search from the starting position to find the nearest target.
     *
     * @param startX starting x-coordinate
     * @param startY starting y-coordinate
     * @param isTarget 2D array marking which tiles are targets
     * @return PathfindingResult containing the goal location and parent tracking arrays
     */
    private PathfindingResult breadthFirstSearch(int startX, int startY, boolean[][] isTarget) {
        int height = level.getLevelHeight();
        int width = level.getLevelWidth();

        boolean[][] visited = new boolean[height][width];
        int[][] previousTileX = new int[height][width];
        int[][] previousTileY = new int[height][width];

        // Initialize previous arrays to have no parent
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
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

        // BFS main loop
        while (!queue.isEmpty()) {
            int[] position = queue.removeFirst();
            int currentX = position[0];
            int currentY = position[1];

            // Check if we've reached a target (but not the starting tile)
            if (!(currentX == startX && currentY == startY) && isTarget[currentY][currentX]) {
                goalX = currentX;
                goalY = currentY;
                foundGoal = true;
                break; // First target found = nearest target
            }

            Tile currentTile = level.getTile(currentY, currentX);

            // Explore neighbors in all 4 directions
            for (Direction direction : Direction.values()) {
                Tile neighborTile = level.findNextValidTile(currentTile, direction);

                if (neighborTile == null) {
                    continue;
                }

                int nextX = neighborTile.getX();
                int nextY = neighborTile.getY();

                if (visited[nextY][nextX]) {
                    continue;
                }

                // Check if tile is passable (using null as mover for generic passability)
                if (isBlocked(neighborTile)) {
                    continue;
                }

                visited[nextY][nextX] = true;
                previousTileX[nextY][nextX] = currentX;
                previousTileY[nextY][nextX] = currentY;

                queue.addLast(new int[]{nextX, nextY});
            }
        }

        return new PathfindingResult(foundGoal, goalX, goalY, previousTileX, previousTileY);
    }

    /**
     * Checks if a tile is blocked for pathfinding purposes.
     *
     * @param tile the tile to check
     * @return true if the tile is blocked, false otherwise
     */
    private boolean isBlocked(Tile tile) {
        if (tile == null) {
            return true;
        }

        Item[][] itemsGrid = level.getItemsGrid();
        Item item = itemsGrid[tile.getY()][tile.getX()];

        // Gates and doors block pathfinding
        if (item instanceof Gate || item instanceof Door) {
            return true;
        }

        // Check for blocking entities
        for (var entity : level.getEntities()) {
            if (!entity.isAlive() || !entity.isBlocksMovement()) {
                continue;
            }
            if (entity.getX() == tile.getX() && entity.getY() == tile.getY()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Reconstructs the first step from the source to the goal by walking backwards
     * through the parent chain.
     *
     * @param startX starting x-coordinate
     * @param startY starting y-coordinate
     * @param result the pathfinding result containing goal and parent arrays
     * @return the tile representing the first step, or null if reconstruction fails
     */
    private Tile reconstructFirstStep(int startX, int startY, PathfindingResult result) {
        int stepX = result.goalX;
        int stepY = result.goalY;

        // Walk backwards until we find the tile immediately after the start
        while (!(result.previousTileX[stepY][stepX] == startX &&
                result.previousTileY[stepY][stepX] == startY)) {

            int prevX = result.previousTileX[stepY][stepX];
            int prevY = result.previousTileY[stepY][stepX];

            // If parent chain is broken, abort
            if (prevX == -1 && prevY == -1) {
                return null;
            }

            stepX = prevX;
            stepY = prevY;
        }

        return level.getTile(stepY, stepX);
    }

    /**
     * Internal class to hold the results of a pathfinding operation.
     */
    private static class PathfindingResult {
        final boolean foundGoal;
        final int goalX;
        final int goalY;
        final int[][] previousTileX;
        final int[][] previousTileY;

        PathfindingResult(boolean foundGoal, int goalX, int goalY,
                          int[][] previousTileX, int[][] previousTileY) {
            this.foundGoal = foundGoal;
            this.goalX = goalX;
            this.goalY = goalY;
            this.previousTileX = previousTileX;
            this.previousTileY = previousTileY;
        }
    }
}