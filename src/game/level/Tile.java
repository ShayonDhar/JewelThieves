package game.level;

import game.entity.npc.NPC;
import game.item.Gate;
import game.item.Item;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Arrays;
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

    private Colour[] colours;

    private Item item;
    private Gate gate;
    private boolean isExit;

    private List<NPC> npc = new ArrayList<>();

    public Tile(int x, int y, Colour[] colours) {
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

    public Color[] getColours() {
        return colours;
    }

    public void setColours(Colour[] colours) {
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

    public void setGate(Gate gate) {
        this.gate = gate;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "x=" + x +
                ", y=" + y +
                ", colours=" + Arrays.toString(colours) +
                ", item=" + item +
                ", gate=" + gate +
                ", isExit=" + isExit +
                ", npc=" + npc +
                '}';
    }
}