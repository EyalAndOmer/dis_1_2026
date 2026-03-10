package uniza.fri.majba.dis1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main class for the JavaFX MVC Application.
 * This is the entry point of the application.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/chart_view.fxml"));
        Parent root = loader.load();

        // Create the scene
        Scene scene = new Scene(root);
        scene.getStylesheets().add(
                getClass().getResource("/chart_styles.css").toExternalForm()
        );

        // Configure the stage
        primaryStage.setTitle("JavaFX MVC Application");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
