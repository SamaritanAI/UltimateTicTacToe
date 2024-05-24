package com.example.ultimatetictactoe;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class MainApp extends Application {
       /* @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
    */
        private Agent agent;
        private UltimateBoard ultimateBoard;
        private Button[][][] buttons;

        @Override
        public void start(Stage primaryStage){
            primaryStage.setTitle("Ultimate TicTacToe");
            agent = new Agent(0.1, 0.9, 0.1);
            ultimateBoard = new UltimateBoard();
            buttons = new Button[3][3][9];

            GridPane grid = new GridPane();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    GridPane subGrid = new GridPane();
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


            BorderPane root = new BorderPane(); root = new BorderPane();

            Scene scene = new Scene(grid, 450, 450);
            primaryStage.setScene(scene);
            primaryStage.show();
        }

    private void makeMove(int boardRow, int boardCol, int cellRow, int cellCol) {
        if (ultimateBoard.isMoveValid(boardRow, boardCol, cellRow, cellCol)) {
            ultimateBoard.makeMove(boardRow, boardCol, cellRow, cellCol, Player.X);
            buttons[boardRow][boardCol][cellRow * 3 + cellCol].setText("X");

            int[] possibleActions = getPossibleActions();
            if (possibleActions.length > 0) {
                String state = getState();
                int action = agent.chooseAction(state, possibleActions);
                int actionBoardRow = action / 27;
                int actionBoardCol = (action % 27) / 9;
                int actionCellRow = (action % 9) / 3;
                int actionCellCol = action % 3;
                ultimateBoard.makeMove(actionBoardRow, actionBoardCol, actionCellRow, actionCellCol, Player.O);
                buttons[actionBoardRow][actionBoardCol][actionCellRow * 3 + actionCellCol].setText("O");
                updateAgent(state, action);
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
        // Flatten the board and get the list of all possible actions
        return java.util.stream.IntStream.range(0, 81)
                .filter(i -> ultimateBoard.getBoards(i / 27, (i % 27) / 9).getCell((i % 9) / 3, i % 3).getPlayer() == null)
                .toArray();
    }

    private String getState() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int m = 0; m < 3; m++) {
                    for (int n = 0; n < 3; n++) {
                        Player player = ultimateBoard.getBoards(i, j).getCell(m, n).getPlayer();
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
        // Check for a winner in the overall board
        Player winner = ultimateBoard.getOverallBoard().getWinner();
        if (winner == Player.X) {
            return 1;
        } else if (winner == Player.O) {
            return -1;
        }
        return 0;  // Game is still ongoing or a draw
    }

    public static void main(String[] args) {
        launch();
    }
}