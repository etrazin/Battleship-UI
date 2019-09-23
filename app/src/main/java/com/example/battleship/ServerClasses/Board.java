package com.example.battleship.ServerClasses;

public class Board {

    final static int BOARD_SIZE = 10;
    private ServerCell[][] board;


    public Board() {
        this.board = new ServerCell[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                this.board[i][j] = new ServerCell();
            }
        }
    }

    public PlayResult hitCell(int row, int col) {
        return this.board[row][col].hitCell();
    }

    public void fillCell(int row, int col) {
        this.board[row][col].fillCell();
    }

    public boolean isValidPlacement(int row, int col) {
        if (!inBounds(row, col)) {
            return false;
        }

        // Check if cell or neighbors are already filled
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                int x = row + i, y = col + j;
                if (inBounds(x,y) && this.board[x][y].getFill() != CellFill.EMPTY) {
                    return false;
                }
            }
        }

        return true;
    }

    public String[][] export(boolean isOpponent) {
        String[][] boardStr = new String[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (isOpponent) {
                    boardStr[i][j] = this.board[i][j].opponentView();
                } else {
                    boardStr[i][j] = this.board[i][j].toString();
                }
            }
        }

        return boardStr;

    }

    private boolean inBounds(int x, int y) {
        if (x < 0 || y < 0 || x >= BOARD_SIZE || y >= BOARD_SIZE) {
            return false;
        }

        return true;
    }

}
