

import java.util.*;

class Board {
    final static int MINE = 9;
    final static int COVERED = 10;

    // Board cells are represented by integers.
    // Specifically, the cell at row i and column j is represented by the integer c = i*width + j
    private final int width;
    private final int height;
    // The set of cell locations containing a mine
    private final Set<Integer> mines;
    // If mineField[c] == MINE, cell c has a mine
    // Otherwise, c has no mines and mineField[c] is the number of mines next to c
    private final int[] mineField;
    // clearedCells[c] with a value of false means c is covered up and true means it has been cleared.
    // All cells start out covered and become cleared through the clear() (and swept()) functions.
    // Note: this variable used to be named "sweptCells" and has been renamed
    private final boolean[] clearedCells;

    Board(int width, int height, int numMines) {
        int n = width*height;
        this.width = width;
        this.height = height;
        this.mines = new HashSet<>();
        {   // generate mine locations
            if (numMines >= n)
                throw new AssertionError("too many mines for the size of this board");
            Random random = new Random();
            for (int k = 0; k < numMines; k++) {
                int c = random.nextInt(n);
                while (mines.contains(c))
                    c = random.nextInt(n);
                mines.add(c);
            }
        }
        this.mineField = new int[n];
        {   // create mine field
            for (int c : mines)
                mineField[c] = MINE;
            for (int c : mines)
                for (int a : adjacentCells(c))
                    if (mineField[a] != MINE)
                        mineField[a] += 1;
        }
        this.clearedCells = new boolean[n];
    }

    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
    }

    int[][] getClearedView() {
        return reshape(mineField, width);
    }

    int[][] getCoveredView() {
        int[] coverField = cover(mineField, clearedCells);
        return reshape(coverField, width);
    }

    boolean allClear() {
        int unclear = height*width;
        for (boolean swept : clearedCells)
            if (swept)
                unclear--;
        return unclear <= mines.size();
    }

    // the function that is called when a user clicks on cell c
    boolean clear(Integer c) {
        if (mines.contains(c))
            return false;
        sweep(c);
        return true;
    }

    private void sweep(Integer c) {
        // TODO: implement sweeping by clearing all the cells in the same region as c.
        clearedCells[c] = true; // set to true to clear cell c
        // Create a set for our cells 
        Set<Integer> neighboringCells = new HashSet<>(); 
        // add them to variable named neighboringCells  
         neighboringCells = adjacentCells(c);
         // Initialize boolean to be used to determine neighbor is a MINE
         boolean nieghbor_MINE = false;
         for (int i: neighboringCells)
         {
         	// If mineField[i] == MINE, then our neighbor has a mine.
         	if (mineField[i] == MINE)
         	{
         		nieghbor_MINE = true;
         	}   
         	else{
         		// Check that cell is not cleared & neighbor is a MINE
         		if (!clearedCells[i] && nieghbor_MINE) 
         		{
         			// clear cell & recursively call sweep.
         			clearedCells[i] = true;
         			sweep(i);
         		}
         	}
         }
    }
    private Set<Integer> adjacentCells(int c) {
        Set<Integer> cells = new HashSet<>();
        int i = c/width;
        int j = c%width;
        for (int ii = Integer.max(0, i - 1); ii < Integer.min(height, i + 2); ii++)
            for (int jj = Integer.max(0, j - 1); jj < Integer.min(width, j + 2); jj++)
                cells.add(ii*width+jj);
        cells.remove(i*width + j);
        return cells;
    }

    private static int[] cover(int[] board, boolean[] sweptCells) {
        int[] covered = Arrays.copyOf(board, board.length);
        for (int c = 0; c < board.length; c++)
            if (!sweptCells[c])
                covered[c] = COVERED;
        return covered;
    }

    private static int[][] reshape(int[] v, int n) {
        int[][] m = new int[v.length/n][n];
        for (int i = 0; i < v.length/n; i++)
            System.arraycopy(v, i*n, m[i], 0, n);
        return m;
    }
}
