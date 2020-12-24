package solver;

import java.util.*;

/**
 * Representation of a sudoku board
 */

public class Board {
    SudokuModel model; //model that this board is for
    Node[][] board; //2d array of nodes to represent the board

    /**
     * This constructor makes a new board for backtracking, making a deep copy
     * @param board current board
     */
    public Board(Node[][] board) {
        this.board = copyArrayBacktracking(board);
    }

    /**
     * This constructor makes an initial empty board
     * @param model model that this board is for
     */
    public Board(SudokuModel model) {
        this.model = model;
        this.board = makeEmptyBoard();
    }

    /**
     * Board accessor
     * @return  board
     */
    public Node[][] getBoard() {
        return this.board;
    }

    /**
     * Initializes an empty board representation
     * @return  2d array of valueless nodes
     */
    public Node[][] makeEmptyBoard() {
        Node[][] board = new Node[9][9];
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                board[r][c] = new Node(0, r, c, model.determineSquare(r, c), model.getButtonID(r, c));
            }
        }
        return board;
    }

    /**
     * Add a number to the original board
     * @param value number to add
     * @param row   row to add number in
     * @param col   column to add number in
     */
    public void addNumber(int value, int row, int col) {
        int square = determineSquare(row, col);
        this.board[row][col] = new Node(value, row, col, square, model.getButtonID(row, col));
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if ((this.board[r][c].getRow() == row) || (this.board[r][c].getCol() == col) || (this.board[r][c].getSquare() == square)) {
                    this.board[r][c].removePossibility(value);
                }
            }
        }
        model.getObserver().update(this.model, new SudokuModelData(value, row, col, model.getButtonID(row, col)));
    }

    /**
     * Adds a number to a board from a successor while backtracking to not update the original board
     * @param value number to add
     * @param row   row to add number in
     * @param col   column to add number in
     */
    public void addNumberBacktracking(int value, int row, int col) {
        int square = determineSquare(row, col);
        this.board[row][col] = new Node(value, row, col, square);
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if ((this.board[r][c].getRow() == row) || (this.board[r][c].getCol() == col) || (this.board[r][c].getSquare() == square)) {
                    this.board[r][c].removePossibility(value);
                }
            }
        }
    }

    /**
     * Checks if the board is filled in
     * @return  true if board is full, otherwise false
     */
    public boolean boardFilled() {
        for (Node[] nodes : this.board) {
            for (Node n : nodes) {
                if (!n.determined()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Gets the successors for the current board
     * @return  collection of successors
     */
    public Collection<Board> getSuccessors() {
        List<Board> successors = new LinkedList<>();
        outerLoop:
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c].getVal() == 0) {
                    ArrayList<Integer> possibilities = board[r][c].getPossibilities();
                    for (int possibility : possibilities) {
                        Node[][] copy = copyArrayBacktracking(this.board);
                        Board newBoard = new Board(this.board);
                        newBoard.addNumberBacktracking(possibility, r, c);
                        successors.add(newBoard);
                        this.board = copy;
                    }
                    break outerLoop;
                }
            }
        }
        return successors;
    }

    /**
     * Gets a string representation of the board
     * @return  board as string
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                str.append(board[r][c].toString());
            }
            str.append("\n");
        }
        return str.toString();
    }

    /**
     * Checks if the board is valid
     * @return  true if board valid so far, false if board unsolvable
     */
    public boolean isValid() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c].getPossibilities().size() == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Deep copies the board
     * @param board board to copy
     * @return      copy of board
     */
    public static Node[][] copyArrayBacktracking(Node[][] board){
        Node[][] copy = new Node[9][9];
        for (int r = 0; r < 9; r++){
            for (int c = 0; c < 9; c++) {
                copy[r][c] = board[r][c].copyNodeBacktracking();
            }
        }
        return copy;
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
}
