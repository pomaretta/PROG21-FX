package es.pomares;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {

    private final int HEIGTH = 600;
    private final int WIDTH = 900;

    @Override
    public void start(Stage stage) throws IOException {

        // FIRST SCENE
        Parent root = FXMLLoader.load(getClass().getResource("/resources/scene1.fxml"));

        Scene scene = new Scene(root,this.WIDTH,this.HEIGTH);
        stage.setTitle("Pong v1.0");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
