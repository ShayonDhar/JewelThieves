package game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * Class that links the MainApplication to the SceneBuilder FXML controlling aspect
 */
public class GameController {

    public Canvas canvas;
    private GraphicsContext gc;
    // private Level level;

    /**
     * Method that runs immediately after initialising the program
     */
    @FXML
    public void initialize() {
        gc = canvas.getGraphicsContext2D();
        // NOTE TO SELF: Go to the GraphicsContext JavaDoc
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Draw the background

        /*
        - Call a method to load the Level attributes to build a level (the Level class needs a draw(GraphicsContext)
        method
        - add level object to the canvas
        - Call a method to load the Tiles to build the tiles with the colours

         */
    }

    @FXML
    public void createCircle(ActionEvent actionEvent) {

    }

    @FXML
    public void createRectangle(ActionEvent actionEvent) {

    }

    @FXML
    public void quitGame(ActionEvent actionEvent) {

    }

}
