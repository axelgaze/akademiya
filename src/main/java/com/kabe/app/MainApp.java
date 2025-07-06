package com.kabe.app;

import com.kabe.app.controllers.LoginController;
import com.kabe.app.views.*;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {
    private Stage primaryStage;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Akademiya");
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(800);
        
        // Mulai dengan tampilan login
        showLoginView();
    }
    
    public void showLoginView() {
        LoginView loginView = new LoginView(primaryStage);
        new LoginController(loginView); // Hubungkan view dengan controller
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
                case "Profile":
                    showProfileView();
                    break;
                case "Logout":
                    showLoginView();
                    break;
            }
        });
    }
    
    public void showTasksView() {
        TasksView tasksView = new TasksView(primaryStage);
        tasksView.show();
        
        tasksView.setNavigationHandler(viewName -> {
            if (viewName.startsWith("TaskDetail:")) {
                showDetailTasksView(viewName);
            } else {
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
                    case "Profile":
                        showProfileView();
                        break;
                    case "Logout":
                        showLoginView();
                        break;
                }
            }
        });
    }

    public void showDetailTasksView(String viewNames) {
        String[] params = viewNames.split(":");
        String title = params[1];
        String className = params[2];
        String teacher = params[3];
        String deadline = params[4];
        String status = params[5];
        boolean isGroupTask = Boolean.parseBoolean(params[6]);
        
        TaskDetailView taskDetailView = new TaskDetailView(primaryStage, title, className, teacher, deadline, status, isGroupTask);
        taskDetailView.show();

        taskDetailView.setNavigationHandler(viewName -> {
            if (viewName == "Tugas") {
                showTasksView();
            }
        });
    }
    
    public void showKelasView() {
        KelasView kelasView = new KelasView(primaryStage);
        kelasView.show();
        
        kelasView.setNavigationHandler(viewName -> {
            if ("KelasDetail".equals(viewName)) {
                KelasView.KelasData selectedKelas = kelasView.getSelectedKelas();
                showDetailKelasView(selectedKelas);
            } else {
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
                    case "Profile":
                        showProfileView();
                        break;
                    case "Logout":
                        showLoginView();
                        break;
                }
            }
        });
    }

    public void showDetailKelasView(KelasView.KelasData selectedKelas) {
        KelasDetailView kelasDetailView = new KelasDetailView(primaryStage, selectedKelas);
        kelasDetailView.show();

        kelasDetailView.setNavigationHandler(viewName -> {
            if (viewName == "Kelas") {
                showKelasView();
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
                case "Profile":
                    showProfileView();
                    break;
                case "Logout":
                    showLoginView();
                    break;
            }
        });
    }

    public void showProfileView() {
        ProfileView profileView = new ProfileView(primaryStage);
        profileView.show();
        
        profileView.setNavigationHandler(viewName -> {
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
                case "Kalender":
                    showCalendarView();
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