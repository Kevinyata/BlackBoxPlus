package org.example.sep2_group46;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * The RayEntry class creates all the ray entries
 * @author David Ibilola, Kevin Yatagampitiya
 * Date: May 2024
 */
public class RayEntry {
    private final double[][] rayEntry; // double array to store the location of the ray entry points
    private final Circle[] entries = new Circle[54]; //array for all entries on the board

    /**
     * Stores a ray entry in the rayEntry double array
     * @param rayEntry double array that stores the ray entry's number, and it's X and Y location
     */
    public RayEntry(double[][] rayEntry)
    {
        this.rayEntry = rayEntry;
    }

    /**
     * Creates and adds the ray entries to the Board
     * @param Board used to update the display
     */
    public void placeRayEntries(Pane Board)
    {
        //Places all entries on the board
        for(int placement = 0; placement < 54; placement++) {
            Circle entry = createEntry();
            entry.setLayoutX(rayEntry[placement][0]);
            entry.setLayoutY(rayEntry[placement][1]);
            entries[placement] = entry;
            Board.getChildren().add(entry);
        }
    }

    /**
     * Allows for the ray entries to be clickable
     * @param Board used to update the display
     * @param atomCreator used in the createRayPath function
     * @return the ray entry which is used in the calculations for the score
     */
    public RayPath entryRayBuilder(Pane Board, AtomCreator atomCreator)
    {
        RayPath rays = new RayPath(rayEntry);
        //Enables ray paths for all entries on the board
        for(int entryNum = 0; entryNum < 54; entryNum++) {
            int finalEntryNum = entryNum;
            entries[entryNum].setOnMouseClicked(mouseEvent -> rays.createRayPath(Board, finalEntryNum, entries, atomCreator));
        }
        return rays;
    }

    /**
     * Creates a ray entry
     * @return the ray entry
     */
    private Circle createEntry()
    {
        Circle entry = new Circle();
        entry.setRadius(4.0f);
        entry.setFill(Color.YELLOW);
        return entry;
    }

}