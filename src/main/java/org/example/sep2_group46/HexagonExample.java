package org.example.sep2_group46;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    @SuppressWarnings("DuplicateExpressions")
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
        double[][] rayEntry = new double[54][2]; // double array to store the location of the ray entry points
        String[] rayLabels = new String[54]; // String array which stores the numbering values

        // to store the values in the rayLabels array
        for(int i = 0, j = 1; i < 54; i++, j++)
        {
            rayLabels[i] = String.valueOf(j);
        }

        // Create a tessellation of rotated hexagons
        int j = 0;
        int k = 0;
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
                //setting coordinates and labels of the entries coming from the left of the hexagons
                if((i + j + 1) == 1 || (i + j + 1) == 6 || (i + j + 1) == 12 || (i + j + 1) == 19 || (i + j + 1) == 27 || (i + j + 1) == 36 || (i + j + 1) == 44 || (i + j + 1) == 51 || (i + j + 1) == 57)
                {
                    rayEntry[k][0] = XLoc + (OffsetX * i) - Math.sqrt(1875);
                    rayEntry[k][1] = 200 + OffsetY;
                    Label label = new Label(rayLabels[k]);
                    label.setLayoutX(XLoc + (OffsetX * i) - Math.sqrt(1875) - 25);
                    label.setLayoutY(200 + OffsetY - 10);
                    label.setTextFill(Color.WHITE);
                    root.getChildren().add(label);
                    k++;
                }
                //setting coordinates and labels of the entries coming from the top left of the hexagons
                if((i + j + 1) < 7 || (i + j + 1) == 12 || (i + j + 1) == 19 || (i + j + 1) == 27)
                {
                    rayEntry[k][0] = XLoc + (OffsetX * i) - (Math.sqrt(1875) * Math.sin(0.523599));
                    rayEntry[k][1] = 200 + OffsetY - 37.5;
                    Label label = new Label(rayLabels[k]);
                    label.setLayoutX(XLoc + (OffsetX * i) - (Math.sqrt(1875) * Math.sin(0.523599)) - 15);
                    label.setLayoutY(200 + OffsetY - 37.5 - 22.5);
                    label.setTextFill(Color.WHITE);
                    root.getChildren().add(label);
                    k++;
                }
                //setting coordinates and labels of the entries coming from the top right of the hexagons
                if((i + j + 1) < 6 || (i + j + 1) == 11 || (i + j + 1) == 18 || (i + j + 1) == 26 || (i + j + 1) == 35)
                {
                    rayEntry[k][0] = XLoc + (OffsetX * i) + (Math.sqrt(1875) * Math.sin(0.523599));
                    rayEntry[k][1] = 200 + OffsetY - 37.5;
                    Label label = new Label(rayLabels[k]);
                    label.setLayoutX(XLoc + (OffsetX * i) + (Math.sqrt(1875) * Math.sin(0.523599)) + 10);
                    label.setLayoutY(200 + OffsetY - 37.5 - 22.5);
                    label.setTextFill(Color.WHITE);
                    root.getChildren().add(label);
                    k++;
                }
                //setting coordinates and labels of the entries coming from the right of the hexagons
                if((i + j + 1) == 5 || (i + j + 1) == 11 || (i + j + 1) == 18 || (i + j + 1) == 26 || (i + j + 1) == 35 || (i + j + 1) == 43 || (i + j + 1) == 50 || (i + j + 1) == 56 || (i + j + 1) == 61)
                {
                    rayEntry[k][0] = XLoc + (OffsetX * i) + Math.sqrt(1875);
                    rayEntry[k][1] = 200 + OffsetY;
                    Label label = new Label(rayLabels[k]);
                    label.setLayoutX(XLoc + (OffsetX * i) + Math.sqrt(1875) + 15);
                    label.setLayoutY(200 + OffsetY - 10);
                    label.setTextFill(Color.WHITE);
                    root.getChildren().add(label);
                    k++;
                }
                //setting coordinates and labels of the entries coming from the bottom right of the hexagons
                if((i + j + 1) == 35 || (i + j + 1) == 43 || (i + j + 1) == 50 || (i + j + 1) > 55)
                {
                    rayEntry[k][0] = XLoc + (OffsetX * i) + (Math.sqrt(1875) * Math.sin(0.523599));
                    rayEntry[k][1] = 200 + OffsetY + 37.5;
                    Label label = new Label(rayLabels[k]);
                    label.setLayoutX(XLoc + (OffsetX * i) + (Math.sqrt(1875) * Math.sin(0.523599)) + 5);
                    label.setLayoutY(200 + OffsetY + 37.5 + 5);
                    label.setTextFill(Color.WHITE);
                    root.getChildren().add(label);
                    k++;
                }
                //setting coordinates and labels of the entries coming from the bottom left of the hexagons
                if((i + j + 1) == 27 || (i + j + 1) == 36 || (i + j + 1) == 44 || (i + j + 1) == 51 || (i + j + 1) > 56)
                {
                    rayEntry[k][0] = XLoc + (OffsetX * i) - (Math.sqrt(1875) * Math.sin(0.523599));
                    rayEntry[k][1] = 200 + OffsetY + 37.5;
                    Label label = new Label(rayLabels[k]);
                    label.setLayoutX(XLoc + (OffsetX * i) - (Math.sqrt(1875) * Math.sin(0.523599)) - 15);
                    label.setLayoutY(200 + OffsetY + 37.5 + 5);
                    label.setTextFill(Color.WHITE);
                    root.getChildren().add(label);
                    k++;
                }
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

        for(int i = 0; i < 54; i++)
        {
            Circle entry = createEntry();
            entry.setFill(Color.WHITE);
            entry.setLayoutX(rayEntry[i][0]);
            entry.setLayoutY(rayEntry[i][1]);
            root.getChildren().add(entry);
        }

        int min = 1; // setting min for the random numbers
        int max = 62; // setting max for the random numbers
        int[] randNum = new int[6]; // array to store the random numbers
        int tempRandNum; // temporary integer used for comparing if there is any duplicate random numbers
        boolean repeat; // used to check if there is any repeat numbers#Sphere[] atoms = new Sphere[5];
        Sphere[] atoms = new Sphere[6]; //used for visibility conditions for atoms
        Circle[] COIs = new Circle[6]; //used for visibility conditions for circles of influence

        for(int i = 0; i < 6; i++)  // loop to get the 6 atoms
        {
            // loop to get the random number and check if there is any repeat numbers and to get another random number if there is a repeat
            do {
                repeat = false;
                tempRandNum = (int) (Math.random() * (max - min + 1) + min);
                tempRandNum--;
                for (int z = 0; z < 6; z++) {
                    if (randNum[z] == tempRandNum) {
                        repeat = true;
                        break;
                    }
                }
            } while (repeat);
            randNum[i] = tempRandNum; // storing the random number in the array
        } // loop to print out the 6 circles of influence
        for(int z = 0; z < 6; z++)
        {
            Circle COI = createCircleOfInfluence();
            COI.setLayoutX(xyLocation[0][randNum[z]]); // getting the x location of the random numbers chosen
            COI.setLayoutY(xyLocation[1][randNum[z]]); // getting the y location of the random numbers chosen
            COIs[z] = COI;
            root.getChildren().add(COI);
        }
        for(int z = 0; z < 6; z++) // loop to print out the 6 atoms
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
                //Shows atoms
                ShowAtoms(atoms, COIs);
                b.setText("Hide"); //Switches functionality to hide
            }
            else if(b.getText().equals("Hide")) {
                //Hides atoms
                HideAtoms(atoms, COIs);
                b.setText("Show"); //Switches functionality to show
            }
        });
        root.getChildren().add(b);



        //Hides atoms initially
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

    private Circle createEntry()
    {
        Circle entry = new Circle();
        entry.setRadius(2.5f);
        return entry;
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
        //Sets atoms visible
        for(int i = 0; i < 6; i++)
        {
            c[i].setVisible(true);
            s[i].setVisible(true);
        }
    }

    private void HideAtoms(Sphere[] s, Circle[] c)
    {
        //Sets atoms invisible
        for(int i = 0; i < 6; i++)
        {
            c[i].setVisible(false);
            s[i].setVisible(false);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
