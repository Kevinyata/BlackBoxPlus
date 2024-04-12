package org.example.sep2_group46;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Sphere;

import java.util.ArrayList;
import java.util.Arrays;

public class RayPath {
    private final double[][] rayEntry; // double array to store the location of the ray entry points;

    public RayPath(double[][] rayEntry)
    {
        this.rayEntry =  rayEntry;
    }

    public void createRayPath(Pane Board, int EntryNum, Circle[] entries , AtomCreator atomCreator)
    {

        double x = rayEntry[EntryNum][0];
        double y = rayEntry[EntryNum][1];

        double[] Velocity = CalculateVelocity(EntryNum);
        double addx = Velocity[0];
        double addy = Velocity[1];
        int dist = (int) Velocity[2];
        int dest = (int) Velocity[3];

        Sphere[] Molecule = atomCreator.getMolecule();
        int[][] COIHexagonIndex = atomCreator.getCOIHexagonIndx();
        double[][] xyCoord = atomCreator.getXyLocation();

        ArrayList<Line> Rays = new ArrayList<Line>();
        Rays.add(new Line());
        int index = 0;
        Rays.get(index).setStartX(x);
        Rays.get(index).setStartY(y);
        Rays.get(index).setStrokeWidth(2);
        Rays.get(index).setStroke(Color.WHITE);
        Rays.get(index).setFill(Color.WHITE);
        double[] Vel = new double[2];

        int m = 0;
        int tolerance =  10;
        while(m < dist*2) {

            for (int p = 0; p < 6; p++) {

                boolean isAbsorbed = Math.abs(Molecule[p].getTranslateX() - (Rays.get(index).getEndX() + addx)) <= tolerance && Math.abs((addy + Rays.get(index).getEndY()) - Molecule[p].getTranslateY()) <= tolerance;
                if (isAbsorbed) {
                    entries[EntryNum].setFill(Color.RED);
                    Rays.get(index).setEndX(x + addx/2);
                    Rays.get(index).setEndY(y + addy/2);
                    Board.getChildren().add(Rays.get(index));
                    return;
                }

                for (int q = 0; q < 6; q++) {
                    //System.out.print(COIHexagonIndex[p][q] + " ");
                    boolean isDeflected = COIHexagonIndex[p][q] != 0 && Math.abs(xyCoord[0][COIHexagonIndex[p][q]] - Rays.get(index).getEndX()) <= tolerance && Math.abs(xyCoord[1][COIHexagonIndex[p][q]] - Rays.get(index).getEndY()) <= tolerance;

                    if (isDeflected) {
                        int key = COIHexagonIndex[p][q];
                        Board.getChildren().add(Rays.get(index));

                        Vel = deflectRay(addx, addy, COIHexagonIndex, key, p);
                        addy = Vel[1];
                        addx = Vel[0];

                        Rays.add(new Line());
                        index++;

                        Rays.get(index).setStartX(Rays.get(index-1).getEndX());
                        Rays.get(index).setStartY(Rays.get(index-1).getEndY());
                        Rays.get(index).setStrokeWidth(2);
                        Rays.get(index).setStroke(Color.WHITE);
                        Rays.get(index).setFill(Color.WHITE);
                    }
                }
            }

            x += addx / 2;
            y += addy / 2;
            Rays.get(index).setEndX(x);
            Rays.get(index).setEndY(y);
            m++;
        }

//        if(!flag) {
//            entries[dest - 1].setFill(Color.LIMEGREEN);
//            entries[EntryNum].setFill(Color.LIMEGREEN);
//            entries[dest-1].setMouseTransparent(true);
//        }
        Board.getChildren().add(Rays.get(index));
        //Ray.setVisible(false);
    }

    public int CountOccurences(int[][] COIHexagonsIndx, int key)
    {
        int count = 0;
        for(int p = 0; p < 6; p++) {
            for (int q = 0; q < 6; q++) {
                if (COIHexagonsIndx[p][q] == key)
                    count++;
            }
        }
        return count;
    }

    public double[] deflectRay(double addx, double addy, int[][] COIHexagonIndex, int key, int indx) {
        int CirclesOfInfluenceHit = CountOccurences(COIHexagonIndex, key);
        int right = COIHexagonIndex[indx][0];
        int left = COIHexagonIndex[indx][1];
        int tl = COIHexagonIndex[indx][2];
        int tr = COIHexagonIndex[indx][3];
        int bl = COIHexagonIndex[indx][4];
        int br = COIHexagonIndex[indx][5];
        double[] Vel = new double[2];
        if (CirclesOfInfluenceHit == 1){
            if(addx > 0 && key == bl)
            {
                addy = (2 * Math.sqrt(1875)) * Math.sin(Math.PI/3);
                addx = Math.sqrt(1875);
            }
            else if(addx < 0 && key == bl)
            {
                addy = 0;
                addx = -88;
            }
            else if(addx < 0 && key == br)
            {
                addy = (2 * Math.sqrt(1875)) * Math.sin(Math.PI/3);
                addx = Math.sqrt(1875) * -1;
            }
            else if(addx > 0 && key == br)
            {
                addy = 0;
                addx = 88;
            }
            else if(addx > 0 && key == tl)
            {
                addy = (2 * Math.sqrt(1875)) * Math.sin(Math.PI/3) * -1;
                addx = Math.sqrt(1875);
            }
            else if(addx < 0 && key == tl)
            {
                addy = 0;
                addx = -88;
            }
            else if(addx < 0 && key == tr)
            {
                addy = (2 * Math.sqrt(1875)) * Math.sin(Math.PI/3) * -1;
                addx = Math.sqrt(1875) * -1;
            }
            else if(addx > 0 && key == tr)
            {
                addy = 0;
                addx = 88;
            }
            else if(addy > 0 && key == left)
            {
                addx = Math.sqrt(1875) * -1;
                addy = (2 * Math.sqrt(1875)) * Math.sin(Math.PI /3);
            }
            else if(addy > 0 && key == right)
            {
                addx = Math.sqrt(1875);
                addy = (2 * Math.sqrt(1875)) * Math.sin(Math.PI /3);
            }
            else if(addy < 0 && key == left)
            {
                addx = Math.sqrt(1875) * -1;
                addy = (2 * Math.sqrt(1875)) * Math.sin(Math.PI /3) * -1;
            }
            else if(addy < 0 && key == right)
            {
                addx = Math.sqrt(1875);
                addy = (2 * Math.sqrt(1875)) * Math.sin(Math.PI /3) * -1;
            }
        }

        //if(two circles of influence are detected)
        {
            //if(addx > 0 && addy == 0 && ray hits top left && bottom left)
            {
                addx = -88;
            }
            //if(addx < 0 && addy == 0 && ray hits top right && bottom right)
            {
                addx = 88;
            }
            //if(addx < 0 && addy == 0 && ray hits bottom right && right)
            {
                addy = (2 * Math.sqrt(1875)) * Math.sin(Math.PI/3);
                addx = Math.sqrt(1875);
            }
            //if(addx < 0 && addy < 0 && ray hits bottom right && right)
            {
                addy = 0;
                addx = 88;
            }
            //if(addx > 0 && addy == 0 && ray hits top left && left)
            {
                addy = (2 * Math.sqrt(1875)) * Math.sin(Math.PI/3) * -1;
                addx = Math.sqrt(1875) * -1;
            }
            //if(addx > 0 && addy > 0 && ray hits top left && left)
            {
                addy = 0;
                addx = -88;
            }
            //if(addx > 0 && addy == 0 && ray hits bottom left && left)
            {
                addy = (2 * Math.sqrt(1875)) * Math.sin(Math.PI/3);
                addx = Math.sqrt(1875) * -1;
            }
            //if(addx > 0 && addy < 0 && ray hits bottom left && left)
            {
                addy = 0;
                addx = -88;
            }
            //if(addx < 0 && addy == 0 && ray hits top right && right)
            {
                addy = (2 * Math.sqrt(1875)) * Math.sin(Math.PI/3) * -1;
                addx = Math.sqrt(1875);
            }
            //if(addx < 0 && addy > 0 && ray hits top right && right)
            {
                addy = 0;
                addx = 88;
            }
            //if(addx > 0 && ray hits top right && top left)
            {
                addy = (2 * Math.sqrt(1875)) * Math.sin(Math.PI/3) * -1;
                addx = Math.sqrt(1875);
            }
            //if(addx < 0 && ray hits top right && top left)
            {
                addy = (2 * Math.sqrt(1875)) * Math.sin(Math.PI/3) * -1;
                addx = Math.sqrt(1875) * -1;
            }
            //if(addx > 0 && ray hits bottom right && bottom left)
            {
                addy = (2 * Math.sqrt(1875)) * Math.sin(Math.PI/3);
                addx = Math.sqrt(1875);
            }
            //if(addx < 0 && ray hits bottom right && bottom left)
            {
                addy = (2 * Math.sqrt(1875)) * Math.sin(Math.PI/3);
                addx = Math.sqrt(1875) * -1;
            }
            //if((hit top left && bottom right) || (hit bottom left && top right))
            {
                addx = addx * -1;
                addy = addy * -1;
            }
        }

        //if(three circles of influence are detected)
        {
            addx = addx * -1;
            addy = addy * -1;
        }

        Vel[0] = addx;
        Vel[1] = addy;
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
        int dest = 0;
        int dist = 0;

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
                addy = ((2 * Math.sqrt(1875)) * -1) * Math.sin(Math.PI /3);
                addx = Math.sqrt(1875) * -1;
                dest = arr6[w];
                dist = arr8[w];
                break;
            }
            if(arr4[w] == EntryCompareValue)
            {
                addx = Math.sqrt(1875) * -1;
                addy = (2 * Math.sqrt(1875)) * Math.sin(Math.PI /3);
                dest = arr5[w];
                dist = arr7[w];
                break;
            }
            if(arr5[w] == EntryCompareValue)
            {
                addx = Math.sqrt(1875);
                addy = ((2 * Math.sqrt(1875)) * -1)  * Math.sin(Math.PI /3);
                dest = arr4[w];
                dist = arr7[w];
                break;
            }
            if(arr6[w] == EntryCompareValue)
            {
                addy = (2 * Math.sqrt(1875)) * Math.sin(Math.PI/3);
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
