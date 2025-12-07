package game.save;

import game.entity.Entity;
import game.entity.Player;
import game.entity.npc.FloorFollowingThief;
import game.entity.npc.FlyingAssassin;
import game.entity.npc.SmartThief;
import game.item.*;
import game.level.Colour;
import game.level.Level;
import game.level.Tile;
import game.playerProfile.PlayerProfile;
import game.playerProfile.ProfileSession;
import javafx.scene.paint.Color;

import java.io.PrintWriter;
import java.util.List;

/**
 * Utility class for writing game state data to save files.
 * Handles the serialization of tiles, entities, and items into
 * the level file format.
 *
 * @author Shayon Dhar
 * @version 1.0.0
 */
public class GameSaveWriter {

    private static final String SHOULD_NOT_BE_INSTANTIATED = "Utility class should not be instantiated";
    private static final String UNKNOWN_ENTITY_SPOTTED = "Warning: Ignoring unknown entity spotted: ";
    private static final String UNKNOWN_ITEM_SPOTTED = "Warning: Ignoring unknown item spotted: ";

    /**
     * Private constructor to prevent instantiation.
     * This is a utility class with only static methods.
     */
    private GameSaveWriter() {
        throw new AssertionError(SHOULD_NOT_BE_INSTANTIATED);
    }

    /**
     * Writes the tile grid to the save file.
     *
     * @param writer the PrintWriter to write to
     * @param level the level containing the tile grid
     */
    public static void writeTileGrid(PrintWriter writer, Level level) {
        Tile[][] grid = level.getLevelGrid();
        int height = level.getLevelHeight();
        int width = level.getLevelWidth();

        for (int y = 0; y < height; y++) {
            StringBuilder line = new StringBuilder();
            for (int x = 0; x < width; x++) {
                Tile tile = grid[y][x];
                Color[] colours = tile.getColours();

                // Convert colors back to tile code (4 characters)
                StringBuilder tileCode = new StringBuilder(4);
                for (int i = 0; i < 4; i++) {
                    tileCode.append(Colour.fromFXColor(colours[i]).getCode());
                }

                line.append(tileCode);
                if (x < width - 1) {
                    line.append(' ');
                }
            }
            writer.println(line);
        }
    }

    /**
     * Writes all entities (player and NPCs) to the save file.
     *
     * @param writer the PrintWriter to write to
     * @param level the level containing the entities
     */
    public static void writeEntities(PrintWriter writer, Level level) {
        List<Entity> entities = level.getEntities();
        if (entities == null) {
            return;
        }

        for (Entity entity : entities) {
            writeEntity(writer, entity);
        }
    }

    /**
     * Writes a single entity to the save file based on its type.
     *
     * @param writer the PrintWriter to write to
     * @param entity the entity to write
     */
    private static void writeEntity(PrintWriter writer, Entity entity) {
        switch (entity) {
            case Player player -> writePlayer(writer, player);
            case FloorFollowingThief thief -> writeFloorFollowingThief(writer, thief);
            case FlyingAssassin assassin -> writeFlyingAssassin(writer, assassin);
            case SmartThief thief -> writeSmartThief(writer, thief);
            default -> System.out.println(UNKNOWN_ENTITY_SPOTTED + entity);
        }
    }

    /**
     * Writes player data to the save file.
     *
     * @param writer the PrintWriter to write to
     * @param player the player to write
     */
    private static void writePlayer(PrintWriter writer, Player player) {
        writer.println("PLAYER " +
                player.getX() + " " +
                player.getY() + " " +
                player.getDirection().name() + " " +
                player.isAlive() + " " +
                player.isBlocksMovement());
    }

    /**
     * Writes FloorFollowingThief data to the save file.
     *
     * @param writer the PrintWriter to write to
     * @param thief the FloorFollowingThief to write
     */
    private static void writeFloorFollowingThief(PrintWriter writer, FloorFollowingThief thief) {
        writer.println("FLOORFOLLOWINGTHIEF " +
                thief.getX() + " " +
                thief.getY() + " " +
                thief.getDirection().name() + " " +
                thief.isAlive() + " " +
                thief.isBlocksMovement() + " " +
                thief.getFollowingColour().name());
    }

    /**
     * Writes FlyingAssassin data to the save file.
     *
     * @param writer the PrintWriter to write to
     * @param assassin the FlyingAssassin to write
     */
    private static void writeFlyingAssassin(PrintWriter writer, FlyingAssassin assassin) {
        writer.println("ASSASSIN " +
                assassin.getX() + " " +
                assassin.getY() + " " +
                assassin.getDirection().name() + " " +
                assassin.isAlive() + " " +
                assassin.isBlocksMovement());
    }

    /**
     * Writes SmartThief data to the save file.
     *
     * @param writer the PrintWriter to write to
     * @param thief the SmartThief to write
     */
    private static void writeSmartThief(PrintWriter writer, SmartThief thief) {
        writer.println("SMARTTHIEF " +
                thief.getX() + " " +
                thief.getY() + " " +
                thief.getDirection().name() + " " +
                thief.isAlive() + " " +
                thief.isBlocksMovement());
    }

    /**
     * Writes all items to the save file.
     *
     * @param writer the PrintWriter to write to
     * @param level the level containing the items
     */
    public static void writeItems(PrintWriter writer, Level level) {
        Item[][] itemsGrid = level.getItemsGrid();
        if (itemsGrid == null) {
            return;
        }

        int height = level.getLevelHeight();
        int width = level.getLevelWidth();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Item item = itemsGrid[y][x];
                if (item != null) {
                    writeItem(writer, item);
                }
            }
        }
    }

    /**
     * Writes a single item to the save file based on its type.
     *
     * @param writer the PrintWriter to write to
     * @param item the item to write
     */
    private static void writeItem(PrintWriter writer, Item item) {
        switch (item) {
            case Loot loot -> writeLoot(writer, loot);
            case Clock clock -> writeClock(writer, clock);
            case Lever lever -> writeLever(writer, lever);
            case Gate gate -> writeGate(writer, gate);
            case Bomb bomb -> writeBomb(writer, bomb);
            case Door door -> writeDoor(writer, door);
            default -> System.out.println(UNKNOWN_ITEM_SPOTTED + item);
        }
    }

    /**
     * Writes Loot data to the save file.
     *
     * @param writer the PrintWriter to write to
     * @param loot the Loot to write
     */
    private static void writeLoot(PrintWriter writer, Loot loot) {
        writer.println("LOOT " +
                loot.getItemID() + " " +
                loot.getX() + " " +
                loot.getY() + " " +
                loot.isOn + " " +
                loot.getLootType().name());
    }

    /**
     * Writes Clock data to the save file.
     *
     * @param writer the PrintWriter to write to
     * @param clock the Clock to write
     */
    private static void writeClock(PrintWriter writer, Clock clock) {
        writer.println("CLOCK " +
                clock.getItemID() + " " +
                clock.getX() + " " +
                clock.getY() + " " +
                clock.isOn + " " +
                clock.getTimeBonus());
    }

    /**
     * Writes Lever data to the save file.
     *
     * @param writer the PrintWriter to write to
     * @param lever the Lever to write
     */
    private static void writeLever(PrintWriter writer, Lever lever) {
        writer.println("LEVER " +
                lever.getItemID() + " " +
                lever.getX() + " " +
                lever.getY() + " " +
                lever.isOn + " " +
                lever.getColour().name());
    }

    /**
     * Writes Gate data to the save file.
     *
     * @param writer the PrintWriter to write to
     * @param gate the Gate to write
     */
    private static void writeGate(PrintWriter writer, Gate gate) {
        writer.println("GATE " +
                gate.getItemID() + " " +
                gate.getX() + " " +
                gate.getY() + " " +
                gate.isOn + " " +
                gate.getColour().name());
    }

    /**
     * Writes Bomb data to the save file.
     *
     * @param writer the PrintWriter to write to
     * @param bomb the Bomb to write
     */
    private static void writeBomb(PrintWriter writer, Bomb bomb) {
        writer.println("BOMB " +
                bomb.getItemID() + " " +
                bomb.getX() + " " +
                bomb.getY() + " " +
                bomb.isOn);
    }

    /**
     * Writes Door data to the save file.
     *
     * @param writer the PrintWriter to write to
     * @param door the Door to write
     */
    private static void writeDoor(PrintWriter writer, Door door) {
        writer.println("DOOR " +
                door.getItemID() + " " +
                door.getX() + " " +
                door.getY() + " " +
                door.isOn);
    }

    /**
     * Writes exit tile positions to the save file.
     *
     * @param writer the PrintWriter to write to
     * @param level the level containing the exit tiles
     */
    public static void writeExitTiles(PrintWriter writer, Level level) {
        List<Tile> exitTiles = level.getExitTiles();
        if (exitTiles == null) {
            return;
        }

        for (Tile exit : exitTiles) {
            writer.println("EXIT " + exit.getX() + " " + exit.getY());
        }
    }
}