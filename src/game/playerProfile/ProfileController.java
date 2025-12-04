package game.playerProfile;

import game.GameController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ProfileController {
    public Button newProfileBtn;

    public Button deleteProfileBtn;
    public ComboBox profileCombo;
    public Button startBtn;
    public Button cancelBtn;

    @FXML
    private void newProfile() {
        try {
            // Load FXML using your preferred FXMLLoader pattern
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProfileCreationForm.fxml"));
            Pane root = loader.load();
            ProfileCreationController creationController = loader.getController();
            creationController.setParent(this);

            // New stage for the profile creation form
            Stage profileStage = new Stage();

            // Create scene
            Scene scene = new Scene(root, 400, 300);
            root.setStyle("-fx-background-color: black;");


            // Show window
            profileStage.setScene(scene);
            profileStage.setTitle("Create New Player Profile");
            profileStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void refreshProfiles(PlayerProfile selected) {
        // Reload profiles from storage
        java.util.List<PlayerProfile> list = ProfileManager.loadProfiles();
        javafx.collections.ObservableList<PlayerProfile> obs = javafx.collections.FXCollections.observableArrayList(list);
        profileCombo.setItems(obs);

        if (selected != null) {
            profileCombo.getSelectionModel().select(selected);
        } else if (!obs.isEmpty()) {
            profileCombo.getSelectionModel().selectFirst();
        }
    }



    @FXML private void deleteProfile() { }

    @FXML private void startGame() {
        if (profileCombo.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("You must select a profile to proceed");
            alert.showAndWait();
        }
    }

    @FXML private void cancel() {
        Stage stage = (Stage) profileCombo.getScene().getWindow();
        stage.close();
    }

}
