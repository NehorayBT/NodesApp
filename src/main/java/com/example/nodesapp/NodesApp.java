package com.example.nodesapp;

import javafx.application.Application;
import javafx.stage.Stage;

public class NodesApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        Director director = new Director(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}