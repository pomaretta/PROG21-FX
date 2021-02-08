package es.pomares.pong;

/*

    Project     PROG21-FX
    Package     es.pomares.pong    
    
    Version     1.0      
    Author      Carlos Pomares
    Date        2021-02-08

    DESCRIPTION
    
*/

import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * @author Carlos Pomares
 */

public class Ball {

    private final double MAX_VEL = 5;
    private double deltaX, deltaY, velocity;
    private Circle sprite;

    private boolean leftLimit = false, rightLimit = false;

    public Ball(int radius, Color color){
        this.sprite = new Circle(radius,color);
        generateAngle();
    }

    public void modifyY(){
        this.deltaY *= -1;
    }

    public void modifyX(){
        this.deltaX *= -1;
    }

    public void accelerate(){
        if(this.velocity <= this.MAX_VEL)
            this.velocity++;
    }

    public void movement(Parent canvas){

        this.sprite.setLayoutX(this.sprite.getLayoutX() + deltaX * velocity);
        this.sprite.setLayoutY(this.sprite.getLayoutY() + deltaY * velocity);

        final Bounds limits = canvas.getBoundsInLocal();
        final boolean leftLimit = this.sprite.getLayoutX() <= (limits.getMinX() + this.sprite.getRadius());
        final boolean rightLimit = this.sprite.getLayoutX() >= (limits.getMaxX() - this.sprite.getRadius());
        final boolean upperLimit = this.sprite.getLayoutY() <= (limits.getMinY() + this.sprite.getRadius());
        final boolean lowerLimit = this.sprite.getLayoutY() >= (limits.getMaxY() - this.sprite.getRadius());

        if(leftLimit)
            this.leftLimit = true;

        if(rightLimit)
            this.rightLimit = true;

        if(upperLimit || lowerLimit)
            modifyY();

    }

    public void generateAngle(){
        double angle = Math.toRadians(45);
        this.velocity = 1;
        this.deltaX = this.velocity * Math.cos(angle);
        this.deltaY = this.velocity * Math.sin(angle);
    }

    public void resetProperties(){
        this.leftLimit = false;
        this.rightLimit = false;
    }

    public void relocateInMiddle(Parent canvas){
        Bounds limits = canvas.getBoundsInLocal();
        this.sprite.relocate(
                ((limits.getMaxX() - limits.getMaxX() / 2) - this.sprite.getRadius()),
                ((limits.getMaxY() - (limits.getMaxY() / 2)) - this.sprite.getRadius())
        );
    }

    public double getDeltaX() {
        return deltaX;
    }

    public double getDeltaY() {
        return deltaY;
    }

    public double getVelocity() {
        return velocity;
    }

    public Circle getSprite() {
        return sprite;
    }

    public boolean isLeftLimit() {
        return leftLimit;
    }

    public boolean isRightLimit() {
        return rightLimit;
    }
}
