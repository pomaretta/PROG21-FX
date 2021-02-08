package es.pomares.controllers.screen;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Screen {

    private Pane root;
    private Rectangle rect;

    public Screen(Scene scene){

        root = new Pane();
        rect = new Rectangle(100,100, Color.RED);
        rect.relocate(200,200);
        root.getChildren().add(rect);

        scene.setRoot(root);

    }

}
