/**
 * COSC121 Lab 8
 * March 25th 2026
 * @author Ronald J.L. Lorette 300294189
 * @version 1.2
 */

import java.io.IOException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class MinesweeperView {

    private static final int TILE_SIZE = 32;
    private static final int FACE_SIZE = 26;
    private static final int DIGIT_WIDTH = 13;
    private static final int DIGIT_HEIGHT = 23;
    private static final int OUTER_PADDING = 8;
    private static final int HEADER_PADDING = 6;
    private static final int HEADER_TO_BOARD_GAP = 8;

    private BorderPane root;
    private VBox topPane; //holds the menu bar above the header
    private MenuBar menuBar; //menu bar for level selection and highscores
    private HBox headerPane;
    private GridPane boardPane;

    private Button[][] tileButtons;

    private ImageView faceView;
    private ImageView[] bombsLeftDigits;
    private ImageView[] timerDigits;

    private MenuItem beginnerItem; //menu option to switch to beginner level
    private MenuItem intermediateItem; //menu option to switch to intermediate level
    private MenuItem expertItem; //menu option to switch to expert level
    private MenuItem highScoresItem; //menu option to display saved highscores

    /**
     * Constructs the view and initializes UI containers.
     */
    public MinesweeperView() {
        root = new BorderPane();
        topPane = new VBox();
        menuBar = new MenuBar();
        headerPane = new HBox();
        boardPane = new GridPane();

        faceView = new ImageView();
        bombsLeftDigits = new ImageView[3];
        timerDigits = new ImageView[3];
    }

    /**
     * Builds the full layout based on board configuration.
     *
     * @param config the board configuration
     * @return the root layout pane
     */
    public BorderPane buildLayout(BoardConfig config) {
        int boardPixelWidth = config.getCols() * TILE_SIZE;
        int boardPixelHeight = config.getRows() * TILE_SIZE;
        int headerPixelHeight = (HEADER_PADDING * 2) + DIGIT_HEIGHT;

        menuBar = createMenuBar();
        headerPane = createHeader(boardPixelWidth);
        boardPane = createBoard(config.getRows(), config.getCols());

        topPane.getChildren().clear();
        topPane.getChildren().addAll(menuBar, headerPane); //rebuilds top section so resized boards use a fresh menu/header layout

        root.setPadding(new Insets(OUTER_PADDING));
        root.setTop(topPane);
        root.setCenter(boardPane);

        BorderPane.setAlignment(topPane, Pos.CENTER);
        BorderPane.setAlignment(boardPane, Pos.CENTER);
        BorderPane.setMargin(headerPane, new Insets(0, 0, HEADER_TO_BOARD_GAP, 0));

        root.setPrefWidth(boardPixelWidth + (OUTER_PADDING * 2));
        root.setPrefHeight(
                boardPixelHeight
                + headerPixelHeight
                + HEADER_TO_BOARD_GAP
                + (OUTER_PADDING * 2)
        );

        return root;
    }

    /**
     * Creates the menu bar and its game menu items.
     *
     * @return the constructed menu bar
     */
    private MenuBar createMenuBar() {
        beginnerItem = new MenuItem("Beginner");
        intermediateItem = new MenuItem("Intermediate");
        expertItem = new MenuItem("Expert");
        highScoresItem = new MenuItem("High Scores");

        Menu gameMenu = new Menu("Game");
        gameMenu.getItems().addAll(beginnerItem, intermediateItem, expertItem, highScoresItem);

        MenuBar bar = new MenuBar();
        bar.getMenus().add(gameMenu);

        return bar;
    }

    /**
     * Creates the header section with bomb counter, face, and timer.
     *
     * @param boardPixelWidth width of the board
     * @return header HBox
     */
    public HBox createHeader(int boardPixelWidth) {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER);
        header.setSpacing(0);
        header.setPadding(new Insets(HEADER_PADDING));

        HBox bombsBox = createDigitBox(bombsLeftDigits);
        HBox timerBox = createDigitBox(timerDigits);

        faceView.setFitWidth(FACE_SIZE);
        faceView.setFitHeight(FACE_SIZE);
        faceView.setPreserveRatio(true);

        Region leftSpacer = new Region();
        Region rightSpacer = new Region();

        double digitBoxWidth = DIGIT_WIDTH * 3;
        double faceBoxWidth = FACE_SIZE;
        double remainingSpace = boardPixelWidth - digitBoxWidth - digitBoxWidth - faceBoxWidth;

        if (remainingSpace < 0) {
            remainingSpace = 0;
        }

        double eachSpacerWidth = remainingSpace / 2.0;

        leftSpacer.setMinWidth(eachSpacerWidth);
        leftSpacer.setPrefWidth(eachSpacerWidth);
        leftSpacer.setMaxWidth(eachSpacerWidth);

        rightSpacer.setMinWidth(eachSpacerWidth);
        rightSpacer.setPrefWidth(eachSpacerWidth);
        rightSpacer.setMaxWidth(eachSpacerWidth);

        header.getChildren().addAll(
                bombsBox,
                leftSpacer,
                faceView,
                rightSpacer,
                timerBox
        );

        header.setMinWidth(boardPixelWidth);
        header.setPrefWidth(boardPixelWidth);
        header.setMaxWidth(boardPixelWidth);

        return header;
    }

    /**
     * Creates a 3-digit display box.
     *
     * @param digitViews array to store digit image views
     * @return digit container
     */
    private HBox createDigitBox(ImageView[] digitViews) {
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER_LEFT);
        box.setSpacing(0);

        for (int index = 0; index < digitViews.length; index++) {
            digitViews[index] = new ImageView();
            digitViews[index].setFitWidth(DIGIT_WIDTH);
            digitViews[index].setFitHeight(DIGIT_HEIGHT);
            digitViews[index].setPreserveRatio(true);
            box.getChildren().add(digitViews[index]);
        }

        box.setMinWidth(DIGIT_WIDTH * 3);
        box.setPrefWidth(DIGIT_WIDTH * 3);
        box.setMaxWidth(DIGIT_WIDTH * 3);

        return box;
    }

    /**
     * Creates the grid of tile buttons.
     *
     * @param rows number of rows
     * @param cols number of columns
     * @return grid pane
     */
    public GridPane createBoard(int rows, int cols) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(0);
        grid.setVgap(0);

        tileButtons = new Button[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Button button = new Button();
                button.setMinSize(TILE_SIZE, TILE_SIZE);
                button.setPrefSize(TILE_SIZE, TILE_SIZE);
                button.setMaxSize(TILE_SIZE, TILE_SIZE);
                button.setFocusTraversable(false);
                button.setPadding(Insets.EMPTY);

                tileButtons[row][col] = button;
                grid.add(button, col, row);
            }
        }

        grid.setMinSize(cols * TILE_SIZE, rows * TILE_SIZE);
        grid.setPrefSize(cols * TILE_SIZE, rows * TILE_SIZE);
        grid.setMaxSize(cols * TILE_SIZE, rows * TILE_SIZE);

        return grid;
    }

    /**
     * Binds tile click handlers using lambdas.
     *
     * @param handler the click handler implementation
     */
    public void bindPrimaryTileHandler(TileClickHandler handler) {
        if (tileButtons == null) {
            return;
        }

        for (int row = 0; row < tileButtons.length; row++) {
            for (int col = 0; col < tileButtons[row].length; col++) {
                final int currentRow = row;
                final int currentCol = col;

                tileButtons[row][col].setOnMousePressed(event -> {
                    faceView.fireEvent(event.copyFor(faceView, faceView)); //forwards board press events so the face can switch to the pressed image
                });

                tileButtons[row][col].setOnMouseReleased(event -> {
                    faceView.fireEvent(event.copyFor(faceView, faceView)); //restores the face state when the board press is released
                });

                tileButtons[row][col].setOnMouseClicked(event -> {
                    boolean isRightClick = event.getButton() == MouseButton.SECONDARY;
                    boolean isLeftClick = event.getButton() == MouseButton.PRIMARY;

                    if (isLeftClick || isRightClick) {
                        handler.handle(currentRow, currentCol, isRightClick);
                    }
                });
            }
        }
    }

    /**
     * Binds the face click handler.
     *
     * @param handler action to run on click
     */
    public void bindFaceHandler(Runnable handler) {
        faceView.setOnMouseClicked(event -> handler.run());
    }

    /**
     * Binds the face pressed handler.
     *
     * @param handler action to run while the face is pressed
     */
    public void bindFacePressedHandler(Runnable handler) {
        faceView.setOnMousePressed(event -> handler.run());
    }

    /**
     * Binds the face released handler.
     *
     * @param handler action to run when the face is released
     */
    public void bindFaceReleasedHandler(Runnable handler) {
        faceView.setOnMouseReleased(event -> handler.run());
    }

    /**
     * Binds the beginner menu item handler.
     *
     * @param handler action to run when beginner is selected
     */
    public void bindBeginnerHandler(Runnable handler) {
        beginnerItem.setOnAction(event -> handler.run());
    }

    /**
     * Binds the intermediate menu item handler.
     *
     * @param handler action to run when intermediate is selected
     */
    public void bindIntermediateHandler(Runnable handler) {
        intermediateItem.setOnAction(event -> handler.run());
    }

    /**
     * Binds the expert menu item handler.
     *
     * @param handler action to run when expert is selected
     */
    public void bindExpertHandler(Runnable handler) {
        expertItem.setOnAction(event -> handler.run());
    }

    /**
     * Binds the high score menu item handler.
     *
     * @param handler action to run when highscores are selected
     */
    public void bindHighScoreHandler(Runnable handler) {
        highScoresItem.setOnAction(event -> handler.run());
    }

    /**
     * Updates a single tile's visual state.
     *
     * @param row tile row
     * @param col tile column
     * @param tile tile state
     * @param assets asset loader
     */
    public void updateTile(int row, int col, TileViewState tile, AssetLoader assets) {
        Button button = tileButtons[row][col];

        try {
            javafx.scene.image.Image image;

            if (tile.isMisflagged()) {
                image = assets.loadMisflaggedTile(); //uses a separate image when a placed flag does not match an actual mine
            }
            else if (tile.isFlagged() && !tile.isRevealed()) {
                image = assets.loadFlagTile();
            }
            else if (tile.isExplodedMine()) {
                image = assets.loadRedMine();
            }
            else if (tile.isShowMine()) {
                image = assets.loadGreyMine();
            }
            else if (!tile.isRevealed()) {
                image = assets.loadCoverTile();
            }
            else {
                int value = tile.getAdjacentMines();
                image = assets.loadNumberTile(value);
            }

            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(TILE_SIZE);
            imageView.setFitHeight(TILE_SIZE);
            imageView.setPreserveRatio(true);

            button.setGraphic(imageView);
        }
        catch (IOException exception) {
            button.setGraphic(null);
            System.out.println("Error loading tile image: " + exception.getMessage());
        }
    }

    /**
     * Refreshes the entire board display.
     *
     * @param snapshot board snapshot
     * @param assets asset loader
     */
    public void refreshBoard(BoardSnapshot snapshot, AssetLoader assets) {
        TileViewState[][] tiles = snapshot.getTiles();

        for (int row = 0; row < snapshot.getRows(); row++) {
            for (int col = 0; col < snapshot.getCols(); col++) {
                updateTile(row, col, tiles[row][col], assets);
            }
        }
    }

    /**
     * Updates the header display.
     *
     * @param header header snapshot
     * @param assets asset loader
     */
    public void updateHeader(HeaderSnapshot header, AssetLoader assets) {
        try {
            faceView.setImage(assets.loadFace(header.getFaceState()));
            updateCounterImages(bombsLeftDigits, header.getBombsLeft(), assets);
            updateCounterImages(timerDigits, header.getElapsedTime(), assets);
        }
        catch (IOException exception) {
            faceView.setImage(null);
            System.out.println("Error loading header image: " + exception.getMessage());
        }
    }

    /**
     * Updates digit images for counters.
     *
     * @param digitViews digit image views
     * @param value value to display
     * @param assets asset loader
     * @throws IOException if loading fails
     */
    private void updateCounterImages(ImageView[] digitViews, int value, AssetLoader assets)
            throws IOException {
        int displayValue = value;

        if (displayValue > 999) {
            displayValue = 999;
        }

        if (displayValue < -99) {
            displayValue = -99;
        }

        if (displayValue < 0) {
            int positiveValue = Math.abs(displayValue);
            int tens = positiveValue / 10;
            int ones = positiveValue % 10;

            digitViews[0].setImage(assets.loadNegativeDigit());
            digitViews[1].setImage(assets.loadDigit(tens));
            digitViews[2].setImage(assets.loadDigit(ones));
        } else {
            int hundreds = displayValue / 100;
            int tens = (displayValue / 10) % 10;
            int ones = displayValue % 10;

            digitViews[0].setImage(assets.loadDigit(hundreds));
            digitViews[1].setImage(assets.loadDigit(tens));
            digitViews[2].setImage(assets.loadDigit(ones));
        }
    }

    /**
     * Returns the root layout.
     *
     * @return root pane
     */
    public BorderPane getRoot() {
        return root;
    }
}