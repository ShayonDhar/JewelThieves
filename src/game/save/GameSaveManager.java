package game.save;

import game.GameController;
import game.level.Level;
import game.level.LevelLoader;
import game.level.Tile;
import game.playerProfile.ProfileSession;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Manages saving and loading of game state.
 * Handles file I/O operations for persisting game progress.
 * Works in conjunction with LevelLoader to restore saved games.
 *
 * @author Shayon Dhar
 * @version 1.0.0
 */
public class GameSaveManager {

    private static final String SAVE_DIRECTORY = "saves/";
    private final LevelLoader levelLoader;

    /**
     * Constructs a new GameSaveManager.
     *
     * @param controller the game controller for loading saved games
     */
    public GameSaveManager(GameController controller) {
        this.levelLoader = new LevelLoader(controller);
        ensureSaveDirectoryExists();
    }

    /**
     * Creates the save directory if it doesn't exist.
     */
    private void ensureSaveDirectoryExists() {
        File saveDir = new File(SAVE_DIRECTORY);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
    }

    /**
     * Saves the current game state to a file.
     *
     * @param level the current level to save
     * @param filename the name of the save file (without path)
     * @return true if save was successful, false otherwise
     */
    public boolean save(Level level, String filename) {
        String fullPath = SAVE_DIRECTORY + filename;

        try (PrintWriter writer = new PrintWriter(new FileWriter(fullPath))) {

            // Write level dimensions
            writer.println(level.getLevelWidth() + " " + level.getLevelHeight());

            // Write tile grid
            GameSaveWriter.writeTileGrid(writer, level);

            // Write remaining time
            writer.println("TIME " + level.getRemainingTime());

            // Write entities (Player and NPCs)
            GameSaveWriter.writeEntities(writer, level);

            // Write items
            GameSaveWriter.writeItems(writer, level);

            // Write exit tiles
            GameSaveWriter.writeExitTiles(writer, level);

            System.out.println("Game saved successfully to: " + fullPath);
            return true;

        } catch (IOException e) {
            System.out.println("Error saving game: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Loads a saved game from a file.
     * Reuses the existing LevelLoader to parse the save file.
     *
     * @param filename the name of the save file to load
     * @return the loaded Level object, or null if loading failed
     */
    public Level load(String filename) {
        String fullPath = SAVE_DIRECTORY + filename;

        File saveFile = new File(fullPath);
        if (!saveFile.exists()) {
            System.out.println("Save file not found: " + fullPath);
            return null;
        }

        System.out.println("Loading game from: " + fullPath);
        return levelLoader.load(fullPath);
    }

    /**
     * Gets a list of all available save files.
     *
     * @return array of save file names, or empty array if none exist
     */
    public String[] listSaves() {
        File saveDir = new File(SAVE_DIRECTORY);

        if (!saveDir.exists() || !saveDir.isDirectory()) {
            return new String[0];
        }

        File[] files = saveDir.listFiles((dir, name) -> name.endsWith(".txt"));

        if (files == null || files.length == 0) {
            return new String[0];
        }

        String[] saveNames = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            saveNames[i] = files[i].getName();
        }

        return saveNames;
    }

    /**
     * Deletes a save file.
     *
     * @param filename the name of the save file to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteSave(String filename) {
        File saveFile = new File(SAVE_DIRECTORY + filename);
        if (saveFile.exists()) {
            boolean deleted = saveFile.delete();
            if (deleted) {
                System.out.println("Save file deleted: " + filename);
            }
            return deleted;
        }
        return false;
    }

    /**
     * Checks if a save file exists.
     *
     * @param filename the name of the save file to check
     * @return true if the save file exists, false otherwise
     */
    public boolean saveExists(String filename) {
        File saveFile = new File(SAVE_DIRECTORY + filename);
        return saveFile.exists();
    }

    /**
     * Generates the next available save filename (save1.txt, save2.txt, etc.).
     *
     * @return the next available save filename
     */
    public String generateSaveFilename() {
        int saveNumber = 1;
        while (saveExists(ProfileSession.getCurrentName() + saveNumber + ".txt")) {
            saveNumber++;
        }
        return ProfileSession.getCurrentName() + saveNumber + ".txt";
    }
}