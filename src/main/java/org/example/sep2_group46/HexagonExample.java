package org.example.sep2_group46;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import java.lang.Math;


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
        double[][] xyLocation = new double[2][62]; // double array used to store x and y location for atom and circle of influence placement

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
                hexagon.setLayoutY(200 + OffsetY);

                // Rotate hexagons by 90 degrees
                hexagon.setRotate(90);

                // saving each hexagon's x and y coordinates
                xyLocation[0][i + j + 1] = XLoc + (OffsetX * i);
                xyLocation[1][i + j + 1] = 200 + OffsetY;

                root.getChildren().add(hexagon);
            }

            j+=numHexagons;

            if(numHexagons == 9)            {
                flag = false; //When number of hexagons printed reaches 9, this turns off the flag in order to start decrementing the number
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
        }

        int min = 1; // setting min for the random numbers
        int max = 62; // setting max for the random numbers
        int[] randNum = new int[5]; // array to store the random numbers
        int tempRandNum; // temporary integer used for comparing if there is any duplicate random numbers
        boolean repeat; // used to check if there is any repeat numbers#Sphere[] atoms = new Sphere[5];
        Sphere[] atoms = new Sphere[5];
        Circle[] COIs = new Circle[5];

        for(int i = 0; i < 5; i++)  // loop to get the 5 atoms
        {
            // loop to get the random number and check if there is any repeat numbers and to get another random number if there is a repeat
            do {
                repeat = false;
                tempRandNum = (int) (Math.random() * (max - min + 1) + min);
                tempRandNum--;
                for (int z = 0; z < 5; z++) {
                    if (randNum[z] == tempRandNum) {
                        repeat = true;
                        break;
                    }
                }
            } while (repeat);
            randNum[i] = tempRandNum; // storing the random number in the array
        } // loop to print out the 5 circles of influence
        for(int z = 0; z < 5; z++)
        {
            Circle COI = createCircleOfInfluence();
            COI.setLayoutX(xyLocation[0][randNum[z]]); // getting the x location of the random numbers chosen
            COI.setLayoutY(xyLocation[1][randNum[z]]); // getting the y location of the random numbers chosen
            COIs[z] = COI;
            root.getChildren().add(COI);
        }
        for(int z = 0; z < 5; z++) // loop to print out the 5 atoms
        {
            Sphere atom = createAtom();
            PhongMaterial material = new PhongMaterial(); // used for setting the atoms colour
            material.setDiffuseColor(Color.GREY);
            material.setSpecularColor(Color.BLACK);
            atom.setMaterial(material); // setting the colours to the atoms
            atom.setLayoutX(xyLocation[0][randNum[z]]); // getting the x location of the random numbers chosen
            atom.setLayoutY(xyLocation[1][randNum[z]]); // getting the y location of the random numbers chosen
            atoms[z] = atom;
            root.getChildren().add(atom);
        }

        Button b = new Button();
        b.setText("Show");
        b.setLayoutX(1300);
        b.setLayoutY(680);
        b.setPrefSize(150, 50);
        b.setOnAction(actionEvent -> {
            if(b.getText().equals("Show"))
            {
                ShowAtoms(atoms, COIs);
                b.setText("Hide");
            }
            else if(b.getText().equals("Hide")) {
                HideAtoms(atoms, COIs);
                b.setText("Show");
            }
        });
        root.getChildren().add(b);



        HideAtoms(atoms, COIs);



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

    // method to create the atom
    private Sphere createAtom()
    {
        Sphere atom = new Sphere();
        atom.setRadius(30.0f); // setting the atom's size
        return atom;
    }

    // method to create the circle of influence
    private Circle createCircleOfInfluence()
    {
        Circle COI = new Circle();
        COI.setRadius(85.0f); // setting the circle of influence's size
        COI.setFill(null); // setting the inside of the circle of influence to empty, so it is just an outline
        COI.setStroke(Color.WHITE); // setting the outline of the circle of influence to white
        return COI;
    }

    private void ShowAtoms(Sphere[] s, Circle[] c)
    {
        for(int i = 0; i < 5; i++)
        {
            c[i].setVisible(true);
            s[i].setVisible(true);
        }
    }

    private void HideAtoms(Sphere[] s, Circle[] c)
    {
        for(int i = 0; i < 5; i++)
        {
            c[i].setVisible(false);
            s[i].setVisible(false);
        }
    }



    public static void main(String[] args) {
        launch(args);
    }
}

