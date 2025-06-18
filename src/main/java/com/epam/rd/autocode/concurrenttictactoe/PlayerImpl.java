package com.epam.rd.autocode.concurrenttictactoe;

public class PlayerImpl implements Player {

    private final TicTacToe ticTacToe;
    private final char mark;
    private final PlayerStrategy strategy;

    public PlayerImpl(TicTacToe ticTacToe, char mark, PlayerStrategy strategy) {
        this.ticTacToe = ticTacToe;
        this.mark = mark;
        this.strategy = strategy;
    }

    @Override
    public void run() {
        while (!isGameOver()) {
            synchronized (ticTacToe) {
                try {
                    while (ticTacToe.lastMark() == mark && !isGameOver()) {
                        ticTacToe.wait();
                    }

                    if (isGameOver()) return;

                    Move move = strategy.computeMove(mark, ticTacToe);
                    ticTacToe.setMark(move.row, move.column, mark);

                    ticTacToe.notifyAll();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    private boolean isGameOver() {
        char[][] table = ticTacToe.table();
        return checkWinner(table, 'X') || checkWinner(table, 'O') || isBoardFull(table);
    }

    private boolean isBoardFull(char[][] table) {
        for (char[] row : table) {
            for (char c : row) {
                if (c == ' ') return false;
            }
        }
        return true;
    }

    private boolean checkWinner(char[][] table, char m) {
        for (int i = 0; i < 3; i++) {
            if (table[i][0] == m && table[i][1] == m && table[i][2] == m) return true;
            if (table[0][i] == m && table[1][i] == m && table[2][i] == m) return true;
        }
        return (table[0][0] == m && table[1][1] == m && table[2][2] == m)
                || (table[0][2] == m && table[1][1] == m && table[2][0] == m);
    }
}
