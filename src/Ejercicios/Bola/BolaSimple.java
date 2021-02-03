package Ejercicios.Bola;

/*

    Project     PROG21-FX
    Package     Ejercicios.Bola    
    
    Version     1.0      
    Author      Carlos Pomares
    Date        2021-01-27

    DESCRIPTION
    
*/

/**
 * @author Carlos Pomares
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

public class BolaSimple extends Application {

    public static Circle circle;
    public static Pane canvas;

    @Override
    public void start(Stage primaryStage){

        canvas = new Pane();
        Scene escena = new Scene(canvas,400,400);

        primaryStage.setTitle("Bola 0");
        primaryStage.setScene(escena);
        primaryStage.show();

        int radio = 15;
        circle = new Circle(radio, Color.RED);
        circle.relocate(200-radio,0+radio);

        canvas.getChildren().addAll(circle);

        final Timeline loop = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {


            // Formula en radians
            //double deltaX = 3*Math.cos(Math.PI/3);
            //double deltaY = 3*Math.sin(Math.PI/3);

            // Formula en graus


            // Simulació gravitatòria
            double temps=1;
            double masa = 50000;
            double acc = 9.8;
            final Bounds limits = canvas.getBoundsInLocal();

            double angle_en_radians =Math.toRadians(90);
            double velocitat = 5;
            double gravedad = 1;
            double velocidadGravedad = 0;
            double deltaX = velocitat*Math.cos(angle_en_radians);
            double deltaY = velocitat*Math.sin(angle_en_radians);

            @Override
            public void handle(final ActionEvent t) {

                velocidadGravedad += gravedad;
                deltaY += velocidadGravedad / Math.pow(temps,2);
                temps += 1;
                System.out.println(circle.getLayoutY());

                //cercle.setLayoutX(cercle.getLayoutX() + deltaX/2);
                circle.setLayoutX(circle.getLayoutX() + deltaX);
                //cercle.setLayoutY(cercle.getLayoutY() + deltaY/3);
                circle.setLayoutY(circle.getLayoutY() + deltaY);
                //System.out.println(cercle.getLayoutX()+":"+cercle.getLayoutY());

                final boolean alLimitDret = circle.getLayoutX() >= (limits.getMaxX() - circle.getRadius());
                final boolean alLimitEsquerra = circle.getLayoutX() <= (limits.getMinX() + circle.getRadius());
                final boolean alLimitInferior = circle.getLayoutY() >= (limits.getMaxY() - circle.getRadius());
                final boolean alLimitSuperior = circle.getLayoutY() <= (limits.getMinY() + circle.getRadius());


                if (alLimitDret || alLimitEsquerra) {
                    // Delta aleatori
                    // Multiplicam pel signe de deltaX per mantenir la trajectoria
                    //deltaX = Math.signum(deltaX)*(Math.random()*10+1);
                    deltaX *= -1;
                }
                if (alLimitInferior) {
                    // Delta aleatori
                    // Multiplicam pel signe de deltaX per mantenir la trajectori'
                    deltaY *= -1;
                }



                System.out.println("VELOCIDAD: " + (velocitat + acc) / temps);
            }
        }));

        loop.setCycleCount(Timeline.INDEFINITE);
        loop.play();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
