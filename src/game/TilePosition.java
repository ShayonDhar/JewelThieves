package game;

/**
 * TilePosition record class which solely gets the tile position
 * Prevents having to create Tile objects can talk
 * about coordinates without dragging around entire
 * Tile objects full of game rules, items, and NPCs.
 *
 * @author Alex Samuel
 * @version 1.0
 *
 * @param x tile x coordinate
 * @param y tile y coordinate
 */

public record TilePosition(int x, int y) {
}
