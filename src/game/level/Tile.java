package game.level;

import game.entity.Entity;
import game.entity.EntityName;
import game.entity.npc.NPC;
import game.item.Bomb;
import game.item.Gate;
import game.item.Item;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
 */

public class Tile {

    private static final int MAX_COLOURS = 4;

    private final int x;
    private final int y;

    private Color[] colours;

    private Item item;
    private Gate gate;
    private boolean isExit;
    private List<NPC> npc = new ArrayList<>();

    private static final int TILE_INNER_SQUARE_SIZE = 25;

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
    public void removeItem() {
        item = null;
    }

    public void setGate(Gate gate) {
        this.gate = gate;
    }

    /**
     * Method to convert a tile into a StackPane JavaFX object.
     * This allows for a tile to be displayed within a StackPane on the TileGrid.
     * @return a StackPane object for the GameController to read
     */
    public StackPane toStackPane() {
        StackPane root = new StackPane();
        GridPane gridPane = new GridPane();

        Rectangle r0 = createOutlinedRect(colours[0]);
        Rectangle r1 = createOutlinedRect(colours[1]);
        Rectangle r2 = createOutlinedRect(colours[2]);
        Rectangle r3 = createOutlinedRect(colours[3]);

        gridPane.add(r0, 0, 0);
        gridPane.add(r1, 1, 0);
        gridPane.add(r2, 0, 1);
        gridPane.add(r3, 1, 1);

        root.getChildren().add(gridPane);
        return root;
    }

    /**
     * Creates a rectangle with an outline stroke.
     * @param fill the fill color of the rectangle
     * @return a Rectangle with stroke applied
     */
    private Rectangle createOutlinedRect(Color fill) {
        Rectangle rect = new Rectangle(TILE_INNER_SQUARE_SIZE, TILE_INNER_SQUARE_SIZE, fill);

        rect.setStroke(new Color(0, 0, 0, 0.3));
        rect.setStrokeWidth(1);
        rect.setStrokeType(StrokeType.INSIDE);

        return rect;
    }


    @Override
    public String toString() {
        return "Tile{" +
                "x=" + x +
                ", y=" + y +
                ", colours=" + Arrays.toString(colours) +
                ", item=" + item +
                ", gate=" + gate +
                ", bomb=" + (item instanceof Bomb) +
                ", isExit=" + isExit +
                ", npc=" + npc +
                '}';
    }
}