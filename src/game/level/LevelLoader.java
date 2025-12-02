package game.level;

import game.GameController;
import game.entity.Direction;
import game.entity.Entity;
import game.entity.Player;
import game.entity.npc.FloorFollowingThief;
import game.entity.npc.FlyingAssassin;
import game.entity.npc.SmartThief;
import game.item.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.scene.paint.Color;

/**
 * This class reads the data from the level file.
 * It then loads the data stored in this file
 * onto the game.
 *
 * @author Alex Samuel
 * @version 1.0
 */

public class LevelLoader {
    private static final int COLOUR_NUMBER = 4;

    private final GameController controller;

    /**
     * Level loader constructor which links the controller
     * with the level loader.
     *
     * @param controller the game controller
     */
    public LevelLoader(GameController controller) {
        this.controller = controller;
    }

    /**
     * The load method which loads the level using the auxiliary methods below.
     *
     * @param filename The name of the file
     * @return the loaded level
     */

    public Level load(String filename) {
        Level level = new Level(controller);
        parseFile(filename, level);
        return level;
    }

    private void parseFile(String filename, Level level) {
        try (Scanner sc = new Scanner(new File(filename))) {
            readLevelDimensions(sc, level);
            initializeLevelGrids(level);
            readTileGrid(sc, level);
            readTokens(sc, level);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readLevelDimensions(Scanner sc, Level level) {
        level.setLevelWidth(sc.nextInt());
        level.setLevelHeight(sc.nextInt());
        sc.nextLine();  // move scanner to next line
    }

    private void initializeLevelGrids(Level level) {
        level.setLevelGrid(new Tile[level.getLevelHeight()][level.getLevelWidth()]);
        level.setItemsGrid(new Item[level.getLevelHeight()][level.getLevelWidth()]);
        ArrayList<Entity> entities = new ArrayList<>();
        level.setEntities(entities);
        level.setActiveBombs(new ArrayList<>());
        level.setExitTiles(new ArrayList<>());
    }

    private void readTileGrid(Scanner sc, Level level) {
        for (int y = 0; y < level.getLevelHeight(); y++) {
            for (int x = 0; x < level.getLevelWidth(); x++) {
                String tileCode = sc.next();
                Color[] colours = new Color[COLOUR_NUMBER];
                for (int i = 0; i < COLOUR_NUMBER; i++) {
                    Colour enumCol = Colour.fromChar(tileCode.charAt(i));
                    colours[i] = enumCol.getFXColor(); // convert to JavaFX Color
                }
                level.getLevelGrid()[y][x] = new Tile(x, y, colours);
            }
        }
    }

    private void readTokens(Scanner sc, Level level) {
        while (sc.hasNext()) {
            String token = sc.next();
            switch (token.toUpperCase()) {
                case "PLAYER" -> parsePlayer(sc, level);
                case "FLOORFOLLOWINGTHIEF" -> parseFloorFollowingThief(sc, level);
                case "ASSASSIN" -> parseFlyingAssassin(sc, level);
                case "SMARTTHIEF" -> parseSmartThief(sc, level);
                case "LOOT" -> parseLoot(sc, level);
                case "CLOCK" -> parseClock(sc, level);
                case "LEVER" -> parseLever(sc, level);
                case "GATE" -> parseGate(sc, level);
                case "BOMB" -> parseBomb(sc, level);
                case "DOOR" -> parseDoor(sc, level);
                case "EXIT" -> parseExit(sc, level);
                case "TIME" -> level.setRemainingTime(sc.nextInt());
                default -> {
                    if (token.startsWith("#")) {
                        sc.nextLine(); // skip comment
                    } else {
                        throw new IllegalArgumentException("Unknown token: " + token);
                    }
                }
            }
        }
    }

    // --- Entity / Item Parsing Methods ---
    private void parsePlayer(Scanner sc, Level level) {
        int x = sc.nextInt();
        int y = sc.nextInt();
        Direction dir = Direction.valueOf(sc.next().toUpperCase());
        boolean alive = Boolean.parseBoolean(sc.next());
        boolean blocks = Boolean.parseBoolean(sc.next());
        Player player = new Player(y, x, dir, alive, blocks, controller, level);
        level.getEntities().add(player);
        level.setPlayer(player);
    }

    private void parseFloorFollowingThief(Scanner sc, Level level) {
        int x = sc.nextInt();
        int y = sc.nextInt();
        Direction dir = Direction.valueOf(sc.next().toUpperCase());
        boolean alive = Boolean.parseBoolean(sc.next());
        boolean blocks = Boolean.parseBoolean(sc.next());
        Colour colour = Colour.valueOf(sc.next().toUpperCase());
        level.getEntities().add(new FloorFollowingThief(x, y, dir, alive, blocks, level, colour));
    }

    private void parseFlyingAssassin(Scanner sc, Level level) {
        int x = sc.nextInt();
        int y = sc.nextInt();
        Direction dir = Direction.valueOf(sc.next().toUpperCase());
        boolean alive = Boolean.parseBoolean(sc.next());
        boolean blocks = Boolean.parseBoolean(sc.next());
        level.getEntities().add(new FlyingAssassin(x, y, dir, alive, blocks, level));
    }

    private void parseSmartThief(Scanner sc, Level level) {
        int x = sc.nextInt();
        int y = sc.nextInt();
        Direction dir = Direction.valueOf(sc.next().toUpperCase());
        boolean alive = Boolean.parseBoolean(sc.next());
        boolean blocks = Boolean.parseBoolean(sc.next());
        level.getEntities().add(new SmartThief(x, y, dir, alive, blocks, level));
    }

    private void parseLoot(Scanner sc, Level level) {
        int id = sc.nextInt();
        int x = sc.nextInt();
        int y = sc.nextInt();
        boolean isOn = Boolean.parseBoolean(sc.next());
        String subtype = sc.next();
        LootType lootType = switch (subtype.toUpperCase()) {
            case "CENT" -> LootType.CENT;
            case "DOLLAR" -> LootType.DOLLAR;
            case "RUBY" -> LootType.RUBY;
            case "DIAMOND" -> LootType.DIAMOND;
            default -> throw new IllegalArgumentException("Unknown loot type: " + subtype);
        };
        level.setItemAt(y, x, new Loot(subtype, id, x, y, isOn, lootType));
    }

    private void parseClock(Scanner sc, Level level) {
        int id = sc.nextInt();
        int x = sc.nextInt();
        int y = sc.nextInt();
        boolean state = Boolean.parseBoolean(sc.next());
        int mod = sc.nextInt();
        level.setItemAt(y, x, new Clock("Clock", id, x, y, state, mod));
    }

    private void parseLever(Scanner sc, Level level) {
        int id = sc.nextInt();
        int x = sc.nextInt();
        int y = sc.nextInt();
        boolean state = Boolean.parseBoolean(sc.next());
        Colour colour = Colour.valueOf(sc.next().toUpperCase());
        level.setItemAt(y, x, new Lever("Lever", id, x, y, state, colour));
    }

    private void parseGate(Scanner sc, Level level) {
        int id = sc.nextInt();
        int x = sc.nextInt();
        int y = sc.nextInt();
        boolean state = Boolean.parseBoolean(sc.next());
        Colour colour = Colour.valueOf(sc.next().toUpperCase());
        level.setItemAt(y, x, new Gate("Gate", id, x, y, state, colour));
    }

    private void parseBomb(Scanner sc, Level level) {
        int id = sc.nextInt();
        int x = sc.nextInt();
        int y = sc.nextInt();
        boolean state = Boolean.parseBoolean(sc.next());
        level.setItemAt(y, x, new Bomb("Bomb", id, x, y, state));
    }

    private void parseDoor(Scanner sc, Level level) {
        int id = sc.nextInt();
        int x = sc.nextInt();
        int y = sc.nextInt();
        boolean state = Boolean.parseBoolean(sc.next());
        level.setItemAt(y, x, new Door("Door", id, x, y, state));
    }

    private void parseExit(Scanner sc, Level level) {
        int x = sc.nextInt();
        int y = sc.nextInt();
        level.getExitTiles().add(level.getTile(y, x));
    }
}

