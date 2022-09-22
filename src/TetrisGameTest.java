

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * You can use this file (and others) to test your
 * implementation.
 */

public class TetrisGameTest {

    @Test
    public void testConstructor() {
        Tetris t = new Tetris();
        assertFalse(t.gameIsOver());
        assertEquals(4, t.getQueue().size());
        assertNull(t.getSavedTetromino());
        Block[][] board = t.getBoard();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                assertNull(board[i][j]);
            }
        }
        assertEquals(0, t.getScore());
        assertEquals(0, t.getLinesCleared());
    }

    @Test
    public void testClearEntireBoard() {
        Tetris t = new Tetris();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                t.fill(i, j);
            }
        }
        t.clear();
        Block[][] board = t.getBoard();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                assertNull(board[i][j]);
            }
        }
        assertEquals(20, t.getLinesCleared());
    }

    @Test
    public void testClearNoFullLines() {
        Tetris t = new Tetris();
        t.fill(0, 1);
        t.fill(3, 2);
        t.fill(19, 9);
        t.fill(18, 5);
        t.clear();
        Block[][] board = t.getBoard();
        Assertions.assertNotNull(board[0][1]);
        Assertions.assertNotNull(board[3][2]);
        Assertions.assertNotNull(board[19][9]);
        Assertions.assertNotNull(board[18][5]);

    }

    @Test
    public void testClearOneLineBottom() {
        Tetris t = new Tetris();
        for (int i = 0; i < 10; i++) {
            t.fill(19, i);
        }
        t.clear();
        Block[][] board = t.getBoard();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                assertNull(board[i][j]);
            }
        }
        assertEquals(40, t.getScore());
        assertEquals(1, t.getLinesCleared());
    }

    @Test
    public void testClearOneLineMiddle() {
        Tetris t = new Tetris();
        for (int i = 0; i < 10; i++) {
            t.fill(10, i);
        }
        t.clear();
        Block[][] board = t.getBoard();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                assertNull(board[i][j]);
            }
        }
        assertEquals(40, t.getScore());
        assertEquals(1, t.getLinesCleared());
    }

    @Test
    public void testClearOneLineTop() {
        Tetris t = new Tetris();
        for (int i = 0; i < 10; i++) {
            t.fill(0, i);
        }
        t.clear();
        Block[][] board = t.getBoard();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                assertNull(board[i][j]);
            }
        }
        assertEquals(40, t.getScore());
        assertEquals(1, t.getLinesCleared());
    }

    @Test
    public void testClearTwoLines() {
        Tetris t = new Tetris();
        for (int i = 0; i < 10; i++) {
            t.fill(19, i);
            t.fill(18, i);
        }
        t.clear();
        Block[][] board = t.getBoard();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                assertNull(board[i][j]);
            }
        }
        assertEquals(100, t.getScore());
        assertEquals(2, t.getLinesCleared());
    }

    @Test
    public void testClearThreeLines() {
        Tetris t = new Tetris();
        for (int i = 0; i < 10; i++) {
            t.fill(19, i);
            t.fill(18, i);
            t.fill(17, i);
        }
        t.clear();
        Block[][] board = t.getBoard();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                assertNull(board[i][j]);
            }
        }
        assertEquals(300, t.getScore());
        assertEquals(3, t.getLinesCleared());
    }

    @Test
    public void testClearFourLines() {
        Tetris t = new Tetris();
        for (int i = 0; i < 10; i++) {
            t.fill(19, i);
            t.fill(18, i);
            t.fill(17, i);
            t.fill(16, i);
        }
        t.clear();
        Block[][] board = t.getBoard();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                assertNull(board[i][j]);
            }
        }
        assertEquals(1200, t.getScore());
        assertEquals(4, t.getLinesCleared());
    }

    @Test
    public void testClearNonFullRowsShiftDown() {
        Tetris t = new Tetris();
        for (int i = 0; i < 10; i++) {
            t.fill(19, i);
        }
        t.fill(18, 3);
        t.fill(18, 5);
        t.clear();
        Block[][] board = t.getBoard();
        assertNull(board[18][3]);
        assertNull(board[18][5]);

        Assertions.assertNotNull(board[19][3]);
        Assertions.assertNotNull(board[19][5]);
        assertEquals(40, t.getScore());
        assertEquals(1, t.getLinesCleared());
    }

    @Test
    public void testClearMultipleNonFullRowsShiftDown() {
        Tetris t = new Tetris();
        for (int i = 0; i < 10; i++) {
            t.fill(19, i);
        }
        t.fill(18, 3);
        t.fill(18, 5);
        t.fill(14, 7);
        t.fill(13, 9);
        t.clear();
        Block[][] board = t.getBoard();
        assertNull(board[18][3]);
        assertNull(board[18][5]);
        assertNull(board[14][7]);
        assertNull(board[13][9]);
        Assertions.assertNotNull(board[19][3]);
        Assertions.assertNotNull(board[19][5]);
        Assertions.assertNotNull(board[15][7]);
        Assertions.assertNotNull(board[14][9]);
        assertEquals(40, t.getScore());
        assertEquals(1, t.getLinesCleared());
    }

    @Test
    public void testFall() {
        Tetris t = new Tetris();
        Tetromino fallingInitial = t.getFallingTetromino();
        double y1 = fallingInitial.getYPivot();

        t.fall();
        Tetromino fallingFinal = t.getFallingTetromino();
        double y2 = fallingFinal.getYPivot();
        assertEquals(y1 + 1, y2, 0.01);
    }

    @Test
    public void testFallCannotFall() {
        Tetris t = new Tetris();
        Tetromino fallingInitial = t.getFallingTetromino();
        double y1 = fallingInitial.getYPivot();

        int yBlock = (int) (y1 + 1);

        for (int i = 0; i < 10; i++) {
            t.fill(yBlock, i);
        }
        t.fall();
        Tetromino fallingFinal = t.getFallingTetromino();
        double y2 = fallingFinal.getYPivot();
        Assertions.assertNotEquals(y1 + 1, y2);
    }

    @Test
    public void testHardFall() {
        Tetris t = new Tetris();

        t.hardFall();
        Block[][] board = t.getBoard();
        boolean bottomRowAllNull = true;
        for (int i = 0; i < 10; i++) {
            if (board[19][i] != null) {
                bottomRowAllNull = false;
            }
        }

        if (bottomRowAllNull) {
            Assertions.fail();
        }
    }

    @Test
    public void testMoveRight() {
        Tetris t = new Tetris();
        Tetromino initial = t.getFallingTetromino();
        double x1 = initial.getXPivot();
        t.moveRight();
        Tetromino after = t.getFallingTetromino();
        double x2 = after.getXPivot();
        assertEquals(x1 + 1, x2, 0.01);
    }

    @Test
    public void testMoveRightBlocked() {
        Tetris t = new Tetris();
        Tetromino initial = t.getFallingTetromino();
        double x1 = initial.getXPivot();
        for (int i = 0; i < 20; i++) {
            t.fill(i, 5);
        }

        t.moveRight();
        Tetromino after = t.getFallingTetromino();
        double x2 = after.getXPivot();
        assertEquals(x1, x2, 0.01);
    }

    @Test
    public void testMoveLeft() {
        Tetris t = new Tetris();
        Tetromino initial = t.getFallingTetromino();
        double x1 = initial.getXPivot();
        t.moveLeft();
        Tetromino after = t.getFallingTetromino();
        double x2 = after.getXPivot();
        assertEquals(x1 - 1, x2, 0.01);
    }

    @Test
    public void testMoveLeftBlocked() {
        Tetris t = new Tetris();
        Tetromino initial = t.getFallingTetromino();
        double x1 = initial.getXPivot();
        for (int i = 0; i < 20; i++) {
            t.fill(i, 3);
        }

        t.moveLeft();
        Tetromino after = t.getFallingTetromino();
        double x2 = after.getXPivot();
        assertEquals(x1, x2, 0.01);
    }

    @Test
    public void testSaveBlockCurrentlyNull() {
        Tetris t = new Tetris();
        Tetromino t1 = t.getFallingTetromino();
        t.saveBlock();
        Tetromino t2 = t.getSavedTetromino();
        assertEquals(t1, t2);
    }

    @Test
    public void testSaveBlockSavingDifferentBlock() {
        Tetris t = new Tetris();
        Tetromino t1 = t.getFallingTetromino();
        t.saveBlock();
        t.sendNextBlock();

        Tetromino t2 = t.getFallingTetromino();
        t.saveBlock();

        Tetromino t3 = t.getSavedTetromino();
        Tetromino t4 = t.getFallingTetromino();
        assertEquals(t2, t3);
        assertEquals(t1, t4);
    }

    @Test
    public void testGameOver() {
        Tetris t = new Tetris();
        t.fill(0, 4);
        t.fill(0, 5);
        t.fill(1, 4);
        t.fill(1, 5);
        t.fill(2, 4);
        t.fill(2, 5);
        t.fill(3, 4);
        t.fill(3, 5);
        t.sendNextBlock();
        t.hardFall();
        assertTrue(t.gameIsOver());
    }

}
