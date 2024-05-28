package com.example.ultimatetictactoe;

public class Board {
    private Cell[][] cells;
    private Player winner;

    public Board() {
        cells = new Cell[3][3];
        winner = null;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cells[i][j] = new Cell();
            }
        }
    }

    public Cell getCell(int row, int col) {
        return cells[row][col];
    }

    public boolean isFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (cells[i][j].getPlayer() == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isWon() {
        return getWinner() != null;
    }

    public Player getWinner() {

        if (winner != null) {
            return winner;
        }

        // Check rows
        for (int i = 0; i < 3; i++) {
            if (cells[i][0].getPlayer() != null &&
                    cells[i][0].getPlayer() == cells[i][1].getPlayer() &&
                    cells[i][0].getPlayer() == cells[i][2].getPlayer()) {
                winner = cells[i][0].getPlayer();
                return winner;
            }
        }

        // Check columns
        for (int j = 0; j < 3; j++) {
            if (cells[0][j].getPlayer() != null &&
                    cells[0][j].getPlayer() == cells[1][j].getPlayer() &&
                    cells[0][j].getPlayer() == cells[2][j].getPlayer()) {
                winner = cells[0][j].getPlayer();
                return winner;
            }
        }

        // Check diagonals
        if (cells[0][0].getPlayer() != null &&
                cells[0][0].getPlayer() == cells[1][1].getPlayer() &&
                cells[0][0].getPlayer() == cells[2][2].getPlayer()) {
            winner = cells[0][0].getPlayer();
            return winner;
        }

        if (cells[0][2].getPlayer() != null &&
                cells[0][2].getPlayer() == cells[1][1].getPlayer() &&
                cells[0][2].getPlayer() == cells[2][0].getPlayer()) {
            winner = cells[0][2].getPlayer();
            return winner;
        }

        return null;
    }
}