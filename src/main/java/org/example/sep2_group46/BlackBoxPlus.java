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
        GameAtoms.createAtoms(randomHexagonCoordinates);

        root.getChildren().addAll(Arrays.asList(GameAtoms.getMolecule()));
        root.getChildren().addAll(Arrays.asList(GameAtoms.getCirclesOfInfluence()));

        RayEntry GameEntries = new RayEntry(GameBoard.getRayEntry());
        GameEntries.placeRayEntries(root);
        RayPath rays = GameEntries.entryRayBuilder(root, GameAtoms);

        createGuessButton(root, rays, randomHexagonCoordinates);
        root.getChildren().addAll(createEndGameButton(), createButtonRelatedToAtoms(GameAtoms));

        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    private Button createEndGameButton()
    {
        Button endGameButton = new Button();
        endGameButton.setText("End Game");
        endGameButton.setTextFill(Color.YELLOW);
        endGameButton.setStyle("-fx-background-color: black;-fx-border-color: yellow; -fx-border-width: 2;");
        endGameButton.setLayoutX(1300);
        endGameButton.setLayoutY(590);
        endGameButton.setPrefSize(150, 50);
        endGameButton.setOnAction(e -> System.exit(0));
        return endGameButton;
    }

    private void createGuessButton(Pane Board, RayPath rays, int[] hexagonCoordinates)
    {
        Button guessButton = new Button();
        guessButton.setText("Guess Atoms");
        guessButton.setTextFill(Color.YELLOW);
        guessButton.setStyle("-fx-background-color: black;-fx-border-color: yellow; -fx-border-width: 2;");
        guessButton.setLayoutX(1300);
        guessButton.setLayoutY(500);
        guessButton.setPrefSize(150, 50);
        guessButton.setOnAction(e -> {
            Text GuessPrompt = new Text();
            GuessPrompt.setText("""
                    Guess Atoms by typing the hexagon numbers you think the atoms are in:
                    Format your guesses like this in the white text box under this prompt
                    "Hexagon Number 1,Hexagon Number 2,Hexagon Number 3,etc"
                    Commas between each hexagon number and no spaces
                    Press ENTER to confirm your answers
                    """);
            GuessPrompt.setFont(Font.font("Arial", 12));
            GuessPrompt.setLayoutX(1140);
            GuessPrompt.setLayoutY(200);
            GuessPrompt.setFill(Color.YELLOW);
            Board.getChildren().add(GuessPrompt);

            TextArea GuessPromptArea = new TextArea();
            GuessPromptArea.setEditable(true);
            VBox box = new VBox(GuessPromptArea);
            box.setLayoutX(1250);
            box.setLayoutY(270);
            box.setPrefSize(150, 50);
            Board.getChildren().add(box);
            guessButton.setDisable(true);

            GuessPromptArea.setOnKeyPressed(keyEvent -> {if(keyEvent.getCode() == KeyCode.ENTER)
            {
                GuessPromptArea.setEditable(false);
                GuessPromptArea.setMouseTransparent(true);
                String text = GuessPromptArea.getText();
                int misplacedAtomsCount = 0;
                String[] hexagonNumbers = text.split("[,\n]");

                if(hexagonNumbers.length < 6)
                {
                    misplacedAtomsCount += (6 - hexagonNumbers.length);
                }

                for(int i = 0; i < hexagonNumbers.length; i++) {
                    int finalI = i;
                    boolean isGuessCorrect = Arrays.stream(hexagonCoordinates).noneMatch(hex -> hex == Integer.parseInt(hexagonNumbers[finalI]));
                    if(isGuessCorrect)
                        misplacedAtomsCount++;
                }

                Board.getChildren().forEach(node -> node.setVisible(true));
                Text score = new Text();
                int scoreValue = rays.getRayMarkerCount() + (5 * misplacedAtomsCount);
                score.setText("Score: " + scoreValue);
                score.setFont(Font.font("Arial", 20));
                score.setLayoutX(1250);
                score.setLayoutY(100);
                score.setFill(Color.YELLOW);
                Board.getChildren().add(score);
            }
            });

        });
        Board.getChildren().add(guessButton);
    }

    private Button createButtonRelatedToAtoms(AtomCreator atoms)
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
        return b;
    }

    public static void main(String[] args) {
        launch(args);
    }
}