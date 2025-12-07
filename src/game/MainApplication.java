package game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * This class provides the initialisation of the program.
 * It loads the FXML, shows the stage, and passes the scene to GameController.
 * Does not include any game logic.
 *
 * @author Antoni Wachowiak
 * @version 1.0
 */
public class MainApplication extends Application {

    // Constants for the window dimensions
    private static final int WINDOW_WIDTH = 750;
    private static final int WINDOW_HEIGHT = 600;

    /**
     * The method that launches the JavaFX application.
     *
     * @param args command-line parameter passed to the application
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     */
    @Override
    public void start(Stage primaryStage) {

        try {
            // Load FXML using FXMLLoader instance (not static)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MenuGraphics.fxml"));
            Pane root = loader.load();

            // Load the scene onto the GUI
            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

            // Setting the scene and displaying it
            primaryStage.setScene(scene);
            primaryStage.setTitle("Jewel Thieves Group 01");
            primaryStage.sizeToScene();
            primaryStage.setResizable(false);
            primaryStage.show();

            // Giving MenuController the control of the stage
            MenuController controller = loader.getController();
            controller.setStage(primaryStage);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
