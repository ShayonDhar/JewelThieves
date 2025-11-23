package game;

import game.entity.Direction;
import game.entity.Player;
import game.level.Level;
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

    public Canvas canvas;
    public GraphicsContext gc;
    public Level level;
    // TODO: Temp code until player is implemented
    public Player player = new Player(40, 45,
            Direction.NORTH, true, true);

    // Timeline which will cause tick method to be called periodically.
    private static Timeline tickTimeline;

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
        level = new Level("LevelFile.txt");
        level.draw(gc);
        player.draw(gc);
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
            // case W -> player.moveUp();
            // case A -> player.moveLeft();
            // case S -> player.moveDown();
            // case D -> player.moveRight();
        }

        // Redraw the scene after moving
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




}
