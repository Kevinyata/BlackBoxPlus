package org.example.sep2_group46;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Sphere;

import java.util.ArrayList;

public class RayPath {
    private final double[][] rayEntry; // double array to store the location of the ray entry points;

    public RayPath(double[][] rayEntry)
    {
        this.rayEntry =  rayEntry;
    }

    public void createRayPath(Pane Board, int EntryNum, Circle[] entries , AtomCreator atomCreator)
    {
        //Obtains X and Y location of entry
        double x = rayEntry[EntryNum][0];
        double y = rayEntry[EntryNum][1];

        //Determines the direction of the ray travels in
        double[] Velocity = CalculateVelocity(EntryNum);

        double addToXCoordinates = Velocity[0]/2;
        double addToYCoordinates = Velocity[1]/2;
        int dist = (int) Velocity[2]*2;
        int dest = (int) Velocity[3];

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
        Rays.get(index).setFill(Color.WHITE);


        //Checks if ray will be reflected and if so marks appropriate ray entry
        for(int k = 0; k < 6; k++) {
            double x3 = (entries[EntryNum].getLayoutX() - Molecule[k].getTranslateX());
            double y3 = (entries[EntryNum].getLayoutY() - Molecule[k].getTranslateY());
            double distance = Math.hypot(x3, y3);
            boolean isReflected = distance >= 70 && distance <= 80;

            if(isReflected) {
                entries[EntryNum].setFill(Color.WHITE);
                return;
            }
        }

        int currentDistance = 0;
        double AbsorptionTolerance = 10;
        double CircleOfInfluenceTolerance = 0.7;
        while(currentDistance < dist) {

            //Checks if ray is absorbed
            for (int atomNumber = 0; atomNumber < 6; atomNumber++) {
                //See if coordinates of endpoint of a line approximately matches the coordinates of the centre of the atom
                boolean areXCoordinatesEqual = Math.abs(Molecule[atomNumber].getTranslateX() - Rays.get(index).getEndX()) <= AbsorptionTolerance;
                boolean areYCoordinatesEqual = Math.abs(Rays.get(index).getEndY() - Molecule[atomNumber].getTranslateY()) <= AbsorptionTolerance;
                boolean isAbsorbed = areXCoordinatesEqual && areYCoordinatesEqual;
                if (isAbsorbed) {
                    entries[EntryNum].setFill(Color.RED);
                    Rays.get(index).setEndX(x + addToXCoordinates);
                    Rays.get(index).setEndY(y + addToYCoordinates);
                    Board.getChildren().add(Rays.get(index));
                    return;
                }
            }

            //Checks if ray is deflected
            for (int atomNumber = 0; atomNumber < 6; atomNumber++) {
                for (int adjacentHexagon = 0; adjacentHexagon < 6; adjacentHexagon++) {
                    boolean isAdjacentHexagonLocation = COIHexagonIndex[atomNumber][adjacentHexagon] != 0;
                    boolean areXCoordinatesEqual = Math.abs(xyCoordinates[0][COIHexagonIndex[atomNumber][adjacentHexagon]] - Rays.get(index).getEndX()) <= CircleOfInfluenceTolerance;
                    boolean areYCoordinatesEqual = Math.abs(xyCoordinates[1][COIHexagonIndex[atomNumber][adjacentHexagon]] - Rays.get(index).getEndY()) <= CircleOfInfluenceTolerance;
                    boolean isDeflected = isAdjacentHexagonLocation && areXCoordinatesEqual && areYCoordinatesEqual;
                    if (isDeflected) {

                        //Checks if the ray hits 1,2 or 3 circles of influence
                        int count = countOccurrences(COIHexagonIndex, COIHexagonIndex[atomNumber][adjacentHexagon]);

                        //Adds previous ray to pane
                        Board.getChildren().add(Rays.get(index));

                        //For debugging
                        System.out.println("\nHexagon index: " + adjacentHexagon);
                        System.out.println("count: " + count);

                        //Deflects ray appropriately and creates a new line to simulate that deflection
                        double[] Vel = deflectRay(addToXCoordinates, addToYCoordinates, adjacentHexagon, count);
                        addToYCoordinates = Vel[1]/2;
                        addToXCoordinates = Vel[0]/2;

                        Rays.add(new Line());
                        index++;

                        Rays.get(index).setStartX(Rays.get(index - 1).getEndX());
                        Rays.get(index).setStartY(Rays.get(index - 1).getEndY());
                        Rays.get(index).setStrokeWidth(2);
                        Rays.get(index).setStroke(Color.WHITE);
                        Rays.get(index).setFill(Color.WHITE);
                    }
                }
            }

            //Ray continues to travel until entry endpoint
            x += addToXCoordinates;
            y += addToYCoordinates;
            Rays.get(index).setEndX(x);
            Rays.get(index).setEndY(y);
            currentDistance++;
        }

        //Adds ray to pane
        Board.getChildren().add(Rays.get(index));

        //Sets ray invisible
        //Ray.setVisible(false);
    }

    public int countOccurrences(int[][] HexagonsIndex, int key)
    {
        //counts the amount of circle of influences that intersect at hexagon location key
        int count = 0;
        for(int atomNumber = 0; atomNumber < 6; atomNumber++) {
            for (int adjacentHexagon = 0; adjacentHexagon < 6; adjacentHexagon++) {
                if (HexagonsIndex[atomNumber][adjacentHexagon] == key)
                    count++;
            }
        }
        return count;
    }

    public double[] deflectRay(double addToXCoordinates, double addToYCoordinates, int index, int count) {
        double[] Vel = new double[2];
        double directionY = (2 * Math.sqrt(1875)) * Math.sin(Math.PI/3);
        if (count == 1) //ray hits 1 circle of influence
        {
            if(addToXCoordinates > 0 && index == 2) //If ray hits bottom left
            {
                addToYCoordinates = directionY;
                addToXCoordinates = Math.sqrt(1875);
            }
            else if(addToXCoordinates < 0 && index == 2) //If ray hits bottom left
            {
                addToYCoordinates = 0;
                addToXCoordinates = -88;
            }
            else if(addToXCoordinates < 0 && index == 3) //If ray hits bottom right
            {
                addToYCoordinates = directionY;
                addToXCoordinates = Math.sqrt(1875) * -1;
            }
            else if(addToXCoordinates > 0 && index == 3) //If ray hits bottom right
            {
                addToYCoordinates = 0;
                addToXCoordinates = 88;
            }
            else if(addToXCoordinates > 0 && index == 4)
            {
                addToYCoordinates = directionY * -1;
                addToXCoordinates = Math.sqrt(1875);
            }
            else if(addToXCoordinates < 0 && index == 4) //If ray hits top left
            {
                addToYCoordinates = 0;
                addToXCoordinates = -88;
            }
            else if(addToXCoordinates < 0 && index == 5) //If ray hits top right
            {
                addToYCoordinates = directionY * -1;
                addToXCoordinates = Math.sqrt(1875) * -1;
            }
            else if(addToXCoordinates > 0 && index == 5) //If ray hits top right
            {
                addToYCoordinates = 0;
                addToXCoordinates = 88;
            }
            else if(addToYCoordinates > 0 && index == 0) //If ray hits left
            {
                addToXCoordinates = Math.sqrt(1875) * -1;
                addToYCoordinates = directionY;
            }
            else if(addToYCoordinates > 0 && index == 1) //If ray hits right
            {
                addToXCoordinates = Math.sqrt(1875);
                addToYCoordinates = directionY;
            }
            else if(addToYCoordinates < 0 && index == 0) //If ray hits left
            {
                addToXCoordinates = Math.sqrt(1875) * -1;
                addToYCoordinates = directionY * -1;
            }
            else if(addToYCoordinates < 0 && index == 1) //If ray hits right
            {
                addToXCoordinates = Math.sqrt(1875);
                addToYCoordinates = directionY * -1;
            }
        }
//
//        //if(two circles of influence are detected)
//        {
//            //if(addx > 0 && addy == 0 && ray hits top left && bottom left)
//            {
//                addx = -88;
//            }
//            //if(addx < 0 && addy == 0 && ray hits top right && bottom right)
//            {
//                addx = 88;
//            }
//            //if(addx < 0 && addy == 0 && ray hits bottom right && right)
//            {
//                addy = (2 * Math.sqrt(1875)) * Math.sin(Math.PI/3);
//                addx = Math.sqrt(1875);
//            }
//            //if(addx < 0 && addy < 0 && ray hits bottom right && right)
//            {
//                addy = 0;
//                addx = 88;
//            }
//            //if(addx > 0 && addy == 0 && ray hits top left && left)
//            {
//                addy = (2 * Math.sqrt(1875)) * Math.sin(Math.PI/3) * -1;
//                addx = Math.sqrt(1875) * -1;
//            }
//            //if(addx > 0 && addy > 0 && ray hits top left && left)
//            {
//                addy = 0;
//                addx = -88;
//            }
//            //if(addx > 0 && addy == 0 && ray hits bottom left && left)
//            {
//                addy = (2 * Math.sqrt(1875)) * Math.sin(Math.PI/3);
//                addx = Math.sqrt(1875) * -1;
//            }
//            //if(addx > 0 && addy < 0 && ray hits bottom left && left)
//            {
//                addy = 0;
//                addx = -88;
//            }
//            //if(addx < 0 && addy == 0 && ray hits top right && right)
//            {
//                addy = (2 * Math.sqrt(1875)) * Math.sin(Math.PI/3) * -1;
//                addx = Math.sqrt(1875);
//            }
//            //if(addx < 0 && addy > 0 && ray hits top right && right)
//            {
//                addy = 0;
//                addx = 88;
//            }
//            //if(addx > 0 && ray hits top right && top left)
//            {
//                addy = (2 * Math.sqrt(1875)) * Math.sin(Math.PI/3) * -1;
//                addx = Math.sqrt(1875);
//            }
//            //if(addx < 0 && ray hits top right && top left)
//            {
//                addy = (2 * Math.sqrt(1875)) * Math.sin(Math.PI/3) * -1;
//                addx = Math.sqrt(1875) * -1;
//            }
//            //if(addx > 0 && ray hits bottom right && bottom left)
//            {
//                addy = (2 * Math.sqrt(1875)) * Math.sin(Math.PI/3);
//                addx = Math.sqrt(1875);
//            }
//            //if(addx < 0 && ray hits bottom right && bottom left)
//            {
//                addy = (2 * Math.sqrt(1875)) * Math.sin(Math.PI/3);
//                addx = Math.sqrt(1875) * -1;
//            }
//            //if((hit top left && bottom right) || (hit bottom left && top right))
//            {
//                addx = addx * -1;
//                addy = addy * -1;
//            }
        //}
//
//        //if(three circles of influence are detected)
//        {
//            addx = addx * -1;
//            addy = addy * -1;
//        }

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
        int[] arr7 = new int[]{5,6,7,8,9,8,7,6,5};
        int[] arr8 = new int[]{9,8,7,6,5,8,7,6,5};

        int EntryCompareValue = EntryNum + 1;

        double directionY = (2 * Math.sqrt(1875)) * Math.sin(Math.PI/3);

        //the entry where the ray ends
        int dest = 0;

        //distance the ray travels
        int dist = 0;

        //Determines the direction the initial ray travels
        for(int w = 0; w < 9; w++)
        {
            if(arr1[w] == EntryCompareValue)
            {
                addx = 88;
                addy = 0;
                dest = arr2[w];
                dist = arr7[w];
                break;
            }
            if(arr2[w] == EntryCompareValue)
            {
                addy = 0;
                addx = -88;
                dest = arr1[w];
                dist = arr7[w];
                break;
            }
            if(arr3[w] == EntryCompareValue)
            {
                addy = directionY * -1;
                addx = Math.sqrt(1875) * -1;
                dest = arr6[w];
                dist = arr8[w];
                break;
            }
            if(arr4[w] == EntryCompareValue)
            {
                addx = Math.sqrt(1875) * -1;
                addy = directionY;
                dest = arr5[w];
                dist = arr7[w];
                break;
            }
            if(arr5[w] == EntryCompareValue)
            {
                addx = Math.sqrt(1875);
                addy = directionY * -1;
                dest = arr4[w];
                dist = arr7[w];
                break;
            }
            if(arr6[w] == EntryCompareValue)
            {
                addy = directionY;
                addx = Math.sqrt(1875);
                dest = arr3[w];
                dist = arr8[w];
                break;
            }
        }
        double[] Velocity = new double[4];
        Velocity[0] = addx;
        Velocity[1] = addy;
        Velocity[2] = dist;
        Velocity[3] = dest;
        return Velocity;
    }


}
