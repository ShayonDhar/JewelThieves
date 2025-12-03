package game.playerProfile;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LevelMenuTest extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("levelmenu.fxml"));
        Parent root = loader.load();

        // Create a scene
        Scene scene = new Scene(root, 800, 600);

        // Apply CSS
        scene.getStylesheets().add(getClass().getResource("levelmenu.css").toExternalForm());

        // Set up stage
        primaryStage.setTitle("Level Menu Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
