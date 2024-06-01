# Ultimate Tic Tac Toe with Reinforcement Learning

This repository contains an implementation of Ultimate Tic Tac Toe with an AI agent that learns to play using Q-learning, a form of Reinforcement Learning. The project also includes a simulation framework to test and evaluate the performance of the AI against a random player.

![image](https://github.com/SamaritanAI/UltimateTicTacToe/assets/33179496/f094eaa3-7346-4680-9823-2e43f7f6874b)

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Setup](#setup)
- [Usage](#usage)
- [Code Overview](#code-overview)
- [Agent Training](#agent-training)
- [Results](#results)
- [Further Improvements](#further-improvements)
- [Contributing](#contributing)
- [License](#license)

## Introduction

Ultimate Tic Tac Toe is an extension of the traditional Tic Tac Toe game where each cell of the board contains a smaller Tic Tac Toe board. The challenge increases due to the larger state space and the rules that dictate the next move based on the opponent's previous move.

This project uses Q-learning to train an AI agent to play Ultimate Tic Tac Toe. The agent learns by playing games against a random player and updating its Q-table based on the outcomes of these games.

## Features

- Ultimate Tic Tac Toe game implementation
- Q-learning agent for AI-based gameplay
- Random player for testing and comparison
- Simulation framework for training and evaluating the AI agent
- Saving and loading Q-table for persistent learning

## Setup

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Maven

### Installation

1. Clone the repository:

    ```sh
    git clone https://github.com/yourusername/ultimate-tic-tac-toe.git
    cd ultimate-tic-tac-toe
    ```

2. Build the project using Maven:

    ```sh
    mvn clean install
    ```

## Usage

### Running the Simulation

You can run the simulation to train the Q-learning agent by executing the `Simulation` class:

```sh
mvn exec:java -Dexec.mainClass="com.example.ultimatetictactoe.Simulation"
