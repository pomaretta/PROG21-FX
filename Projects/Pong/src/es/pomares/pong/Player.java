package es.pomares.pong;

/*

    Project     PROG21-FX
    Package     es.pomares.pong    
    
    Version     1.0      
    Author      Carlos Pomares
    Date        2021-02-08

    DESCRIPTION
    
*/

import es.pomares.Main;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * @author Carlos Pomares
 */

public class Player {

    private double deltaY = 10,velocity = 1.5;
    private Rectangle sprite;
    private int points;

    public Player(int WIDTH,int HEIGHT,Color color){
        this.sprite = new Rectangle(WIDTH,HEIGHT,color);
        this.sprite.setStyle("-fx-background-radius: 25px");
    }

    public double getDeltaY() {
        return deltaY;
    }

    public Rectangle getSprite() {
        return sprite;
    }

    public int getPoints() {
        return points;
    }

    public void addPoint(){
        this.points++;
    }

    public void moveUp(){
        this.sprite.setLayoutY(this.sprite.getLayoutY() - this.deltaY * velocity);
    }

    public void moveDown(){
        this.sprite.setLayoutY(this.sprite.getLayoutY() + this.deltaY * velocity);
    }

    // STATIC
    public static boolean detectCollision(Player player,Ball ball){
        if(ball.getSprite().getBoundsInParent().intersects(player.getSprite().getBoundsInParent())){
            Main.impactAudio();
            ball.modifyX();
            //ball.modifyY();
            ball.accelerate();
        }
        return false;
    }

    public static boolean upperLimit(Player player, Parent canvas){
        final Bounds limits = canvas.getBoundsInLocal();
        final boolean upperLimit = player.getSprite().getLayoutY() <= (limits.getMinY());
        return !upperLimit;
    }

    public static boolean lowerLimit(Player player,Parent canvas){
        final Bounds limits = canvas.getBoundsInLocal();
        final boolean lowerLimit = player.getSprite().getLayoutY() >= (limits.getMaxY() - player.getSprite().getHeight());
        return !lowerLimit;
    }

}
