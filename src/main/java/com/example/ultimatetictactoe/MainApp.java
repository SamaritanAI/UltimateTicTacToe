package com.example.ultimatetictactoe;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;

//styling
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class MainApp extends Application {
    private Agent agent;
    private UltimateBoard ultimateBoard;
    private Button[][][] buttons;
    private GridPane[][] subGrids;
    private int nextBoardRow = -1;
    private int nextBoardCol = -1;

    @Override
    public void start(Stage primaryStage) {
        agent = new Agent(0.1, 0.9, 0.1);
        ultimateBoard = new UltimateBoard();
        buttons = new Button[3][3][9];
        subGrids = new GridPane[3][3];


        primaryStage.setTitle("Ultimate Tic Tac Toe with RL");

        GridPane grid = new GridPane();
        grid.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                GridPane subGrid = new GridPane();
                subGrid.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
                subGrids[i][j] = subGrid;
                for (int m = 0; m < 3; m++) {
                    for (int n = 0; n < 3; n++) {
                        Button button = new Button();
                        button.setPrefSize(50, 50);
                        final int boardRow = i;
                        final int boardCol = j;
                        final int cellRow = m;
                        final int cellCol = n;
                        button.setOnAction(e -> makeMove(boardRow, boardCol, cellRow, cellCol));
                        buttons[boardRow][boardCol][m * 3 + n] = button;
                        subGrid.add(button, n, m);
                    }
                }
                grid.add(subGrid, j, i);
            }
        }

        Scene scene = new Scene(grid, 450, 450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void makeMove(int boardRow, int boardCol, int cellRow, int cellCol) {
        if ((nextBoardRow == -1 && nextBoardCol == -1) || (nextBoardRow == boardRow && nextBoardCol == boardCol)) {
            if (!ultimateBoard.getBoard(boardRow, boardCol).isWon() && ultimateBoard.isMoveValid(boardRow, boardCol, cellRow, cellCol)) {
                ultimateBoard.makeMove(boardRow, boardCol, cellRow, cellCol, Player.X);
                buttons[boardRow][boardCol][cellRow * 3 + cellCol].setText("X");

                if (ultimateBoard.getBoard(boardRow, boardCol).isWon() || ultimateBoard.getBoard(boardRow, boardCol).isFull()) {
                    disableBoard(boardRow, boardCol);
                }

                // Update the next legal board based on the player's move
                nextBoardRow = cellRow;
                nextBoardCol = cellCol;

                // Ensure all boards are updated for the next move
                updateBoards();

                // Agent move
                int[] possibleActions = getPossibleActions();
                if (possibleActions.length > 0) {
                    String state = getState();
                    int action = agent.chooseAction(state, possibleActions);
                    int actionBoardRow = action / 27;
                    int actionBoardCol = (action % 27) / 9;
                    int actionCellRow = (action % 9) / 3;
                    int actionCellCol = action % 3;

                    // Check if the agent's chosen move is valid
                    if (ultimateBoard.isMoveValid(actionBoardRow, actionBoardCol, actionCellRow, actionCellCol)) {
                        ultimateBoard.makeMove(actionBoardRow, actionBoardCol, actionCellRow, actionCellCol, Player.O);
                        buttons[actionBoardRow][actionBoardCol][actionCellRow * 3 + actionCellCol].setText("O");

                        if (ultimateBoard.getBoard(actionBoardRow, actionBoardCol).isWon() || ultimateBoard.getBoard(actionBoardRow, actionBoardCol).isFull()) {
                            disableBoard(actionBoardRow, actionBoardCol);
                        }

                        // Update the next legal board based on the agent's move
                        nextBoardRow = actionCellRow;
                        nextBoardCol = actionCellCol;

                        // Ensure all boards are updated for the next move
                        updateBoards();

                        updateAgent(state, action);
                    }
                }
            }
        }

    }

    private void disableBoard(int boardRow, int boardCol) {
        subGrids[boardRow][boardCol].setStyle("-fx-background-color: lightgray;");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[boardRow][boardCol][i * 3 + j].setDisable(true);
            }
        }
    }

    private void updateBoards() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boolean isTargetBoard = i == nextBoardRow && j == nextBoardCol;
                boolean disable = !isTargetBoard;
                if (ultimateBoard.getBoard(i, j).isWon() || ultimateBoard.getBoard(i, j).isFull()) {
                    subGrids[i][j].setStyle("-fx-background-color: lightgray;");
                    disable = true;
                }
                for (int m = 0; m < 3; m++) {
                    for (int n = 0; n < 3; n++) {
                        buttons[i][j][m * 3 + n].setDisable(disable);
                    }
                }
            }
        }
    }

    private void updateAgent(String state, int action) {
        int reward = checkWinner();
        int[] nextPossibleActions = getPossibleActions();
        String nextState = getState();
        agent.updateQTable(state, action, reward, nextState, nextPossibleActions);
    }

    private int[] getPossibleActions() {
        return java.util.stream.IntStream.range(0, 81)
                .filter(i -> {
                    int boardRow = i / 27;
                    int boardCol = (i % 27) / 9;
                    int cellRow = (i % 9) / 3;
                    int cellCol = i % 3;
                    return ultimateBoard.isMoveValid(boardRow, boardCol, cellRow, cellCol) &&
                            (nextBoardRow == -1 && nextBoardCol == -1 || (nextBoardRow == boardRow && nextBoardCol == boardCol));
                }).toArray();
    }

    private String getState() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int m = 0; m < 3; m++) {
                    for (int n = 0; n < 3; n++) {
                        Player player = ultimateBoard.getBoard(i, j).getCell(m, n).getPlayer();
                        if (player == Player.X) {
                            sb.append('X');
                        } else if (player == Player.O) {
                            sb.append('O');
                        } else {
                            sb.append(' ');
                        }
                    }
                }
            }
        }
        return sb.toString();
    }

    private int checkWinner() {
        Player winner = ultimateBoard.getOverallBoard().getWinner();
        if (winner == Player.X) {
            return 1;
        } else if (winner == Player.O) {
            return -1;
        }
        return 0;  // Game is still ongoing or a draw
    }

    public static void main(String[] args) {
        launch(args);
    }
}