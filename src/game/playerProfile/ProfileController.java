package game.playerProfile;

import game.GameController;
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
    private GameController gameController;
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
        if (profileCombo.getSelectionModel().getSelectedItem() == null
                || profileCombo.getSelectionModel().getSelectedItem().equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("You must select a profile to proceed");
            alert.showAndWait();
            return;
        }

        PlayerProfile selectedProfile =
                (PlayerProfile) profileCombo.getSelectionModel().getSelectedItem();

        if (selectedProfile.getName().equals("Select Player")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("You must select a real profile to proceed");
            alert.showAndWait();
            return;
        }

        // FIX: store profile before loading next window
        ProfileSession.set(selectedProfile);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LevelMenu.fxml"));
            Pane root = loader.load();

            Stage gameStage = new Stage();
            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

            scene.getStylesheets().add(
                    getClass().getResource("levelMenu.css").toExternalForm()
            );

            gameStage.setScene(scene);
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

    public void setGameController(GameController controller) {
        this.gameController = controller;
    }


}
