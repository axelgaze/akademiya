package com.kabe.app;

import com.kabe.app.controllers.NavigationController;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {
    private NavigationController navigationController;
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Akademiya");
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(800);
        
        // Initialize navigation controller
        navigationController = new NavigationController(primaryStage);
        
        // Start with login view
        navigationController.showLoginView();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}