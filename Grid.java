import java.awt.*;
import javax.swing.*;

public class Grid  {
    private boolean[][] bombGrid;
    private int[][] countGrid;
    private  int numRows;
    private  int numColumns;
    private  int numBombs;

    public Grid() {
        numRows = 10;
        numColumns = 10;
        numBombs = 25;
        createBombGrid();
        createCountGrid();
        GUI();
    }
    public Grid(int rows, int cols) {
        numColumns = cols;
        numRows = rows;
        numBombs = 25;
        createBombGrid();
        createCountGrid();
        GUI();
    }
    public Grid(int rows, int cols, int bombs) {
        numColumns = cols;
        numRows = rows;
        numBombs = bombs;
        createBombGrid();
        createCountGrid();
        GUI();
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumBombs() {
        return numBombs;
    }

    public int getNumColumns() {
        return numColumns;
    }

    public boolean[][] getBombGrid() {
        boolean[][] cloneBombGrid = new boolean[numRows][numColumns];
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numColumns; col++) {
                cloneBombGrid[row][col] = bombGrid[row][col];
            }
        }
        return cloneBombGrid;
    }

    public int[][] getCountGrid() {
        int[][] cloneCountGrid = new int[numRows][numColumns];
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numColumns; col++) {
                cloneCountGrid[row][col] = countGrid[row][col];
            }
        }
        return cloneCountGrid;
    }

    public boolean isBombAtLocation(int row, int col) {
        return bombGrid[row][col];
    }

    public int getCountAtLocation(int row, int col) {
        return countGrid[row][col];
    }

    private void createBombGrid() {
        bombGrid =  new boolean[numRows][numColumns];
        int currentBombs = 0;
        while (currentBombs < numBombs) {
            int row = (int)(Math.random() * numRows);
            int col = (int)(Math.random() * numColumns);
            if (!bombGrid[row][col]) {
                bombGrid[row][col] = true;
                currentBombs++;
            }
        }
    }

    private void createCountGrid() {
        countGrid = new int[numRows][numColumns];
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numColumns; col++) {
                if (row - 1 >= 0) {
                    if (col - 1 >= 0 && isBombAtLocation(row - 1, col - 1)) {
                        countGrid[row][col] += 1;
                    }
                    if (isBombAtLocation(row - 1, col)) {
                        countGrid[row][col] += 1;
                    }
                    if (col + 1 < countGrid[row].length && isBombAtLocation(row - 1, col + 1)) {
                        countGrid[row][col] += 1;
                    }
                }
                if (isBombAtLocation(row, col)) {
                    countGrid[row][col] += 1;
                }
                if (col + 1 < countGrid[row].length && isBombAtLocation(row, col + 1)) {
                    countGrid[row][col] += 1;
                }
                if (col - 1 >= 0 && isBombAtLocation(row, col - 1)) {
                    countGrid[row][col] += 1;
                }

                if (row + 1 < numRows) {
                    if (col - 1 >= 0 && isBombAtLocation(row + 1, col - 1)) {
                        countGrid[row][col] += 1;
                    }
                    if (isBombAtLocation(row + 1, col)) {
                        countGrid[row][col] += 1;
                    }
                    if (col + 1 < numColumns && isBombAtLocation(row + 1, col + 1)) {
                        countGrid[row][col] += 1;
                    }
                }
            }
        }
    }

    public void GUI() {
        JFrame grid = new JFrame("Minesweeper");
        grid.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        grid.setLayout(new GridLayout(numRows, numColumns));
        JButton[][] buttons = new JButton[numRows][numColumns];
        //i had to make some variables effectively final to work in lambda action listener
        //the array for gameOver & noBombs is also so i can update value without issue (tip online)
        final boolean[] gameOver = {false};
        final int[] noBombs = { (numRows * numColumns) - numBombs };
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numColumns; col++) {
                buttons[row][col] = new JButton();
                final int r = row;
                final int c = col;
                buttons[r][c].addActionListener(e -> {
                        if (gameOver[0] || !buttons[r][c].isEnabled()) {
                            return;
                        }

                        if (isBombAtLocation(r, c)) {
                            gameOver[0] = true;
                            for (int R = 0; R < numRows; R++) {
                                for (int C = 0; C < numColumns; C++) {
                                    buttons[R][C].setEnabled(false);

                                    if (isBombAtLocation(R, C)) {
                                        buttons[R][C].setText("💣");
                                    } else {
                                        buttons[R][C].setText(String.valueOf(getCountAtLocation(R, C)));
                                    }
                                }
                            }
                            int choice = JOptionPane.showConfirmDialog(grid, "*Explosion* You lose!\nPlay again?", "Game Over", JOptionPane.YES_NO_OPTION);
                            if (choice == JOptionPane.YES_OPTION) {
                                grid.dispose();
                                new Grid(numRows, numColumns, numBombs);
                            } else {
                                System.exit(0);
                            }
                        } else {
                            buttons[r][c].setText(String.valueOf(getCountAtLocation(r, c)));
                            buttons[r][c].setEnabled(false);
                            noBombs[0]--;
                            if (noBombs[0] == 0) {
                                gameOver[0] = true;
                                int choice = JOptionPane.showConfirmDialog(grid, "All bombs dodged. You win!\nPlay again?", "Game Won", JOptionPane.YES_NO_OPTION);
                                if (choice == JOptionPane.YES_OPTION) {
                                    grid.dispose();
                                    new Grid(numRows, numColumns, numBombs);
                                } else {
                                    System.exit(0);
                                }
                            }
                        }
                    });
                grid.getContentPane().add(buttons[row][col]);
            }
        }
        grid.pack();
        grid.setVisible(true);
    }

    public static void main(String[] args) {
        System.out.println("How many rows?");


    }
}
