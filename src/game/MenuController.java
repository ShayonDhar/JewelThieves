package game;

import game.entity.Direction;
import game.level.LevelLoader;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class MenuController {

    private Stage stage;

    // Constants for the window dimensions
    private static final int WINDOW_WIDTH = 950;
    private static final int WINDOW_HEIGHT = 700;

    // Called by Main to give the menu controller access to the stage.
    public void setStage(Stage stage) {
        this.stage = stage;
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
            Scene scene = new Scene(root,WINDOW_WIDTH,WINDOW_HEIGHT);
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
}
