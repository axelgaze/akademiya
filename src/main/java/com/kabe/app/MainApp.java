package com.kabe.app;

import com.kabe.app.view.*;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {
    private Stage primaryStage;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Akademiya");
        
        // Mulai dengan tampilan login
        showLoginView();
    }
    
    public void showLoginView() {
        LoginView loginView = new LoginView(primaryStage);
        loginView.show();
        
        // Set handler untuk login berhasil (contoh sederhana)
        // Di implementasi nyata, ini akan dipanggil setelah validasi login
        loginView.setOnLoginSuccess(() -> {
            showDashboardView();
        });
    }
    
    public void showDashboardView() {
        DashboardView dashboardView = new DashboardView(primaryStage);
        dashboardView.show();
        
        // Setup navigation handlers
        dashboardView.setNavigationHandler(viewName -> {
            switch(viewName) {
                case "Tugas":
                    showTasksView();
                    break;
                case "Kelas":
                    showKelasView();
                    break;
                case "Kalender":
                    showCalendarView();
                    break;
                case "Logout":
                    showLoginView();
                    break;
                // Dashboard tidak perlu di-handle karena sudah di dashboard
            }
        });
    }
    
    public void showTasksView() {
        TasksView tasksView = new TasksView(primaryStage);
        tasksView.show();
        
        tasksView.setNavigationHandler(viewName -> {
            switch(viewName) {
                case "Dashboard":
                    showDashboardView();
                    break;
                case "Kelas":
                    showKelasView();
                    break;
                case "Kalender":
                    showCalendarView();
                    break;
                case "Logout":
                    showLoginView();
                    break;
            }
        });
    }
    
    public void showKelasView() {
        KelasView kelasView = new KelasView(primaryStage);
        kelasView.show();
        
        kelasView.setNavigationHandler(viewName -> {
            switch(viewName) {
                case "Dashboard":
                    showDashboardView();
                    break;
                case "Tugas":
                    showTasksView();
                    break;
                case "Kalender":
                    showCalendarView();
                    break;
                case "Logout":
                    showLoginView();
                    break;
            }
        });
    }
    
    public void showCalendarView() {
        CalendarView calendarView = new CalendarView(primaryStage);
        calendarView.show();
        
        calendarView.setNavigationHandler(viewName -> {
            switch(viewName) {
                case "Dashboard":
                    showDashboardView();
                    break;
                case "Tugas":
                    showTasksView();
                    break;
                case "Kelas":
                    showKelasView();
                    break;
                case "Logout":
                    showLoginView();
                    break;
            }
        });
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}