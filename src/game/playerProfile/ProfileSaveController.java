package game.playerProfile;

import game.GameController;
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

}
