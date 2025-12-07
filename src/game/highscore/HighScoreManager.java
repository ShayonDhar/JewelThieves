package game.highscore;

import java.io.*;
import java.util.*;

/**
 * Manages high score tables for all game levels.
 * This class provides centralized management of high score tables across
 * multiple levels, including automatic file persistence. High scores are
 * automatically saved when recorded and can be loaded from disk on demand.
 *
 * @author Shayon Dhar
 * @version 1.0.0
 */
public class HighScoreManager {

    private static final String HIGH_SCORE = "highscores";
    private static final String LEVEL = "level_";
    private static final String FAILED_TO_SAVE_HIGH_SCORE = "Failed to save high scores for level ";
    private static final String FAILED_LOAD = "Could not load high scores for level ";
    private static final String FAILED_SAVE = "Failed to save level ";
    private static final String SCORES = "_scores";
    private static final String SCORES_TXT = SCORES + ".txt";
    private static final String FAILED_TO_LOAD = "Failed to load ";
    private final Map<Integer, LevelHighScoreTable> levelTables;
    private final String saveDirectory;

    /**
     * Sets the highscore for this current game.
     */
    public HighScoreManager() {
        this(HIGH_SCORE);
    }

    /**
     * Creates a HighScoreManager with a custom save directory.
     *
     * @param saveDirectory the directory path where high score files will be stored
     * @throws NullPointerException if saveDirectory is null
     */
    public HighScoreManager(String saveDirectory) {
        this.levelTables = new HashMap<>();
        this.saveDirectory = saveDirectory;

        // Create directory if it doesn't exist
        File dir = new File(saveDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Gets the file path for a specific level's high scores.
     *
     * @param levelNumber the level number
     * @return the full file path for the level's high score file
     */
    private String getFilePath(int levelNumber) {
        return saveDirectory + File.separator + LEVEL + levelNumber + SCORES_TXT;
    }

    /**
     * Records a score for a player on a specific level.
     * The score is added to the appropriate level's high score table
     * if it qualifies.
     *
     * @param levelNumber the level number (must be positive)
     * @param playerName  the name of the player
     * @param score       the score to record
     * @return true if the score was added to the high score table
     * @throws NullPointerException if playerName is null
     */
    public boolean recordScore(int levelNumber, String playerName, int score) {
        LevelHighScoreTable table = levelTables.computeIfAbsent(
                levelNumber,
                LevelHighScoreTable::new
        );

        boolean added = table.addScore(playerName, score);

        if (added) {
            try {
                saveLevel(levelNumber);
            } catch (IOException e) {
                System.out.println(FAILED_TO_SAVE_HIGH_SCORE
                        + levelNumber + ": " + e.getMessage());
            }
        }

        return added;
    }

    /**
     * Gets the high score table for a specific level.
     * If the table is not currently loaded in memory, it is automatically
     * loaded from the corresponding file. If no file exists for the level,
     * a new empty table is created.
     *
     * @param levelNumber the level number
     * @return the high score table for the specified level (never null)
     */
    public LevelHighScoreTable getHighScoreTable(int levelNumber) {
        if (!levelTables.containsKey(levelNumber)) {
            LevelHighScoreTable table = new LevelHighScoreTable(levelNumber);
            try {
                table.loadFromFile(getFilePath(levelNumber));
            } catch (IOException e) {
                System.out.println(FAILED_LOAD
                        + levelNumber + ": " + e.getMessage());
            }
            levelTables.put(levelNumber, table);
        }
        return levelTables.get(levelNumber);
    }

    /**
     * Saves the high scores for a specific level to disk.
     *
     * @param levelNumber the level number
     * @throws IOException              if an I/O error occurs during saving
     * @throws IllegalArgumentException if no high score table exists for the level
     */
    public void saveLevel(int levelNumber) throws IOException {
        LevelHighScoreTable table = levelTables.get(levelNumber);
        if (table != null) {
            table.saveToFile(getFilePath(levelNumber));
        }
    }

    /**
     * Saves all currently loaded high score tables to disk.
     * Errors saving individual levels are logged but do not prevent
     * other levels from being saved.
     */
    public void saveAll() {
        for (Integer levelNumber : levelTables.keySet()) {
            try {
                saveLevel(levelNumber);
            } catch (IOException e) {
                System.out.println(FAILED_SAVE + levelNumber
                        + ": " + e.getMessage());
            }
        }
    }

    /**
     * Loads high scores for a specific level from disk.
     * If a table for this level is already loaded, it is replaced with
     * the data from the file.
     *
     * @param levelNumber the level number
     * @throws IOException if an I/O error occurs or the file format is invalid
     */
    public void loadLevel(int levelNumber) throws IOException {
        LevelHighScoreTable table = new LevelHighScoreTable(levelNumber);
        table.loadFromFile(getFilePath(levelNumber));
        levelTables.put(levelNumber, table);
    }

    /**
     * Loads all high score files from the save directory.
     * This method scans the save directory for all files matching the
     * file pattern and loads each one.
     */
    public void loadAll() {
        File dir = new File(saveDirectory);
        File[] files = dir.listFiles((d, name) ->
                name.startsWith(LEVEL) && name.endsWith(SCORES_TXT));

        if (files != null) {
            for (File file : files) {
                try {
                    // Extract level number from filename
                    String filename = file.getName();
                    String levelStr = filename.substring(6, filename.indexOf(SCORES));
                    int levelNumber = Integer.parseInt(levelStr);

                    loadLevel(levelNumber);
                } catch (Exception e) {
                    System.out.println(FAILED_TO_LOAD + file.getName()
                            + ": " + e.getMessage());
                }
            }
        }
    }

    /**
     * Displays the high score table for a specific level to standard output.
     *
     * @param levelNumber the level number
     */
    public void displayHighScores(int levelNumber) {
        LevelHighScoreTable table = getHighScoreTable(levelNumber);
        System.out.println(table);
    }

    /**
     * Gets all level numbers that have high score tables currently loaded.
     *
     * @return a set of level numbers with loaded high score tables
     */
    public Set<Integer> getLevelsWithScores() {
        return new HashSet<>(levelTables.keySet());
    }

    /**
     * Clears all high score tables and deletes all high score files.
     * This operation is irreversible and will permanently delete all
     * high score data.
     */
    public void clearAllScores() {
        // Delete all files
        File dir = new File(saveDirectory);
        File[] files = dir.listFiles((d, name) ->
                name.startsWith(LEVEL) && name.endsWith(SCORES_TXT));

        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }

        levelTables.clear();
    }

    /**
     * Clears the high score table for a specific level and deletes its file.
     * This operation is irreversible and will permanently delete the
     * high score data for the specified level.
     *
     * @param levelNumber the level number
     */
    public void clearLevelScores(int levelNumber) {
        levelTables.remove(levelNumber);
        File file = new File(getFilePath(levelNumber));
        if (file.exists()) {
            file.delete();
        }
    }
}
