package game.playerProfile;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller responsible for handling creation of new player profiles.
 * This class manages the input of a profile name, validates it,
 * prevents duplicates, creates a new {@link PlayerProfile}, saves it,
 * and notifies its parent controller to refresh the profile list.
 *
 * @author Alex Samuel
 * @version 1.0
 */
public class ProfileCreationController {

    /** Text field where the user types the new profile name. */
    @FXML
    private TextField nameField;

    /** Reference to the parent controller so it can refresh its profile list after creation. */
    private ProfileManagerController parent;

    /**
     * Sets the parent controller to be notified after a profile is successfully created.
     *
     * @param parent the managing controller that opened this window
     */
    public void setParent(ProfileManagerController parent) {
        this.parent = parent;
    }

    /**
     * Event handler triggered when the user clicks the Create button.
     * This method validates the entered name, checks for duplicate profiles,
     * saves the new profile, refreshes the parent controller's list, and closes the window.
     */
    @FXML
    public void onCreate() {
        String name = nameField.getText();
        if (name == null) {
            name = "";
        }
        name = name.trim();

        if (name.isEmpty()) {
            showInfo("Invalid Name", "Name cannot be empty.");
            return;
        }

        // Check if profile already exists
        String finalName = name;
        boolean exists = ProfileManager.loadProfiles()
                .stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(finalName));

        if (exists) {
            showInfo("Duplicate Profile", "A profile with that name already exists.");
            return;
        }

        // Create new profile with level 1 unlocked
        PlayerProfile newProfile = new PlayerProfile(name);

        try {
            ProfileManager.addProfile(newProfile);
        } catch (Exception e) {
            showInfo("Save Error", "Failed to save the new profile.");
            return;
        }

        // Refresh parent controller
        if (parent != null) {
            parent.refreshProfiles(newProfile);
        }
        assert parent != null;
        parent.refreshProfiles(null);

        closeWindow();
    }

    /**
     * Event handler triggered when the user clicks the Cancel button.
     * Closes the creation window without making changes.
     */
    @FXML
    public void onCancel() {
        closeWindow();
    }

    /**
     * Closes the profile creation window.
     */
    private void closeWindow() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    /**
     * Displays an informational alert with the given title and message.
     *
     * @param title   the alert title
     * @param message the message to display
     */
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
