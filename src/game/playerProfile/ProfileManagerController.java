package game.playerProfile;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.List;

public class ProfileManagerController {

    @FXML
    private Button newProfileBtn;

    @FXML
    private Button deleteProfileBtn;

    @FXML
    private Button closeBtn;

    @FXML
    private ComboBox<PlayerProfile> profileCombo;

    @FXML
    public void initialize() {
        refreshProfiles(null);
    }

    @FXML
    private void createNewProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProfileCreation.fxml"));
            Pane root = loader.load();

            ProfileCreationController creationController = loader.getController();
            creationController.setParent(this);

            Stage profileStage = new Stage();
            Scene scene = new Scene(root, 400, 300);

            profileStage.setScene(scene);
            profileStage.setTitle("Create New Player Profile");
            profileStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshProfiles(PlayerProfile selected) {

        List<PlayerProfile> profiles = ProfileManager.loadProfiles();

        ObservableList<PlayerProfile> option = FXCollections.observableArrayList();

        PlayerProfile placeholder = new PlayerProfile("Select Player", 1);
        option.add(placeholder);
        option.addAll(profiles);

        profileCombo.setItems(option);

        if (selected != null) {
            profileCombo.getSelectionModel().select(selected);
        } else {
            profileCombo.getSelectionModel().selectFirst();
        }
    }

    @FXML
    private void deleteSelectedProfile() {

        PlayerProfile selected = profileCombo.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.ERROR, "No profile selected.");
            return;
        }

        if (selected.getName().equals("Select Player")) {
            showAlert(Alert.AlertType.ERROR, "You must choose a real profile to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Profile");
        confirm.setHeaderText("Profile Deleted");
        ProfileManager.deleteProfile(selected);

        confirm.showAndWait();
        refreshProfiles(null);
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) profileCombo.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(msg);
        alert.showAndWait();
    }
}
