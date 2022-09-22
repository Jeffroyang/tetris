

import javax.swing.*;

public class Game {
    public static void main(String[] args) {
        Runnable game = new RunTetris();
        SwingUtilities.invokeLater(game);
    }
}
