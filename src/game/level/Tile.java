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
 * Represents a single tile within the game level grid.
 * Each tile has a fixed position defined by its x and y coordinates, and contains
 * four colours that determine valid movement between tiles.
 * May contain at most one item such as loot, a clock, a bomb, or a lever.
 * Tiles may also hold multiple NPCs at the same time.
 * May contain a gate which blocks movement until opened via a matching lever.
 * Tiles may also be designated as exit tiles used for completing the level.
 *
 * @author Alex Samuel
 * @author Shayon Dhar
 * @version 1.0.0
 */
public class Tile {

    private static final int TILE_INNER_SQUARE_SIZE = 25;
    private static final int MAX_COLOURS = 4;
    public static final int COLOUR_3 = 3;
    public static final double V_3 = 0.3;

    private final int x;
    private final int y;

    private Color[] colours;

    private Item item;
    private Gate gate;
    private boolean isExit;
    private Bomb bomb;

    private List<NPC> npc = new ArrayList<>();

    /**
     * Constructs a new Tile with the specified position and colours.
     *
     * @param x the x-coordinate of the tile
     * @param y the y-coordinate of the tile
     * @param colours an array of four colours for the tile
     */
    public Tile(int x, int y, Color[] colours) {
        validateColourSize(colours.length);
        this.x = x;
        this.y = y;
        this.colours = colours;
    }

    /**
     * Validates that the colour array size does not exceed the maximum allowed.
     *
     * @param size the size of the colour array to validate
     */
    private void validateColourSize(int size) {
        if (size > MAX_COLOURS) {
            throw new IllegalArgumentException("A tile can have at most " + MAX_COLOURS + " colours.");
        }
    }

    /**
     * Gets the x-coordinate of this tile.
     *
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of this tile.
     *
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Checks if this tile has a gate.
     *
     * @return true if the tile has a gate, false otherwise
     */
    public boolean hasGate() {
        return gate != null;
    }

    /**
     * Checks if this tile is an exit tile.
     *
     * @return true if this tile is an exit, false otherwise
     */
    public boolean isExit() {
        return isExit;
    }

    /**
     * Gets the item on this tile.
     *
     * @return the item on this tile, or null if no item exists
     */
    public Item getItem() {
        return item;
    }

    /**
     * Sets the item on this tile.
     *
     * @param item the item to place on this tile
     */
    public void setItem(Item item) {
        this.item = item;
    }

    /**
     * Gets the list of NPCs on this tile.
     *
     * @return the list of NPCs
     */
    public List<NPC> getNpc() {
        return npc;
    }

    /**
     * Checks whether this tile has a bomb.
     *
     * @return true if the tile has a bomb, false otherwise
     */
    public boolean hasBomb() {
        return bomb != null;
    }

    /**
     * Checks whether this tile contains a flying assassin.
     *
     * @return true if the tile contains a flying assassin, false otherwise
     */
    public boolean containsFlyingAssassin() {
        for (Entity npc : getNpc()) {
            if (npc.getEntityName() == EntityName.FLYING_ASSASSIN) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the array of colours for this tile.
     *
     * @return the array of colours
     */
    public Color[] getColours() {
        return colours;
    }

    /**
     * Gets the colours as a collection.
     *
     * @return a collection of colours
     */
    public Collection<Color> getColoursAsList() {
        return Arrays.asList(colours);
    }

    /**
     * Sets the colours for this tile.
     *
     * @param colours the new array of colours
     */
    public void setColours(Color[] colours) {
        this.colours = colours;
    }

    /**
     * Sets the list of NPCs on this tile.
     *
     * @param npc the list of NPCs
     */
    public void setNpc(List<NPC> npc) {
        this.npc = npc;
    }

    /**
     * Sets whether this tile is an exit tile.
     *
     * @param exit true if this tile is an exit, false otherwise
     */
    public void setExit(boolean exit) {
        isExit = exit;
    }

    /**
     * Gets the gate on this tile.
     *
     * @return the gate, or null if no gate exists
     */
    public Gate getGate() {
        return gate;
    }

    /**
     * Gets the bomb on this tile.
     *
     * @return the bomb, or null if no bomb exists
     */
    public Bomb getBomb() {
        return bomb;
    }

    /**
     * Sets the bomb on this tile.
     *
     * @param bomb the bomb to place on this tile
     */
    public void setBomb(Bomb bomb) {
        this.bomb = bomb;
    }

    /**
     * Removes the item from this tile.
     */
    public void removeItem() {
        item = null;
    }

    /**
     * Sets the gate on this tile.
     *
     * @param gate the gate to place on this tile
     */
    public void setGate(Gate gate) {
        this.gate = gate;
    }

    /**
     * Converts this tile into a StackPane JavaFX object for display.
     *
     * @return a StackPane representation of this tile
     */
    public StackPane toStackPane() {
        StackPane root = new StackPane();
        GridPane gridPane = new GridPane();

        Rectangle r0 = createOutlinedRect(colours[0]);
        Rectangle r1 = createOutlinedRect(colours[1]);
        Rectangle r2 = createOutlinedRect(colours[2]);
        Rectangle r3 = createOutlinedRect(colours[COLOUR_3]);

        gridPane.add(r0, 0, 0);
        gridPane.add(r1, 1, 0);
        gridPane.add(r2, 0, 1);
        gridPane.add(r3, 1, 1);

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

        rect.setStroke(new Color(0, 0, 0, V_3));
        rect.setStrokeWidth(1);
        rect.setStrokeType(StrokeType.INSIDE);

        return rect;
    }

    /**
     * Returns a string representation of this tile.
     *
     * @return a string describing this tile's properties
     */
    @Override
    public String toString() {
        return "Tile{"
                + "x=" + x
                + ", y=" + y
                + ", colours=" + Arrays.toString(colours)
                + ", item=" + item
                + ", gate=" + gate
                + ", bomb=" + (item instanceof Bomb)
                + ", isExit=" + isExit
                + ", npc=" + npc
                + '}';
    }
}