package org.example.sep2_group46;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.util.Arrays;


public class BlackBoxPlus extends Application {

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        primaryStage.setTitle("Hexagonal Black Box+");

        HexagonalBoardSetup GameBoard  = new HexagonalBoardSetup();
        GameBoard.createHexagonalBoard(root);

        AtomCreator GameAtoms = new AtomCreator(GameBoard.getXyLocation());
        int[] randomHexagonCoordinates = GameBoard.randomHexagonCoordinates();
        GameAtoms.createAtoms(root, randomHexagonCoordinates);

        RayEntry GameEntries = new RayEntry(GameBoard.getRayEntry());
        GameEntries.placeRayEntries(root);
        RayPath rays = GameEntries.entryRayBuilder(root, GameAtoms);

        createButtonRelatedToAtoms(root, GameAtoms);
        createGuessButton(root, rays, randomHexagonCoordinates);
        createEndGameButton(root);

        //createGuide(root);

        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    private void createEndGameButton(Pane Board)
    {
        Button b = new Button();
        b.setText("End Game");
        b.setTextFill(Color.YELLOW);
        b.setStyle("-fx-background-color: black;-fx-border-color: yellow; -fx-border-width: 2;");
        b.setLayoutX(1300);
        b.setLayoutY(590);
        b.setPrefSize(150, 50);
        b.setOnAction(e -> {
            System.exit(0);
        });
        Board.getChildren().add(b);
    }

    private void createGuessButton(Pane Board, RayPath rays, int[] hexagonCoordinates)
    {
        Button b = new Button();
        b.setText("Guess Atoms");
        b.setTextFill(Color.YELLOW);
        b.setStyle("-fx-background-color: black;-fx-border-color: yellow; -fx-border-width: 2;");
        b.setLayoutX(1300);
        b.setLayoutY(500);
        b.setPrefSize(150, 50);
        b.setOnAction(e -> {
            Text GuessPrompt = new Text();
            GuessPrompt.setText("""
                    Guess Atoms by typing the hexagon numbers you think the atoms are in:
                    Make sure to format it as this in the white text box
                    "Hexagon Number 1,Hexagon Number 2,Hexagon Number 3 etc" 
                    Please press enter to confirm your answers
                    """);
            GuessPrompt.setFont(Font.font("Arial", 12));
            GuessPrompt.setLayoutX(1150);
            GuessPrompt.setLayoutY(200);
            GuessPrompt.setFill(Color.YELLOW);
            Board.getChildren().add(GuessPrompt);

            TextArea GuessPromptArea = new TextArea();
            GuessPromptArea.setEditable(true);
            VBox box = new VBox(GuessPromptArea);
            box.setLayoutX(1250);
            box.setLayoutY(250);
            box.setPrefSize(150, 50);
            Board.getChildren().add(box);

            GuessPromptArea.setOnKeyPressed(keyEvent -> {if(keyEvent.getCode() == KeyCode.ENTER)
            {
                String text = GuessPromptArea.getText();
                int misplacedAtomsCount = 0;
                String[] hexagonNumbers = text.split("[,\n]");

                for(int i = 0; i < hexagonNumbers.length; i++) {
                    int finalI = i;
                    boolean a1 = Arrays.stream(hexagonCoordinates).noneMatch(hex -> hex == Integer.parseInt(hexagonNumbers[finalI]));
                    if(a1)
                        misplacedAtomsCount++;
                }
                Board.getChildren().forEach(node -> node.setVisible(true));
                Text score = new Text();
                int scoreValue = rays.getRayMarkerCount() + (5 * misplacedAtomsCount);
                score.setText("Score: " + scoreValue);
                score.setFont(Font.font("Arial", 20));
                score.setLayoutX(1300);
                score.setLayoutY(100);
                score.setFill(Color.YELLOW);
                Board.getChildren().add(score);
                //System.exit(1);
            }
            });

        });
        Board.getChildren().add(b);
    }

    private void createButtonRelatedToAtoms(Pane Board, AtomCreator atoms)
    {
        Button b = new Button();
        b.setText("Show Atoms");
        b.setTextFill(Color.YELLOW);
        b.setStyle("-fx-background-color: black;-fx-border-color: yellow; -fx-border-width: 2;");
        b.setLayoutX(1300);
        b.setLayoutY(680);
        b.setPrefSize(150, 50);
        b.setOnAction(actionEvent -> {
            if(b.getText().equals("Show Atoms"))
            {
                //Shows atoms
                atoms.showAtoms();
                b.setText("Hide Atoms"); //Switches functionality to hide
            }
            else if(b.getText().equals("Hide Atoms")) {
                //Hides atoms
                atoms.hideAtoms();
                b.setText("Show Atoms"); //Switches functionality to show
            }
        });
        Board.getChildren().add(b);
    }


    private void createGuide(Pane board, RayPath rays)
    {
        Text score = new Text();
        score.setText("Score: " + rays.getRayMarkerCount());
        score.setFont(Font.font("Arial", 12));
        score.setLayoutX(1200);
        score.setLayoutY(200);
        score.setFill(Color.YELLOW);
        board.getChildren().add(score);
    }

    public static void main(String[] args) {
        launch(args);
    }
}