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
import javafx.stage.Stage;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import com.kabe.app.controllers.UserController;

public class ProfileView {
    private Stage stage;
    private Scene scene;
    private BorderPane root;
    private VBox sidebar;
    private VBox mainContent;
    private boolean isEditMode = false;
    
    // Profile data fields
    private TextField nameField;
    private TextField emailField;
    private TextField phoneField;
    private TextArea bioTextArea;
    private Label avatarLabel;
    private Label joinDateLabel;
    private Label roleLabel;
    private Label usernameLabel;
    private NavigationHandler navigationHandler;

    public interface NavigationHandler {
        void handleNavigation(String viewName);
    }

    public void setNavigationHandler(NavigationHandler handler) {
        this.navigationHandler = handler;
    }
    
    public ProfileView(Stage stage) {
        this.stage = stage;
        initializeView();
    }
    
    private void initializeView() {
        root = new BorderPane();
        
        // Background with Sumeru-inspired gradient
        LinearGradient backgroundGradient = new LinearGradient(
            0, 0, 1, 1, true, null,
            new Stop(0, Color.web("#F1F8E9")), // Light green
            new Stop(0.5, Color.web("#E8F5E8")), // Very light green
            new Stop(1, Color.web("#C8E6C9"))  // Soft green
        );
        
        BackgroundFill backgroundFill = new BackgroundFill(backgroundGradient, null, null);
        root.setBackground(new Background(backgroundFill));
        
        // Create sidebar
        createSidebar();
        
        // Create main content
        createMainContent();
        
        // Wrap mainContent in a ScrollPane
        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true); // Konten menyesuaikan lebar
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Nonaktifkan scroll horizontal
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Aktifkan scroll vertikal jika diperlukan
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;"); // Hilangkan latar belakang
        
        root.setLeft(sidebar);
        root.setCenter(scrollPane); // Gunakan scrollPane sebagai pengganti mainContent
        
        scene = new Scene(root, 1200, 800);
        stage.setTitle("Akademiya - Profile");
        stage.setScene(scene);
    }
    
    private void createSidebar() {
        sidebar = new VBox(20);
        sidebar.setPrefWidth(280);
        sidebar.setPadding(new Insets(20));
        
        // Sidebar background
        LinearGradient sidebarGradient = new LinearGradient(
            0, 0, 1, 0, true, null,
            new Stop(0, Color.web("#2D5016")), // Dark forest green
            new Stop(1, Color.web("#3D6B1F"))  // Medium forest green
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
        
        Label logoSubtitle = new Label("Platform Pembelajaran");
        logoSubtitle.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        logoSubtitle.setTextFill(Color.web("#C8E6C9"));
        
        logoContainer.getChildren().addAll(logoLabel, logoSubtitle);
        
        // Navigation menu
        VBox navigationMenu = new VBox(10);
        navigationMenu.setPadding(new Insets(20, 0, 0, 0));
        
        Button dashboardBtn = createMenuButton("🏠 Dashboard", false);
        Button tasksBtn = createMenuButton("📋 Tugas", false);
        Button classesBtn = createMenuButton("🏫 Kelas", false);
        Button calendarBtn = createMenuButton("📅 Kalender", false);
        Button profileBtn = createMenuButton("👤 Profile", true);
        
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
        
        // User info at bottom
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
        
        // Hover effect
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
        HBox header = createHeader();
        
        // Profile content
        HBox profileContent = createProfileContent();
        
        mainContent.getChildren().addAll(header, profileContent);
    }
    
    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));
        
        VBox headerText = new VBox(5);
        
        Label headerTitle = new Label("Profile");
        headerTitle.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        headerTitle.setTextFill(Color.web("#2D5016"));
        
        Label headerSubtitle = new Label("Kelola informasi akun dan preferensi Anda");
        headerSubtitle.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        headerSubtitle.setTextFill(Color.web("#4A7C26"));
        
        headerText.getChildren().addAll(headerTitle, headerSubtitle);
        
        // Edit button
        Button editButton = new Button("✏️ Edit Profile");
        editButton.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        editButton.setStyle("-fx-background-color: #4CAF50; " +
                           "-fx-background-radius: 8; " +
                           "-fx-text-fill: white; " +
                           "-fx-cursor: hand; " +
                           "-fx-padding: 10 20;");
        
        editButton.setOnMouseEntered(e -> {
            editButton.setStyle("-fx-background-color: #45a049; " +
                               "-fx-background-radius: 8; " +
                               "-fx-text-fill: white; " +
                               "-fx-cursor: hand; " +
                               "-fx-padding: 10 20;");
        });
        
        editButton.setOnMouseExited(e -> {
            editButton.setStyle("-fx-background-color: #4CAF50; " +
                               "-fx-background-radius: 8; " +
                               "-fx-text-fill: white; " +
                               "-fx-cursor: hand; " +
                               "-fx-padding: 10 20;");
        });
        
        editButton.setOnAction(e -> toggleEditMode());
        
        header.getChildren().addAll(headerText, editButton);
        HBox.setHgrow(headerText, Priority.ALWAYS);
        
        return header;
    }
    
    private HBox createProfileContent() {
        HBox content = new HBox(30);
        content.setAlignment(Pos.TOP_CENTER);
        
        // Left side - Avatar and basic info
        VBox leftSide = createLeftSide();
        
        // Right side - Detailed information
        VBox rightSide = createRightSide();
        
        content.getChildren().addAll(leftSide, rightSide);
        
        return content;
    }
    
    private VBox createLeftSide() {
        VBox leftSide = new VBox(20);
        leftSide.setAlignment(Pos.TOP_CENTER);
        leftSide.setPrefWidth(350);
        leftSide.setPadding(new Insets(20));
        leftSide.setStyle("-fx-background-color: white; " +
                         "-fx-background-radius: 15; " +
                         "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                         "-fx-border-width: 1; " +
                         "-fx-border-radius: 15;");
        leftSide.setEffect(new DropShadow(10, Color.web("#E0E0E0")));
        
        // Avatar section
        VBox avatarSection = new VBox(15);
        avatarSection.setAlignment(Pos.CENTER);
        
        // Avatar container
        VBox avatarContainer = new VBox();
        avatarContainer.setAlignment(Pos.CENTER);
        avatarContainer.setPrefWidth(120);
        avatarContainer.setPrefHeight(120);
        avatarContainer.setStyle("-fx-background-color: #E8F5E8; " +
                               "-fx-background-radius: 60; " +
                               "-fx-border-color: #4CAF50; " +
                               "-fx-border-width: 3; " +
                               "-fx-border-radius: 60;");
        
        avatarLabel = new Label("👤");
        avatarLabel.setFont(Font.font(48));
        avatarLabel.setTextFill(Color.web("#4CAF50"));
        
        avatarContainer.getChildren().add(avatarLabel);
        
        // Change avatar button
        Button changeAvatarBtn = new Button("📷 Ganti Avatar");
        changeAvatarBtn.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        changeAvatarBtn.setStyle("-fx-background-color: transparent; " +
                               "-fx-text-fill: #4CAF50; " +
                               "-fx-cursor: hand; " +
                               "-fx-border-color: #4CAF50; " +
                               "-fx-border-width: 1; " +
                               "-fx-border-radius: 5; " +
                               "-fx-padding: 5 10;");
        
        changeAvatarBtn.setOnMouseEntered(e -> {
            changeAvatarBtn.setStyle("-fx-background-color: #4CAF50; " +
                                   "-fx-text-fill: white; " +
                                   "-fx-cursor: hand; " +
                                   "-fx-border-color: #4CAF50; " +
                                   "-fx-border-width: 1; " +
                                   "-fx-border-radius: 5; " +
                                   "-fx-padding: 5 10;");
        });
        
        changeAvatarBtn.setOnMouseExited(e -> {
            changeAvatarBtn.setStyle("-fx-background-color: transparent; " +
                                   "-fx-text-fill: #4CAF50; " +
                                   "-fx-cursor: hand; " +
                                   "-fx-border-color: #4CAF50; " +
                                   "-fx-border-width: 1; " +
                                   "-fx-border-radius: 5; " +
                                   "-fx-padding: 5 10;");
        });
        
        changeAvatarBtn.setOnAction(e -> handleAvatarChange());
        
        avatarSection.getChildren().addAll(avatarContainer, changeAvatarBtn);
        
        // Basic info section
        VBox basicInfoSection = new VBox(15);
        basicInfoSection.setAlignment(Pos.CENTER);
        
        Label nameLabel = new Label("John Doe");
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        nameLabel.setTextFill(Color.web("#2D5016"));
        
        roleLabel = new Label("Pelajar");
        roleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        roleLabel.setTextFill(Color.web("#4A7C26"));
        
        usernameLabel = new Label("@johndoe");
        usernameLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        usernameLabel.setTextFill(Color.web("#666666"));
        
        basicInfoSection.getChildren().addAll(nameLabel, roleLabel, usernameLabel);
        
        // Statistics section
        VBox statisticsSection = createStatisticsSection();
        
        leftSide.getChildren().addAll(avatarSection, basicInfoSection, statisticsSection);
        
        return leftSide;
    }
    
    private VBox createStatisticsSection() {
        VBox statisticsSection = new VBox(15);
        statisticsSection.setAlignment(Pos.CENTER);
        statisticsSection.setPadding(new Insets(20, 0, 0, 0));
        
        Label statsTitle = new Label("Statistik");
        statsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        statsTitle.setTextFill(Color.web("#2D5016"));
        
        HBox statsContainer = new HBox(20);
        statsContainer.setAlignment(Pos.CENTER);
        
        VBox completedStats = createMiniStatCard("12", "Tugas Selesai", Color.web("#4CAF50"));
        VBox pendingStats = createMiniStatCard("5", "Tugas Aktif", Color.web("#FF9800"));
        VBox classStats = createMiniStatCard("8", "Kelas Diikuti", Color.web("#2196F3"));
        
        statsContainer.getChildren().addAll(completedStats, pendingStats, classStats);
        
        statisticsSection.getChildren().addAll(statsTitle, statsContainer);
        
        return statisticsSection;
    }
    
    private VBox createMiniStatCard(String value, String label, Color color) {
        VBox card = new VBox(5);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(10));
        
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        valueLabel.setTextFill(color);
        
        Label labelText = new Label(label);
        labelText.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
        labelText.setTextFill(Color.web("#666666"));
        labelText.setWrapText(true);
        labelText.setAlignment(Pos.CENTER);
        
        card.getChildren().addAll(valueLabel, labelText);
        
        return card;
    }
    
    private VBox createRightSide() {
        VBox rightSide = new VBox(20);
        rightSide.setPrefWidth(650);
        
        // Personal information section
        VBox personalInfoSection = createPersonalInfoSection();
        
        // Account information section
        VBox accountInfoSection = createAccountInfoSection();
        
        // Activity section
        VBox activitySection = createActivitySection();
        
        rightSide.getChildren().addAll(personalInfoSection, accountInfoSection, activitySection);
        
        return rightSide;
    }
    
    private VBox createPersonalInfoSection() {
        VBox section = new VBox(20);
        section.setPadding(new Insets(20));
        section.setStyle("-fx-background-color: white; " +
                        "-fx-background-radius: 15; " +
                        "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 15;");
        section.setEffect(new DropShadow(10, Color.web("#E0E0E0")));
        
        Label sectionTitle = new Label("Informasi Personal");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        sectionTitle.setTextFill(Color.web("#2D5016"));
        
        // Form fields
        VBox formFields = new VBox(15);
        
        // Name field
        VBox nameFieldContainer = createFieldContainer("Nama Lengkap", "John Doe");
        nameField = (TextField) ((VBox) nameFieldContainer.getChildren().get(1)).getChildren().get(0);
        
        // Email field
        VBox emailFieldContainer = createFieldContainer("Email", "john.doe@example.com");
        emailField = (TextField) ((VBox) emailFieldContainer.getChildren().get(1)).getChildren().get(0);
        
        // Phone field
        VBox phoneFieldContainer = createFieldContainer("Nomor Telepon", "+62 812 3456 7890");
        phoneField = (TextField) ((VBox) phoneFieldContainer.getChildren().get(1)).getChildren().get(0);
        
        // Bio field
        VBox bioFieldContainer = createBioFieldContainer();
        
        formFields.getChildren().addAll(nameFieldContainer, emailFieldContainer, phoneFieldContainer, bioFieldContainer);
        
        section.getChildren().addAll(sectionTitle, formFields);
        
        return section;
    }
    
    private VBox createFieldContainer(String labelText, String value) {
        VBox container = new VBox(5);
        
        Label label = new Label(labelText);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        label.setTextFill(Color.web("#2D5016"));
        
        VBox inputContainer = new VBox();
        
        TextField field = new TextField(value);
        field.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        field.setStyle("-fx-background-color: #F5F5F5; " +
                      "-fx-border-color: #E0E0E0; " +
                      "-fx-border-width: 1; " +
                      "-fx-border-radius: 8; " +
                      "-fx-background-radius: 8; " +
                      "-fx-padding: 10;");
        field.setEditable(false);
        
        field.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                field.setStyle("-fx-background-color: white; " +
                              "-fx-border-color: #4CAF50; " +
                              "-fx-border-width: 2; " +
                              "-fx-border-radius: 8; " +
                              "-fx-background-radius: 8; " +
                              "-fx-padding: 10;");
            } else {
                field.setStyle("-fx-background-color: #F5F5F5; " +
                              "-fx-border-color: #E0E0E0; " +
                              "-fx-border-width: 1; " +
                              "-fx-border-radius: 8; " +
                              "-fx-background-radius: 8; " +
                              "-fx-padding: 10;");
            }
        });
        
        inputContainer.getChildren().add(field);
        container.getChildren().addAll(label, inputContainer);
        
        return container;
    }
    
    private VBox createBioFieldContainer() {
        VBox container = new VBox(5);
        
        Label label = new Label("Bio");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        label.setTextFill(Color.web("#2D5016"));
        
        bioTextArea = new TextArea("Saya adalah seorang pelajar yang antusias dalam belajar teknologi dan sains. " +
                                  "Saya senang berkolaborasi dalam proyek kelompok dan selalu berusaha memberikan yang terbaik.");
        bioTextArea.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        bioTextArea.setPrefRowCount(3);
        bioTextArea.setWrapText(true);
        bioTextArea.setStyle("-fx-background-color: #F5F5F5; " +
                            "-fx-border-color: #E0E0E0; " +
                            "-fx-border-width: 1; " +
                            "-fx-border-radius: 8; " +
                            "-fx-background-radius: 8; " +
                            "-fx-padding: 10;");
        bioTextArea.setEditable(false);
        
        bioTextArea.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                bioTextArea.setStyle("-fx-background-color: white; " +
                                    "-fx-border-color: #4CAF50; " +
                                    "-fx-border-width: 2; " +
                                    "-fx-border-radius: 8; " +
                                    "-fx-background-radius: 8; " +
                                    "-fx-padding: 10;");
            } else {
                bioTextArea.setStyle("-fx-background-color: #F5F5F5; " +
                                    "-fx-border-color: #E0E0E0; " +
                                    "-fx-border-width: 1; " +
                                    "-fx-border-radius: 8; " +
                                    "-fx-background-radius: 8; " +
                                    "-fx-padding: 10;");
            }
        });
        
        container.getChildren().addAll(label, bioTextArea);
        
        return container;
    }
    
    private VBox createAccountInfoSection() {
        VBox section = new VBox(20);
        section.setPadding(new Insets(20));
        section.setStyle("-fx-background-color: white; " +
                        "-fx-background-radius: 15; " +
                        "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 15;");
        section.setEffect(new DropShadow(10, Color.web("#E0E0E0")));
        
        Label sectionTitle = new Label("Informasi Akun");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        sectionTitle.setTextFill(Color.web("#2D5016"));
        
        // Account info grid
        VBox accountGrid = new VBox(15);
        
        HBox joinDateRow = createInfoRow("Bergabung", "15 Januari 2024");
        HBox lastLoginRow = createInfoRow("Login Terakhir", "Hari ini, 09:30 WIB");
        HBox accountTypeRow = createInfoRow("Tipe Akun", "Pelajar Premium");
        HBox statusRow = createInfoRow("Status", "Aktif");
        
        accountGrid.getChildren().addAll(joinDateRow, lastLoginRow, accountTypeRow, statusRow);
        
        // Change password button
        Button changePasswordBtn = new Button("🔒 Ganti Password");
        changePasswordBtn.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        changePasswordBtn.setStyle("-fx-background-color: transparent; " +
                                  "-fx-text-fill: #2196F3; " +
                                  "-fx-cursor: hand; " +
                                  "-fx-border-color: #2196F3; " +
                                  "-fx-border-width: 1; " +
                                  "-fx-border-radius: 8; " +
                                  "-fx-padding: 10 15;");
        
        changePasswordBtn.setOnMouseEntered(e -> {
            changePasswordBtn.setStyle("-fx-background-color: #2196F3; " +
                                      "-fx-text-fill: white; " +
                                      "-fx-cursor: hand; " +
                                      "-fx-border-color: #2196F3; " +
                                      "-fx-border-width: 1; " +
                                      "-fx-border-radius: 8; " +
                                      "-fx-padding: 10 15;");
        });
        
        changePasswordBtn.setOnMouseExited(e -> {
            changePasswordBtn.setStyle("-fx-background-color: transparent; " +
                                      "-fx-text-fill: #2196F3; " +
                                      "-fx-cursor: hand; " +
                                      "-fx-border-color: #2196F3; " +
                                      "-fx-border-width: 1; " +
                                      "-fx-border-radius: 8; " +
                                      "-fx-padding: 10 15;");
        });
        
        changePasswordBtn.setOnAction(e -> handlePasswordChange());
        
        section.getChildren().addAll(sectionTitle, accountGrid, changePasswordBtn);
        
        return section;
    }
    
    private HBox createInfoRow(String label, String value) {
        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER_LEFT);
        
        Label labelText = new Label(label);
        labelText.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        labelText.setTextFill(Color.web("#666666"));
        labelText.setPrefWidth(120);
        
        Label valueText = new Label(value);
        valueText.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        valueText.setTextFill(Color.web("#333333"));
        
        row.getChildren().addAll(labelText, valueText);
        
        return row;
    }
    
    private VBox createActivitySection() {
        VBox section = new VBox(15);
        section.setPadding(new Insets(20));
        section.setStyle("-fx-background-color: white; " +
                        "-fx-background-radius: 15; " +
                        "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 15;");
        section.setEffect(new DropShadow(10, Color.web("#E0E0E0")));
        
        Label sectionTitle = new Label("Aktivitas Terbaru");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        sectionTitle.setTextFill(Color.web("#2D5016"));
        
        VBox activitiesContainer = new VBox(10);
        
        // Sample recent activities
        HBox activity1 = createActivityItem("✅", "Menyelesaikan tugas Matematika Bab 5", "2 jam yang lalu");
        HBox activity2 = createActivityItem("📚", "Membaca materi Fisika - Gelombang", "1 hari yang lalu");
        HBox activity3 = createActivityItem("👥", "Bergabung dengan grup Proyek Biologi", "2 hari yang lalu");
        HBox activity4 = createActivityItem("🎯", "Mencapai target belajar mingguan", "3 hari yang lalu");
        
        activitiesContainer.getChildren().addAll(activity1, activity2, activity3, activity4);
        
        section.getChildren().addAll(sectionTitle, activitiesContainer);
        
        return section;
    }
    
    private HBox createActivityItem(String icon, String description, String time) {
        HBox item = new HBox(15);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(10));
        
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(16));
        iconLabel.setPrefWidth(30);
        iconLabel.setAlignment(Pos.CENTER);
        
        VBox textContainer = new VBox(2);
        textContainer.setAlignment(Pos.CENTER_LEFT);
        
        Label descriptionLabel = new Label(description);
        descriptionLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        descriptionLabel.setTextFill(Color.web("#333333"));
        
        Label timeLabel = new Label(time);
        timeLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        timeLabel.setTextFill(Color.web("#888888"));
        
        textContainer.getChildren().addAll(descriptionLabel, timeLabel);
        
        item.getChildren().addAll(iconLabel, textContainer);
        HBox.setHgrow(textContainer, Priority.ALWAYS);
        
        return item;
    }
    
    private void toggleEditMode() {
        isEditMode = !isEditMode;
        
        // Enable/disable editing for fields
        nameField.setEditable(isEditMode);
        emailField.setEditable(isEditMode);
        phoneField.setEditable(isEditMode);
        bioTextArea.setEditable(isEditMode);
        
        // Update field styles
        String editableStyle = "-fx-background-color: white; " +
                              "-fx-border-color: #4CAF50; " +
                              "-fx-border-width: 1; " +
                              "-fx-border-radius: 8; " +
                              "-fx-background-radius: 8; " +
                              "-fx-padding: 10;";
        
        String readOnlyStyle = "-fx-background-color: #F5F5F5; " +
                              "-fx-border-color: #E0E0E0; " +
                              "-fx-border-width: 1; " +
                              "-fx-border-radius: 8; " +
                              "-fx-background-radius: 8; " +
                              "-fx-padding: 10;";
        
        if (isEditMode) {
            nameField.setStyle(editableStyle);
            emailField.setStyle(editableStyle);
            phoneField.setStyle(editableStyle);
            bioTextArea.setStyle(editableStyle);
            
            // Show save/cancel buttons
            showEditButtons();
        } else {
            nameField.setStyle(readOnlyStyle);
            emailField.setStyle(readOnlyStyle);
            phoneField.setStyle(readOnlyStyle);
            bioTextArea.setStyle(readOnlyStyle);
            
            // Hide save/cancel buttons
            hideEditButtons();
        }
    }
    
    private void showEditButtons() {
        // Find the header and add save/cancel buttons
        HBox header = (HBox) mainContent.getChildren().get(0);
        
        // Remove existing edit button
        if (header.getChildren().size() > 1) {
            header.getChildren().remove(header.getChildren().size() - 1);
        }
        
        HBox editButtons = new HBox(10);
        editButtons.setAlignment(Pos.CENTER_RIGHT);
        
        Button saveButton = new Button("💾 Simpan");
        saveButton.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        saveButton.setStyle("-fx-background-color: #4CAF50; " +
                           "-fx-background-radius: 8; " +
                           "-fx-text-fill: white; " +
                           "-fx-cursor: hand; " +
                           "-fx-padding: 10 20;");
        
        saveButton.setOnMouseEntered(e -> {
            saveButton.setStyle("-fx-background-color: #45a049; " +
                               "-fx-background-radius: 8; " +
                               "-fx-text-fill: white; " +
                               "-fx-cursor: hand; " +
                               "-fx-padding: 10 20;");
        });
        
        saveButton.setOnMouseExited(e -> {
            saveButton.setStyle("-fx-background-color: #4CAF50; " +
                               "-fx-background-radius: 8; " +
                               "-fx-text-fill: white; " +
                               "-fx-cursor: hand; " +
                               "-fx-padding: 10 20;");
        });
        
        saveButton.setOnAction(e -> handleSaveProfile());
        
        Button cancelButton = new Button("❌ Batal");
        cancelButton.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        cancelButton.setStyle("-fx-background-color: #F44336; " +
                             "-fx-background-radius: 8; " +
                             "-fx-text-fill: white; " +
                             "-fx-cursor: hand; " +
                             "-fx-padding: 10 20;");
        
        cancelButton.setOnMouseEntered(e -> {
            cancelButton.setStyle("-fx-background-color: #da190b; " +
                                 "-fx-background-radius: 8; " +
                                 "-fx-text-fill: white; " +
                                 "-fx-cursor: hand; " +
                                 "-fx-padding: 10 20;");
        });
        
        cancelButton.setOnMouseExited(e -> {
            cancelButton.setStyle("-fx-background-color: #F44336; " +
                                 "-fx-background-radius: 8; " +
                                 "-fx-text-fill: white; " +
                                 "-fx-cursor: hand; " +
                                 "-fx-padding: 10 20;");
        });
        
        cancelButton.setOnAction(e -> handleCancelEdit());
        
        editButtons.getChildren().addAll(saveButton, cancelButton);
        header.getChildren().add(editButtons);
    }
    
    private void hideEditButtons() {
        // Find the header and restore edit button
        HBox header = (HBox) mainContent.getChildren().get(0);
        
        // Remove save/cancel buttons
        if (header.getChildren().size() > 1) {
            header.getChildren().remove(header.getChildren().size() - 1);
        }
        
        // Add back edit button
        Button editButton = new Button("✏️ Edit Profile");
        editButton.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        editButton.setStyle("-fx-background-color: #4CAF50; " +
                           "-fx-background-radius: 8; " +
                           "-fx-text-fill: white; " +
                           "-fx-cursor: hand; " +
                           "-fx-padding: 10 20;");
        
        editButton.setOnMouseEntered(e -> {
            editButton.setStyle("-fx-background-color: #45a049; " +
                               "-fx-background-radius: 8; " +
                               "-fx-text-fill: white; " +
                               "-fx-cursor: hand; " +
                               "-fx-padding: 10 20;");
        });
        
        editButton.setOnMouseExited(e -> {
            editButton.setStyle("-fx-background-color: #4CAF50; " +
                               "-fx-background-radius: 8; " +
                               "-fx-text-fill: white; " +
                               "-fx-cursor: hand; " +
                               "-fx-padding: 10 20;");
        });
        
        editButton.setOnAction(e -> toggleEditMode());
        
        header.getChildren().add(editButton);
    }
    
    private void handleSaveProfile() {
        // Here you would implement the save logic
        // For now, just show a confirmation and exit edit mode
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Profil Disimpan");
        alert.setHeaderText(null);
        alert.setContentText("Perubahan profil Anda telah berhasil disimpan.");
        
        // Style the alert dialog
        alert.getDialogPane().setStyle("-fx-background-color: white; " +
                                      "-fx-border-color: #4CAF50; " +
                                      "-fx-border-width: 2; " +
                                      "-fx-border-radius: 10;");
        
        alert.showAndWait();
        
        // Exit edit mode
        toggleEditMode();
    }
    
    private void handleCancelEdit() {
        // Reset fields to original values
        nameField.setText("John Doe");
        emailField.setText("john.doe@example.com");
        phoneField.setText("+62 812 3456 7890");
        bioTextArea.setText("Saya adalah seorang pelajar yang antusias dalam belajar teknologi dan sains. " +
                           "Saya senang berkolaborasi dalam proyek kelompok dan selalu berusaha memberikan yang terbaik.");
        
        // Exit edit mode
        toggleEditMode();
    }
    
    private void handleAvatarChange() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih Avatar");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            // Here you would implement the avatar upload logic
            // For now, just show a confirmation
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Avatar Diubah");
            alert.setHeaderText(null);
            alert.setContentText("Avatar Anda telah berhasil diubah!");
            
            alert.getDialogPane().setStyle("-fx-background-color: white; " +
                                          "-fx-border-color: #4CAF50; " +
                                          "-fx-border-width: 2; " +
                                          "-fx-border-radius: 10;");
            
            alert.showAndWait();
        }
    }
    
    private void handlePasswordChange() {
        // Create password change dialog
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Ganti Password");
        dialog.setHeaderText("Masukkan password baru Anda");
        
        // Set the button types
        ButtonType confirmButtonType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);
        
        // Create password fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        PasswordField currentPassword = new PasswordField();
        currentPassword.setPromptText("Password saat ini");
        PasswordField newPassword = new PasswordField();
        newPassword.setPromptText("Password baru");
        PasswordField confirmPassword = new PasswordField();
        confirmPassword.setPromptText("Konfirmasi password baru");
        
        grid.add(new Label("Password saat ini:"), 0, 0);
        grid.add(currentPassword, 1, 0);
        grid.add(new Label("Password baru:"), 0, 1);
        grid.add(newPassword, 1, 1);
        grid.add(new Label("Konfirmasi password:"), 0, 2);
        grid.add(confirmPassword, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        
        // Style the dialog
        dialog.getDialogPane().setStyle("-fx-background-color: white; " +
                                       "-fx-border-color: #2196F3; " +
                                       "-fx-border-width: 2; " +
                                       "-fx-border-radius: 10;");
        
        // Convert the result when the confirm button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                return newPassword.getText();
            }
            return null;
        });
        
        Optional<String> result = dialog.showAndWait();
        
        result.ifPresent(password -> {
            // Here you would implement the password change logic
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Password Diubah");
            alert.setHeaderText(null);
            alert.setContentText("Password Anda telah berhasil diubah!");
            
            alert.getDialogPane().setStyle("-fx-background-color: white; " +
                                          "-fx-border-color: #4CAF50; " +
                                          "-fx-border-width: 2; " +
                                          "-fx-border-radius: 10;");
            
            alert.showAndWait();
        });
    }
    
    public void show() {
        stage.show();
    }
}