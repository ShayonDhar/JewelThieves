package game;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Class that controls the main menu display.
 *
 * @author Antoni Wachowiak
 * @version 1.0
 */
public class MenuController {

    // Constants for the GameGraphics window dimensions
    private static final int WINDOW_WIDTH = 950;
    private static final int WINDOW_HEIGHT = 700;

    /**
     * Called by Main to give the menu controller access to the stage.
     *
     * @param stage stage to be given control by Main
     */
    public void setStage(Stage stage) {
    }

    /**
     * Button-click method to load a new game.
     */
    @FXML
    public void buttonNewGame() {

        try {
            // Load FXML using FXMLLoader instance (not static)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameGraphics.fxml"));
            Pane root = loader.load();
            GameController controller = loader.getController();

            // New stage
            Stage gameStage = new Stage();

            // Load the scene onto the scene
            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            root.setStyle("-fx-background-color: black");

            // Register key input into the GameController
            scene.setOnKeyPressed(controller::onKeyPressed);

            // Setting the scene and displaying it
            gameStage.setScene(scene);
            gameStage.setTitle("Jewel Thieves Group 01 - Game");
            gameStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Button action to close the stage.
     */
    @FXML
    public void buttonQuit() {
        System.exit(0);
    }
}
