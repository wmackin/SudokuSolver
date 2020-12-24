package ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import solver.SudokuModel;
import solver.SudokuModelData;
import solver.SudokuObserver;

import java.util.ArrayList;

/**
 * This program opens up a GUI in which you can enter numbers on a sudoku grid to then solve
 * @author Will Mackin
 */

public class SudokuView extends Application implements SudokuObserver<SudokuModel, SudokuModelData> {

    private final ArrayList<SudokuButton> buttons = new ArrayList<>(); //buttons for each grid cell
    private SudokuButton selectedButton; //button to highlight
    private SudokuModel model; //board model

    public class SudokuButton extends Button {
        /**
         * This is a custom button class for cells in the grid
         */
        private final int row; //row location of button
        private final int col; //col location of button
        private int value; //number in cell
        private final int id; //button id

        public SudokuButton(int row, int col, int value, int id) {
            this.row = row;
            this.col = col;
            this.value = value;
            this.id = id;
            this.setMinSize(100, 100);
            this.setMaxSize(100, 100);
            if (value != 0) {
                this.setText(String.valueOf(value));
            }
            else {
                this.setText("");
            }
            this.setStyle("-fx-font-size: 50");
            this.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        }

        /**
         * Highlights this button and unhighlights any other buttons
         */
        public void selectButton() {
            for (SudokuButton button : buttons) {
                button.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            }
            this.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
            selectedButton = this;
        }

        /**
         * Sets the value of this button
         * @param newValue  value for button
         */
        public void setValue(int newValue) {
            this.value = newValue;
            if (this.value != 0) {
                this.setText(Integer.toString(newValue));
            }
            else {
                this.setText("");
            }
        }

        /**
         * Row accessor
         * @return  button row
         */
        public int getRow() {
            return this.row;
        }

        /**
         * Column accessor
         * @return  button column
         */
        public int getCol() {
            return this.col;
        }

        /**
         * ID accessor
         * @return  button id
         */
        public int getID() {
            return this.id;
        }

        /**
         * Value accessor
         * @return  button value (number in cell)
         */
        public int getValue() {
            return this.value;
        }
    }

    /**
     * Accessor for buttons collection
     * @return  collection of buttons in the grid
     */
    @Override
    public ArrayList<SudokuButton> getButtons() {
        return this.buttons;
    }

    /**
     * Makes one of the 9 squares for the grid.
     * @param square    Square # to make
     * @return          gridpane for the 3x3 area
     */
    private GridPane makeGridPane(int square) {
        GridPane gridPane = new GridPane();
        SudokuButton button;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                button = new SudokuButton(((square / 3) * 3) + row, ((square % 3) * 3) + col,0, buttons.size());
                SudokuButton finalButton = button;
                button.setOnAction(event -> finalButton.selectButton());
                this.buttons.add(button);
                gridPane.add(button, col, row);
            }
        }
        gridPane.setVgap(1);
        gridPane.setHgap(1);
        gridPane.setGridLinesVisible(false);
        return gridPane;
    }

    /**
     * Constructs the GUI display
     * @param stage display stage
     */
    @Override
    public void start(Stage stage) {
        stage.setTitle("Sudoku");
        GridPane topLeft = makeGridPane(0);
        GridPane topCenter = makeGridPane(1);
        GridPane topRight = makeGridPane(2);
        GridPane centerLeft = makeGridPane(3);
        GridPane center = makeGridPane(4);
        GridPane centerRight = makeGridPane(5);
        GridPane bottomLeft = makeGridPane(6);
        GridPane bottomCenter = makeGridPane(7);
        GridPane bottomRight = makeGridPane(8);
        //javafx grid for future access
        GridPane grid = new GridPane();
        grid.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setGridLinesVisible(false);
        grid.add(topLeft, 0, 0);
        grid.add(topCenter, 1, 0);
        grid.add(topRight, 2, 0);
        grid.add(centerLeft, 0, 1);
        grid.add(center, 1, 1);
        grid.add(centerRight, 2, 1);
        grid.add(bottomLeft, 0, 2);
        grid.add(bottomCenter, 1, 2);
        grid.add(bottomRight, 2, 2);

        Button solveButton = new Button("Solve");
        solveButton.setMinWidth(306);
        solveButton.setMaxWidth(306);
        solveButton.setMinHeight(50);
        solveButton.setMaxHeight(50);
        solveButton.setOnAction(event -> {
            model.reset();
            for (SudokuButton b : this.buttons) {
                if (b.getValue() != 0) {
                    model.getBoard().addNumber(b.getValue(), b.getRow(), b.getCol());
                }
            }
            model.solve();
        });
        Button resetButton = new Button("Reset");
        resetButton.setMinWidth(305);
        resetButton.setMaxWidth(305);
        resetButton.setMinHeight(50);
        resetButton.setMaxHeight(50);
        resetButton.setOnAction(event -> {
            model.reset();
            for (SudokuButton b : buttons) {
                b.setValue(0);
            }
        });
        Button quitButton = new Button("Quit");
        quitButton.setMinWidth(305);
        quitButton.setMaxWidth(305);
        quitButton.setMinHeight(50);
        quitButton.setMaxHeight(50);
        quitButton.setOnAction(event -> ((Stage)(((Button)event.getSource()).getScene().getWindow())).close());

        HBox hbox = new HBox(resetButton, solveButton, quitButton);
        VBox vbox = new VBox(grid, hbox);
        Scene scene = new Scene(vbox);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            switch (keyEvent.getCode()) {
                case DIGIT1:
                    selectedButton.setValue(1);
                    break;
                case DIGIT2:
                    selectedButton.setValue(2);
                    break;
                case DIGIT3:
                    selectedButton.setValue(3);
                    break;
                case DIGIT4:
                    selectedButton.setValue(4);
                    break;
                case DIGIT5:
                    selectedButton.setValue(5);
                    break;
                case DIGIT6:
                    selectedButton.setValue(6);
                    break;
                case DIGIT7:
                    selectedButton.setValue(7);
                    break;
                case DIGIT8:
                    selectedButton.setValue(8);
                    break;
                case DIGIT9:
                    selectedButton.setValue(9);
                    break;
                case BACK_SPACE:
                    selectedButton.setValue(0);
                    break;
                case UP:
                case W:
                    int id = selectedButton.getID();
                    int newID;
                    if (selectedButton.getRow() == 0 || selectedButton.getRow() == 3 || selectedButton.getRow() == 6) {
                        newID = id - 21;
                    }
                    else {
                        newID = id - 3;
                    }
                    if (newID < 0) {
                        newID = newID + buttons.size();
                    }
                    buttons.get(newID).selectButton();
                    break;
                case DOWN:
                case S:
                    id = selectedButton.getID();
                    if (selectedButton.getRow() == 2 || selectedButton.getRow() == 5 || selectedButton.getRow() == 8) {
                        newID = id + 21;
                    }
                    else {
                        newID = id + 3;
                    }
                    if (newID >= buttons.size()) {
                        newID = newID - buttons.size();
                    }
                    buttons.get(newID).selectButton();
                    break;
                case RIGHT:
                case D:
                    id = selectedButton.getID();
                    if (selectedButton.getCol() == 8) {
                        newID = id + 61;
                    }
                    else if (selectedButton.getCol() == 2 || selectedButton.getCol() == 5) {
                        newID = id + 7;
                    }
                    else {
                        newID = id + 1;
                    }
                    if (newID >= buttons.size()) {
                        newID = newID - buttons.size();
                    }
                    buttons.get(newID).selectButton();
                    break;
                case LEFT:
                case A:
                    id = selectedButton.getID();
                    if (selectedButton.getCol() == 0) {
                        newID = id - 61;
                    }
                    else if (selectedButton.getCol() == 3 || selectedButton.getCol() == 6) {
                        newID = id - 7;
                    }
                    else {
                        newID = id - 1;
                    }
                    if (newID < 0) {
                        newID = newID + buttons.size();
                    }
                    buttons.get(newID).selectButton();
                    break;
            }

        });
        stage.setScene(scene);
        this.model = new SudokuModel(this);
        stage.show();
    }

    /**
     * Updates display
     * @param model model to get data from
     * @param data  data to use for update
     */
    @Override
    public void update(SudokuModel model, SudokuModelData data) {
        if (data.getValue() != 0) {
            this.buttons.get(data.getID()).setText(String.valueOf(data.getValue()));
        }
        else {
            this.buttons.get(data.getRow() * 9 + data.getCol()).setText("");
        }
    }

    public static void main( String[] args ) {
        launch();
    }
}
