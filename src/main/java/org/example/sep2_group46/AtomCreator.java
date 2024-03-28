package org.example.sep2_group46;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;

import java.util.ArrayList;
import java.util.Arrays;

public class AtomCreator {

    private final ArrayList<Circle> CirclesofInfluence;
    private final ArrayList<Sphere> Molecule;
    private final double[][] xyLocation;


    public AtomCreator(double[][] xyLocation)
    {
        this.CirclesofInfluence = new ArrayList<>();
        this.Molecule = new ArrayList<>();
        this.xyLocation = xyLocation;
    }

    public void createAtoms(Pane Board, int[] RandomXY)
    {
        for(int i =0; i < 6; i++) {
            Circle COI = createCircleofInfluence();
            COI.setCenterX(xyLocation[0][RandomXY[i]]);
            COI.setCenterY(xyLocation[1][RandomXY[i]]);

            CirclesofInfluence.add(COI);
            Board.getChildren().add(COI);

            Sphere molecule = createMolecule();
            molecule.setTranslateX(xyLocation[0][RandomXY[i]]);
            molecule.setTranslateY(xyLocation[1][RandomXY[i]]);

            Molecule.add(molecule);
            Board.getChildren().add(molecule);

            COI.setVisible(false);
            molecule.setVisible(false);
        }
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
            CirclesofInfluence.get(i).setVisible(true);
            Molecule.get(i).setVisible(true);
        }
    }

    public void HideAtoms()
    {
        for(int i = 0; i < 6; i++)
        {
            CirclesofInfluence.get(i).setVisible(false);
            Molecule.get(i).setVisible(false);
        }
    }


}
