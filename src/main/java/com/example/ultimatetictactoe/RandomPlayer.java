package com.example;

import com.example.ultimatetictactoe.UltimateBoard;

public class RandomPlayer {
    public int chooseMove(UltimateBoard ultimateBoard) {
        int[] possibleActions = getPossibleActions(ultimateBoard);
        if (possibleActions.length == 0) {
            return -1; // No valid moves left
        }
        int randomIndex = (int) (Math.random() * possibleActions.length);
        return possibleActions[randomIndex];
    }

    private int[] getPossibleActions(UltimateBoard ultimateBoard) {
        return java.util.stream.IntStream.range(0, 81)
                .filter(i -> {
                    int boardRow = i / 27;
                    int boardCol = (i % 27) / 9;
                    int cellRow = (i % 9) / 3;
                    int cellCol = i % 3;
                    return ultimateBoard.isMoveValid(boardRow, boardCol, cellRow, cellCol);
                }).toArray();
    }
}
