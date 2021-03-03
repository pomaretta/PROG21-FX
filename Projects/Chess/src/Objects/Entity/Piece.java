package Objects.Entity;

/*

    Project     PROG21-FX
    Package     Objects    
    
    Version     1.0      
    Author      Carlos Pomares
    Date        2021-03-02

    DESCRIPTION
    
*/

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author Carlos Pomares
 */

public class Piece {

    private Image image;
    private ImageView sprite;
    private double x,y,h,w;
    private PieceType type;
    private PieceImage imageUrl;

    public Piece(double x, double y, double h, double w, PieceType type, PieceImage image) {
        this.x = x;
        this.y = y;
        this.h = h;
        this.w = w;
        this.type = type;
        setImageUrl(image);
    }

    private void setImageUrl(PieceImage imageUrl){
        image = new Image(getClass().getResourceAsStream(imageUrl.getResource()),w,h,true,true);
        sprite.setImage(image);
    }

    public Image getImage() {
        return image;
    }

    public ImageView getSprite() {
        return sprite;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getH() {
        return h;
    }

    public double getW() {
        return w;
    }

    public PieceType getType() {
        return type;
    }

    public PieceImage getImageUrl() {
        return imageUrl;
    }

    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }

}
