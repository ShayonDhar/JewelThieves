package game;

import game.entity.Direction;
import game.entity.Player;
import game.level.Level;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

/**
 * Class that links the MainApplication to the SceneBuilder FXML controlling aspect
 */
public class GameController {

    public Canvas canvas;
    public GraphicsContext gc;
    public Level level;
    public Player player;

    /**
     * Method that initialises the game.
     */
    @FXML
    public void initialize() {
        // Drawing the canvas background
        gc = canvas.getGraphicsContext2D();
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Drawing the level background
        level = new Level("LevelFile.txt");
        level.draw(gc);

        // TODO: Temp code Drawing the player
        player = new Player(45, 40, 20, Direction.NORTH, true, true);
        player.draw(gc);


    }

    /**
     * Tester method
     * @param actionEvent on pressing the "Random Circle" button
     */
    @FXML
    public void createCircle(ActionEvent actionEvent) {
        gc.setFill(Color.PURPLE);
        gc.fillOval(100,100,50,50);
    }

    public void onKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
//            case W -> player.moveUp();
//            case A -> player.moveLeft();
//            case S -> player.moveDown();
//            case D -> player.moveRight();
        }

        // Redraw the scene after moving
        level.draw(gc);
        player.draw(gc);
    }

}
