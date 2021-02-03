package Ejercicios.Bola;

/*

    Project     PROG21-FX
    Package     Ejercicios.Bola    
    
    Version     1.0      
    Author      Carlos Pomares
    Date        2021-01-29

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

public class BolaGravedad extends Application {

    public static Circle circle;
    public static Pane canvas;

    @Override
    public void start(Stage primaryStage){

        canvas = new Pane();
        Scene escena = new Scene(canvas,400,400);

        primaryStage.setTitle("Bola Gravedad");
        primaryStage.setScene(escena);
        primaryStage.show();

        int radio = 15;
        circle = new Circle(radio, Color.RED);
        circle.relocate(200-radio,200-radio);

        canvas.getChildren().addAll(circle);

        final Timeline loop = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {

            private int temps;
            double angle_en_radians =Math.toRadians(30);
            int velocitat=2;
            double deltaX = velocitat*Math.cos(angle_en_radians);
            double deltaY = velocitat*Math.sin(angle_en_radians);

            // Simulació gravitatòria

            final Bounds limits = canvas.getBoundsInLocal();

            @Override
            public void handle(final ActionEvent t) {
                //cercle.setLayoutX(cercle.getLayoutX() + deltaX/2);

                this.temps += 1;

                circle.setLayoutX(circle.getLayoutX() + deltaX);
                //cercle.setLayoutY(cercle.getLayoutY() + deltaY/3);
                circle.setLayoutY(circle.getLayoutY() + deltaY);
                //System.out.println(cercle.getLayoutX()+":"+cercle.getLayoutY());

                final boolean alLimitDret = circle.getLayoutX() >= (limits.getMaxX() - circle.getRadius());
                final boolean alLimitEsquerra = circle.getLayoutX() <= (limits.getMinX() + circle.getRadius());
                final boolean alLimitInferior = circle.getLayoutY() >= (limits.getMaxY() - circle.getRadius());
                final boolean alLimitSuperior = circle.getLayoutY() <= (limits.getMinY() + circle.getRadius());


                if (alLimitDret || alLimitEsquerra) {
                    //deltaX = Math.signum(deltaX)*(Math.random()*10+1);
                    deltaX *= -1;
                }
                if (alLimitInferior || alLimitSuperior) {
                    //deltaY = Math.signum(deltaY)*(Math.random()*10+1);
                    deltaY *= -1;
                }

            }
        }));

        loop.setCycleCount(Timeline.INDEFINITE);
        loop.play();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
