package bombsweeper;

import javax.swing.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.Iterator;

public class SmartSquare extends GameSquare {

    private boolean thisSquareHasBomb = false; //Boolean Variable for each SmartSquare to see if it has a bomb or not.
    private boolean revealed = false; //Boolean Variable for each SmartSquare to see if it has been revealed or not.
    public static final int MINE_PROBABILITY = 5; //Probability of Bomb. Higher the number, the less likely.
    private ArrayList<SmartSquare> squares; //ArrayList that will hold the nearby squares to this SmartSquare.

    public SmartSquare(int x, int y, GameBoard board) {
        super(x, y, "images/blank.png", board);
        Random r = new Random();
        thisSquareHasBomb = (r.nextInt(MINE_PROBABILITY) == 0); //Randomises bombs. Whether this SmartSquare has a bomb or not.
        squares = new ArrayList<SmartSquare>(); //Squares arrayList is initialised.
    }

    /**
     * The following method lays out what happens when a SmartSquare is clicked.
     * Used to override redraw method in super class. If a bomb is clicked, the
     * bomb image is revealed and a Game Over message pane pops up. If the
     * square hasn't got a bomb, nearby squares are checked in the BombCount
     * method.
     *
     */
    public void clicked() {
        //If statement to check if this SmartSquare contains a bomb.
        if (this.thisSquareHasBomb) {
            this.setImage("images/bomb.png"); //Sets image to the bomb image if this square has a bomb.
            this.reveal();
            JOptionPane.showMessageDialog(this, "You found a bomb! Game Over!");  //end game with a message pane
        } else {
            squares = this.nearbySquares(this.xLocation, this.yLocation); //ArrayList of all nearby squares(above, below, to the side and diagonal)
            bombCount(this, squares);  //Calls bombCount method to check how many bombs are nearby.
        }
    }

    /**
     * The following method obtains whether or not a square has a bomb. returns
     * true or false if the square has a bomb or doesn't have a bomb
     * respectively.
     */
    public boolean SquareHasBomb() {
        return thisSquareHasBomb;

    }

    /**
     * The following method obtains whether or not a square has been revealed.
     * returns true or false if the square has been revealed or not
     * respectively.
     */
    public boolean hasBeenRevealed() {
        return revealed;
    }

    /**
     * The following method sets the revealed instance variable to true. This
     * indicates that this square has been revealed.
     */
    public void reveal() {
        revealed = true;
    }

    /**
     * The following method checks nearby squares for bombs and returns how many
     * bombs are in the surrounding area. Then through recursion, if a square
     * with no bombs surrounding it is chosen, the method checks these
     * surrounding squares and reveals more squares.
     *
     * @param s the subject square.
     * @param squareList list of surrounding squares to be checked for bombs.
     */
    public void bombCount(SmartSquare s, ArrayList<SmartSquare> squareList) {
        if (s.SquareHasBomb() || s.hasBeenRevealed()) { //If square has bomb or has already been revealed. Exit method as this square doesn't need to be revealed.
        } else {
            int bombCounter = 0; //Will be used to count bombs

            Iterator<SmartSquare> iter = squareList.iterator(); //Initialise iterator

            while (iter.hasNext()) { //While loop to loop through squarelist until there are no more squares in the list.
                SmartSquare x = iter.next(); //Sets x to next square in squareList.
                if (!x.hasBeenRevealed() && x.SquareHasBomb()) { //Checks if x hasn't been revealed and if it has a bomb.
                    bombCounter++;
                    iter.remove(); //Remove this from there squarelist as this has a bomb and this shouldn't be revealed.
                }
            }

            String imageString = "images/" + Integer.toString(bombCounter) + ".png"; //Sets image to the number of surrounding bombs
            s.setImage(imageString);
            s.reveal();

            if ((bombCounter == 0 || squareList.contains(s)) && !squareList.isEmpty()) { //Checks if bombcount is 0 or that the square is a surrounding square and that the list isn't empty.

                iter = squareList.iterator();

                while (iter.hasNext()) {
                    SmartSquare x = iter.next();
                    ArrayList<SmartSquare> newSquareList = x.nearbySquares(x.xLocation, x.yLocation); //Creates new list of surrounding squares

                    bombCount(x, newSquareList); //Method calls itself
                }
            }
        }
    }

    /**
     * The following method obtains all surrounding squares. returns an
     * ArrayList of all surrounding squares.
     *
     * @param x The row of the subject square.
     * @param y The column of the subject square.
     */
    public ArrayList<SmartSquare> nearbySquares(int x, int y) {
        ArrayList<SmartSquare> nearbySquares = new ArrayList<>();   //Initialise ArrayList of nearby squares
        SmartSquare squareHolder;               //Acts as a temporary holder of each surrounding SmartSquare

        if ((squareHolder = (SmartSquare) board.getSquareAt(x, y + 1)) != null) { //if statement to check if null isn't returned. Null means square is out of bounds.
            nearbySquares.add(squareHolder);    //Adds the square to the ArrayList since it is within bounds.
        }

        if ((squareHolder = (SmartSquare) board.getSquareAt(x + 1, y + 1)) != null) {
            nearbySquares.add(squareHolder);
        }

        if ((squareHolder = (SmartSquare) board.getSquareAt(x + 1, y)) != null) {
            nearbySquares.add(squareHolder);
        }

        if ((squareHolder = (SmartSquare) board.getSquareAt(x + 1, y - 1)) != null) {
            nearbySquares.add(squareHolder);
        }

        if ((squareHolder = (SmartSquare) board.getSquareAt(x, y - 1)) != null) {
            nearbySquares.add(squareHolder);
        }

        if ((squareHolder = (SmartSquare) board.getSquareAt(x - 1, y - 1)) != null) {
            nearbySquares.add(squareHolder);
        }

        if ((squareHolder = (SmartSquare) board.getSquareAt(x - 1, y)) != null) {
            nearbySquares.add(squareHolder);
        }

        if ((squareHolder = (SmartSquare) board.getSquareAt(x - 1, y + 1)) != null) {
            nearbySquares.add(squareHolder);
        }

        return nearbySquares; //returns the ArrayList
    }
}
