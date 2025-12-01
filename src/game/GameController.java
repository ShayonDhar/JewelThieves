package game;

import game.entity.Direction;
import game.entity.Entity;
import game.entity.Player;
import game.entity.npc.NPC;
import game.item.Item;
import game.item.Loot;
import game.level.Level;
import game.level.LevelLoader;
import game.level.Tile;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.util.Duration;

/**
 * Class that links the MainApplication to the SceneBuilder FXML controlling aspect.
 *
 * @author Antoni Wachowiak
 * @version $1.0.
 */
public class GameController {

    private static final String UNHANDLED_KEY = "Unhandled key: ";
    private static final int TICK_DURATION = 1000;

    // Timeline which will cause tick method to be called periodically.
    private static Timeline tickTimeline;

    public TilePane boardTilePane;
    public Level level;
    public Player player;
    public Item [][] itemGrid;
    public boolean tickPlaying = false;

    private int score = 0;

    /**
     * Method that initialises the game.
     */
    @FXML
    public void initialize() {

        // New timeline with one keyframe that triggers the tick method every half a second.
        tickTimeline = new Timeline(new KeyFrame(
                Duration.millis(TICK_DURATION), event -> tick()));
        tickTimeline.setCycleCount(Animation.INDEFINITE); // Loop indefinitely

        // Drawing the game
        LevelLoader loader = new LevelLoader(this);
        level = loader.load("LevelOne.txt");
        player = level.getPlayer();
        itemGrid = level.getItemsGrid();

        drawGame();
    }

    /**
     * Updates the game state and redraws the scene.
     * Updates periodically to update the entity positions, the state of items, and the game time
     */
    public void tick() {

        // Tick NPCs, bombs + time (Just NPCs for now)
        level.updateLevel(1);

        // Check for loot collection
        Item item = level.getItemAt(player.getX(), player.getY());
        if (item instanceof Loot loot) {
            addScore(loot.getLootType().getValue());
            level.removeItemFromGrid(player.getX(), player.getY());
        }

        // Redraw the whole canvas
        drawGame();
    }

    /**
     * Method to draw the game onto the FXML GUI.
     */
    public void drawGame() {

        // Clear the tilePane
        boardTilePane.getChildren().clear();

        // 2D array that stores the tiles
        StackPane[][] tiles = new StackPane[level.getLevelWidth()][level.getLevelHeight()];

        // Looping through height/width of tilePane
        for (int y = 0; y < level.getLevelHeight(); y++) {
            for (int x = 0; x < level.getLevelWidth(); x++) {

                // Gets the tile object from the level, and converts the colours/item/entity to a StackPane
                Tile tile = level.getTile(y, x);
                StackPane tileStack = tile.toStackPane();
                tiles[x][y] = tileStack;

                Item item = itemGrid[y][x];
                if (item != null && item.getSprite() != null) {
                    tileStack.getChildren().add(item.getSprite());
                }

                // Displaying the tile pane
                boardTilePane.getChildren().add(tileStack);
            }
        }

        // Draw NPCs at their current tiles
        for (Entity entities : level.getEntities()) {
            if (entities instanceof NPC npc) {
                int entityX = entities.getX();
                int entityY = entities.getY();
                tiles[entityX][entityY].getChildren().add(npc.getSprite());
            }
        }

        // Displaying the player at their current tile
        tiles[player.getX()][player.getY()].getChildren().add(player.getSprite());

    }

    /**
     * Method to start the tick timeline.
     */
    @FXML
    public void buttonStartAction() {
        tickTimeline.play();
        tickPlaying = true;
    }

    /**
     * Method to stop the tick timeline when the STOP button is pressed.
     */
    @FXML
    public void buttonStopAction() {
        tickTimeline.stop();
        tickPlaying = false;
    }

    /**
     * Method to read keyboard input.
     *
     * @param event key that is pressed on the keyboard.
     */
    public void onKeyPressed(KeyEvent event) {

        // Read the key input as a direction within the game
        switch (event.getCode()) {
            case W -> player.setDirection(Direction.NORTH);
            case A -> player.setDirection(Direction.WEST);
            case S -> player.setDirection(Direction.SOUTH);
            case D -> player.setDirection(Direction.EAST);
            default -> {
                System.out.println(UNHANDLED_KEY + event.getCode());
            }
        }

        // Checking if tick timeline is playing
        if (tickPlaying) {
            // Now perform the move based on the direction we just set
            player.move();

            // Redraw the scene after moving
            drawGame();
        }

        // Marking the event as being "done dealt with"
        event.consume();
    }

    /** Method to stop timeline and indicate "game-over".
     * Called when the player dies (Flying Assassin, timer expires, etc.)
     */
    public static void gameOver() {
        tickTimeline.stop();
        System.out.println("GAME OVER");

        // TODO: Switch over to a game over screen
    }

    /** Method to indicate the level has finished.
     * Called when the player reaches an exit AND all loot + levers collected
     */
    public void finishLevel() {
        tickTimeline.stop();
        System.out.println("LEVEL COMPLETE");

        // TODO: Create a finish level screen
    }

    /** Method to increase the score.
     *
     * @param score value of score
     */
    public void addScore(int score) {
        this.score += score;
    }

    public int getScore() {
        return score;
    }
}
