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

        double AbsorptionTolerance = 10;
        double CircleOfInfluenceTolerance = 8;
        boolean isDeflectedFlag = false;

        while(true) {
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


                            int[] indx = new int[2];
                            indx[0] = adjacentHexagon;
                            indx[1] = countAndSecondOccurrence[1];

                            //Adds previous ray to pane
                            Board.getChildren().add(Rays.get(index++));

                            //For debugging
                            System.out.println("\nHexagon index: " + adjacentHexagon);
                            System.out.println("count: " + countAndSecondOccurrence[0]);

                            //Deflects ray appropriately and creates a new line to simulate that deflection
                            double[] Vel = deflectRay(addToXCoordinates, addToYCoordinates, indx, countAndSecondOccurrence[0]);
                            addToYCoordinates = Vel[1] / 2;
                            addToXCoordinates = Vel[0] / 2;

                            Rays.add(new Line());
                            Rays.get(index).setStartX(Rays.get(index - 1).getEndX());
                            Rays.get(index).setStartY(Rays.get(index - 1).getEndY());
                            Rays.get(index).setStrokeWidth(2);
                            Rays.get(index).setStroke(Color.WHITE);
                            break;
                    }
                }
                if(isDeflectedFlag)
                    break;
            }

            //Checks if ray is absorbed
            for (int atomNumber = 0; atomNumber < 6; atomNumber++) {
                //See if coordinates of endpoint of a line approximately matches the coordinates of the centre of the atom
                boolean areXCoordinatesEqual = Math.abs(Molecule[atomNumber].getTranslateX() - (Rays.get(index).getEndX() + addToXCoordinates*2)) <= AbsorptionTolerance;
                boolean areYCoordinatesEqual = Math.abs((Rays.get(index).getEndY() + addToYCoordinates*2) - Molecule[atomNumber].getTranslateY()) <= AbsorptionTolerance;
                boolean areXCoordinatesEqual2 = Math.abs(Molecule[atomNumber].getTranslateX() - Rays.get(index).getEndX()) <= AbsorptionTolerance;
                boolean areYCoordinatesEqual2 = Math.abs(Rays.get(index).getEndY() - Molecule[atomNumber].getTranslateY()) <= AbsorptionTolerance;
                boolean isAbsorbed = (areXCoordinatesEqual && areYCoordinatesEqual) || (areXCoordinatesEqual2 && areYCoordinatesEqual2);
                if (isAbsorbed) {
                    //Marks the entry as absorbed
                    entries[EntryNum].setFill(Color.RED);

                    //Adjusts ray endpoint at middle of atom appropriately
                    double differenceX = Molecule[atomNumber].getTranslateX() - Rays.get(index).getEndX();
                    double differenceY = Molecule[atomNumber].getTranslateY() - Rays.get(index).getEndY();
                    Rays.get(index).setEndX(x + differenceX);
                    Rays.get(index).setEndY(y + differenceY);

                    //Adds ray to board
                    Board.getChildren().add(Rays.get(index));
                    entries[EntryNum].setMouseTransparent(true);
                    rayMarkerCount++;
                    return;
                }
            }

            //Ray continues to travel until entry endpoint
            x += addToXCoordinates;
            y += addToYCoordinates;
            Rays.get(index).setEndX(x);
            Rays.get(index).setEndY(y);

            if(addToXCoordinates > 0 || addToYCoordinates > 0) // go to bottom right
            {
                for(int z = 0; z < 9; z++)
                {
                    double distance = Math.abs(Math.hypot(Rays.get(index).getEndX() - rayEntry[bottomRightLoc[z]-1][0], Rays.get(index).getEndY() - rayEntry[bottomRightLoc[z]-1][1]));
                    if(distance <= 10)
                    {
                        rayMarkerCount+=2;
                        Rays.get(index).setEndX(Rays.get(index).getEndX());
                        Rays.get(index).setEndY(Rays.get(index).getEndY());
                        entries[bottomRightLoc[z]-1].setFill(colorsForRayMarker());
                        entries[bottomRightLoc[z]-1].setMouseTransparent(true);
                        entries[EntryNum].setFill(colorsForRayMarker());
                        entries[EntryNum].setMouseTransparent(true);
                        colorIndex++;
                        Board.getChildren().add(Rays.get(index));
                        //Rays.forEach(ray -> ray.setVisible(false));
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
                        rayMarkerCount+=2;
                        Rays.get(index).setEndX(Rays.get(index).getEndX());
                        Rays.get(index).setEndY(Rays.get(index).getEndY());
                        entries[rightLoc[z]-1].setFill(colorsForRayMarker());
                        entries[rightLoc[z]-1].setMouseTransparent(true);
                        entries[EntryNum].setFill(colorsForRayMarker());
                        entries[EntryNum].setMouseTransparent(true);
                        colorIndex++;
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
                        rayMarkerCount+=2;
                        Rays.get(index).setEndX(Rays.get(index).getEndX());
                        Rays.get(index).setEndY(Rays.get(index).getEndY());
                        entries[topRightLoc[z]-1].setFill(colorsForRayMarker());
                        entries[topRightLoc[z]-1].setMouseTransparent(true);
                        entries[EntryNum].setFill(colorsForRayMarker());
                        entries[EntryNum].setMouseTransparent(true);
                        colorIndex++;
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
                        rayMarkerCount+=2;
                        Rays.get(index).setEndX(Rays.get(index).getEndX());
                        Rays.get(index).setEndY(Rays.get(index).getEndY());
                        entries[topLeftLoc[z]-1].setFill(colorsForRayMarker());
                        entries[topLeftLoc[z]-1].setMouseTransparent(true);
                        entries[EntryNum].setFill(colorsForRayMarker());
                        entries[EntryNum].setMouseTransparent(true);
                        colorIndex++;
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
                        rayMarkerCount+=2;
                        Rays.get(index).setEndX(Rays.get(index).getEndX());
                        Rays.get(index).setEndY(Rays.get(index).getEndY());
                        entries[leftLoc[z]-1].setFill(colorsForRayMarker());
                        entries[leftLoc[z]-1].setMouseTransparent(true);
                        entries[EntryNum].setFill(colorsForRayMarker());
                        entries[EntryNum].setMouseTransparent(true);
                        colorIndex++;
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
                        rayMarkerCount+=2;
                        Rays.get(index).setEndX(Rays.get(index).getEndX());
                        Rays.get(index).setEndY(Rays.get(index).getEndY());
                        entries[bottomLeftLoc[z]-1].setFill(colorsForRayMarker());
                        entries[bottomLeftLoc[z]-1].setMouseTransparent(true);
                        entries[EntryNum].setFill(colorsForRayMarker());
                        entries[EntryNum].setMouseTransparent(true);
                        colorIndex++;
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
        int[] countAndSecondOccurrence = new int[2];
        for(int atomNumber = 0; atomNumber < 6; atomNumber++) {
            for (int adjacentHexagon = 0; adjacentHexagon < 6; adjacentHexagon++) {
                if (HexagonsIndex[atomNumber][adjacentHexagon] == key)
                    count++;
                if(count == 2)
                    countAndSecondOccurrence[1] = adjacentHexagon;
            }
        }
        countAndSecondOccurrence[0] = count;
        return countAndSecondOccurrence;
    }




    public double[] deflectRay(double addToXCoordinates, double addToYCoordinates, int[] index, int count) {
        double[] Vel = new double[2];
        double directionY = (2 * Math.sqrt(1875)) * Math.sin(Math.PI/3);

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
            if(addToXCoordinates > 0 && addToYCoordinates == 0 && (firstContactPoint == topLeft && secondContactPoint == bottomLeft) || (secondContactPoint == topLeft  && firstContactPoint == bottomLeft))//ray hits top left && bottom left
            {
               addToXCoordinates = -88;
            }
           if(addToXCoordinates < 0 && addToYCoordinates == 0 && (secondContactPoint == topRight && firstContactPoint == bottomRight) || (firstContactPoint == topRight && secondContactPoint == bottomRight))//ray hits top right && bottom right
            {
                addToXCoordinates = 88;
            }
           if(addToXCoordinates < 0 && addToYCoordinates == 0 && (firstContactPoint == right && secondContactPoint == bottomRight) || (firstContactPoint == bottomRight && secondContactPoint == right)) //ray hits bottom right && right
            {
                addToYCoordinates = directionY;
                addToXCoordinates = Math.sqrt(1875);
            }
            if(addToXCoordinates < 0 && addToYCoordinates < 0 && (secondContactPoint == bottomRight && firstContactPoint == right) || (firstContactPoint == bottomRight && secondContactPoint == right))//ray hits bottom right && right
            {
                addToYCoordinates = 0;
                addToXCoordinates = 88;
            }
           if(addToXCoordinates > 0 && addToYCoordinates == 0 && (firstContactPoint == topLeft && secondContactPoint == left) || (secondContactPoint == topLeft && firstContactPoint == left))//ray hits top left && left
            {
               addToYCoordinates = (2 * Math.sqrt(1875)) * Math.sin(Math.PI/3) * -1;
               addToXCoordinates = Math.sqrt(1875) * -1;
            }
            if(addToXCoordinates > 0 && addToYCoordinates > 0 && (firstContactPoint == topLeft && secondContactPoint == left) || (secondContactPoint == topLeft && firstContactPoint == left))//ray hits top left && left
           {
                addToYCoordinates = 0;
                addToXCoordinates = -88;
            }
            if(addToXCoordinates > 0 && addToYCoordinates == 0 && (secondContactPoint == bottomLeft && firstContactPoint == left) || (firstContactPoint == bottomLeft && secondContactPoint == left))//ray hits bottom left && left
            {
                addToYCoordinates = directionY;
                addToXCoordinates = Math.sqrt(1875) * -1;
            }
            if(addToXCoordinates > 0 && addToYCoordinates < 0 && (secondContactPoint == bottomLeft && firstContactPoint == left) || (firstContactPoint == bottomLeft && secondContactPoint == left))//ray hits bottom left && left
            {
                addToYCoordinates = 0;
                addToXCoordinates = -88;
            }
            if(addToXCoordinates < 0 && addToYCoordinates == 0 && (secondContactPoint == topRight && firstContactPoint == right) || (firstContactPoint == topRight && secondContactPoint == right))//ray hits top right && right
            {
                addToYCoordinates = directionY * -1;
                addToXCoordinates = Math.sqrt(1875);
            }
            if(addToXCoordinates < 0 && addToYCoordinates > 0 && (secondContactPoint == topRight && firstContactPoint == right) || (firstContactPoint == topRight && secondContactPoint == right))//ray hits top right && right
            {
                addToYCoordinates = 0;
                addToXCoordinates = 88;
            }
            if(addToXCoordinates > 0 && (secondContactPoint == topRight && firstContactPoint == topLeft) || (firstContactPoint == topRight && secondContactPoint == topLeft))//ray hits top right && top left
            {
                addToYCoordinates = directionY * -1;
                addToXCoordinates = Math.sqrt(1875);
            }
            if(addToXCoordinates < 0 && (firstContactPoint == topRight && secondContactPoint == topLeft) || (secondContactPoint == topRight && firstContactPoint == topLeft))//ray hits top right && top left
            {
                addToYCoordinates = directionY * -1;
                addToXCoordinates = Math.sqrt(1875) * -1;
            }
            if(addToXCoordinates > 0 && (secondContactPoint == bottomRight && firstContactPoint == bottomLeft) || (firstContactPoint == bottomRight && secondContactPoint == bottomLeft)) //ray hits bottom right && bottom left
            {
               addToYCoordinates = directionY;
               addToXCoordinates = Math.sqrt(1875);
            }
            if(addToXCoordinates < 0 && (firstContactPoint == bottomRight && secondContactPoint == bottomLeft) || (secondContactPoint == bottomRight && firstContactPoint == bottomLeft))//ray hits bottom right && bottom left
            {
                addToYCoordinates = directionY;
                addToXCoordinates = Math.sqrt(1875) * -1;
            }
            if(((secondContactPoint == topLeft && firstContactPoint == bottomRight) || (firstContactPoint == topLeft && secondContactPoint == topRight)) || ((secondContactPoint == bottomLeft && firstContactPoint == topRight) || (secondContactPoint == topRight && firstContactPoint == bottomLeft)))// hit top left && bottom right || hit bottom left && top right
           {
                addToXCoordinates = addToXCoordinates * -1;
                addToYCoordinates = addToYCoordinates * -1;
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