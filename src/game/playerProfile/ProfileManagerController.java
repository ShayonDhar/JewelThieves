package game.playerProfile;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Controller responsible for managing player profiles in the application.
 * This class handles loading and displaying existing profiles, creating new ones,
 * deleting selected profiles, and opening the level menu for the chosen profile.
 * It interacts with to read and modify stored profile data.
 *
 * @author Alex Samuel
 * @version 1.0
 */
public class ProfileManagerController {

    private static final int WIDTH = 950;
    private static final int HEIGHT = 700;
    @FXML
    private ComboBox<PlayerProfile> profileCombo;

    /**
     * Initializes the controller after the FXML has been loaded.
     * Loads and displays all existing profiles.
     */
    @FXML
    public void initialize() {
        refreshProfiles(null);
    }

    /**
     * Opens the profile creation window where the user can create a new player profile.
     * Loads the corresponding FXML layout and displays it in a new window.
     */
    @FXML
    private void createNewProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProfileCreation.fxml"));
            Pane root = loader.load();

            ProfileCreationController creationController = loader.getController();
            creationController.setParent(this);

            Stage profileStage = new Stage();
            Scene scene = new Scene(root);

            profileStage.setScene(scene);
            profileStage.setTitle("Create New Player Profile");
            profileStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Reloads the list of profiles and updates the ComboBox.
     * A "Select Player" placeholder is always added at the top.
     *
     * @param selected profile to select after refresh, or null to select the placeholder
     */
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

    /**
     * Deletes the currently selected profile if it is valid.
     * Displays an error alert if the user selects the placeholder or nothing.
     */
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

    /**
     * Closes the profile manager window.
     */
    @FXML
    private void closeWindow() {
        Stage stage = (Stage) profileCombo.getScene().getWindow();
        stage.close();
    }

    /**
     * Displays an alert dialog of the specified type with the given message.
     *
     * @param type the type of alert
     * @param msg the message to display in the alert header
     */
    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(msg);
        alert.showAndWait();
    }

    /**
     * Opens the level menu for the currently selected profile.
     * Ensures the user selects a valid profile before navigating.
     *
     * @param actionEvent the event that triggered this method
     */
    @FXML
    public void viewLevelsUnlocked(ActionEvent actionEvent) {
        PlayerProfile selectedProfile = profileCombo.getSelectionModel().getSelectedItem();

        if (selectedProfile == null || selectedProfile.getName().equals("Select Player")) {
            showAlert(Alert.AlertType.WARNING, "Please select a valid player profile before viewing unlocked levels.");
            return;
        }

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("LevelMenu.fxml"));
            Pane root = loader.load();

            LevelMenuController controller = loader.getController();

            controller.setProfile(selectedProfile);

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, WIDTH, HEIGHT);
            scene.getStylesheets().add(getClass().getResource("levelMenu.css").toExternalForm());

            stage.setScene(scene);
            stage.setTitle("Unlocked Levels");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Failed to open Level Menu.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
