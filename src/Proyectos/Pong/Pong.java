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
        this.ball = new Ball(BALL_RADIUS, Color.YELLOW);

        // TIMELINE
        loop = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {

            @Override
            public void handle(final ActionEvent event){
                gameController();
            }

        }));
        loop.setCycleCount(Timeline.INDEFINITE);

        generateComponents();
        initGame();

    }

    private void generateComponents(){
        this.canvas.getChildren().add(this.players[0].player);
        this.canvas.getChildren().add(this.players[1].player);
        this.canvas.getChildren().add(ball.ball);
    }

    private void initGame(){

        this.players[0].player.relocate(50,(double)((this.HEIGHT / 2) - players[0].player.getHeight()));
        this.players[1].player.relocate(this.WIDTH - 100,(double)((this.HEIGHT / 2) - players[1].player.getHeight()));
        this.ball.relocateInMiddle(this.canvas);

        startGame();
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

        loop.play();

    }

    private void gameController(){
        if(this.ball.isLeftLimit()){
            this.players[1].addPoint();
            restartGame();
        }

        if(this.ball.isRightLimit()){
            this.players[0].addPoint();
            restartGame();
        }

        Player.detectCollision(this.players[0],this.ball);
        Player.detectCollision(this.players[1],this.ball);
        this.ball.movement(this.canvas);
    }

    private void restartGame(){

        this.ball.resetProperties();
        this.ball.resetVelocity();

        this.loop.stop();

        System.out.println("DELTA X: " + this.ball.deltaX + "DELTA Y:" + this.ball.deltaY);

        initGame();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
