

import java.awt.*;
import java.io.*;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Tetris {

    private Block[][] board; // board where the tetris pieces lie
    private int linesCleared;
    private int score;
    private Tetromino saved;
    private Tetromino falling;
    private LinkedList<Tetromino> queue; // queue of incoming tetrominoes
    private LinkedList<Integer> rowsToClear; // collection of rows to clear
    private boolean canSaveBlock; // whether or not you are allowed to save a block
    private boolean gameOver;

    /**
     * Constructor that calls the reset method to start the game
     */
    public Tetris() {
        reset();
    }

    /**
     * Constructor that resets the game and loads in data from a saved file
     * 
     * @param filename File where the saved data is stored
     */
    public Tetris(String filename) {
        reset();
        load(filename);
    }

    /**
     * Method that saves the state of the current tetris game to a text file
     */
    public void save() {
        try {
            FileWriter writer = new FileWriter("files/saved_tetris_game.txt");
            BufferedWriter bw = new BufferedWriter(writer);

            // save all the current placed blocks on the 20x10 grid (lines 1-200)
            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 10; j++) {
                    if (board[i][j] != null) {
                        bw.write(board[i][j].getColorString() + "\n");
                    } else {
                        bw.write("\n");
                    }

                }
            }

            // save the block character, the position, and the state of the falling
            // block(line 201)
            bw.write(
                    falling.getBlockChar() + " " + falling.getXPivot() + " " + falling.getYPivot()
                            + " " + falling.getState() + "\n"
            );

            // save the block character of the saved Tetromino if not null, else empty (line
            // 202)
            if (saved != null) {
                bw.write(saved.getBlockChar() + "\n");
            } else {
                bw.write("\n");
            }

            // save all the block's chars in the queue (lines 203-206)
            for (Tetromino t : queue) {
                bw.write(t.getBlockChar() + "\n");
            }

            // save the number of lines cleared (lines 207)
            bw.write(linesCleared + "\n");

            // save the current score (lines 208)
            bw.write(score + "\n");

            // save whether or not a block can be saved (line 209)
            if (canSaveBlock) {
                bw.write("true" + "\n");
            } else {
                bw.write("false" + "\n");
            }

            // save whether or not the game is over (line 210)
            if (gameOver) {
                bw.write("true");
            } else {
                bw.write("false");
            }

            bw.flush();
            bw.close();
        } catch (IOException e) {
            JFrame f = new JFrame();
            JOptionPane.showMessageDialog(f, "Save Failed");
        }
    }

    /**
     * method that loads a saved tetris game from a text file
     * 
     * @param filename The text file where tetris game data is stored
     */
    public void load(String filename) {
        try {

            FileReader reader = new FileReader(filename);
            BufferedReader br = new BufferedReader(reader);

            // load all the placed blocks on the 20x10 grid (lines 1-200)
            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 10; j++) {
                    String color = br.readLine();
                    if (color == null) {
                        // reset the game if the save file ends too early
                        reset();
                        JOptionPane.showMessageDialog(null, "Invalid Save File");
                        return;
                    } else if (!color.equals("")) {
                        Block b = new Block(i, j, color);
                        board[i][j] = b;
                    }
                }
            }

            // load the falling block (line 201)
            String fallingData = br.readLine();
            if (fallingData.equals(null) || fallingData.equals("")) {
                // reset the game if save file ends too early, or previous falling block not
                // saved
                reset();
                JOptionPane.showMessageDialog(null, "Invalid Save File");
                return;
            } else {
                // scan the fallingData line to get information for the falling block
                Scanner scan = new Scanner(fallingData);
                char blockChar = scan.next().charAt(0);
                double xPivot = scan.nextDouble();
                double yPivot = scan.nextDouble();
                int state = scan.nextInt();

                // recreate the falling block
                falling = new Tetromino(blockChar);
                falling.setPivot(xPivot, yPivot);
                falling.setState(state);
                scan.close();
            }

            // load the block character of the saved Tetromino if line is not empty (line
            // 202)
            String savedBlock = br.readLine();
            if (savedBlock == null) {
                // reset the game if the save file ends too early
                reset();
                JOptionPane.showMessageDialog(null, "Invalid Save File");
                return;
            } else if (!savedBlock.equals("")) {
                // loads in the saved block
                char blockSaved = savedBlock.charAt(0);
                saved = new Tetromino(blockSaved);
            }

            // clear all the tetrominoes in the queue
            queue.clear();

            // load all the blocks in queue (lines 203-206)
            for (int i = 0; i < 4; i++) {
                String queueBlock = br.readLine();
                if (queueBlock == null) {
                    // reset the game if the save file ends too early
                    reset();
                    JOptionPane.showMessageDialog(null, "Invalid Save File");
                    return;
                } else if (!queueBlock.equals("")) {
                    // loads in the blocks in queue
                    char blockQueue = queueBlock.charAt(0);
                    queue.add(new Tetromino(blockQueue));
                }
            }

            // load the lines cleared in the saved game (line 207)
            String linesClearedStr = br.readLine();
            if (linesClearedStr == null) {
                // reset the game if the save file ends too early
                reset();
                JOptionPane.showMessageDialog(null, "Invalid Save File");
                return;
            } else {
                linesCleared = Integer.parseInt(linesClearedStr);
            }

            // load the score in the saved game (line 208)
            String scoreStr = br.readLine();
            if (scoreStr == null) {
                // reset the game if the save file ends too early
                reset();
                JOptionPane.showMessageDialog(null, "Invalid Save File");
                return;
            } else {
                score = Integer.parseInt(scoreStr);
            }

            // load whether or not a block can be saved in the current round (line 209)
            String canSave = br.readLine();
            if (canSave == null) {
                // reset the game if the save file ends too early
                reset();
                JOptionPane.showMessageDialog(null, "Invalid Save File");
                return;
            } else {
                canSaveBlock = Boolean.parseBoolean(canSave);
            }

            // load whether or not the game is over (line 210)
            String gameOverStr = br.readLine();
            if (gameOverStr == null) {
                // reset the game if the save file ends too early
                reset();
                JOptionPane.showMessageDialog(null, "Invalid Save File");
                return;
            } else {
                gameOver = Boolean.parseBoolean(gameOverStr);
            }

            br.close();

        } catch (FileNotFoundException e) {
            reset();
            JOptionPane.showMessageDialog(null, "Save File Could Not Be Found");
        } catch (IOException e) {
            reset();
            JOptionPane.showMessageDialog(null, "Loading failed");
        }
    }

    /**
     * Method that resets the game
     */
    public void reset() {
        gameOver = false;
        canSaveBlock = true;
        rowsToClear = new LinkedList<Integer>();
        queue = new LinkedList<Tetromino>();
        saved = null;
        falling = generateTetromino();
        for (int i = 0; i < 4; i++) {
            queue.add(generateTetromino());
        }

        board = new Block[20][10];
        linesCleared = 0;
        score = 0;
    }

    /**
     * Method that clears any rows that are completely filled
     */
    public void clear() {

        // check if there are lines to clear
        checkLinesToClear();

        // if there are not rows to clear, return
        if (rowsToClear.isEmpty()) {
            return;
        }

        // add score based on the number of lines cleared simultaneously
        int numLinesToClear = rowsToClear.size();
        if (numLinesToClear == 1) {
            score += 40;
        } else if (numLinesToClear == 2) {
            score += 100;
        } else if (numLinesToClear == 3) {
            score += 300;
        } else if (numLinesToClear == 4) {
            score += 1200;
        }

        linesCleared += numLinesToClear;

        // shift everything above the cleared row down by one
        while (!rowsToClear.isEmpty()) {
            int row = rowsToClear.remove();
            for (int j = 0; j < 10; j++) {
                board[row][j] = null;
            }
            shiftDownOne(row);
        }
    }

    /**
     * Helper method that checks if there are any lines to clear on the board, and
     * then store
     * those rows in the rowsToClear list (ordering matters since topmost full row
     * is removed first)
     */
    private void checkLinesToClear() {
        for (int i = 0; i < 20; i++) {
            boolean fullRow = true;
            // check if any of the cells in the row are empty
            for (int j = 0; j < 10; j++) {
                if (board[i][j] == null) {
                    fullRow = false;
                    break;
                }
            }

            // add this row to the list of rows to clear
            if (fullRow) {
                rowsToClear.add(i);
            }
        }
    }

    /**
     * Helper method that shifts everything above a specified row down by one
     * 
     * @param row The row that is removed
     */
    private void shiftDownOne(int row) {
        for (int i = row; i > 0; i--) {
            for (int j = 0; j < 10; j++) {
                if (board[i - 1][j] != null) {
                    board[i][j] = board[i - 1][j];
                    board[i][j].setRow(i);
                    board[i - 1][j] = null;
                }
            }
        }
    }

    /**
     * Method that sends the Tetromino down the board until it can no longer fall
     */
    public void fall() {

        // do nothing if the game is over
        if (gameOver) {
            return;
        }

        if (canFall(falling)) {
            falling.fall();
        } else {
            fillBoard(falling);
            clear();
            sendNextBlock();
        }
    }

    /**
     * Method that immediately sends tetromino as far down as possible
     */
    public void hardFall() {
        // do nothing if the game is over
        if (gameOver) {
            return;
        }

        while (canFall(falling)) {
            falling.fall();
        }
        fillBoard(falling);
        clear();
        sendNextBlock();
    }

    /**
     * Helper method that checks whether or not a Tetromino can continue to fall
     * in the grid
     * 
     * @param t The Tetromino we want to check if it can continue falling
     * @return boolean Whether or not the Tetromino can fall again
     */
    private boolean canFall(Tetromino t) {
        for (int i = 0; i < 4; i++) {
            Block curr = t.getBlock(i);
            int row = curr.getRow();
            int col = curr.getCol();
            if (row + 1 < 0) {
                continue;
            }
            if (row + 1 >= 20 || board[row + 1][col] != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method that moves the falling tetromino to the left
     */
    public void moveLeft() {
        // do nothing if the game is over
        if (gameOver) {
            return;
        }

        // check if tetromino can move left
        for (int i = 0; i < 4; i++) {
            Block curr = falling.getBlock(i);
            int row = curr.getRow();
            int col = curr.getCol();
            if (row < 0) {
                continue;
            }
            // do nothing if moving left goes out of bounds or intersects another block
            if (col - 1 < 0 || board[row][col - 1] != null) {
                return;
            }
        }
        falling.moveLeft();
    }

    /**
     * Method that moves the falling tetromino to the right
     */
    public void moveRight() {

        // do nothing if the game is over
        if (gameOver) {
            return;
        }

        // check if tetromino can move right
        for (int i = 0; i < 4; i++) {
            Block curr = falling.getBlock(i);
            int row = curr.getRow();
            int col = curr.getCol();
            // do nothing if moving right goes out of bounds or intersects another block
            if (row < 0) {
                continue;
            }
            if (col + 1 >= 10 || board[row][col + 1] != null) {
                return;
            }
        }
        falling.moveRight();
    }

    /**
     * Helper method that is called on a block that can no longer fall anymore, and
     * fills all the positions of the tetris board that the Tetromino is currently
     * occupying
     * 
     * @param t The Tetromino used to fill the grid
     */
    private void fillBoard(Tetromino t) {
        for (int i = 0; i < 4; i++) {
            Block curr = t.getBlock(i);
            int row = curr.getRow();
            int col = curr.getCol();

            // if a row is negative while trying to fill a cell in the board, game is over
            if (row < 0) {
                gameOver = true;
                return;
            }
            board[row][col] = curr;
        }
    }

    /**
     * method that dequeues from the head of the Tetromino queue to get our next
     * falling block
     */
    public void sendNextBlock() {
        // do nothing if the game is over
        if (gameOver) {
            return;
        }

        // set falling to the first item of the queue and remove the head of queue
        falling = queue.remove();

        // shift the tetramino up one while a block is blocking the spawn point
        while (spawnIntersectsGridBlocks()) {
            double xPivot = falling.getXPivot();
            double yPivot = falling.getYPivot();
            falling.setPivot(xPivot, yPivot - 1);
        }

        // add a new tetromino to the queue
        Tetromino tetGenerated = generateTetromino();
        queue.add(tetGenerated);

        // new round so we can save again
        canSaveBlock = true;
    }

    /**
     * Helper method that checks whether the spawn of the falling block is occupied
     * 
     * @return Boolean that represents whether or not spawn is blocked
     */
    private boolean spawnIntersectsGridBlocks() {
        for (int i = 0; i < 4; i++) {
            int row = falling.getBlock(i).getRow();
            int col = falling.getBlock(i).getCol();
            if (row < 0) {
                continue;
            }
            if (board[row][col] != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * method that saves a Tetromino that can be used later
     */
    public void saveBlock() {
        // do nothing if the game is over
        if (gameOver) {
            return;
        }

        if (saved == null) {
            saved = falling;
            falling.resetTetromino();
            sendNextBlock();
            canSaveBlock = false;
        } else if (canSaveBlock) {
            Tetromino temp = falling;
            falling = saved;
            falling.resetTetromino();
            saved = temp;
            canSaveBlock = false;
        }
    }

    /**
     * getter method that determines whether or not the game is over
     * 
     * @return boolean representing whether or not the game is over
     */
    public boolean gameIsOver() {
        return gameOver;
    }

    /**
     * Getter method for the current score of the game
     * 
     * @return The current score of the game
     */
    public int getScore() {
        return score;
    }

    /**
     * Getter method for the number of lines cleared in the current game
     * 
     * @return The total lines cleared in the game so far
     */
    public int getLinesCleared() {
        return linesCleared;
    }

    /**
     * Getter method for the saved tetromino block
     * 
     * @return Clone of the saved Tetromino block
     */
    public Tetromino getSavedTetromino() {
        if (saved == null) {
            return null;
        }

        return saved.clone();
    }

    /**
     * Getter method for the falling tetromino block
     * 
     * @return Clone of the falling tetromino block
     */
    public Tetromino getFallingTetromino() {
        return falling.clone();
    }

    /**
     * Getter method for the list of Tetromino blocks in the queue
     * 
     * @return LinkedList of Tetromino blocks in the queue
     */
    public LinkedList<Tetromino> getQueue() {
        return new LinkedList<Tetromino>(queue);
    }

    /**
     * Getter method for a copy of the tetris board
     * 
     * @return Copy of the Tetris board
     */
    public Block[][] getBoard() {
        Block[][] tetBoard = new Block[20][10];
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                tetBoard[i][j] = board[i][j];
            }
        }
        return tetBoard;
    }

    /**
     * Method used for testing: fills a block on the grid
     * 
     * @param row The row of the block to be filled
     * @param col The column of the block to be fileld
     */
    public void fill(int row, int col) {
        board[row][col] = new Block(row, col, "red");
    }

    /**
     * Helper method that generates a random Tetromino block based on assumption
     * of uniform distribution, and no reduction of spawn chance from previous
     * generated tetromino
     * 
     * @return Tetromino that is randomly generated
     */
    private Tetromino generateTetromino() {
        double numberGen = Math.random();
        if (numberGen < 1.0 / 7) {
            return new Tetromino('I');
        } else if (numberGen < 2.0 / 7) {
            return new Tetromino('O');
        } else if (numberGen < 3.0 / 7) {
            return new Tetromino('J');
        } else if (numberGen < 4.0 / 7) {
            return new Tetromino('T');
        } else if (numberGen < 5.0 / 7) {
            return new Tetromino('L');
        } else if (numberGen < 6.0 / 7) {
            return new Tetromino('S');
        } else {
            return new Tetromino('Z');
        }
    }

    /**
     * Draw all the blocks within the board
     * 
     * @param g The Graphics context we are drawing in
     */
    public void draw(Graphics g) {
        // draw all the blocks in the grid
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                if (board[i][j] != null) {
                    board[i][j].draw(g);
                }
            }
        }

        // draw the saved tetromino
        if (saved != null) {
            Tetromino savedDisplay = saved.clone();
            savedDisplay.setPivot(-4, 4);
            savedDisplay.draw(g);
        }

        // draw the tetrominoes in the queue
        int order = 1;
        for (Tetromino t : queue) {
            Tetromino display = t.clone();
            display.setPivot(13.5, 4 * order);
            display.draw(g);
            order++;
        }

        // draw text for score and lines cleared
        Graphics2D g2 = (Graphics2D) g;
        Font font = new Font("serif", Font.BOLD, 25);
        g2.setFont(font);
        g2.drawString("Lines Cleared: " + linesCleared, 10, 650);
        g2.drawString("Score: " + score, 10, 750);

        // draw the falling block
        falling.draw(g);
    }

    /**
     * method that tries to rotate the tetromino clockwise by testing if the
     * rotated
     * tetromino is in bounds and not intersecting any other placed blocks. If it is
     * not possible,
     * we will sequentially run series of tests based on the state the tetromino is
     * in (number of
     * clockwise rotation deviations from spawn)
     */
    public void rotateCW() {
        double xPivot = falling.getXPivot();
        double yPivot = falling.getYPivot();
        int state = falling.getState();
        if (canRotateCW(0, 0)) {
            falling.rotateCW();
        } else if (falling.getBlockChar() == 'I') {
            // if the falling block is I block, attempt to rotate it
            rotateCWIBlock();
        } else if (state == 0) {
            // Perform the following tests for blocks J, L, S, T, Z, and O (O does not
            // matter) run all the tests for state 0
            if (canRotateCW(-1, 0)) {
                falling.setPivot(xPivot - 1, yPivot);
                falling.rotateCW();
            } else if (canRotateCW(-1, -1)) {
                falling.setPivot(xPivot - 1, yPivot - 1);
                falling.rotateCW();
            } else if (canRotateCW(0, 2)) {
                falling.setPivot(xPivot, yPivot + 2);
                falling.rotateCW();
            } else if (canRotateCW(-1, 2)) {
                falling.setPivot(xPivot - 1, yPivot + 2);
                falling.rotateCW();
            }
        } else if (state == 1) {
            // run all the tests for state 1
            if (canRotateCW(1, 0)) {
                falling.setPivot(xPivot + 1, yPivot);
                falling.rotateCW();
            } else if (canRotateCW(1, 1)) {
                falling.setPivot(xPivot + 1, yPivot + 1);
                falling.rotateCW();
            } else if (canRotateCW(0, -2)) {
                falling.setPivot(xPivot, yPivot - 2);
                falling.rotateCW();
            } else if (canRotateCW(1, -2)) {
                falling.setPivot(xPivot + 1, yPivot - 2);
                falling.rotateCW();
            }
        } else if (state == 2) {
            // run all the tests for state 2
            if (canRotateCW(1, 0)) {
                falling.setPivot(xPivot + 1, yPivot);
                falling.rotateCW();
            } else if (canRotateCW(1, -1)) {
                falling.setPivot(xPivot + 1, yPivot - 1);
                falling.rotateCW();
            } else if (canRotateCW(0, 2)) {
                falling.setPivot(xPivot, yPivot + 2);
                falling.rotateCW();
            } else if (canRotateCW(1, 2)) {
                falling.setPivot(xPivot + 1, yPivot + 2);
                falling.rotateCW();
            }
        } else if (state == 3) {
            // run all the tests for state 3
            if (canRotateCW(-1, 0)) {
                falling.setPivot(xPivot - 1, yPivot);
                falling.rotateCW();
            } else if (canRotateCW(-1, 1)) {
                falling.setPivot(xPivot - 1, yPivot + 1);
                falling.rotateCW();
            } else if (canRotateCW(0, -2)) {
                falling.setPivot(xPivot, yPivot - 2);
                falling.rotateCW();
            } else if (canRotateCW(-1, -2)) {
                falling.setPivot(xPivot - 1, yPivot - 2);
                falling.rotateCW();
            }
        }

    }

    private void rotateCWIBlock() {
        double xPivot = falling.getXPivot();
        double yPivot = falling.getYPivot();
        int state = falling.getState();

        // run all the tests for state 0
        if (state == 0) {
            if (canRotateCW(-2, 0)) {
                falling.setPivot(xPivot - 2, yPivot);
                falling.rotateCW();
            } else if (canRotateCW(1, 0)) {
                falling.setPivot(xPivot + 1, yPivot);
                falling.rotateCW();
            } else if (canRotateCW(-2, 1)) {
                falling.setPivot(xPivot - 2, yPivot + 1);
                falling.rotateCW();
            } else if (canRotateCW(1, -2)) {
                falling.setPivot(xPivot + 1, yPivot - 2);
                falling.rotateCW();
            }
        } else if (state == 1) {
            // run all the tests for state 1
            if (canRotateCW(-1, 0)) {
                falling.setPivot(xPivot - 1, yPivot);
                falling.rotateCW();
            } else if (canRotateCW(2, 0)) {
                falling.setPivot(xPivot + 2, yPivot);
                falling.rotateCW();
            } else if (canRotateCW(-1, -2)) {
                falling.setPivot(xPivot - 1, yPivot - 2);
                falling.rotateCW();
            } else if (canRotateCW(2, 1)) {
                falling.setPivot(xPivot + 2, yPivot + 1);
                falling.rotateCW();
            }
        } else if (state == 2) {
            // run all the tests for state 2
            if (canRotateCW(2, 0)) {
                falling.setPivot(xPivot + 2, yPivot);
                falling.rotateCW();
            } else if (canRotateCW(-1, 0)) {
                falling.setPivot(xPivot - 1, yPivot);
                falling.rotateCW();
            } else if (canRotateCW(2, -1)) {
                falling.setPivot(xPivot + 2, yPivot - 1);
                falling.rotateCW();
            } else if (canRotateCW(-1, 2)) {
                falling.setPivot(xPivot - 1, yPivot + 2);
                falling.rotateCW();
            }
        } else if (state == 3) {
            // run all the tests for state 3
            if (canRotateCW(1, 0)) {
                falling.setPivot(xPivot + 1, yPivot);
                falling.rotateCW();
            } else if (canRotateCW(-2, 0)) {
                falling.setPivot(xPivot - 2, yPivot);
                falling.rotateCW();
            } else if (canRotateCW(1, 2)) {
                falling.setPivot(xPivot + 1, yPivot + 2);
                falling.rotateCW();
            } else if (canRotateCW(-2, -1)) {
                falling.setPivot(xPivot - 2, yPivot - 1);
                falling.rotateCW();
            }
        }

    }

    /**
     * Helper method that checks if the falling tetromino can rotate clockwise
     * when translated in
     * x and y directions
     * 
     * @param xKick The x direction to translate the tetromino
     * @param yKick The Y direction to translate the tetromino
     * @return boolean Whether the tetromino can rotate clockwise
     */
    private boolean canRotateCW(double xKick, double yKick) {
        Tetromino test = falling.clone();
        double xPivot = test.getXPivot();
        double yPivot = test.getYPivot();
        test.setPivot(xPivot + xKick, yPivot + yKick);
        test.rotateCW();
        for (int i = 0; i < 4; i++) {
            Block curr = test.getBlock(i);
            int row = curr.getRow();
            int col = curr.getCol();
            // check if rotation is in bound
            if (col < 0 || col >= 10 || row < 0 || row >= 20) {
                return false;
            }
            // check if rotation causes intersection with other placed blocks
            if (board[row][col] != null) {
                return false;
            }
        }
        return true;
    }

    public void rotateCCW() {
        double xPivot = falling.getXPivot();
        double yPivot = falling.getYPivot();
        int state = falling.getState();

        if (canRotateCCW(0, 0)) {
            falling.rotateCCW();
        } else if (falling.getBlockChar() == 'I') {
            // if the falling block is I block, attempt to rotate it
            rotateCCWIBlock();
        } else if (state == 0) {
            // Perform the following tests for blocks J, L, S, T, Z, and O (O does
            // not matter) run all the tests for state 0
            if (canRotateCCW(1, 0)) {
                falling.setPivot(xPivot + 1, yPivot);
                falling.rotateCCW();
            } else if (canRotateCCW(1, -1)) {
                falling.setPivot(xPivot + 1, yPivot - 1);
                falling.rotateCCW();
            } else if (canRotateCCW(0, 2)) {
                falling.setPivot(xPivot, yPivot + 2);
                falling.rotateCCW();
            } else if (canRotateCCW(1, 2)) {
                falling.setPivot(xPivot + 1, yPivot + 2);
                falling.rotateCCW();
            }
        } else if (state == 1) {
            // run all the tests for state 1
            if (canRotateCCW(1, 0)) {
                falling.setPivot(xPivot + 1, yPivot);
                falling.rotateCCW();
            } else if (canRotateCCW(1, 1)) {
                falling.setPivot(xPivot + 1, yPivot + 1);
                falling.rotateCCW();
            } else if (canRotateCCW(0, -2)) {
                falling.setPivot(xPivot, yPivot - 2);
                falling.rotateCCW();
            } else if (canRotateCCW(1, -2)) {
                falling.setPivot(xPivot + 1, yPivot - 2);
                falling.rotateCCW();
            }
        } else if (state == 2) {
            // run all the tests for state 2
            if (canRotateCCW(-1, 0)) {
                falling.setPivot(xPivot - 1, yPivot);
                falling.rotateCCW();
            } else if (canRotateCCW(-1, -1)) {
                falling.setPivot(xPivot - 1, yPivot - 1);
                falling.rotateCCW();
            } else if (canRotateCCW(0, 2)) {
                falling.setPivot(xPivot, yPivot + 2);
                falling.rotateCCW();
            } else if (canRotateCCW(-1, 2)) {
                falling.setPivot(xPivot - 1, yPivot + 2);
                falling.rotateCCW();
            }
        } else if (state == 3) {
            // run all the tests for state 3
            if (canRotateCCW(-1, 0)) {
                falling.setPivot(xPivot - 1, yPivot);
                falling.rotateCCW();
            } else if (canRotateCCW(-1, 1)) {
                falling.setPivot(xPivot - 1, yPivot + 1);
                falling.rotateCCW();
            } else if (canRotateCCW(0, -2)) {
                falling.setPivot(xPivot, yPivot - 2);
                falling.rotateCCW();
            } else if (canRotateCCW(-1, -2)) {
                falling.setPivot(xPivot - 1, yPivot - 2);
                falling.rotateCCW();
            }
        }
    }

    private void rotateCCWIBlock() {
        double xPivot = falling.getXPivot();
        double yPivot = falling.getYPivot();
        int state = falling.getState();

        // run all the tests for state 0
        if (state == 0) {
            if (canRotateCCW(-1, 0)) {
                falling.setPivot(xPivot - 1, yPivot);
                falling.rotateCCW();
            } else if (canRotateCCW(2, 0)) {
                falling.setPivot(xPivot + 2, yPivot);
                falling.rotateCCW();
            } else if (canRotateCCW(-1, -2)) {
                falling.setPivot(xPivot - 1, yPivot - 2);
                falling.rotateCCW();
            } else if (canRotateCCW(2, 1)) {
                falling.setPivot(xPivot + 2, yPivot + 1);
                falling.rotateCCW();
            }
        } else if (state == 1) {
            // run all the tests for state 1
            if (canRotateCW(2, 0)) {
                falling.setPivot(xPivot + 2, yPivot);
                falling.rotateCW();
            } else if (canRotateCW(-1, 0)) {
                falling.setPivot(xPivot - 1, yPivot);
                falling.rotateCW();
            } else if (canRotateCW(2, -1)) {
                falling.setPivot(xPivot + 2, yPivot - 1);
                falling.rotateCW();
            } else if (canRotateCW(-1, 2)) {
                falling.setPivot(xPivot - 1, yPivot + 2);
                falling.rotateCW();
            }
        } else if (state == 2) {
            // run all the tests for state 2
            if (canRotateCW(1, 0)) {
                falling.setPivot(xPivot + 1, yPivot);
                falling.rotateCW();
            } else if (canRotateCW(-2, 0)) {
                falling.setPivot(xPivot - 2, yPivot);
                falling.rotateCW();
            } else if (canRotateCW(1, 2)) {
                falling.setPivot(xPivot + 1, yPivot + 2);
                falling.rotateCW();
            } else if (canRotateCW(-2, -1)) {
                falling.setPivot(xPivot - 2, yPivot - 1);
                falling.rotateCW();
            }
        } else if (state == 3) {
            // run all the tests for state 3
            if (canRotateCW(-2, 0)) {
                falling.setPivot(xPivot - 2, yPivot);
                falling.rotateCW();
            } else if (canRotateCW(1, 0)) {
                falling.setPivot(xPivot + 1, yPivot);
                falling.rotateCW();
            } else if (canRotateCW(-2, 1)) {
                falling.setPivot(xPivot - 2, yPivot + 1);
                falling.rotateCW();
            } else if (canRotateCW(1, -2)) {
                falling.setPivot(xPivot + 1, yPivot - 2);
                falling.rotateCW();
            }
        }
    }

    /**
     * Helper method that checks if the falling tetromino can rotate
     * counter-clockwise when
     * translated in x and y directions
     * 
     * @param xKick The x direction to translate the tetromino
     * @param yKick The Y direction to translate the tetromino
     * @return boolean Whether the tetromino can rotate counter-clockwise
     */
    private boolean canRotateCCW(double xKick, double yKick) {
        Tetromino test = falling.clone();
        double xPivot = test.getXPivot();
        double yPivot = test.getYPivot();
        test.setPivot(xPivot + xKick, yPivot + yKick);
        test.rotateCCW();
        for (int i = 0; i < 4; i++) {
            Block curr = test.getBlock(i);
            int row = curr.getRow();
            int col = curr.getCol();
            System.out.println("Column: " + col);
            System.out.println("Row: " + row);
            // check if rotation is in bound
            if (col < 0 || col >= 10 || row < 0 || row >= 20) {
                return false;
            }

            // check if rotation causes intersection with other placed blocks
            if (board[row][col] != null) {
                return false;
            }
        }
        return true;
    }
}
