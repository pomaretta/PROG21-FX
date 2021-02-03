package Ejercicios.Bola;

/*

    Project     PROG21-FX
    Package     Ejercicios.Bola    
    
    Version     1.0      
    Author      Carlos Pomares
    Date        2021-02-02

    DESCRIPTION
    
*/

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @author Carlos Pomares
 */

public class BolaCollisions extends Application {

    class Bola {
        public double deltaX;
        public double deltaY;
        Circle circle;
        public Bola(int radio, Color color){
            this.circle = new Circle(radio,color);
            this.deltaX = 1;
            this.deltaY = 1;
        }
    }

    final int RADIUS = 15;

    public static Pane canvas;
    double width = 400;
    double height = 400;
    Bola bola1;
    Bola bola2;

    @Override
    public void start(Stage primaryStage){

        canvas = new Pane();
        Scene scene = new Scene(canvas,width,height);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Pong");
        primaryStage.show();

        bola1 = new Bola(RADIUS,Color.RED);
        bola2 = new Bola(RADIUS,Color.BLUE);

        bola1.circle.relocate(100, 100);
        bola2.circle.relocate((int)(Math.random()*width)-RADIUS, (int)(Math.random()*height)-RADIUS);

        canvas.getChildren().add(bola1.circle);
        canvas.getChildren().add(bola2.circle);

        final Timeline loop = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {

            @Override
            public void handle(final ActionEvent event) {
                compareCircles(bola1,bola2);
            }

        }));
        loop.setCycleCount(Timeline.INDEFINITE);
        loop.play();

    }

    public void circleMovement(Bola bola) {
        bola.circle.setLayoutX(bola.circle.getLayoutX() + bola.deltaX);
        bola.circle.setLayoutY(bola.circle.getLayoutY() + bola.deltaY);

        final Bounds limits = canvas.getBoundsInLocal();
        final boolean alLimitDret = bola.circle.getLayoutX() >= (limits.getMaxX() - bola.circle.getRadius()-bola.deltaX);
        final boolean alLimitEsquerra = bola.circle.getLayoutX() <= (limits.getMinX() + bola.circle.getRadius()-bola.deltaX);
        final boolean alLimitInferior = bola.circle.getLayoutY() >= (limits.getMaxY() - bola.circle.getRadius()-bola.deltaY);
        final boolean alLimitSuperior = bola.circle.getLayoutY() <= (limits.getMinY() + bola.circle.getRadius()-bola.deltaY);

        if (alLimitDret || alLimitEsquerra) {
            modifyX(bola);
        }
        if (alLimitInferior || alLimitSuperior) {
            modifyY(bola);
        }
    }

    public boolean detectCollision(Bola bola1, Bola bola2){
        if(bola1.circle.getBoundsInParent().intersects(bola2.circle.getBoundsInParent())) {
            System.out.println("Impacte");
            modifyX(bola1);
            modifyY(bola1);
            modifyX(bola2);
            modifyY(bola2);
            return true;
        }
        return false;
    }

    public void modifyX(Bola bola) {
        bola.deltaX = Math.signum(bola.deltaX)*(int)(Math.random()*10+1);
        //bola.deltaX *= -1;
    }
    public void modifyY(Bola bola) {
        bola.deltaY = Math.signum(bola.deltaY)*(int)(Math.random()*10+1);
        //bola.deltaY *= -1;
    }

    public void compareCircles(Bola bola1, Bola bola2) {
        detectCollision(bola1,bola2);
        circleMovement(bola1);
        circleMovement(bola2);
    }


    public static void main(String[] args) {
        launch(args);
    }

}
