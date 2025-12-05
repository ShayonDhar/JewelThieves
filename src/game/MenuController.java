package game;

import game.save.GameSaveManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Optional;

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

    private Stage stage;

    /**
     * Called by Main to give the menu controller access to the stage.
     *
     * @param stage stage to be given control by Main
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Handles the Load Game button from the main menu.
     */
    @FXML
    public void buttonLoadGame() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("playerProfile/ProfileSelectionLoad.fxml"));
            Pane root = loader.load();

            // Pass menu stage if the profile screen needs to close it later
            Stage selectionStage = new Stage();
            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            selectionStage.setScene(scene);
            selectionStage.setTitle("Select Profile to Load");
            selectionStage.show();

            // Close the main menu window so you don’t stack windows like a raccoon hoarding trash
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to open profile selection: " + e.getMessage());
            alert.showAndWait();
        }
    }


    /**
     * Loads the game scene and tells it to load a specific save file.
     */
    private void loadGameWithSave(String saveFilename) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameGraphics.fxml"));
            Pane root = loader.load();

            GameController gameController = loader.getController();

            gameController.loadSaveFile(saveFilename);

            Stage gameStage = new Stage();

            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            root.setStyle("-fx-background-color: black");

            scene.setOnKeyPressed(gameController::onKeyPressed);
            gameStage.setTitle("Jewel Thieves Group 01 - Game");

            gameStage.setScene(scene);
            gameStage.show();

            // Close the menu stage to avoid multiple windows
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to start game: " + e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Button-click method to load a new game.
     */
    @FXML
    public void buttonNewGame() {

        try {
            // Load FXML using FXMLLoader instance (not static)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProfileSelection.fxml"));
            Pane root = loader.load();
            loader.getController();

            // New stage
            Stage gameStage = new Stage();

            // Load the scene onto the scene
            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);


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
