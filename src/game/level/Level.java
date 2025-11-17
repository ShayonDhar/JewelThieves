package game.level;

import game.entity.Entity;

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
    private List<Tile> tiles;
    private List<Entity> entities;
    private int width;
    private int height;
    private Level nextLevel;
    private int remainingTime;
    private int initialTime;
    private boolean levelComplete;
    private boolean levelFailed;




}
