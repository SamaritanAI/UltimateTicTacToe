package com.example.ultimatetictactoe;

import com.example.RandomPlayer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private Agent rlAgent;
    private RandomPlayer randomPlayer;
    private int gamesToPlay;
    private List<Double> winRates;
    private List<Double> lossRates;
    private List<Double> drawRates;

    public Simulation(int gamesToPlay) {
        this.rlAgent = new Agent(0.1, 0.9, 0.1);
        this.randomPlayer = new RandomPlayer();
        this.gamesToPlay = gamesToPlay;
        this.winRates = new ArrayList<>();
        this.lossRates = new ArrayList<>();
        this.drawRates = new ArrayList<>();
    }

    public void run() {
        int rlAgentWins = 0;
        int randomPlayerWins = 0;
        int draws = 0;

        for (int i = 0; i < gamesToPlay; i++) {
            UltimateBoard ultimateBoard = new UltimateBoard();
            Player currentPlayer = Player.X;

            String previousState = null;
            int previousAction = -1;

            while (true) {
                String state = getState(ultimateBoard);
                if (currentPlayer == Player.X) {
                    int[] possibleActions = getPossibleActions(ultimateBoard);
                    if (possibleActions.length == 0) {
                        break; // No valid moves left, it's a draw
                    }

                    int action = rlAgent.chooseAction(state, possibleActions);
                    int boardRow = action / 27;
                    int boardCol = (action % 27) / 9;
                    int cellRow = (action % 9) / 3;
                    int cellCol = action % 3;

                    if (ultimateBoard.isMoveValid(boardRow, boardCol, cellRow, cellCol)) {
                        ultimateBoard.makeMove(boardRow, boardCol, cellRow, cellCol, currentPlayer);

                        if (previousState != null) {
                            int reward = checkWinner(ultimateBoard);
                            int[] nextPossibleActions = getPossibleActions(ultimateBoard);
                            rlAgent.updateQTable(previousState, previousAction, reward, state, nextPossibleActions);
                        }

                        previousState = state;
                        previousAction = action;

                        if (ultimateBoard.getBoard(boardRow, boardCol).isWon()) {
                            rlAgentWins++;
                            rlAgent.updateQTable(state, action, 1, state, new int[0]); // Reward for winning
                            break;
                        }
                        currentPlayer = Player.O;
                    }
                } else {
                    int action = randomPlayer.chooseMove(ultimateBoard);
                    if (action == -1) {
                        break; // No valid moves left, it's a draw
                    }
                    int boardRow = action / 27;
                    int boardCol = (action % 27) / 9;
                    int cellRow = (action % 9) / 3;
                    int cellCol = action % 3;

                    if (ultimateBoard.isMoveValid(boardRow, boardCol, cellRow, cellCol)) {
                        ultimateBoard.makeMove(boardRow, boardCol, cellRow, cellCol, currentPlayer);

                        if (ultimateBoard.getBoard(boardRow, boardCol).isWon()) {
                            randomPlayerWins++;
                            rlAgent.updateQTable(previousState, previousAction, -1, state, new int[0]); // Penalty for losing
                            break;
                        }
                        currentPlayer = Player.X;
                    }
                }

                if (isDraw(ultimateBoard)) {
                    draws++;
                    rlAgent.updateQTable(previousState, previousAction, 0, getState(ultimateBoard), new int[0]); // No reward for draw
                    break;
                }
            }
            winRates.add((double) rlAgentWins / (i + 1));
            lossRates.add((double) randomPlayerWins / (i + 1));
            drawRates.add((double) draws / (i + 1));
        }

        saveStatistics("win_rates.csv", winRates);
        saveStatistics("loss_rates.csv", lossRates);
        saveStatistics("draw_rates.csv", drawRates);

        System.out.println("Results after " + gamesToPlay + " games:");
        System.out.println("RL Agent Wins: " + rlAgentWins);
        System.out.println("Random Player Wins: " + randomPlayerWins);
        System.out.println("Draws: " + draws);

        saveQTableToFile("qtable.txt");
    }

    private void saveStatistics(String filename, List<Double> statistics) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (Double value : statistics) {
                writer.write(value.toString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private String getState(UltimateBoard ultimateBoard) {
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

    private void saveQTableToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Map.Entry<String, double[]> entry : rlAgent.qTable.entrySet()) {
                writer.write(entry.getKey() + ": ");
                for (double value : entry.getValue()) {
                    writer.write(value + " ");
                }
                writer.newLine();
            }
            System.out.println("Q-table saved to " + filename);
        } catch (IOException e) {
            System.err.println("Error writing Q-table to file: " + e.getMessage());
        }
    }

    private int checkWinner(UltimateBoard ultimateBoard) {
        Player winner = ultimateBoard.getOverallBoard().getWinner();
        if (winner == Player.X) {
            return 1;
        } else if (winner == Player.O) {
            return -1;
        }
        return 0;  // Game is still ongoing or a draw
    }

    private boolean isDraw(UltimateBoard ultimateBoard) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!ultimateBoard.getBoard(i, j).isFull() && !ultimateBoard.getBoard(i, j).isWon()) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Simulation simulation = new Simulation(1_000_000);
        simulation.run();
    }
}
