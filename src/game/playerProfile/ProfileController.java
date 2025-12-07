package game.playerProfile;

import game.GameController;
import game.level.Level;
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
    public ComboBox profileCombo;
    public Button startBtn;
    public Button cancelBtn;

    @FXML
    public void initialize() {
        refreshProfiles(null);

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
    private void startGame() {

        PlayerProfile selectedProfile =
                (PlayerProfile) profileCombo.getSelectionModel().getSelectedItem();

        // Validate selection: prevent starting with placeholder or null
        if (selectedProfile == null
                || selectedProfile.getName().equals("Select Player")) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Selection");
            alert.setHeaderText("No Valid Profile Selected");
            alert.setContentText("Please select a valid player profile before starting the game.");
            alert.showAndWait();
            return;
        }

        // Store selected profile
        ProfileSession.set(selectedProfile);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/game/GameGraphics.fxml"));
            Pane root = loader.load();

            GameController controller = loader.getController();
            controller.loadLevel(1);

            Stage gameStage = new Stage();
            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            gameStage.setScene(scene);

            // Attach movement handler
            scene.setOnKeyPressed(controller::onKeyPressed);

            gameStage.setTitle("Jewel Thieves Group 01 - Game");
            gameStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void cancel() {
        Stage stage = (Stage) profileCombo.getScene().getWindow();
        stage.close();
    }


}
