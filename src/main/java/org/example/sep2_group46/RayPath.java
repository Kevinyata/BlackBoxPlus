package org.example.sep2_group46;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Sphere;

import java.util.ArrayList;

public class RayPath {
    private final ArrayList<Sphere> Molecule;

    private final ArrayList<Circle> COIs;
    private double[][] rayEntry; // double array to store the location of the ray entry points


    public RayPath(double[][] rayEntry, ArrayList<Sphere> Molecule, ArrayList<Circle> COIs)
    {
        this.COIs = COIs;
        this.Molecule = Molecule;
        this.rayEntry =  rayEntry;
    }

    public void createRayPath(Pane Board, int EntryNum, Circle[] entries)
    {
        Line Ray = new Line();
        Ray.setStrokeWidth(2);
        Ray.setStroke(Color.WHITE);
        Ray.setFill(Color.WHITE);
        double x = rayEntry[EntryNum][0];
        double y = rayEntry[EntryNum][1];
        Ray.setStartX(x);
        Ray.setStartY(y);

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


        boolean flag = false;
        int m = 0;
        int tolerance =  10;
        while(m < dist*2) {
            x += addx/2;
            y += addy/2;

            for (int p = 0; p < 6; p++) {

                if (Math.abs(Molecule.get(p).getTranslateX() - Ray.getEndX()) <= tolerance && Math.abs(Ray.getEndY() - Molecule.get(p).getTranslateY()) <= tolerance) {
                    entries[EntryNum].setFill(Color.RED);
                    flag = true;
                    break;
                }

            }

            if (flag) {
                break;
            }
            Ray.setEndX(x);
            Ray.setEndY(y);
            m++;
        }

        if(!flag) {
            entries[dest - 1].setFill(Color.LIMEGREEN);
            entries[EntryNum].setFill(Color.LIMEGREEN);
            entries[dest-1].setMouseTransparent(true);
        }
        //Ray.setVisible(false);
        Board.getChildren().add(Ray);
    }


    public void deflectRay(Pane Board, int angle)
    {

    }
}
