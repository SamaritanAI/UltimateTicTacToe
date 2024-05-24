package com.example.ultimatetictactoe;

public class UltimateBoard {
    private Board[][] boards;
    private Board overallBoard;

    public UltimateBoard() {
        boards = new Board[3][3];
        overallBoard = new Board();

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                boards[i][j] = new Board();
            }
        }
    }

    public Board getBoards(int row, int col) {
        return boards[row][col];
    }

    public Board getOverallBoard() {
        return overallBoard;
    }

    public boolean isMoveValid(int boardRow, int boardCol, int cellRow, int cellCol) {
        return boards[boardRow][boardCol].getCell(cellRow, cellCol).getPlayer() == null;
    }

    public void makeMove(int boardRow, int boardCol, int cellRow, int cellCol, Player player) {
        if(isMoveValid(boardRow, boardCol, cellRow, cellCol)) {
            boards[boardRow][boardCol].getCell(cellRow, cellCol).setPlayer(player);

            Player winner = boards[boardRow][boardCol].getWinner();
            if (winner != null) {
                overallBoard.getCell(boardRow, boardCol).setPlayer(winner);
            }
        }
    }
}
