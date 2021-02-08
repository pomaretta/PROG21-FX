package Proyectos.Pong;

/*

    Project     PROG21-FX
    Package     Proyectos.Pong    
    
    Version     1.0      
    Author      Carlos Pomares
    Date        2021-02-02

    DESCRIPTION
    
*/

import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * @author Carlos Pomares
 */

public class BallORI {

    public double deltaX;
    public double deltaY;
    public double velocity;
    public Circle ball;

    private boolean leftLimit = false;
    private boolean rightLimit = false;

    public BallORI(int radius, Color color){

        ball = new Circle(radius,color);

        resetVelocity();

    }

    public void relocateInMiddle(Pane canvas){
        Bounds limits = canvas.getBoundsInLocal();
        ball.relocate((double)((limits.getMaxX() - (limits.getMaxX() / 2)) - ball.getRadius()),(double)((limits.getMaxY() - (limits.getMaxY() / 2)) - ball.getRadius()));
    }

    public void modifyX(){
        this.deltaX *= -1;
    }

    public void modifyY(){
        this.deltaY *= -1;
    }

    public void accelerate(){
        System.out.println("ACCELERATE");
        velocity++;
    }

    public void movement(Pane canvas){

        this.ball.setLayoutX(this.ball.getLayoutX() + deltaX);
        this.ball.setLayoutY(this.ball.getLayoutY() + deltaY);

        final Bounds limits = canvas.getBoundsInLocal();
        final boolean leftLimit = this.ball.getLayoutX() <= (limits.getMinX() + this.ball.getRadius());
        final boolean rightLimit = this.ball.getLayoutX() >= (limits.getMaxX() - this.ball.getRadius());
        final boolean topLimit = this.ball.getLayoutY() <= (limits.getMinY() + this.ball.getRadius());
        final boolean lowerLimit = this.ball.getLayoutY() >= (limits.getMaxY() - this.ball.getRadius());

        if(leftLimit){
            System.out.println("LEFT POINT");
            //modifyX(this);
            this.leftLimit = true;
        }

        if(rightLimit){
            System.out.println("RIGHT POINT");
            //modifyX(this);
            this.rightLimit = true;
        }

        if(topLimit || lowerLimit){
            modifyY();
        }

    }

    public boolean isLeftLimit(){
        return leftLimit;
    }

    public boolean isRightLimit(){
        return rightLimit;
    }

    public void resetProperties(){
        this.leftLimit = false;
        this.rightLimit = false;
    }

    public void resetVelocity(){
        double angle = Math.toRadians(Math.random() * 120 + 90);
        velocity = 1;
        deltaX = velocity * Math.cos(angle);
        deltaY = velocity * Math.sin(angle);
    }

}
