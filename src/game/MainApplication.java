package game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


/**
 * This class provides the initialisation of the program.
 * It launches the args, and initialises the GUI canvas that plays the game.
 *
 * @author Antoni Wachowiak
 * @version 1.0
 */
public class MainApplication extends Application {

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
            // Loading the pane onto the scene
            Pane root = (Pane) FXMLLoader.load(getClass().getResource("GameGraphics.fxml"));
            Scene scene = new Scene(root,950,700); // w and h from .fxml file

            // Setting the scene and displaying it
            primaryStage.setScene(scene);
            primaryStage.setTitle("Jewel Thieves Group 01");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
