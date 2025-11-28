package game.item;

import game.entity.Entity;
import game.level.Level;
import javafx.scene.canvas.GraphicsContext;
import game.level.Colour;

/**
 * Gate class represents a gate item that blocks movement.
 * Gates can be unlocked by levers of the same color.
 */
public class Gate extends Item {

    private Colour colour;
    private boolean isLocked;

    /**
     * Constructor for Gate.
     *
     * @param itemName the name of the item
     * @param itemID   the ID of the item
     * @param x        x coordinate on the map
     * @param y        y coordinate on the map
     * @param isOn     whether the item is active
     */
    public Gate(String itemName, int itemID, int x, int y, boolean isOn) {
        super(itemName, itemID, x, y, isOn);
        this.isLocked = true;
    }

    /**
     * Constructor for Gate with color.
     *
     * @param itemName the name of the item
     * @param itemID   the ID of the item
     * @param x        x coordinate on the map
     * @param y        y coordinate on the map
     * @param isOn     whether the item is active
     * @param colour   the color of the gate
     */
    public Gate(String itemName, int itemID, int x, int y, boolean isOn, Colour colour) {
        super(itemName, itemID, x, y, isOn);
        this.colour = colour;
        this.isLocked = true;
    }

    @Override
    public void draw(GraphicsContext gc) {
        // TODO: Implement drawing logic
    }

    @Override
    public void collectItem(Entity entityName, Level level) {

    }

    /**
     * Unlocks the gate.
     */
    public void unlock() {
        this.isLocked = false;
        this.isOn = false;
    }

    /**
     * Locks the gate.
     */
    public void lock() {
        this.isLocked = true;
        this.isOn = true;
    }

    /**
     * @return true if locked, false otherwise
     */
    public boolean isLocked() {
        return isLocked;
    }

    /**
     * @return the colour of the gate
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * @param colour the colour for the gate
     */
    public void setColour(Colour colour) {
        this.colour = colour;
    }

    /**
     * @return true if entities can pass through, false otherwise
     */
    public boolean canPassThrough() {
        return !isLocked;
    }
}