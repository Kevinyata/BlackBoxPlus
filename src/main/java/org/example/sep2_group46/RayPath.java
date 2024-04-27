package org.example.sep2_group46;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Sphere;
import java.util.ArrayList;

public class RayPath {
    private final double[][] rayEntry; // double array to store the location of the ray entry points;
    private int rayMarkerCount = 0; //Counts the amount of rays sent
    final int[] rightLoc = new int[]{12, 16, 20, 24, 29, 33, 37, 41, 52};
    final int[] leftLoc = new int[]{1, 13, 17, 21, 25, 31, 35, 39, 43};
    final int[] bottomRightLoc = new int[]{44, 46, 48, 50, 53, 42, 38, 34, 30};
    final int[] topLeftLoc = new int[]{26, 22, 18, 14, 2, 4, 6, 8, 10};
    final int[] bottomLeftLoc = new int[]{27, 32, 36, 40, 45, 47, 49, 51, 54};
    final int[] topRightLoc = new int[]{3, 5, 7, 9, 11, 15, 19, 23, 28};
    int colorIndex = 0;

    Color[] colorsForRayMarkers = {
            Color.TURQUOISE,
            Color.GREEN,
            Color.BLUE,
            Color.NAVY,
            Color.ORANGE,
            Color.PURPLE,
            Color.CYAN,
            Color.MAGENTA,
            Color.PINK,
            Color.LIGHTBLUE,
            Color.LIGHTGREEN,
            Color.LIGHTPINK,
            Color.LIGHTYELLOW,
            Color.LIGHTGRAY,
            Color.DARKRED,
            Color.DARKGREEN,
            Color.DARKBLUE,
            Color.DARKORANGE,
            Color.DARKVIOLET,
            Color.DARKCYAN,
            Color.DARKMAGENTA,
            Color.DARKSALMON,
            Color.BROWN,
            Color.GRAY,
            Color.BLACK,
            Color.LIMEGREEN,
            Color.LIGHTCORAL
    };

    public RayPath(double[][] rayEntry)
    {
        this.rayEntry =  rayEntry;
    }

    public void createRayPath(Pane Board, int EntryNum, Circle[] entries , AtomCreator atomCreator)
    {
        // loop for storing ray entries for each hexagon

        //Obtains X and Y location of entry
        double x = rayEntry[EntryNum][0];
        double y = rayEntry[EntryNum][1];

        //Determines the direction of the ray travels in
        double[] Velocity = CalculateVelocity(EntryNum);

        double addToXCoordinates = Velocity[0]/2;
        double addToYCoordinates = Velocity[1]/2;
        Sphere[] Molecule = atomCreator.getMolecule();


        int[][] COIHexagonIndex = atomCreator.getCOIHexagonIndex();
        double[][] xyCoordinates = atomCreator.getXyLocation();

        ArrayList<Line> Rays = new ArrayList<>();
        Rays.add(new Line());
        int index = 0;

        int count = 0;

        //Initialises base ray
        Rays.get(index).setStartX(x);
        Rays.get(index).setStartY(y);
        Rays.get(index).setStrokeWidth(2);
        Rays.get(index).setStroke(Color.WHITE);


        //Checks if ray will be reflected and if so marks appropriate ray entry
        for(int k = 0; k < 6; k++) {
            double x3 = (entries[EntryNum].getLayoutX() - Molecule[k].getTranslateX());
            double y3 = (entries[EntryNum].getLayoutY() - Molecule[k].getTranslateY());
            double distance = Math.hypot(x3, y3);
            boolean isReflected = distance >= 70 && distance <= 80;

            if(isReflected) {
                rayMarkerCount++;
                entries[EntryNum].setFill(Color.WHITE);
                entries[EntryNum].setMouseTransparent(true);
                return;
            }
        }

        double AbsorptionTolerance = 15;
        double CircleOfInfluenceTolerance = 15;
        boolean isDeflectedFlag = false;

        while(true) {
            
            x += addToXCoordinates;
            y += addToYCoordinates;
            Rays.get(index).setEndX(x);
            Rays.get(index).setEndY(y);

            //Checks for the first half hexagon
            if(count == 0) {
                //Checks if ray is absorbed
                for (int atomNumber = 0; atomNumber < 6; atomNumber++) {
                    //See if coordinates of endpoint of a line approximately matches the coordinates of the centre of the atom
                    boolean areXCoordinatesEqual;
                    boolean areYCoordinatesEqual;

                    areXCoordinatesEqual = Math.abs(Molecule[atomNumber].getTranslateX() - Rays.get(index).getEndX()) <= AbsorptionTolerance;
                    areYCoordinatesEqual = Math.abs(Rays.get(index).getEndY() - Molecule[atomNumber].getTranslateY()) <= AbsorptionTolerance;
                    count++;

                    boolean isAbsorbed = (areXCoordinatesEqual && areYCoordinatesEqual);
                    if (isAbsorbed) {
                        //Marks the entry as absorbed
                        entries[EntryNum].setFill(Color.RED);

                        //Adjusts ray endpoint at middle of atom appropriately
                        double differenceX = Molecule[atomNumber].getTranslateX() - Rays.get(index).getEndX();
                        double differenceY = Molecule[atomNumber].getTranslateY() - Rays.get(index).getEndY();
                        Rays.get(index).setEndX(x + differenceX);
                        Rays.get(index).setEndY(y + differenceY);

                        Rays.get(index).setVisible(false);
                        //Adds ray to board
                        Board.getChildren().add(Rays.get(index));
                        entries[EntryNum].setMouseTransparent(true);
                        rayMarkerCount++;
                        return;
                    }
                }
            }

            //Checks if ray is deflected
            for (int atomNumber = 0; atomNumber < 6; atomNumber++) {
                for (int adjacentHexagon = 0; adjacentHexagon < 6; adjacentHexagon++) {
                    boolean isAdjacentHexagonLocation = COIHexagonIndex[atomNumber][adjacentHexagon] != 0;
                    boolean areXCoordinatesEqual = Math.abs(xyCoordinates[0][COIHexagonIndex[atomNumber][adjacentHexagon]] - Rays.get(index).getEndX()) <= CircleOfInfluenceTolerance;
                    boolean areYCoordinatesEqual = Math.abs(xyCoordinates[1][COIHexagonIndex[atomNumber][adjacentHexagon]] - Rays.get(index).getEndY()) <= CircleOfInfluenceTolerance;
                    boolean isDeflected = isAdjacentHexagonLocation && areXCoordinatesEqual && areYCoordinatesEqual;
                    isDeflectedFlag = isDeflected;
                    if (isDeflected) {

                        //Checks if the ray hits 1,2 or 3 circles of influence
                        int[] countAndSecondOccurrence = countOccurrences(COIHexagonIndex, COIHexagonIndex[atomNumber][adjacentHexagon]);

                            //Adjusts ray to hexagon midpoint
                            double differenceX = xyCoordinates[0][COIHexagonIndex[atomNumber][adjacentHexagon]] - Rays.get(index).getEndX();
                            double differenceY = xyCoordinates[1][COIHexagonIndex[atomNumber][adjacentHexagon]] - Rays.get(index).getEndY();
                            Rays.get(index).setEndX(x + differenceX);
                            Rays.get(index).setEndY(y + differenceY);

                            if(countAndSecondOccurrence[0] == 1) //If a circle of influence hit is detected
                            {
                                //Checks if ray is absorbed
                                for (int atomNumbers = 0; atomNumbers < 6; atomNumbers++) {
                                    //See if coordinates of endpoint of a line approximately matches the coordinates of the centre of the atom
                                    boolean areXCoordinatesEqual2;
                                    boolean areYCoordinatesEqual2;

                                    areXCoordinatesEqual2 = Math.abs(Molecule[atomNumbers].getTranslateX() - (Rays.get(index).getEndX() + addToXCoordinates*2)) <= AbsorptionTolerance;
                                    areYCoordinatesEqual2 = Math.abs((Rays.get(index).getEndY() + addToYCoordinates*2) - Molecule[atomNumbers].getTranslateY()) <= AbsorptionTolerance;

                                    boolean isAbsorbed = (areXCoordinatesEqual2 && areYCoordinatesEqual2);
                                    if (isAbsorbed) {
                                        //Marks the entry as absorbed
                                        entries[EntryNum].setFill(Color.RED);

                                        //Adjusts ray endpoint at middle of atom appropriately
                                        double differenceX2 = Molecule[atomNumber].getTranslateX() - Rays.get(index).getEndX();
                                        double differenceY2 = Molecule[atomNumber].getTranslateY() - Rays.get(index).getEndY();
                                        Rays.get(index).setEndX(x + differenceX2);
                                        Rays.get(index).setEndY(y + differenceY2);

                                        Rays.get(index).setVisible(false);
                                        //Adds ray to board
                                        Board.getChildren().add(Rays.get(index));
                                        entries[EntryNum].setMouseTransparent(true);
                                        rayMarkerCount++;
                                        return;
                                    }
                                }
                            }

                            int[] indx = new int[2];
                            indx[0] = adjacentHexagon; //where ray hits first circle of influence
                            indx[1] = countAndSecondOccurrence[1]; //where ray hits second circle of influence

                            //Ray is set invisible
                            Rays.get(index).setVisible(false);

                            //Adds previous ray to pane
                            Board.getChildren().add(Rays.get(index++));

                            //For debugging
                            System.out.println("\nFirst Contact Point: " + adjacentHexagon);
                            System.out.println("count: " + countAndSecondOccurrence[0]);

                            //Deflects ray appropriately
                            double[] Vel = deflectRay(addToXCoordinates, addToYCoordinates, indx, countAndSecondOccurrence[0]);
                            addToYCoordinates = Vel[1] / 2;
                            addToXCoordinates = Vel[0] / 2;

                           //creates a new line to simulate that deflection
                            Rays.add(new Line());
                            Rays.get(index).setStartX(Rays.get(index - 1).getEndX());
                            Rays.get(index).setStartY(Rays.get(index - 1).getEndY());
                            Rays.get(index).setStrokeWidth(2);
                            Rays.get(index).setStroke(Color.WHITE);
                            break;
                    }
                }
                if(isDeflectedFlag) //break loop if a deflection is found
                    break;
            }


            if(addToXCoordinates > 0 || addToYCoordinates > 0) // go to bottom right
            {
                for(int z = 0; z < 9; z++)
                {
                    double distance = Math.abs(Math.hypot(Rays.get(index).getEndX() - rayEntry[bottomRightLoc[z]-1][0], Rays.get(index).getEndY() - rayEntry[bottomRightLoc[z]-1][1]));
                    if(distance <= 10)
                    {
                        //ray ends and is set invisible, colours of entries are changed  and set, so they can no longer be clicked
                        rayMarkerCount+=2;
                        Rays.get(index).setEndX(Rays.get(index).getEndX());
                        Rays.get(index).setEndY(Rays.get(index).getEndY());
                        entries[bottomRightLoc[z]-1].setFill(colorsForRayMarker());
                        entries[bottomRightLoc[z]-1].setMouseTransparent(true);
                        entries[EntryNum].setFill(colorsForRayMarker());
                        entries[EntryNum].setMouseTransparent(true);
                        colorIndex++;
                        Rays.get(index).setVisible(false);
                        Board.getChildren().add(Rays.get(index));
                        return;
                    }
                }
            }
            if(addToXCoordinates > 0 || addToYCoordinates == 0) // go to right
            {
                for(int z = 0; z < 9; z++)
                {
                    double distance = Math.abs(Math.hypot(Rays.get(index).getEndX() - rayEntry[rightLoc[z]-1][0], Rays.get(index).getEndY() - rayEntry[rightLoc[z]-1][1]));
                    if(distance <= 10)
                    {
                        //ray ends and is set invisible, colours of entries are changed  and set, so they can no longer be clicked
                        rayMarkerCount+=2;
                        Rays.get(index).setEndX(Rays.get(index).getEndX());
                        Rays.get(index).setEndY(Rays.get(index).getEndY());
                        entries[rightLoc[z]-1].setFill(colorsForRayMarker());
                        entries[rightLoc[z]-1].setMouseTransparent(true);
                        entries[EntryNum].setFill(colorsForRayMarker());
                        entries[EntryNum].setMouseTransparent(true);
                        colorIndex++;
                        Rays.get(index).setVisible(false);
                        Board.getChildren().add(Rays.get(index));
                        return;
                    }
                }

            }
            if(addToXCoordinates > 0 || addToYCoordinates < 0) // go to top right
            {
                for(int z = 0; z < 9; z++)
                {
                    double distance = Math.abs(Math.hypot(Rays.get(index).getEndX() - rayEntry[topRightLoc[z]-1][0], Rays.get(index).getEndY() - rayEntry[topRightLoc[z]-1][1]));
                    if(distance <= 10)
                    {
                        //ray ends and is set invisible, colours of entries are changed  and set, so they can no longer be clicked
                        rayMarkerCount+=2;
                        Rays.get(index).setEndX(Rays.get(index).getEndX());
                        Rays.get(index).setEndY(Rays.get(index).getEndY());
                        entries[topRightLoc[z]-1].setFill(colorsForRayMarker());
                        entries[topRightLoc[z]-1].setMouseTransparent(true);
                        entries[EntryNum].setFill(colorsForRayMarker());
                        entries[EntryNum].setMouseTransparent(true);
                        colorIndex++;
                        Rays.get(index).setVisible(false);
                        Board.getChildren().add(Rays.get(index));
                        return;
                    }
                }
            }
            if(addToXCoordinates < 0 || addToYCoordinates < 0) // go to top left
            {
                for(int z = 0; z < 9; z++)
                {
                    double distance = Math.abs(Math.hypot(Rays.get(index).getEndX() - rayEntry[topLeftLoc[z]-1][0], Rays.get(index).getEndY() - rayEntry[topLeftLoc[z]-1][1]));
                    if(distance <= 10)
                    {
                        //ray ends and is set invisible, colours of entries are changed  and set, so they can no longer be clicked
                        rayMarkerCount+=2;
                        Rays.get(index).setEndX(Rays.get(index).getEndX());
                        Rays.get(index).setEndY(Rays.get(index).getEndY());
                        entries[topLeftLoc[z]-1].setFill(colorsForRayMarker());
                        entries[topLeftLoc[z]-1].setMouseTransparent(true);
                        entries[EntryNum].setFill(colorsForRayMarker());
                        entries[EntryNum].setMouseTransparent(true);
                        colorIndex++;
                        Rays.get(index).setVisible(false);
                        Board.getChildren().add(Rays.get(index));
                        return;
                    }
                }
            }
            if(addToXCoordinates < 0 || addToYCoordinates == 0) // go to left
            {
                for(int z = 0; z < 9; z++)
                {
                    double distance = Math.abs(Math.hypot(Rays.get(index).getEndX() - rayEntry[leftLoc[z]-1][0], Rays.get(index).getEndY() - rayEntry[leftLoc[z]-1][1]));
                    if(distance <= 10)
                    {
                        //ray ends and is set invisible, colours of entries are changed  and set, so they can no longer be clicked
                        rayMarkerCount+=2;
                        Rays.get(index).setEndX(Rays.get(index).getEndX());
                        Rays.get(index).setEndY(Rays.get(index).getEndY());
                        entries[leftLoc[z]-1].setFill(colorsForRayMarker());
                        entries[leftLoc[z]-1].setMouseTransparent(true);
                        entries[EntryNum].setFill(colorsForRayMarker());
                        entries[EntryNum].setMouseTransparent(true);
                        colorIndex++;
                        Rays.get(index).setVisible(false);
                        Board.getChildren().add(Rays.get(index));
                        return;
                    }
                }
            }
            if(addToXCoordinates < 0 || addToYCoordinates > 0) // go to bottom left
            {
                for(int z = 0; z < 9; z++)
                {
                    double distance = Math.abs(Math.hypot(Rays.get(index).getEndX() - rayEntry[bottomLeftLoc[z]-1][0], Rays.get(index).getEndY() - rayEntry[bottomLeftLoc[z]-1][1]));
                    if(distance <= 10)
                    {
                        //ray ends and is set invisible, colours of entries are changed  and set, so they can no longer be clicked
                        rayMarkerCount+=2;
                        Rays.get(index).setEndX(Rays.get(index).getEndX());
                        Rays.get(index).setEndY(Rays.get(index).getEndY());
                        entries[bottomLeftLoc[z]-1].setFill(colorsForRayMarker());
                        entries[bottomLeftLoc[z]-1].setMouseTransparent(true);
                        entries[EntryNum].setFill(colorsForRayMarker());
                        entries[EntryNum].setMouseTransparent(true);
                        colorIndex++;
                        Rays.get(index).setVisible(false);
                        Board.getChildren().add(Rays.get(index));
                        return;
                    }
                }
            }
        }
    }

    public int[] countOccurrences(int[][] HexagonsIndex, int key)
    {
        //counts the amount of circle of influences that intersect at hexagon location key
        int count = 0;
        boolean flag = false;
        int[] countAndSecondOccurrence = new int[2];
        for(int atomNumber = 0; atomNumber < 6; atomNumber++) {
            for (int adjacentHexagon = 0; adjacentHexagon < 6; adjacentHexagon++) {
                if (HexagonsIndex[atomNumber][adjacentHexagon] == key) {
                    count++;
                    if(count == 2 && !flag) {
                        countAndSecondOccurrence[1] = adjacentHexagon;
                        flag = true;
                    }
                }
            }
        }
        countAndSecondOccurrence[0] = count;
        return countAndSecondOccurrence;
    }




    public double[] deflectRay(double addToXCoordinates, double addToYCoordinates, int[] index, int count) {
        double[] Vel = new double[2];
        double directionY = (2 * Math.sqrt(1875)) * Math.sin(Math.PI/3);
        double directionX = Math.sqrt(1875);

        int firstContactPoint = index[0];
        int secondContactPoint = index[1];

        int bottomRight = 3;
        int bottomLeft = 2;
        int topRight = 5;
        int topLeft = 4;
        int left = 0;
        int right = 1;

        if (count == 1) //ray hits 1 circle of influence
        {
            if(addToXCoordinates > 0 && firstContactPoint == bottomLeft) //If ray hits bottom left
            {
                addToYCoordinates = directionY;
                addToXCoordinates = Math.sqrt(1875);
            }
            else if(addToXCoordinates < 0 && firstContactPoint == bottomLeft) //If ray hits bottom left
            {
                addToYCoordinates = 0;
                addToXCoordinates = -88;
            }
            else if(addToXCoordinates < 0 && firstContactPoint == bottomRight) //If ray hits bottom right
            {
                addToYCoordinates = directionY;
                addToXCoordinates = Math.sqrt(1875) * -1;
            }
            else if(addToXCoordinates > 0 && firstContactPoint == bottomRight) //If ray hits bottom right
            {
                addToYCoordinates = 0;
                addToXCoordinates = 88;
            }
            else if(addToXCoordinates > 0 && firstContactPoint == 4)
            {
                addToYCoordinates = directionY * -1;
                addToXCoordinates = Math.sqrt(1875);
            }
            else if(addToXCoordinates < 0 && firstContactPoint == topLeft) //If ray hits top left
            {
                addToYCoordinates = 0;
                addToXCoordinates = -88;
            }
            else if(addToXCoordinates < 0 && firstContactPoint == topRight) //If ray hits top right
            {
                addToYCoordinates = directionY * -1;
                addToXCoordinates = Math.sqrt(1875) * -1;
            }
            else if(addToXCoordinates > 0 && firstContactPoint == topRight) //If ray hits top right
            {
                addToYCoordinates = 0;
                addToXCoordinates = 88;
            }
            else if(addToYCoordinates > 0 && firstContactPoint == left) //If ray hits left
            {
                addToXCoordinates = Math.sqrt(1875) * -1;
                addToYCoordinates = directionY;
            }
            else if(addToYCoordinates > 0 && firstContactPoint == right) //If ray hits right
            {
                addToXCoordinates = Math.sqrt(1875);
                addToYCoordinates = directionY;
            }
            else if(addToYCoordinates < 0 && firstContactPoint == left) //If ray hits left
            {
                addToXCoordinates = Math.sqrt(1875) * -1;
                addToYCoordinates = directionY * -1;
            }
            else if(addToYCoordinates < 0 && firstContactPoint == right) //If ray hits right
            {
                addToXCoordinates = Math.sqrt(1875);
                addToYCoordinates = directionY * -1;
            }
        }
        else if(count == 2) //two circles of influence are detected
       {
           System.out.println("Second contact point: " + secondContactPoint);
            if(addToXCoordinates > 0 && addToYCoordinates == 0) //ray coming from west cases
            {
                if(firstContactPoint == bottomLeft && secondContactPoint == topLeft || secondContactPoint == bottomLeft && firstContactPoint == topLeft)
                {
                    addToYCoordinates = 0;
                    addToXCoordinates = -88;
                }
                else if(firstContactPoint == bottomLeft && secondContactPoint == left || firstContactPoint == left && secondContactPoint == bottomLeft)
                {
                    addToYCoordinates = directionY;
                    addToXCoordinates = directionX * -1;
                }
                else if(firstContactPoint == topLeft && secondContactPoint == left || firstContactPoint == left && secondContactPoint == topLeft)
                {
                    addToYCoordinates = directionY * -1;
                    addToXCoordinates = directionX * -1;
                }
            }
            else if (addToXCoordinates < 0 && addToYCoordinates == 0) //ray coming from east cases
            {
                if(firstContactPoint == bottomRight && secondContactPoint == topRight || firstContactPoint == topRight && secondContactPoint == bottomRight)
                {
                    addToYCoordinates = 0;
                    addToXCoordinates = 88;
                }
                else if(firstContactPoint == bottomRight && secondContactPoint == right || firstContactPoint == right && secondContactPoint == bottomRight)
                {
                    addToYCoordinates = directionY;
                    addToXCoordinates = directionX;
                }
                else if(firstContactPoint == right && secondContactPoint == topRight || firstContactPoint == topRight && secondContactPoint == right)
                {
                    addToYCoordinates = directionY * -1;
                    addToXCoordinates = directionX;
                }
            }
            else if(addToXCoordinates > 0 && addToYCoordinates > 0) //ray coming from northwest cases
            {
                if(firstContactPoint == left && secondContactPoint == topRight || firstContactPoint == topRight && secondContactPoint == left)
                {
                    addToYCoordinates = 0;
                    addToXCoordinates = -88;
                }
                else if(firstContactPoint == topLeft && secondContactPoint == topRight || firstContactPoint == topRight && secondContactPoint == topLeft)
                {
                    addToYCoordinates = directionY * -1;
                    addToXCoordinates = directionX;
                }
                else
                {
                    addToYCoordinates *= -1;
                    addToXCoordinates *= -1;
                }

            }
            else if(addToXCoordinates < 0 && addToYCoordinates > 0) //ray coming from northeast cases
            {
                if(firstContactPoint == right && secondContactPoint == topRight || firstContactPoint == topRight && secondContactPoint == right)
                {
                    addToYCoordinates = 0;
                    addToXCoordinates = 88;
                }
                else if(firstContactPoint == topRight && secondContactPoint == topLeft || firstContactPoint == topLeft && secondContactPoint == topRight)
                {
                    addToYCoordinates = directionY * -1;
                    addToXCoordinates = directionX * -1;
                }
                else
                {
                    addToYCoordinates *= -1;
                    addToXCoordinates *= -1;
                }
            }
            else if(addToXCoordinates < 0 && addToYCoordinates < 0) //ray coming from southeast cases
            {
                if(firstContactPoint == right && secondContactPoint == bottomRight || firstContactPoint == bottomRight && secondContactPoint == right)
                {
                    addToYCoordinates = 0;
                    addToXCoordinates = 88;
                }
                else if(firstContactPoint == bottomLeft && secondContactPoint == bottomRight || firstContactPoint == bottomRight && secondContactPoint == bottomLeft)
                {
                    addToYCoordinates = directionY;
                    addToXCoordinates = directionX * -1;
                }
                else {
                    addToYCoordinates *= -1;
                    addToXCoordinates *= -1;
                }
            }
            else if(addToXCoordinates > 0 && addToYCoordinates < 0) //ray coming from southwest cases
            {
                if(firstContactPoint == bottomLeft && secondContactPoint == bottomRight || firstContactPoint ==bottomRight && secondContactPoint == bottomLeft){
                    addToYCoordinates = directionY;
                    addToXCoordinates = directionX;
                }
                else
                {
                    addToYCoordinates *= -1;
                    addToXCoordinates *= -1;
                }
            }
        }
        else if(count == 3)//three circles of influence are detected
        {
            addToXCoordinates = addToXCoordinates * -1;
            addToYCoordinates = addToYCoordinates * -1;
        }

        Vel[0] = addToXCoordinates;
        Vel[1] = addToYCoordinates;
        return Vel;
    }


    public double[] CalculateVelocity(int EntryNum)
    {
        double addx = 0;
        double addy = 0;

        int[] arr1 = new int[]{1, 13, 17,21, 25, 31, 35, 39, 43};
        int[] arr2 = new int[]{12, 16, 20, 24, 29, 33, 37, 41, 52};
        int[] arr3 = new int[]{53, 42, 38, 34, 30, 50, 48, 46, 44};
        int[] arr4 = new int[]{3,5,7,9,11,15,19,23,28};
        int[] arr5 = new int[]{27, 32, 36, 40, 45, 47, 49, 51, 54};
        int[] arr6 = new int[]{2,4,6, 8, 10, 14,18,22,26};

        int EntryCompareValue = EntryNum + 1;

        double directionY = (2 * Math.sqrt(1875)) * Math.sin(Math.PI/3);

        //Determines the direction the initial ray travels
        for(int w = 0; w < 9; w++)
        {
            if(arr1[w] == EntryCompareValue)
            {
                addx = 88;
                addy = 0;
                break;
            }
            if(arr2[w] == EntryCompareValue)
            {
                addy = 0;
                addx = -88;
                break;
            }
            if(arr3[w] == EntryCompareValue)
            {
                addy = directionY * -1;
                addx = Math.sqrt(1875) * -1;
                break;
            }
            if(arr4[w] == EntryCompareValue)
            {
                addx = Math.sqrt(1875) * -1;
                addy = directionY;
                break;
            }
            if(arr5[w] == EntryCompareValue)
            {
                addx = Math.sqrt(1875);
                addy = directionY * -1;
                break;
            }
            if(arr6[w] == EntryCompareValue)
            {
                addy = directionY;
                addx = Math.sqrt(1875);
                break;
            }
        }
        double[] Velocity = new double[4];
        Velocity[0] = addx;
        Velocity[1] = addy;
        return Velocity;
    }

    public int getRayMarkerCount() {
        return rayMarkerCount;
    }

    private Color colorsForRayMarker()
    {
        return colorsForRayMarkers[colorIndex];
    }


}