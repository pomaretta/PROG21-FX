package Proyectos.Pong;

/*

    Project     PROG21-FX
    Package     Proyectos.Pong    
    
    Version     1.0      
    Author      Carlos Pomares
    Date        2021-02-12

    DESCRIPTION
    
*/

import com.sun.javafx.tk.FontLoader;
import javafx.animation.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

/**
 * @author Carlos Pomares
 */

public class Pong_carlos_pomares extends Application {

    // WINDOW PROPERTIES
    private int HEIGHT;
    private int WIDTH;

    public final String TITLE = "Pong"
            ,VERSION = "1.0"
            , AUTHOR = "Carlos Pomares";

    // MANAGER
    private SceneManager manager;

    // SCENES
    public EntryScene entryScene;
    public GameScene gameScene;
    public OverScene overScene;

    // ENTRY PROPERTIES

    // GAME PROPERTIES
    private final int WINNER_ROUNDS = 15;

    // OVER PROPERTIES

    @Override
    public void start(Stage stage) throws Exception {

        // ASSIGN WIDTH AND HEIGHT
        this.WIDTH = 800;
        this.HEIGHT = 600;

        // SCENE MANAGER
        this.manager = new SceneManager(this);

        // INSTANCE AVAILABLE SCENES
        this.entryScene = new EntryScene(this.WIDTH,this.HEIGHT,this);
        this.gameScene = new GameScene(this.WIDTH,this.HEIGHT,this);
        this.overScene = new OverScene(this.WIDTH,this.HEIGHT,this);

        // REGISTER AVAILABLE SCENES
        this.manager.registerScene(entryScene);
        this.manager.registerScene(gameScene);
        this.manager.registerScene(overScene);

        // LOAD FIRST SCENE
        this.manager.changeScene(entryScene);

        // STAGE
        stage.setScene(this.manager.getScene());
        stage.setTitle(String.format(
                "%s - V%s", this.TITLE, this.VERSION
        ));
        stage.setResizable(false);
        stage.show();

    }

    public int getHeight() {
        return HEIGHT;
    }

    public int getWidth() {
        return WIDTH;
    }

    public static void main(String[] args) {
        launch(args);
    }

}

/*
*
*   SCENE MANAGER
*
* */

class SceneManager {

    private final Scene scene;
    private final Pong_carlos_pomares toManage;
    private PongScene currentScene;
    private final ArrayList<PongScene> scenes;

    public SceneManager(Pong_carlos_pomares bind){
        this.toManage = bind;
        this.scene = new Scene(new Pane(),toManage.getWidth(),toManage.getHeight());
        this.scenes = new ArrayList<>();
    }

    private void loadScene() {
        assert getCurrentScene() != null;
        this.scene.setRoot(getCurrentScene().getScene());
        getCurrentScene().run();
    }

    private void unloadScene(){
        this.scene.setRoot(new Pane());
    }

    public void removeScene(PongScene scene)
        throws NoSuchElementException {
        assert scene != null;
        if(!this.scenes.contains(scene)){
            throw new NoSuchElementException();
        }
        unloadScene();
        this.scenes.remove(scene);
    }

    public void registerScene(PongScene scene)
        throws Exception {
        assert scene != null;
        if(this.scenes.contains(scene)){
            throw new Exception();
        }
        this.scenes.add(scene);
        scene.bindManager(this);
    }

    public PongScene getCurrentScene()
        throws NullPointerException {
        if(this.currentScene == null){
            throw new NullPointerException();
        }
        return this.currentScene;
    }

    public void changeScene(PongScene scene)
        throws NoSuchElementException {
        assert scene != null;
        if(!this.scenes.contains(scene)){
            throw new NoSuchElementException();
        }
        this.currentScene = scene;
        loadScene();
    }

    public Scene getScene(){
        return this.scene;
    }

}

/*
*
*   SCENES
*
* */

interface Component {
    public void generateComponents();
    public void relocateComponents();
    public void loadComponents();
    public void run();
    public void bindManager(SceneManager manager) throws Exception;
    public Parent getScene();
}

abstract class PongScene implements Component {

    private final int WIDTH;
    private final int HEIGHT;
    private final Pane ROOT;
    protected SceneManager manager;
    protected Pong_carlos_pomares parent;

    public PongScene(int width, int height, Pong_carlos_pomares parent){
        this.WIDTH = width;
        this.HEIGHT = height;
        this.parent = parent;
        this.ROOT = new Pane();
    }

    public int getWidth() {
        return this.WIDTH;
    }

    public int getHeight() {
        return this.HEIGHT;
    }

    @Override
    public void run() {
        generateComponents();
        loadComponents();
        relocateComponents();
    }

    @Override
    public void bindManager(SceneManager manager)
            throws Exception {
        assert manager != null;
        if(this.manager != null){
            throw new Exception();
        }
        this.manager = manager;
    }

    public Pane getRoot(){
        return this.ROOT;
    }

    @Override
    public Parent getScene() {
        return this.getRoot();
    }

}

class EntryScene extends PongScene {

    // ENTRY COMPONENTS
    private StackPane options;
    private Label entryTitle;
    private Button sceneChange;
    private Button optionsChange;
    private Button exitOption;

    public EntryScene(int width, int height, Pong_carlos_pomares parent){
        super(width,height, parent);
    }

    @Override
    public void generateComponents() {
        this.options = new StackPane();
        this.options.setPrefSize(400,200);

        // LABELS
        this.entryTitle = new Label("Pong");
        this.entryTitle.setFont(Font.font(56));

        // BUTTONS
        this.sceneChange = new Button("Start game!");
        this.sceneChange.setPrefSize(120,50);
        this.optionsChange = new Button("Options!");
        this.optionsChange.setPrefSize(120,50);
        this.exitOption = new Button("Exit");
        this.exitOption.setPrefSize(120,50);
    }

    @Override
    public void loadComponents() {

        // LABEL
        this.getRoot().getChildren().add(this.entryTitle);

        // OPTIONS
        this.getRoot().getChildren().add(this.options);
        this.options.getChildren().add(this.sceneChange);
        this.options.getChildren().add(this.optionsChange);
        this.options.getChildren().add(this.exitOption);

    }

    @Override
    public void relocateComponents() {

        // LABEL
        this.entryTitle.relocate(
                (this.getRoot().getBoundsInLocal().getCenterX()),50);
        this.entryTitle.layout();

        System.out.println(this.entryTitle.getWidth());

        // OPTIONS
        this.options.relocate(
                (
                        this.getRoot().getBoundsInLocal().getCenterX()
                        - (200)
                ),200);
        StackPane.setAlignment(this.sceneChange,Pos.TOP_CENTER);
        StackPane.setAlignment(this.optionsChange,Pos.CENTER);
        StackPane.setAlignment(this.exitOption,Pos.BOTTOM_CENTER);

    }

    @Override
    public void run() {
        super.run();

        this.sceneChange.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                listener();
            }
        });

        // BUTTON ANIMATION
        final ScaleTransition buttonTransition = new ScaleTransition(Duration.millis(1000));
        buttonTransition.setNode(this.sceneChange);

        //Setting the dimensions for scaling
        buttonTransition.setByY(.5);
        buttonTransition.setByX(.5);

        //Setting the cycle count for the translation
        buttonTransition.setCycleCount(Animation.INDEFINITE);

        //Setting auto reverse value to true
        buttonTransition.setAutoReverse(true);

        //Playing the animation
        this.sceneChange.setOnMouseEntered(e -> buttonTransition.play());

        this.sceneChange.setOnMouseExited(e -> {
            buttonTransition.jumpTo(Duration.ZERO);
            buttonTransition.stop();
        });

    }

    private void listener(){
        this.manager.changeScene(this.parent.gameScene);
    }

}

class GameScene extends PongScene {

    class Player {

        private double deltaY = 10,velocity = 1.5;
        private Rectangle sprite;
        private int points;

        public Player(int WIDTH,int HEIGHT,Color color){
            this.sprite = new Rectangle(WIDTH,HEIGHT,color);
            this.sprite.setStyle("-fx-background-radius: 25px");
        }

        public double getDeltaY() {
            return deltaY;
        }

        public Rectangle getSprite() {
            return sprite;
        }

        public int getPoints() {
            return points;
        }

        public void addPoint(){
            this.points++;
        }

        public void moveUp(){
            this.sprite.setLayoutY(this.sprite.getLayoutY() - this.deltaY * velocity);
        }

        public void moveDown(){
            this.sprite.setLayoutY(this.sprite.getLayoutY() + this.deltaY * velocity);
        }

        public boolean detectCollision(Ball ball){
            if(getSprite().getBoundsInParent().intersects(ball.getSprite().getBoundsInParent())){
                ball.modifyX();
                ball.accelerate();
            }
            return false;
        }

        public boolean upperLimit(Parent canvas){
            final Bounds limits = canvas.getBoundsInLocal();
            final boolean upperLimit = getSprite().getLayoutY() <= (limits.getMinY());
            return !upperLimit;
        }

        public boolean lowerLimit(Parent canvas){
            final Bounds limits = canvas.getBoundsInLocal();
            final boolean lowerLimit = getSprite().getLayoutY() >= (limits.getMaxY() - getSprite().getHeight());
            return !lowerLimit;
        }

    }
    class Ball {

        private final double MAX_VEL = 5;
        private double deltaX, deltaY, velocity;
        private Circle sprite;

        private boolean leftLimit = false, rightLimit = false;

        public Ball(int radius, Color color){
            this.sprite = new Circle(radius,color);
            generateAngle();
        }

        public void modifyY(){
            this.deltaY *= -1;
        }

        public void modifyX(){
            this.deltaX *= -1;
        }

        public void accelerate(){
            if(this.velocity <= this.MAX_VEL)
                this.velocity++;
        }

        public void movement(Parent canvas){

            this.sprite.setLayoutX(this.sprite.getLayoutX() + deltaX * velocity);
            this.sprite.setLayoutY(this.sprite.getLayoutY() + deltaY * velocity);

            final Bounds limits = canvas.getBoundsInLocal();
            final boolean leftLimit = this.sprite.getLayoutX() <= (limits.getMinX() + this.sprite.getRadius());
            final boolean rightLimit = this.sprite.getLayoutX() >= (limits.getMaxX() - this.sprite.getRadius());
            final boolean upperLimit = this.sprite.getLayoutY() <= (limits.getMinY() + this.sprite.getRadius());
            final boolean lowerLimit = this.sprite.getLayoutY() >= (limits.getMaxY() - this.sprite.getRadius());

            if(leftLimit)
                this.leftLimit = true;

            if(rightLimit)
                this.rightLimit = true;

            if(upperLimit || lowerLimit)
                modifyY();

        }

        public void generateAngle(){
            double angle = Math.toRadians(45);
            this.velocity = 1;
            this.deltaX = this.velocity * Math.cos(angle);
            this.deltaY = this.velocity * Math.sin(angle);
        }

        public void resetProperties(){
            this.leftLimit = false;
            this.rightLimit = false;
        }

        public void relocateInMiddle(Parent canvas){
            Bounds limits = canvas.getBoundsInLocal();
            this.sprite.relocate(
                    ((limits.getMaxX() - limits.getMaxX() / 2) - this.sprite.getRadius()),
                    ((limits.getMaxY() - (limits.getMaxY() / 2)) - this.sprite.getRadius())
            );
        }

        public double getDeltaX() {
            return deltaX;
        }

        public double getDeltaY() {
            return deltaY;
        }

        public double getVelocity() {
            return velocity;
        }

        public Circle getSprite() {
            return sprite;
        }

        public boolean isLeftLimit() {
            return leftLimit;
        }

        public boolean isRightLimit() {
            return rightLimit;
        }

    }

    // OBJECTS
    private Player[] players;
    private Ball ball;
    private Timeline loop;

    // PROPERTIES
    final private int MAX_POINTS = 2;
    final private int BALL_RADIUS = 15;
    final private double[] P1_POS;
    final private double[] P2_POS;

    // Components
    private Label points;

    public GameScene(int width, int height, Pong_carlos_pomares parent){
        super(width, height, parent);

        this.ball = new Ball(this.BALL_RADIUS,Color.GREEN);
        this.players = new Player[]{
                new Player(30,80,Color.GREEN),
                new Player(30,80,Color.GREEN)
        };

        this.loop = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                roundHandler();
            }
        }));
        this.loop.setCycleCount(Timeline.INDEFINITE);
        
        this.P1_POS = new double[]{
                50,
                (((double) this.getHeight() / 2) - this.players[0].getSprite().getHeight())
        };

        this.P2_POS = new double[]{
                this.getWidth() - 100,
                (((double) this.getHeight() / 2) - this.players[0].getSprite().getHeight())
        };

    }

    @Override
    public void generateComponents() {
        this.points = new Label("0 - 0");
    }

    @Override
    public void loadComponents() {
        this.getRoot().getChildren().add(this.players[0].getSprite());
        this.getRoot().getChildren().add(this.players[1].getSprite());
        this.getRoot().getChildren().add(this.ball.getSprite());
        this.getRoot().getChildren().add(this.points);

        this.getRoot().setStyle("-fx-background-color: black");

    }

    @Override
    public void relocateComponents() {
        this.players[0].getSprite().relocate(this.P1_POS[0],this.P1_POS[1]);
        this.players[1].getSprite().relocate(this.P2_POS[0],this.P2_POS[1]);
        this.ball.relocateInMiddle(this.getRoot());
        this.points.relocate(((double) this.getWidth() / 2),50);
    }

    @Override
    public void run() {
        super.run();
        startRound();
    }

    private void startRound(){

        if(this.players[0].getPoints() == MAX_POINTS)
            gameOver();

        if(this.players[1].getPoints() == MAX_POINTS)
            gameOver();

        this.points.setText(String.format("%d - %d",this.players[0].getPoints(),this.players[1].getPoints()));

        // https://stackoverflow.com/questions/52580865/javafx-multiple-keylisteners-at-once
        final List<KeyCode> acceptedCodes = Arrays.asList(KeyCode.S, KeyCode.W, KeyCode.UP, KeyCode.DOWN);
        final Set<KeyCode> codes = new HashSet<>();
        manager.getScene().setOnKeyReleased(e -> codes.clear());
        manager.getScene().setOnKeyPressed(e -> {
            if (acceptedCodes.contains(e.getCode())) {
                codes.add(e.getCode());
                if (codes.contains(KeyCode.W) && this.players[0].upperLimit(this.getRoot())) {
                    this.players[0].moveUp();
                } else if (codes.contains(KeyCode.S) && this.players[0].lowerLimit(this.getRoot())) {
                    this.players[0].moveDown();
                }
                if (codes.contains(KeyCode.UP) && this.players[1].upperLimit(this.getRoot())) {
                    this.players[1].moveUp();
                } else if (codes.contains(KeyCode.DOWN) && this.players[1].lowerLimit(this.getRoot())) {
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

        this.players[0].detectCollision(this.ball);
        this.players[1].detectCollision(this.ball);
        this.ball.movement(this.getRoot());
    }

    private void resetRound(){
        this.ball.resetProperties();
        this.ball.generateAngle();
        relocateComponents();
        startRound();
    }

    private void gameOver() {
        this.parent.overScene.setPlayers(this.players);
        this.manager.changeScene(this.parent.overScene);
    }

}

class OverScene extends PongScene {

    // OBJECTS
    private GameScene.Player[] players;

    // COMPONENTS
    private Label winner;

    public OverScene(int width, int height, Pong_carlos_pomares parent) {
        super(width, height, parent);
    }

    @Override
    public void generateComponents() {
        this.winner = new Label();
        setWinner();
    }

    @Override
    public void loadComponents() {
        this.getRoot().getChildren().add(this.winner);
    }

    @Override
    public void relocateComponents() {
        this.winner.relocate(100,100);
    }

    public void setPlayers(GameScene.Player[] players){
        assert players != null;
        this.players = players;
    }

    private void setWinner(){
        if(players[0].getPoints() > players[1].getPoints()){
            this.winner.setText(String.format("Winner is: %s","Player 0"));
        } else {
            this.winner.setText(String.format("Winner is: %s","Player 1"));
        }
    }

}