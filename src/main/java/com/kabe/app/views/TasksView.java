package com.kabe.app.views;

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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class TasksView {
    private Stage stage;
    private Scene scene;
    private BorderPane root;
    private VBox sidebar;
    private VBox mainContent;
    private TextField searchField;
    private ComboBox<String> statusFilter;
    private ComboBox<String> classFilter;
    private VBox tasksContainer;
    private NavigationHandler navigationHandler;

    public interface NavigationHandler {
        void handleNavigation(String viewName);
    }

    public void setNavigationHandler(NavigationHandler handler) {
        this.navigationHandler = handler;
    }
    
    public TasksView(Stage stage) {
        this.stage = stage;
        initializeView();
    }
    
    private void initializeView() {
        root = new BorderPane();
        
        // Background with gradient
        LinearGradient backgroundGradient = new LinearGradient(
            0, 0, 1, 1, true, null,
            new Stop(0, Color.web("#F1F8E9")),
            new Stop(0.5, Color.web("#E8F5E8")),
            new Stop(1, Color.web("#C8E6C9"))
        );
        
        BackgroundFill backgroundFill = new BackgroundFill(backgroundGradient, null, null);
        root.setBackground(new Background(backgroundFill));
        
        createSidebar();
        createMainContent();

        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true); // Konten menyesuaikan lebar
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Nonaktifkan scroll horizontal
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Aktifkan scroll vertikal jika diperlukan
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;"); // Hilangkan latar belakang
        
        root.setLeft(sidebar);
        root.setCenter(scrollPane);
        
        scene = new Scene(root, 1200, 800);
        stage.setTitle("Akademiya - Tugas");
        stage.setScene(scene);
    }
    
    private void createSidebar() {
        sidebar = new VBox(20);
        sidebar.setPrefWidth(280);
        sidebar.setPadding(new Insets(20));
        
        // Sidebar background
        LinearGradient sidebarGradient = new LinearGradient(
            0, 0, 1, 0, true, null,
            new Stop(0, Color.web("#2D5016")),
            new Stop(1, Color.web("#3D6B1F"))
        );
        
        BackgroundFill sidebarFill = new BackgroundFill(sidebarGradient, 
                                                        new CornerRadii(0, 15, 15, 0, false), 
                                                        null);
        sidebar.setBackground(new Background(sidebarFill));
        sidebar.setEffect(new DropShadow(15, Color.web("#1A3009")));
        
        // Logo
        VBox logoContainer = new VBox(5);
        logoContainer.setAlignment(Pos.CENTER);
        
        Label logoLabel = new Label("AKADEMIYA");
        logoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        logoLabel.setTextFill(Color.web("#E8F5E8"));
        
        Label logoSubtitle = new Label("Platform Pembelajaran");
        logoSubtitle.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        logoSubtitle.setTextFill(Color.web("#C8E6C9"));
        
        logoContainer.getChildren().addAll(logoLabel, logoSubtitle);
        
        // Navigation menu
        VBox navigationMenu = new VBox(10);
        navigationMenu.setPadding(new Insets(20, 0, 0, 0));
        
        Button dashboardBtn = createMenuButton("🏠 Dashboard", false);
        Button tasksBtn = createMenuButton("📋 Tugas", true);
        Button classesBtn = createMenuButton("🏫 Kelas", false);
        Button calendarBtn = createMenuButton("📅 Kalender", false);
        Button profileBtn = createMenuButton("👤 Profile", false);
        
        navigationMenu.getChildren().addAll(dashboardBtn, tasksBtn, classesBtn, calendarBtn, profileBtn);

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
        
        
        // User info
        VBox userInfo = new VBox(10);
        userInfo.setAlignment(Pos.CENTER);
        userInfo.setPadding(new Insets(20, 0, 0, 0));
        
        Label userAvatar = new Label("👤");
        userAvatar.setFont(Font.font(32));
        userAvatar.setTextFill(Color.web("#E8F5E8"));
        
        Label userName = new Label("John Doe");
        userName.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        userName.setTextFill(Color.web("#E8F5E8"));
        
        Label userRole = new Label("Pelajar");
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
                           "-fx-text-fill: #E8F5E8; " +
                           "-fx-cursor: hand;");
        } else {
            button.setStyle("-fx-background-color: transparent; " +
                           "-fx-background-radius: 8; " +
                           "-fx-text-fill: #C8E6C9; " +
                           "-fx-cursor: hand;");
        }
        
        button.setOnMouseEntered(e -> {
            if (!isActive) {
                button.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); " +
                               "-fx-background-radius: 8; " +
                               "-fx-text-fill: #E8F5E8; " +
                               "-fx-cursor: hand;");
            }
        });
        
        button.setOnMouseExited(e -> {
            if (!isActive) {
                button.setStyle("-fx-background-color: transparent; " +
                               "-fx-background-radius: 8; " +
                               "-fx-text-fill: #C8E6C9; " +
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
        
        Label headerTitle = new Label("Tugas");
        headerTitle.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        headerTitle.setTextFill(Color.web("#2D5016"));
        
        Label headerSubtitle = new Label("Kelola dan kumpulkan tugas Anda");
        headerSubtitle.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        headerSubtitle.setTextFill(Color.web("#4A7C26"));
        
        VBox headerText = new VBox(5);
        headerText.getChildren().addAll(headerTitle, headerSubtitle);
        
        header.getChildren().add(headerText);
        
        // Search and filter section
        HBox searchFilterContainer = createSearchFilterSection();
        
        // Tasks container
        createTasksContainer();
        
        mainContent.getChildren().addAll(header, searchFilterContainer, tasksContainer);
    }
    
    private HBox createSearchFilterSection() {
        HBox container = new HBox(20);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setPadding(new Insets(0, 0, 20, 0));
        
        // Search field
        searchField = new TextField();
        searchField.setPromptText("Cari tugas...");
        searchField.setPrefWidth(300);
        searchField.setPrefHeight(40);
        searchField.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        searchField.setStyle("-fx-background-color: white; " +
                           "-fx-background-radius: 10; " +
                           "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                           "-fx-border-radius: 10; " +
                           "-fx-padding: 10;");
        
        // Status filter
        statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll("Semua Status", "Belum Dikumpulkan", "Sudah Dikumpulkan", "Terlambat");
        statusFilter.setValue("Semua Status");
        statusFilter.setPrefWidth(150);
        statusFilter.setPrefHeight(40);
        statusFilter.setStyle("-fx-background-color: white; " +
                            "-fx-background-radius: 10; " +
                            "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                            "-fx-border-radius: 10;");
        
        // Class filter
        classFilter = new ComboBox<>();
        classFilter.getItems().addAll("Semua Kelas", "Matematika", "Fisika", "Kimia", "Biologi", "Bahasa Indonesia");
        classFilter.setValue("Semua Kelas");
        classFilter.setPrefWidth(150);
        classFilter.setPrefHeight(40);
        classFilter.setStyle("-fx-background-color: white; " +
                           "-fx-background-radius: 10; " +
                           "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                           "-fx-border-radius: 10;");
        
        container.getChildren().addAll(searchField, statusFilter, classFilter);
        
        return container;
    }
    
    private void createTasksContainer() {
        tasksContainer = new VBox(15);
        tasksContainer.setPadding(new Insets(20));
        tasksContainer.setStyle("-fx-background-color: white; " +
                              "-fx-background-radius: 15; " +
                              "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                              "-fx-border-width: 1; " +
                              "-fx-border-radius: 15;");
        tasksContainer.setEffect(new DropShadow(10, Color.web("#E0E0E0")));
        
        // Sample tasks
        HBox task1 = createTaskItem("Tugas Matematika - Integral", "Matematika", "Bu Sari", "2024-01-15", "Belum Dikumpulkan", false);
        HBox task2 = createTaskItem("Laporan Praktikum Fisika", "Fisika", "Pak Budi", "2024-01-12", "Sudah Dikumpulkan", false);
        HBox task3 = createTaskItem("Proyek Biologi - Ekosistem", "Biologi", "Bu Rina", "2024-01-20", "Belum Dikumpulkan", true);
        HBox task4 = createTaskItem("Essay Bahasa Indonesia", "Bahasa Indonesia", "Pak Ahmad", "2024-01-10", "Terlambat", false);
        HBox task5 = createTaskItem("Eksperimen Kimia Organik", "Kimia", "Bu Dewi", "2024-01-18", "Belum Dikumpulkan", true);
        
        tasksContainer.getChildren().addAll(task1, task2, task3, task4, task5);
    }
    
    private HBox createTaskItem(String title, String className, String teacher, String deadline, String status, boolean isGroupTask) {
        HBox item = new HBox(20);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(15));
        item.setStyle("-fx-background-color: transparent; " +
                     "-fx-background-radius: 10; " +
                     "-fx-cursor: hand;");
        
        // Task icon
        Label taskIcon = new Label(isGroupTask ? "📚" : "📔");
        taskIcon.setFont(Font.font(24));
        taskIcon.setTextFill(Color.web("#123456"));
        
        // Task info
        VBox taskInfo = new VBox(5);
        taskInfo.setAlignment(Pos.CENTER_LEFT);
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        titleLabel.setTextFill(Color.web("#2D5016"));
        
        Label classLabel = new Label(className + " - " + teacher);
        classLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        classLabel.setTextFill(Color.web("#666666"));
        
        Label deadlineLabel = new Label("Deadline: " + deadline);
        deadlineLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        deadlineLabel.setTextFill(Color.web("#888888"));
        
        taskInfo.getChildren().addAll(titleLabel, classLabel, deadlineLabel);
        
        // Status badge
        Label statusBadge = new Label(status);
        statusBadge.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        statusBadge.setPadding(new Insets(5, 10, 5, 10));
        statusBadge.setStyle("-fx-background-radius: 15;");
        
        switch (status) {
            case "Belum Dikumpulkan":
                statusBadge.setStyle("-fx-background-color: #FFF3E0; " +
                                   "-fx-text-fill: #FF9800; " +
                                   "-fx-background-radius: 15;");
                break;
            case "Sudah Dikumpulkan":
                statusBadge.setStyle("-fx-background-color: #E8F5E9; " +
                                   "-fx-text-fill: #4CAF50; " +
                                   "-fx-background-radius: 15;");
                break;
            case "Terlambat":
                statusBadge.setStyle("-fx-background-color: #FFEBEE; " +
                                   "-fx-text-fill: #F44336; " +
                                   "-fx-background-radius: 15;");
                break;
        }
        
        // Group task indicator
        VBox rightContainer = new VBox(5);
        rightContainer.setAlignment(Pos.CENTER_RIGHT);
        rightContainer.getChildren().add(statusBadge);
        
        if (isGroupTask) {
            Label groupLabel = new Label("Tugas Kelompok");
            groupLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
            groupLabel.setTextFill(Color.web("#9C27B0"));
            rightContainer.getChildren().add(groupLabel);
        }
        
        item.getChildren().addAll(taskIcon, taskInfo, rightContainer);
        HBox.setHgrow(taskInfo, Priority.ALWAYS);
        
        // Click event
        //item.setOnMouseClicked(e -> showTaskDetailDialog(title, className, teacher, deadline, status, isGroupTask));
        item.setOnMouseClicked(e -> {
            if (navigationHandler != null) {
                navigationHandler.handleNavigation("TaskDetail:" + title + ":" + className + ":" + teacher + ":" + deadline + ":" + status + ":" + isGroupTask);
            }
        });

        // Hover effect
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
    
    public void show() {
        stage.show();
    }
}