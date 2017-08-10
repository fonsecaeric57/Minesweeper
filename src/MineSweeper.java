

class MineSweeper {
    private final Board board;
    private boolean running;
    private boolean won;

    MineSweeper(int width, int height, int numMines) {
        board = new Board(width, height, numMines);
        running = true;
        won = false;
    }

    Board getBoard() {
        return board;
    }

    boolean isRunning() {
        return running;
    }

    boolean isWon() {
        return won;
    }

    void update(int c) {
        if (!running) return;
        if (!board.clear(c)) {
            running = false;
        } else if (board.allClear()) {
            running = false;
            won = true;
        }
    }
}
