package game;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Class that controls the main menu display.
 *
 * @author Antoni Wachowiak
 * @version 1.0
 */
public class MenuController {

    // Constants for the GameGraphics window dimensions
    private static final int WINDOW_WIDTH = 950;
    private static final int WINDOW_HEIGHT = 700;

    private Stage stage;

    @FXML
    private StackPane rootStackPane;

    /**
     * Initialize method called automatically when FXML loads.
     */
    @FXML
    public void initialize() {
        setupBackground();
    }

    /**
     * Called by Main to give the menu controller access to the stage.
     *
     * @param stage stage to be given control by Main
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Handles the Load Game button from the main menu.
     */
    @FXML
    public void buttonLoadGame() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("playerProfile/ProfileSelectionLoad.fxml"));
            Pane root = loader.load();


            Stage selectionStage = new Stage();
            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            selectionStage.setScene(scene);
            selectionStage.setTitle("Select Profile to Load");
            selectionStage.show();


        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to open profile selection: " + e.getMessage());
            alert.showAndWait();
        }
    }
    @FXML
    public void buttonProfileManager() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("playerProfile/ProfileManager.fxml"));
            Pane root = loader.load();


            Stage selectionStage = new Stage();
            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            selectionStage.setScene(scene);
            selectionStage.setTitle("Edit Profiles");
            selectionStage.show();


        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to open profile manager: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void buttonLeaderboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("highscore/Leaderboard.fxml"));
            Pane root = loader.load();

            Stage leaderboardStage = new Stage();
            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            leaderboardStage.setScene(scene);
            leaderboardStage.setTitle("Leaderboard - Jewel Thieves");
            //leaderboardStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/resources/icon.png"))));
            leaderboardStage.setResizable(false);
            leaderboardStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to open leaderboard: " + e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Loads the game scene and tells it to load a specific save file.
     */
    public void loadGameWithSave(String saveFilename) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameGraphics.fxml"));
            Pane root = loader.load();

            GameController gameController = loader.getController();

            gameController.loadSaveFile(saveFilename);

            Stage gameStage = new Stage();

            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

            scene.setOnKeyPressed(gameController::onKeyPressed);
            gameStage.setTitle("Jewel Thieves Group 01 - Game");

            gameStage.setScene(scene);
            gameStage.show();

            // Close the menu stage to avoid multiple windows
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to start game: " + e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Button-click method to load a new game.
     */
    @FXML
    public void buttonNewGame() {

        try {
            // Load FXML using FXMLLoader instance (not static)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("playerProfile/ProfileSelection.fxml"));
            Pane root = loader.load();
            loader.getController();

            // New stage
            Stage gameStage = new Stage();

            // Load the scene onto the scene
            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);


//            // Adding the css class for setting the lever colour
//            scene.getStylesheets().add(getClass().getResource("lever-colour.css").toExternalForm());

            // Setting the scene and displaying it
            gameStage.setScene(scene);
            gameStage.setTitle("Jewel Thieves Group 01 - Game");
            gameStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Button action to close the stage.
     */
    @FXML
    public void buttonQuit() {
        System.exit(0);
    }

    /**
     * Sets up the background image with blur and gradient overlays.
     */
    private void setupBackground() {
        try {
            String path = "resources/gamebackground2.jpg";
            var inputStream = getClass().getResourceAsStream(path);

            if (inputStream == null) {
                return;
            }

            Image backgroundImage = new Image(inputStream);

            ImageView bgView = new ImageView(backgroundImage);
            bgView.setPreserveRatio(false);
            bgView.fitWidthProperty().bind(rootStackPane.widthProperty());
            bgView.fitHeightProperty().bind(rootStackPane.heightProperty());

            GaussianBlur blur = new GaussianBlur();
            blur.setRadius(2);
            bgView.setEffect(blur);

            // Create gradient overlays
            Pane topGradient = new Pane();
            topGradient.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, rgba(0, 0, 0, 0.2), transparent);"
            );
            topGradient.prefWidthProperty().bind(rootStackPane.widthProperty());
            topGradient.setPrefHeight(150);
            topGradient.setMouseTransparent(true);

            Pane bottomGradient = new Pane();
            bottomGradient.setStyle(
                    "-fx-background-color: linear-gradient(to top, rgba(0, 0, 0, 0.2), transparent);"
            );
            bottomGradient.prefWidthProperty().bind(rootStackPane.widthProperty());
            bottomGradient.setPrefHeight(150);
            bottomGradient.setMouseTransparent(true);

            bottomGradient.translateYProperty().bind(
                    rootStackPane.heightProperty().subtract(bottomGradient.prefHeightProperty())
            );

            // Add in order: background image, top gradient, bottom gradient
            rootStackPane.getChildren().add(0, bgView);
            rootStackPane.getChildren().add(1, topGradient);
            rootStackPane.getChildren().add(2, bottomGradient);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}