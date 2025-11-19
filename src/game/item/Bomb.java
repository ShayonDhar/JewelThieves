package game.item;

public class Bomb extends Item {
    private static final int BOMB_COUNTDOWN = 3;
    private BombState state;
    private int countdown = BOMB_COUNTDOWN;
    /**
     * Constructor that all the items will use.
     * It has all the properties they have in common
     * But each item will have different String, int
     * or boolean for each attribute.
     *
     * @param itemName is the name of the item, e.g. Gate
     * @param itemID   is the ID of the item,
     *                 e.g. if there were 5 bombs, one ID would be 4
     * @param x        is the x coordinate of the location of the item on the map
     * @param y        is the y coordinate of the location of the item on the map
     * @param isOn     is a boolean that will either be true or false. It tells us
     *                 whether the item has been claimed or triggered.
     */
    public Bomb(String itemName, int itemID, int x, int y, boolean isOn) {
        super(itemName, itemID, x, y, isOn);
        this.state = BombState.WAITING;
    }
    public BombState getState() {
        return state;
    }
    public void setState(BombState state) {
        this.state = state;
    }
    public void updateBombState() {
        switch (state) {
            case WAITING:
                break;
            case COUNTING:
                countdown--;
                if (countdown <= 0) {
                    state = BombState.EXPLODED;
                }
                break;
                case EXPLODED:
                    break;
        }


    }
    public void trigger() {
        if (state == BombState.WAITING) {
            state = BombState.COUNTING;
        }
    }

}
