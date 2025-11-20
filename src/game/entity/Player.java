package game.entity;

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

    private static final String ENTITY_NAME = "Player";

    /**
     * Constructs a new Player entity.
     *
     * @param entityID       unique ID of the player
     * @param y              y coordinate of the player
     * @param x              x coordinate of the player
     * @param direction      direction the player is facing
     * @param alive          the alive state of the player
     * @param blocksMovement whether the player blocks movement of other entities
     */
    public Player(int entityID, int y, int x, Direction direction, boolean alive, boolean blocksMovement) {
        super(entityID, ENTITY_NAME, y, x, direction, alive, blocksMovement);
    }

    /**
     * Attempts to move the player in their current direction, applying movement
     * rules, collecting items, triggering bombs, handling collisions, and checking
     * exit conditions.
     */
    @Override
    public void move() {
        /*
        Direction moveDirection = getDirection();

        Tile currentTile = Level.getTile(getY(), getX());
        Tile targetTile = Level.findNextValidTile(currentTile, moveDirection);

        if (targetTile == null) {
            return;
        }
        if (targetTile.hasGate()) {
            return;
        }


        if (targetTile.containsDangerousNpc()) {
            game.gameOver();
            return;
        }

        setX(targetTile.getX());
        setY(targetTile.getY());

        Item item = targetTile.getItem();
        if (item != null) {
            switch (item.getType()) {
                case LOOT:
                    score.add(item.getValue());
                    break;

                case CLOCK:
                    timer.add(item.getTimeBonus());
                    break;

                case LEVER:
                    level.openGatesOfColor(item.getColor());
                    break;

                case BOMB:
                    // Player cannot stand on a bomb tile,
                    // but if the tile next to it contains a bomb, trigger it.
                    break;
            }
            targetTile.removeItem();
        }

        // Bomb triggering logic
        for (Tile neighbour : level.getNeighbours(targetTile)) {
            if (neighbour.hasBomb()) {
                neighbour.getBomb().trigger();
            }
        }
        //Handles exit logic
        if (targetTile.isExitTile()) {
            if (level.allLootAndLeversCollected()) {
                game.finishLevel();
            }
        }

         */
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
        gc.drawImage(playerImage, 50, 60, 40, 40);
    }
}

