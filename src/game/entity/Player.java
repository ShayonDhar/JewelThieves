package game.entity;

import game.item.Item;
import game.item.Loot;
import game.item.LootType;
import game.level.Tile;
import game.level.Level;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Objects;

/**
 * Represents the player entity in the game.
 * The Player class extends the generic Entity class and provides
 * a concrete implementation for the player character. It sets the entity
 * name to "Player" and inherits all shared state such as position, direction,
 * and movement blocking.
 *
 * @author Shayon Dhar
 * @version 1.0.0
 */
public class Player extends Entity {

    private final Image playerImage = new Image(Objects.requireNonNull(getClass().getResource(
            "/game/resources/player.png")).toExternalForm());
    private int highscore;

    /**
     * Constructs a new Player entity.
     *
     * @param y              y coordinate of the player
     * @param x              x coordinate of the player
     * @param direction      direction the player is facing
     * @param alive          the alive state of the player
     * @param blocksMovement whether the player blocks movement of other entities
     */
    public Player(int y, int x, Direction direction, boolean alive, boolean blocksMovement) {
        super(EntityName.PLAYER, x, y, direction, alive, blocksMovement);
    }

    /**
     * Attempts to move the player in their current direction, applying movement
     * rules, collecting items, triggering bombs, handling collisions, and checking
     * exit conditions.
     */
    @Override
    public void move() {
        Direction dir = getDirection();
        int newX = getX();
        int newY = getY();

        switch (getDirection()) {
            case NORTH -> newY += 1;  // up
            case SOUTH -> newY -= 1;  // down
            case WEST  -> newX -= 1;  // left
            case EAST  -> newX += 1;  // right
        }

        Tile currentTile = Level.getTile(getY(), getX());
        Tile targetTile = Level.getTile(newY, newX);

        if (targetTile == null) return;          // Off map
        if (targetTile.hasGate()) return;        // Closed gate
        if (targetTile.containsFlyingAssassin()) {
            game.GameController.gameOver();
            return;
        }

        // Move player
        setX(newX);
        setY(newY);

//        // Handle items
//        Item item = targetTile.getItem();
//        if (item != null) {
//            switch (item.getItemType()) {
//                case LOOT -> {
//                    Loot loot = (Loot) item;
//                    addToHighscore(loot.getLootValue());
//                }
//                case CLOCK -> timer.add(item.getTimeBonus());
//                case LEVER -> level.openGatesOfColor(item.getColor());
//                case BOMB -> { /* bombs handled after */ }
//            }
//            targetTile.removeItem();
//        }
//
//
//
//        // Trigger bombs around player
//        for (Tile neighbour : level.getNeighbours(targetTile)) {
//            if (neighbour.hasBomb()) {
//                neighbour.getBomb().trigger();
//            }
//        }
//
//        // Exit tile logic
//        if (targetTile.isExitTile() && level.allLootAndLeversCollected()) {
//            game.finishLevel();
//        }
//
    }

    public int getHighscore() {
        return highscore;
    }

    public void setHighscore(int highscore) {
        this.highscore = highscore;
    }

    /**
     * Renders the entity onto the JavaFX application.
     *
     * @param gc The graphics context used within the JavaFX application
     * @author Antoni Wachowiak
     */
    @Override
    public void draw(GraphicsContext gc) {

        // Drawing the level background
        gc.drawImage(playerImage, getX(), getY(), 40, 40);
    }

    @Override
    public void addToHighscore(int value) {
        this.highscore += value;
    }
}

