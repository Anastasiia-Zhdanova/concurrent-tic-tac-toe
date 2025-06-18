package com.epam.rd.autocode.concurrenttictactoe;

import java.util.Arrays;

public class TicTacToeImpl implements TicTacToe {

    private final char[][] board = new char[3][3];
    private char lastMark = ' ';

    public TicTacToeImpl() {
        for (char[] row : board) {
            Arrays.fill(row, ' ');
        }
    }

    @Override
    public synchronized void setMark(int x, int y, char mark) {
        if (board[x][y] != ' ') {
            throw new IllegalArgumentException("Cell [" + x + ", " + y + "] is already occupied.");
        }

        board[x][y] = mark;
        lastMark = mark;
        notifyAll(); // Notify waiting thread
    }

    @Override
    public synchronized char[][] table() {
        char[][] copy = new char[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, 3);
        }
        return copy;
    }

    @Override
    public synchronized char lastMark() {
        return lastMark;
    }
}
