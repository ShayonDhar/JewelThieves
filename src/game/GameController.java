package game;

import game.entity.Direction;
import game.entity.Entity;
import game.entity.Player;
import game.entity.npc.NPC;
import game.item.Item;
import game.item.Loot;
import game.item.Clock;
import game.item.Lever;
import game.item.Door;
import game.item.Gate;
import game.level.Level;
import game.level.LevelLoader;
import game.level.Tile;
import game.save.GameSaveManager;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
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
    private static final int START_TIME_REMAINING = 30;
    private static Timeline tickTimeline;
    @FXML private static Text gameOverText;
    @FXML private static Text levelCompleteText;
    private boolean isLevelComplete = false;
    public TilePane boardTilePane;
    public Level level;
    public Player player;
    public Item [][] itemGrid;
    public TextArea textArea;
    public static boolean tickPlaying = false;
    private GameSaveManager saveManager;
    private int score = 0;
    private final ArrayList<ExplosionEffect> activeExplosions = new ArrayList<>();

    private int timeRemaining = START_TIME_REMAINING; // TODO Read time from the level file

    /**
     * Method that initialises the game.
     */
    @FXML
    public void initialize() {

        // New timeline with one keyframe that triggers the tick method every half a second.
        tickTimeline = new Timeline(new KeyFrame(
                Duration.millis(TICK_DURATION), event -> tick()));
        tickTimeline.setCycleCount(Animation.INDEFINITE); // Loop indefinitely

        // Reading a text file
        LevelLoader loader = new LevelLoader(this);
        level = loader.load("LevelOne.txt");

        // Setting the text area
        textArea.setText("Time: " + timeRemaining + "s\nScore: " + score);
        textArea.setEditable(false);

        // Preparing the GAME-OVER message for the static environment
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

        // Setting the player, items from game save manager
        player = level.getPlayer();
        itemGrid = level.getItemsGrid();
        saveManager = new GameSaveManager(this);

        // Drawing the game
        drawGame();
    }

    /**
     * Updates the game state and redraws the scene.
     * Updates periodically to update the entity positions, the state of items, and the game time
     */
    public void tick() {

        // TODO Tick NPCs, bombs + time (Just NPCs for now)
        level.updateLevel(1);

        // Check for loot collection
        Item item = level.getItemAt(player.getY(), player.getX());
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
                isLevelComplete = true;
                editTextArea();
                levelCompleted();
            }
        }

        // Controlling the game time
        if (timeRemaining == 0 && !isLevelComplete) {
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

            // TODO add comments explaining code on this please
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

            Item item = level.getItemAt(player.getY(), player.getX());
            if (item instanceof Loot loot) {
                addScore(loot.getLootType().getValue());
                level.removeItemFromGrid(player.getY(), player.getX());
            }

            // Redraw the scene after moving
            drawGame();
        }

        // Marking the event as being "done dealt with"
        event.consume();
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
     * Method to stop the timeline and suggest that the level has been completed
     * Occurs when the door of the level is unlocked
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

        // TODO: Create a finish level screen OR Load next level

        /*
        Note from Anton:
        Tried doing Level Complete message like the Game-over message but since its in a static environment
        I had to set up a stack pane that holds a border pane (view initialize()), and this didnt allow me
        to set up two different overlays
         */
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

    /**
     * Handles the Save button action.
     * Saves the current game state to a file.
     */
    @FXML
    public void buttonSaveAction() {
        try {
            String filename = saveManager.generateSaveFilename();

            boolean success = saveManager.save(level, filename);

            Alert alert;
            if (success) {
                // Show success message
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Game Saved");
                alert.setHeaderText(null);
                alert.setContentText("Game saved successfully as " + filename);
            } else {
                // Show error message
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Save Failed");
                alert.setHeaderText(null);
                alert.setContentText("Failed to save the game. Please try again.");
            }
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

    /**
     * Handles the Load button action.
     * Shows a dialog to select a save file and loads it.
     */
    @FXML
    public void buttonLoadAction() {
        try {
            // Get list of available save files
            String[] saveFiles = saveManager.listSaves();

            if (saveFiles.length == 0) {
                // No saves found
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("No Saves Found");
                alert.setHeaderText(null);
                alert.setContentText("No save files found. Start a new game first!");
                alert.showAndWait();
                return;
            }

            // Show choice dialog to select a save file
            ChoiceDialog<String> dialog = new ChoiceDialog<>(saveFiles[0], saveFiles);
            dialog.setTitle("Load Game");
            dialog.setHeaderText("Select a save file to load");
            dialog.setContentText("Save file:");

            Optional<String> result = dialog.showAndWait();

            if (result.isPresent()) {
                String selectedFile = result.get();

                // Stop the game tick before loading
                if (tickPlaying) {
                    tickTimeline.stop();
                    tickPlaying = false;
                }

                // Load the game
                Level loadedLevel = saveManager.load(selectedFile);

                if (loadedLevel != null) {
                    // Update the level and player references
                    level = loadedLevel;
                    player = level.getPlayer();
                    itemGrid = level.getItemsGrid();

                    // Redraw the game
                    drawGame();

                    // Show success message
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Game Loaded");
                    alert.setHeaderText(null);
                    alert.setContentText("Game loaded successfully from " + selectedFile);
                    alert.showAndWait();

                } else {
                    // Load failed
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Load Failed");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to load the game. The save file may be corrupted.");
                    alert.showAndWait();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Load Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while loading: " + e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Quick save method - saves to save1.txt immediately without dialog.
     */
    @FXML
    public void buttonQuickSaveAction() {
        try {
            boolean success = saveManager.save(level, "save1.txt");

            if (success) {
                System.out.println("Quick saved to save1.txt");
                // Optional: Show a brief notification
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Quick load method - loads save1.txt immediately without dialog.
     */
    @FXML
    public void buttonQuickLoadAction() {
        try {
            if (saveManager.saveExists("save1.txt")) {
                // Stop the game tick before loading
                if (tickPlaying) {
                    tickTimeline.stop();
                    tickPlaying = false;
                }

                Level loadedLevel = saveManager.load("save1.txt");

                if (loadedLevel != null) {
                    level = loadedLevel;
                    player = level.getPlayer();
                    itemGrid = level.getItemsGrid();
                    drawGame();
                    System.out.println("Quick loaded from save1.txt");
                }
            } else {
                System.out.println("No quick save found (save1.txt)");
            }
        } catch (Exception e) {
            e.printStackTrace();
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
}
