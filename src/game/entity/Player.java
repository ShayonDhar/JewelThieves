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

    private final Image playerImage = new Image(Objects.requireNonNull(getClass().getResource(
            "/game/resources/player.png")).toExternalForm());
    private static final EntityName ENTITY_NAME = EntityName.PLAYER;
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
        super(ENTITY_NAME, x, y, direction, alive, blocksMovement);
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

        Tile currentTile = Level.getTile(getY(), getX());
        Tile targetTile = Level.findNextValidTile(currentTile, moveDirection);

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

        Item item = targetTile.getItem();
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

        // Bomb triggering logic
        for (Tile neighbour : level.getNeighbourTiles(targetTile)) {
            if (neighbour.hasBomb()) {
                neighbour.getBomb().trigger();
            }
        }
        //Handles exit logic
        if (targetTile.isExit()) {
            if (level.allLootAndLeversCollected()) {
                controller.finishLevel();
            }
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

