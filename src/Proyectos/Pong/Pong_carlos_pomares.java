package Proyectos.Pong;

/*

    Project     PROG21-FX
    Package     Proyectos.Pong    
    
    Version     1.0      
    Author      Carlos Pomares
    Date        2021-02-12

    DESCRIPTION
    
*/

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * @author Carlos Pomares
 */

public class Pong_carlos_pomares extends Application {

    // WINDOW PROPERTIES
    private final int HEIGHT;
    private final int WIDTH;

    // PONG
    private Scene scene;

    // SCENES
    private SceneManager manager;
    private EntryScene entryScene;

    // ENTRY PROPERTIES

    // GAME PROPERTIES
    private final int WINNER_ROUNDS = 15;

    // OVER PROPERTIES

    @Override
    public void start(Stage stage) throws Exception {
        //Pong_carlos_pomares pong = new Pong_carlos_pomares(stage,800,600);

        Pane root = new Pane();

        // SCENE
        this.scene = new Scene(root,800,600);

        // STAGE
        stage.setScene(scene);
        //stage.setTitle("Pong");
        stage.show();

    }

    public Pong_carlos_pomares(Stage stage, int width,int height){

        // ASSIGN WIDTH AND HEIGHT
        this.WIDTH = width;
        this.HEIGHT = height;

    }

    // ENTRY SCENE
    private void entryScene(){}

    // GAME SCENE
    private void gameScene(){}

    // GAME OVER SCENE
    private void overScreen(){}

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
    private PongScene currentScene;
    private final ArrayList<PongScene> scenes;

    public SceneManager(Scene mainScene){
        assert mainScene != null;
        this.scene = mainScene;
        this.scenes = new ArrayList<>();
    }

    private void loadScene() {
        assert getCurrentScene() != null;
        this.scene.setRoot(getCurrentScene().getScene());
    }

    private void unloadScene(){
        this.scene.setRoot(null);
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

}

/*
*
*   SCENES
*
* */

interface Component {
    public void generateComponents();
    public void loadComponents();
    public void run();
    public Parent getScene();
}

abstract class PongScene implements Component {

    private final int WIDTH;
    private final int HEIGHT;
    private final Pane ROOT;

    public PongScene(int width, int height){
        this.WIDTH = width;
        this.HEIGHT = height;
        this.ROOT = new Pane();
    }

    public int getWidth() {
        return this.WIDTH;
    }

    public int getHeight() {
        return this.HEIGHT;
    }

    public Pane getRoot(){
        return this.ROOT;
    }

}

class EntryScene extends PongScene {

    // ENTRY COMPONENTS
    private Circle circle;

    public EntryScene(int width, int height){
        super(width,height);
    }

    @Override
    public void generateComponents() {

        this.circle = new Circle(50, Color.RED);

    }

    @Override
    public void loadComponents() {

        this.getRoot().getChildren().add(this.circle);

    }

    @Override
    public void run() {



    }

    @Override
    public Parent getScene() {
        return this.getRoot();
    }

}



class Player {

    public Player(){}

}

class Ball {

    public Ball(){}

}