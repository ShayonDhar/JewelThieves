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
    private static final int WINDOW_WIDTH = 950;
    private static final int WINDOW_HEIGHT = 700;

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
     * @throws Exception if something goes wrong
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        try {
            // Load FXML using FXMLLoader instance (not static)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameGraphics.fxml"));
            Pane root = loader.load();
            GameController controller = loader.getController();

            // Load the scene onto the GUI
            Scene scene = new Scene(root,WINDOW_WIDTH,WINDOW_HEIGHT);

            // Register key input into the GameController
            scene.setOnKeyPressed(controller::onKeyPressed);

            // Setting the scene and displaying it
            primaryStage.setScene(scene);
            primaryStage.setTitle("Jewel Thieves Group 01");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
