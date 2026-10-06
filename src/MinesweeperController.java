/**
 * COSC121 Lab 8
 * March 25th 2026
 * @author Ronald J.L. Lorette 300294189
 * @version 1.2
 */

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import java.util.Optional;

public class MinesweeperController implements TileClickHandler {

    private MinesweeperEngine engine;
    private MinesweeperView view;
    private AssetLoader assets;
    private BoardConfig config;
    private Levels currentLevel; //stores the currently selected difficulty level
    private Timeline timerTimeline; //controls the one-second repeating timer
    private HighScoreManager highScoreManager; //manages saved high score data
    private boolean boardPrinted; //prevents the board from printing more than once per game
    private boolean scoreProcessed; //prevents win/loss end-state logic from running repeatedly

    /**
     * Constructs a MinesweeperController and initializes its dependencies.
     *
     * @param config the initial board configuration
     * @param level the initial difficulty level
     */
    public MinesweeperController(BoardConfig config, Levels level) {
        this.config = config;
        this.currentLevel = level;
        this.engine = new MinesweeperEngine(config, level);
        this.view = new MinesweeperView();
        this.assets = new AssetLoader();
        this.highScoreManager = new HighScoreManager();
        this.boardPrinted = false;
        this.scoreProcessed = false;
    }

    /**
     * Builds the root layout and binds all view handlers.
     *
     * @return the root BorderPane for the application
     */
    public BorderPane buildRoot() {
        BorderPane root = view.buildLayout(config);

        view.bindPrimaryTileHandler(this);
        view.bindFaceHandler(() -> startGame());
        view.bindFacePressedHandler(() -> {
            engine.setFacePressed(); //temporarily switches the face image while the mouse is held down
            refreshHeader();
        });
        view.bindFaceReleasedHandler(() -> {
            engine.restoreFaceState(); //restores the normal face once the press ends
            refreshHeader();
        });

        view.bindBeginnerHandler(() -> changeLevel(Levels.BEGINNER));
        view.bindIntermediateHandler(() -> changeLevel(Levels.INTERMEDIATE));
        view.bindExpertHandler(() -> changeLevel(Levels.EXPERT));
        view.bindHighScoreHandler(() -> showHighScores());

        return root;
    }

    /**
     * Starts a new game using the current configuration.
     */
    public void startGame() {
        stopTimer();

        engine.applyConfig(config, currentLevel);
        engine.startNewGame();

        boardPrinted = false; //resets one-time board print tracking for the new game
        scoreProcessed = false; //resets end-state tracking for the new game

        startTimer();
        refreshView();
    }

    /**
     * Changes the active difficulty level and rebuilds the game layout.
     *
     * @param level the new difficulty level
     */
    private void changeLevel(Levels level) {
        currentLevel = level;
        config = BoardConfig.fromLevel(level);
        BorderPane root = buildRoot();
        startGame();

        javafx.stage.Stage stage = (javafx.stage.Stage) root.getScene().getWindow();
        stage.sizeToScene(); //resizes the window so the new board dimensions fit
        stage.centerOnScreen();
    }

    /**
     * Starts the repeating one-second timer.
     */
    private void startTimer() {
        timerTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            engine.incrementElapsedTime();
            refreshHeader();
        }));

        timerTimeline.setCycleCount(Animation.INDEFINITE);
        timerTimeline.play();
    }

    /**
     * Stops the timer if it is running.
     */
    private void stopTimer() {
        if (timerTimeline != null) {
            timerTimeline.stop();
        }
    }

    /**
     * Refreshes both the board and header, then processes win/loss state if needed.
     */
    public void refreshView() {
        refreshBoard();
        refreshHeader();

        if (!scoreProcessed && engine.getGameState() == GameState.WON) {
            stopTimer();
            processHighScore(); //checks whether the completed game should be stored as a high score
            scoreProcessed = true;
        }

        if (engine.getGameState() == GameState.LOST) {
            stopTimer();
            scoreProcessed = true;
        }
    }

    /**
     * Refreshes the header using the latest engine snapshot.
     */
    public void refreshHeader() {
        HeaderSnapshot header = engine.getHeaderSnapshot();
        view.updateHeader(header, assets);
    }

    /**
     * Refreshes the board using the latest engine snapshot.
     */
    public void refreshBoard() {
        BoardSnapshot boardSnapshot = engine.getBoardSnapshot();
        view.refreshBoard(boardSnapshot, assets);
    }

    /**
     * Handles a tile interaction from the view.
     *
     * @param row the clicked row
     * @param col the clicked column
     * @param isRightClick true if the click was a right click, false otherwise
     */
    @Override
    public void handle(int row, int col, boolean isRightClick) {
        if (engine.getGameState() != GameState.RUNNING) {
            return;
        }

        if (isRightClick) {
            engine.toggleFlag(row, col);
            refreshView();
            return;
        }

        engine.revealTile(row, col);

        if (!boardPrinted) {
            printBoard(engine.getBoardSnapshot()); //prints only after the first completed reveal so the rebuilt board is accurate
            boardPrinted = true;
        }

        refreshView();
    }

    /**
     * Returns the current board snapshot.
     *
     * @return the current board snapshot
     */
    public BoardSnapshot getBoardSnapshot() {
        return engine.getBoardSnapshot();
    }

    /**
     * Prints the current board layout to the console.
     *
     * @param snapshot the board snapshot to print
     */
    private void printBoard(BoardSnapshot snapshot) {
        TileViewState[][] tiles = snapshot.getTiles();

        System.out.println("Mine layout:");

        for (int row = 0; row < snapshot.getRows(); row++) {
            for (int col = 0; col < snapshot.getCols(); col++) {
                if (tiles[row][col].isMine()) {
                    System.out.print("* ");
                }
                else {
                    System.out.print(tiles[row][col].getAdjacentMines() + " ");
                }
            }

            System.out.println();
        }

        System.out.println();
    }

    /**
     * Prompts for a player name and saves the result if the current time qualifies
     * as a high score.
     */
    private void processHighScore() {
        int time = engine.getElapsedTime();

        if (!highScoreManager.isHighScore(currentLevel, time)) {
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New High Score");
        dialog.setHeaderText("High score for " + currentLevel.name());
        dialog.setContentText("Enter your name:");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            String name = result.get().trim();

            if (name.isEmpty()) {
                name = "Anonymous";
            }

            highScoreManager.updateScore(currentLevel, name, time);
        }
    }

    /**
     * Displays all saved high scores grouped by difficulty level in a dialog.
     *
     * Iterates through each level, retrieves its top scores, and formats them
     * into a ranked list for display.
     */
    private void showHighScores() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("High Scores");
        alert.setHeaderText("Best Times");

        StringBuilder builder = new StringBuilder(); //used to efficiently build multi-line output text

        for (Levels level : Levels.values()) { //iterates through each difficulty level
            builder.append(level.name()).append(":\n");

            java.util.ArrayList<ScoreEntry> entries = highScoreManager.getScores(level); //retrieves sorted score list for the level

            if (entries.isEmpty()) {
                builder.append("No score\n"); //handles case where no scores exist for the level
            }
            else {
                for (int index = 0; index < entries.size(); index++) {
                    ScoreEntry entry = entries.get(index);

                    builder.append(index + 1) //ranking position (1 = best score)
                           .append(". ")
                           .append(entry.getName())
                           .append(" - ")
                           .append(entry.getTime())
                           .append("s\n"); //formats each score entry as "rank. name - time"
                }
            }

            builder.append("\n"); //adds spacing between difficulty sections
        }

        alert.setContentText(builder.toString()); //sets the formatted text into the dialog
        alert.showAndWait(); //displays the dialog and waits for user to close it
    }
}