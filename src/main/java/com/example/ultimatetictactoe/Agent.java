package com.example.ultimatetictactoe;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Agent {
    private double alpha;
    private double epsilon;
    public double gamma;
    public Map<String, double[]> qTable;
    private Random random;

    public Agent(double alpha, double gamma, double epsilon) {
        this.alpha = alpha;
        this.gamma = gamma;
        this.epsilon = epsilon;
        this.qTable = new HashMap<>();
        this.random = new Random();
    }

    private int bestAction(String state, int[] possibleActions) {
        if (!qTable.containsKey(state)) {
            qTable.put(state, new double[81]);
        }
        double[] qValues = qTable.get(state);
        int bestAction = possibleActions[0];
        double maxQValue = qValues[bestAction];

        for (int action : possibleActions) {
            if (qValues[action] > maxQValue) {
                maxQValue = qValues[action];
                bestAction = action;
            }
        }
        return bestAction;
    }

    public void updateQTable(String state, int action, int reward, String nextState, int[] nextPossibleActions) {
        if (!qTable.containsKey(state)) {
            qTable.put(state, new double[81]);
        }
        double[] qValues = qTable.get(state);
        double maxNextQValue = nextPossibleActions.length > 0 ? qTable.getOrDefault(nextState, new double[81])[bestAction(nextState, nextPossibleActions)] : 0;
        qValues[action] = qValues[action] + alpha * (reward + gamma * maxNextQValue - qValues[action]);
    }

    public int chooseAction(String state, int[] possibleActions) {
        if (random.nextDouble() < epsilon) {
            return possibleActions[random.nextInt(possibleActions.length)];
        } else {
            return bestAction(state, possibleActions);
        }
    }
}

