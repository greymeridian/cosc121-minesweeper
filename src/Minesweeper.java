/**
 * COSC121 Lab 8
 * March 25th 2026
 * @author Ronald J.L. Lorette 300294189
 * @version 1.2
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Optional;

public class Minesweeper extends Application {

    /**
     * Launches the JavaFX application.
     *
     * @param args command-line arguments passed to the program
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Builds and shows the main application window.
     *
     * @param stage the primary stage for the application
     */
    @Override
    public void start(Stage stage) {
        Levels selectedLevel = promptForLevel(); //gets the starting difficulty before building the game

        BoardConfig config = BoardConfig.fromLevel(selectedLevel);
        MinesweeperController controller = new MinesweeperController(config, selectedLevel);
        BorderPane root = controller.buildRoot();

        Scene scene = new Scene(root);

        stage.setTitle("Minesweeper");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();

        controller.startGame();
    }

    /**
     * Prompts the user to select a starting difficulty level using a dialog.
     *
     * @return the selected difficulty level, or BEGINNER if the dialog is closed
     */
    private Levels promptForLevel() {
        ChoiceDialog<Levels> dialog = new ChoiceDialog<>(Levels.BEGINNER, Levels.values()); //creates a dialog with BEGINNER as default and all levels as options
        dialog.setTitle("Select Level");
        dialog.setHeaderText("Choose a difficulty level");
        dialog.setContentText("Level:");

        Optional<Levels> result = dialog.showAndWait(); //waits for user selection and wraps result in Optional

        if (result.isPresent()) {
            return result.get(); //returns selected level if user made a choice
        }

        return Levels.BEGINNER; //fallback if dialog is closed without selection
    }
}