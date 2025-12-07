package game.level;

import game.GameController;
import game.entity.Direction;
import game.entity.Entity;
import game.entity.Player;
import game.entity.npc.FloorFollowingThief;
import game.entity.npc.FlyingAssassin;
import game.entity.npc.SmartThief;
import game.item.*;

import java.util.*;

/**
 * Manages NPC movement, pathfinding, and interactions within a level.
 * This class handles all NPC-related logic including movement calculations,
 * combat interactions, and item collection for different NPC types.
 *
 * @author Refactored from Level.java
 * @version 1.0.0
 */
public class NPCManager {
    private final Level level;
    private final Random smartThiefRandomMove = new Random();
    private final SmartThiefPathfinder pathfinder;

    /**
     * Creates a new NPCManager for the given level.
     *
     * @param level the level this manager operates on
     */
    public NPCManager(Level level) {
        this.level = level;
        this.pathfinder = new SmartThiefPathfinder(level);
    }

    /**
     * Updates all NPCs in the level, handling their movement and interactions.
     */
    public void updateAllNPCs() {
        List<Entity> entities = level.getEntities();
        if (entities == null) {
            return;
        }

        List<Entity> entityCopy = new ArrayList<>(entities);
        List<Entity> toRemove = new ArrayList<>();

        for (Entity entity : entityCopy) {
            if (entity instanceof game.entity.npc.NPC npc && npc.isAlive()) {
                Tile targetTile = calculateNPCMovement(npc);
                if (targetTile != null) {
                    handleNPCMovementAndInteractions(npc, targetTile, entityCopy, toRemove);
                }
            }
        }

        entities.removeAll(toRemove);
    }

    /**
     * Calculates the target tile for an NPCs movement.
     *
     * @param npc the NPC to calculate movement for
     * @return the target tile, or null if no valid movement exists
     */
    private Tile calculateNPCMovement(game.entity.npc.NPC npc) {
        Tile currentTile = level.getTile(npc.getY(), npc.getX());
        if (currentTile == null) {
            return null;
        }

        return getNextTileForNpc(npc);
    }

    /**
     * Determines the next tile that an NPC should move to based on that NPCs
     * movement rules.
     *
     * @param npc the NPC requesting its next tile
     * @return the tile the NPC should move to, or null if no valid move exists
     */
    public Tile getNextTileForNpc(Entity npc) {
        Tile current = level.getTile(npc.getY(), npc.getX());
        if (current == null) {
            return null;
        }

        return switch (npc) {
            case FlyingAssassin flyingAssassin -> getNextTileForFlyingAssassin(flyingAssassin, current);
            case FloorFollowingThief floorThief -> getNextTileForFloorFollowingThief(floorThief, current);
            case SmartThief smartThief -> getNextTileForSmartThief(smartThief, current);
            default -> null;
        };
    }

    /**
     * Calculates the next tile for a Flying Assassin NPC.
     * Flying Assassins move in straight lines and reverse direction when hitting boundaries.
     *
     * @param flyingAssassin the Flying Assassin entity
     * @param current        the tile the assassin is currently on
     * @return the next tile to move to, or null if no valid move exists
     */
    private Tile getNextTileForFlyingAssassin(FlyingAssassin flyingAssassin, Tile current) {
        Direction flyingDirection = flyingAssassin.getDirection();
        int dx = getOffsetX(flyingDirection);
        int dy = getOffsetY(flyingDirection);

        int nextX = current.getX() + dx;
        int nextY = current.getY() + dy;

        // If about to leave bounds, turn around
        if (!level.isInBounds(nextX, nextY)) {
            flyingDirection = flyingDirection.opposite();
            flyingAssassin.setDirection(flyingDirection);

            dx = getOffsetX(flyingDirection);
            dy = getOffsetY(flyingDirection);

            nextX = current.getX() + dx;
            nextY = current.getY() + dy;

            if (!level.isInBounds(nextX, nextY)) {
                return null;
            }
        }

        return level.getTile(nextY, nextX);
    }

    /**
     * Calculates the next tile for a Floor Following Thief NPC.
     * Floor Following Thieves follow a specific colour and use directional priority.
     *
     * @param floorThief the Floor Following Thief entity
     * @param current    the tile the thief is currently on
     * @return the next tile to move to, or null if no valid move exists
     */
    private Tile getNextTileForFloorFollowingThief(FloorFollowingThief floorThief, Tile current) {
        Colour followingColour = floorThief.getFollowingColour();
        Direction[] directionPriority = floorThief.getDirectionPriority();

        for (Direction floorDirection : directionPriority) {
            Tile candidateTile = level.findNextValidTile(current, floorDirection);
            if (candidateTile != null
                    && !(level.tileSharesFollowingColour(current, followingColour)
                    || level.tileSharesFollowingColour(candidateTile, followingColour))
                    && !level.blocksMovement(floorThief, candidateTile)) {
                floorThief.setDirection(floorDirection);
                return candidateTile;
            }
        }

        return null;
    }

    /**
     * Calculates the next tile for a Smart Thief NPC.
     * Smart Thieves use pathfinding to reach targets, falling back to random valid moves.
     *
     * @param smartThief the Smart Thief entity
     * @param current    the tile the thief is currently on
     * @return the next tile to move to, or null if no valid move exists
     */
    private Tile getNextTileForSmartThief(SmartThief smartThief, Tile current) {
        // Try pathfinding first
        Tile nextTileMovingTo = pathfinder.findShortestPathTarget(current);
        if (nextTileMovingTo != null && !level.blocksMovement(smartThief, nextTileMovingTo)) {
            updateSmartThiefDirection(smartThief, current, nextTileMovingTo);
            return nextTileMovingTo;
        }

        // Fall back to random valid move
        Tile randomlyMovingTo = getRandomButValidMove(current, smartThief);
        if (randomlyMovingTo != null) {
            updateSmartThiefDirection(smartThief, current, randomlyMovingTo);
            return randomlyMovingTo;
        }

        return null;
    }

    /**
     * Updates the Smart Thief's facing direction based on target tile.
     *
     * @param smartThief the Smart Thief whose direction needs updating
     * @param from       the current tile
     * @param to         the target tile
     */
    private void updateSmartThiefDirection(SmartThief smartThief, Tile from, Tile to) {
        Direction direction = getDirectionBetween(from, to);
        if (direction != null) {
            smartThief.setDirection(direction);
        }
    }

    /**
     * Returns a "random but valid" tile that Smart Thief could move to
     * as part of its movement from the given tile, or null if no such move exists.
     *
     * @param smartCurrentTile smart thief current tile
     * @param mover            smart thief "random but valid" target tile
     * @return Tile that is valid to move to but random
     */
    private Tile getRandomButValidMove(Tile smartCurrentTile, Entity mover) {
        List<Direction> smartDirections = new ArrayList<>(Arrays.asList(Direction.values()));
        Collections.shuffle(smartDirections, smartThiefRandomMove);

        for (Direction direction : smartDirections) {
            Tile candidateTile = level.findNextValidTile(smartCurrentTile, direction);
            if (candidateTile != null && !level.blocksMovement(mover, candidateTile)) {
                return candidateTile;
            }
        }
        return null;
    }

    /**
     * Works out direction smart thief would travel from one tile to another.
     * Assumes both tiles are on the same row/column (as they should)
     *
     * @param from current tile of smart thief
     * @param to   target tile of smart thief
     * @return Direction between two tiles
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
     * Handles NPC movement and all related interactions (combat, item collection, etc.).
     *
     * @param npc        the NPC being updated
     * @param targetTile the tile the NPC is moving to
     * @param entityCopy copy of all entities for collision checking
     * @param toRemove   list to track entities that should be removed
     */
    private void handleNPCMovementAndInteractions(game.entity.npc.NPC npc, Tile targetTile,
                                                  List<Entity> entityCopy, List<Entity> toRemove) {
        int targetX = targetTile.getX();
        int targetY = targetTile.getY();

        if (npc instanceof FlyingAssassin flyingAssassin) {
            handleFlyingAssassinInteractions(flyingAssassin, targetX, targetY, entityCopy, toRemove);
        } else if (npc instanceof SmartThief smartThief) {
            handleSmartThiefInteractions(smartThief, targetX, targetY);
        } else if (npc instanceof FloorFollowingThief floorThief) {
            floorThief.setPosition(targetX, targetY);
        }
    }

    /**
     * Handles Flying Assassin combat interactions with player and other NPCs.
     *
     * @param flyingAssassin the Flying Assassin entity
     * @param targetX        target X coordinate
     * @param targetY        target Y coordinate
     * @param entityCopy     copy of all entities
     * @param toRemove       list of entities to remove
     */
    private void handleFlyingAssassinInteractions(FlyingAssassin flyingAssassin, int targetX, int targetY,
                                                  List<Entity> entityCopy, List<Entity> toRemove) {
        int currentX = flyingAssassin.getX();
        int currentY = flyingAssassin.getY();

        if (checkPlayerOnCurrentTile(currentX, currentY)) {
            return;
        }
        handleNPCsAtPosition(flyingAssassin, currentX, currentY, entityCopy, toRemove);

        if (checkPlayerOnTargetTile(flyingAssassin, targetX, targetY)) {
            return;
        }

        handleNPCsOnTargetTile(flyingAssassin, targetX, targetY, entityCopy, toRemove);
        moveFlyingAssassin(flyingAssassin, targetX, targetY);
    }

    /**
     * Checks if the player is on the current tile of the Flying Assassin.
     *
     * @param currentX current X coordinate of the Flying Assassin
     * @param currentY current Y coordinate of the Flying Assassin
     * @return true if player is killed and game over triggered, false otherwise
     */
    private boolean checkPlayerOnCurrentTile(int currentX, int currentY) {
        Player player = level.getPlayer();
        if (player != null && player.isAlive() && player.getX() == currentX && player.getY() == currentY) {
            player.die(false);
            GameController.gameOver();
            return true;
        }
        return false;
    }

    /**
     * Checks if the player is on the target tile of the Flying Assassin.
     *
     * @param flyingAssassin the Flying Assassin entity
     * @param targetX        target X coordinate
     * @param targetY        target Y coordinate
     * @return true if player is killed and game over triggered, false otherwise
     */
    private boolean checkPlayerOnTargetTile(FlyingAssassin flyingAssassin, int targetX, int targetY) {
        Player player = level.getPlayer();
        if (player != null && player.isAlive() && player.getX() == targetX && player.getY() == targetY) {
            player.die(false);
            flyingAssassin.setPosition(targetX, targetY);
            GameController.gameOver();
            return true;
        }
        return false;
    }

    /**
     * Handles NPCs located on the target tile of the Flying Assassin.
     *
     * @param flyingAssassin the Flying Assassin entity
     * @param targetX        target X coordinate
     * @param targetY        target Y coordinate
     * @param entityCopy     copy of all entities
     * @param toRemove       list of entities to remove
     */
    private void handleNPCsOnTargetTile(FlyingAssassin flyingAssassin, int targetX, int targetY,
                                        List<Entity> entityCopy, List<Entity> toRemove) {
        handleNPCsAtPosition(flyingAssassin, targetX, targetY, entityCopy, toRemove);
    }

    /**
     * Helper method to handle NPCs at a specific position for Flying Assassin interactions.
     * Kills any NPCs (except the assassin and player) at the given coordinates.
     *
     * @param flyingAssassin the Flying Assassin entity
     * @param x              X coordinate to check
     * @param y              Y coordinate to check
     * @param entityCopy     copy of all entities
     * @param toRemove       list of entities to remove
     */
    private void handleNPCsAtPosition(FlyingAssassin flyingAssassin, int x, int y,
                                      List<Entity> entityCopy, List<Entity> toRemove) {
        Player player = level.getPlayer();
        for (Entity otherNPC : entityCopy) {
            if (otherNPC != flyingAssassin && otherNPC != player && otherNPC.isAlive()
                    && otherNPC.getX() == x && otherNPC.getY() == y) {
                otherNPC.die(false);
                toRemove.add(otherNPC);
            }
        }
    }

    /**
     * Moves the Flying Assassin to the target tile.
     *
     * @param flyingAssassin the Flying Assassin entity
     * @param targetX        target X coordinate
     * @param targetY        target Y coordinate
     */
    private void moveFlyingAssassin(FlyingAssassin flyingAssassin, int targetX, int targetY) {
        flyingAssassin.setPosition(targetX, targetY);
    }

    /**
     * Handles Smart Thief item collection and exit interactions.
     *
     * @param smartThief the Smart Thief entity
     * @param targetX    target X coordinate
     * @param targetY    target Y coordinate
     */
    private void handleSmartThiefInteractions(SmartThief smartThief, int targetX, int targetY) {
        smartThief.setPosition(targetX, targetY);

        Item item = level.getItemAt(targetY, targetX);

        if (item instanceof Loot) {
            level.removeItemFromGrid(targetY, targetX);
        } else if (item instanceof Lever lever) {
            level.openGatesOfColour(lever.getColour());
            level.removeItemFromGrid(targetY, targetX);
        } else if (item instanceof Door && level.allLootAndLeversCollected()) {
            level.setLevelFailed(true); // Thief wins, player loses
        } else if (item instanceof Clock) {
            level.removeItemFromGrid(targetY, targetX);
        }

        List<Bomb> activeBombs = level.getActiveBombs();
        if (activeBombs != null) {
            for (Bomb bomb : new ArrayList<>(activeBombs)) {
                bomb.updateBombState(level);
            }
        }
    }
}
