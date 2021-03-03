package Objects.Entity;

/*

    Project     PROG21-FX
    Package     Objects.Entity    
    
    Version     1.0      
    Author      Carlos Pomares
    Date        2021-03-02

    DESCRIPTION
    
*/

/**
 * @author Carlos Pomares
 */

public enum PieceImage {
    KING("/resources/sprite/king.png"),
    QUEEN("/resources/sprite/queen.png"),
    ROOK("/resources/sprite/rook.png"),
    BISHOP("/resources/sprite/bishop.png"),
    KNIGHT("/resources/sprite/knight.png"),
    PAWN("/resources/sprite/pawn.png");

    final private String resource;

    PieceImage(String resource){
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }

}
