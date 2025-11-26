package game.entity;

import game.GameController;
import game.item.*;
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

    private static final String PLAYER_PNG = "/game/resources/player.png";
    private final Image playerImage = new Image(Objects.requireNonNull(getClass().getResource(
            PLAYER_PNG)).toExternalForm());
    private int highscore;
    private GameController controller;
    private Level level;


    /**
     * Constructs a new Player entity.
     *
     * @param y              y coordinate of the player
     * @param x              x coordinate of the player
     * @param direction      direction the player is facing
     * @param alive          the alive state of the player
     * @param blocksMovement whether the player blocks movement of other entities
     */
    public Player(int y, int x, Direction direction, boolean alive,
                  boolean blocksMovement, GameController controller, Level level) {
        super(EntityName.PLAYER, x, y, direction, alive, blocksMovement);
        this.controller = controller;
        this.level = level;
    }


    /**
     * Attempts to move the player in their current direction, applying movement
     * rules, collecting items, triggering bombs, handling collisions, and checking
     * exit conditions.
     */
    @Override
    public void move() {

        Direction moveDirection = getDirection();

        Tile currentTile = level.getTile(getY(), getX());
        Tile targetTile = level.findNextValidTile(currentTile, moveDirection);

        if (targetTile == null) {
            return;
        }

        if (targetTile.hasGate()) {
            return;
        }

        if (targetTile.containsFlyingAssassin()) {
            game.GameController.gameOver();
            return;
        }

        setX(targetTile.getX());
        setY(targetTile.getY());

        updateScore(targetTile.getItem(), targetTile);

        // Bomb triggering logic
        for (Tile neighbour : level.getNeighbourTiles(targetTile)) {
            if (neighbour.hasBomb()) {
                neighbour.getBomb().trigger();
            }
        }
        //Handles exit logic
        if (targetTile.isExit() && level.allLootAndLeversCollected()) {
                controller.finishLevel();
        }
    }

    private void updateScore(Item item, Tile targetTile) {
        if (item != null) {
            switch (item.getItemType()) {
                case LOOT:
                    Loot loot = (Loot) item;
                    controller.addScore(loot.getLootType().getValue());
                    break;

                case CLOCK:
                    Clock clock = (Clock) item;
                    level.update(clock.getTimeBonus());
                    break;

                case LEVER:
                    Lever lever = (Lever) item;
                    level.openGatesOfColour(lever.getColour());
                    break;

                case BOMB:
                    // Player cannot stand on a bomb tile.
                    break;
            }
            targetTile.removeItem();
        }
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

