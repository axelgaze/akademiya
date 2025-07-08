package com.kabe.app.views.teacher;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import com.kabe.app.models.Kelas;
import com.kabe.app.models.User;
import com.kabe.app.controllers.KelasController;
import com.kabe.app.controllers.TugasController;
import com.kabe.app.controllers.UserController;
import com.kabe.app.views.interfaces.ViewInterface;
import com.kabe.app.models.Tugas;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class TeacherDashboardView implements ViewInterface {
    private Stage stage;
    private Scene scene;
    private BorderPane root;
    private VBox sidebar;
    private VBox mainContent;
    private NavigationHandler navigationHandler;
    private UserController userController;
    private KelasController kelasController;
    private TugasController tugasController;
    
    public TeacherDashboardView(Stage stage, UserController userController, KelasController kelasController, TugasController tugasController) {
        this.userController = userController;
        this.kelasController = kelasController;
        this.tugasController = tugasController;
        this.stage = stage;
        initializeView();
    }

    public void setNavigationHandler(NavigationHandler handler) {
        this.navigationHandler = handler;
    }
    
    private void initializeView() {
        root = new BorderPane();
    
        LinearGradient backgroundGradient = new LinearGradient(
            0, 0, 1, 1, true, null,
            new Stop(0, Color.web("#F3E5F5")),
            new Stop(0.5, Color.web("#E8EAF6")),
            new Stop(1, Color.web("#C5CAE9"))
        );
        
        BackgroundFill backgroundFill = new BackgroundFill(backgroundGradient, null, null);
        root.setBackground(new Background(backgroundFill));
        
        // Create sidebar
        createSidebar();
        
        // Create main content with real data
        createMainContent();

        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        
        root.setLeft(sidebar);
        root.setCenter(scrollPane);
        
        scene = new Scene(root, 1200, 800);
        stage.setTitle("Akademiya - Dashboard Guru");
        stage.setScene(scene);
    }
    
    private void createSidebar() {
        sidebar = new VBox(20);
        sidebar.setPrefWidth(280);
        sidebar.setPadding(new Insets(20));
        
        LinearGradient sidebarGradient = new LinearGradient(
            0, 0, 1, 0, true, null,
            new Stop(0, Color.web("#4A148C")),
            new Stop(1, Color.web("#7B1FA2"))
        );
        
        BackgroundFill sidebarFill = new BackgroundFill(sidebarGradient, 
                                                        new CornerRadii(0, 15, 15, 0, false), 
                                                        null);
        sidebar.setBackground(new Background(sidebarFill));
        sidebar.setEffect(new DropShadow(15, Color.web("#1A3009")));
        
        // Logo and title
        VBox logoContainer = new VBox(5);
        logoContainer.setAlignment(Pos.CENTER);
        
        Label logoLabel = new Label("AKADEMIYA");
        logoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        logoLabel.setTextFill(Color.web("#E8F5E8"));
        
        Label logoSubtitle = new Label("Panel Pengajar");
        logoSubtitle.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        logoSubtitle.setTextFill(Color.web("#E1BEE7"));
        
        logoContainer.getChildren().addAll(logoLabel, logoSubtitle);
        
        // Navigation menu
        VBox navigationMenu = new VBox(10);
        navigationMenu.setPadding(new Insets(20, 0, 0, 0));
        
        Button dashboardBtn = createMenuButton("🏠 Dashboard", true);
        Button tasksBtn = createMenuButton("📋 Tugas", false);
        Button classesBtn = createMenuButton("🏫 Kelas", false);
        Button calendarBtn = createMenuButton("📅 Kalender", false);
        Button profileBtn = createMenuButton("👤 Profile", false);
        
        navigationMenu.getChildren().addAll(dashboardBtn, tasksBtn, classesBtn);

        dashboardBtn.setOnAction(e -> {
            if (navigationHandler != null) {
                navigationHandler.handleNavigation("Dashboard");
            }
        });

        tasksBtn.setOnAction(e -> {
            if (navigationHandler != null) {
                navigationHandler.handleNavigation("Tugas");
            }
        });

        classesBtn.setOnAction(e -> {
            if (navigationHandler != null) {
                navigationHandler.handleNavigation("Kelas");
            }
        });

        calendarBtn.setOnAction(e -> {
            if (navigationHandler != null) {
                navigationHandler.handleNavigation("Kalender");
            }
        });

        profileBtn.setOnAction(e -> {
            if (navigationHandler != null) {
                navigationHandler.handleNavigation("Profile");
            }
        });
        
        // User info at bottom
        VBox userInfo = new VBox(10);
        userInfo.setAlignment(Pos.CENTER);
        userInfo.setPadding(new Insets(20, 0, 0, 0));
        
        Label userAvatar = new Label("👨‍🏫");
        userAvatar.setFont(Font.font(32));
        userAvatar.setTextFill(Color.web("#E8F5E8"));
        
        Label userName = new Label(userController.getUser().getFullName());
        userName.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        userName.setTextFill(Color.web("#E8F5E8"));
        
        Label userRole = new Label(userController.getUser().getRole());
        userRole.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        userRole.setTextFill(Color.web("#C8E6C9"));
        
        Button logoutBtn = createMenuButton("🚪 Logout", false);
        logoutBtn.setStyle("-fx-background-color: rgba(244, 67, 54, 0.8); " +
                          "-fx-background-radius: 8; " +
                          "-fx-text-fill: white; " +
                          "-fx-cursor: hand;");

        logoutBtn.setOnAction(e -> {
            if (navigationHandler != null) {
                navigationHandler.handleNavigation("Logout");
            }
        });
        
        userInfo.getChildren().addAll(userAvatar, userName, userRole, logoutBtn);
        
        sidebar.getChildren().addAll(logoContainer, navigationMenu, userInfo);
        VBox.setVgrow(navigationMenu, Priority.ALWAYS);
    }
    
    private Button createMenuButton(String text, boolean isActive) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        button.setPrefWidth(240);
        button.setPrefHeight(40);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(10, 15, 10, 15));
        
        if (isActive) {
            button.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); " +
                           "-fx-background-radius: 8; " +
                           "-fx-text-fill: #F3E5F5; " +
                           "-fx-cursor: hand;");
        } else {
            button.setStyle("-fx-background-color: transparent; " +
                           "-fx-background-radius: 8; " +
                           "-fx-text-fill: #E1BEE7; " +
                           "-fx-cursor: hand;");
        }
        
        button.setOnMouseEntered(e -> {
            if (!isActive) {
                button.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); " +
                               "-fx-background-radius: 8; " +
                               "-fx-text-fill: #F3E5F5; " +
                               "-fx-cursor: hand;");
            }
        });
        
        button.setOnMouseExited(e -> {
            if (!isActive) {
                button.setStyle("-fx-background-color: transparent; " +
                               "-fx-background-radius: 8; " +
                               "-fx-text-fill: #E1BEE7; " +
                               "-fx-cursor: hand;");
            }
        });
        
        return button;
    }
    
    private void createMainContent() {
        mainContent = new VBox(30);
        mainContent.setPadding(new Insets(30));
        
        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));
        
        Label headerTitle = new Label("Dashboard Guru");
        headerTitle.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        headerTitle.setTextFill(Color.web("#4A148C"));
        
        Label headerSubtitle = new Label("Ringkasan aktivitas mengajar Anda");
        headerSubtitle.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        headerSubtitle.setTextFill(Color.web("#7B1FA2"));
        
        VBox headerText = new VBox(5);
        headerText.getChildren().addAll(headerTitle, headerSubtitle);
        
        header.getChildren().add(headerText);
        
        // Statistics cards with real data
        HBox statisticsContainer = createStatisticsCards();
        
        // Recent activities with real data
        VBox recentActivities = createRecentActivities();
        
        mainContent.getChildren().addAll(header, statisticsContainer, recentActivities);
    }
    
    private HBox createStatisticsCards() {
        HBox container = new HBox(20);
        container.setAlignment(Pos.CENTER);
        
        // Get teacher's classes
        List<Kelas> teacherClasses = kelasController.getClassesByTeacher(userController.getUser().getId());
        int classCount = teacherClasses != null ? teacherClasses.size() : 0;
        
        // Get assignments to grade (count of assignments that have submissions but no grade)
        int assignmentsToGrade = 0;
        int totalStudents = 0;
        int upcomingDeadlines = 0;
        
        if (teacherClasses != null) {
            for (Kelas kelas : teacherClasses) {
                // Count total students across all classes
                totalStudents += kelasController.getStudentCount(kelas.getId());
                
                // Get assignments for this class
                List<Tugas> assignments = tugasController.getTugasByTeacher(userController.getUser().getId());
                
                if (assignments != null) {
                    for (Tugas assignment : assignments) {
                        // Count assignments that need grading
                        int submittedCount = tugasController.countUniquePengumpul(assignment.getId());
                        int gradedCount = 0; // You might need to add a method to count graded assignments
                        assignmentsToGrade += (submittedCount - gradedCount);
                        
                        // Count upcoming deadlines (within next 7 days)
                        if (assignment.getDeadline() != null && assignment.getDeadline().after(Timestamp.valueOf(LocalDateTime.now()))) {
                            upcomingDeadlines++;
                        }
                    }
                }
            }
        }
        
        // Classes taught card
        VBox classesCard = createStatCard("🏫", "Kelas Diajar", String.valueOf(classCount), Color.web("#2196F3"));
        
        // Assignments to grade card  
        VBox gradingCard = createStatCard("📝", "Tugas Perlu Dinilai", String.valueOf(assignmentsToGrade), Color.web("#FF9800"));
        
        // Students card
        VBox studentsCard = createStatCard("👥", "Total Siswa", String.valueOf(totalStudents), Color.web("#4CAF50"));
        
        // Upcoming deadlines card
        VBox deadlinesCard = createStatCard("⏰", "Deadline Mendatang", String.valueOf(upcomingDeadlines), Color.web("#F44336"));
        
        container.getChildren().addAll(classesCard, gradingCard, studentsCard, deadlinesCard);
        
        return container;
    }
    
    private VBox createStatCard(String icon, String title, String value, Color color) {
        VBox card = new VBox(15);
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(250);
        card.setPrefHeight(140);
        card.setPadding(new Insets(20));
        
        card.setStyle("-fx-background-color: white; " +
                     "-fx-background-radius: 15; " +
                     "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                     "-fx-border-width: 1; " +
                     "-fx-border-radius: 15;");
        card.setEffect(new DropShadow(10, Color.web("#E0E0E0")));
        
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(32));
        
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        valueLabel.setTextFill(color);
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        titleLabel.setTextFill(Color.web("#666666"));
        
        card.getChildren().addAll(iconLabel, valueLabel, titleLabel);
        
        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: white; " +
                         "-fx-background-radius: 15; " +
                         "-fx-border-color: " + toHexString(color) + "; " +
                         "-fx-border-width: 2; " +
                         "-fx-border-radius: 15; " +
                         "-fx-cursor: hand;");
        });
        
        card.setOnMouseExited(e -> {
            card.setStyle("-fx-background-color: white; " +
                         "-fx-background-radius: 15; " +
                         "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                         "-fx-border-width: 1; " +
                         "-fx-border-radius: 15;");
        });
        
        return card;
    }
    
    private VBox createRecentActivities() {
        VBox container = new VBox(20);
        
        Label sectionTitle = new Label("Aktivitas Mengajar Terbaru");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        sectionTitle.setTextFill(Color.web("#7B1FA2"));
        
        VBox activitiesContainer = new VBox(10);
        activitiesContainer.setPadding(new Insets(20));
        activitiesContainer.setStyle("-fx-background-color: white; " +
                                   "-fx-background-radius: 15; " +
                                   "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                                   "-fx-border-width: 1; " +
                                   "-fx-border-radius: 15;");
        activitiesContainer.setEffect(new DropShadow(10, Color.web("#E0E0E0")));
        
        // Get recent activities from database
        List<Kelas> teacherClasses = kelasController.getClassesByTeacher(userController.getUser().getId());
        
        if (teacherClasses != null) {
            for (Kelas kelas : teacherClasses) {
                // Get recent assignments
                List<Tugas> recentAssignments = tugasController.getTugasByTeacher(userController.getUser().getId());
                
                if (recentAssignments != null && !recentAssignments.isEmpty()) {
                    Tugas latestAssignment = recentAssignments.get(0); // Get most recent
                    HBox activity = createActivityItem("📝", 
                        "Anda telah membuat tugas baru untuk " + kelas.getNama() + ": " + latestAssignment.getTitle(),
                        "Baru saja", Color.web("#2196F3"));
                    activitiesContainer.getChildren().add(activity);
                }
                
                // Get recent student submissions
                List<Tugas> assignments = tugasController.getTugasByTeacher(userController.getUser().getId());
                if (assignments != null) {
                    for (Tugas assignment : assignments) {
                        List<User> studentsSubmitted = tugasController.getSiswaSudahMengumpulkan(assignment.getId());
                        if (studentsSubmitted != null && !studentsSubmitted.isEmpty()) {
                            HBox activity = createActivityItem("📋", 
                                studentsSubmitted.size() + " siswa telah mengumpulkan tugas " + assignment.getTitle(),
                                "Hari ini", Color.web("#FF9800"));
                            activitiesContainer.getChildren().add(activity);
                            break; // Just show one for example
                        }
                    }
                }
            }
        }
        
        // Add some default activities if no real data found
        if (activitiesContainer.getChildren().isEmpty()) {
            HBox activity1 = createActivityItem("📝", "Anda belum membuat tugas apapun", "Mulai sekarang", Color.web("#2196F3"));
            HBox activity2 = createActivityItem("👥", "Belum ada siswa yang mengumpulkan tugas", "Mulai sekarang", Color.web("#4CAF50"));
            activitiesContainer.getChildren().addAll(activity1, activity2);
        }
        
        container.getChildren().addAll(sectionTitle, activitiesContainer);
        
        return container;
    }
    
    private HBox createActivityItem(String icon, String description, String time, Color color) {
        HBox item = new HBox(15);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(15));
        item.setStyle("-fx-background-color: transparent; " +
                     "-fx-background-radius: 10; " +
                     "-fx-cursor: hand;");
        
        VBox iconContainer = new VBox();
        iconContainer.setAlignment(Pos.CENTER);
        iconContainer.setPrefWidth(40);
        iconContainer.setPrefHeight(40);
        iconContainer.setStyle("-fx-background-color: " + toHexString(color.deriveColor(0, 1, 1, 0.1)) + "; " +
                              "-fx-background-radius: 20;");
        
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(18));
        iconContainer.getChildren().add(iconLabel);
        
        VBox textContainer = new VBox(5);
        textContainer.setAlignment(Pos.CENTER_LEFT);
        
        Label descriptionLabel = new Label(description);
        descriptionLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        descriptionLabel.setTextFill(Color.web("#333333"));
        descriptionLabel.setWrapText(true);
        
        Label timeLabel = new Label(time);
        timeLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        timeLabel.setTextFill(Color.web("#888888"));
        
        textContainer.getChildren().addAll(descriptionLabel, timeLabel);
        
        item.getChildren().addAll(iconContainer, textContainer);
        HBox.setHgrow(textContainer, Priority.ALWAYS);
        
        item.setOnMouseEntered(e -> {
            item.setStyle("-fx-background-color: rgba(0, 0, 0, 0.05); " +
                         "-fx-background-radius: 10; " +
                         "-fx-cursor: hand;");
        });
        
        item.setOnMouseExited(e -> {
            item.setStyle("-fx-background-color: transparent; " +
                         "-fx-background-radius: 10; " +
                         "-fx-cursor: hand;");
        });
        
        return item;
    }
    
    private String toHexString(Color color) {
        return String.format("#%02X%02X%02X",
                           (int)(color.getRed() * 255),
                           (int)(color.getGreen() * 255),
                           (int)(color.getBlue() * 255));
    }
    
    public void show() {
        stage.show();
    }
}