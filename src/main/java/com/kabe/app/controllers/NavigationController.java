package com.kabe.app.controllers;

import com.kabe.app.views.*;
import javafx.stage.Stage;

public class NavigationController {
    private Stage primaryStage;
    
    public NavigationController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    
    public void showLoginView() {
        LoginView loginView = new LoginView(primaryStage);
        new LoginController(loginView);
        loginView.show();
        
        loginView.setOnLoginSuccess(() -> {
            showDashboardView();
        });
    }
    
    public void showDashboardView() {
        DashboardView dashboardView = new DashboardView(primaryStage);
        dashboardView.show();
        
        dashboardView.setNavigationHandler(this::navigate);
    }
    
    public void showTasksView() {
        TasksView tasksView = new TasksView(primaryStage);
        tasksView.show();
        
        tasksView.setNavigationHandler(viewName -> {
            if (viewName.startsWith("TaskDetail:")) {
                showDetailTasksView(viewName);
            } else {
                navigate(viewName);
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
        
        taskDetailView.setNavigationHandler(this::navigate);
    }
    
    public void showKelasView() {
        KelasView kelasView = new KelasView(primaryStage);
        kelasView.show();
        
        kelasView.setNavigationHandler(viewName -> {
            if ("KelasDetail".equals(viewName)) {
                KelasView.KelasData selectedKelas = kelasView.getSelectedKelas();
                showDetailKelasView(selectedKelas);
            } else {
                navigate(viewName);
            }
        });
    }
    
    public void showDetailKelasView(KelasView.KelasData selectedKelas) {
        KelasDetailView kelasDetailView = new KelasDetailView(primaryStage, selectedKelas);
        kelasDetailView.show();
        
        kelasDetailView.setNavigationHandler(this::navigate);
    }
    
    public void showCalendarView() {
        CalendarView calendarView = new CalendarView(primaryStage);
        calendarView.show();
        
        calendarView.setNavigationHandler(this::navigate);
    }
    
    public void showProfileView() {
        ProfileView profileView = new ProfileView(primaryStage);
        profileView.show();
        
        profileView.setNavigationHandler(this::navigate);
    }
    
    // Centralized navigation method
    private void navigate(String viewName) {
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
            case "Profile":
                showProfileView();
                break;
            case "Logout":
                showLoginView();
                break;
        }
    }
}