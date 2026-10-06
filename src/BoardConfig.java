/**
 * COSC121 Lab 8
 * March 25th 2026
 * @author Ronald J.L. Lorette 300294189
 * @version 1.2
 */

public class BoardConfig {

    private int rows;
    private int cols;
    private int mineCount;
    private int[][] fixedMinePositions;

    /**
     * Constructs a BoardConfig with specified dimensions and mine configuration.
     *
     * @param rows the number of rows in the board
     * @param cols the number of columns in the board
     * @param mineCount the total number of mines
     * @param fixedMinePositions optional fixed mine positions (null if random)
     */
    public BoardConfig(int rows, int cols, int mineCount, int[][] fixedMinePositions) {
        this.rows = rows;
        this.cols = cols;
        this.mineCount = mineCount;
        this.fixedMinePositions = fixedMinePositions;
    }

    /**
     * Returns the number of rows.
     *
     * @return the number of rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * Returns the number of columns.
     *
     * @return the number of columns
     */
    public int getCols() {
        return cols;
    }

    /**
     * Returns the number of mines.
     *
     * @return the mine count
     */
    public int getMineCount() {
        return mineCount;
    }

    /**
     * Returns the fixed mine positions.
     *
     * @return a 2D array of fixed mine positions, or null if random placement is used
     */
    public int[][] getFixedMinePositions() {
        return fixedMinePositions;
    }

    /**
     * Returns a beginner-level board configuration.
     *
     * @return the beginner board configuration
     */
    public static BoardConfig beginner() {
        return new BoardConfig(8, 8, 10, null);
    }

    /**
     * Returns an intermediate-level board configuration.
     *
     * @return the intermediate board configuration
     */
    public static BoardConfig intermediate() {
        return new BoardConfig(16, 16, 40, null);
    }

    /**
     * Returns an expert-level board configuration.
     *
     * @return the expert board configuration
     */
    public static BoardConfig expert() {
        return new BoardConfig(16, 32, 99, null);
    }

    /**
     * Returns a BoardConfig based on the selected level.
     *
     * @param level the selected difficulty level
     * @return the corresponding board configuration
     */
    public static BoardConfig fromLevel(Levels level) { //maps difficulty enum to board dimensions and mine count
        switch (level) {
            case BEGINNER:
                return beginner();
            case INTERMEDIATE:
                return intermediate();
            case EXPERT:
                return expert();
            default:
                throw new IllegalArgumentException("Invalid level: " + level);
        }
    }
}