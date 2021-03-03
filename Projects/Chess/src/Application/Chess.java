package Application;/*

    Project     PROG21-FX
        
    
    Version     1.0      
    Author      Carlos Pomares
    Date        2021-03-02

    DESCRIPTION
    
*/

import Managers.SceneManager;
import Scenes.Game;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Carlos Pomares
 */

public class Chess extends Application {

    // WINDOW PROPERTIES
    private int HEIGHT;
    private int WIDTH;

    public final String TITLE = "Chess"
            ,VERSION = "1.0"
            , AUTHOR = "Carlos Pomares";

    // STAGE
    private Stage stage;

    // MANAGER
    private SceneManager manager;
    private Game gameScene;

    @Override
    public void start(Stage stage) throws Exception {

        // SET STAGE
        this.stage = stage;

        // ASSIGN WIDTH AND HEIGHT
        this.WIDTH = 800;
        this.HEIGHT = 600;

        // SCENE MANAGER
        this.manager = new SceneManager(this);

        // INSTACE SCENES
        this.gameScene = new Game(this.WIDTH,this.HEIGHT,this);

        // REGISTER SCENES
        this.manager.registerScene(gameScene);

        // LOAD FIRST SCENE
        this.manager.changeScene(gameScene);

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
