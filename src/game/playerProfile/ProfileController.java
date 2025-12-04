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

public class ProfileController {
    private static final int WINDOW_WIDTH = 950;
    private static final int WINDOW_HEIGHT = 700;
    public Button newProfileBtn;

    public Button deleteProfileBtn;
    public ComboBox profileCombo;
    public Button startBtn;
    public Button cancelBtn;

    @FXML
    public void initialize() {
        refreshProfiles(null);
    }

    @FXML
    private void newProfile() {
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

        // Load real profiles
        List<PlayerProfile> profiles = ProfileManager.loadProfiles();

        // Create list with placeholder at the top
        ObservableList<PlayerProfile> option = FXCollections.observableArrayList();

        PlayerProfile placeholder = new PlayerProfile("Select Player", 1);
        option.add(placeholder);               // index 0
        option.addAll(profiles);               // real profiles follow

        // Assign list to ComboBox
        profileCombo.setItems(option);

        // Select placeholder by default unless something was chosen
        if (selected != null) {
            profileCombo.getSelectionModel().select(selected);
        } else {
            profileCombo.getSelectionModel().selectFirst();   // selects placeholder
        }
    }

    @FXML
    private void deleteProfile() {

        PlayerProfile selected = (PlayerProfile) profileCombo.getSelectionModel().getSelectedItem();

        // No profile selected
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setHeaderText("No profile selected.");
            alert.showAndWait();
            return;
        }

        // Cannot delete the placeholder
        if (selected.getName().equals("Select Player")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setHeaderText("You must select a real profile to delete.");
            alert.showAndWait();
            return;
        } else {
            // Delete from file
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Profile Deleted");
            ProfileManager.deleteProfile(selected);
            alert.showAndWait();

    }


        // Refresh and select placeholder again
        refreshProfiles(null);
    }


    @FXML private void startGame() {
        if (profileCombo.getSelectionModel().getSelectedItem() == null
                ||
                profileCombo.getSelectionModel().getSelectedItem().equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("You must select a profile to proceed");
            alert.showAndWait();
        } else if (((PlayerProfile) profileCombo.getSelectionModel().getSelectedItem())
                .getName().equals("Select Player")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("You must select a profile to proceed");
            alert.showAndWait();
        } else {
            // Start the game with the selected profile
            PlayerProfile selectedProfile = (PlayerProfile) profileCombo.getSelectionModel().getSelectedItem();
            try {
                // Load FXML using FXMLLoader instance (not static)
                FXMLLoader loader = new FXMLLoader(getClass().getResource("LevelMenu.fxml"));
                Pane root = loader.load();
                loader.getController();

                // New stage
                Stage gameStage = new Stage();

                // Load the scene onto the scene
                Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

                scene.getStylesheets().add(
                        getClass().getResource("levelMenu.css").toExternalForm()
                );


                // Setting the scene and displaying it
                gameStage.setScene(scene);
                gameStage.setTitle("Jewel Thieves Group 01 - Game");
                gameStage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        PlayerProfile selectedProfile = (PlayerProfile) profileCombo.getSelectionModel().getSelectedItem();
        ProfileSession.set(selectedProfile);

    }

    @FXML private void cancel() {
        Stage stage = (Stage) profileCombo.getScene().getWindow();
        stage.close();
    }

}
