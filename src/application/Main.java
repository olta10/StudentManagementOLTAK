package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the Home.fxml file
            Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));

            // Set the scene with the loaded FXML
            Scene scene = new Scene(root);

            // Configure the primary stage
            primaryStage.setScene(scene);
            primaryStage.initStyle(StageStyle.UNDECORATED); // Remove window decorations
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}
