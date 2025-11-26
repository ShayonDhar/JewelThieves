package game;

import game.entity.Direction;
import game.entity.Player;
import game.item.Item;
import game.item.Loot;
import game.level.Level;
import game.level.LevelLoader;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

/**
 * Class that links the MainApplication to the SceneBuilder FXML controlling aspect.
 *
 */
public class GameController {

    private static final String UNHANDLED_KEY = "Unhandled key: ";
    private static final String LEVEL_FILE = "LevelFile.txt";

    public Canvas canvas;
    public GraphicsContext gc;
    public Level level;
    public Player player;

    // Timeline which will cause tick method to be called periodically.
    private static Timeline tickTimeline;
    private int score = 0;

    /**
     * Method that initialises the game.
     */
    @FXML
    public void initialize() {
        // New timeline with one keyframe that triggers the tick method every half a second.
        tickTimeline = new Timeline(new KeyFrame(
                Duration.millis(500), event -> tick()));
        tickTimeline.setCycleCount(Animation.INDEFINITE); // Loop indefinitely
        // Drawing the canvas background
        gc = canvas.getGraphicsContext2D();
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Drawing the game
        LevelLoader loader = new LevelLoader(this);
        level = loader.load("LevelFile.txt");
        player = level.getPlayer();
    }

    /**
     * Updates the game state and redraws the scene.
     * Updates periodically to update the entity positions, the state of items, and the game time
     */
    public void tick() {
        //Level.moveNPCs();
        player.move();
        player.setX(player.getX() + 30);
        if (player.getX() > canvas.getWidth()) {
            player.setX(0);
        }
        // Check for loot collection
        Item item = level.getItemAt(player.getX(), player.getY());
        if (item instanceof Loot loot) {
            addScore(loot.getLootType().getValue());
            level.removeItemFromGrid(player.getX(), player.getY());
        }

        // Redraw the whole canvas
        player.draw(gc);
    }

    /**
     * Method to start the tick timeline.
     * @param actionEvent
     */
    @FXML
    public void buttonStartAction(ActionEvent actionEvent) {
        tickTimeline.play();
    }

    /**
     * Method to stop the tick timeline.
     * @param actionEvent
     */
    @FXML
    public void buttonStopAction(ActionEvent actionEvent) {
        tickTimeline.stop();
    }

    public void onKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case W -> player.setDirection(Direction.NORTH);
            case A -> player.setDirection(Direction.WEST);
            case S -> player.setDirection(Direction.SOUTH);
            case D -> player.setDirection(Direction.EAST);
            default -> {
                System.out.println(UNHANDLED_KEY + event.getCode());
            }
        }

        // Now perform the move based on the direction we just set
        player.move();

        level.draw(gc);
        player.draw(gc);

        // Marking the event as being "done dealt with"
        event.consume();
    }

    // Called when the player dies (Flying Assassin, timer expires, etc.)
    public static void gameOver() {
        tickTimeline.stop();
        System.out.println("GAME OVER");

        //TODO: Switch over to a game over screen
    }
    // Called when the player reaches an exit AND all loot + levers collected
    public void finishLevel() {
        tickTimeline.stop();
        System.out.println("LEVEL COMPLETE");

        //TODO: Create a finish level screen 
    }
    // Called when starting gameplay (after pressing Start)
    public void startLevel() {
        level.draw(gc);
        player.draw(gc);
        tickTimeline.play();
    }
    public void addScore(int score) {
        this.score += score;
    }
    public int getScore() {
        return score;
    }




}
