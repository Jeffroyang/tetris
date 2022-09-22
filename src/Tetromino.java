

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

public class Tetromino {

    Point2D.Double pivot; // the point where the tetromino block rotates around
    private Block[] tetromino;
    private char block;
    private Color color;
    private boolean[][] blockSpace;
    private int numRotations; // number of clockwise rotations on the tetramino

    /**
     * Constructor for Tetromino class
     * 
     * @param block Character representing the block to be created
     */
    public Tetromino(char block) {
        tetromino = new Block[4];
        this.block = block;
        pivot = (new Point2D.Double());
        resetTetromino();
    }

    /**
     * Clone of the current Tetromino block, used later for checking if a rotation
     * is possible
     */
    public Tetromino clone() {
        Tetromino clone = new Tetromino(block);
        clone.setPivot(pivot.getX(), pivot.getY());
        clone.setState(numRotations);
        return clone;
    }

    /**
     * method that rests a tetromino to its original spawning orientation, this is
     * used in the contructor and used when player decides to hold a piece
     */
    public void resetTetromino() {

        // set the number of clockwise rotations to 0
        this.numRotations = 0;

        // set up the block space based on block character
        if (block == 'I' || block == 'O') {
            ioTetromino();
        } else if (block == 'L' || block == 'T' || block == 'J') {
            jltTetromino();
        } else if (block == 'S' || block == 'Z') {
            szTetromino();
        } else {
            System.out.println("invalid block");
        }

        // update orientation of the block
        updateOrientation();
    }

    /**
     * Helper method that fills the Tetromino array with blocks of a certain color
     * 
     * @param c The Color of the blocks
     */
    private void fillTetromino(Color c) {
        for (int i = 0; i < 4; i++) {
            tetromino[i] = new Block(c);
        }
    }

    /**
     * Helper method that sets up the Block Space (boolean matrix) of the i or o
     * blocks, and sets the color of the Tetromino
     */
    private void ioTetromino() {
        blockSpace = new boolean[4][4];
        blockSpace[1][1] = true;
        blockSpace[1][2] = true;
        if (block == 'O') {
            color = Color.YELLOW;
            blockSpace[2][1] = true;
            blockSpace[2][2] = true;
            pivot.setLocation(4.5, 0.5);
        } else if (block == 'I') {
            color = Color.CYAN;
            blockSpace[1][0] = true;
            blockSpace[1][3] = true;
            pivot.setLocation(4.5, 1.5);
        }
        fillTetromino(color);
    }

    /**
     * Helper method that sets up the Block Space (boolean matrix) of the j,l, or
     * t block, and sets the color of the Tetromino
     */
    private void jltTetromino() {
        blockSpace = new boolean[3][3];
        blockSpace[1][0] = true;
        blockSpace[1][1] = true;
        blockSpace[1][2] = true;
        if (block == 'J') {
            color = Color.BLUE;
            blockSpace[0][0] = true;
        } else if (block == 'L') {
            color = Color.ORANGE;
            blockSpace[0][2] = true;
        } else if (block == 'T') {
            color = Color.MAGENTA;
            blockSpace[0][1] = true;
        }

        fillTetromino(color);
        pivot.setLocation(4, 1);
    }

    /**
     * Helper method that sets up the Block Space (boolean matrix) of the s or z
     * block, and sets the color of the Tetromino
     */
    private void szTetromino() {
        blockSpace = new boolean[3][3];
        blockSpace[0][1] = true;
        blockSpace[1][1] = true;

        if (block == 'S') {
            color = Color.GREEN;
            blockSpace[0][2] = true;
            blockSpace[1][0] = true;
        } else if (block == 'Z') {
            color = Color.RED;
            blockSpace[0][0] = true;
            blockSpace[1][2] = true;
        }
        fillTetromino(color);
        pivot.setLocation(4, 1);
    }

    /**
     * Helper method that helps propogate updateOrientation to ioUpdateOrientation
     * or jlstzUpdateOrientation
     */
    private void updateOrientation() {
        if (block == 'I' || block == 'O') {
            ioUpdateOrientation();
        } else {
            jltszUpdateOrientation();
        }
    }

    /**
     * Helper method that updates the orientation of the i or o block based on the
     * our block space
     * which is a boolean matrix
     */
    private void ioUpdateOrientation() {
        // index for which block's location to update
        int index = 0;
        double pivotX = pivot.getX();
        double pivotY = pivot.getY();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (blockSpace[i][j]) {
                    int x = (int) (pivotX - 1.5 + j);
                    int y = (int) (pivotY - 1.5 + i);
                    tetromino[index].setCol(x);
                    tetromino[index].setRow(y);
                    index++;
                }
            }
        }
    }

    /**
     * Helper method that updates the orientation of the j,l,t,s, or z block based
     * on the our
     * block space which is a boolean matrix
     */
    private void jltszUpdateOrientation() {
        // index for which block's location to update
        int index = 0;
        double pivotX = pivot.getX();
        double pivotY = pivot.getY();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (blockSpace[i][j]) {
                    int x = (int) (pivotX - 1 + j);
                    int y = (int) (pivotY - 1 + i);
                    tetromino[index].setCol(x);
                    tetromino[index].setRow(y);
                    index++;
                }
            }
        }
    }

    /**
     * Method that rotates the Tetromino clockwise by updating the blockSpace and
     * then updating it's
     * orientation
     */
    public void rotateCW() {
        int size = blockSpace[0].length;
        boolean[][] postRotation = new boolean[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                postRotation[col][size - row - 1] = blockSpace[row][col];
            }
        }
        blockSpace = postRotation;

        // update the orientation of the tetromino
        updateOrientation();

        // add to the number of rotations
        this.numRotations = (numRotations + 1) % 4;
    }

    /**
     * method that rotates the Tetromino counterclockwise by updating blockSpace
     * and then updating
     * it's orientation
     */
    public void rotateCCW() {
        int size = blockSpace[0].length;
        boolean[][] postRotation = new boolean[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                postRotation[row][col] = blockSpace[col][size - row - 1];
            }
        }
        blockSpace = postRotation;

        // update the orientation
        updateOrientation();

        if (numRotations == 0) {
            numRotations = 3;
        } else {
            numRotations--;
        }
    }

    /**
     * method that makes the tetris pieces fall by one level
     */
    public void fall() {
        // set the pivot one row lower
        pivot.setLocation(pivot.getX(), pivot.getY() + 1);
        // set all the blocks one row lower
        for (Block b : tetromino) {
            int row = b.getRow();
            b.setRow(row + 1);
        }
    }

    /**
     * Method that moves every block in the Tetromino to the left
     */
    public void moveLeft() {
        // set the pivot one column to the left
        pivot.setLocation(pivot.getX() - 1, pivot.getY());

        // set all the blocks one column to the left
        for (Block b : tetromino) {
            int col = b.getCol();
            b.setCol(col - 1);
        }
    }

    /**
     * Method that moves every block in the Tetromino to the right
     */
    public void moveRight() {
        // set the pivot one column to the right
        pivot.setLocation(pivot.getX() + 1, pivot.getY());

        // set all the blocks one column to the right
        for (Block b : tetromino) {
            int col = b.getCol();
            b.setCol(col + 1);
        }
    }

    /**
     * Method that sets the state of the block (number of clockwise rotations it has
     * experienced)
     * 
     * @param state Number of clockwise rotations
     */
    public void setState(int state) {
        for (int i = 0; i < state; i++) {
            rotateCW();
        }
    }

    /**
     * Method that sets a new pivot for the tetromino and updates the orientation of
     * the tetromino
     * 
     * @param x The new X pivot of the tetromino
     * @param y The new Y pivot of the tetromino
     */
    public void setPivot(double x, double y) {
        pivot.setLocation(x, y);
        updateOrientation();
    }

    /**
     * Getter method that returns the X coordinate of the pivot
     * 
     * @return int The X coordinate of the pivot
     */
    public double getXPivot() {
        return pivot.getX();
    }

    /**
     * Getter method that returns the Y coordinate of the pivot
     * 
     * @return int The Y coordinate of the pivot
     */
    public double getYPivot() {
        return pivot.getY();
    }

    /**
     * Getter method that returns the number of clockwise rotations the tetromino
     * deviates from
     * it's based orientation
     * 
     * @return int The number of clockwise rotations
     */
    public int getState() {
        return this.numRotations;
    }

    /**
     * Getter rethod that returns the character representing the block
     * 
     * @return Char The character representing the block
     */
    public char getBlockChar() {
        return block;
    }

    /**
     * method that returns the ith block of the Tetromino array
     * 
     * @param i the ith block to return
     * @return The ith Block object of the Tetromino array
     */
    public Block getBlock(int i) {
        return tetromino[i];
    }

    /**
     * method that returns the row number of the lowest block
     * 
     * @return Lowest row occupied by the tetromino
     */
    public int getLowestBlock() {
        // higher the row number, the lower the block
        int lowest = tetromino[0].getRow();
        for (int i = 1; i < 4; i++) {
            int level = tetromino[i].getRow();
            if (level > lowest) {
                lowest = level;
            }
        }
        return lowest;
    }

    /**
     * Method that returns a copy of the current block space
     * 
     * @return Boolean matrix representing the block orientation
     */
    public boolean[][] getBlockSpace() {
        int size = blockSpace.length;
        boolean[][] bSpace = new boolean[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                bSpace[i][j] = blockSpace[i][j];
            }
        }
        return bSpace;
    }

    /**
     * Draw each block of the tetramino array
     * 
     * @param g The current Graphics context to draw in
     */
    public void draw(Graphics g) {
        for (Block b : tetromino) {
            b.draw(g);
        }
    }

    /**
     * Equals method used to compare two Tetromino objects
     * Equal if same pivot coordinates and same block character
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Tetromino) {
            Tetromino other = (Tetromino) o;
            if (other.getXPivot() == this.getXPivot()
                    && other.getYPivot() == this.getYPivot()
                    && other.getBlockChar() == this.getBlockChar()
                    && other.getState() == this.getState()) {
                return true;
            }
        }
        return false;
    }

}
