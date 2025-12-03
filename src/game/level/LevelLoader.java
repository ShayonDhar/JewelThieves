package game.level;

import game.GameController;
import game.entity.Direction;
import game.entity.Player;
import game.entity.npc.FloorFollowingThief;
import game.entity.npc.FlyingAssassin;
import game.entity.npc.SmartThief;
import game.item.*;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class reads the data from the level file.
 * It then loads the data stored in this file onto the game.
 *
 * @author Alex Samuel.
 */
public class LevelLoader {

    private static final int TILE_COLOR_COUNT = 4;
    private final GameController controller;

    public LevelLoader(GameController controller) {
        this.controller = controller;
    }

    /**
     * Loads a level from the specified file.
     *
     * @param filename the name of the level file to load
     * @return the loaded Level object, or null if loading fails
     */
    public Level load(String filename) {
        Level level = new Level(controller);
        parseFile(filename, level);
        return level;
    }

    /**
     * Parses the level file and populates the Level object.
     * Reads level dimensions, tile grid, and all entities/items from the file.
     *
     * @param filename the name of the file to parse
     * @param level    the Level object to populate with data from the file
     */
    private void parseFile(String filename, Level level) {
        try (Scanner sc = new Scanner(new File(filename))) {
            initializeLevel(sc, level);
            loadTileGrid(sc, level);
            loadEntitiesAndItems(sc, level);
        } catch (FileNotFoundException e) {
            System.out.println("Level file not found: " + filename);
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error parsing level file: " + filename);
            e.printStackTrace();
        }
    }

    /**
     * Initializes the level dimensions and data structures.
     * Reads width and height from the file and creates empty grids and lists.
     *
     * @param sc    the Scanner reading the file
     * @param level the Level to initialize with dimensions and empty data structures
     */
    private void initializeLevel(Scanner sc, Level level) {
        level.setLevelWidth(sc.nextInt());
        level.setLevelHeight(sc.nextInt());
        sc.nextLine();

        level.setLevelGrid(new Tile[level.getLevelHeight()][level.getLevelWidth()]);
        level.setItemsGrid(new Item[level.getLevelHeight()][level.getLevelWidth()]);
        level.setEntities(new ArrayList<>());
        level.setActiveBombs(new ArrayList<>());
        level.setExitTiles(new ArrayList<>());
    }

    /**
     * Loads the tile grid from the file.
     * Reads tile color codes and creates Tile objects for each position.
     *
     * @param sc    the Scanner reading the file
     * @param level the Level to populate with tile data
     */
    private void loadTileGrid(Scanner sc, Level level) {
        for (int y = 0; y < level.getLevelHeight(); y++) {
            for (int x = 0; x < level.getLevelWidth(); x++) {
                String tileCode = sc.next();
                Color[] colours = parseTileColors(tileCode);
                level.getLevelGrid()[y][x] = new Tile(x, y, colours);
            }
        }
    }

    /**
     * Parses the color codes from a tile string.
     * Converts 4-character tile code into an array of JavaFX Color objects.
     *
     * @param tileCode the 4-character tile code representing colors
     * @return array of 4 JavaFX Colors corresponding to the tile code
     */
    private Color[] parseTileColors(String tileCode) {
        Color[] colours = new Color[TILE_COLOR_COUNT];
        for (int i = 0; i < TILE_COLOR_COUNT; i++) {
            Colour enumCol = Colour.fromChar(tileCode.charAt(i));
            colours[i] = enumCol.getFXColor();
        }
        return colours;
    }

    /**
     * Loads all entities and items from the remaining file tokens.
     * Processes each token until the end of the file is reached.
     *
     * @param sc    the Scanner reading the file
     * @param level the Level to populate with entities and items
     */
    private void loadEntitiesAndItems(Scanner sc, Level level) {
        while (sc.hasNext()) {
            String token = sc.next();
            parseToken(token, sc, level);
        }
    }

    /**
     * Parses a single token and creates the corresponding entity or item.
     * Delegates to specific loading methods based on the token type.
     *
     * @param token the token string to parse (e.g., "PLAYER", "LOOT")
     * @param sc    the Scanner for reading additional data associated with the token
     * @param level the Level to add the created entity or item to
     */
    private void parseToken(String token, Scanner sc, Level level) {
        switch (token.toUpperCase()) {
            case "PLAYER" -> loadPlayer(sc, level);
            case "FLOORFOLLOWINGTHIEF" -> loadFloorFollowingThief(sc, level);
            case "ASSASSIN" -> loadFlyingAssassin(sc, level);
            case "SMARTTHIEF" -> loadSmartThief(sc, level);
            case "LOOT" -> loadLoot(sc, level);
            case "CLOCK" -> loadClock(sc, level);
            case "LEVER" -> loadLever(sc, level);
            case "GATE" -> loadGate(sc, level);
            case "BOMB" -> loadBomb(sc, level);
            case "DOOR" -> loadDoor(sc, level);
            case "EXIT" -> loadExit(sc, level);
            case "TIME" -> level.setRemainingTime(sc.nextInt());
            default -> handleUnknownToken(token, sc);
        }
    }

    /**
     * Loads the player entity from the file.
     * Reads position, direction, alive status, and blocking status.
     *
     * @param sc    the Scanner reading the file
     * @param level the Level to add the player to
     */
    private void loadPlayer(Scanner sc, Level level) {
        int x = sc.nextInt();
        int y = sc.nextInt();
        Direction dir = Direction.valueOf(sc.next().toUpperCase());
        boolean alive = sc.nextBoolean();
        boolean blocks = sc.nextBoolean();

        Player player = new Player(y, x, dir, alive, blocks, controller, level);
        level.getEntities().add(player);
        level.setPlayer(player);
    }

    /**
     * Loads a Floor Following Thief entity from the file.
     * Reads position, direction, alive status, blocking status, and following color.
     *
     * @param sc    the Scanner reading the file
     * @param level the Level to add the thief to
     */
    private void loadFloorFollowingThief(Scanner sc, Level level) {
        int x = sc.nextInt();
        int y = sc.nextInt();
        Direction dir = Direction.valueOf(sc.next().toUpperCase());
        boolean alive = sc.nextBoolean();
        boolean blocks = sc.nextBoolean();
        Colour colour = Colour.valueOf(sc.next().toUpperCase());

        level.getEntities().add(new FloorFollowingThief(x, y, dir, alive, blocks, level, colour));
    }

    /**
     * Loads a Flying Assassin entity from the file.
     * Reads position, direction, alive status, and blocking status.
     *
     * @param sc    the Scanner reading the file
     * @param level the Level to add the assassin to
     */
    private void loadFlyingAssassin(Scanner sc, Level level) {
        int x = sc.nextInt();
        int y = sc.nextInt();
        Direction dir = Direction.valueOf(sc.next().toUpperCase());
        boolean alive = sc.nextBoolean();
        boolean blocks = sc.nextBoolean();

        level.getEntities().add(new FlyingAssassin(x, y, dir, alive, blocks, level));
    }

    /**
     * Loads a Smart Thief entity from the file.
     * Reads position, direction, alive status, and blocking status.
     *
     * @param sc    the Scanner reading the file
     * @param level the Level to add the smart thief to
     */
    private void loadSmartThief(Scanner sc, Level level) {
        int x = sc.nextInt();
        int y = sc.nextInt();
        Direction dir = Direction.valueOf(sc.next().toUpperCase());
        boolean alive = sc.nextBoolean();
        boolean blocks = sc.nextBoolean();

        level.getEntities().add(new SmartThief(x, y, dir, alive, blocks, level));
    }

    /**
     * Loads a loot item from the file.
     * Reads ID, position, state, and loot type (CENT, DOLLAR, RUBY, DIAMOND).
     *
     * @param sc    the Scanner reading the file
     * @param level the Level to add the loot to
     */
    private void loadLoot(Scanner sc, Level level) {
        int id = sc.nextInt();
        int x = sc.nextInt();
        int y = sc.nextInt();
        boolean isOn = sc.nextBoolean();
        String subtype = sc.next();

        LootType lootType = parseLootType(subtype);
        level.setItemAt(y, x, new Loot(subtype, id, x, y, isOn, lootType));
    }

    /**
     * Parses a loot type string into a LootType enum.
     *
     * @param subtype the string representation of the loot type
     * @return the corresponding LootType enum value
     * @throws IllegalArgumentException if the subtype is not recognized
     */
    private LootType parseLootType(String subtype) {
        return switch (subtype.toUpperCase()) {
            case "CENT" -> LootType.CENT;
            case "DOLLAR" -> LootType.DOLLAR;
            case "RUBY" -> LootType.RUBY;
            case "DIAMOND" -> LootType.DIAMOND;
            default -> throw new IllegalArgumentException("Unknown loot type: " + subtype);
        };
    }

    /**
     * Loads a clock item from the file.
     * Reads ID, position, state, and time modifier value.
     *
     * @param sc    the Scanner reading the file
     * @param level the Level to add the clock to
     */
    private void loadClock(Scanner sc, Level level) {
        int id = sc.nextInt();
        int x = sc.nextInt();
        int y = sc.nextInt();
        boolean state = sc.nextBoolean();
        int mod = sc.nextInt();

        level.setItemAt(y, x, new Clock("Clock", id, x, y, state, mod));
    }

    /**
     * Loads a lever item from the file.
     * Reads ID, position, state, and the color of gates it controls.
     *
     * @param sc    the Scanner reading the file
     * @param level the Level to add the lever to
     */
    private void loadLever(Scanner sc, Level level) {
        int id = sc.nextInt();
        int x = sc.nextInt();
        int y = sc.nextInt();
        boolean state = sc.nextBoolean();
        Colour colour = Colour.valueOf(sc.next().toUpperCase());

        level.setItemAt(y, x, new Lever("Lever", id, x, y, state, colour));
    }

    /**
     * Loads a gate item from the file.
     * Reads ID, position, state, and gate color.
     *
     * @param sc    the Scanner reading the file
     * @param level the Level to add the gate to
     */
    private void loadGate(Scanner sc, Level level) {
        int id = sc.nextInt();
        int x = sc.nextInt();
        int y = sc.nextInt();
        boolean state = sc.nextBoolean();
        Colour colour = Colour.valueOf(sc.next().toUpperCase());

        level.setItemAt(y, x, new Gate("Gate", id, x, y, state, colour));
    }

    /**
     * Loads a bomb item from the file.
     * Reads ID, position, and state. Adds the bomb to both the items grid and active bombs list.
     *
     * @param sc    the Scanner reading the file
     * @param level the Level to add the bomb to
     */
    private void loadBomb(Scanner sc, Level level) {
        int id = sc.nextInt();
        int x = sc.nextInt();
        int y = sc.nextInt();
        boolean state = sc.nextBoolean();

        Bomb bomb = new Bomb("Bomb", id, x, y, state);
        level.setItemAt(y, x, bomb);
        level.getActiveBombs().add(bomb);

        Tile tile = level.getTile(y, x);
        if (tile != null) {
            tile.setItem(bomb);
        }

        System.out.println("Loaded bomb at (" + x + ", " + y + ")");
    }

    /**
     * Loads a door item from the file.
     * Reads ID, position, and state.
     *
     * @param sc    the Scanner reading the file
     * @param level the Level to add the door to
     */
    private void loadDoor(Scanner sc, Level level) {
        int id = sc.nextInt();
        int x = sc.nextInt();
        int y = sc.nextInt();
        boolean state = sc.nextBoolean();

        level.setItemAt(y, x, new Door("Door", id, x, y, state));
    }

    /**
     * Loads an exit tile from the file.
     * Reads position and adds the tile to the list of exit tiles.
     *
     * @param sc    the Scanner reading the file
     * @param level the Level to add the exit tile to
     */
    private void loadExit(Scanner sc, Level level) {
        int x = sc.nextInt();
        int y = sc.nextInt();
        level.getExitTiles().add(level.getTile(y, x));
    }

    /**
     * Handles unknown tokens found in the level file.
     * Skips comment lines (starting with #) or throws an exception for unrecognized tokens.
     *
     * @param token the unknown token string
     * @param sc    the Scanner reading the file
     * @throws IllegalArgumentException if the token is not a comment and is unrecognized
     */
    private void handleUnknownToken(String token, Scanner sc) {
        if (token.startsWith("#")) {
            sc.nextLine(); // Skip comment line
        } else {
            throw new IllegalArgumentException("Unknown token: " + token);
        }
    }
}