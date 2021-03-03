package Static;

/*

    Project     PROG21-FX
    Package     Static    
    
    Version     1.0      
    Author      Carlos Pomares
    Date        2021-03-02

    DESCRIPTION
    
*/

import javafx.scene.input.MouseEvent;

/**
 * @author Carlos Pomares
 */

public interface MouseListener {
    public void mouseAssigment();
    public void onMouseClicked(MouseEvent event);
    public void onMouseEntered(MouseEvent event);
    public void onMouseExited(MouseEvent event);
    public void onMouseMoved(MouseEvent event);
    public void onMousePressed(MouseEvent event);
    public void onMouseReleased(MouseEvent event);
    public void onMouseDragEntered(MouseEvent event);
    public void onMouseDragExited(MouseEvent event);
    public void onMouseDragged(MouseEvent event);
    public void onMouseDragOver(MouseEvent event);
    public void onMouseDragReleased(MouseEvent event);
}
