package org.example.sep2_group46;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;

import java.util.ArrayList;

public class RayEntry {
    private final double[][] rayEntry; // double array to store the location of the ray entry points
    private Circle[] entries = new Circle[54];

    public RayEntry(double[][] rayEntry)
    {
        this.rayEntry = rayEntry;
    }

    public void placeRayEntries(Pane Board)
    {
        for(int placement = 0; placement < 54; placement++) {
            Circle entry = createEntry();
            entry.setLayoutX(rayEntry[placement][0]);
            entry.setLayoutY(rayEntry[placement][1]);
            entries[placement] = entry;
            Board.getChildren().add(entry);
        }
    }

    public void EntryRayBuilder(Pane Board, ArrayList<Sphere> Molecule, ArrayList<Circle> CirclesofInfluence)
    {
        RayPath rays = new RayPath(rayEntry, Molecule, CirclesofInfluence);
        for(int entryNum = 0; entryNum < 54; entryNum++) {
            int finalEntryNum = entryNum;
            entries[entryNum].setOnMouseClicked(mouseEvent -> rays.createRayPath(Board, finalEntryNum, entries));
        }
    }


    private Circle createEntry()
    {
        Circle entry = new Circle();
        entry.setRadius(4.0f);
        entry.setFill(Color.WHITE);
        return entry;
    }

}
