

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private Tetris tetris; // model for the game
    private JLabel status; // current status text
    private boolean paused;
    private Timer timer;
    private Timer fastTimer;

    // Game constants
    public static final int BOARD_WIDTH = 1000;
    public static final int BOARD_HEIGHT = 900;

    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        tetris = new Tetris();
        status = statusInit; // initializes the status JLabel

        timer = new Timer(500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });

        fastTimer = new Timer(50, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start(); // starts the normal timer

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT && !paused) {
                    tetris.moveLeft();
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && !paused) {
                    tetris.moveRight();
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE && !paused) {
                    tetris.hardFall();
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN && !paused) {
                    // stops the normal timer
                    timer.stop();
                    // starts the fast timer
                    fastTimer.start();
                } else if (e.getKeyChar() == 'x' || e.getKeyChar() == 'X' && !paused) {
                    tetris.rotateCW();
                } else if (e.getKeyChar() == 'z' || e.getKeyChar() == 'Z' && !paused) {
                    tetris.rotateCCW();
                } else if (e.getKeyChar() == 'c' || e.getKeyChar() == 'C' && !paused) {
                    tetris.saveBlock();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    pause();
                }
                repaint();
            }

            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    // stop the fast timer and start the normal timer
                    fastTimer.stop();
                    timer.start();
                }
            }
        });
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        tetris.reset();
        status.setText("Playing...");
        paused = false;
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     * Pauses the game and saves the games current state
     */
    public void save() {
        if (!paused) {
            paused = true;
            status.setText("Paused");
        }
        tetris.save();
    }

    /**
     * Loads a previously saved game
     */
    public void load() {
        reset();
        tetris = new Tetris("files/saved_tetris_game.txt");
        pause();
        repaint();
    }

    public void pause() {
        paused = !paused;
        if (paused) {
            status.setText("Paused");
        } else {
            status.setText("Playing...");
        }
        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    public void tick() {
        if (tetris.gameIsOver()) {
            status.setText("Game Over");
            return;
        } else if (!paused) {
            tetris.fall();
        }
        repaint();
    }

    /**
     * Draws the game board.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // draws the tetris game
        tetris.draw(g);

        // draw the lines of the grid
        g.setColor(Color.WHITE);
        for (int i = 0; i <= 10; i++) {
            g.drawLine(250 + 45 * i, 0, 250 + 45 * i, 900);
        }

        for (int i = 0; i < 21; i++) {
            g.drawLine(250, 45 * i, 700, 45 * i);
        }

        // draw instructions
        Graphics2D g2 = (Graphics2D) g;
        Font font = new Font("serif", Font.BOLD, 14);
        g2.setFont(font);
        g2.drawString("Instruction: ", 10, 350);
        g2.drawString("Press Right to move Right", 10, 375);
        g2.drawString("Press Left to move Left", 10, 400);
        g2.drawString("Press 'Z' to rotate Counter-Clockwise ", 10, 425);
        g2.drawString("Press 'X' to rotate Clockwise ", 10, 450);
        g2.drawString("Press 'C' to hold ", 10, 475);
        g2.drawString("Press SpaceBar to Hard Drop ", 10, 500);
        g2.drawString("Press Down Arrow Key to Fast Drop", 10, 525);
        g2.drawString("Press ESC to move Pause", 10, 550);

        // draw the border around the score, lines cleared, and the saved tetromino
        g.drawRect(0, 600, 250, 300);
        g.drawRect(0, 75, 250, 250);
    }

    /**
     * Returns the size of the game board
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
