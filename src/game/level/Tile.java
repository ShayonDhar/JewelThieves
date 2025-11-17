package game.level;
import javafx.scene.paint.Color;

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
    private int xCoordinate;
    private int yCoordinate;
    private Color[] colours;
    /*
    Uncomment once item/npc/gate class has been created.
    private Item item;
    private NPC npcs;
    private Gate gate;
     */
    private boolean hasEntity;
    private boolean isExit;


}
