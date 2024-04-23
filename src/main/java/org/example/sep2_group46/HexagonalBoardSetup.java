package org.example.sep2_group46;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;

public class HexagonalBoardSetup {
    private final double[][] xyLocation = new double[2][62]; // double array used to store x and y location for atom and circle of influence placement
    private double XLoc = 570; //The initial X location before offset is applied
    private int numHexagons = 5; //The number of hexagons to print for each iteration of the inner loop
    final double OffsetX = 88; //The difference of X location for each row
    private double OffsetY = -100; //The difference of Y location for each row
    private boolean flag = true; //Indicates whether X location decreases or increases
    private boolean methodCalled = false;
    double[][] rayEntry = new double[54][2]; // double array to store the location of the ray entry points
    String[] rayLabels = new String[61]; // String array which stores the numbering values
    double[][][] hexagonsRayEntries = new double[24][3][2]; // Double array for storing ray entries for each hexagon
    private final int[] EdgeLocations = new int[]{1, 2, 3, 4, 5, 6, 11, 12, 18, 19, 26, 27, 35, 36, 43, 44, 50, 51, 56, 57, 58, 59, 60, 61}; // integer array of all edge hexagons

    public HexagonalBoardSetup() {

    }

    public void createHexagonalBoard(Pane Board) {
        if(!methodCalled) {
            // to store the values in the rayLabels array
            for(int l = 0, m = 1; l < 61; l++, m++)
            {
                rayLabels[l] = String.valueOf(m);
            }

            xyLocation[0][0] = -100;
            xyLocation[1][0] = -100;
            int j = 0;
            int k = 0;
            int hexCounter = 0;

            while (j <= 57) {
                for (int i = 0; i < numHexagons; i++) {
                    //The size of the hexagon
                    double hexagonSize = 50.0;
                    Polygon BoardHexagon = createHexagon(hexagonSize);
                    BoardHexagon.setFill(Color.BLACK); //Sets hexagon colour to black
                    BoardHexagon.setStroke(Color.YELLOW); //Sets outline color to yellow

                    // Position and rotate hexagons
                    BoardHexagon.setLayoutX(XLoc + (OffsetX * i));
                    BoardHexagon.setLayoutY(200 + OffsetY);
                    BoardHexagon.setRotate(90);

                    xyLocation[0][i + j + 1] = XLoc + (OffsetX * i);
                    xyLocation[1][i + j + 1] = 200 + OffsetY;

                    // Adding numbering for hexagons
                    Label hexLabel = new Label(rayLabels[hexCounter]);
                    if(hexCounter < 9)
                    {
                        hexLabel.setLayoutX(XLoc + (OffsetX * i) - 7);
                    }
                    else
                    {
                        hexLabel.setLayoutX(XLoc + (OffsetX * i) - 12);
                    }
                    hexLabel.setLayoutY(187 + OffsetY);
                    hexLabel.setFont(new Font("Arial", 24));
                    hexLabel.setTextFill(Color.GREY);
                    hexLabel.setOpacity(0.5);
                    hexCounter++;

                    //setting coordinates and labels of the entries coming from the left of the hexagons
                    if((i + j + 1) == 1 || (i + j + 1) == 6 || (i + j + 1) == 12 || (i + j + 1) == 19 || (i + j + 1) == 27 || (i + j + 1) == 36 || (i + j + 1) == 44 || (i + j + 1) == 51 || (i + j + 1) == 57)
                    {
                        rayEntry[k][0] = XLoc + (OffsetX * i) - Math.sqrt(1875);
                        rayEntry[k][1] = 200 + OffsetY;
                        Label label = new Label(rayLabels[k]);
                        label.setLayoutX(XLoc + (OffsetX * i) - Math.sqrt(1875) - 25);
                        label.setLayoutY(200 + OffsetY - 10);
                        label.setTextFill(Color.WHITE);
                        Board.getChildren().add(label);
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
                        Board.getChildren().add(label);
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
                        Board.getChildren().add(label);
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
                        Board.getChildren().add(label);
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
                        Board.getChildren().add(label);
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
                        Board.getChildren().add(label);
                        k++;
                    }
                    Board.getChildren().add(BoardHexagon);
                    Board.getChildren().add(hexLabel);
                }
                j += numHexagons;
                if (numHexagons == 9) {
                    flag = false; //When number of hexagons printed reaches 9, this turns off the flag in order to start decrementing the number
                }

                if (flag) {
                    numHexagons++;
                    XLoc -= 42;
                } else {
                    numHexagons--;
                    XLoc += 42;
                }
                OffsetY += 75; //Changes y location offset for each row
            }
            methodCalled = true;
        }
        else
        {
            throw new IllegalStateException("This method can only be called once");
        }

        // loop for storing ray entries for each hexagon
        for(int x = 0; x < 24; x++)
        {
            int k = 0;
            for(int y = 0; y < 54; y++)
            {
                double distance = Math.abs(Math.hypot(rayEntry[y][0] - xyLocation[0][EdgeLocations[x]], rayEntry[y][1] - xyLocation[1][EdgeLocations[x]]));
                if(distance < 50 && distance > 40)
                {
                    hexagonsRayEntries[x][k][0] = rayEntry[y][0];
                    hexagonsRayEntries[x][k][1] = rayEntry[y][1];
                    k++;
                }
                if(k == 3)
                {
                    break;
                }
            }
        }
    }
    public int[] randomHexagonCoordinates()
    {
        int min = 1; // setting min for the random numbers
        int max = 62; // setting max for the random numbers
        int[] randNum = new int[6]; // array to store the random numbers
        int tempRandNum; // temporary integer used for comparing if there is any duplicate random numbers
        boolean repeat; // used to check if there is any repeat numbers

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
        }
        return randNum;
    }

    private Polygon createHexagon (double size){
        Polygon hexagon = new Polygon();

        for (int i = 0; i < 6; i++) {
            double angle = 2.0 * Math.PI * i / 6;
            double x = size * Math.cos(angle);
            double y = size * Math.sin(angle);
            hexagon.getPoints().addAll(x, y);
        }

        return hexagon;
    }

    public double[][] getXyLocation() {
        return xyLocation;
    }

    public double[][] getRayEntry() {
        return rayEntry;
    }



}