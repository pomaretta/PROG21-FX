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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Carlos Pomares
 */

public class GameController {

    final private int MAX_POINTS = 5;
    final private int BALL_RADIUS = 15;
    final private int HEIGHT, WIDTH;
    final private double[] P1_POS;
    final private double[] P2_POS;

    // FX Objects
    private Pane canvas;

    private Label points;

    private Player[] players;
    private Ball ball;
    private Timeline loop;

    public GameController(int HEIGHT, int WIDTH){

        this.HEIGHT = HEIGHT;
        this.WIDTH = WIDTH;

        this.canvas = new Pane();
        this.canvas.setStyle("-fx-background-color: black");

        this.points = new Label("0 - 0");
        this.points.setStyle("-fx-font-size: 22px;-fx-background-color: white");

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
        this.canvas.getChildren().add(this.points);
    }

    private void relocateComponents(){
        this.players[0].getSprite().relocate(this.P1_POS[0],this.P1_POS[1]);
        this.players[1].getSprite().relocate(this.P2_POS[0],this.P2_POS[1]);
        this.ball.relocateInMiddle(this.canvas);
        this.points.relocate(((double) this.WIDTH / 2),50);
    }

    private void startRound(){

        if(this.players[0].getPoints() == MAX_POINTS)
            goBack();

        if(this.players[1].getPoints() == MAX_POINTS)
            goBack();

        this.points.setText(String.format("%d - %d",this.players[0].getPoints(),this.players[1].getPoints()));

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

    private void goBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/resources/fxml/Screen.fxml"));
            Main.scene.setRoot(root);
        } catch (Exception exception){
            System.out.println(exception.getMessage());
        }
    }

    // SCENE
    public Parent getScene(){
        return this.canvas;
    }

}
