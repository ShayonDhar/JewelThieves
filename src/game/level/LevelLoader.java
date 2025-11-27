package game.level;

import game.GameController;
import game.entity.Direction;
import game.entity.Entity;
import game.entity.Player;
import game.entity.npc.FloorFollowingThief;
import game.entity.npc.FlyingAssassin;
import game.entity.npc.SmartThief;
import game.item.*;
import javafx.scene.paint.Color;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class reads the data from the level file.
 * It then loads the data stored in this file
 * onto the game.
 * @author Alex Samuel.
 */

public class LevelLoader {

    private final GameController controller;

    public LevelLoader(GameController controller) {
        this.controller = controller;
    }

    public Level load(String filename) {
        Level level = new Level(controller);
        parseFile(filename, level);
        return level;
    }

    private void parseFile(String filename, Level level) {
        try (Scanner sc = new Scanner(new File(filename))) {

            level.setLevelWidth(sc.nextInt());
            level.setLevelHeight(sc.nextInt());
            sc.nextLine();  // move scanner to next line

            // Initialize grids
            level.setLevelGrid(new Tile[level.getLevelHeight()][level.getLevelWidth()]);
            level.setItemsGrid(new Item[level.getLevelHeight()][level.getLevelWidth()]);
            ArrayList<Entity> entities = new ArrayList<>();
            level.setEntities(entities);
            level.setActiveBombs(new ArrayList<>());
            level.setExitTiles(new ArrayList<>());

            // Read tile codes
            for (int y = 0; y < level.getLevelHeight(); y++) {
                for (int x = 0; x < level.getLevelWidth(); x++) {
                    String tileCode = sc.next();
                    Color[] colours = new Color[4];
                    for (int i = 0; i < 4; i++) {
                        Colour enumCol = Colour.fromChar(tileCode.charAt(i));
                        colours[i] = enumCol.getFXColor(); // convert to JavaFX Color
                    }
                    level.getLevelGrid()[y][x] = new Tile(x, y, colours);
                }
            }

            // Read remaining tokens
            while (sc.hasNext()) {
                String token = sc.next();

                switch (token.toUpperCase()) {

                    case "PLAYER" -> {
                        int x = sc.nextInt();
                        int y = sc.nextInt();
                        Direction dir = Direction.valueOf(sc.next().toUpperCase());
                        boolean alive = Boolean.parseBoolean(sc.next());
                        boolean blocks = Boolean.parseBoolean(sc.next());

                        Player player = new Player(y, x, dir, alive, blocks, controller, level);
                        entities.add(player);
                        level.setPlayer(player);
                    }

                    case "FLOORFOLLOWINGTHIEF" -> {
                        int x = sc.nextInt();
                        int y = sc.nextInt();
                        Direction dir = Direction.valueOf(sc.next().toUpperCase());
                        boolean alive = Boolean.parseBoolean(sc.next());
                        boolean blocks = Boolean.parseBoolean(sc.next());
                        Colour colour = Colour.valueOf(sc.next().toUpperCase());

                        entities.add(new FloorFollowingThief(x, y, dir, alive, blocks, colour));
                    }

                    case "ASSASSIN" -> {
                        int x = sc.nextInt();
                        int y = sc.nextInt();
                        Direction dir = Direction.valueOf(sc.next().toUpperCase());
                        boolean alive = Boolean.parseBoolean(sc.next());
                        boolean blocks = Boolean.parseBoolean(sc.next());

                        entities.add(new FlyingAssassin(x, y, dir, alive, blocks));
                    }

                    case "SMARTTHIEF" -> {
                        int x = sc.nextInt();
                        int y = sc.nextInt();
                        Direction dir = Direction.valueOf(sc.next().toUpperCase());
                        boolean alive = Boolean.parseBoolean(sc.next());
                        boolean blocks = Boolean.parseBoolean(sc.next());

                        entities.add(new SmartThief(x, y, dir, alive, blocks));
                    }

                    case "LOOT" -> {
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

                    case "CLOCK" -> {
                        int id = sc.nextInt();
                        int x = sc.nextInt();
                        int y = sc.nextInt();
                        boolean state = Boolean.parseBoolean(sc.next());
                        int mod = sc.nextInt();

                        level.setItemAt(y, x, new Clock("Clock", id, x, y, state, mod));
                    }

                    case "LEVER" -> {
                        int id = sc.nextInt();
                        int x = sc.nextInt();
                        int y = sc.nextInt();
                        boolean state = Boolean.parseBoolean(sc.next());
                        Colour colour = Colour.valueOf(sc.next().toUpperCase());

                        level.setItemAt(y, x, new Lever("Lever", id, x, y, state, colour));
                    }

                    case "GATE" -> {
                        int id = sc.nextInt();
                        int x = sc.nextInt();
                        int y = sc.nextInt();
                        boolean state = Boolean.parseBoolean(sc.next());
                        Colour colour = Colour.valueOf(sc.next().toUpperCase());

                        level.setItemAt(y, x, new Gate("Gate", id, x, y, state, colour));
                    }

                    case "BOMB" -> {
                        int id = sc.nextInt();
                        int x = sc.nextInt();
                        int y = sc.nextInt();
                        boolean state = Boolean.parseBoolean(sc.next());

                        level.setItemAt(y, x, new Bomb("Bomb", id, x, y, state));
                    }

                    case "EXIT" -> {
                        int x = sc.nextInt();
                        int y = sc.nextInt();
                        level.getExitTiles().add(level.getTile(y, x));
                    }

                    case "TIME" -> level.setRemainingTime(sc.nextInt());

                    default -> {
                        if (token.startsWith("#")) sc.nextLine(); // skip comment
                        else throw new IllegalArgumentException("Unknown token: " + token);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

