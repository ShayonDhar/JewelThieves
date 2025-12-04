package game.playerProfile;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class LevelMenuController {
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
        PlayerProfile p = ProfileSession.getProfile();
        if (p == null) {
            return;
        }

        int unlocked = p.getMaxUnlockedLevel();

        // Disable levels above unlocked level
        level1Btn.setDisable(unlocked < 1);
        level2Btn.setDisable(unlocked < 2);
        level3Btn.setDisable(unlocked < 3);
        level4Btn.setDisable(unlocked < 4);
        level5Btn.setDisable(unlocked < 5);
        level6Btn.setDisable(unlocked < 6);
        level7Btn.setDisable(unlocked < 7);
        level8Btn.setDisable(unlocked < 8);
        level9Btn.setDisable(unlocked < 9);
        level10Btn.setDisable(unlocked < 10);
    }

}
