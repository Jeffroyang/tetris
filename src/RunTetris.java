import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class RunTetris implements Runnable {
    public void run() {

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Tetris");
        frame.setLocation(0, 0);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        // Game board
        final GameBoard board = new GameBoard(status);

        board.setBackground(Color.BLACK);
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        final JButton reset = new JButton("Restart");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.reset();
            }
        });

        final JButton pause = new JButton("Pause/Resume");
        pause.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.pause();
            }
        });

        final JButton save = new JButton("Save");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.save();
            }
        });

        final JButton load = new JButton("Load Save");
        load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.load();
            }
        });

        control_panel.add(reset);
        control_panel.add(pause);
        control_panel.add(save);
        control_panel.add(load);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        board.reset();
    }
}