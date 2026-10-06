/**
 * COSC121 Lab 8
 * March 25th 2026
 * @author Ronald J.L. Lorette 300294189
 * @version 1.2
 */

import java.io.IOException;
import javafx.scene.image.Image;

public class AssetLoader {

    /**
     * Loads an image from a given file path.
     *
     * @param path the file path to the image
     * @return the loaded Image object
     * @throws IOException if the image fails to load or an error occurs
     */
    public Image loadImage(String path) throws IOException {
        try {
            Image image = new Image("file:" + path);

            if (image.isError()) {
                throw new IOException("Failed to load image: " + path, image.getException());
            }

            return image;
        }
        catch (IOException exception) {
            throw new IOException("Failed to load image: " + path, exception);
        }
        catch (Exception exception) {
            throw new IOException("Unexpected error loading image: " + path, exception);
        }
    }

    /**
     * Loads the face image corresponding to the current face state.
     *
     * @param faceState the current face state
     * @return the corresponding face image
     * @throws IOException if the image fails to load
     */
    public Image loadFace(FaceState faceState) throws IOException {
        switch (faceState) {
            case SMILE:
                return loadImage("res/minesweeper-basic/face-smile.png");
            case WIN:
                return loadImage("res/minesweeper-basic/face-win.png");
            case DEAD:
                return loadImage("res/minesweeper-basic/face-dead.png");
            case PRESSED:
                return loadImage("res/minesweeper-basic/face-o.png"); //used when mouse is pressed anywhere on board
            default:
                throw new IOException("Invalid face state: " + faceState);
        }
    }

    /**
     * Loads the covered tile image.
     *
     * @return the covered tile image
     * @throws IOException if the image fails to load
     */
    public Image loadCoverTile() throws IOException {
        return loadImage("res/minesweeper-basic/cover.png");
    }

    /**
     * Loads the red mine image (exploded mine).
     *
     * @return the red mine image
     * @throws IOException if the image fails to load
     */
    public Image loadRedMine() throws IOException {
        return loadImage("res/minesweeper-basic/mine-red.png");
    }

    /**
     * Loads the grey mine image (untriggered mine).
     *
     * @return the grey mine image
     * @throws IOException if the image fails to load
     */
    public Image loadGreyMine() throws IOException {
        return loadImage("res/minesweeper-basic/mine-grey.png");
    }

    /**
     * Loads the flag tile image.
     *
     * @return the flag image
     * @throws IOException if the image fails to load
     */
    public Image loadFlagTile() throws IOException {
        return loadImage("res/minesweeper-basic/flag.png");
    }

    /**
     * Loads the misflagged tile image.
     *
     * @return the misflagged tile image
     * @throws IOException if the image fails to load
     */
    public Image loadMisflaggedTile() throws IOException {
        return loadImage("res/minesweeper-basic/mine-misflagged.png"); //used when a flag is placed incorrectly
    }

    /**
     * Loads a numbered tile image.
     *
     * @param value the number of adjacent mines
     * @return the corresponding numbered tile image
     * @throws IOException if the image fails to load
     */
    public Image loadNumberTile(int value) throws IOException {
        return loadImage("res/minesweeper-basic/" + value + ".png");
    }

    /**
     * Loads a digit image for the counters.
     *
     * @param digit the digit to load (0–9)
     * @return the digit image
     * @throws IOException if the image fails to load
     */
    public Image loadDigit(int digit) throws IOException {
        return loadImage("res/digits/" + digit + ".png");
    }

    /**
     * Loads the negative digit image for counters.
     *
     * @return the negative sign image
     * @throws IOException if the image fails to load
     */
    public Image loadNegativeDigit() throws IOException {
        return loadImage("res/digits/neg.png");
    }
}