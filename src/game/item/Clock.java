package game.item;

import game.entity.Entity;
import game.entity.EntityName;
import game.level.Level;
import javafx.scene.canvas.GraphicsContext;

/**
 * Clock class which allows the clock object to created
 * It implements all the clock functions.
 *
 * @author Alex Samuel
 * @version 1.o
 */

public class Clock extends Item {
    private final int timeBonus;

    /**
     * Constructor that all the items will use.
     * It has all the properties they have in common
     * But each item will have different String, int or boolean for each attribute
     *
     * @param itemName is the name of the item, e.g. Gate
     * @param itemID   is the ID of the item, e.g. if there were 5 bombs, one ID would be 4
     * @param x        is the x coordinate of the location of the item on the map
     * @param y        is the y coordinate of the location of the item on the map
     * @param isOn     is a boolean that will either be true or false. It tells us
     *                 whether the item has been claimed or triggered.
     * @param timeBonus The time to be added to the level after clock has been collected
     */
    public Clock(String itemName, int itemID, int x, int y, boolean isOn, int timeBonus) {
        super(itemName, itemID, x, y, isOn, ItemType.CLOCK);
        this.timeBonus = timeBonus;
    }

    public int getTimeBonus() {
        return timeBonus;
    }

    @Override
    public void draw(GraphicsContext gc) {

    }

    /**
     * When a player collects a clock, the value of the time on the clock.
     * will be added to the time remaining for the player to complete the level
     *
     * @param entityName Name of the entity
     * @param level The value of the level that the entity is on
     */
    @Override
    public void collectItem(Entity entityName, Level level) {

        if (!isOn) {
            return;
        }

        if (entityName.getEntityName().equals(EntityName.PLAYER)) {
            if (entityName.getX() == x && entityName.getY() == y) {
                entityName.getLevel().update(timeBonus);
            }
        }
    }
}
