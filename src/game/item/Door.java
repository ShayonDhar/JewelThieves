package game.item;

import game.entity.Entity;
import game.level.Level;
import javafx.scene.canvas.GraphicsContext;
import game.level.Colour;

/**
 * Door class represents a door item that can be opened and closed.
 * Doors are controlled by levers of the same color.
 */
public class Door extends Item {

    private Colour colour;
    private boolean isOpen;

    /**
     * Constructor for Door.
     *
     * @param itemName the name of the item
     * @param itemID   the ID of the item
     * @param x        x coordinate on the map
     * @param y        y coordinate on the map
     * @param isOn     whether the item is active
     */
    public Door(String itemName, int itemID, int x, int y, boolean isOn) {
        super(itemName, itemID, x, y, isOn);
        this.isOpen = false;
    }

    /**
     * Constructor for Door with color.
     *
     * @param itemName the name of the item
     * @param itemID   the ID of the item
     * @param x        x coordinate on the map
     * @param y        y coordinate on the map
     * @param isOn     whether the item is active
     * @param colour   the color of the door
     */
    public Door(String itemName, int itemID, int x, int y, boolean isOn, Colour colour) {
        super(itemName, itemID, x, y, isOn);
        this.colour = colour;
        this.isOpen = false;
    }

    @Override
    public void draw(GraphicsContext gc) {
        // TODO: Implement drawing logic
    }

    @Override
    public void collectItem(Entity entityName, Level level) {

    }

    /**
     * Opens the door.
     */
    public void open() {
        this.isOpen = true;
        this.isOn = false;
    }

    /**
     * Closes the door.
     */
    public void close() {
        this.isOpen = false;
        this.isOn = true;
    }

    /**
     * Toggles the door state.
     */
    public void toggle() {
        if (isOpen) {
            close();
        } else {
            open();
        }
    }

    /**
     * @return true if open, false otherwise
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * @param open the open state
     */
    public void setOpen(boolean open) {
        this.isOpen = open;
        this.isOn = !open;
    }

    /**
     * @return the colour of the door
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * @param colour the colour for the door
     */
    public void setColour(Colour colour) {
        this.colour = colour;
    }

    /**
     * @return true if entities can pass through, false otherwise
     */
    public boolean canPassThrough() {
        return isOpen;
    }
}