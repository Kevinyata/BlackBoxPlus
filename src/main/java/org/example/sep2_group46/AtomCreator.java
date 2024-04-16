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

    public void CalculateHexagonsCOIs(int HexagonLocation, int AtomNumber)
    {
        //0 -> left
        //1 -> right
        //2 -> bottom left
        //3 -> bottom right
        //4 - top left
        //5 -> top right

        //If an entry is 0 it means that that particular atom location's circle of influence doesn't touch that position adjacent to it
        //So if entry 5 of atom 1 is empty it means that atom's circle of influence touches no hexagon top right to the  atom location
        
        if(HexagonLocation == 1)
        {
            COIHexagonIndex[AtomNumber][1] = HexagonLocation+1;
            COIHexagonIndex[AtomNumber][2] = 6;
            COIHexagonIndex[AtomNumber][3] = 7;
        }
        else if(HexagonLocation == 5)
        {
            COIHexagonIndex[AtomNumber][0] = HexagonLocation-1;
            COIHexagonIndex[AtomNumber][2] = 10;
            COIHexagonIndex[AtomNumber][3] = 11;
        }
        else if(HexagonLocation == 57){
            COIHexagonIndex[AtomNumber][1] = HexagonLocation+1;
            COIHexagonIndex[AtomNumber][4] = 51;
            COIHexagonIndex[AtomNumber][5] = 52;
        }
        else if(HexagonLocation == 61){
            COIHexagonIndex[AtomNumber][0] = HexagonLocation-1;
            COIHexagonIndex[AtomNumber][4] = 55;
            COIHexagonIndex[AtomNumber][5] = 56;
        }
        else if(HexagonLocation == 27) {
            COIHexagonIndex[AtomNumber][1] = HexagonLocation+1;
            COIHexagonIndex[AtomNumber][5] = 19;
            COIHexagonIndex[AtomNumber][3] = 36;
        }
        else if(HexagonLocation == 35) {
            COIHexagonIndex[AtomNumber][0] = HexagonLocation-1;
            COIHexagonIndex[AtomNumber][4] = 26;
            COIHexagonIndex[AtomNumber][2] = 43;
        }
        else if(HexagonLocation > 1 && HexagonLocation < 5) {
            COIHexagonIndex[AtomNumber][0] = HexagonLocation-1;
            COIHexagonIndex[AtomNumber][1] = HexagonLocation+1;
            COIHexagonIndex[AtomNumber][2] = HexagonLocation+5;
            COIHexagonIndex[AtomNumber][3] = HexagonLocation+6;
        }
        else if(HexagonLocation > 57 && HexagonLocation < 61) {
            COIHexagonIndex[AtomNumber][0] = HexagonLocation-1;
            COIHexagonIndex[AtomNumber][1] = HexagonLocation+1;
            COIHexagonIndex[AtomNumber][4] = HexagonLocation-5;
            COIHexagonIndex[AtomNumber][5] = HexagonLocation-6;
        }
        else if(HexagonLocation == 12 || HexagonLocation == 6 || HexagonLocation == 19) {
            int index = Arrays.binarySearch(LeftEdgeLocations, HexagonLocation);
            COIHexagonIndex[AtomNumber][1] = HexagonLocation+1;
            COIHexagonIndex[AtomNumber][5] = LeftEdgeLocations[index-1];
            COIHexagonIndex[AtomNumber][2] = LeftEdgeLocations[index+1];
            COIHexagonIndex[AtomNumber][3] = LeftEdgeLocations[index+1] + 1;
        }
        else if(HexagonLocation == 11 || HexagonLocation == 18 || HexagonLocation == 26)
        {
            int index = Arrays.binarySearch(RightEdgeLocations, HexagonLocation);
            COIHexagonIndex[AtomNumber][0] = HexagonLocation-1;
            COIHexagonIndex[AtomNumber][4] = RightEdgeLocations[index-1];
            COIHexagonIndex[AtomNumber][2] = RightEdgeLocations[index+1];
            COIHexagonIndex[AtomNumber][3] = RightEdgeLocations[index+1] - 1;
        }
        else if(HexagonLocation == 36 || HexagonLocation ==  44 || HexagonLocation == 51)
        {
            int index = Arrays.binarySearch(LeftEdgeLocations, HexagonLocation);
            COIHexagonIndex[AtomNumber][1] = HexagonLocation+1;
            COIHexagonIndex[AtomNumber][3] = LeftEdgeLocations[index+1];
            COIHexagonIndex[AtomNumber][4] = LeftEdgeLocations[index-1];
            COIHexagonIndex[AtomNumber][5] = LeftEdgeLocations[index-1] + 1;
        }
        else if(HexagonLocation == 43 || HexagonLocation == 50 || HexagonLocation == 56)
        {
            int index = Arrays.binarySearch(RightEdgeLocations, HexagonLocation);
            COIHexagonIndex[AtomNumber][0] = HexagonLocation-1;
            COIHexagonIndex[AtomNumber][2] = RightEdgeLocations[index+1];
            COIHexagonIndex[AtomNumber][4] = RightEdgeLocations[index-1];
            COIHexagonIndex[AtomNumber][5] = RightEdgeLocations[index-1] - 1;
        }
        else
        {
            COIHexagonIndex[AtomNumber][0] = HexagonLocation-1;
            COIHexagonIndex[AtomNumber][1] = HexagonLocation+1;
            for(int i = 1; i < 8; i++)
            {
                if(HexagonLocation > LeftEdgeLocations[i] && HexagonLocation < RightEdgeLocations[i])
                {
                    int numHexagonsOnCurrRow = RightEdgeLocations[i] - LeftEdgeLocations[i];
                    int numHexagonsOnPrevRow = RightEdgeLocations[i-1] - LeftEdgeLocations[i-1];
                    int numHexagonsOnNextRow = RightEdgeLocations[i+1] - LeftEdgeLocations[i+1];

                    if(numHexagonsOnPrevRow > numHexagonsOnCurrRow)
                    {
                        COIHexagonIndex[AtomNumber][2] = (HexagonLocation - numHexagonsOnCurrRow) - 1;
                        COIHexagonIndex[AtomNumber][3] = COIHexagonIndex[AtomNumber][2] + 1;
                    }
                    else if(numHexagonsOnPrevRow < numHexagonsOnCurrRow)
                    {
                        COIHexagonIndex[AtomNumber][2] = (HexagonLocation - numHexagonsOnCurrRow);
                        COIHexagonIndex[AtomNumber][3] = COIHexagonIndex[AtomNumber][2] + 1;
                    }

                    if(numHexagonsOnNextRow < numHexagonsOnCurrRow) {
                        COIHexagonIndex[AtomNumber][4] = (HexagonLocation + numHexagonsOnNextRow);
                        COIHexagonIndex[AtomNumber][5] = COIHexagonIndex[AtomNumber][4] + 1;
                    }
                    else if(numHexagonsOnNextRow > numHexagonsOnCurrRow)
                    {
                        COIHexagonIndex[AtomNumber][4] = (HexagonLocation + numHexagonsOnCurrRow);
                        COIHexagonIndex[AtomNumber][5] = COIHexagonIndex[AtomNumber][4] + 1;
                    }
                    break;
                }
            }
        }
        //For debugging
        System.out.print("An atom located at Hexagonal Location " + HexagonLocation + " has its circle of influence touch hexagon locations:");
        for(int i = 0; i < 6; i++) {
            System.out.print(" " + COIHexagonIndex[AtomNumber][i] + ",");
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
