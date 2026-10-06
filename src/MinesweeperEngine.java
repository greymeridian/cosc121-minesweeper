/**
 * COSC121 Lab 8
 * March 25th 2026
 * @author Ronald J.L. Lorette 300294189
 * @version 1.2
 */

import java.util.Random;

public class MinesweeperEngine {

    private Tile[][] board;
    private int rows;
    private int cols;
    private int mineCount;
    private int flagsPlaced;
    private int elapsedTime; //stores the running time shown in the header

    private GameState gameState;
    private FaceState faceState;
    private boolean firstClick; //tracks whether the next reveal is the first reveal of the game
    private Levels currentLevel; //stores the currently selected difficulty

    private final int[][] neighborOffsets = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},           {0, 1},
            {1, -1},  {1, 0},  {1, 1}
    };

    /**
     * Constructs a MinesweeperEngine using the supplied configuration and level.
     *
     * @param config the board configuration containing size and mine count
     * @param level the selected difficulty level
     */
    public MinesweeperEngine(BoardConfig config, Levels level) {
        applyConfig(config, level);
    }

    /**
     * Applies a new board configuration and level to the engine.
     *
     * @param config the new board configuration
     * @param level the selected difficulty level
     */
    public void applyConfig(BoardConfig config, Levels level) {
        this.rows = config.getRows();
        this.cols = config.getCols();
        this.mineCount = config.getMineCount();
        this.currentLevel = level;
        this.board = new Tile[rows][cols];
    }

    /**
     * Starts a new game by resetting all game state and rebuilding the board.
     */
    public void startNewGame() {
        gameState = GameState.RUNNING;
        faceState = FaceState.SMILE;
        flagsPlaced = 0;
        elapsedTime = 0;
        firstClick = true; //first reveal gets special handling so the starting tile is always safe

        initializeBoard();
        placeMines();
        calculateAllAdjacentMines();
    }

    /**
     * Creates a new Tile object in every position of the board array.
     */
    private void initializeBoard() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                board[row][col] = new Tile();
            }
        }
    }

    /**
     * Randomly places the required number of mines on the board.
     */
    private void placeMines() {
        Random random = new Random();
        int placed = 0;

        while (placed < mineCount) {
            int row = random.nextInt(rows);
            int col = random.nextInt(cols);

            if (!board[row][col].isMine()) {
                board[row][col].setMine(true);
                placed++;
            }
        }
    }

    /**
     * Calculates and stores the adjacent mine count for every non-mine tile.
     */
    private void calculateAllAdjacentMines() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (!board[row][col].isMine()) {
                    int count = countNeighborMines(row, col, 0);
                    board[row][col].setAdjacentMines(count);
                }
            }
        }
    }

    /**
     * Recursively counts mines in the eight neighboring positions around a tile.
     *
     * Base case:
     * When index reaches the length of neighborOffsets, there are no more neighbors
     * left to check, so the method returns 0.
     *
     * Recursive step:
     * The method checks one neighboring offset at the current index, computes either
     * 0 or 1 for that neighbor, then adds that value to the result of the next call:
     * count(index) = currentNeighborValue + count(index + 1)
     *
     * This means each stack frame stores one partial result until the recursion
     * unwinds and the totals are added together.
     *
     * @param row the row of the tile whose neighbors are being checked
     * @param col the column of the tile whose neighbors are being checked
     * @param index the current neighbor offset index
     * @return the total number of neighboring mines
     */
    private int countNeighborMines(int row, int col, int index) {
        if (index >= neighborOffsets.length) {
            return 0; //base case:no more neighbors remain to be checked
        }

        int newRow = row + neighborOffsets[index][0];
        int newCol = col + neighborOffsets[index][1];

        int count = 0;

        if (isInBounds(newRow, newCol) &&
            board[newRow][newCol].isMine()) {
            count = 1;
        }

        return count + countNeighborMines(row, col, index + 1); //recursive equation:current result + result of remaining neighbors
    }

    /**
     * Returns whether a row and column are inside the board bounds.
     *
     * @param row the row to test
     * @param col the column to test
     * @return true if the position is inside the board, otherwise false
     */
    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < rows &&
               col >= 0 && col < cols;
    }

    /**
     * Rebuilds the board repeatedly until the given position contains a zero tile.
     *
     * @param row the required safe row
     * @param col the required safe column
     */
    private void rebuildUntilZeroAt(int row, int col) {
        boolean valid = false;

        while (!valid) {
            initializeBoard();
            placeMines();
            calculateAllAdjacentMines();

            if (!board[row][col].isMine() &&
                board[row][col].getAdjacentMines() == 0) {
                valid = true;
            }
        }
    }

    /**
     * Reveals a tile and applies all related game logic.
     *
     * @param row the row of the tile to reveal
     * @param col the column of the tile to reveal
     */
    public void revealTile(int row, int col) {
        if (gameState != GameState.RUNNING) {
            return;
        }

        if (!isInBounds(row, col)) {
            return;
        }

        if (firstClick) {
            rebuildUntilZeroAt(row, col); //rebuilds until the starting tile is a zero tile
            firstClick = false;
        }

        Tile tile = board[row][col];

        if (tile.isFlagged()) {
            return;
        }

        if (tile.isRevealed()) {
            chordReveal(row, col); //allows a revealed numbered tile to open its neighbors when flags match
            return;
        }

        revealRecursive(row, col);

        if (gameState == GameState.RUNNING && checkWin()) {
            gameState = GameState.WON;
            faceState = FaceState.WIN;
            revealAllTiles();
        }
    }

    /**
     * Recursively reveals a tile and expands through connected zero-value tiles.
     *
     * Base cases:
     * 1. The position is outside the board.
     * 2. The tile is already revealed.
     * 3. The tile is flagged.
     * 4. The tile is not zero after being revealed.
     *
     * Recursive step:
     * If the tile is a revealed zero, the method calls revealNeighbors, which
     * continues the flood-fill process into surrounding tiles.
     *
     * Conceptually:
     * revealRecursive(tile) = reveal current tile + reveal recursive neighbors
     *
     * @param row the row of the tile to reveal
     * @param col the column of the tile to reveal
     */
    private void revealRecursive(int row, int col) {
        if (!isInBounds(row, col)) {
            return; //base case:position is outside the board
        }

        Tile tile = board[row][col];

        if (tile.isRevealed() || tile.isFlagged()) {
            return; //base case:stop if tile is already processed or blocked by a flag
        }

        tile.setRevealed(true);

        if (tile.isMine()) {
            tile.setExplodedMine(true);
            gameState = GameState.LOST;
            faceState = FaceState.DEAD;
            revealAllTiles();
            markMisflags();
            return; //base case:hitting a mine ends the recursive path immediately
        }

        if (tile.getAdjacentMines() != 0) {
            return; //base case:numbered tiles reveal once and do not expand further
        }

        revealNeighbors(row, col, 0); //recursive flood-fill continues only from zero tiles
    }

    /**
     * Recursively processes all eight neighbors of a revealed zero tile.
     *
     * Base case:
     * When index reaches the length of neighborOffsets, all neighbors have been processed.
     *
     * Recursive step:
     * The method reveals the neighbor at the current offset, then calls itself for the
     * next offset:
     * neighbors(index) = reveal one neighbor + neighbors(index + 1)
     *
     * @param row the row of the source tile
     * @param col the column of the source tile
     * @param index the current neighbor offset index
     */
    private void revealNeighbors(int row, int col, int index) {
        if (index >= neighborOffsets.length) {
            return; //base case:all neighboring offsets have been visited
        }

        int newRow = row + neighborOffsets[index][0];
        int newCol = col + neighborOffsets[index][1];

        if (isInBounds(newRow, newCol)) {
            revealRecursive(newRow, newCol);
        }

        revealNeighbors(row, col, index + 1); //recursive equation:process current neighbor, then remaining neighbors
    }

    /**
     * Reveals neighboring tiles around a revealed numbered tile if its flag count matches
     * its adjacent mine count.
     *
     * @param row the row of the revealed numbered tile
     * @param col the column of the revealed numbered tile
     */
    private void chordReveal(int row, int col) {
        Tile tile = board[row][col];

        if (!tile.isRevealed()) {
            return;
        }

        if (tile.getAdjacentMines() <= 0) {
            return;
        }

        int flags = countFlags(row, col, 0);

        if (flags != tile.getAdjacentMines()) {
            return;
        }

        chordRevealNeighbors(row, col, 0); //opens remaining surrounding tiles when placed flags match the number
    }

    /**
     * Recursively counts flagged neighbors around a tile.
     *
     * Base case:
     * When index reaches the length of neighborOffsets, no more neighbors remain.
     *
     * Recursive step:
     * count(index) = currentFlagValue + count(index + 1)
     *
     * @param row the row of the tile whose neighbors are being checked
     * @param col the column of the tile whose neighbors are being checked
     * @param index the current neighbor offset index
     * @return the number of flagged neighboring tiles
     */
    private int countFlags(int row, int col, int index) {
        if (index >= neighborOffsets.length) {
            return 0; //base case:no more neighbors remain
        }

        int newRow = row + neighborOffsets[index][0];
        int newCol = col + neighborOffsets[index][1];

        int count = 0;

        if (isInBounds(newRow, newCol) &&
            board[newRow][newCol].isFlagged()) {
            count = 1;
        }

        return count + countFlags(row, col, index + 1); //recursive equation:current flag count + remaining flag counts
    }

    /**
     * Recursively reveals the neighbors around a tile after a valid chord action.
     *
     * Base case:
     * When index reaches the length of neighborOffsets, all neighbors are done.
     *
     * Recursive step:
     * Reveal the current eligible neighbor, then move to index + 1.
     *
     * @param row the row of the source tile
     * @param col the column of the source tile
     * @param index the current neighbor offset index
     */
    private void chordRevealNeighbors(int row, int col, int index) {
        if (index >= neighborOffsets.length) {
            return; //base case:all surrounding neighbors have been checked
        }

        int newRow = row + neighborOffsets[index][0];
        int newCol = col + neighborOffsets[index][1];

        if (isInBounds(newRow, newCol)) {
            Tile neighbor = board[newRow][newCol];

            if (!neighbor.isFlagged()) {
                revealRecursive(newRow, newCol);
            }
        }

        chordRevealNeighbors(row, col, index + 1); //recursive equation:process current neighbor, then remaining neighbors
    }

    /**
     * Toggles a flag on a tile.
     *
     * @param row the row of the tile
     * @param col the column of the tile
     */
    public void toggleFlag(int row, int col) {
        if (gameState != GameState.RUNNING) {
            return;
        }

        if (!isInBounds(row, col)) {
            return;
        }

        Tile tile = board[row][col];

        if (tile.isRevealed()) {
            return;
        }

        if (tile.isFlagged()) {
            tile.setFlagged(false);
            flagsPlaced--;
            return;
        }

        tile.setFlagged(true);
        flagsPlaced++;
    }

    /**
     * Reveals all tiles at the end of the game.
     */
    private void revealAllTiles() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Tile tile = board[row][col];

                if (!tile.isMine()) {
                    tile.setRevealed(true);
                }

                if (tile.isMine() &&
                    !tile.isFlagged() &&
                    !tile.isExplodedMine()) {
                    tile.setShowMine(true);
                }
            }
        }
    }

    /**
     * Marks flagged tiles that are not actually mines.
     */
    private void markMisflags() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Tile tile = board[row][col];

                if (tile.isFlagged() && !tile.isMine()) {
                    tile.setMisflagged(true); //lets the view render an incorrect flag image
                }
            }
        }
    }

    /**
     * Returns whether the player has revealed all non-mine tiles.
     *
     * @return true if the player has won, otherwise false
     */
    private boolean checkWin() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Tile tile = board[row][col];

                if (!tile.isMine() && !tile.isRevealed()) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Increments the elapsed time counter.
     */
    public void incrementElapsedTime() {
        if (gameState == GameState.RUNNING) {
            elapsedTime++;
        }
    }

    /**
     * Returns the elapsed time.
     *
     * @return the elapsed time in seconds
     */
    public int getElapsedTime() {
        return elapsedTime;
    }

    /**
     * Returns the current difficulty level.
     *
     * @return the current level
     */
    public Levels getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Sets the face state to the pressed face.
     */
    public void setFacePressed() {
        if (gameState == GameState.RUNNING) {
            faceState = FaceState.PRESSED; //temporary face state while mouse is held down
        }
    }

    /**
     * Restores the face to the normal smile state.
     */
    public void restoreFaceState() {
        if (gameState == GameState.RUNNING) {
            faceState = FaceState.SMILE;
        }
    }

    /**
     * Builds and returns a snapshot of the board state for the view.
     *
     * @return a snapshot of the board state
     */
    public BoardSnapshot getBoardSnapshot() {
        TileViewState[][] snapshot = new TileViewState[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Tile tile = board[row][col];

                snapshot[row][col] = new TileViewState(
                        tile.isRevealed(),
                        tile.isMine(),
                        tile.isFlagged(),
                        tile.isExplodedMine(),
                        tile.isShowMine(),
                        tile.isMisflagged(),
                        tile.getAdjacentMines()
                );
            }
        }

        return new BoardSnapshot(snapshot, rows, cols);
    }

    /**
     * Builds and returns a snapshot of the header state for the view.
     *
     * @return a snapshot of the header state
     */
    public HeaderSnapshot getHeaderSnapshot() {
        int bombsLeft = mineCount - flagsPlaced;

        return new HeaderSnapshot(faceState, bombsLeft, elapsedTime);
    }

    /**
     * Returns the current game state.
     *
     * @return the current game state
     */
    public GameState getGameState() {
        return gameState;
    }
}