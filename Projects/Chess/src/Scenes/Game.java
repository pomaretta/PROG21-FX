package Scenes;

/*

    Project     PROG21-FX
    Package     Scenes    
    
    Version     1.0      
    Author      Carlos Pomares
    Date        2021-03-02

    DESCRIPTION
    
*/

import Application.Chess;
import Objects.Board.BoardType;
import Objects.Board.Piece;
import Objects.Board.PieceLetter;
import Static.ChessScene;

import java.util.ArrayList;

/**
 * @author Carlos Pomares
 */

public class Game extends ChessScene {

    private ArrayList<Piece> board;

    private Piece selectedPiece;
    private Piece targetPiece;

    public Game(int width, int height, Chess parent) {
        super(width, height, parent);
        board = new ArrayList<>();
        getRoot().setFocusTraversable(true);
    }

    @Override
    public void generateComponents() {
        generateBoard(50,50,50,50,board);
        getRoot().setStyle("-fx-background-color: red");
    }

    @Override
    public void loadComponents() {
        board.forEach(piece -> getRoot().getChildren().add(piece.getSprite()));
    }

    @Override
    public void relocateComponents() {}

    private void generateBoard(double initialX, double initialY, double pieceW, double pieceH, ArrayList<Piece> board){

        double width = pieceW;
        double height = pieceH;

        double currentX = initialX;
        double currentY = initialY;

        PieceLetter[] letters = {
                PieceLetter.A
                ,PieceLetter.B
                ,PieceLetter.C
                ,PieceLetter.D
                ,PieceLetter.E
                ,PieceLetter.F
                ,PieceLetter.G
                ,PieceLetter.H
        };

        BoardType initialColor;

        for (int i = 1; i <= 8; i++) {

            if(i % 2 == 0){
                initialColor = BoardType.BLACK;
            } else {
                initialColor = BoardType.WHITE;
            }

            PieceLetter currentLetter = letters[i - 1];

            for (int j = 9; j >= 2; j--) {
                BoardType color = initialColor;
                if(j % 2 == 0){
                    if(initialColor == BoardType.WHITE){
                        color = BoardType.BLACK;
                    } else {
                        color = BoardType.WHITE;
                    }
                }
                board.add(new Piece(currentX,currentY,pieceW,pieceH,color,currentLetter,(j - 1)));
                currentY += pieceH;
            }
            currentY = initialY;
            currentX += pieceW;
        }

    }

}
