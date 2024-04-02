package org.example.sep2_group46;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class BlackBoxPlus extends Application {

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        primaryStage.setTitle("Hexagonal Black Box+");

        HexagonalBoardSetup GameBoard  = new HexagonalBoardSetup();
        GameBoard.createHexgonalBoard(root);

        AtomCreator GameAtoms = new AtomCreator(GameBoard.getXyLocation());
        GameAtoms.createAtoms(root, GameBoard.RandomHexagonCoordinates());

        RayEntry GameEntries = new RayEntry(GameBoard.getRayEntry());
        GameEntries.placeRayEntries(root);
        GameEntries.EntryRayBuilder(root, GameAtoms.getMolecule(), GameAtoms.getCirclesofInfluence());

        createButton(root, GameAtoms);

        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    private void createButton(Pane Board, AtomCreator atoms)
    {
        Button b = new Button();
        b.setText("Show");
        b.setLayoutX(1300);
        b.setLayoutY(680);
        b.setPrefSize(150, 50);
        b.setOnAction(actionEvent -> {
            if(b.getText().equals("Show"))
            {
                //Shows atoms
                atoms.ShowAtoms();
                b.setText("Hide"); //Switches functionality to hide
            }
            else if(b.getText().equals("Hide")) {
                //Hides atoms
                atoms.HideAtoms();
                b.setText("Show"); //Switches functionality to show
            }
        });
        Board.getChildren().add(b);
    }

    public static void main(String[] args) {
        launch(args);
    }
}