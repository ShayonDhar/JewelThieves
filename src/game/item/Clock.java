package game.item;

import game.entity.Entity;
import game.entity.EntityName;
import game.level.Level;
import javafx.scene.canvas.GraphicsContext;

public class Clock extends Item{
    private Level level;
    private int addedTime;

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
     */
    public Clock(String itemName, int itemID, int x, int y, boolean isOn) {
        super(itemName, itemID, x, y, isOn);
        this.level = level;
        this.addedTime = addedTime;
    }

    @Override
    public void draw(GraphicsContext gc) {

    }

    /**
     * If the player collects a clock it will add the additional time provided
     * by the clock to the time remaining for the player to complete the level.
     * If a smart thief or floor following thief collects a clock it will
     * deduct the same value as the additional time provided by the clock to
     * the time remaining to complete the level
     * @param entityName the name of the entity, e.g. PLAYER, SMART_THIEF
     */
    @Override
    public void collectItem(Entity entityName) {
        if (!isOn) {
            return;
        }

        if (entityName.getX() == x && entityName.getY() == y) {
            if (entityName.getEntityName().equals("PLAYER")) {
                level.updateLevel(addedTime);
            }


            if (entityName.getEntityName().equals("SMART_THIEF") || entityName.getEntityName().equals("FLOOR_FOLLOWING_THIEF")) {
                level.updateLevel(addedTime * -1);
            }

        }

        isOn = false;
    }

    public void setAddedTime(int addedTime) {
        this.addedTime = addedTime;
    }

    public int getAddedTime(int addedTime) {
        return addedTime;
    }
}
