package Proyectos.Pong;

/*

    Project     PROG21-FX
    Package     Proyectos.Pong    
    
    Version     1.0      
    Author      Carlos Pomares
    Date        2021-02-02

    DESCRIPTION
    
*/

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @author Carlos Pomares
 */

public class Pong extends Application {

    // PONG SCENE PROPERTIES
    final private int HEIGHT = 400;
    final private int WIDTH = 600;

    // PONG GAME PROPERTIES
    final private int BALL_RADIUS = 15;

    // SCENE OBJECTS
    private Pane canvas;

    // GAME OBJECTS
    private Player[] players;
    private Ball ball;
    Timeline loop;

    // PLAYER SCORE
    private Text player0;
    private Text player1;

    @Override
    public void start(Stage applicationStage){

        this.canvas = new Pane();
        final Scene primaryScene = new Scene(canvas,this.WIDTH,this.HEIGHT);

        applicationStage.setScene(primaryScene);
        applicationStage.setTitle("Pong Game");
        applicationStage.show();
        canvas.requestFocus();

        this.players = new Player[]{
            new Player(20,50,Color.RED),
            new Player(20,50,Color.RED)
        };

        this.player0 = new Text("Player0: " + this.players[0].getPoints());
        this.player1 = new Text("Player1: " + this.players[0].getPoints());

        initGame();

    }

    private void startGame(){

        this.canvas.setOnKeyPressed(e -> {

            switch (e.getCode()){
                case P: restartGame(); break;
            }

            // PLAYER 1
            switch (e.getCode()){
                case W: this.players[0].moveUp(); break;
                case S: this.players[0].moveDown(); break;
            }

            // PLAYER 2
            switch (e.getCode()){
                case UP: this.players[1].moveUp(); break;
                case DOWN: this.players[1].moveDown(); break;
            }

        });

        // TIMELINE
        loop = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {

            @Override
            public void handle(final ActionEvent event){
                gameController();
            }

        }));
        loop.setCycleCount(Timeline.INDEFINITE);
        loop.play();

    }

    private void initGame(){

        // OBJECT INIT
        this.ball = new Ball(BALL_RADIUS, Color.YELLOW);

        this.player0.setText("Player0: " + this.players[0].getPoints());
        this.player1.setText("Player1: " + this.players[1].getPoints());

        this.canvas.getChildren().add(this.players[0].player);
        this.canvas.getChildren().add(this.players[1].player);
        this.canvas.getChildren().add(this.player0);
        this.canvas.getChildren().add(this.player1);
        this.canvas.getChildren().add(ball.ball);

        System.out.println(this.ball.deltaX);

        this.players[0].player.relocate(50,(double)((this.HEIGHT / 2) - players[0].player.getHeight()));
        this.players[1].player.relocate(this.WIDTH - 100,(double)((this.HEIGHT / 2) - players[1].player.getHeight()));
        this.player0.relocate(50,50);
        this.player1.relocate(this.WIDTH - 50,50);
        this.ball.relocateInMiddle(this.canvas);

        startGame();
    }

    private void gameController(){
        if(this.ball.isLeftPoint()){
            this.players[1].addPoint();
            restartGame();
        }

        if(this.ball.isRightPoint()){
            this.players[0].addPoint();
            restartGame();
        }

        Player.detectCollision(this.players[0],this.ball);
        Player.detectCollision(this.players[1],this.ball);
        this.ball.movement(this.canvas);
    }

    private void restartGame(){

        this.canvas.getChildren().remove(this.players[0].player);
        this.canvas.getChildren().remove(this.players[1].player);
        this.canvas.getChildren().remove(this.player0);
        this.canvas.getChildren().remove(this.player1);
        this.canvas.getChildren().remove(this.ball.ball);

        this.ball = null;
        this.loop = null;

        this.initGame();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
