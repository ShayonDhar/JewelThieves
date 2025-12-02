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

    /**
     * Private constructor to prevent instantiation.
     * This is a utility class with only static methods.
     */
    private GameSaveWriter() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    /**
     * Writes the tile grid to the save file.
     *
     * @param writer the PrintWriter to write to
     * @param level the level containing the tile grid
     */
    public static void writeTileGrid(PrintWriter writer, Level level) {
        Tile[][] grid = level.getLevelGrid();

        for (int y = 0; y < level.getLevelHeight(); y++) {
            for (int x = 0; x < level.getLevelWidth(); x++) {
                Tile tile = grid[y][x];
                Color[] colours = tile.getColours();

                // Convert colors back to tile code (4 characters)
                StringBuilder tileCode = new StringBuilder();
                for (int i = 0; i < 4; i++) {
                    tileCode.append(Colour.fromFXColor(colours[i]).getCode());
                }

                writer.print(tileCode);
                if (x < level.getLevelWidth() - 1) {
                    writer.print(" ");
                }
            }
            writer.println();
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

        if (entities == null) return;

        for (Entity entity : entities) {
            if (entity instanceof Player) {
                writePlayer(writer, (Player) entity);
            } else if (entity instanceof FloorFollowingThief) {
                writeFloorFollowingThief(writer, (FloorFollowingThief) entity);
            } else if (entity instanceof FlyingAssassin) {
                writeFlyingAssassin(writer, (FlyingAssassin) entity);
            } else if (entity instanceof SmartThief) {
                writeSmartThief(writer, (SmartThief) entity);
            }
        }
    }

    /**
     * Writes player data to the save file.
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

        if (itemsGrid == null) return;

        for (int y = 0; y < level.getLevelHeight(); y++) {
            for (int x = 0; x < level.getLevelWidth(); x++) {
                Item item = itemsGrid[y][x];

                if (item == null) continue;

                if (item instanceof Loot) {
                    writeLoot(writer, (Loot) item);
                } else if (item instanceof Clock) {
                    writeClock(writer, (Clock) item);
                } else if (item instanceof Lever) {
                    writeLever(writer, (Lever) item);
                } else if (item instanceof Gate) {
                    writeGate(writer, (Gate) item);
                } else if (item instanceof Bomb) {
                    writeBomb(writer, (Bomb) item);
                } else if (item instanceof Door) {
                    writeDoor(writer, (Door) item);
                }
            }
        }
    }

    /**
     * Writes Loot data to the save file.
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

        if (exitTiles == null) return;

        for (Tile exit : exitTiles) {
            writer.println("EXIT " + exit.getX() + " " + exit.getY());
        }
    }
}