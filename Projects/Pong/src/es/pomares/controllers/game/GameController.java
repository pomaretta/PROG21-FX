package es.pomares.controllers.game;

/*

    Project     PROG21-FX
    Package     es.pomares.controllers.game    
    
    Version     1.0      
    Author      Carlos Pomares
    Date        2021-02-08

    DESCRIPTION
    
*/

import es.pomares.Main;
import es.pomares.pong.Ball;
import es.pomares.pong.Player;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Carlos Pomares
 */

public class GameController {

    final private int BALL_RADIUS = 15;
    final private int HEIGHT, WIDTH;
    final private double[] P1_POS;
    final private double[] P2_POS;

    private Pane canvas;

    private Player[] players;
    private Ball ball;
    private Timeline loop;

    public GameController(int HEIGHT, int WIDTH){

        this.HEIGHT = HEIGHT;
        this.WIDTH = WIDTH;

        this.canvas = new Pane();

        this.players = new Player[]{
                new Player(20,80, Color.GREEN),
                new Player(20,80, Color.GREEN)
        };
        this.ball = new Ball(BALL_RADIUS,Color.GREENYELLOW);

        this.loop = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                roundHandler();
            }
        }));
        this.loop.setCycleCount(Timeline.INDEFINITE);

        this.P1_POS = new double[]{
                50,
                (((double) this.HEIGHT / 2) - this.players[0].getSprite().getHeight())
        };

        this.P2_POS = new double[]{
                this.WIDTH - 100,
                (((double) this.HEIGHT / 2) - this.players[0].getSprite().getHeight())
        };

        generateComponents();
        relocateComponents();
        startRound();
    }

    private void generateComponents(){
        this.canvas.getChildren().add(this.players[0].getSprite());
        this.canvas.getChildren().add(this.players[1].getSprite());
        this.canvas.getChildren().add(this.ball.getSprite());
    }

    private void relocateComponents(){
        this.players[0].getSprite().relocate(this.P1_POS[0],this.P1_POS[1]);
        this.players[1].getSprite().relocate(this.P2_POS[0],this.P2_POS[1]);
        this.ball.relocateInMiddle(this.canvas);
    }

    private void startRound(){
        // https://stackoverflow.com/questions/52580865/javafx-multiple-keylisteners-at-once
        final List<KeyCode> acceptedCodes = Arrays.asList(KeyCode.S, KeyCode.W, KeyCode.UP, KeyCode.DOWN);
        final Set<KeyCode> codes = new HashSet<>();
        Main.scene.setOnKeyReleased(e -> codes.clear());
        Main.scene.setOnKeyPressed(e -> {
            if (acceptedCodes.contains(e.getCode())) {
                codes.add(e.getCode());
                if (codes.contains(KeyCode.W) && Player.upperLimit(this.players[0],this.canvas)) {
                    this.players[0].moveUp();
                } else if (codes.contains(KeyCode.S) && Player.lowerLimit(this.players[0],this.canvas)) {
                    this.players[0].moveDown();
                }
                if (codes.contains(KeyCode.UP) && Player.upperLimit(this.players[1],this.canvas)) {
                    this.players[1].moveUp();
                } else if (codes.contains(KeyCode.DOWN) && Player.lowerLimit(this.players[1],this.canvas)) {
                    this.players[1].moveDown();
                }
            }
        });

        loop.play();
    }

    private void roundHandler(){
        if(this.ball.isLeftLimit()) {
            this.players[1].addPoint();
            resetRound();
        }
        if(this.ball.isRightLimit()) {
            this.players[0].addPoint();
            resetRound();
        }

        Player.detectCollision(this.players[0],this.ball);
        Player.detectCollision(this.players[1],this.ball);
        this.ball.movement(this.canvas);
    }

    private void resetRound(){
        this.ball.resetProperties();
        this.ball.generateAngle();
        relocateComponents();
        startRound();
    }

    // SCENE
    public Parent getScene(){
        return this.canvas;
    }

}
