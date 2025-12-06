package game.playerProfile;

import game.GameController;
import game.level.Level;
import game.save.GameSaveManager;
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

public class ProfileSaveController {
    private static final int WINDOW_WIDTH = 950;
    private static final int WINDOW_HEIGHT = 700;
    private GameController gameController;
    public ComboBox profileCombo;
    public Button LoadBtn;
    public Button cancelBtn;
    public ComboBox saveFileCombo;

    @FXML
    public void initialize() {
        refreshProfiles(null);
        profileCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal instanceof PlayerProfile profile) {
                loadSaveFilesForProfile(profile);
            }
        });

    }
    private void loadSaveFilesForProfile(PlayerProfile profile) {
        saveFileCombo.getItems().clear();

        if (profile.getName().equals("Select Player")) {
            return;
        }


        GameSaveManager gsm = new GameSaveManager();
        String[] saves = gsm.listSaves();


        ObservableList<String> filtered = FXCollections.observableArrayList();

        for (String s : saves) {
            if (s.startsWith(profile.getName())) {
                filtered.add(s);
            }
        }

        saveFileCombo.setItems(filtered);

        if (!filtered.isEmpty()) {
            saveFileCombo.getSelectionModel().selectFirst();
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
    private void startGame() {
        // Get selected profile and save file
        PlayerProfile selectedProfile = (PlayerProfile) profileCombo.getSelectionModel().getSelectedItem();
        String selectedSave = (String) saveFileCombo.getSelectionModel().getSelectedItem();

        // Validate profile
        if (selectedProfile == null || selectedProfile.getName().equals("Select Player")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("You must select a real profile.");
            alert.showAndWait();
            return;
        }

        // Validate save file
        if (selectedSave == null || selectedSave.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("You must select a save file.");
            alert.showAndWait();
            return;
        }

        // Load the saved level from the selected save file
        GameSaveManager gsm = new GameSaveManager(gameController);
        Level loadedLevel = gsm.load(selectedSave);

        if (loadedLevel == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Failed to load save file.");
            alert.showAndWait();
            return;
        }

        // Set the selected profile in the session
        ProfileSession.set(selectedProfile);

        try {
            // Get the current stage
            Stage gameStage = (Stage) profileCombo.getScene().getWindow();

            // Initialize GameController if it's not already
            if (gameController == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/game/GameGraphics.fxml"));
                Pane root = loader.load();
                gameController = loader.getController();

                // Set up scene
                Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
                gameStage.setScene(scene);
            }

            // Load the saved level into GameController
            gameController.loadSavedLevel(loadedLevel);

            // Show the stage
            gameStage.setTitle("Jewel Thieves Group 01 - Game");
            gameStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error starting game");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }





    @FXML
    private void cancel() {
        Stage stage = (Stage) profileCombo.getScene().getWindow();
        stage.close();
    }

}
