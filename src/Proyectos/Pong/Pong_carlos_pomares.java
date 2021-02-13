package Proyectos.Pong;

/*

    Project     PROG21-FX
    Package     Proyectos.Pong    
    
    Version     1.0      
    Author      Carlos Pomares
    Date        2021-02-12

    DESCRIPTION
    
*/

import javafx.animation.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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

    // STAGE
    private Stage stage;

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

        // SET STAGE
        this.stage = stage;

        // ASSIGN WIDTH AND HEIGHT
        this.WIDTH = 800;
        this.HEIGHT = 600;

        // SCENE MANAGER
        this.manager = new SceneManager(this);

        // INSTANCE AVAILABLE SCENES
        this.entryScene = new EntryScene(this.WIDTH,this.HEIGHT,this);
        this.gameScene = new GameScene(this.WIDTH,this.HEIGHT,this);
        this.overScene = new OverScene(this.WIDTH,this.HEIGHT,this);

        // SCENE PROPERTIES IF EXISTS
        this.gameScene.setMaxPoints(this.WINNER_ROUNDS);

        // REGISTER AVAILABLE SCENES
        this.manager.registerScene(entryScene);
        this.manager.registerScene(gameScene);
        this.manager.registerScene(overScene);

        // LOAD FIRST SCENE
        this.manager.changeScene(entryScene);

        // STAGE
        this.stage.setScene(this.manager.getScene());
        this.stage.setTitle(String.format(
                "%s - V%s", this.TITLE, this.VERSION
        ));
        this.stage.setResizable(false);
        this.stage.show();

    }

    public int getHeight() {
        return HEIGHT;
    }

    public int getWidth() {
        return WIDTH;
    }

    public Stage getStage() {
        return stage;
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

    static class AnimatedComponent {

        public static Button animateButton(Button button){

            // BUTTON ANIMATION
            final ScaleTransition buttonTransition = new ScaleTransition(Duration.millis(1000));
            buttonTransition.setNode(button);

            //Setting the dimensions for scaling
            buttonTransition.setByY(.2);
            buttonTransition.setByX(.2);

            //Setting the cycle count for the translation
            buttonTransition.setCycleCount(Animation.INDEFINITE);

            //Setting auto reverse value to true
            buttonTransition.setAutoReverse(true);

            //Playing the animation
            button.setOnMouseEntered(e -> buttonTransition.play());

            button.setOnMouseExited(e -> {
                buttonTransition.jumpTo(Duration.ZERO);
                buttonTransition.stop();
            });

            return button;
        }
        public static Node animateNode(Node node){
            final ScaleTransition nodeTransition = new ScaleTransition(Duration.millis(3000));
            nodeTransition.setNode(node);

            nodeTransition.setByY(.1);
            nodeTransition.setByX(.1);

            nodeTransition.setCycleCount(Animation.INDEFINITE);

            nodeTransition.setAutoReverse(true);

            nodeTransition.play();

            return node;
        }

    }

    // ENTRY COMPONENTS
    private StackPane title;
    private StackPane options;

    private Label entryTitle;

    private Button sceneChange;
    private Button optionsChange;
    private Button exitOption;

    // BACKGROUND ADDS
    private Circle topCircle;
    private Circle topCircleShadow;


    public EntryScene(int width, int height, Pong_carlos_pomares parent){
        super(width,height, parent);
    }

    @Override
    public void generateComponents() {

        this.getRoot().setStyle("-fx-background-color: white");

        this.title = new StackPane();
        this.title.setPrefSize(400,80);

        this.options = new StackPane();
        this.options.setPrefSize(400,250);
        this.options.setStyle("-fx-padding: 1.5em");

        // LABELS
        this.entryTitle = new Label("PONG");
        this.entryTitle.setFont(Font.font("Arial", FontWeight.BOLD,56));

        // BUTTONS
        this.sceneChange = new Button("Start game!");
        this.sceneChange.setPrefSize(120,50);
        this.sceneChange.setTextFill(Color.WHITE);
        this.sceneChange.setStyle("-fx-background-color: #444");

        this.optionsChange = new Button("Options");
        this.optionsChange.setPrefSize(120,50);
        this.optionsChange.setTextFill(Color.WHITE);
        this.optionsChange.setStyle("-fx-background-color: #444");

        this.exitOption = new Button("Exit");
        this.exitOption.setPrefSize(120,50);
        this.exitOption.setTextFill(Color.WHITE);
        this.exitOption.setStyle("-fx-background-color: #444");

        // BACKGROUND
        this.topCircle = new Circle(150,Color.BLUEVIOLET);
        this.topCircleShadow = new Circle(180,Color.BLUEVIOLET);
        this.topCircleShadow.setOpacity(0.2);

    }

    @Override
    public void loadComponents() {

        // LABEL
        this.getRoot().getChildren().add(this.title);
        this.title.getChildren().add(this.entryTitle);

        // OPTIONS
        this.getRoot().getChildren().add(this.options);
        this.options.getChildren().add(AnimatedComponent.animateButton(this.sceneChange));
        this.options.getChildren().add(AnimatedComponent.animateButton(this.optionsChange));
        this.options.getChildren().add(AnimatedComponent.animateButton(this.exitOption));

        // BACKGROUND
        this.getRoot().getChildren().add(this.topCircle);
        this.getRoot().getChildren().add(AnimatedComponent.animateNode(this.topCircleShadow));

    }

    @Override
    public void relocateComponents() {

        // TITLE
        this.title.relocate(
                (
                        ((double) this.parent.getWidth() / 2)
                        - (this.title.getPrefWidth() / 2)
                ), 80
        );

        StackPane.setAlignment(this.entryTitle,Pos.CENTER);

        // OPTIONS
        this.options.relocate(
                (
                        ((double) this.parent.getWidth() / 2)
                        - (this.options.getPrefWidth() / 2)
                ),250
        );

        StackPane.setAlignment(this.sceneChange,Pos.TOP_CENTER);
        StackPane.setAlignment(this.optionsChange,Pos.CENTER);
        StackPane.setAlignment(this.exitOption,Pos.BOTTOM_CENTER);

        // BACKGROUND
        this.topCircle.relocate(0 - this.topCircle.getRadius(),0 - this.topCircle.getRadius());
        this.topCircle.toBack();
        this.topCircleShadow.relocate(0 - this.topCircleShadow.getRadius(),0 - this.topCircleShadow.getRadius());
        this.topCircleShadow.toBack();

    }

    @Override
    public void run() {
        super.run();

        this.sceneChange.setOnAction(e -> startGameHandler());
        this.exitOption.setOnAction(e -> exitApplication());

    }

    private void startGameHandler(){
        this.manager.changeScene(this.parent.gameScene);
    }

    private void exitApplication(){
        this.parent.getStage().close();
    }

}

class GameScene extends PongScene {

    class Player {

        private double deltaY = 10,velocity = 1.5;
        private Rectangle sprite;
        private int points;
        private int collisionCounter = 0;

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
                this.collisionCounter++;

                ball.modifyX();

                if(collisionCounter > 1 && getSprite().getBoundsInParent().intersects(ball.getSprite().getBoundsInParent())){
                    ball.sprite.setLayoutX(
                            (ball.getDeltaX() < 0)
                            ? ball.sprite.getLayoutX() + 1 + (ball.sprite.getRadius() * 2)
                            : ball.sprite.getLayoutX() -  1 - (ball.sprite.getRadius() * 2)
                    );
                }

                if(collisionCounter <= 0) {
                    ball.increaseCollision();
                }


                if(ball.getConsecutiveCollision() >= 5){
                    ball.accelerate();
                }
            } else if(!getSprite().getBoundsInParent().intersects(ball.getSprite().getBoundsInParent())){
                resetCounter();
            }
            return false;
        }

        public void resetCounter(){
            this.collisionCounter = 0;
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

        private int consecutiveCollision = 0;
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
            if(this.velocity < this.MAX_VEL)
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
            double angle = Math.toRadians(
                    (lastWinnerPlayer)
                    ? (Math.random() * 5 + 1 > 2.5)
                            ? 45 + ((int) (Math.random() * 15) + 1)
                            : -45 - ((int) (Math.random() * 15) + 1)
                    : (Math.random() * 5 + 1 > 2.5)
                            ? 135 + ((int) (Math.random() * 15) + 1)
                            : -135 - ((int) (Math.random() * 15) + 1)
            );
            this.velocity = 1;
            this.deltaX = this.velocity * Math.cos(angle);
            this.deltaY = this.velocity * Math.sin(angle);
        }

        public void resetProperties(){
            this.leftLimit = false;
            this.rightLimit = false;
            this.consecutiveCollision = 0;
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

        public void increaseCollision(){
            this.consecutiveCollision++;
        }

        public int getConsecutiveCollision(){
            return this.consecutiveCollision;
        }

    }

    // OBJECTS
    private Player[] players;
    private Ball ball;
    private Timeline loop;

    // PROPERTIES
    final private int BALL_RADIUS = 15;
    final private double[] P1_POS;
    final private double[] P2_POS;
    private int maxPoints;
    private boolean lastWinnerPlayer = false;

    // Components
    private Label leftPoint;
    private Label rightPoint;
    private Line midLine;
    private Label spaceRequirement;

    public GameScene(int width, int height, Pong_carlos_pomares parent){
        super(width, height, parent);

        this.ball = new Ball(this.BALL_RADIUS,Color.WHITE);
        this.players = new Player[]{
                new Player(10,60,Color.WHITE),
                new Player(10,60,Color.WHITE)
        };

        this.loop = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                roundHandler();
            }
        }));
        this.loop.setCycleCount(Timeline.INDEFINITE);
        
        this.P1_POS = new double[]{
                90,
                (((double) this.getHeight() / 2) - this.players[0].getSprite().getHeight())
        };

        this.P2_POS = new double[]{
                this.getWidth() - 100,
                (((double) this.getHeight() / 2) - this.players[0].getSprite().getHeight())
        };

    }

    @Override
    public void generateComponents() {
        this.leftPoint = new Label("0");
        this.rightPoint = new Label("0");

        this.midLine = new Line();
        this.midLine.setEndY(this.getHeight() - 5);
        this.midLine.setStrokeWidth(5);
        this.midLine.getStrokeDashArray().addAll(2d, 15d);
        this.midLine.setStroke(Color.WHITE);

        this.leftPoint.setTextFill(Color.WHITE);
        this.rightPoint.setTextFill(Color.WHITE);
        this.leftPoint.setFont(Font.font("Arial",FontWeight.BOLD,22));
        this.rightPoint.setFont(Font.font("Arial",FontWeight.BOLD,22));

        this.spaceRequirement = new Label("Press space to start.");
        this.spaceRequirement.setFont(Font.font("Arial",FontWeight.BOLD,56));
        this.spaceRequirement.setTextFill(Color.WHITE);
        this.spaceRequirement.setOpacity(.8);

        this.getRoot().setStyle("-fx-background-color: black");
    }

    @Override
    public void loadComponents() {

        this.getRoot().getChildren().addAll(
                this.players[0].getSprite()
                ,this.players[1].getSprite()
                ,this.ball.getSprite()
        );

        this.getRoot().getChildren().addAll(
                this.leftPoint
                ,this.rightPoint
                ,this.midLine);

        this.getRoot().getChildren().add(this.spaceRequirement);

    }

    @Override
    public void relocateComponents() {
        this.players[0].getSprite().relocate(this.P1_POS[0],this.P1_POS[1]);
        this.players[1].getSprite().relocate(this.P2_POS[0],this.P2_POS[1]);
        this.ball.relocateInMiddle(this.getRoot());
        this.leftPoint.relocate(
                (double) this.getWidth() / 2 - 35,
                50
        );

        this.rightPoint.relocate(
                (double) this.getWidth() / 2 + 25,
                50
        );

        this.midLine.relocate(
                (double) this.getWidth() / 2 - 2.5,
                0
        );
        this.midLine.toBack();


        this.spaceRequirement.relocate(
                (double) this.getWidth() / 2 - 280,
                400
        );
        this.spaceRequirement.toFront();

    }

    @Override
    public void run() {
        super.run();

        manager.getScene().setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.SPACE){
                this.spaceRequirement.setVisible(false);
                startRound();
            }
        });

    }

    public void setMaxPoints(int maxPoints){
        assert maxPoints > 0;
        this.maxPoints = maxPoints;
    }

    private void startRound(){

        if(this.players[0].getPoints() == maxPoints)
            gameOver();

        if(this.players[1].getPoints() == maxPoints)
            gameOver();

        this.leftPoint.setText(String.format("%d",this.players[0].getPoints()));
        this.rightPoint.setText(String.format("%d",this.players[1].getPoints()));

        // https://stackoverflow.com/questions/52580865/javafx-multiple-keylisteners-at-once
        final List<KeyCode> acceptedCodes = Arrays.asList(KeyCode.S, KeyCode.W, KeyCode.UP, KeyCode.DOWN);
        final Set<KeyCode> codes = new HashSet<>();
        manager.getScene().setOnKeyReleased(e -> {
            codes.clear();
        });
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
            this.lastWinnerPlayer = false;
            resetRound();
        }
        if(this.ball.isRightLimit()) {
            this.lastWinnerPlayer = true;
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