package com.example.ultimatetictactoe;

public class Board {
    private Cell[][] cells;

    public Board() {
        cells = new Cell[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cells[i][j] = new Cell();
            }
        }
    }

    public Cell getCell(int row, int col) {
        return cells[row][col];
    }

    public boolean isFull(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(cells[i][j].getPlayer() == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public Player getWinner() {
        //TODO Implement code to check rows and diagrams
        return getWinner(); //to avoid compilation errors
    }

}
