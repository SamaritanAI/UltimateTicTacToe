package com.example.ultimatetictactoe;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {
        @Override/*
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
    */

        public void start(Stage primaryStage){
            primaryStage.setTitle("Ultimate TicTacToe");

            BorderPane root = new BorderPane(); root = new BorderPane();

            Scene scene = new Scene(root, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.show();
        }

    public static void main(String[] args) {
        launch();
    }
}