package solver;

/**
 * Model data for GUI's update()
 * @author Will Mackin
 */

public class SudokuModelData {

    private final int value;
    private final int row;
    private final int col;
    private final int id;

    public SudokuModelData(int value, int row, int col, int id) {
        this.value = value;
        this.row = row;
        this.col = col;
        this.id = id;
    }

    /**
     * Value accessor
     * @return  value
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Row accessor
     * @return  row
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Column accessor
     * @return  column
     */
    public int getCol() {
        return this.col;
    }

    /**
     * ID accessor
     * @return  id
     */
    public int getID() {
        return this.id;
    }

}
