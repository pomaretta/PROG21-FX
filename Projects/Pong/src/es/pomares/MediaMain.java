package es.pomares;

/*

    Project     PROG21-FX
    Package     es.pomares    
    
    Version     1.0      
    Author      Carlos Pomares
    Date        2021-02-08

    DESCRIPTION
    
*/

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * @author Carlos Pomares
 */

public class MediaMain extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // Set up the audio
        String audioPath = getClass().getResource("/resources/media/pong.mp3").toString();
        Media audioMedia = new Media(audioPath);
        MediaPlayer audioPlayer = new MediaPlayer(audioMedia);

        //String name = audioPath.getFileName().toString();
        Label playingStatus = new Label("Now Playing: ");

        // Set the play and stop button
        Button playButton = new Button("Play");
        playButton.setPrefWidth(60);
        Button stopButton = new Button("Stop");
        stopButton.setPrefWidth(60);

        playButton.setOnAction(e -> {
            if (audioPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                audioPlayer.stop();
                audioPlayer.play();
                playingStatus.setText("Now replaying: " + "name");
            } else {
                audioPlayer.play();
                playingStatus.setText("Now playing: " + "name");
            }
        });

        stopButton.setOnAction(e -> {
            audioPlayer.stop();
            playingStatus.setText("Audio stopped");
        });

        HBox buttonSection = new HBox(playButton, stopButton);
        buttonSection.setSpacing(10);
        buttonSection.setPadding(new Insets(15));

        VBox root = new VBox(playingStatus, buttonSection);
        root.setPadding(new Insets(50));

        stage.setTitle("Simple Media Example");
        stage.setScene(new Scene(root, 300, 300));
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
