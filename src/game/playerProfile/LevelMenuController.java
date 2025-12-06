package game.playerProfile;

import game.GameController;
import game.level.Level;
import game.save.GameSaveManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class LevelMenuController {
    private static final int LEVEL_ONE = 1;
    private static final int LEVEL_TWO = 2;
    private static final int LEVEL_THREE = 3;
    private static final int LEVEL_FOUR = 4;
    private static final int LEVEL_FIVE = 5;
    private static final int LEVEL_SIX = 6;
    private static final int LEVEL_SEVEN = 7;
    private static final int LEVEL_EIGHT = 8;
    private static final int LEVEL_NINE = 9;
    private static final int LEVEL_TEN = 10;
    private static final int WINDOW_WIDTH = 950;
    private static final int WINDOW_HEIGHT = 700;
    public Button level1Btn;
    public Button level2Btn;
    public Button level3Btn;
    public Button level4Btn;
    public Button level5Btn;
    public Button level6Btn;
    public Button level7Btn;
    public Button level8Btn;
    public Button level9Btn;
    public Button level10Btn;
    @FXML
    public void initialize() {
        PlayerProfile profile = ProfileSession.getProfile();
        if (profile == null) {
            throw new RuntimeException("Error: Profile is null");
        }

        level1Btn.setUserData(LEVEL_ONE);
        level2Btn.setUserData(LEVEL_TWO);
        level3Btn.setUserData(LEVEL_THREE);
        level4Btn.setUserData(LEVEL_FOUR);
        level5Btn.setUserData(LEVEL_FIVE);
        level6Btn.setUserData(LEVEL_SIX);
        level7Btn.setUserData(LEVEL_SEVEN);
        level8Btn.setUserData(LEVEL_EIGHT);
        level9Btn.setUserData(LEVEL_NINE);
        level10Btn.setUserData(LEVEL_TEN);

        int unlocked = profile.getMaxUnlockedLevel();

        // Disable levels above unlocked level
        level1Btn.setDisable(unlocked < LEVEL_ONE);
        level2Btn.setDisable(unlocked < LEVEL_TWO);
        level3Btn.setDisable(unlocked < LEVEL_THREE);
        level4Btn.setDisable(unlocked < LEVEL_FOUR);
        level5Btn.setDisable(unlocked < LEVEL_FIVE);
        level6Btn.setDisable(unlocked < LEVEL_SIX);
        level7Btn.setDisable(unlocked < LEVEL_SEVEN);
        level8Btn.setDisable(unlocked < LEVEL_EIGHT);
        level9Btn.setDisable(unlocked < LEVEL_NINE);
        level10Btn.setDisable(unlocked < LEVEL_TEN);
    }

    @FXML
    private void onLevelClicked(ActionEvent event) {
        Button clicked = (Button) event.getSource();
        int levelNumber = (int) clicked.getUserData();

        loadLevel(levelNumber, clicked);
    }

    private void loadLevel(int levelNumber, Button sourceButton) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/game/GameGraphics.fxml"));
            Parent root = loader.load();

            GameController controller = loader.getController();
            controller.loadLevel(levelNumber);

            // now correctly get the stage from the *clicked* button
            Stage gameStage = (Stage) sourceButton.getScene().getWindow();

            // Load the scene onto the scene
            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            scene.setOnKeyPressed(controller::onKeyPressed);

            // Setting the scene and displaying it
            gameStage.setScene(scene);
            gameStage.setTitle("Jewel Thieves Group 01 - Game");
            gameStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadFromSave(String saveFileName, Stage stage) {
        try {
            // Load the Game FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/game/GameGraphics.fxml"));
            Parent root = loader.load();

            // Get the controller
            GameController controller = loader.getController();

            // Create the save manager with the controller
            GameSaveManager gsm = new GameSaveManager(controller);

            // Load the saved level from file
            Level loadedLevel = gsm.load(saveFileName);
            if (loadedLevel == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Failed to load save file: " + saveFileName);
                alert.showAndWait();
                return;
            }
            controller.loadSavedLevel(loadedLevel);

            // Create the scene and set the key event handler
            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            scene.setOnKeyPressed(controller::onKeyPressed);

            // Set the scene on the passed-in stage and show
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error loading saved game: " + e.getMessage());
            alert.showAndWait();
        }
    }


}
