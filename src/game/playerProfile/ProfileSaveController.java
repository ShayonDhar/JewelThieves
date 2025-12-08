package game.playerProfile;

import game.GameController;
import game.level.Level;
import game.save.GameSaveManager;
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
 * Controller class for managing the profile and save file selection screen in the game.
 * Provides functionality to load player profiles, display associated save files,
 * and start the game using a selected profile and save file.
 * Handles initialization of the scene, validation of inputs, and transitioning to the game scene.
 *
 * @author Alex Samuel
 * @version 1.0
 */
public class ProfileSaveController {
    private static final int WINDOW_WIDTH = 950;
    private static final int WINDOW_HEIGHT = 700;
    public ComboBox profileCombo;
    public ComboBox saveFileCombo;
    public Button LoadBtn;
    public Button cancelBtn;
    private GameController gameController;


    /**
     * Initializes the controller. Populates the profile combo box and adds a listener
     * to update the save file combo box when a profile is selected.
     */
    @FXML
    public void initialize() {
        refreshProfiles(null);
        profileCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal instanceof PlayerProfile profile) {
                loadSaveFilesForProfile(profile);
            }
        });
    }

    /**
     * Loads the available save files for the specified profile into the save file combo box.
     *
     * @param profile The player profile for which save files should be displayed.
     */
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

    /**
     * Refreshes the list of player profiles in the profile combo box.
     * Adds a placeholder option to prompt the user to select a profile.
     *
     * @param selected The profile to pre-select, or null to select the placeholder.
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
     * Starts the game using the currently selected profile and save file.
     * Validates user selections, loads the saved game, and sets up the game scene.
     * Displays alerts if any validation or loading errors occur.
     */
    @FXML
    private void startGame() {
        PlayerProfile selectedProfile =
                (PlayerProfile) profileCombo.getSelectionModel().getSelectedItem();
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

        // Load save
        GameSaveManager gsm = new GameSaveManager(gameController);
        Level loadedLevel = gsm.load(selectedSave);

        if (loadedLevel == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Failed to load save file.");
            alert.showAndWait();
            return;
        }

        // Store profile
        ProfileSession.set(selectedProfile);

        try {
            Stage gameStage = (Stage) profileCombo.getScene().getWindow();

            Pane root;
            Scene scene;

            // Load FXML and GameController if needed
            if (gameController == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/game/GameGraphics.fxml"));
                root = loader.load();
                gameController = loader.getController();

                scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
                gameStage.setScene(scene);
            } else {
                // Reuse the existing controller's scene
                root = (Pane) gameStage.getScene().getRoot();
                scene = gameStage.getScene();
            }

            if (gameController.saveManager == null) {
                gameController.setSaveManager(new GameSaveManager(gameController));
            }

            gameController.loadSavedLevel(loadedLevel);
            scene.setOnKeyPressed(gameController::onKeyPressed);

            gameController.boardTilePane.requestFocus();

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

    /**
     * Closes the profile selection window without starting a game.
     */
    @FXML
    private void cancel() {
        Stage stage = (Stage) profileCombo.getScene().getWindow();
        stage.close();
    }

    /**
     * Sets the GameController instance for this controller, allowing it to interact
     * with the game logic and manage save files.
     *
     * @param controller The GameController to associate with this controller.
     */
    public void setGameController(GameController controller) {
        this.gameController = controller;
    }
}
