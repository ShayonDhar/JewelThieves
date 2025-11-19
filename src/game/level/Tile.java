package game.level;

import game.entity.NPC;
import game.item.Gate;
import game.item.Item;
import javafx.scene.paint.Color;
import java.util.ArrayList;
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
 * @author Alex Samuel
 */

public class Tile {

    private final int x;
    private final int y;

    private Color[] colours;

    private Item item;
    private Gate gate;
    private boolean isExit;

    private List<NPC> npcs = new ArrayList<>();

    public Tile(int x, int y, Color[] colours) {
        this.x = x;
        this.y = y;
        this.colours = colours;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public boolean hasGate() { return gate != null; }
    public boolean isExit() { return isExit; }

    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }

    public List<NPC> getNpcs() { return npcs; }
}