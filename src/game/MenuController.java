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
            // Create a temporary save manager just to list saves
            GameSaveManager saveManager = new GameSaveManager(null);

            // Get list of available save files
            String[] saveFiles = saveManager.listSaves();

            if (saveFiles.length == 0) {
                // No saves found
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("No Saves Found");
                alert.setHeaderText(null);
                alert.setContentText("No save files found. Start a new game first!");
                alert.showAndWait();
                return;
            }

            ChoiceDialog<String> dialog = new ChoiceDialog<>(saveFiles[0], saveFiles);
            dialog.setTitle("Load Game");
            dialog.setHeaderText("Select a save file to load");
            dialog.setContentText("Save file:");

            Optional<String> result = dialog.showAndWait();

            if (result.isPresent()) {
                String selectedFile = result.get();
                loadGameWithSave(selectedFile);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to load game: " + e.getMessage());
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameGraphics.fxml"));
            Pane root = loader.load();
            GameController controller = loader.getController();

            // New stage
            Stage gameStage = new Stage();

            // Load the scene onto the scene
            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

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
