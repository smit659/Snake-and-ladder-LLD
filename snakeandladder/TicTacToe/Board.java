package snakeandladder.TicTacToe;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Board {
    private final int rows, cols;
    private Character[][] board;
    private int count = 0;
    private final ExecutorService executorService;

    Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        board = new Character[rows][cols];
        executorService = Executors.newFixedThreadPool(3);
    }

    // Bug fix #1: was "row<row && col<col" (always false due to shadowing)
    public boolean isValid(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols && board[row][col] == null;
    }

    public Character getSymbol(int row, int col) {
        return board[row][col];
    }

    public void setSymbol(int row, int col, Character symbol) {
        board[row][col] = symbol;
        count++;
    }

    // Bug fix #5: null cells caused NPE; replaced with '.'
    public void printBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print((board[i][j] == null ? "." : board[i][j]) + " ");
            }
            System.out.println();
        }
    }

    public boolean isBoardFull() {
        return count == rows * cols;
    }

    public boolean checkWinner(Character symbol, int row, int col) throws InterruptedException, ExecutionException {
        Future<Boolean> future1 = executorService.submit(() -> checkColWin(symbol, col));
        Future<Boolean> future2 = executorService.submit(() -> checkDiagonalWin(symbol));
        Future<Boolean> future3 = executorService.submit(() -> checkRowWin(symbol, row));
        return future1.get() || future2.get() || future3.get();
    }

    // Bug fix #1: loop bound was j<col (param vs param, always false) — now uses cols field
    private boolean checkRowWin(Character symbol, int row) {
        for (int j = 0; j < cols; j++) {
            if (board[row][j] != symbol) return false;
        }
        return true;
    }

    // Bug fix #1: loop bound was i<row (param vs param, always false) — now uses rows field
    private boolean checkColWin(Character symbol, int col) {
        for (int i = 0; i < rows; i++) {
            if (board[i][col] != symbol) return false;
        }
        return true;
    }

    // Bug fix #2: secondary diagonal was board[i][col-i] (col = column index, not board size)
    // Fixed to board[i][rows-1-i] which correctly walks the anti-diagonal
    // Also fix #13: renamed checkDiagnolWin → checkDiagonalWin
    private boolean checkDiagonalWin(Character symbol) {
        boolean primaryDiagonal = true;
        boolean secondaryDiagonal = true;
        for (int i = 0; i < rows; i++) {
            if (board[i][i] != symbol) primaryDiagonal = false;
        }
        for (int i = 0; i < rows; i++) {
            if (board[i][rows - 1 - i] != symbol) secondaryDiagonal = false;
        }
        return primaryDiagonal || secondaryDiagonal;
    }

    // Shut down thread pool to prevent resource leak
    public void shutdown() {
        executorService.shutdown();
    }

    // Bug fix #5: null cells caused NPE in string building
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sb.append((board[i][j] == null ? "." : board[i][j])).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
