package game.item;

import game.entity.Entity;
import game.level.Colour;
import game.level.Level;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Lever class represents a lever item that can trigger gates and doors.
 * When activated, it affects all gates/doors of the same color.
 */
public class Lever extends Item {

    private Colour colour;
    private boolean isActivated;
    private Level level;
    private List<Gate> connectedGates;
    private List<Door> connectedDoors;

    /**
     * Constructor for Lever.
     *
     * @param itemName the name of the item
     * @param itemID   the ID of the item
     * @param x        x coordinate on the map
     * @param y        y coordinate on the map
     * @param isOn     whether the item is active
     */
    public Lever(String itemName, int itemID, int x, int y, boolean isOn) {
        super(itemName, itemID, x, y, isOn);
        this.isActivated = false;
        this.connectedGates = new ArrayList<>();
        this.connectedDoors = new ArrayList<>();
    }

    /**
     * Constructor for Lever with color.
     *
     * @param itemName the name of the item
     * @param itemID   the ID of the item
     * @param x        x coordinate on the map
     * @param y        y coordinate on the map
     * @param isOn     whether the item is active
     * @param colour   the color of the lever
     */
    public Lever(String itemName, int itemID, int x, int y, boolean isOn, Colour colour) {
        super(itemName, itemID, x, y, isOn);
        this.colour = colour;
        this.isActivated = false;
        this.connectedGates = new ArrayList<>();
        this.connectedDoors = new ArrayList<>();
    }

    /**
     * Constructor for Lever with color and level.
     *
     * @param itemName the name of the item
     * @param itemID   the ID of the item
     * @param x        x coordinate on the map
     * @param y        y coordinate on the map
     * @param isOn     whether the item is active
     * @param colour   the color of the lever
     * @param level    the level reference
     */
    public Lever(String itemName, int itemID, int x, int y, boolean isOn, Colour colour, Level level) {
        super(itemName, itemID, x, y, isOn);
        this.colour = colour;
        this.isActivated = false;
        this.level = level;
        this.connectedGates = new ArrayList<>();
        this.connectedDoors = new ArrayList<>();
    }

    @Override
    public void draw(GraphicsContext gc) {
        // TODO: Implement drawing logic
    }

    /**
     * @param entity the entity collecting the item
     */
    @Override
    public void collectItem(Entity entity) {
        if (!isOn) {
            return;
        }

        if (entity.getX() == x && entity.getY() == y) {
            trigger();
        }
    }

    /**
     * Triggers the lever, toggling its state.
     */
    public void trigger() {
        isActivated = !isActivated;

        if (isActivated) {
            for (Gate gate : connectedGates) {
                if (gate.getColour() == this.colour) {
                    gate.unlock();
                }
            }

            for (Door door : connectedDoors) {
                if (door.getColour() == this.colour) {
                    door.open();
                }
            }
        } else {
            for (Gate gate : connectedGates) {
                if (gate.getColour() == this.colour) {
                    gate.lock();
                }
            }

            for (Door door : connectedDoors) {
                if (door.getColour() == this.colour) {
                    door.close();
                }
            }
        }
    }

    /**
     * @param gate the gate to connect
     */
    public void addConnectedGate(Gate gate) {
        if (gate != null && !connectedGates.contains(gate)) {
            connectedGates.add(gate);
        }
    }

    /**
     * @param door the door to connect
     */
    public void addConnectedDoor(Door door) {
        if (door != null && !connectedDoors.contains(door)) {
            connectedDoors.add(door);
        }
    }

    /**
     * @param gate the gate to disconnect
     */
    public void removeConnectedGate(Gate gate) {
        connectedGates.remove(gate);
    }

    /**
     * @param door the door to disconnect
     */
    public void removeConnectedDoor(Door door) {
        connectedDoors.remove(door);
    }

    /**
     * @return list of connected gates
     */
    public List<Gate> getConnectedGates() {
        return new ArrayList<>(connectedGates);
    }

    /**
     * @return list of connected doors
     */
    public List<Door> getConnectedDoors() {
        return new ArrayList<>(connectedDoors);
    }

    /**
     * @return true if activated, false otherwise
     */
    public boolean isActivated() {
        return isActivated;
    }

    /**
     * @param activated the activation state
     */
    public void setActivated(boolean activated) {
        this.isActivated = activated;
    }

    /**
     * @return the colour of the lever
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * @param colour the colour for the lever
     */
    public void setColour(Colour colour) {
        this.colour = colour;
    }

    /**
     * @param level the level object
     */
    public void setLevel(Level level) {
        this.level = level;
    }
}