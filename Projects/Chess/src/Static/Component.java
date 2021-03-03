package Static;

/*

    Project     PROG21-FX
    Package     Scenes    
    
    Version     1.0      
    Author      Carlos Pomares
    Date        2021-03-02

    DESCRIPTION
    
*/

import Managers.SceneManager;
import javafx.scene.Parent;

/**
 * @author Carlos Pomares
 */

interface Component {
    public void generateComponents();
    public void loadComponents();
    public void unloadComponents();
    public void relocateComponents();
    public void run();
    public void bindManager(SceneManager manager) throws Exception;
    public Parent getScene();
}
