package org.example.sep2_group46;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class RayEntry {
    private double[][] rayEntry; // double array to store the location of the ray entry points

    public RayEntry(double[][] rayEntry)
    {
        this.rayEntry = rayEntry;
    }

    public void placeRayEntry(Pane Board, int placement)
    {
        Circle entry = createEntry();
        entry.setLayoutX(rayEntry[placement][0]);
        entry.setLayoutY(rayEntry[placement][1]);
        Board.getChildren().add(entry);
    }

    private Circle createEntry()
    {
        Circle entry = new Circle();
        entry.setRadius(4.0f);
        entry.setFill(Color.WHITE);
        return entry;
    }

}
