package Objects.Board;

/*

    Project     PROG21-FX
    Package     Objects.Board    
    
    Version     1.0      
    Author      Carlos Pomares
    Date        2021-03-02

    DESCRIPTION
    
*/

import Static.MouseListener;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * @author Carlos Pomares
 */

public class Piece implements MouseListener {

    private Rectangle sprite;
    private double x,y,w,h;
    private BoardType type;
    private PieceLetter letter;
    private int number;
    private Objects.Entity.Piece piece;

    private boolean selected;

    public Piece(double x, double y, double w, double h, BoardType type, PieceLetter letter, int number) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.type = type;
        this.letter = letter;
        this.number = number;
        this.sprite = new Rectangle(x,y,w,h);
        this.sprite.toFront();
        this.sprite.setFill(this.type.getTypeColor());
        mouseAssigment();
    }

    public Rectangle getSprite() {
        return sprite;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getW() {
        return w;
    }

    public double getH() {
        return h;
    }

    public BoardType getType() {
        return type;
    }

    public PieceLetter getLetter() {
        return letter;
    }

    public int getNumber() {
        return number;
    }

    public Objects.Entity.Piece getPiece() {
        return piece;
    }

    public void setSelected(boolean flag, Color color){
        if(!selected){
            this.selected = flag;
            this.sprite.setFill(color);
        } else {
            this.selected = flag;
            this.sprite.setFill(type.getTypeColor());
        }
    }

    @Override
    public void mouseAssigment() {
        this.sprite.setOnMouseClicked(this::onMouseClicked);
    }

    @Override
    public void onMouseClicked(MouseEvent event) {
        System.out.println("CLICKED: " + getLetter() + " " + getNumber());
        this.setSelected(true,Color.GREEN);
    }

    @Override
    public void onMouseEntered(MouseEvent event) {

    }

    @Override
    public void onMouseExited(MouseEvent event) {

    }

    @Override
    public void onMouseMoved(MouseEvent event) {

    }

    @Override
    public void onMousePressed(MouseEvent event) {

    }

    @Override
    public void onMouseReleased(MouseEvent event) {

    }

    @Override
    public void onMouseDragEntered(MouseEvent event) {

    }

    @Override
    public void onMouseDragExited(MouseEvent event) {

    }

    @Override
    public void onMouseDragged(MouseEvent event) {

    }

    @Override
    public void onMouseDragOver(MouseEvent event) {

    }

    @Override
    public void onMouseDragReleased(MouseEvent event) {

    }

}
