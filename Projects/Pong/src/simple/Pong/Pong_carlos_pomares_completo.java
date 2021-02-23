package simple.Pong;

/*

    Project     PROG21-FX
    Package     simple.Pong
    
    Version     1.0      
    Author      Carlos Pomares
    Date        2021-02-12

    DESCRIPTION
    
*/

import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

/**
 * @author Carlos Pomares
 */

public class Pong_carlos_pomares_completo extends Application {

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
    public PongScene entryScene;
    public OptionsScene optionsScene;
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
        this.optionsScene = new OptionsScene(this.WIDTH,this.HEIGHT,this);
        this.gameScene = new GameScene(this.WIDTH,this.HEIGHT,this);
        this.overScene = new OverScene(this.WIDTH,this.HEIGHT,this);

        // SCENE PROPERTIES IF EXISTS
        this.gameScene.setMaxPoints(this.WINNER_ROUNDS);

        // REGISTER AVAILABLE SCENES
        this.manager.registerScene(entryScene);
        this.manager.registerScene(optionsScene);
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
    private final Pong_carlos_pomares_completo toManage;
    private PongScene currentScene;
    private final ArrayList<PongScene> scenes;

    public SceneManager(Pong_carlos_pomares_completo bind){
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
    public void loadComponents();
    public void unloadComponents();
    public void relocateComponents();
    public void run();
    public void bindManager(SceneManager manager) throws Exception;
    public Parent getScene();
}

abstract class PongScene implements Component {

    private final int WIDTH;
    private final int HEIGHT;
    private final Pane ROOT;
    protected SceneManager manager;
    protected Pong_carlos_pomares_completo parent;

    public PongScene(int width, int height, Pong_carlos_pomares_completo parent){
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
    public void unloadComponents() {
        getRoot().getChildren().clear();
    }

    @Override
    public void run() {
        unloadComponents();
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

    public EntryScene(int width, int height, Pong_carlos_pomares_completo parent){
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
        this.optionsChange.setOnAction(e -> goToOptions());
        this.exitOption.setOnAction(e -> exitApplication());

    }

    private void startGameHandler(){
        this.manager.changeScene(this.parent.gameScene);
    }

    private void goToOptions() {

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(this.getRoot().translateXProperty(),-this.getWidth(),Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.millis(1000),kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();

        timeline.setOnFinished(e -> {
            this.getRoot().translateXProperty().set(0);
            this.manager.changeScene(this.parent.optionsScene);
        });

    }

    private void exitApplication(){
        this.parent.getStage().close();
    }

}

class OptionsScene extends PongScene {

    // COMPONENTS
    private Label sceneTitle;
    private Region backArrow;

    private Font optionsTitleFont = Font.font("Arial",FontWeight.BOLD,18);
    private Font radioFont = Font.font("Arial",FontWeight.BOLD,14);
    private Font optionsFont = Font.font("Arial",FontWeight.BOLD,12);
    private Label gameOptionsTitle;
    private StackPane gameOptions;
    private Label playerOptionsTitle;
    private StackPane playerOptions;

    // PLAYER OPTIONS
    private Pane playerContainer;
    private RadioButton player0;
    private RadioButton player1;
    private ToggleGroup playerGroup;
    private Label playerNameLabel;
    private TextField playerName;
    private Label playerColorLabel;
    private ColorPicker playerColor;
    private Button playerSubmit;

    // GAME OPTIONS
    private Pane velocityContainer;
    private RadioButton defaultVelocity;
    private RadioButton maxVelocity;
    private ToggleGroup velocityGroup;
    private Label velocityLabel;
    private Slider velocitySlider;
    private TextField maxVelocityField;
    private Button velocitySubmit;

    private Label maxPointsLabel;
    private TextField maxPointsField;

    private boolean playerSelection = true;
    private boolean velocitySelection = true;

    private ColorPicker colorPicker;

    public OptionsScene(int width, int height, Pong_carlos_pomares_completo parent) {
        super(width, height, parent);
    }

    @Override
    public void generateComponents() {

        this.getRoot().setStyle("-fx-background-color: white");

        // Title
        this.sceneTitle = new Label("Options Menu");
        this.sceneTitle.setFont(Font.font("Arial",FontWeight.BOLD,42));

        // Arrow
        this.backArrow = generateArrow(50,40,"black");

        // PLAYER OPTIONS
        this.playerOptionsTitle = new Label("Player Options");
        this.playerOptionsTitle.setFont(this.optionsTitleFont);
        this.playerOptions = new StackPane();
        this.playerOptions.setPrefSize(300,350);

        this.playerContainer = new Pane();
        this.playerContainer.setMaxSize(250,250);

        this.playerGroup = new ToggleGroup();

        this.player0 = new RadioButton();
        this.player0.setText("Player 0");
        this.player0.setUserData("player0");
        this.player0.setFont(this.radioFont);
        this.player0.setSelected(true);
        this.player0.setToggleGroup(this.playerGroup);

        this.player1 = new RadioButton();
        this.player1.setText("Player 1");
        this.player1.setUserData("player1");
        this.player1.setFont(this.radioFont);
        this.player1.setToggleGroup(this.playerGroup);

        this.playerName = new TextField();
        this.playerColor = new ColorPicker();

        this.playerNameLabel = new Label("Player name:");
        this.playerNameLabel.setFont(this.optionsFont);
        this.playerNameLabel.setLabelFor(this.playerName);

        this.playerColorLabel = new Label("Player color:");
        this.playerColorLabel.setFont(this.optionsFont);
        this.playerColorLabel.setLabelFor(this.playerColor);

        this.playerSubmit = new Button("Set changes");
        this.playerSubmit.setFont(this.radioFont);
        this.playerSubmit.setMinSize(60,30);

        // VELOCITY OPTIONS
        this.gameOptionsTitle = new Label("Game Options");
        this.gameOptionsTitle.setFont(this.optionsTitleFont);
        this.gameOptions = new StackPane();
        this.gameOptions.setPrefSize(300,350);

        this.velocityContainer = new Pane();
        this.velocityContainer.setMinSize(250,250);

        this.velocityGroup = new ToggleGroup();

        this.defaultVelocity = new RadioButton();
        this.defaultVelocity.setText("Default Velocity");
        this.defaultVelocity.setUserData("default");
        this.defaultVelocity.setFont(this.radioFont);
        this.defaultVelocity.setSelected(true);
        this.defaultVelocity.setToggleGroup(this.velocityGroup);

        this.maxVelocity = new RadioButton();
        this.maxVelocity.setText("Maximum Velocity");
        this.maxVelocity.setUserData("maximum");
        this.maxVelocity.setFont(this.radioFont);
        this.maxVelocity.setToggleGroup(this.velocityGroup);

        this.velocityLabel = new Label("2");
        this.velocityLabel.setLabelFor(this.velocitySlider);

        this.maxVelocityField = new TextField();
        this.maxVelocityField.setText("5");
        this.maxVelocityField.setMinSize(200,20);

        this.velocitySlider = new Slider(1,4,1);
        this.velocitySlider.setShowTickLabels(true);
        this.velocitySlider.setBlockIncrement(1.0);
        this.velocitySlider.setMinorTickCount(0);
        this.velocitySlider.setMajorTickUnit(1);
        this.velocitySlider.setSnapToTicks(true);
        this.velocitySlider.setMinSize(200,10);

        this.maxPointsLabel = new Label("Maximum Points:");
        this.maxPointsLabel.setFont(this.radioFont);

        this.maxPointsField = new TextField();
        this.maxPointsField.setMinSize(200,20);

        this.velocitySubmit = new Button("Set changes.");
        this.velocitySubmit.setFont(this.radioFont);
        this.velocitySubmit.setMinSize(60,30);

    }

    @Override
    public void loadComponents() {

        this.getRoot().getChildren().addAll(
                this.sceneTitle
                ,this.backArrow
        );

        this.getRoot().getChildren().addAll(
                this.playerOptionsTitle,
                this.gameOptionsTitle,
                this.playerOptions,
                this.gameOptions
        );

        this.playerOptions.getChildren().addAll(
                this.player0,
                this.player1,
                this.playerContainer
        );

        this.playerContainer.getChildren().addAll(
                this.playerNameLabel
                ,this.playerColorLabel
                ,this.playerName
                ,this.playerColor
                ,this.playerSubmit
        );

        this.gameOptions.getChildren().addAll(
                this.defaultVelocity,
                this.maxVelocity,
                this.velocityContainer
        );

        this.velocityContainer.getChildren().addAll(
                this.maxVelocityField
                ,this.velocityLabel
                ,this.velocitySlider
                ,this.maxPointsLabel
                ,this.maxPointsField
                ,this.velocitySubmit
        );

    }

    @Override
    public void relocateComponents() {

        this.sceneTitle.relocate(
                ((double) this.getWidth() / 2 - 130),
                50
        );

        this.backArrow.relocate(
                50,
                55
        );

        this.playerOptionsTitle.relocate(
                135
                , 150
        );

        this.playerOptions.relocate(
                50
                , 200
        );

        StackPane.setAlignment(this.player0,Pos.TOP_LEFT);
        StackPane.setAlignment(this.player1,Pos.TOP_RIGHT);
        StackPane.setAlignment(this.playerContainer,Pos.CENTER);

        this.playerNameLabel.relocate(80,20);
        this.playerName.relocate(40,50);

        this.playerColorLabel.relocate(80,90);
        this.playerColor.relocate(50,120);

        this.playerSubmit.relocate(65,170);

        this.gameOptionsTitle.relocate(
                this.getWidth() - this.gameOptions.getPrefWidth() + 45,
                150
        );

        this.gameOptions.relocate(
                this.getWidth() - this.gameOptions.getPrefWidth() - 50
                ,200
        );

        StackPane.setAlignment(this.defaultVelocity,Pos.TOP_LEFT);
        this.defaultVelocity.toFront();
        StackPane.setAlignment(this.maxVelocity,Pos.TOP_RIGHT);
        this.maxVelocity.toFront();
        StackPane.setAlignment(this.velocityContainer,Pos.CENTER);

        this.maxVelocityField.relocate(50,50);
        this.velocityLabel.relocate(150,50);
        this.velocitySlider.relocate(50,90);

        this.maxPointsLabel.relocate(100,150);
        this.maxPointsField.relocate(50,175);

        this.velocitySubmit.relocate(100,220);

    }

    @Override
    public void run() {
        super.run();

        arrowFunction();

        this.velocitySlider.valueProperty().addListener(((observableValue, number, t1) -> {
            this.velocityLabel.setText(t1.toString());
        }));

        this.playerGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle t1) {
                if(t1.getUserData().equals("player0")){
                    playerSelection = true;
                } else {
                    playerSelection = false;
                }
                updateSelection();
            }
        });

        this.velocityGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle t1) {
                if(t1.getUserData().equals("default")){
                    velocitySelection = true;
                } else {
                    velocitySelection = false;
                }
                updateSelection();
            }
        });

        this.playerSubmit.setOnAction(e -> setPlayerOptions());
        this.velocitySubmit.setOnAction(e -> setGameOptions());

        updateSelection();

    }

    private void updateSelection(){

        if(this.playerSelection){
            this.playerName.setText(retrievePlayers()[0].getName());
            this.playerColor.setValue((Color) retrievePlayers()[0].getSprite().getFill());
        } else {
            this.playerName.setText(retrievePlayers()[1].getName());
            this.playerColor.setValue((Color) retrievePlayers()[1].getSprite().getFill());
        }

        if(this.velocitySelection){
            this.velocitySlider.setVisible(true);
            this.velocityLabel.setVisible(true);
            this.velocitySlider.setValue(retrieveBall().getVelocity());
            this.maxVelocityField.setVisible(false);
        } else {
            this.velocitySlider.setVisible(false);
            this.velocityLabel.setVisible(false);
            this.maxVelocityField.setVisible(true);
            this.maxVelocityField.setText(String.valueOf(retrieveBall().getMaxVelocity()));
        }

        this.maxPointsField.setText(String.valueOf(this.parent.gameScene.getMaxPoints()));

    }

    private void setPlayerOptions(){
        if(this.playerSelection){
            this.retrievePlayers()[0].setName(this.playerName.getText());
            this.retrievePlayers()[0].setColor(this.playerColor.getValue());
        } else {
            this.retrievePlayers()[1].setName(this.playerName.getText());
            this.retrievePlayers()[1].setColor(this.playerColor.getValue());
        }
    }

    private void setGameOptions(){
        if(this.velocitySelection){
            this.retrieveBall().setDefaultVelocity(this.velocitySlider.getValue());
        } else {
            double filterNumber = this.retrieveBall().getMaxVelocity();
            try {
                filterNumber = Double.parseDouble(this.maxVelocityField.getText());
            } catch (NumberFormatException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input error.");
                alert.setHeaderText("Look your maximum velocity field.");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
            this.retrieveBall().setMaxVelocity(filterNumber);
        }

        int filteredNumber = this.parent.gameScene.getMaxPoints();

        try {
            filteredNumber = Integer.parseInt(this.maxPointsField.getText());
        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input error.");
            alert.setHeaderText("Look your maximum points field.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

        this.parent.gameScene.setMaxPoints(filteredNumber);

    }

    private GameScene.Player[] retrievePlayers(){
        return this.parent.gameScene.players;
    }

    private GameScene.Ball retrieveBall(){
        return this.parent.gameScene.ball;
    }

    private void arrowFunction(){
        ScaleTransition arrowScale = this.animateScale(backArrow);

        this.backArrow.setOnMouseEntered(e -> {
            arrowScale.play();
        });

        this.backArrow.setOnMouseExited(e -> {
            arrowScale.jumpTo(Duration.ZERO);
            arrowScale.stop();
        });

        this.backArrow.setOnMouseClicked(e -> {
            goToTitleScreen();
        });
    }

    private Region generateArrow(int minWidth, int minHeight, String fxColor){
        Region arrow = new Region();
        SVGPath svg = new SVGPath();
        svg.setContent(
                "M420.361,192.229c-1.83-0.297-3.682-0.434-5.535-0.41H99.305l6.88-3.2c6.725-3.183,12.843-7.515,18.08-12.8l88.48-88.48,c11.653-11.124,13.611-29.019,4.64-42.4c-10.441-14.259-30.464-17.355-44.724-6.914c-1.152,0.844-2.247,1.764-3.276,2.754,l-160,160C-3.119,213.269-3.13,233.53,9.36,246.034c0.008,0.008,0.017,0.017,0.025,0.025l160,160,c12.514,12.479,32.775,12.451,45.255-0.063c0.982-0.985,1.899-2.033,2.745-3.137c8.971-13.381,7.013-31.276-4.64-42.4,l-88.32-88.64c-4.695-4.7-10.093-8.641-16-11.68l-9.6-4.32h314.24c16.347,0.607,30.689-10.812,33.76-26.88,C449.654,211.494,437.806,195.059,420.361,192.229z"
        );
        arrow.setShape(svg);
        arrow.setMinSize(minWidth,minHeight);
        arrow.setStyle(String.format("-fx-background-color: %s",fxColor));
        return arrow;
    }

    private void goToTitleScreen(){

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(this.getRoot().translateXProperty(),this.getWidth(),Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.millis(1000),kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();

        timeline.setOnFinished(e -> {
            this.getRoot().translateXProperty().set(0);
            this.manager.changeScene(this.parent.entryScene);
        });

    }

    private ScaleTransition animateScale(Node node){

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(1000),node);
        scaleTransition.setCycleCount(Animation.INDEFINITE);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setByX(0.2);
        scaleTransition.setByY(0.2);

        return scaleTransition;
    }

}

class GameScene extends PongScene {

    class Player {

        private double deltaY = 10,velocity = 1.5;
        private Rectangle sprite;
        private int points;
        private int collisionCounter = 0;
        private String name;

        public Player(int WIDTH,int HEIGHT,Color color,String playerName){
            this.sprite = new Rectangle(WIDTH,HEIGHT,color);
            this.sprite.setStyle("-fx-background-radius: 25px");
            this.name = playerName;
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

        public String getName(){
            return this.name;
        }

        public void setName(String name){
            this.name = name;
        }

        public void setColor(Color color){
            this.sprite.setFill(color);
        }

        public void addPoint(){
            this.points++;
        }

        public void resetPoints(){
            this.points = 0;
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

                ball.increaseCollision();
                ball.modifyX();

                if(collisionCounter > 1 && getSprite().getBoundsInParent().intersects(ball.getSprite().getBoundsInParent())){
                    ball.sprite.setLayoutX(
                            (ball.getDeltaX() < 0)
                            ? ball.sprite.getLayoutX() + 1 + (ball.sprite.getRadius() * 2)
                            : ball.sprite.getLayoutX() -  1 - (ball.sprite.getRadius() * 2)
                    );
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

        private double DEFAULT_VELOCITY = 1;
        private int consecutiveCollision = 0;
        private double maxVelocity = 5;
        private double deltaX, deltaY, velocity;
        private Circle sprite;

        private boolean leftLimit = false, rightLimit = false;

        public Ball(int radius, Color color){
            this.sprite = new Circle(radius,color);
            generateAngle();
        }

        public void setMaxVelocity(double maxVelocity){
            this.maxVelocity = maxVelocity;
        }

        public void setDefaultVelocity(double defaultVelocity){
            this.DEFAULT_VELOCITY = defaultVelocity;
        }

        public void modifyY(){
            this.deltaY *= -1;
        }

        public void modifyX(){
            this.deltaX *= -1;
        }

        public void accelerate(){
            if(this.velocity < this.maxVelocity)
                this.velocity++;
        }

        public void movement(Parent canvas){

            this.sprite.setLayoutX(this.sprite.getLayoutX() + deltaX * velocity);
            this.sprite.setLayoutY(this.sprite.getLayoutY() + deltaY * velocity);

            // RESIZE PROBLEM
            /*final Bounds limits = canvas.getBoundsInLocal();
            final boolean leftLimit = this.sprite.getLayoutX() <= (limits.getMinX() + this.sprite.getRadius());
            final boolean rightLimit = this.sprite.getLayoutX() >= (limits.getMaxX() - this.sprite.getRadius());
            final boolean upperLimit = this.sprite.getLayoutY() <= (limits.getMinY() + this.sprite.getRadius());
            final boolean lowerLimit = this.sprite.getLayoutY() >= (limits.getMaxY() - this.sprite.getRadius());*/

            // RESIZE SOLUTION
            final boolean leftLimit = this.sprite.getLayoutX() <= (0 + this.sprite.getRadius());
            final boolean rightLimit = this.sprite.getLayoutX() >= (getWidth() - this.sprite.getRadius());
            final boolean upperLimit = this.sprite.getLayoutY() <= (0 + this.sprite.getRadius());
            final boolean lowerLimit = this.sprite.getLayoutY() >= (getHeight() - this.sprite.getRadius());

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
            this.velocity = this.DEFAULT_VELOCITY;
            this.deltaX = this.velocity * Math.cos(angle);
            this.deltaY = this.velocity * Math.sin(angle);
        }

        public void randomRelocate(double height, double gap){
            double randomY = Math.random() * height;
            if(height < gap){
                this.sprite.relocate(this.sprite.getLayoutX(),randomY + gap);
            } else {
                this.sprite.relocate(this.sprite.getLayoutX(),randomY - gap);
            }
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
            return DEFAULT_VELOCITY;
        }

        public double getMaxVelocity(){
            return this.maxVelocity;
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
    public Player[] players;
    public Ball ball;
    private Timeline loop;

    // ADDITIONS
    private FireEmitter fireEffect;
    private ParticleSystem particleSystem;
    private Canvas fireEffectCanvas;

    // PROPERTIES
    final private int BALL_RADIUS = 15;
    final private double[] P1_POS;
    final private double[] P2_POS;
    private int maxPoints;
    private boolean lastWinnerPlayer = false;
    private boolean playing = true;

    // Components
    private Label leftPoint;
    private Label rightPoint;
    private Line midLine;
    private Label spaceRequirement;
    private Label gameOver;

    public GameScene(int width, int height, Pong_carlos_pomares_completo parent){
        super(width, height, parent);

        this.getRoot().setMaxHeight(this.getHeight());
        this.getRoot().setMaxWidth(this.getWidth());

        this.fireEffectCanvas = new Canvas(100,100);
        this.fireEffect = new FireEmitter(Color.rgb(15,167,178),15,1,0.5,0.5);
        this.particleSystem = new ParticleSystem(fireEffect,fireEffectCanvas);

        this.ball = new Ball(this.BALL_RADIUS,Color.WHITE);
        this.players = new Player[]{
                new Player(10,60,Color.WHITE,"Player 0"),
                new Player(10,60,Color.WHITE, "Player 1")
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

        this.lastWinnerPlayer = !(Math.random() * 1 > 0.5);

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

        this.gameOver = new Label("Game over");
        this.gameOver.setFont(Font.font("Arial",FontWeight.BOLD,52));
        this.gameOver.setTextFill(Color.WHITE);
        this.gameOver.setOpacity(0.0);

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

        this.getRoot().getChildren().addAll(
                this.spaceRequirement,
                this.gameOver
        );

        this.getRoot().getChildren().add(this.fireEffectCanvas);

    }

    @Override
    public void relocateComponents() {
        this.players[0].getSprite().relocate(this.P1_POS[0],this.P1_POS[1]);
        this.players[1].getSprite().relocate(this.P2_POS[0],this.P2_POS[1]);
        this.ball.relocateInMiddle(this.getRoot());
        this.ball.randomRelocate(this.getHeight(),50);

        this.fireEffectCanvas.relocate(this.ball.sprite.getLayoutX() - 50,this.ball.sprite.getLayoutY() - 50);

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

        this.gameOver.relocate(
                ((double) this.getWidth() / 2) - 130
                ,280
        );
        this.gameOver.toFront();

    }

    @Override
    public void run() {
        super.run();

        this.loop.stop();

        manager.getScene().setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.SPACE){
                this.spaceRequirement.setVisible(false);
                startRound();
            }
        });

        this.playing = true;
        this.ball.sprite.setVisible(true);
        this.players[0].resetPoints();
        this.players[1].resetPoints();

    }

    public void setMaxPoints(int maxPoints){
        assert maxPoints > 0;
        this.maxPoints = maxPoints;
    }

    public int getMaxPoints(){
        return this.maxPoints;
    }

    private void startRound(){

        this.ball.generateAngle();

        if(this.players[0].getPoints() == maxPoints || this.players[1].getPoints() == maxPoints) {
            this.playing = false;
        }

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

        if(this.playing) {
            loop.play();
            particleSystem.run();
        } else {
            gameOver();
        }

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

        this.fireEffectCanvas.setLayoutX(this.ball.sprite.getLayoutX() - 50);
        this.fireEffectCanvas.setLayoutY(this.ball.sprite.getLayoutY() - 50);

        if(this.ball.velocity > this.ball.DEFAULT_VELOCITY){
            this.fireEffectCanvas.setVisible(true);
            if(this.ball.deltaX > 0){
                this.fireEffect.xPoint = -0.5;
            } else {
                this.fireEffect.xPoint = 0.5;
            }

            if(this.ball.deltaY > 0){
                this.fireEffect.yPoint = -0.5;
            } else {
                this.fireEffect.yPoint = 0.5;
            }
        } else {
            this.fireEffectCanvas.setVisible(false);
        }

    }

    private void resetRound(){
        this.ball.resetProperties();
        this.ball.generateAngle();
        relocateComponents();
        startRound();
    }

    private void gameOver() {

        this.loop.stop();
        this.ball.sprite.setVisible(false);

        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(800),this.gameOver);
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000),this.gameOver);

        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);

        translateTransition.setByY(-30);
        translateTransition.setCycleCount(3);
        translateTransition.setAutoReverse(true);

        translateTransition.play();
        fadeTransition.play();

        translateTransition.setOnFinished(e -> {
            this.parent.overScene.setPlayers(this.players);
            this.manager.changeScene(this.parent.overScene);
        });

    }

}

class OverScene extends PongScene {

    static class AnimateNode {
        public static TranslateTransition translateNode(Duration duration, double setByY){
            TranslateTransition translateTransition = new TranslateTransition(duration);
            translateTransition.setByY(setByY);
            translateTransition.setCycleCount(1);
            return translateTransition;
        }
        public static FadeTransition fadeNode(Duration duration, double from, double to){
            FadeTransition fadeTransition = new FadeTransition(duration);
            fadeTransition.setFromValue(from);
            fadeTransition.setToValue(to);
            fadeTransition.setCycleCount(1);
            return fadeTransition;
        }
        public static ScaleTransition animateScale(Duration duration,int cycleCount,double setByX, double setByY,boolean autoreverse){
            ScaleTransition scaleTransition = new ScaleTransition(duration);
            scaleTransition.setCycleCount(cycleCount);
            scaleTransition.setAutoReverse(autoreverse);
            scaleTransition.setByX(setByX);
            scaleTransition.setByY(setByY);
            return scaleTransition;
        }
    }

    // OBJECTS
    private GameScene.Player[] players;

    // PARTICLES
    private Canvas canvas;
    private ConfettiEmitter confettiEffect;
    private ParticleSystem particleSystem;
    private Timeline particleIterator;

    // COMPONENTS
    final private Font tagNameFont = Font.font("Arial",FontWeight.BOLD,22);
    private Region backArrow;

    // PODIUM
    private Rectangle firstPosition;
    private Rectangle secondPosition;
    private Rectangle thirdPosition;

    private StackPane firstPlayerContainer;
    private StackPane secondPlayerContainer;
    private StackPane thirdPlayerContainer;

    private Circle firstPlayer;
    private Label firstPlayerTagname;

    private Circle secondPlayer;
    private Label secondPlayerTagname;

    private Circle thirdPlayer;
    private Label thirdPlayerTagname;

    // ANIMATIONS
    ScaleTransition arrowScale;
    FadeTransition arrowFade;

    TranslateTransition translateFirstPosition;
    TranslateTransition translateSecondPosition;
    TranslateTransition translateThirdPosition;

    FadeTransition fadeFirstPlayer;
    FadeTransition fadeSecondPlayer;
    FadeTransition fadeThirdPlayer;

    public OverScene(int width, int height, Pong_carlos_pomares_completo parent) {
        super(width, height, parent);

        arrowScale = AnimateNode.animateScale(Duration.millis(1000),Animation.INDEFINITE,0.2,0.2,true);
        arrowFade = AnimateNode.fadeNode(Duration.millis(1000),0.0,1.0);

        translateFirstPosition = AnimateNode.translateNode(Duration.millis(2000),-350);
        translateSecondPosition = AnimateNode.translateNode(Duration.millis(2000),-200);
        translateThirdPosition = AnimateNode.translateNode(Duration.millis(2000),-150);

        fadeFirstPlayer = AnimateNode.fadeNode(Duration.millis(3000),0.0,1.0);
        fadeSecondPlayer = AnimateNode.fadeNode(Duration.millis(3000),0.0,1.0);
        fadeThirdPlayer = AnimateNode.fadeNode(Duration.millis(3000),0.0,1.0);

    }

    @Override
    public void generateComponents() {

        this.getRoot().setStyle("-fx-background-color: white");

        this.backArrow = generateArrow(50,40,"black");
        this.backArrow.setOpacity(0.0);

        // PODIUM
        this.firstPosition = new Rectangle(250,350,Color.YELLOW);
        this.secondPosition = new Rectangle(200,200,Color.GRAY);
        this.thirdPosition = new Rectangle(200,150,Color.BROWN);

        // CONTAINER
        this.firstPlayerContainer = new StackPane();
        this.firstPlayerContainer.setPrefSize(150,150);
        this.firstPlayerContainer.setOpacity(0.0);

        this.secondPlayerContainer = new StackPane();
        this.secondPlayerContainer.setPrefSize(150,150);
        this.secondPlayerContainer.setOpacity(0.0);

        this.thirdPlayerContainer = new StackPane();
        this.thirdPlayerContainer.setPrefSize(150,150);
        this.thirdPlayerContainer.setOpacity(0.0);

        // PLAYERS
        this.firstPlayer = new Circle(45,Color.BLACK);
        this.firstPlayerTagname = new Label();
        this.firstPlayerTagname.setTextAlignment(TextAlignment.CENTER);
        this.firstPlayerTagname.setFont(tagNameFont);

        this.secondPlayer = new Circle(40,Color.BLACK);
        this.secondPlayerTagname = new Label();
        this.secondPlayerTagname.setTextAlignment(TextAlignment.CENTER);
        this.secondPlayerTagname.setFont(tagNameFont);

        this.thirdPlayer = new Circle(35,Color.rgb(15,230,56));
        this.thirdPlayerTagname = new Label("C. Pomares\nAuthor");
        this.thirdPlayerTagname.setTextAlignment(TextAlignment.CENTER);
        this.thirdPlayerTagname.setFont(tagNameFont);

        // PARTICLES
        this.canvas = new Canvas(this.getWidth(),this.getHeight());
        this.confettiEffect = new ConfettiEmitter(2,6,6,-1,2);
        this.particleSystem = new ParticleSystem(confettiEffect,canvas,BlendMode.EXCLUSION,this.canvas.getWidth() / 2, -100);

        setWinner();

    }

    @Override
    public void loadComponents() {

        // COMPONENTS
        this.getRoot().getChildren().add(backArrow);

        // PODIUM
        this.getRoot().getChildren().addAll(
                this.firstPosition,
                this.secondPosition,
                this.thirdPosition
        );

        // CONTAINERS
        this.getRoot().getChildren().addAll(
                this.firstPlayerContainer
                ,this.secondPlayerContainer
                ,this.thirdPlayerContainer
        );

        // PLAYERS
        this.firstPlayerContainer.getChildren().addAll(
                this.firstPlayer
                ,this.firstPlayerTagname
        );

        this.secondPlayerContainer.getChildren().addAll(
                this.secondPlayer
                ,this.secondPlayerTagname
        );

        this.thirdPlayerContainer.getChildren().addAll(
                this.thirdPlayer
                ,this.thirdPlayerTagname
        );

        // PARTICLES
        this.getRoot().getChildren().add(this.canvas);

        // ANIMATIONS
        this.translateFirstPosition.setNode(this.firstPosition);
        this.translateSecondPosition.setNode(this.secondPosition);
        this.translateThirdPosition.setNode(this.thirdPosition);

        this.fadeFirstPlayer.setNode(this.firstPlayerContainer);
        this.fadeSecondPlayer.setNode(this.secondPlayerContainer);
        this.fadeThirdPlayer.setNode(this.thirdPlayerContainer);

        arrowFade.setNode(this.backArrow);
        arrowScale.setNode(this.backArrow);

    }

    @Override
    public void relocateComponents() {

        this.backArrow.relocate(
                50,
                55
        );

        // PODIUM
        this.firstPosition.relocate(
                ((double) this.getWidth() / 2) - this.firstPosition.getWidth() / 2
                ,this.getHeight()
        );

        this.secondPosition.relocate(
                ((double) this.getWidth() / 2) - this.secondPosition.getWidth() / 2 + 200
                ,this.getHeight()
        );

        this.thirdPosition.relocate(
                ((double) this.getWidth() / 2) - this.thirdPosition.getWidth() / 2 - 200
                ,this.getHeight()
        );

        // CONTAINERS
        this.firstPlayerContainer.relocate(
                ((double) this.getWidth() / 2) - (this.firstPosition.getWidth() / 2) + (this.firstPosition.getWidth() / 2) - 75
                ,this.getHeight() - this.firstPosition.getHeight() - 160
        );

        this.secondPlayerContainer.relocate(
                (((double) this.getWidth() / 2) - (this.secondPosition.getWidth() / 2) + 200) + (this.secondPosition.getWidth() / 2) - 75
                ,this.getHeight() - this.secondPosition.getHeight() - 160
        );

        this.thirdPlayerContainer.relocate(
                (((double) this.getWidth() / 2) - (this.thirdPosition.getWidth() / 2) - 200) + (this.thirdPosition.getWidth() / 2) - 75
                ,this.getHeight() - this.thirdPosition.getHeight() - 160
        );

        // PLAYERS
        StackPane.setAlignment(this.firstPlayer,Pos.BOTTOM_CENTER);
        StackPane.setAlignment(this.firstPlayerTagname,Pos.TOP_CENTER);

        StackPane.setAlignment(this.secondPlayer,Pos.BOTTOM_CENTER);
        StackPane.setAlignment(this.secondPlayerTagname,Pos.TOP_CENTER);

        StackPane.setAlignment(this.thirdPlayer,Pos.BOTTOM_CENTER);
        StackPane.setAlignment(this.thirdPlayerTagname,Pos.TOP_CENTER);

    }

    @Override
    public void run() {
        super.run();
        loadAnimations();
    }

    public void setPlayers(GameScene.Player[] players){
        assert players != null;
        this.players = players;
    }

    private void setWinner(){
        if(players[0].getPoints() > players[1].getPoints()){
            setOrder(this.players[0],true);
            setOrder(this.players[1],false);
        } else {
            setOrder(this.players[1],true);
            setOrder(this.players[0],false);
        }
    }

    private void loadAnimations(){

        particleIterator = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                confettiEffect.xPoint = (Math.random() * 2 > 1) ? 1 : -1;
            }
        }));
        particleIterator.setCycleCount(Animation.INDEFINITE);


        translateFirstPosition.play();
        translateFirstPosition.setOnFinished(e -> translateSecondPosition.play());
        translateFirstPosition.setOnFinished(e -> {
            translateSecondPosition.play();
        });
        translateSecondPosition.setOnFinished(e -> {
            translateThirdPosition.play();
        });
        translateThirdPosition.setOnFinished(e -> {
            translateThirdPosition.stop();
            this.particleSystem.run();
            particleIterator.play();
            fadeFirstPlayer.play();
        });

        fadeFirstPlayer.setOnFinished(e -> {
            fadeFirstPlayer.stop();
            fadeSecondPlayer.play();
        });
        fadeSecondPlayer.setOnFinished(e -> {
            fadeSecondPlayer.stop();
            fadeThirdPlayer.play();
        });
        fadeThirdPlayer.setOnFinished(e -> {
            fadeThirdPlayer.stop();
            this.particleSystem.timer.stop();
            particleIterator.stop();
            this.getRoot().getChildren().remove(this.canvas);
            arrowFade.play();
        });

        this.backArrow.setOnMouseEntered(e -> arrowScale.play());
        this.backArrow.setOnMouseExited(e -> {
            arrowScale.jumpTo(Duration.ZERO);
            arrowScale.stop();
        });

        this.backArrow.setOnMouseClicked(e -> goBackToTitle());

    }

    private void setOrder(GameScene.Player player, boolean winner){
        if(winner){
            setPlayerProperties(player,"1st Position",this.firstPlayerTagname,this.firstPlayer);
        } else {
            setPlayerProperties(player,"2nd Position",this.secondPlayerTagname,this.secondPlayer);
        }
    }

    private void setPlayerProperties(GameScene.Player player,String position,Label tagName,Circle playerBall){
        tagName.setText(String.format("%s\n%s",player.getName(),position));
        if(player.getSprite().getFill() == Color.WHITE){
            playerBall.setFill(Color.GREEN);
        } else {
            playerBall.setFill(player.getSprite().getFill());
        }
    }

    private void goBackToTitle(){

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(this.getRoot().translateXProperty(),this.getWidth(),Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.millis(1000),kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();

        timeline.setOnFinished(e -> {
            this.getRoot().translateXProperty().set(0);
            this.manager.changeScene(this.parent.entryScene);
        });

    }

    private Region generateArrow(int minWidth, int minHeight, String fxColor){
        Region arrow = new Region();
        SVGPath svg = new SVGPath();
        svg.setContent(
                "M420.361,192.229c-1.83-0.297-3.682-0.434-5.535-0.41H99.305l6.88-3.2c6.725-3.183,12.843-7.515,18.08-12.8l88.48-88.48,c11.653-11.124,13.611-29.019,4.64-42.4c-10.441-14.259-30.464-17.355-44.724-6.914c-1.152,0.844-2.247,1.764-3.276,2.754,l-160,160C-3.119,213.269-3.13,233.53,9.36,246.034c0.008,0.008,0.017,0.017,0.025,0.025l160,160,c12.514,12.479,32.775,12.451,45.255-0.063c0.982-0.985,1.899-2.033,2.745-3.137c8.971-13.381,7.013-31.276-4.64-42.4,l-88.32-88.64c-4.695-4.7-10.093-8.641-16-11.68l-9.6-4.32h314.24c16.347,0.607,30.689-10.812,33.76-26.88,C449.654,211.494,437.806,195.059,420.361,192.229z"
        );
        arrow.setShape(svg);
        arrow.setMinSize(minWidth,minHeight);
        arrow.setStyle(String.format("-fx-background-color: %s",fxColor));
        return arrow;
    }

}

/*
 *
 *   PARTICLE SYSTEM
 *
 * */

abstract class Emitter {
    public abstract List<Particle> emit(double x, double y);
}

class Particle {

    private double x;
    private double y;

    private Point2D velocity;

    private double radius;
    private double life = 1.0;
    private double decay;

    private Paint color;
    private BlendMode blendMode;

    public Particle(double x, double y, Point2D velocity, double radius, double expireTime, Paint color, BlendMode blendMode) {
        this.x = x;
        this.y = y;
        this.velocity = velocity;
        this.radius = radius;
        this.decay = 0.016 / expireTime;
        this.color = color;
        this.blendMode = blendMode;
    }

    public boolean isAlive(){
        return life > 0;
    }

    public void update(){
        x += velocity.getX();
        y += velocity.getY();
        life -= decay;
    }

    public void render(GraphicsContext g){
        g.setGlobalAlpha(life);
        g.setGlobalBlendMode(blendMode);
        g.setFill(color);
        g.fillOval(x,y,radius,radius);
    }

}

class ParticleSystem {

    final private Emitter EMITTER;
    final private Canvas CANVAS;
    final private List<Particle> PARTICLES = new ArrayList<>();

    private boolean custom = false;
    private double particlesX, particlesY;
    private GraphicsContext g;
    public AnimationTimer timer;

    public ParticleSystem(Emitter emitter, Canvas canvas){
        this.EMITTER = emitter;
        this.CANVAS = canvas;
        this.CANVAS.setBlendMode(BlendMode.SCREEN);
    }

    public ParticleSystem(Emitter emitter, Canvas canvas,BlendMode blendMode){
        this.EMITTER = emitter;
        this.CANVAS = canvas;
        this.CANVAS.setBlendMode(blendMode);
    }

    public ParticleSystem(Emitter emitter, Canvas canvas,BlendMode blendMode,double particleX, double particleY){
        this.EMITTER = emitter;
        this.CANVAS = canvas;
        this.CANVAS.setBlendMode(blendMode);
        this.particlesX = particleX;
        this.particlesY = particleY;
        this.custom = true;
    }

    private void onUpdate(){

        g.setGlobalAlpha(1.0);
        g.setGlobalBlendMode(BlendMode.SRC_OVER);
        g.setFill(Color.BLACK);
        g.fillRect(0,0,this.CANVAS.getWidth(),this.CANVAS.getHeight());

        // PARTICLES CONST
        if(this.custom){
            this.PARTICLES.addAll(this.EMITTER.emit(
                    this.particlesX,
                    this.particlesY
            ));
        } else {
            this.PARTICLES.addAll(this.EMITTER.emit(
                    this.CANVAS.getWidth() / 2,
                    this.CANVAS.getHeight() / 2
            ));
        }

        for(Iterator<Particle> it = this.PARTICLES.iterator(); it.hasNext();){
            Particle p = it.next();
            p.update();
            if(!p.isAlive()){
                it.remove();
                continue;
            }
            p.render(g);
        }

    }

    public void run(){

        g = this.CANVAS.getGraphicsContext2D();

        this.timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                onUpdate();
            }
        };

        timer.start();

    }

}

/*
 *
 *   EFFECTS
 *
 * */

class FireEmitter extends Emitter {

    final private Color COLOR;
    final private int NUM_PARTICLES;
    final private int LIFETIME;

    public double xPoint, yPoint;

    public FireEmitter(Color color, int numPartciles, int lifetime, double xPoint, double yPoint) {
        this.COLOR = color;
        this.NUM_PARTICLES = numPartciles;
        this.LIFETIME = lifetime;
        this.xPoint = xPoint;
        this.yPoint = yPoint;
    }

    @Override
    public List<Particle> emit(double x, double y) {
        List<Particle> particles = new ArrayList<>();
        for (int i = 0; i < this.NUM_PARTICLES; i++) {
            Particle particle = new Particle(x,y,new Point2D( Math.random() * xPoint,Math.random() * yPoint),
                    10,this.LIFETIME,this.COLOR,BlendMode.ADD);
            particles.add(particle);
        }
        return particles;
    }

}

class BubbleEmitter extends Emitter {

    final private Color COLOR;
    final private int NUM_PARTICLES;
    final private int LIFETIME;
    final private int RADIUS;

    public double xPoint, yPoint;

    public BubbleEmitter(Color color, int numParticles, int lifetime, double xPoint, double yPoint, int radius) {
        this.COLOR = color;
        this.NUM_PARTICLES = numParticles;
        this.LIFETIME = lifetime;
        this.RADIUS = radius;
        this.xPoint = xPoint;
        this.yPoint = yPoint;
    }

    @Override
    public List<Particle> emit(double x, double y) {
        List<Particle> particles = new ArrayList<>();
        for (int i = 0; i < this.NUM_PARTICLES; i++) {
            Particle particle = new Particle(x,y,new Point2D( (Math.random() * xPoint),(Math.random() * yPoint)),
                    this.RADIUS,this.LIFETIME,this.COLOR,BlendMode.LIGHTEN);
            particles.add(particle);
        }
        return particles;
    }

}

class ConfettiEmitter extends Emitter {

    final private int NUM_PARTICLES;
    final private int LIFETIME;
    final private int RADIUS;
    public double xPoint;
    public double yPoint;

    public ConfettiEmitter(int numParticles, int lifetime, int radius, double x, double y) {
        this.NUM_PARTICLES = numParticles;
        this.LIFETIME = lifetime;
        this.RADIUS = radius;
        this.xPoint = x;
        this.yPoint = y;
    }

    @Override
    public List<Particle> emit(double x, double y) {
        List<Particle> particles = new ArrayList<>();
        for (int i = 0; i < this.NUM_PARTICLES; i++) {
            Particle particle = new Particle(x,y,new Point2D( (Math.random() * this.xPoint),(Math.random() * yPoint)),
                    this.RADIUS,this.LIFETIME,Color.rgb((int) (Math.random() * 255),(int) (Math.random() * 255), (int) (Math.random() * 255)),BlendMode.LIGHTEN);
            particles.add(particle);
        }
        return particles;
    }
}