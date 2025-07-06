package com.kabe.app.controllers;

import com.kabe.app.views.*;
import com.kabe.app.views.student.*;
import com.kabe.app.views.teacher.*;
import javafx.stage.Stage;
import com.kabe.app.controllers.UserController;
import com.kabe.app.views.interfaces.*;
import com.kabe.app.models.*;

public class NavigationController {
    private Stage primaryStage;
    UserController userController;
    
    public NavigationController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    
    public void showLoginView() {
        LoginView loginView = new LoginView(primaryStage);
        userController = new UserController(loginView);
        loginView.show();
        
        loginView.setOnLoginSuccess(() -> {
            showDashboardView();
        });
    }
    
    public void showDashboardView() {
        ViewInterface dashboardView;
        if (userController.getUser().getRole().equals("siswa")) {
            dashboardView = new StudentDashboardView(primaryStage, userController);
        } else {
            dashboardView = new TeacherDashboardView(primaryStage, userController);
        }

        dashboardView.show();
        
        dashboardView.setNavigationHandler(this::navigate);
    }
    
    public void showTasksView() {
        ViewInterface tasksView;
        if (userController.getUser().getRole().equals("siswa")) {
            tasksView = new StudentTasksView(primaryStage, userController);
        } else {
            tasksView = new TeacherTasksView(primaryStage, userController);
        }
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
        ViewInterface taskDetailView;
        if (userController.getUser().getRole().equals("siswa")) {
            String[] params = viewNames.split(":");
            String title = params[1];
            String className = params[2];
            String teacher = params[3];
            String deadline = params[4];
            String status = params[5];
            boolean isGroupTask = Boolean.parseBoolean(params[6]);
            
            taskDetailView = new StudentTaskDetailView(primaryStage, title, className, teacher, deadline, status, isGroupTask);
        } else {
            String[] params = viewNames.split(":");
            String title = params[1];
            String className = params[2];
            String deadline = params[3];
            int totalStudents = Integer.valueOf(params[6]);
            int submittedCount = Integer.valueOf(params[7]);
            boolean isGroupTask = Boolean.parseBoolean(params[5]);

            taskDetailView = new TeacherTaskDetailView(primaryStage, title, className, deadline, totalStudents, submittedCount);
        }

        taskDetailView.show();
        
        taskDetailView.setNavigationHandler(this::navigate);
    }
    
    public void showKelasView() {
        KelasInterface kelasView;
        if (userController.getUser().getRole().equals("siswa")) {
            kelasView = new StudentKelasView(primaryStage, userController);
        } else {
            kelasView = new TeacherKelasView(primaryStage, userController);
        }
        kelasView.show();
        
        kelasView.setNavigationHandler(viewName -> {
            if ("KelasDetail".equals(viewName)) {
                KelasController kelasController = kelasView.getKelasController();
                Kelas selectedKelas = kelasView.getSelectedKelas();
                showDetailKelasView(selectedKelas, kelasController);
            } else if ("CreateClass".equals(viewName)) {
                KelasController kelasController = kelasView.getKelasController();
                showBuatKelasView(kelasController, userController);
            } else {
                navigate(viewName);
            }
        });
    }
    
    public void showDetailKelasView(Kelas selectedKelas, KelasController kelasController) {
        ViewInterface kelasDetailView;
        if (userController.getUser().getRole().equals("siswa")) {
            kelasDetailView = new StudentKelasDetailView(primaryStage, selectedKelas, kelasController);
        } else {
            kelasDetailView = new TeacherKelasDetailView(primaryStage, selectedKelas, kelasController);
        }
        
        kelasDetailView.show();
        
        kelasDetailView.setNavigationHandler(this::navigate);
    }

    public void showBuatKelasView(KelasController kelasController, UserController userController) {
        BuatKelasView buatKelasView = new BuatKelasView(primaryStage, kelasController, userController);
        buatKelasView.show();
        kelasController = buatKelasView.getKelasController();
        
        buatKelasView.setNavigationHandler(this::navigate);
    }
    
    public void showCalendarView() {
        CalendarView calendarView = new CalendarView(primaryStage, userController);
        calendarView.show();
        
        calendarView.setNavigationHandler(this::navigate);
    }
    
    public void showProfileView() {
        ProfileView profileView = new ProfileView(primaryStage, userController);
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