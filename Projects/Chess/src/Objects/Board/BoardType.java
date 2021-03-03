package Objects.Board;

/*

    Project     PROG21-FX
    Package     Objects.Board    
    
    Version     1.0      
    Author      Carlos Pomares
    Date        2021-03-02

    DESCRIPTION
    
*/

import javafx.scene.paint.Color;

/**
 * @author Carlos Pomares
 */

public enum BoardType {
    BLACK(Color.BLACK),
    WHITE(Color.WHITE);

    final private Color typeColor;

    BoardType(Color typeColor) {
        this.typeColor = typeColor;
    }

    public Color getTypeColor() {
        return typeColor;
    }

}
