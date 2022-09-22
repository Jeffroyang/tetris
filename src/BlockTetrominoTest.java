
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;


public class BlockTetrominoTest {

    @Test
    public void testBlockConstructor() {
        Block b = new Block(0, 10, "red");
        assertEquals(0, b.getRow());
        assertEquals(10, b.getCol());
        assertEquals("red", b.getColorString());
    }

    @Test
    public void testGetColorString() {
        Block b = new Block(Color.RED);
        assertEquals("red", b.getColorString());
    }

    @Test
    public void testConstructorIBlock() {
        Tetromino t = new Tetromino('I');
        assertEquals(t.getBlockChar(), 'I');
        assertEquals(t.getXPivot(), 4.5, 0.01);
        assertEquals(t.getYPivot(), 1.5, 0.01);

    }

    @Test
    public void testConstructorOBlock() {
        Tetromino t = new Tetromino('O');
        assertEquals(t.getBlockChar(), 'O');
        assertEquals(t.getXPivot(), 4.5, 0.01);
        assertEquals(t.getYPivot(), 0.5, 0.01);

    }

    @Test
    public void testConstructorSBlock() {
        Tetromino t = new Tetromino('S');
        assertEquals(t.getBlockChar(), 'S');
        assertEquals(t.getXPivot(), 4, 0.01);
        assertEquals(t.getYPivot(), 1, 0.01);

    }

    @Test
    public void testConstructorJBlock() {
        Tetromino t = new Tetromino('J');
        assertEquals(t.getBlockChar(), 'J');
        assertEquals(t.getXPivot(), 4, 0.01);
        assertEquals(t.getYPivot(), 1, 0.01);

    }

    @Test
    public void testEqualTetrominoes() {
        Tetromino t1 = new Tetromino('I');
        Tetromino t2 = new Tetromino('I');
        assertEquals(t1, t2, "same character");

        t1.rotateCCW();
        t2.rotateCCW();
        assertEquals(t1, t2, "after rotation");

        t1.rotateCCW();
        t2.rotateCCW();
        assertEquals(t1, t2, "after two rotations");

        t1.setPivot(0, 10);
        t2.setPivot(0, 10);
        assertEquals(t1, t2, "after translation");
    }

    @Test
    public void testTetrominoClone() {
        Tetromino t = new Tetromino('I');
        Tetromino clone = t.clone();
        assertEquals(clone, t);
    }

    @Test
    public void testTetrominoCloneAfterRotation() {
        Tetromino t = new Tetromino('Z');
        t.rotateCW();
        Tetromino clone = t.clone();
        assertEquals(clone, t);
    }

    @Test
    public void testTetrominoCloneAfterTranslations() {
        Tetromino t = new Tetromino('Z');
        t.setPivot(10, 40);
        Tetromino clone = t.clone();
        assertEquals(clone, t);
    }

    @Test
    public void testTetrominoTranslationCloneEncapsulation() {
        Tetromino t = new Tetromino('Z');
        Tetromino clone = t.clone();
        t.setPivot(10, 40);
        assertFalse(clone.equals(t));
    }

    @Test
    public void testTetrominoRotationCloneEncapsulation() {
        Tetromino t = new Tetromino('Z');
        Tetromino clone = t.clone();
        t.rotateCW();
        assertFalse(clone.equals(t));
    }

    @Test
    public void testFall() {
        // all I blocks start on row 1
        Tetromino t = new Tetromino('I');
        double yPivot = t.getYPivot();
        t.fall();

        for (int i = 0; i < 4; i++) {
            Block curr = t.getBlock(i);
            int row = curr.getRow();
            assertEquals(2, row);
        }

        assertEquals(yPivot + 1, t.getYPivot(), 0.01);
    }

    @Test
    public void testMoveRight() {
        Tetromino t = new Tetromino('I');
        double xPivot = t.getXPivot();
        t.moveRight();
        assertEquals(xPivot + 1, t.getXPivot(), 0.01);

    }

    @Test
    public void testMoveLeft() {
        Tetromino t = new Tetromino('I');
        double xPivot = t.getXPivot();
        t.moveLeft();
        assertEquals(xPivot - 1, t.getXPivot(), 0.01);
    }

    @Test
    public void testRotateCW() {
        Tetromino t = new Tetromino('I');
        t.rotateCW();
        boolean[][] bSpace1 = t.getBlockSpace();
        assertTrue(bSpace1[0][2]);
        assertTrue(bSpace1[1][2]);
        assertTrue(bSpace1[2][2]);
        assertTrue(bSpace1[3][2]);

        t.rotateCW();
        boolean[][] bSpace2 = t.getBlockSpace();
        assertTrue(bSpace2[2][0]);
        assertTrue(bSpace2[2][1]);
        assertTrue(bSpace2[2][2]);
        assertTrue(bSpace2[2][3]);

        t.rotateCW();
        boolean[][] bSpace3 = t.getBlockSpace();
        assertTrue(bSpace3[0][1]);
        assertTrue(bSpace3[1][1]);
        assertTrue(bSpace3[2][1]);
        assertTrue(bSpace3[3][1]);
    }

    @Test
    public void testRotateCCW() {
        Tetromino t = new Tetromino('I');

        t.rotateCCW();
        boolean[][] bSpace1 = t.getBlockSpace();
        assertTrue(bSpace1[0][1]);
        assertTrue(bSpace1[1][1]);
        assertTrue(bSpace1[2][1]);
        assertTrue(bSpace1[3][1]);

        t.rotateCCW();
        boolean[][] bSpace2 = t.getBlockSpace();
        assertTrue(bSpace2[2][0]);
        assertTrue(bSpace2[2][1]);
        assertTrue(bSpace2[2][2]);
        assertTrue(bSpace2[2][3]);

        t.rotateCCW();
        boolean[][] bSpace3 = t.getBlockSpace();
        assertTrue(bSpace3[0][2]);
        assertTrue(bSpace3[1][2]);
        assertTrue(bSpace3[2][2]);
        assertTrue(bSpace3[3][2]);
    }

}
