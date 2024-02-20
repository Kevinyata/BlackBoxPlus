package org.example.sep2_group46;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;


public class HexagonExample extends Application {

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        primaryStage.setTitle("Hexagonal Black Box+");

        int numHexagons = 5; //The number of hexagons to print for each iteration of the inner loop
        double hexagonSize = 50.0; //The size of the hexagon
        double OffsetX = 88; //The difference of X location for each row
        double OffsetY = 0; //The difference of Y location for each row
        double XLoc = 570; //The initial X location before offset is applied
        boolean flag = true; //Indicates whether X location decreases or increases

        // Create a tessellation of rotated hexagons
        int j = 0;
        while(j <= 57)
        {

            for (int i = 0; i < numHexagons; i++) {
                Polygon hexagon = createHexagon(hexagonSize);
                hexagon.setFill(Color.BLACK); //Sets hexagon colour to black
                hexagon.setStroke(Color.YELLOW); //Sets outline color to yellow

                // Position and rotate hexagons
                hexagon.setLayoutX(XLoc + (OffsetX * i));
                hexagon.setLayoutY(80 + OffsetY);

                // Rotate hexagons by 90 degrees
                hexagon.setRotate(90);

                root.getChildren().add(hexagon);
            }

            if(numHexagons == 9)            {
                flag = false; //When number of hexagons printed reaches 9, this turns off the flag im order to start decrementing the number
            }

            if(flag)
            {
                numHexagons++;
                XLoc -= 42;
            }
            else {
                numHexagons--;
                XLoc += 42;
            }
            OffsetY += 75; //Changes y location offset for each row

            j+=numHexagons; 
        }

        Button b = new Button();
        b.setText("Show");
        b.setLayoutX(1300);
        b.setLayoutY(680);
        b.setPrefSize(150, 50);
        b.setOnAction(actionEvent -> {
            if(b.getText().equals("Show")) b.setText("Hide");
            else if(b.getText().equals("Hide")) b.setText("Show");});
        root.getChildren().add(b);



        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Polygon createHexagon(double size) {
        Polygon hexagon = new Polygon();

        for (int i = 0; i < 6; i++) {
            double angle = 2.0 * Math.PI * i / 6;
            double x = size * Math.cos(angle);
            double y = size * Math.sin(angle);
            hexagon.getPoints().addAll(x, y);
        }

        return hexagon;
    }



    public static void main(String[] args) {
        launch(args);
    }
}

