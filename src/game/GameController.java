package game;

import game.entity.Direction;
import game.entity.Entity;
import game.entity.Player;
import game.entity.npc.NPC;
import game.highscore.HighScoreManager;
import game.item.Clock;
import game.item.Door;
import game.item.Gate;
import game.item.Item;
import game.item.Lever;
import game.item.Loot;
import game.level.Level;
import game.level.LevelLoader;
import game.level.Tile;
import game.playerProfile.ProfileController;
import game.playerProfile.ProfileSaveController;
import game.playerProfile.ProfileSession;
import game.save.GameSaveManager;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Class that links the MainApplication to the SceneBuilder FXML controlling aspect.
 *
 * @author Antoni Wachowiak
 * @version $1.0.
 */
public class GameController {

    private static final String UNHANDLED_KEY = "Unhandled key: ";
    private static final int TICK_DURATION = 1000;
    private static final int TIME_BONUS_MULTIPLIER = 10; // Points per second remaining

    private static Timeline tickTimeline;

    @FXML private static Text gameOverText;
    @FXML private static Text levelCompleteText;
    public TilePane boardTilePane;
    public Level level;
    private int startTimeRemaining;
    public Player player;
    public Item [][] itemGrid;
    public TextArea textArea;
    public static boolean tickPlaying = false;
    public GameSaveManager saveManager;
    private HighScoreManager highScoreManager;
    private int score = 0;
    private int currentLevelNumber = 1;
    private String currentPlayerName = ProfileSession.getCurrentName();
    private final ArrayList<ExplosionEffect> activeExplosions = new ArrayList<>();

    private int timeRemaining = startTimeRemaining;

    @FXML
    private StackPane rootStackPane;

    /**
     * Method that initialises the game.
     */
    @FXML
    public void initialize() {
        highScoreManager = new HighScoreManager();

        setupBackground();

        // New timeline with one keyframe that triggers the tick method every half a second.
        tickTimeline = new Timeline(new KeyFrame(
                Duration.millis(TICK_DURATION), event -> tick()));
        tickTimeline.setCycleCount(Animation.INDEFINITE); // Loop indefinitely

        // Setting the text area
        textArea.setText("Time: " + timeRemaining + "s\nScore: " + score);
        textArea.setEditable(false);

        // Preparing the GAME-OVER and LEVEL_COMPLETE message for the static environment
        gameOverText = new Text("GAME OVER");
        gameOverText.setStyle("-fx-font-size: 75px; -fx-fill: white; -fx-font-weight: bold; -fx-stroke: "
                + "black; -fx-stroke-width: 5px");
        levelCompleteText = new Text("LEVEL COMPLETE");
        levelCompleteText.setStyle("-fx-font-size: 75px; -fx-fill: white; -fx-font-weight: bold; -fx-stroke: "
                + "black; -fx-stroke-width: 5px");
        gameOverText.setVisible(false);
        levelCompleteText.setVisible(false);
        // Hiding GAME-OVER for now
        StackPane overlay = new StackPane(gameOverText, levelCompleteText); // Stack Pane that contains the BorderPane
        ((BorderPane) boardTilePane.getParent()).setCenter(new StackPane(boardTilePane, overlay));
        boardTilePane.setOnKeyPressed(this::onKeyPressed);
        boardTilePane.setFocusTraversable(true);
        boardTilePane.requestFocus();
    }

    /**
     * Level loader, loads the game.
     *
     * @param levelNumber the level that is being loaded.
     */
    public void loadLevel(int levelNumber) {
        if (saveManager == null) {
            saveManager = new GameSaveManager(this);
        }

        LevelLoader loader = new LevelLoader(this);
        String filename = "Level" + levelNumber + ".txt";

        level = loader.load(filename);
        currentLevelNumber = levelNumber;

        player = level.getPlayer();
        itemGrid = level.getItemsGrid();

        // FIXED: timer comes from the Level object
        startTimeRemaining = level.getRemainingTime();
        timeRemaining = startTimeRemaining;

        score = 0;

        saveManager = new GameSaveManager(this);

        drawGame();
    }


    /**
     * Updates the game state and redraws the scene.
     * Updates periodically to update the entity positions, the state of items, and the game time
     */
    public void tick() {
        level.updateLevel(1);

        // Controlling the game time
        if (timeRemaining == 0) {
            editTextArea();
            gameOver();
        } else {
            timeRemaining--;
            editTextArea();
        }

        // Redraw the whole canvas
        drawGame();
    }

    /**
     * Method to draw the game onto the FXML GUI.
     */
    public void drawGame() {
        // Clear the tilePane i.e. everything from the tiles
        boardTilePane.getChildren().clear();

        // 2D array that stores the tiles
        StackPane[][] tiles = new StackPane[level.getLevelWidth()][level.getLevelHeight()];

        // Looping through height/width of tilePane
        for (int y = 0; y < level.getLevelHeight(); y++) {
            for (int x = 0; x < level.getLevelWidth(); x++) {
                // Gets the tile object from the level, and converts the colours/item/entity to a StackPane
                Tile tile = level.getTile(y, x);
                StackPane tileStack = tile.toStackPane();
                tiles[x][y] = tileStack;

                Item item = itemGrid[y][x];
                if (item != null && item.getSprite() != null) {
                    tileStack.getChildren().add(item.getSprite());
                }

                // Displaying the tile pane
                boardTilePane.getChildren().add(tileStack);
            }
        }
        // Draw NPCs at their current tiles
        for (Entity entities : level.getEntities()) {
            if (entities instanceof NPC npc) {
                int entityX = entities.getX();
                int entityY = entities.getY();
                tiles[entityX][entityY].getChildren().add(npc.getSprite());
            }
        }

        // Displaying the player at their current tile
        tiles[player.getX()][player.getY()].getChildren().add(player.getSprite());

        // Bomb Animation
        List<ExplosionEffect> toRemove = new ArrayList<>();

        for (ExplosionEffect effect : activeExplosions) {
            StackPane tileStack = tiles[effect.x][effect.y];

            long remaining = effect.endTime - System.currentTimeMillis();

            if (remaining <= 0) {
                toRemove.add(effect);
            } else {
                StackPane overlay = new StackPane();
                overlay.setStyle("-fx-background-color: rgba(255, 120, 0, 1); -fx-background-radius: 50%;");
                overlay.setPrefSize(tileStack.getWidth(), tileStack.getHeight());

                tileStack.getChildren().add(overlay);

                FadeTransition fade = new FadeTransition(Duration.millis(remaining), overlay);
                fade.setFromValue(1.0);
                fade.setToValue(0.0);
                fade.play();
            }
        }
        activeExplosions.removeAll(toRemove);
    }

    /**
     * method is responsible for triggering explosion effects on specific tiles in the game.
     *
     * @param tiles a list of TilePosition objects.
     */
    public void showExplosionAtTiles(List<TilePosition> tiles) {
        long duration = 250;
        for (TilePosition pos : tiles) {
            activeExplosions.add(new ExplosionEffect(pos.x(), pos.y(), duration));
        }
    }

    /**
     * Method to show the time remaining and the score.
     */
    public void editTextArea() {
        textArea.setText("Time: " + timeRemaining + "s\nScore: " + score);
    }

    /**
     * Method to start the tick timeline.
     */
    @FXML
    public void buttonStartAction() {
        tickTimeline.play();
        tickPlaying = true;
    }

    /**
     * Method to stop the tick timeline when the STOP button is pressed.
     */
    @FXML
    public void buttonStopAction() {
        tickTimeline.stop();
        tickPlaying = false;
    }

    /**
     * Method to read keyboard input.
     *
     * @param event key that is pressed on the keyboard.
     */
    public void onKeyPressed(KeyEvent event) {
        // Read the key input as a direction within the game
        switch (event.getCode()) {
            case W -> player.setDirection(Direction.NORTH);
            case A -> player.setDirection(Direction.WEST);
            case S -> player.setDirection(Direction.SOUTH);
            case D -> player.setDirection(Direction.EAST);
            default -> System.out.println(UNHANDLED_KEY + event.getCode());
        }

        Tile nextTile = level.getTile(player.getY(), player.getX());

        if (nextTile.hasGate()) {
            Gate gate = nextTile.getGate();

            if (gate.isOn) {
                return;
            }
        }

        // Checking if tick timeline is playing
        if (tickPlaying && timeRemaining != 0) {
            // Now perform the move based on the direction we just set
            player.move();

            // COLLECTING ITEMS
            Item item = level.getItemAt(player.getY(), player.getX());

            // Loot
            if (item instanceof Loot loot) {
                addScore(loot.getLootType().getValue());
                loot.isOn = false;
                level.removeItemFromGrid(player.getY(), player.getX());
            }

            // Check for clock collection
            if (item instanceof Clock clock) {
                timeRemaining += clock.getTimeBonus();
                clock.isOn = false;
                level.removeItemFromGrid(player.getY(), player.getX());
            }

            // Check for lever collection
            if (item instanceof Lever lever) {
                level.unlockGates(lever.getColour());
                lever.isOn = false;
                level.removeItemFromGrid(player.getY(), player.getX());
            }

            // Check for whether door can be unlocked
            if (level.allLootAndLeversCollected()) {
                if (item instanceof Door door) {
                    door.isOn = false;
                    level.removeItemFromGrid(player.getY(), player.getX());
                    handleLevelComplete();
                }
            }

            // Redraw the scene after moving
            drawGame();
        }

        // Marking the event as being "done dealt with"
        event.consume();
    }

    /**
     * Handles level completion: calculates final score, records high score,
     * and displays results.
     */
    private void handleLevelComplete() {
        tickPlaying = false;
        tickTimeline.stop();

        // Calculate final score with time bonus
        int finalScore = calculateFinalScore();

        // Record the high score
        boolean madeHighScore = highScoreManager.recordScore(
                currentLevelNumber,
                currentPlayerName,
                finalScore
        );

        // Update display
        editTextArea();
        levelCompleteText.setVisible(true);

        // Show high score notification
        if (madeHighScore) {
            showHighScoreAlert(finalScore);
        } else {
            showLevelCompleteAlert(finalScore);
        }
        FadeTransition fade = new FadeTransition(Duration.seconds(1.5), levelCompleteText);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);

        // After fade-out, hide text and load next level
        fade.setOnFinished(e -> {
            levelCompleteText.setVisible(false);
            loadLevel(currentLevelNumber + 1);
        });

        fade.play();
    }

    /**
     * Calculates the final score including time bonus.
     *
     * @return the final score with time bonus applied
     */
    private int calculateFinalScore() {
        int timeBonus = timeRemaining * TIME_BONUS_MULTIPLIER;
        return score + timeBonus;
    }

    /**
     * Shows an alert when the player achieves a high score.
     *
     * @param finalScore the player's final score
     */
    private void showHighScoreAlert(int finalScore) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("New High Score!");
        alert.setHeaderText("Congratulations!");
        alert.setContentText(
                "You made the high score table!\n"
                        + "Final Score: " + finalScore + "\n\n"
                        + getHighScoreTableString()
        );
        alert.showAndWait();
    }

    /**
     * Shows an alert when the player completes the level but doesn't make high score.
     *
     * @param finalScore the player's final score
     */
    private void showLevelCompleteAlert(int finalScore) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Level Complete");
        alert.setHeaderText("Well Done!");
        alert.setContentText(
                "Level completed!\n"
                        + "Final Score: "
                        + finalScore + "\n\n"
        );
        alert.showAndWait();
    }

    /**
     * Gets a formatted string of the current level's high score table.
     *
     * @return formatted high score table string
     */
    private String getHighScoreTableString() {
        return highScoreManager.getHighScoreTable(currentLevelNumber).toString();
    }

    /**
     * Displays the high score table for the current level.
     */
    @FXML
    public void buttonViewHighScoresAction() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("High Scores - Level " + currentLevelNumber);
        alert.setHeaderText(null);
        alert.setContentText(getHighScoreTableString());
        alert.showAndWait();
    }

    /** Method to stop timeline and indicate "game-over".
     * Called when the player dies (Flying Assassin, timer expires, etc.)
     */
    public static void gameOver() {
        tickPlaying = false;
        tickTimeline.stop();
        gameOverText.setVisible(true);
    }

    /**
     * Checks whether the game is over.
     *
     * @return the current game state
     */
    private boolean isGameOver() {
        return gameOverText != null && gameOverText.isVisible();
    }

    /**
     * Method to stop the timeline and suggest that the level has been completed
     * Occurs when the door of the level is unlocked.
     */
    public static void levelCompleted() {
        tickPlaying = false;
        tickTimeline.stop();
        levelCompleteText.setVisible(true);
    }

    /** Method to indicate the level has finished.
     * Called when the player reaches an exit AND all loot + levers collected
     */
    public void finishLevel() {
        tickTimeline.stop();
        System.out.println("LEVEL COMPLETE");
    }

    /** Method to increase the score.
     *
     * @param score value of score
     */
    public void addScore(int score) {
        this.score += score;
    }

    public int getScore() {
        return score;
    }

    public void setCurrentLevelNumber(int levelNumber) {
        this.currentLevelNumber = levelNumber;
    }

    public void setCurrentPlayerName(String playerName) {
        this.currentPlayerName = playerName;
    }

    /**
     * Handles the Save button action.
     * Saves the current game state to a file.
     */
    @FXML
    public void buttonSaveAction() {
        try {
            if (isGameOver()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cannot Save");
                alert.setHeaderText(null);
                alert.setContentText("The game cannot be saved because it is over.");
                alert.showAndWait();
                return;
            }

            // Make sure saveManager exists
            if (saveManager == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Save Error");
                alert.setHeaderText(null);
                alert.setContentText("Save system not initialized.");
                alert.showAndWait();
                return;
            }

            String filename = saveManager.generateSaveFilename();
            boolean success = saveManager.save(level, filename);

            Alert alert = getAlert(success, filename);
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while saving: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private static Alert getAlert(boolean success, String filename) {
        Alert alert;
        if (success) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Saved");
            alert.setHeaderText(null);
            alert.setContentText("Game saved successfully as " + filename);
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save Failed");
            alert.setHeaderText(null);
            alert.setContentText("Failed to save the game. Please try again.");
        }
        return alert;
    }

    /**
     * Handles the Load button action.
     * Shows a dialog to select a save file and loads it.
     */
    @FXML
    public void buttonLoadAction() {
        try {
            // Load the Profile Selection screen for LOAD mode
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("playerProfile/ProfileSelectionLoad.fxml")
            );

            Pane root = loader.load();

            // Get controller and inject the GameController for callbacks
            ProfileSaveController controller = loader.getController();
            controller.setGameController(this);

            // Create a popup stage for load selection
            Stage profileStage = new Stage();
            Scene scene = new Scene(root, 450, 300);
            profileStage.setScene(scene);
            profileStage.setTitle("Load Save File");
            profileStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Load Error");
            alert.setHeaderText(null);
            alert.setContentText("Could not open profile selection screen: " + e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Loads a specific save file when starting from the main menu.
     * Called by MenuController after the scene is loaded.
     *
     * @param filename the name of the level file to be loaded
     */
    public void loadSaveFile(String filename) {
        try {
            if (saveManager == null) {
                saveManager = new GameSaveManager(this);
            }
            Level loadedLevel = saveManager.load(filename);

            if (loadedLevel != null) {
                level = loadedLevel;
                player = level.getPlayer();
                itemGrid = level.getItemsGrid();
                drawGame();

                System.out.println("Loaded save: " + filename);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Load Failed");
                alert.setHeaderText(null);
                alert.setContentText("Failed to load the save file.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupBackground() {
        try {
            String path = "resources/gamebackground.jpg";
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
            blur.setRadius(10);
            bgView.setEffect(blur);

            // Create gradient overlays
            Pane topGradient = new Pane();
            topGradient.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, rgba(0, 0, 0, 0.4), transparent);"
            );
            topGradient.prefWidthProperty().bind(rootStackPane.widthProperty());
            topGradient.setPrefHeight(150);
            topGradient.setMouseTransparent(true);

            Pane bottomGradient = new Pane();
            bottomGradient.setStyle(
                    "-fx-background-color: linear-gradient(to top, rgba(0, 0, 0, 0.8), transparent);"
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

    public void setSaveManager(GameSaveManager sm) {
        this.saveManager = sm;
    }

    public void loadSavedLevel(Level savedLevel) {
        if (savedLevel == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Could not load save.");
            alert.setContentText("The save file returned no level data.");
            alert.showAndWait();
            return;
        }

        // SaveManager MUST exist when loading a save
        if (saveManager == null) {
            saveManager = new GameSaveManager(this);
        }

        this.level = savedLevel;
        this.player = level.getPlayer();
        this.itemGrid = level.getItemsGrid();

        timeRemaining = startTimeRemaining;
        score = 0;

        drawGame();
    }

    @FXML
    private void buttonQuitAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Unsaved Progress");
        alert.setHeaderText("Ensure your game is saved.");
        alert.setContentText("Any unsaved progress will be lost. Return to main menu?");

        ButtonType menuButton = new ButtonType("Continue");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(menuButton, cancelButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == menuButton) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("MenuGraphics.fxml"));
                    Pane root = loader.load();

                    Stage stage = (Stage) textArea.getScene().getWindow();
                    Scene scene = new Scene(root, 950, 700);
                    stage.setScene(scene);
                    stage.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}