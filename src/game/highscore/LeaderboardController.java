package game.highscore;

import game.highscore.HighScoreEntry;
import game.highscore.HighScoreManager;
import game.highscore.LevelHighScoreTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import java.time.LocalDateTime;
import java.util.*;

public class LeaderboardController {

    @FXML private StackPane rootStackPane;
    @FXML private TableView<LeaderboardEntry> leaderboardTable;
    @FXML private TableColumn<LeaderboardEntry, Integer> rankColumn;
    @FXML private TableColumn<LeaderboardEntry, String> playerColumn;
    @FXML private TableColumn<LeaderboardEntry, Integer> scoreColumn;
    @FXML private TableColumn<LeaderboardEntry, Integer> levelColumn;

    // Simple POJO for the table
    public static class LeaderboardEntry {
        private final int rank;
        private final String player;
        private final int score;
        private final int level;

        public LeaderboardEntry(int rank, String player, int score, int level) {
            this.rank = rank;
            this.player = player;
            this.score = score;
            this.level = level;
        }

        public int getRank() { return rank; }
        public String getPlayer() { return player; }
        public int getScore() { return score; }
        public int getLevel() { return level; }
    }

    // Temporary class to track each player's best progress
    private static class PlayerProgress {
        String player;
        int maxLevel = 0;
        int bestScoreOnMaxLevel = 0;
        LocalDateTime timestamp = null;

        PlayerProgress(String player, int maxLevel, int bestScoreOnMaxLevel, LocalDateTime timestamp) {
            this.player = player;
            this.maxLevel = maxLevel;
            this.bestScoreOnMaxLevel = bestScoreOnMaxLevel;
            this.timestamp = timestamp;
        }
    }

    @FXML
    private void initialize() {
        setupBackground();

        // Table setup
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        playerColumn.setCellValueFactory(new PropertyValueFactory<>("player"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        levelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));

        rankColumn.setStyle("-fx-alignment: CENTER;");
        scoreColumn.setStyle("-fx-alignment: CENTER;");
        levelColumn.setStyle("-fx-alignment: CENTER;");

        // Placeholder when empty
        Label placeholder = new Label("No high scores recorded yet.");
        placeholder.setStyle("-fx-text-fill: #ffd700; -fx-font-size: 28px; -fx-font-weight: bold;");
        leaderboardTable.setPlaceholder(placeholder);

        // === REAL LEADERBOARD LOGIC ===
        HighScoreManager manager = new HighScoreManager(); // uses "./highscores" folder
        manager.loadAll();

        Map<String, PlayerProgress> progressMap = new HashMap<>();

        for (int levelNum : manager.getLevelsWithScores()) {
            LevelHighScoreTable table = manager.getHighScoreTable(levelNum);
            for (HighScoreEntry entry : table.getHighScores()) {
                String player = entry.getPlayerName();
                int score = entry.getScore();
                LocalDateTime ts = entry.getTimestamp();

                progressMap.compute(player, (k, v) -> {
                    if (v == null) {
                        return new PlayerProgress(player, levelNum, score, ts);
                    }

                    boolean update = false;
                    if (levelNum > v.maxLevel) {
                        update = true;
                    } else if (levelNum == v.maxLevel && score > v.bestScoreOnMaxLevel) {
                        update = true;
                    } else if (levelNum == v.maxLevel && score == v.bestScoreOnMaxLevel
                            && (v.timestamp == null || ts.isBefore(v.timestamp))) {
                        update = true;
                    }

                    if (update) {
                        v.maxLevel = levelNum;
                        v.bestScoreOnMaxLevel = score;
                        v.timestamp = ts;
                    }
                    return v;
                });
            }
        }

        List<PlayerProgress> list = new ArrayList<>(progressMap.values());
        list.sort((a, b) -> {
            if (b.maxLevel != a.maxLevel) {
                return Integer.compare(b.maxLevel, a.maxLevel);
            }
            if (b.bestScoreOnMaxLevel != a.bestScoreOnMaxLevel) {
                return Integer.compare(b.bestScoreOnMaxLevel, a.bestScoreOnMaxLevel);
            }
            if (a.timestamp == null) return 1;
            if (b.timestamp == null) return -1;
            return a.timestamp.compareTo(b.timestamp);
        });

        // Build final list
        ObservableList<LeaderboardEntry> data = FXCollections.observableArrayList();
        int rank = 1;
        for (PlayerProgress pp : list) {
            data.add(new LeaderboardEntry(rank++, pp.player, pp.bestScoreOnMaxLevel, pp.maxLevel));
        }

        leaderboardTable.setItems(data);
    }

    @FXML
    private void closeWindow() {
        rootStackPane.getScene().getWindow().hide();
    }

    private void setupBackground() {
        try {
            var inputStream = getClass().getResourceAsStream("resources/gamebackground2.jpg");
            if (inputStream == null) return;

            Image backgroundImage = new Image(inputStream);
            ImageView bgView = new ImageView(backgroundImage);
            bgView.setPreserveRatio(false);
            bgView.fitWidthProperty().bind(rootStackPane.widthProperty());
            bgView.fitHeightProperty().bind(rootStackPane.heightProperty());

            GaussianBlur blur = new GaussianBlur(2);
            bgView.setEffect(blur);

            // Dark overlay so text is readable
            Pane overlay = new Pane();
            overlay.setStyle("-fx-background-color: rgba(0,0,0,0.45);");
            overlay.setMouseTransparent(true);

            rootStackPane.getChildren().addAll(bgView, overlay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
