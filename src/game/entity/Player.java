package game.entity;

import game.GameController;
import game.item.*;
import game.level.Tile;
import game.level.Level;
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

    private int highscore;
    private GameController controller;
    private Level level;

    private final ImageView sprite = new ImageView(
            new Image(Player.class.getResource("/game/resources/player.png").toExternalForm()));

    private static final String PLAYER_PNG = "/game/resources/player.png";
    private static final int SPRITE_SIZE = 35;

    /**
     * Constructs a new Player entity.
     *
     * @param y              y coordinate of the player
     * @param x              x coordinate of the player
     * @param direction      direction the player is facing
     * @param alive          the alive state of the player
     * @param blocksMovement whether the player blocks movement of other entities
     * @param level          The level that the entity is on
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

        System.out.println("Player before move: (" + getX() + ", " + getY() + ")");
        Direction moveDirection = getDirection();

        Tile currentTile = level.getTile(getY(), getX());
        Tile targetTile = level.findNextValidTile(currentTile, moveDirection);

        if (targetTile == null) {
            return;
        } else {
            System.out.println("Target tile: (" + targetTile.getX() + ", " + targetTile.getY() + ")");
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
        System.out.println("Player after move: (" + getX() + ", " + getY() + ")");
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
     * Method to return the player image.
     * @return image of the player png
     */
    public ImageView getSprite() {
        return sprite;
    }

    /**
     * Adds value to Highscore
     * @param value value to be added to Highscore
     */
    @Override
    public void addToHighscore(int value) {
        this.highscore += value;
    }
}

