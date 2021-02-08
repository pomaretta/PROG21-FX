package es.pomares;

import es.pomares.controllers.screen.ScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class Main extends Application {

    private final int HEIGTH = 600;
    private final int WIDTH = 900;
    private final String version = "1.0";

    private static String impactPath;
    private static Media impactMedia;
    private static MediaPlayer impactPlayer;

    private static String audioPath;
    private static Media audioMedia;
    private static MediaPlayer audioPlayer;

    private static String videoPath;
    private static Media videoMedia;
    private static MediaPlayer videoPlayer;
    private static MediaView videoView;

    public static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {

        // IMPACT
        impactPath = getClass().getResource("/resources/media/impact.mp3").toString();
        impactMedia = new Media(impactPath);
        impactPlayer = new MediaPlayer(impactMedia);
        //impactPlayer.setVolume(1);
        //impactPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        // AUDIO
        audioPath = getClass().getResource("/resources/media/pong.mp3").toString();
        audioMedia = new Media(audioPath);
        audioPlayer = new MediaPlayer(audioMedia);
        audioPlayer.setVolume(.1);
        audioPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        audioPlayer.setAutoPlay(true);

        // VIDEO BACKGROUND
        videoPath = getClass().getResource("/resources/media/matrix.mp4").toString();
        videoMedia = new Media(videoPath);
        videoPlayer = new MediaPlayer(videoMedia);
        videoPlayer.setAutoPlay(true);
        videoPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        videoView = new MediaView(videoPlayer);
        videoView.setFitHeight(this.HEIGTH);
        videoView.setFitWidth(this.WIDTH);
        videoView.toBack();
        videoView.setOpacity(.2);
        ScreenController.setBackground(videoView);

        ScreenController.setStage(stage);

        // FIRST SCENE
        Parent root = FXMLLoader.load(getClass().getResource("/resources/fxml/Screen.fxml"));
        scene = new Scene(root,this.WIDTH,this.HEIGTH);
        ScreenController.setScene(scene);
        stage.setTitle("Pong RETRO v" + version);
        stage.setScene(scene);
        stage.show();

    }

    public static void playMusic(){
        audioPlayer.play();
    }

    public static void pauseMusic(){
        audioPlayer.pause();
    }

    public static void playVideo(){videoPlayer.play();}

    public static void pauseVideo(){videoPlayer.pause();}

    public static void impactAudio(){
        impactPlayer.play();
        impactPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                impactPlayer.stop();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

}
