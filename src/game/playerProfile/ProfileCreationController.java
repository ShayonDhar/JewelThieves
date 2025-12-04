package game.playerProfile;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ProfileCreationController {

    @FXML
    private TextField nameField;

    private ProfileController parent;

    public void setParent(ProfileController parent) {
        this.parent = parent;
    }

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

        // Load existing profiles to check duplicates
        String finalName = name;
        boolean exists = ProfileManager.loadProfiles()
                .stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(finalName));

        if (exists) {
            showInfo("Duplicate Profile", "A profile with that name already exists.");
            return;
        }

        // Create new profile with level 1 unlocked by default
        PlayerProfile newProfile = new PlayerProfile(name);

        // Save profile
        try {
            ProfileManager.addProfile(newProfile);
        } catch (Exception e) {
            showInfo("Save Error", "Failed to save the new profile.");
            return;
        }

        // Update UI
        if (parent != null) {
            parent.refreshProfiles(newProfile);
        }
        closeWindow();
    }

    @FXML
    public void onCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
