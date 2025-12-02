package game.level;

import game.entity.Entity;
import game.entity.EntityName;
import game.entity.npc.NPC;
import game.item.Bomb;
import game.item.Gate;
import game.item.Item;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 * Represents a single tile within the game level grid. Each tile has a fixed
 * position defined by its x and y coordinates, and contains four colours that
 * determine valid movement between tiles.
 * May contain at most one item (such as loot, a clock, a bomb, or a
 * lever). Tiles may also hold multiple NPCs at the same time,
 * and provide methods for adding or removing them.
 * May contain a gate, which blocks movement until opened via a
 * matching lever. Tiles may also be designated as exit tiles,
 * (used for completing the level).
 * This class also manages bomb behaviour.
 *
 * @author Alex Samuel, Shayon Dhar
 * @version 1.0
 */

public class Tile {

    private static final int MAX_COLOURS = 4;
    private static final int TILE_INNER_SQUARE_SIZE = 25;
    private static final int RECTANGLE_THREE_INDEX = 3;
    private static final double OPACITY = 0.3;

    private final int x;
    private final int y;

    private Color[] colours;

    private Item item;
    private Gate gate;
    private boolean isExit;
    private Bomb bomb;

    private List<NPC> npc = new ArrayList<>();

    /**
     * Tile constructor.
     *
     * @param x c-coordinate of the tile
     * @param y y-coordinate of the tile
     * @param colours the list of tile colours
     */

    public Tile(int x, int y, Color[] colours) {
        validateColourSize(colours.length);
        this.x = x;
        this.y = y;
        this.colours = colours;
    }

    private void validateColourSize(int size) {
        if (size > MAX_COLOURS) {
            throw new IllegalArgumentException("A tile can have at most " + MAX_COLOURS + " colours.");
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Checks whether a tile has a gate.
     *
     * @return whether the tile has a gate
     */

    public boolean hasGate() {
        return gate != null;
    }

    public boolean isExit() {
        return isExit;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public List<NPC> getNpc() {
        return npc;
    }

    /**
     * Checks whether a tile has a bomb.
     *
     * @return whether the tile has a bomb
     */
    public boolean hasBomb() {
        return bomb != null;
    }

    /**
     * Checks whether a tile contains a flying assassin.
     *
     * @return whether the tile contains the flying assassin or not
     */
    public boolean containsFlyingAssassin() {
        for (Entity npc : getNpc()) {
            if (npc.getEntityName() == EntityName.FLYING_ASSASSIN) {
                return true;
            }
        }
        return false;
    }

    public Color[] getColours() {
        return colours;
    }

    public Collection<Color> getColoursAsList() {
        return Arrays.asList(colours);
    }

    public void setColours(Color[] colours) {
        this.colours = colours;
    }

    public void setNpc(List<NPC> npc) {
        this.npc = npc;
    }

    public void setExit(boolean exit) {
        isExit = exit;
    }

    public Gate getGate() {
        return gate;
    }

    public Bomb getBomb() {
        return bomb;
    }

    public void setBomb(Bomb bomb) {
        this.bomb = bomb;
    }

    /**
     * Removes an item from the tile.
     */
    public void removeItem() {
        item = null;
    }

    public void setGate(Gate gate) {
        this.gate = gate;
    }

    /**
     * Method to convert a tile into a StackPane JavaFX object.
     * This allows for a tile to be displayed within a StackPane on the TileGrid.
     *
     * @return a StackPane object for the GameController to read
     */
    public StackPane toStackPane() {
        GridPane gridPane = new GridPane();

        Rectangle r0 = createOutlinedRect(colours[0]);
        Rectangle r1 = createOutlinedRect(colours[1]);
        Rectangle r2 = createOutlinedRect(colours[2]);
        Rectangle r3 = createOutlinedRect(colours[RECTANGLE_THREE_INDEX]);

        gridPane.add(r0, 0, 0);
        gridPane.add(r1, 1, 0);
        gridPane.add(r2, 0, 1);
        gridPane.add(r3, 1, 1);
        StackPane root = new StackPane();
        root.getChildren().add(gridPane);
        return root;
    }

    /**
     * Creates a rectangle with an outline stroke.
     *
     * @param fill the fill color of the rectangle
     * @return a Rectangle with stroke applied
     */
    private Rectangle createOutlinedRect(Color fill) {
        Rectangle rect = new Rectangle(TILE_INNER_SQUARE_SIZE, TILE_INNER_SQUARE_SIZE, fill);

        rect.setStroke(new Color(0, 0, 0, OPACITY));
        rect.setStrokeWidth(1);
        rect.setStrokeType(StrokeType.INSIDE);

        return rect;
    }

    @Override
    public String toString() {
        return "Tile{"
                + "x=" + x
                + ", y=" + y
                + ", colours=" + Arrays.toString(colours)
                + ", item=" + item
                + ", gate=" + gate
                + ", bomb=" + bomb
                + ", isExit=" + isExit
                + ", npc=" + npc
                + '}';
    }
}