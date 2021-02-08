package Proyectos.Pong;

/*

    Project     PROG21-FX
    Package     Proyectos.Pong    
    
    Version     1.0      
    Author      Carlos Pomares
    Date        2021-02-02

    DESCRIPTION
    
*/

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * @author Carlos Pomares
 */

public class Player {

    public double deltaY;
    public Rectangle player;
    private int points = 0;

    public Player(int width, int height, Color color){

        player = new Rectangle(width,height,color);
        deltaY = 5;

    }

    public void addPoint(){
        this.points++;
    }

    public int getPoints(){
        return this.points;
    }

    // KEYBOARD FUNCTIONALITY
    public void moveUp(){
        player.setLayoutY(player.getLayoutY() - deltaY);
    }

    public void moveDown(){
        player.setLayoutY(player.getLayoutY() + deltaY);
    }

    public static boolean detectCollision(Player player, Ball ball){
        if(ball.ball.getBoundsInParent().intersects(player.player.getBoundsInParent())){
            System.out.println("Impact");
            ball.modifyY();
            ball.modifyX();
            ball.accelerate();
        }
        return false;
    }

}
