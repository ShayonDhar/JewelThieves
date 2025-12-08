package game.highscore;

import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.TreeSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * Controller for the leaderboard UI. Displays top 10 high scores per level
 * with a blurred background and dark overlay for readability.
 *
 * @author Shayon Dhar
 * @version 1.0.0
 */
public class LeaderboardController {

    private static final String BACKGROUND_IMAGE_PATH = "resources/gamebackground2.jpg";
    private static final double BACKGROUND_BLUR_RADIUS = 3.0;
    private static final String OVERLAY_STYLE = "-fx-background-color: rgba(0,0,0,0.5);";
    private static final String PLACEHOLDER_STYLE = "-fx-text-fill: #ffd700; "
            + "-fx-font-size: 24px; -fx-font-weight: bold;";
    private static final String NO_SCORES_MESSAGE = "No high scores recorded for this level yet.";
    private static final String LEVEL_TITLE_FORMAT = "Level %d - Top 10 Scores";
    private static final int DEFAULT_LEVEL_IF_EMPTY = 1;

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

    @FXML private StackPane rootStackPane;
    @FXML private ComboBox<Integer> levelComboBox;
    @FXML private TableView<ScoreRow> leaderboardTable;
    @FXML private Label levelTitleLabel;

    private final HighScoreManager manager = new HighScoreManager();

    /**
     * Initializes the controller after FXML fields have been injected.
     * Sets up background, loads high score data, and configures level selection.
     */
    @FXML
    private void initialize() {
        setupBackground();
        loadAllHighScores();
        configureLevelComboBox();
        selectFirstAvailableLevel();
        setupLevelChangeListener();

        // Auto-refresh when window becomes visible/focused
        rootStackPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obsWin, oldWindow, newWindow) -> {
                    if (newWindow != null) {
                        newWindow.focusedProperty().addListener((obsFocus, wasFocused, isNowFocused) -> {
                            if (Boolean.TRUE.equals(isNowFocused)) {
                                refreshLeaderboard();
                            }
                        });
                    }
                });
            }
        });

        loadLevelScores(levelComboBox.getValue());
    }

    /**
     * Refreshes the leaderboard by reloading all high scores from disk
     * and updating the display.
     */
    @FXML
    public void refreshLeaderboard() {
        loadAllHighScores();

        // Refresh the ComboBox items
        configureLevelComboBox();

        // Reload the currently selected level
        Integer selectedLevel = levelComboBox.getValue();
        if (selectedLevel != null) {
            loadLevelScores(selectedLevel);
        }
    }

    /**
     * Loads all high score files and populates the level ComboBox with levels that have scores.
     */
    private void loadAllHighScores() {
        manager.loadAll();
    }

    /**
     * Configures the ComboBox with available levels. Ensures at least level 1 is shown.
     */
    private void configureLevelComboBox() {
        Set<Integer> levelsWithScores = manager.getLevelsWithScores();
        Set<Integer> levels = levelsWithScores.isEmpty()
                ? Set.of(DEFAULT_LEVEL_IF_EMPTY)
                : new TreeSet<>(levelsWithScores);

        ObservableList<Integer> levelItems = FXCollections.observableArrayList(levels);
        levelComboBox.setItems(levelItems);
    }

    /**
     * Selects the first level in the ComboBox (usually the lowest level).
     */
    private void selectFirstAvailableLevel() {
        levelComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Adds a listener to reload the leaderboard whenever the selected level changes.
     */
    private void setupLevelChangeListener() {
        levelComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        loadLevelScores(newValue);
                    }
                }
        );
    }

    /**
     * Loads and displays the top 10 high scores for the specified level.
     *
     * @param level the level number to display scores for
     */
    private void loadLevelScores(int level) {
        levelTitleLabel.setText(String.format(LEVEL_TITLE_FORMAT, level));

        LevelHighScoreTable table = manager.getHighScoreTable(level);
        var highScores = table.getHighScores();

        ObservableList<ScoreRow> rows = FXCollections.observableArrayList();
        int rank = 1;
        for (HighScoreEntry entry : highScores) {
            rows.add(ScoreRow.of(rank++, entry.getPlayerName(), entry.getScore(), entry.getTimestamp()));
        }

        leaderboardTable.setItems(rows);

        // Set a styled placeholder if no scores exist
        if (rows.isEmpty()) {
            Label placeholder = new Label(NO_SCORES_MESSAGE);
            placeholder.setStyle(PLACEHOLDER_STYLE);
            leaderboardTable.setPlaceholder(placeholder);
        }
    }

    /**
     * Closes the leaderboard window.
     */
    @FXML
    private void closeWindow() {
        if (rootStackPane != null && rootStackPane.getScene() != null) {
            rootStackPane.getScene().getWindow().hide();
        }
    }

    /**
     * Sets up a full-screen blurred background image with a semi-transparent dark overlay
     * to improve text readability.
     */
    private void setupBackground() {
        try (var is = getClass().getResourceAsStream(BACKGROUND_IMAGE_PATH)) {
            if (is == null) {
                return;
            }

            Image backgroundImage = new Image(is);
            ImageView backgroundView = new ImageView(backgroundImage);
            backgroundView.setPreserveRatio(false);
            backgroundView.setEffect(new GaussianBlur(BACKGROUND_BLUR_RADIUS));
            backgroundView.fitWidthProperty().bind(rootStackPane.widthProperty());
            backgroundView.fitHeightProperty().bind(rootStackPane.heightProperty());

            Pane overlay = new Pane();
            overlay.setStyle(OVERLAY_STYLE);
            overlay.setMouseTransparent(true);

            // Insert at bottom (behind all existing content) – works on ALL JavaFX versions
            rootStackPane.getChildren().add(0, backgroundView);
            rootStackPane.getChildren().add(1, overlay);

        } catch (Exception e) {
            System.out.println("Failed to load leaderboard background: " + e.getMessage());
        }
    }
}