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

public class Ball {

    public double deltaX;
    public double deltaY;
    public double velocity;
    public Circle ball;

    private boolean leftPoint = false;
    private boolean rightPoint = false;

    public Ball(int radius, Color color){

        ball = new Circle(radius,color);

        double angle = Math.toRadians(Math.random() * 120 + 90);
        velocity = 2;
        deltaX = velocity * Math.cos(angle);
        deltaY = velocity * Math.sin(angle);

    }

    public void relocateInMiddle(Pane canvas){
        Bounds limits = canvas.getBoundsInLocal();
        ball.relocate((double)((limits.getMaxX() - (limits.getMaxX() / 2)) - ball.getRadius()),(double)((limits.getMaxY() - (limits.getMaxY() / 2)) - ball.getRadius()));
    }

    public void modifyX(Ball ball){
        ball.deltaX *= -1;
    }

    public void modifyY(Ball ball){
        ball.deltaY *= -1;
    }

    public void accelerate(){
        System.out.println("ACCELERATE");
        velocity += 0.25;
    }

    public void movement(Pane canvas){

        ball.setLayoutX(ball.getLayoutX() + deltaX);
        ball.setLayoutY(ball.getLayoutY() + deltaY);

        final Bounds limits = canvas.getBoundsInLocal();
        final boolean leftLimit = ball.getLayoutX() <= (limits.getMinX() + ball.getRadius());
        final boolean rightLimit = ball.getLayoutX() >= (limits.getMaxX() - ball.getRadius());
        final boolean topLimit = ball.getLayoutY() <= (limits.getMinY() + ball.getRadius());
        final boolean lowerLimit = ball.getLayoutY() >= (limits.getMaxY() - ball.getRadius());

        if(leftLimit || rightLimit){
            System.out.println("LEFT POINT");
            //modifyX(this);
            leftPoint = true;
        }

        if(rightLimit){
            System.out.println("RIGHT POINT");
            //modifyX(this);
            rightPoint = true;
        }

        if(topLimit || lowerLimit){
            modifyY(this);
        }

    }

    public boolean isLeftPoint(){
        return leftPoint;
    }

    public boolean isRightPoint(){
        return rightPoint;
    }

}
