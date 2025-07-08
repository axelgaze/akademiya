package com.kabe.app.controllers;

import com.kabe.app.views.*;
import com.kabe.app.views.student.*;
import com.kabe.app.views.teacher.*;
import javafx.stage.Stage;
import com.kabe.app.controllers.*;
import com.kabe.app.views.interfaces.*;
import com.kabe.app.models.*;

public class NavigationController {
    private Stage primaryStage;
    UserController userController;
    KelasController kelasController;
    TugasController tugasController;

    
    public NavigationController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    
    public void showLoginView() {
        LoginView loginView = new LoginView(primaryStage);
        userController = new UserController(loginView);
        kelasController = new KelasController();
        tugasController = new TugasController();
        loginView.show();
        
        loginView.setOnLoginSuccess(() -> {
            showDashboardView();
        });
    }
    
    public void showDashboardView() {
        ViewInterface dashboardView;
        if (userController.getUser().getRole().equals("siswa")) {
            dashboardView = new StudentDashboardView(primaryStage, userController, kelasController, tugasController);
        } else {
            dashboardView = new TeacherDashboardView(primaryStage, userController, kelasController, tugasController);
        }

        dashboardView.show();
        
        dashboardView.setNavigationHandler(this::navigate);
    }
    
    public void showTasksView() {
        TasksInterface tasksView;
        if (userController.getUser().getRole().equals("siswa")) {
            tasksView = new StudentTasksView(primaryStage, userController, kelasController, tugasController);
        } else {
            tasksView = new TeacherTasksView(primaryStage, userController, tugasController, kelasController);
        }
        tasksView.show();
        
        tasksView.setNavigationHandler(viewName -> {
            if (viewName.startsWith("TaskDetail")) {
                showDetailTasksView(viewName, tasksView);
            } else if ("AddTask".equals(viewName)) {
                showAddTaskView();
            } else {
                navigate(viewName);
            }
        });
    }
    
    public void showDetailTasksView(String viewNames, TasksInterface tasksView) {
        ViewInterface taskDetailView;
        if (userController.getUser().getRole().equals("siswa")) {
            taskDetailView = new StudentTaskDetailView(primaryStage, userController, kelasController, tugasController, tasksView.getSelectedTugas());
        } else {
            taskDetailView = new TeacherTaskDetailView(primaryStage, userController, kelasController, tugasController, tasksView.getSelectedTugas());
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
                kelasController = kelasView.getKelasController();
                Kelas selectedKelas = kelasView.getSelectedKelas();
                showDetailKelasView(selectedKelas, userController, kelasController);
            } else if ("CreateClass".equals(viewName)) {
                kelasController = kelasView.getKelasController();
                showBuatKelasView(kelasController, userController);
            } else {
                navigate(viewName);
            }
        });
    }

    public void showAddTaskView() {
        AddTaskView addTaskView = new AddTaskView(primaryStage, userController, kelasController, tugasController);
        addTaskView.show();
        addTaskView.setNavigationHandler(this::navigate);
    }
    
    public void showDetailKelasView(Kelas selectedKelas, UserController userController, KelasController kelasController) {
        ViewInterface kelasDetailView;
        if (userController.getUser().getRole().equals("siswa")) {
            kelasDetailView = new StudentKelasDetailView(primaryStage, selectedKelas, kelasController, userController);
        } else {
            kelasDetailView = new TeacherKelasDetailView(primaryStage, selectedKelas, kelasController, userController);
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
        CalendarView calendarView = new CalendarView(primaryStage, userController, kelasController, tugasController);
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