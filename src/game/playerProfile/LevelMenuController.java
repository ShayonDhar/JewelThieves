package game.playerProfile;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Controller for the Level Selection Menu in the player profile section.
 * This class manages the display of available and locked levels based on the player's progress.
 * Levels that have been unlocked are displayed normally, while locked levels are visually distinguished
 * and marked as "Locked". The controller also handles navigation back to the profile manager screen.
 *
 * @author Alex Samuel
 * @version 1.0.0
 */
public class LevelMenuController {

    // UI Labels for each level (1 through 10)
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

    public Button backButton;
    private PlayerProfile profile;

    /**
     * Sets the player profile and updates the level display accordingly.
     *
     * @param profile the PlayerProfile instance containing the player's progress
     */
    public void setProfile(PlayerProfile profile) {
        this.profile = profile;
        updateLabels();
    }

    /**
     * Updates all level labels to reflect the player's current progress.
     * Unlocked levels display their number (e.g., "Level 3") with normal styling.
     * Locked levels display "Locked" and receive an additional "locked-label" CSS class for visual distinction.
     * This method is safe to call even if profile is null.
     */
    private void updateLabels() {
        if (profile == null) {
            return;
        }

        int unlocked = profile.getMaxUnlockedLevel();

        // Array of all level labels for convenient iteration
        Label[] labels = {
                level1Label, level2Label, level3Label, level4Label, level5Label,
                level6Label, level7Label, level8Label, level9Label, level10Label
        };

        for (int i = 0; i < labels.length; i++) {
            Label label = labels[i];
            int levelNumber = i + 1;

            if (unlocked >= levelNumber) {
                label.setText("Level " + levelNumber);
                label.getStyleClass().setAll("title-label");
            } else {
                label.setText("Locked");
                label.getStyleClass().setAll("title-label", "locked-label");
            }
        }
    }

    /**
     * Handles the "Back" button click event.
     * Navigates from the level menu back to the main Profile Manager screen
     * by loading ProfileManager.fxml and replacing the current scene.
     */
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