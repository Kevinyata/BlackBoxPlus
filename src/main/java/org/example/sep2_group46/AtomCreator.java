package org.example.sep2_group46;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;
import java.util.Arrays;

public class AtomCreator {

    //Atoms
    private final Circle[] CirclesOfInfluence;
    private final Sphere[] Molecule;

    //array of x and y coordinates
    private final double[][] xyLocation;

    private final int[][] COIHexagonIndex = new int[6][6]; //Stores hexagons locations that every atom's circle of influence touches

    //Stores the hexagons locations of the left edge of the board
    private final int[] LeftEdgeLocations = new int[]{1, 6, 12, 19, 27, 36, 44, 51, 57};

    //Stores the hexagons locations of the right edge of the board
    private final int[] RightEdgeLocations = new int[]{5, 11, 18, 26, 35, 43, 50, 56, 61};

    public AtomCreator(double[][] xyLocation)
    {
        this.CirclesOfInfluence = new Circle[6];
        this.Molecule = new Sphere[6];
        this.xyLocation = xyLocation;
    }

    public void createAtoms(Pane Board, int[] RandomXY)
    {
        //Initialises array with 0s
        for (int i = 0; i < 6; i++) {
            Arrays.fill(COIHexagonIndex[i], 0);
        }

        //Creates atoms at 6 random hexagon locations
        for(int i = 0; i < 6; i++) {

            //calculates the hexagons that an atom's circle of influence touches
            CalculateHexagonsCOIs(RandomXY[i], i);

            Circle COI = createCircleOfInfluence();
            COI.setCenterX(xyLocation[0][RandomXY[i]]);
            COI.setCenterY(xyLocation[1][RandomXY[i]]);

            CirclesOfInfluence[i] = COI;
            Board.getChildren().add(COI);

            Sphere molecule = createMolecule();
            molecule.setTranslateX(xyLocation[0][RandomXY[i]]);
            molecule.setTranslateY(xyLocation[1][RandomXY[i]]);

            Molecule[i] = molecule;
            Board.getChildren().add(molecule);

            //Sets the atom invisible
            COI.setVisible(false);
            molecule.setVisible(false);
        }
    }

    public void CalculateHexagonsCOIs(int hexagonLocation, int atomNumber)
    {
        //[atomNumber][0] -> hexagon left to hexagon atom is in
        //[atomNumber][1] -> hexagon right to hexagon atom is in
        //[atomNumber][2] -> hexagon bottom left to hexagon atom is in
        //[atomNumber][3] -> hexagon bottom right to hexagon atom is in
        //[atomNumber][4] -> hexagon top left to hexagon atom is in
        //[atomNumber][5] -> hexagon top right to hexagon atom is in

        //If an entry is 0 it means that that particular atom location's circle of influence doesn't touch that position adjacent to it
        //So if entry 5 of atom 1 is 0 it means that atom's circle of influence touches no hexagon top right to the  atom location

        if(hexagonLocation == 1)
        {
            COIHexagonIndex[atomNumber][1] = hexagonLocation+1;
            COIHexagonIndex[atomNumber][2] = 6;
            COIHexagonIndex[atomNumber][3] = 7;
        }
        else if(hexagonLocation == 5)
        {
            COIHexagonIndex[atomNumber][0] = hexagonLocation-1;
            COIHexagonIndex[atomNumber][2] = 10;
            COIHexagonIndex[atomNumber][3] = 11;
        }
        else if(hexagonLocation == 57){
            COIHexagonIndex[atomNumber][1] = hexagonLocation+1;
            COIHexagonIndex[atomNumber][4] = 51;
            COIHexagonIndex[atomNumber][5] = 52;
        }
        else if(hexagonLocation == 61){
            COIHexagonIndex[atomNumber][0] = hexagonLocation-1;
            COIHexagonIndex[atomNumber][4] = 55;
            COIHexagonIndex[atomNumber][5] = 56;
        }
        else if(hexagonLocation == 27) {
            COIHexagonIndex[atomNumber][1] = hexagonLocation+1;
            COIHexagonIndex[atomNumber][5] = 19;
            COIHexagonIndex[atomNumber][3] = 36;
        }
        else if(hexagonLocation == 35) {
            COIHexagonIndex[atomNumber][0] = hexagonLocation-1;
            COIHexagonIndex[atomNumber][4] = 26;
            COIHexagonIndex[atomNumber][2] = 43;
        }
        else if(hexagonLocation > 1 && hexagonLocation < 5) {
            COIHexagonIndex[atomNumber][0] = hexagonLocation-1;
            COIHexagonIndex[atomNumber][1] = hexagonLocation+1;
            COIHexagonIndex[atomNumber][2] = hexagonLocation+5;
            COIHexagonIndex[atomNumber][3] = COIHexagonIndex[atomNumber][2] + 1;
        }
        else if(hexagonLocation > 57 && hexagonLocation < 61) {
            COIHexagonIndex[atomNumber][0] = hexagonLocation-1;
            COIHexagonIndex[atomNumber][1] = hexagonLocation+1;
            COIHexagonIndex[atomNumber][4] = hexagonLocation-6;
            COIHexagonIndex[atomNumber][5] =  COIHexagonIndex[atomNumber][4] + 1;
        }
        else if(hexagonLocation == 12 || hexagonLocation == 6 || hexagonLocation == 19) {
            int index = Arrays.binarySearch(LeftEdgeLocations, hexagonLocation);
            COIHexagonIndex[atomNumber][1] = hexagonLocation+1;
            COIHexagonIndex[atomNumber][2] = LeftEdgeLocations[index+1];
            COIHexagonIndex[atomNumber][3] = COIHexagonIndex[atomNumber][2] + 1;
            COIHexagonIndex[atomNumber][5] = LeftEdgeLocations[index-1];
        }
        else if(hexagonLocation == 11 || hexagonLocation == 18 || hexagonLocation == 26)
        {
            int index = Arrays.binarySearch(RightEdgeLocations, hexagonLocation);
            COIHexagonIndex[atomNumber][0] = hexagonLocation-1;
            COIHexagonIndex[atomNumber][2] = RightEdgeLocations[index+1] - 1;
            COIHexagonIndex[atomNumber][3] = COIHexagonIndex[atomNumber][2]+1;
            COIHexagonIndex[atomNumber][4] = RightEdgeLocations[index-1];
        }
        else if(hexagonLocation == 36 || hexagonLocation ==  44 || hexagonLocation == 51)
        {
            int index = Arrays.binarySearch(LeftEdgeLocations, hexagonLocation);
            COIHexagonIndex[atomNumber][1] = hexagonLocation+1;
            COIHexagonIndex[atomNumber][3] = LeftEdgeLocations[index+1];
            COIHexagonIndex[atomNumber][4] = LeftEdgeLocations[index-1];
            COIHexagonIndex[atomNumber][5] = COIHexagonIndex[atomNumber][4] + 1;
        }
        else if(hexagonLocation == 43 || hexagonLocation == 50 || hexagonLocation == 56)
        {
            int index = Arrays.binarySearch(RightEdgeLocations, hexagonLocation);
            COIHexagonIndex[atomNumber][0] = hexagonLocation-1;
            COIHexagonIndex[atomNumber][2] = RightEdgeLocations[index+1];
            COIHexagonIndex[atomNumber][4] = RightEdgeLocations[index-1] - 1;
            COIHexagonIndex[atomNumber][5] = COIHexagonIndex[atomNumber][4] + 1;
        }
        else //For hexagons location that aren't at the edge of the board
        {
            COIHexagonIndex[atomNumber][0] = hexagonLocation-1;
            COIHexagonIndex[atomNumber][1] = hexagonLocation+1;
            for(int i = 1; i < 8; i++)
            {
                if(hexagonLocation > LeftEdgeLocations[i] && hexagonLocation < RightEdgeLocations[i])
                {
                    int numberOfHexagonsOnCurrentRow = (RightEdgeLocations[i] - LeftEdgeLocations[i]) + 1;
                    int numberOfHexagonsOnPreviousRow = (RightEdgeLocations[i-1] - LeftEdgeLocations[i-1]) + 1;
                    int numberOfHexagonsOnNextRow = (RightEdgeLocations[i+1] - LeftEdgeLocations[i+1]) + 1;

                    if(hexagonLocation < 27 && hexagonLocation > 7)
                    {
                        COIHexagonIndex[atomNumber][2] = hexagonLocation + numberOfHexagonsOnCurrentRow;
                        COIHexagonIndex[atomNumber][3] = COIHexagonIndex[atomNumber][2] + 1;
                        COIHexagonIndex[atomNumber][4] = hexagonLocation - numberOfHexagonsOnCurrentRow;
                        COIHexagonIndex[atomNumber][5] = COIHexagonIndex[atomNumber][4] + 1;
                    }
                    else if(hexagonLocation > 27 && hexagonLocation < 35)
                    {
                        COIHexagonIndex[atomNumber][2] = hexagonLocation + 8;
                        COIHexagonIndex[atomNumber][3] = COIHexagonIndex[atomNumber][2] + 1;
                        COIHexagonIndex[atomNumber][4] = hexagonLocation - 9;
                        COIHexagonIndex[atomNumber][5] = COIHexagonIndex[atomNumber][4] + 1;
                    }
                    else
                    {
                        COIHexagonIndex[atomNumber][2] = hexagonLocation + numberOfHexagonsOnNextRow;
                        COIHexagonIndex[atomNumber][3] = COIHexagonIndex[atomNumber][2] + 1;
                        COIHexagonIndex[atomNumber][4] = hexagonLocation - numberOfHexagonsOnPreviousRow;
                        COIHexagonIndex[atomNumber][5] = COIHexagonIndex[atomNumber][4] + 1;
                    }
                    break;
                }
            }
        }

        //For debugging
        System.out.print("An atom located at Hexagonal Location " + hexagonLocation + " has its circle of influence touch hexagon locations:");
        for(int i = 0; i < 6; i++) {
            System.out.print(" " + COIHexagonIndex[atomNumber][i] + ",");
        }
        System.out.print("\n");
    }

    public Sphere[] getMolecule() {
        return Molecule;
    }

    public int[][] getCOIHexagonIndex() {
        return COIHexagonIndex;
    }

    public double[][] getXyLocation() {
        return xyLocation;
    }

    public Circle createCircleOfInfluence()
    {
        Circle COI = new Circle();
        COI.setRadius(85.0f); // setting the circle of influence's size
        COI.setFill(null); // setting the inside of the circle of influence to empty, so it is just an outline
        COI.setStroke(Color.WHITE); // setting the outline of the circle of influence to white
        return COI;
    }

    public Sphere createMolecule()
    {
        Sphere molecule = new Sphere();
        molecule.setRadius(30.0f); // setting the atom's size
        PhongMaterial material = new PhongMaterial(); // used for setting the atoms colour
        material.setDiffuseColor(Color.GREY);
        material.setSpecularColor(Color.BLACK);
        molecule.setMaterial(material); // setting the colours to the atoms
        return molecule;
    }

    public void showAtoms()
    {
        for(int i = 0; i < 6; i++)
        {
            CirclesOfInfluence[i].setVisible(true);
            Molecule[i].setVisible(true);
        }
    }

    public void hideAtoms()
    {
        for(int i = 0; i < 6; i++)
        {
            CirclesOfInfluence[i].setVisible(false);
            Molecule[i].setVisible(false);
        }
    }


}
