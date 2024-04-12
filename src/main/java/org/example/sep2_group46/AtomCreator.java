package org.example.sep2_group46;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;
import java.util.Arrays;

public class AtomCreator {

    private final Circle[] CirclesofInfluence;
    private final Sphere[] Molecule;
    private final double[][] xyLocation;
    private final int[][] COIHexagonIndx = new int[6][6];
    private final int[] LeftEdgeLocations = new int[]{1, 6, 12, 19, 27, 36, 44, 51, 57};
    private final int[] RightEdgeLocations = new int[]{5, 11, 18, 26, 35, 43, 50, 56, 61};

    public AtomCreator(double[][] xyLocation)
    {
        this.CirclesofInfluence = new Circle[6];
        this.Molecule = new Sphere[6];
        this.xyLocation = xyLocation;
    }

    public void createAtoms(Pane Board, int[] RandomXY)
    {
        for (int i = 0; i < 6; i++) {
            Arrays.fill(COIHexagonIndx[i], 0);
        }

        for(int i = 0; i < 6; i++) {
            CalculateHexagonsCOIs(RandomXY[i], i);
            Circle COI = createCircleofInfluence();
            COI.setCenterX(xyLocation[0][RandomXY[i]]);
            COI.setCenterY(xyLocation[1][RandomXY[i]]);

            CirclesofInfluence[i] = COI;
            Board.getChildren().add(COI);

            Sphere molecule = createMolecule();
            molecule.setTranslateX(xyLocation[0][RandomXY[i]]);
            molecule.setTranslateY(xyLocation[1][RandomXY[i]]);

            Molecule[i] = molecule;
            Board.getChildren().add(molecule);

            COI.setVisible(false);
            molecule.setVisible(false);
        }
    }

    public void CalculateHexagonsCOIs(int HexagonLocation, int AtomNumber)
    {
        COIHexagonIndx[AtomNumber][0] = HexagonLocation+1;
        COIHexagonIndx[AtomNumber][1] = HexagonLocation-1;

        if(HexagonLocation == 1)
        {
            COIHexagonIndx[AtomNumber][0] = HexagonLocation+1;
            COIHexagonIndx[AtomNumber][4] = 6;
            COIHexagonIndx[AtomNumber][5] = 7;
        }
        else if(HexagonLocation == 5)
        {
            COIHexagonIndx[AtomNumber][1] = HexagonLocation-1;
            COIHexagonIndx[AtomNumber][4] = 10;
            COIHexagonIndx[AtomNumber][5] = 11;
        }
        else if(HexagonLocation == 57){
            COIHexagonIndx[AtomNumber][0] = HexagonLocation+1;
            COIHexagonIndx[AtomNumber][2] = 51;
            COIHexagonIndx[AtomNumber][3] = 52;
        }
        else if(HexagonLocation == 61){
            COIHexagonIndx[AtomNumber][1] = HexagonLocation-1;
            COIHexagonIndx[AtomNumber][2] = 55;
            COIHexagonIndx[AtomNumber][3] = 56;
        }
        else if(HexagonLocation == 27) {
            COIHexagonIndx[AtomNumber][0] = HexagonLocation+1;
            COIHexagonIndx[AtomNumber][3] = 19;
            COIHexagonIndx[AtomNumber][5] = 36;
        }
        else if(HexagonLocation == 35) {
            COIHexagonIndx[AtomNumber][1] = HexagonLocation-1;
            COIHexagonIndx[AtomNumber][3] = 26;
            COIHexagonIndx[AtomNumber][5] = 43;
        }
        else if(HexagonLocation > 1 && HexagonLocation < 5) {
            COIHexagonIndx[AtomNumber][0] = HexagonLocation+1;
            COIHexagonIndx[AtomNumber][1] = HexagonLocation-1;
            COIHexagonIndx[AtomNumber][4] = HexagonLocation+5;
            COIHexagonIndx[AtomNumber][5] = HexagonLocation+6;
        }
        else if(HexagonLocation > 57 && HexagonLocation < 61) {
            COIHexagonIndx[AtomNumber][0] = HexagonLocation+1;
            COIHexagonIndx[AtomNumber][1] = HexagonLocation-1;
            COIHexagonIndx[AtomNumber][2] = HexagonLocation-5;
            COIHexagonIndx[AtomNumber][3] = HexagonLocation-6;
        }
        else if(HexagonLocation == 12 || HexagonLocation == 6 || HexagonLocation == 19) {
            int index = Arrays.binarySearch(LeftEdgeLocations, HexagonLocation);
            COIHexagonIndx[AtomNumber][0] = HexagonLocation+1;
            COIHexagonIndx[AtomNumber][3] = LeftEdgeLocations[index-1];
            COIHexagonIndx[AtomNumber][4] = LeftEdgeLocations[index+1];
            COIHexagonIndx[AtomNumber][5] = LeftEdgeLocations[index+1] + 1;
        }
        else if(HexagonLocation == 11 || HexagonLocation == 18 || HexagonLocation == 26)
        {
            int index = Arrays.binarySearch(RightEdgeLocations, HexagonLocation);
            COIHexagonIndx[AtomNumber][1] = HexagonLocation-1;
            COIHexagonIndx[AtomNumber][2] = RightEdgeLocations[index-1];
            COIHexagonIndx[AtomNumber][4] = RightEdgeLocations[index+1];
            COIHexagonIndx[AtomNumber][5] = RightEdgeLocations[index+1] - 1;
        }
        else if(HexagonLocation == 36 || HexagonLocation ==  44 || HexagonLocation == 51)
        {
            int index = Arrays.binarySearch(LeftEdgeLocations, HexagonLocation);
            COIHexagonIndx[AtomNumber][0] = HexagonLocation+1;
            COIHexagonIndx[AtomNumber][5] = LeftEdgeLocations[index+1];
            COIHexagonIndx[AtomNumber][2] = LeftEdgeLocations[index-1];
            COIHexagonIndx[AtomNumber][3] = LeftEdgeLocations[index-1] + 1;
        }
        else if(HexagonLocation == 43 || HexagonLocation == 50 || HexagonLocation == 56)
        {
            int index = Arrays.binarySearch(RightEdgeLocations, HexagonLocation);
            COIHexagonIndx[AtomNumber][1] = HexagonLocation-1;
            COIHexagonIndx[AtomNumber][4] = RightEdgeLocations[index+1];
            COIHexagonIndx[AtomNumber][2] = RightEdgeLocations[index-1];
            COIHexagonIndx[AtomNumber][3] = RightEdgeLocations[index-1] - 1;
        }
        else
        {
            COIHexagonIndx[AtomNumber][0] = HexagonLocation+1;
            COIHexagonIndx[AtomNumber][1] = HexagonLocation-1;
            for(int i = 1; i < 8; i++)
            {
                if(HexagonLocation > LeftEdgeLocations[i] && HexagonLocation < RightEdgeLocations[i])
                {
                    int numHexagonsOnCurrRow = RightEdgeLocations[i] - LeftEdgeLocations[i];
                    int numHexagonsOnPrevRow = RightEdgeLocations[i-1] - LeftEdgeLocations[i-1];
                    int numHexagonsOnNextRow = RightEdgeLocations[i+1] - LeftEdgeLocations[i+1];

                    if(numHexagonsOnPrevRow > numHexagonsOnCurrRow)
                    {
                        COIHexagonIndx[AtomNumber][2] = (HexagonLocation - numHexagonsOnCurrRow) - 1;
                        COIHexagonIndx[AtomNumber][3] = COIHexagonIndx[AtomNumber][2] + 1;
                    }
                    else if(numHexagonsOnPrevRow < numHexagonsOnCurrRow)
                    {
                        COIHexagonIndx[AtomNumber][2] = (HexagonLocation - numHexagonsOnCurrRow);
                        COIHexagonIndx[AtomNumber][3] = COIHexagonIndx[AtomNumber][2] + 1;
                    }

                    if(numHexagonsOnNextRow < numHexagonsOnCurrRow) {
                        COIHexagonIndx[AtomNumber][4] = (HexagonLocation + numHexagonsOnCurrRow);
                        COIHexagonIndx[AtomNumber][5] = COIHexagonIndx[AtomNumber][4] + 1;
                    }
                    else if(numHexagonsOnNextRow > numHexagonsOnCurrRow)
                    {
                        COIHexagonIndx[AtomNumber][4] = (HexagonLocation + numHexagonsOnNextRow);
                        COIHexagonIndx[AtomNumber][5] = COIHexagonIndx[AtomNumber][4] + 1;
                    }
                    break;
                }
            }
            for(int i = 0; i < 6; i++) {
                System.out.print("Hexagon " + HexagonLocation + ":" + COIHexagonIndx[AtomNumber][i] + " ");
                System.out.print("\n");
            }
        }
    }

    public Circle[] getCirclesofInfluence() {
        return CirclesofInfluence;
    }

    public Sphere[] getMolecule() {
        return Molecule;
    }

    public int[][] getCOIHexagonIndx() {
        return COIHexagonIndx;
    }

    public double[][] getXyLocation() {
        return xyLocation;
    }

    public Circle createCircleofInfluence()
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

    public void ShowAtoms()
    {
        for(int i = 0; i < 6; i++)
        {
            CirclesofInfluence[i].setVisible(true);
            Molecule[i].setVisible(true);
        }
    }

    public void HideAtoms()
    {
        for(int i = 0; i < 6; i++)
        {
            CirclesofInfluence[i].setVisible(false);
            Molecule[i].setVisible(false);
        }
    }


}
