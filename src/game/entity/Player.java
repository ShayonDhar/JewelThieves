package game.entity;

import game.GameController;
import game.item.*;
import game.level.Level;
import game.level.Tile;
import java.util.Objects;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
    private static final int SPRITE_SIZE = 35;
    private static final int ROTATE_TO_EAST = 90;
    private static final int ROTATE_TO_SOUTH = 180;
    private static final int ROTATE_TO_WEST = 270;
    private final GameController controller;
    private final Level level;

    private final ImageView sprite = new ImageView(
            new Image(Objects.requireNonNull(Player.class.getResource(
                    "/game/resources/player.png")).toExternalForm()));

    /**
     * Constructs a new Player entity.
     *
     * @param y              y coordinate of the player
     * @param x              x coordinate of the player
     * @param direction      direction the player is facing
     * @param alive          the alive state of the player
     * @param blocksMovement whether the player blocks movement of other entities
     * @param level          The level that the entity is on
     * @param controller Links the player to the game controller
     */
    public Player(int y, int x, Direction direction, boolean alive,
                  boolean blocksMovement, GameController controller, Level level) {
        super(EntityName.PLAYER, x, y, direction, alive, blocksMovement, level);
        this.controller = controller;
        this.level = level;
        sprite.setFitWidth(SPRITE_SIZE);
        sprite.setFitHeight(SPRITE_SIZE);
    }


    /**
     * Attempts to move the player in their current direction, applying movement
     * rules, collecting items, triggering bombs, handling collisions, and checking
     * exit conditions.
     */
    @Override
    public void move() {
        Direction moveDirection = getDirection();

        // Locating current and target tiles
        Tile currentTile = level.getTile(getY(), getX());
        Tile targetTile = level.findNextValidTile(currentTile, moveDirection);

        // Different situations depending on what the target tile is
        if (targetTile == null) {
            return;
        }

        // Checking if gate is on the target tile
        Item[][] items = level.getItemsGrid();
        for (int y = 0; y < items.length; y++) {
            for (int x = 0; x < items[y].length; x++) {
                if (items[y][x] instanceof Gate && targetTile.getY() == y && targetTile.getX() == x) {
                    return;
                }
            }
        }

        // If flying assassin on the tile
        if (targetTile.containsFlyingAssassin()) {
            game.GameController.gameOver();
            return;
        }

        checkAdjacentBombs(currentTile);

        setX(targetTile.getX());
        setY(targetTile.getY());

        checkAdjacentBombs(targetTile);

        updateScore(targetTile.getItem(), targetTile);


        // Handles exit logic
        if (targetTile.isExit() && level.allLootAndLeversCollected()) {
            controller.finishLevel();
        }
    }

    private void checkAdjacentBombs(Tile tile) {

        if (tile == null) {
            return;
        }


        for (Tile neighbour : level.getNeighbourTiles(tile)) {
            checkBombAtTile(neighbour);
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
                default:
                    System.out.println("Game over");
                    break;
            }
            targetTile.removeItem();
        }
    }

    private void checkBombAtTile(Tile tile) {
        if (tile == null) {
            return;
        }

        Item item = level.getItemAt(tile.getY(), tile.getX());

        if (item instanceof Bomb bomb) {
            if (bomb.getState() == BombState.WAITING) {
                System.out.println("    Triggering bomb!");
                bomb.trigger();
            }
        }
    }

    /**
     * Method to return the player image.
     * Rotation is based on the direction they are facing.
     *
     * @return image of the player png
     */
    public ImageView getSprite() {

        // Get direction that player sprite is facing
        Direction facingDirection = getDirection();

        // Reset imageview orientation
        sprite.setRotate(0);

        // Rotate based on new direction
        switch (facingDirection) {
            case EAST:
                sprite.rotateProperty().set(ROTATE_TO_EAST);
                break;
            case SOUTH:
                sprite.rotateProperty().set(ROTATE_TO_SOUTH);
                break;
            case WEST:
                sprite.rotateProperty().set(ROTATE_TO_WEST);
                break;
            default:
                break;
        }
        return sprite;
    }
}

