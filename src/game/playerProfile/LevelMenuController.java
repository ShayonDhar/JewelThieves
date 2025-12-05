package game.playerProfile;

import game.GameController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    private void loadLevel1() {

        loadLevel(LEVEL_ONE, level1Btn);
    }

    @FXML
    private void loadLevel2() {
        loadLevel(LEVEL_TWO, level2Btn);
    }

    @FXML
    private void loadLevel3() {
        loadLevel(LEVEL_THREE, level3Btn);
    }

    @FXML
    private void loadLevel4() {
        loadLevel(LEVEL_FOUR, level4Btn);
    }

    @FXML
    private void loadLevel5() {
        loadLevel(LEVEL_FIVE, level5Btn);
    }

    @FXML
    private void loadLevel6() {
        loadLevel(LEVEL_SIX, level6Btn);
    }

    @FXML
    private void loadLevel7() {
        loadLevel(LEVEL_SEVEN, level7Btn);
    }

    @FXML
    private void loadLevel8() {
        loadLevel(LEVEL_EIGHT, level8Btn);
    }

    @FXML
    private void loadLevel9() {
        loadLevel(LEVEL_NINE, level9Btn);
    }

    @FXML
    private void loadLevel10() {
        loadLevel(LEVEL_TEN, level10Btn);
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
            Stage stage = (Stage) sourceButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
