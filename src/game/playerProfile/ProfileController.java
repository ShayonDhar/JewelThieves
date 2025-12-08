package game.playerProfile;

import game.GameController;
import java.util.List;
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

/**
 * Controller responsible for managing player profiles within the profile selection screen.
 * This class handles displaying available profiles, validating the selected profile,
 * launching the game window with the chosen profile, and returning to the previous scene.
 *
 * @author Alex Samuel
 * @version 1.0
 */
public class ProfileController {

    private static final int WINDOW_WIDTH = 950;
    private static final int WINDOW_HEIGHT = 700;

    /** ComboBox displaying the available player profiles. */
    public ComboBox profileCombo;

    /** Button used to start the game with the selected profile. */
    public Button startBtn;

    /** Button used to cancel and close the profile selection window. */
    public Button cancelBtn;

    /**
     * Initializes the controller after its FXML components are loaded.
     * Loads and populates the profile list.
     */
    @FXML
    public void initialize() {
        refreshProfiles(null);
    }

    /**
     * Reloads all player profiles from storage and updates the ComboBox.
     * A placeholder entry is added at the top, and the method can optionally
     * auto-select a given profile.
     *
     * @param selected the profile to pre-select, or null to select the placeholder
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
     * Starts the game using the profile selected in the ComboBox.
     * This method validates that a real profile is chosen (not the placeholder or null),
     * stores it in, loads the game UI, initializes the first level,
     * and opens the game window.
     */
    @FXML
    private void startGame() {

        PlayerProfile selectedProfile =
                (PlayerProfile) profileCombo.getSelectionModel().getSelectedItem();

        if (selectedProfile == null
                || selectedProfile.getName().equals("Select Player")) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Selection");
            alert.setHeaderText("No Valid Profile Selected");
            alert.setContentText("Please select a valid player profile before starting the game.");
            alert.showAndWait();
            return;
        }

        ProfileSession.set(selectedProfile);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/game/GameGraphics.fxml"));
            Pane root = loader.load();

            GameController controller = loader.getController();
            controller.loadLevel(1);

            Stage gameStage = new Stage();
            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            gameStage.setScene(scene);

            scene.setOnKeyPressed(controller::onKeyPressed);

            scene.getStylesheets().add(
                    getClass().getResource("/game/lever-colour.css").toExternalForm()
            );

            gameStage.setTitle("Jewel Thieves Group 01 - Game");
            gameStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the profile selection window.
     */
    @FXML
    private void cancel() {
        Stage stage = (Stage) profileCombo.getScene().getWindow();
        stage.close();
    }
}
