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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LevelMenu.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 800, 600);

        // Linking the css file
        scene.getStylesheets().add(
                getClass().getResource("levelMenu.css").toExternalForm()
        );

        primaryStage.setTitle("Level Menu Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
