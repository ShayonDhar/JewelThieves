package game.playerProfile;

import game.playerProfile.PlayerProfile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class LevelMenuController {

    public Label level1Label;
    public Label level2Label;
    public Label level3Label;
    public Label level4Label;
    public Label level5Label;
    public Label level6Label;
    public Label level7Label;
    public Label level8Label;
    public Label level9Label;
    public Label level10Label;
    @FXML
    private Button backButton;

    private PlayerProfile profile;

    public void setProfile(PlayerProfile profile) {
        this.profile = profile;
        updateLabels();
    }

    private void updateLabels() {
        if (profile == null) {
            return;
        }

        int unlocked = profile.getMaxUnlockedLevel();

        if (profile == null) {
            return;
        }

        if (unlocked >= 1) {
            level1Label.setText("Level 1");
            level1Label.getStyleClass().setAll("title-label"); // same style as title
        } else {
            level1Label.setText("Locked");
            level1Label.getStyleClass().setAll("title-label", "locked-label"); // add grey color
        }

        if (unlocked >= 2) {
            level2Label.setText("Level 2");
            level2Label.getStyleClass().setAll("title-label");
        } else {
            level2Label.setText("Locked");
            level2Label.getStyleClass().setAll("title-label", "locked-label");
        }

        if (unlocked >= 3) {
            level3Label.setText("Level 3");
            level3Label.getStyleClass().setAll("title-label");
        } else {
            level3Label.setText("Locked");
            level3Label.getStyleClass().setAll("title-label", "locked-label");
        }

        if (unlocked >= 4) {
            level4Label.setText("Level 4");
            level4Label.getStyleClass().setAll("title-label");
        } else {
            level4Label.setText("Locked");
            level4Label.getStyleClass().setAll("title-label", "locked-label");
        }

        if (unlocked >= 5) {
            level5Label.setText("Level 5");
            level5Label.getStyleClass().setAll("title-label");
        } else {
            level5Label.setText("Locked");
            level5Label.getStyleClass().setAll("title-label", "locked-label");
        }

        if (unlocked >= 6) {
            level6Label.setText("Level 6");
            level6Label.getStyleClass().setAll("title-label");
        } else {
            level6Label.setText("Locked");
            level6Label.getStyleClass().setAll("title-label", "locked-label");
        }

        if (unlocked >= 7) {
            level7Label.setText("Level 7");
            level7Label.getStyleClass().setAll("title-label");
        } else {
            level7Label.setText("Locked");
            level7Label.getStyleClass().setAll("title-label", "locked-label");
        }

        if (unlocked >= 8) {
            level8Label.setText("Level 8");
            level8Label.getStyleClass().setAll("title-label");
        } else {
            level8Label.setText("Locked");
            level8Label.getStyleClass().setAll("title-label", "locked-label");
        }

        if (unlocked >= 9) {
            level9Label.setText("Level 9");
            level9Label.getStyleClass().setAll("title-label");
        } else {
            level9Label.setText("Locked");
            level9Label.getStyleClass().setAll("title-label", "locked-label");
        }

        if (unlocked >= 10) {
            level10Label.setText("Level 10");
            level10Label.getStyleClass().setAll("title-label");
        } else {
            level10Label.setText("Locked");
            level10Label.getStyleClass().setAll("title-label", "locked-label");
        }
    }

    @FXML
    private void backToProfileManager() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProfileManager.fxml"));
            Parent root = loader.load();


            Stage stage = (Stage) backButton.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

