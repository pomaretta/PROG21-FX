package Static;

/*

    Project     PROG21-FX
    Package     Scenes    
    
    Version     1.0      
    Author      Carlos Pomares
    Date        2021-03-02

    DESCRIPTION
    
*/

import Application.Chess;
import Managers.SceneManager;
import Static.Component;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

/**
 * @author Carlos Pomares
 */

public abstract class ChessScene implements Component {

    private final int WIDTH;
    private final int HEIGHT;
    private final Pane ROOT;
    protected SceneManager manager;
    protected Chess parent;

    public ChessScene(int width, int height, Chess parent){
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
