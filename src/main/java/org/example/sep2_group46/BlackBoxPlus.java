package org.example.sep2_group46;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import java.lang.Math;


public class BlackBoxPlus extends Application {

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        primaryStage.setTitle("Hexagonal Black Box+");

        HexagonalBoardSetup GameBoard  = new HexagonalBoardSetup();
        GameBoard.createHexgonalBoard(root);

        int[] randNum = GameBoard.RandomHexagonCoordinates();
        double[][] xyCoordinates = GameBoard.getXyLocation();

        AtomCreator GameAtoms = new AtomCreator(xyCoordinates);
        GameAtoms.createAtoms(root, randNum);

        RayEntry GameEntries = new RayEntry(GameBoard.getRayEntry());
        for(int i = 0; i < 54; i++)
        {
            GameEntries.placeRayEntry(root, i);
        }

//        for(int i = 0; i < 54; i++)
//        {
//            int finalI = i;
//            entries[i].setOnMouseClicked(mouseEvent -> {
//                //Line Ray = RayPath(atoms, COIs, rayEntry, finalI , entries);
//                //root.getChildren().add(Ray);
//            });
//            root.getChildren().add(entries[i]);
//        }

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

    private Line RayPath(Sphere[] s, Circle[] c, double[][] RayEntry, int i, Circle[] entries) {
        Line Ray = new Line();
        Ray.setStrokeWidth(2);
        Ray.setStroke(Color.WHITE);
        Ray.setFill(Color.WHITE);
        double l = RayEntry[i][0];
        double y = RayEntry[i][1];
        Ray.setStartX(l);
        Ray.setStartY(y);

        double addx = 1;
        double addy = 0;

        int[] arr1 = new int[]{1, 13, 17,21, 25, 31, 35, 39, 43};
        int[] arr2 = new int[]{12, 16, 20, 24, 29, 33, 37, 41, 52};
        int[] arr3 = new int[]{53, 42, 38, 34, 30, 50, 48, 46, 44};
        int[] arr4 = new int[]{3,5,7,9,11,15,19,23,28};
        int[] arr5 = new int[]{27, 32, 36, 40, 45, 47, 49, 51, 54};
        int[] arr6 = new int[]{2,4,6, 8, 10, 14,18,22,26};
        int[] arr7 = new int[]{5,6,7,8,9,8,7,6,5};
        int[] arr8 = new int[]{9,8,7,6,5,8,7,6,5};
        int x = i+1;
        int dest=0;
        int dist = 0;

        for(int w = 0; w < 9; w++)
        {
            if(arr1[w] == x)
            {
                addx = 88;
                addy = 0;
                dest = arr2[w];
                dist = switch (w) //Determines distance
                {
                    case 0 -> 5;
                    case 1 -> 6;
                    case 2 -> 7;
                    case 3 -> 8;
                    case 4 -> 9;
                    case 5 -> 8;
                    case 6 -> 7;
                    case 7 -> 6;
                    case 8 -> 5;
                    default -> dist;
                };

                break;
            }
            if(arr2[w] == x)
            {
                addy = 0;
                addx = -88;
                dest = arr1[w];
                dist = switch (w) //Determines distance
                {
                    case 0 -> 5;
                    case 1 -> 6;
                    case 2 -> 7;
                    case 3 -> 8;
                    case 4 -> 9;
                    case 5 -> 8;
                    case 6 -> 7;
                    case 7 -> 6;
                    case 8 -> 5;
                    default -> dist;
                };
                break;
            }
            if(arr3[w] == x)
            {
                addy = ((2 * Math.sqrt(1875)) * -1) * Math.sin(Math.PI /3);
                addx = Math.sqrt(1875) * -1;
                dest = arr6[w];
                dist = switch (w) //Determines distance
                {
                    case 0 -> 9;
                    case 1 -> 8;
                    case 2 -> 7;
                    case 3 -> 6;
                    case 4 -> 5;
                    case 5 -> 8;
                    case 6 -> 7;
                    case 7 -> 6;
                    case 8 -> 5;
                    default -> dist;
                };
                break;
            }
            if(arr4[w] == x)
            {
                addx = Math.sqrt(1875) * -1;
                addy = (2 * Math.sqrt(1875)) * Math.sin(Math.PI /3);
                dest = arr5[w];
                dist = switch (w) //Determines distance
                {
                    case 0 -> 5;
                    case 1 -> 6;
                    case 2 -> 7;
                    case 3 -> 8;
                    case 4 -> 9;
                    case 5 -> 8;
                    case 6 -> 7;
                    case 7 -> 6;
                    case 8 -> 5;
                    default -> dist;
                };
                break;
            }
            if(arr5[w] == x)
            {
                addx = Math.sqrt(1875);
                addy = ((2 * Math.sqrt(1875)) * -1)  * Math.sin(Math.PI /3);
                dest = arr4[w];
                dist = switch (w) //Determines distance
                {
                    case 0 -> 5;
                    case 1 -> 6;
                    case 2 -> 7;
                    case 3 -> 8;
                    case 4 -> 9;
                    case 5 -> 8;
                    case 6 -> 7;
                    case 7 -> 6;
                    case 8 -> 5;
                    default -> dist;
                };
                break;
            }
            if(arr6[w] == x)
            {
                addy = (2 * Math.sqrt(1875)) * Math.sin(Math.PI/3);
                addx = Math.sqrt(1875);
                dest = arr3[w];
                dist = switch (w) //Determines distance
                {
                    case 0 -> 9;
                    case 1 -> 8;
                    case 2 -> 7;
                    case 3 -> 6;
                    case 4 -> 5;
                    case 5 -> 8;
                    case 6 -> 7;
                    case 7 -> 6;
                    case 8 -> 5;
                    default -> dist;
                };
                break;
            }
        }


        boolean flag = false;
        int m = 0;
        int tolerance =  10;
        while(m < dist*2) {
            l += addx/2;
            y += addy/2;

            for (int p = 0; p < 6; p++) {

                if (Math.abs(s[p].getTranslateX() - Ray.getEndX()) <= tolerance && Math.abs(Ray.getEndY() - s[p].getTranslateY()) <= tolerance) {
                    entries[i].setFill(Color.RED);
                    flag = true;
                    break;
                }

                if(TouchesEdgeCircle(c[p], Ray))
                {
                    entries[i].setFill(Color.BLUE);
                    entries[dest-1].setFill(Color.BLUE);
                    flag = true;
                    break;
                }
            }

                if (flag) {
                    break;
                }
                Ray.setEndX(l);
                Ray.setEndY(y);
                m++;
            }

        if(!flag) {
            entries[dest - 1].setFill(Color.LIMEGREEN);
            entries[i].setFill(Color.LIMEGREEN);
            entries[dest-1].setMouseTransparent(true);
        }
        //Ray.setVisible(false);
        return Ray;
        }

        boolean TouchesEdgeCircle(Circle c, Line ray)
        {
            double tolerance = 0;
            return Math.abs(distance(ray.getEndX(), ray.getEndY(), c.getCenterX(), c.getCenterY()) - 85.0f) <= tolerance;
        }


        double distance(double x1, double y1, double x2, double y2)
        {
            double x3 = x2-x1;
            double y3 = y2-y1;
            return Math.hypot(x3, y3);
        }

    public static void main(String[] args) {
        launch(args);
    }
}