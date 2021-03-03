package Managers;

/*

    Project     PROG21-FX
    Package     Managers    
    
    Version     1.0      
    Author      Carlos Pomares
    Date        2021-03-02

    DESCRIPTION
    
*/

import Application.Chess;
import Static.ChessScene;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * @author Carlos Pomares
 */

public class SceneManager {

    private final Scene scene;
    private final Chess toManage;
    private ChessScene currentScene;
    private final ArrayList<ChessScene> scenes;

    public SceneManager(Chess bind){
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

    public void removeScene(ChessScene scene)
            throws NoSuchElementException {
        assert scene != null;
        if(!this.scenes.contains(scene)){
            throw new NoSuchElementException();
        }
        unloadScene();
        this.scenes.remove(scene);
    }

    public void registerScene(ChessScene scene)
            throws Exception {
        assert scene != null;
        if(this.scenes.contains(scene)){
            throw new Exception();
        }
        this.scenes.add(scene);
        scene.bindManager(this);
    }

    public ChessScene getCurrentScene()
            throws NullPointerException {
        if(this.currentScene == null){
            throw new NullPointerException();
        }
        return this.currentScene;
    }

    public void changeScene(ChessScene scene)
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