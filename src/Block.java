

import java.awt.*;

public class Block {

    // INVARIANT: color can be yellow, cyan, blue, orange, magenta, green, or red
    // (for loading game)
    private Color color;
    private int row;
    private int column;

    /**
     * Constructor for the block object
     * 
     * @param color The color of the block
     */
    public Block(Color color) {
        this.color = color;
        row = 0;
        column = 0;
    }

    /**
     * Constructor that sets a block to a given row, column, and String color; Used
     * when loading in
     * a saved game
     * 
     * @param row   The row the block is in
     * @param col   The col the block is in
     * @param color The color of the block
     */
    public Block(int row, int col, String color) {
        this.row = row;
        this.column = col;
        if (color.equals("yellow")) {
            this.color = Color.YELLOW;
        } else if (color.equals("cyan")) {
            this.color = Color.CYAN;
        } else if (color.equals("blue")) {
            this.color = Color.BLUE;
        } else if (color.equals("orange")) {
            this.color = Color.ORANGE;
        } else if (color.equals("magenta")) {
            this.color = Color.MAGENTA;
        } else if (color.equals("green")) {
            this.color = Color.GREEN;
        } else if (color.equals("red")) {
            this.color = Color.RED;
        }
    }

    /**
     * getter method for the row the block is in
     * 
     * @return int Row the block is in
     */
    public int getRow() {
        return row;

    }

    /**
     * Getter method for the column the block is in
     * 
     * @return int Column the block is in
     */
    public int getCol() {
        return column;
    }

    /**
     * Setter method for the row of the block
     * 
     * @param r The row to set the block to
     */
    public void setRow(int r) {
        row = r;
    }

    /**
     * setter method for the column of the block
     * 
     * @param c The column to set the block to
     */
    public void setCol(int c) {
        column = c;
    }

    /**
     * getter method for the String name of the block color
     * 
     * @return String for the color of the block
     */
    public String getColorString() {
        if (color.equals(Color.YELLOW)) {
            return "yellow";
        } else if (color.equals(Color.CYAN)) {
            return "cyan";
        } else if (color.equals(Color.BLUE)) {
            return "blue";
        } else if (color.equals(Color.ORANGE)) {
            return "orange";
        } else if (color.equals(Color.MAGENTA)) {
            return "magenta";
        } else if (color.equals(Color.GREEN)) {
            return "green";
        } else if (color.equals(Color.RED)) {
            return "red";
        } else {
            return null;
        }
    }

    /**
     * Draws the block based on the column and row it is in, and draw a border
     * around it
     * 
     * @param g The Graphics context we are drawing in
     */
    public void draw(Graphics g) {

        // draw the block
        g.setColor(color);
        g.fillRect(column * 45 + 250, row * 45, 45, 45);

        // draw a border around the block
        g.setColor(Color.WHITE);
        // top line
        g.drawLine(column * 45 + 250, row * 45, (column + 1) * 45 + 250, row * 45);
        // left line
        g.drawLine(column * 45 + 250, row * 45, column * 45 + 250, (row + 1) * 45);
        // right line
        g.drawLine((column + 1) * 45 + 250, row * 45, (column + 1) * 45 + 250, (row + 1) * 45);
        // bottom line
        g.drawLine(column * 45 + 250, (row + 1) * 45, (column + 1) * 45 + 250, (row + 1) * 45);
    }

}
