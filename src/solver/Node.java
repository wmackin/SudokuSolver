package solver;

import java.util.ArrayList;

/**
 * Representation of a cell in the sudoku grid
 * @author Will Mackin
 */

public class Node {
    private final int value; //value of node, 0 when empty
    private final int row; //node row
    private final int col; //node col
    private final int square; //square that this node is in
    private int id; //node id
    private final ArrayList<Integer> possibleValues; //possible values at this location

    public Node(int value, int row, int col, int square, int id) {
        this.value = value;
        this.row = row;
        this.col = col;
        this.square = square;
        this.id = id;
        this.possibleValues = new ArrayList<>();
        if (this.value == 0) {
            for (int i = 1; i <= 9; i++) {
                this.possibleValues.add(i);
            }
        }
        else {
            this.possibleValues.add(value);
        }
    }

    public Node(int value, int row, int col, int square, int id, ArrayList<Integer> possibleValues) {
        this.value = value;
        this.row = row;
        this.col = col;
        this.square = square;
        this.id = id;
        this.possibleValues = new ArrayList<>();
        this.possibleValues.addAll(possibleValues);

    }

    public Node(int value, int row, int col, int square) {
        this.value = value;
        this.row = row;
        this.col = col;
        this.square = square;
        this.possibleValues = new ArrayList<>();
        if (this.value == 0) {
            for (int i = 1; i <= 9; i++) {
                this.possibleValues.add(i);
            }
        }
        else {
            this.possibleValues.add(value);
        }
    }

    /**
     * Deep copies this node
     * @return  copy of this node
     */
    public Node copyNodeBacktracking() {
        return new Node(getVal(), getRow(), getCol(), getSquare(), getID(), getPossibilities());
    }

    /**
     * Row accessor
     * @return  node row
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Column accessor
     * @return  node column
     */
    public int getCol() {
        return this.col;
    }

    /**
     * Square accessor
     * @return  node square
     */
    public int getSquare() {
        return this.square;
    }

    /**
     * Value accessor
     * @return  node value
     */
    public int getVal() {
        return this.value;
    }

    /**
     * ID accessor
     * @return  node id
     */
    public int getID() {
        return this.id;
    }

    /**
     * Gets the first possibility for this node
     * @return  One possible value of this node
     */
    public int getPossibility() {
        return this.possibleValues.get(0);
    }

    /**
     * Possibilities accessor
     * @return  All possible values for this node
     */
    public ArrayList<Integer> getPossibilities() {
        return this.possibleValues;
    }

    /**
     * Determines if we know what value must go in this node by seeing if there is only one possible value
     * @return  true if node can be solved, otherwise false
     */
    public boolean determined() {
        return this.possibleValues.size() == 1;
    }

    /**
     * Removes a possibility from this node
     * @param number    number to remove as possibile value
     */
    public void removePossibility(Integer number) {
        if (possibleValues.contains(number) && this.value != number) {
            possibleValues.remove(number);
        }
    }

    /**
     * Gives a string representation of this node
     * @return  node as string
     */
    @Override
    public String toString() {
        return possibleValues.toString();
    }

}
