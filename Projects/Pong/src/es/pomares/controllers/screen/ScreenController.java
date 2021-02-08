package es.pomares.controllers.screen;

import es.pomares.Main;
import es.pomares.controllers.game.GameController;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class ScreenController implements Initializable {

    private static Stage stage;
    private static Scene scene;
    private static MediaView mediaView;

    private GameController gameController;

    @FXML
    private AnchorPane root;
    @FXML
    private CheckBox musicButton;
    @FXML
    private Button gameButton;
    @FXML
    private Button exitButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.root.getChildren().add(mediaView);
        this.gameButton.toFront();
        this.exitButton.toFront();
        Main.playVideo();
    }

    @FXML
    private void startGame(){
        this.gameController = new GameController(600,900);
        scene.setRoot(this.gameController.getScene());
        Main.pauseVideo();
    }

    @FXML
    private void exitApplication(){
        stage.close();
    }

    @FXML
    private void onMusicAction(){
        this.musicButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(musicButton.isSelected()){
                    Main.playMusic();
                } else {
                    Main.pauseMusic();
                }
            }
        });
    }

    // STATIC
    public static void setStage(Stage primaryStage){
        stage = primaryStage;
    }
    public static void setScene(Scene primaryStage){
        scene = primaryStage;
    }
    public static void setBackground(MediaView media){
        mediaView = media;
    }


}
