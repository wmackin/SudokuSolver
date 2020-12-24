package solver;

import ui.SudokuView;

import java.util.HashMap;

/**
 * Model class for sudoku solver. Stores information of a sudoku board.
 */

public class SudokuModel {
    private Board board; //board representation
    private final SudokuObserver<SudokuModel, SudokuModelData> observer; //model's observer, this will be the GUI

    public SudokuModel(SudokuObserver<SudokuModel, SudokuModelData> observer) {
        this.observer = observer;
        this.board = new Board(this);
    }

    /**
     * Forms string representation of the board
     * @return  board as string
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                str.append(board.getBoard()[r][c].toString());
            }
            str.append("\n");
        }
        return str.toString();
    }

    /**
     * Board accessor
     * @return  board representation
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Determines the square of a certain location
     * @param row   row
     * @param col   column
     * @return      square that this row and column coordinate is in
     */
    public int determineSquare(int row, int col) {
        if (row < 3) {
            return (col / 3);
        }
        else if (row < 6) {
            return 3 + (col / 3);
        }
        else {
            return 6 + (col / 3);
        }
    }

    /**
     * Ensures that the display shows every number once a solution is found
     * @param sol   solution board
     */
    public void fillBoard(Board sol) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board.getBoard()[r][c].getVal() == 0) {
                    board.addNumber(sol.getBoard()[r][c].getVal(), r, c);
                }
            }
        }
    }

    /**
     * Removes all numbers from the display and model
     */
    public void reset() {
        this.board = new Board(this);
    }

    /**
     * Solves the current board.
     */
    public void solve() {
        System.out.println("trying to solve on layer 0");
        System.out.println(this);
        boolean solving = true;
        while (solving) {
            solving = false;
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    if (board.getBoard()[r][c].determined() && board.getBoard()[r][c].getVal() == 0) {
                        board.addNumber(board.getBoard()[r][c].getPossibility(), r, c);
                        solving = true;
                    }
                }
            }
            for (int r = 0; r < 9; r++) {
                HashMap<Integer, Integer> rowMap = new HashMap<>();
                for (int i = 1; i <= 9; i++) {
                    rowMap.put(i, 0);
                }
                for (int c = 0; c < 9; c++) {
                    if (board.getBoard()[r][c].getVal() == 0) {
                        for (int val : board.getBoard()[r][c].getPossibilities()) {
                            rowMap.put(val, rowMap.get(val) + 1);
                        }
                    }
                }
                for (int i = 1; i <= 9; i ++) {
                    if (rowMap.get(i) == 1) {
                        for (int c = 0; c < 9; c++) {
                            if (board.getBoard()[r][c].getPossibilities().contains(i)) {
                                board.addNumber(i, r, c);
                                solving = true;
                            }
                        }
                    }
                }
            }
            for (int c = 0; c < 9; c++) {
                HashMap<Integer, Integer> colMap = new HashMap<>();
                for (int i = 1; i <= 9; i++) {
                    colMap.put(i, 0);
                }
                for (int r = 0; r < 9; r++) {
                    if (board.getBoard()[r][c].getVal() == 0) {
                        for (int val : board.getBoard()[r][c].getPossibilities()) {
                            colMap.put(val, colMap.get(val) + 1);
                        }
                    }
                }
                for (int i = 1; i <= 9; i ++) {
                    if (colMap.get(i) == 1) {
                        for (int r = 0; r < 9; r++) {
                            if (board.getBoard()[r][c].getPossibilities().contains(i)) {
                                board.addNumber(i, r, c);
                                solving = true;
                            }
                        }
                    }
                }
            }
        }
        if (!board.boardFilled()) {
            for (Board child : board.getSuccessors()) {
                System.out.println("board");
                System.out.println(board);
                System.out.println("child");
                System.out.println(child);
                if (child.isValid()) {
                    Board sol = solve(child, 1);
                    if (sol.boardFilled()) {
                        fillBoard(sol);
                        for (int r = 0; r < 9; r++) {
                            for (int c = 0; c < 9; c++) {
                                if (board.getBoard()[r][c].determined() && board.getBoard()[r][c].getVal() == 0) {
                                    board.addNumber(board.getBoard()[r][c].getPossibility(), r, c);
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    /**
     * Recursively solves the board
     * @param grid  successor to try solving
     * @param layer level of recursion
     * @return      solution board
     */
    public Board solve (Board grid, int layer) {
        System.out.println("trying to solve on layer " + layer);
        System.out.println(grid);
        Board board = new Board(grid.getBoard());
        boolean solving = true;
        while (solving) {
            solving = false;
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    if (board.getBoard()[r][c].determined() && board.getBoard()[r][c].getVal() == 0) {
                        board.addNumberBacktracking(board.getBoard()[r][c].getPossibility(), r, c);
                        solving = true;
                    }
                }
            }
            for (int r = 0; r < 9; r++) {
                HashMap<Integer, Integer> rowMap = new HashMap<>();
                for (int i = 1; i <= 9; i++) {
                    rowMap.put(i, 0);
                }
                for (int c = 0; c < 9; c++) {
                    if (board.getBoard()[r][c].getVal() == 0) {
                        for (int val : board.getBoard()[r][c].getPossibilities()) {
                            rowMap.put(val, rowMap.get(val) + 1);
                        }
                    }
                }
                for (int i = 1; i <= 9; i ++) {
                    if (rowMap.get(i) == 1) {
                        for (int c = 0; c < 9; c++) {
                            if (board.getBoard()[r][c].getPossibilities().contains(i)) {
                                board.addNumberBacktracking(i, r, c);
                                solving = true;
                            }
                        }
                    }
                }
            }
            for (int c = 0; c < 9; c++) {
                HashMap<Integer, Integer> colMap = new HashMap<>();
                for (int i = 1; i <= 9; i++) {
                    colMap.put(i, 0);
                }
                for (int r = 0; r < 9; r++) {
                    if (board.getBoard()[r][c].getVal() == 0) {
                        for (int val : board.getBoard()[r][c].getPossibilities()) {
                            colMap.put(val, colMap.get(val) + 1);
                        }
                    }
                }
                for (int i = 1; i <= 9; i ++) {
                    if (colMap.get(i) == 1) {
                        for (int r = 0; r < 9; r++) {
                            if (board.getBoard()[r][c].getPossibilities().contains(i)) {
                                board.addNumberBacktracking(i, r, c);
                                solving = true;
                            }
                        }
                    }
                }
            }
        }
        if (board.boardFilled()) {
            return board;
        }
        else {
            boolean validChildren = false;
            for (Board child : board.getSuccessors()) {
                if (child.isValid()) {
                    System.out.println("board");
                    System.out.println(board);
                    System.out.println("child");
                    System.out.println(child);
                    validChildren = true;
                    Board sol = solve(child, layer + 1);
                    if (sol.boardFilled()) {
                        return sol;
                    }
                }
            }
            if (!validChildren) {
                return board;
            }
        }
        return board;
    }

    /**
     * Gets the id of a button at a location
     * @param row   button row
     * @param col   button column
     * @return      button id
     */
    public int getButtonID(int row, int col) {
        for (SudokuView.SudokuButton b : observer.getButtons()) {
            if (b.getRow() == row && b.getCol() == col) {
                return b.getID();
            }
        }
        return 0;
    }

    /**
     * Observer accessor
     * @return  observer
     */
    public SudokuObserver<SudokuModel, SudokuModelData> getObserver() {
        return observer;
    }
}
