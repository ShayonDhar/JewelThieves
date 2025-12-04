package game.playerProfile;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ProfileCreationController {
    @FXML
    private TextField nameField;

    // Parent controller reference so we can tell it to refresh the profile list
    private ProfileController parent;

    // Called by the parent after FXMLLoader.load()
    public void setParent(ProfileController parent) {
        this.parent = parent;
    }

    @FXML
    public void onCreate() {
        String name = nameField.getText();
        if (name == null) name = "";
        name = name.trim();

        if (name.isEmpty()) {
            showInfo("Invalid name", "Name cannot be empty.");
            return;
        }

        // Load existing profiles to check duplicates
        // (ProfileManager assumed to exist — earlier I suggested a simple file-based one)
        boolean exists = ProfileManager.loadProfiles()
                .stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(name));

        if (exists) {
            showInfo("Duplicate", "A profile with that name already exists.");
            return;
        }

        // Create and save
        PlayerProfile newProfile = new PlayerProfile(name);
        ProfileManager.addProfile(newProfile);

        // Optionally set as current profile for the game
        GameState.setCurrentProfile(newProfile);

        // Tell parent to refresh its list and select the new profile
        if (parent != null) {
            parent.refreshProfiles(newProfile);
        }

        // Close this window
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
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }
}
