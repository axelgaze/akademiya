package com.kabe.app.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.kabe.app.controllers.UserController;

public class KelasView {
    private Stage stage;
    private Scene scene;
    private BorderPane root;
    private VBox sidebar;
    private VBox mainContent;
    private GridPane kelasGrid;
    private TextField searchField;
    private ComboBox<String> filterComboBox;
    private NavigationHandler navigationHandler;
    private KelasData selectedKelas;

    public KelasData getSelectedKelas() {
    return selectedKelas;
    }

    public void setSelectedKelas(KelasData kelas) {
        this.selectedKelas = kelas;
    }

    public interface NavigationHandler {
        void handleNavigation(String viewName);
    }

    public void setNavigationHandler(NavigationHandler handler) {
        this.navigationHandler = handler;
    }
    
    // Sample data
    private ObservableList<KelasData> kelasList = FXCollections.observableArrayList();
    
    public KelasView(Stage stage) {
        this.stage = stage;
        initializeSampleData();
        initializeView();
    }
    
    private void initializeSampleData() {
        kelasList.addAll(
            new KelasData("Matematika Lanjutan", "Bu Sarah Wijaya", "MAT-301", 25, 
                         "Kelas matematika untuk siswa tingkat lanjut dengan fokus pada kalkulus dan statistik.", 
                         new String[]{"John Doe", "Jane Smith", "Michael Johnson", "Emily Davis", "Robert Brown"}),
            new KelasData("Fisika Quantum", "Pak Ahmad Rizki", "FIS-201", 20,
                         "Memahami konsep dasar fisika quantum dan aplikasinya dalam teknologi modern.",
                         new String[]{"Alice Wilson", "Bob Taylor", "Charlie Anderson", "Diana Martinez", "Edward Clark"}),
            new KelasData("Kimia Organik", "Dr. Lisa Chen", "KIM-401", 18,
                         "Studi mendalam tentang senyawa organik dan reaksi-reaksinya.",
                         new String[]{"Frank Lewis", "Grace Walker", "Henry Hall", "Ivy Young", "Jack King"}),
            new KelasData("Biologi Molekuler", "Prof. David Kumar", "BIO-501", 22,
                         "Eksplorasi struktur dan fungsi molekul dalam sistem biologis.",
                         new String[]{"Karen Wright", "Leo Lopez", "Mia Hill", "Noah Green", "Olivia Adams"}),
            new KelasData("Bahasa Indonesia", "Ibu Siti Nurhaliza", "BHS-101", 30,
                         "Pembelajaran bahasa Indonesia yang mencakup sastra dan tata bahasa.",
                         new String[]{"Paul Baker", "Quinn Rivera", "Ruby Campbell", "Sam Mitchell", "Tina Cooper"}),
            new KelasData("Sejarah Dunia", "Pak Bambang Sutrisno", "SEJ-201", 28,
                         "Memahami peristiwa-peristiwa penting dalam sejarah dunia.",
                         new String[]{"Uma Patel", "Victor Ross", "Wendy Morgan", "Xavier Bell", "Yara Foster"})
        );
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
        
        root.setLeft(sidebar);
        root.setCenter(mainContent);
        
        scene = new Scene(root, 1200, 800);
        stage.setTitle("Akademiya - Kelas");
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
        Button classesBtn = createMenuButton("🏫 Kelas", true);
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
        
        // Search and filter section
        HBox searchSection = createSearchSection();
        
        // Kelas grid
        ScrollPane scrollPane = createKelasGrid();
        
        mainContent.getChildren().addAll(header, searchSection, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
    }
    
    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));
        
        Label headerTitle = new Label("Kelas");
        headerTitle.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        headerTitle.setTextFill(Color.web("#2D5016"));
        
        Label headerSubtitle = new Label("Daftar kelas yang Anda ikuti");
        headerSubtitle.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        headerSubtitle.setTextFill(Color.web("#4A7C26"));
        
        VBox headerText = new VBox(5);
        headerText.getChildren().addAll(headerTitle, headerSubtitle);
        
        header.getChildren().add(headerText);
        
        return header;
    }
    
    private HBox createSearchSection() {
        HBox searchSection = new HBox(15);
        searchSection.setAlignment(Pos.CENTER_LEFT);
        searchSection.setPadding(new Insets(0, 0, 20, 0));
        
        // Search field
        searchField = new TextField();
        searchField.setPromptText("Cari kelas...");
        searchField.setPrefWidth(300);
        searchField.setPrefHeight(40);
        searchField.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        searchField.setStyle("-fx-background-color: white; " +
                           "-fx-background-radius: 10; " +
                           "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                           "-fx-border-width: 1; " +
                           "-fx-border-radius: 10; " +
                           "-fx-padding: 10;");
        
        // Filter dropdown
        filterComboBox = new ComboBox<>();
        filterComboBox.getItems().addAll("Semua Kelas", "Matematika", "Fisika", "Kimia", "Biologi", "Bahasa", "Sejarah");
        filterComboBox.setValue("Semua Kelas");
        filterComboBox.setPrefWidth(200);
        filterComboBox.setPrefHeight(40);
        filterComboBox.setStyle("-fx-background-color: white; " +
                              "-fx-background-radius: 10; " +
                              "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                              "-fx-border-width: 1; " +
                              "-fx-border-radius: 10;");
        
        searchSection.getChildren().addAll(searchField, filterComboBox);
        
        return searchSection;
    }
    
    private ScrollPane createKelasGrid() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; " +
                           "-fx-background: transparent;");
        
        kelasGrid = new GridPane();
        kelasGrid.setHgap(20);
        kelasGrid.setVgap(20);
        kelasGrid.setPadding(new Insets(10));
        
        populateKelasGrid();
        
        scrollPane.setContent(kelasGrid);
        
        return scrollPane;
    }
    
    private void populateKelasGrid() {
        kelasGrid.getChildren().clear();
        
        int column = 0;
        int row = 0;
        int maxColumns = 3;
        
        for (KelasData kelas : kelasList) {
            VBox kelasCard = createKelasCard(kelas);
            kelasGrid.add(kelasCard, column, row);
            
            column++;
            if (column >= maxColumns) {
                column = 0;
                row++;
            }
        }
    }
    
    private VBox createKelasCard(KelasData kelas) {
        VBox card = new VBox(15);
        card.setAlignment(Pos.TOP_LEFT);
        card.setPrefWidth(320);
        card.setPrefHeight(200);
        card.setPadding(new Insets(25));
        
        // Card background
        card.setStyle("-fx-background-color: white; " +
                     "-fx-background-radius: 15; " +
                     "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                     "-fx-border-width: 1; " +
                     "-fx-border-radius: 15; " +
                     "-fx-cursor: hand;");
        card.setEffect(new DropShadow(10, Color.web("#E0E0E0")));
        
        // Kelas code
        Label codeLabel = new Label(kelas.getKode());
        codeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        codeLabel.setTextFill(Color.web("#4A7C26"));
        codeLabel.setStyle("-fx-background-color: rgba(74, 124, 38, 0.1); " +
                          "-fx-background-radius: 6; " +
                          "-fx-padding: 4 8 4 8;");
        
        HBox codeContainer = new HBox();
        codeContainer.setAlignment(Pos.CENTER_LEFT);
        codeContainer.getChildren().add(codeLabel);
        
        // Kelas name
        Label nameLabel = new Label(kelas.getNama());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        nameLabel.setTextFill(Color.web("#2D5016"));
        nameLabel.setWrapText(true);
        
        // Pengajar
        Label pengajarLabel = new Label("👨‍🏫 " + kelas.getPengajar());
        pengajarLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        pengajarLabel.setTextFill(Color.web("#666666"));
        
        // Students count
        Label studentsLabel = new Label("👥 " + kelas.getJumlahSiswa() + " siswa");
        studentsLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        studentsLabel.setTextFill(Color.web("#888888"));
        
        card.getChildren().addAll(codeContainer, nameLabel, pengajarLabel, studentsLabel);
        
        // Hover effect
        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: white; " +
                         "-fx-background-radius: 15; " +
                         "-fx-border-color: #4A7C26; " +
                         "-fx-border-width: 2; " +
                         "-fx-border-radius: 15; " +
                         "-fx-cursor: hand;");
        });
        
        card.setOnMouseExited(e -> {
            card.setStyle("-fx-background-color: white; " +
                         "-fx-background-radius: 15; " +
                         "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                         "-fx-border-width: 1; " +
                         "-fx-border-radius: 15; " +
                         "-fx-cursor: hand;");
        });
        
        // Click handler
        card.setOnMouseClicked(e -> {
            if (navigationHandler != null) {
                setSelectedKelas(kelas);
                navigationHandler.handleNavigation("KelasDetail");
            }
        });
        
        return card;
    }
    
    private void showKelasDetail(KelasData kelas) {
        // Apply blur effect to main content
        BoxBlur blur = new BoxBlur(5, 5, 3);
        root.setEffect(blur);
        
        // Create modal stage
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initOwner(stage);
        modalStage.initStyle(StageStyle.TRANSPARENT);
        
        // Create modal content
        VBox modalContent = new VBox(20);
        modalContent.setAlignment(Pos.CENTER);
        modalContent.setPadding(new Insets(40));
        modalContent.setStyle("-fx-background-color: white; " +
                             "-fx-background-radius: 20; " +
                             "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                             "-fx-border-width: 1; " +
                             "-fx-border-radius: 20;");
        modalContent.setEffect(new DropShadow(20, Color.web("#333333")));
        modalContent.setPrefWidth(700);
        modalContent.setMaxHeight(600);
        
        // Header
        HBox modalHeader = new HBox();
        modalHeader.setAlignment(Pos.CENTER_LEFT);
        modalHeader.setPadding(new Insets(0, 0, 20, 0));
        
        VBox headerInfo = new VBox(5);
        
        Label modalTitle = new Label(kelas.getNama());
        modalTitle.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        modalTitle.setTextFill(Color.web("#2D5016"));
        
        Label modalSubtitle = new Label(kelas.getKode() + " • " + kelas.getPengajar());
        modalSubtitle.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        modalSubtitle.setTextFill(Color.web("#4A7C26"));
        
        headerInfo.getChildren().addAll(modalTitle, modalSubtitle);
        
        // Close button
        Button closeBtn = new Button("✕");
        closeBtn.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        closeBtn.setStyle("-fx-background-color: transparent; " +
                         "-fx-text-fill: #888888; " +
                         "-fx-cursor: hand; " +
                         "-fx-padding: 5;");
        closeBtn.setOnAction(e -> {
            modalStage.close();
            root.setEffect(null);
        });
        
        modalHeader.getChildren().addAll(headerInfo, closeBtn);
        HBox.setHgrow(headerInfo, Priority.ALWAYS);
        
        // Description
        Label descLabel = new Label(kelas.getDeskripsi());
        descLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        descLabel.setTextFill(Color.web("#666666"));
        descLabel.setWrapText(true);
        
        // Tabs
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setPrefHeight(300);
        
        // Anggota tab
        Tab anggotaTab = new Tab("Anggota Kelas");
        VBox anggotaContent = createAnggotaContent(kelas);
        anggotaTab.setContent(anggotaContent);
        
        // Materi tab
        Tab materiTab = new Tab("Materi");
        VBox materiContent = createMateriContent();
        materiTab.setContent(materiContent);
        
        // Pemberitahuan tab
        Tab pemberitahuanTab = new Tab("Pemberitahuan");
        VBox pemberitahuanContent = createPemberitahuanContent();
        pemberitahuanTab.setContent(pemberitahuanContent);
        
        tabPane.getTabs().addAll(anggotaTab, materiTab, pemberitahuanTab);
        
        modalContent.getChildren().addAll(modalHeader, descLabel, tabPane);
        
        // Modal scene
        StackPane modalRoot = new StackPane();
        modalRoot.setAlignment(Pos.CENTER);
        modalRoot.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        modalRoot.getChildren().add(modalContent);
        
        Scene modalScene = new Scene(modalRoot, 800, 700);
        modalScene.setFill(Color.TRANSPARENT);
        modalStage.setScene(modalScene);
        
        // Show modal
        modalStage.showAndWait();
        
        // Remove blur effect
        root.setEffect(null);
    }
    
    private VBox createAnggotaContent(KelasData kelas) {
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        
        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 15, 0));
        
        Label headerLabel = new Label("Anggota Kelas (" + kelas.getJumlahSiswa() + ")");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        headerLabel.setTextFill(Color.web("#2D5016"));
        
        header.getChildren().add(headerLabel);
        
        // Members list
        ScrollPane membersScroll = new ScrollPane();
        membersScroll.setFitToWidth(true);
        membersScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        membersScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        membersScroll.setStyle("-fx-background-color: transparent; " +
                              "-fx-background: transparent;");
        
        VBox membersList = new VBox(10);
        
        // Add teacher
        HBox teacherItem = createMemberItem("👨‍🏫", kelas.getPengajar(), "Pengajar", Color.web("#4A7C26"));
        membersList.getChildren().add(teacherItem);
        
        // Add students
        for (String student : kelas.getAnggota()) {
            HBox studentItem = createMemberItem("👨‍🎓", student, "Pelajar", Color.web("#2196F3"));
            membersList.getChildren().add(studentItem);
        }
        
        membersScroll.setContent(membersList);
        
        content.getChildren().addAll(header, membersScroll);
        VBox.setVgrow(membersScroll, Priority.ALWAYS);
        
        return content;
    }
    
    private HBox createMemberItem(String icon, String name, String role, Color color) {
        HBox item = new HBox(15);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(10));
        item.setStyle("-fx-background-color: rgba(0, 0, 0, 0.02); " +
                     "-fx-background-radius: 8;");
        
        // Icon
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(20));
        
        // Info
        VBox infoBox = new VBox(2);
        
        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        nameLabel.setTextFill(Color.web("#333333"));
        
        Label roleLabel = new Label(role);
        roleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        roleLabel.setTextFill(color);
        
        infoBox.getChildren().addAll(nameLabel, roleLabel);
        
        item.getChildren().addAll(iconLabel, infoBox);
        
        return item;
    }
    
    private VBox createMateriContent() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        
        // Header
        Label headerLabel = new Label("Materi Kelas");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        headerLabel.setTextFill(Color.web("#2D5016"));
        
        // Sample materials
        VBox materiList = new VBox(10);
        
        HBox materi1 = createMateriItem("📄", "Pengantar Matematika Lanjutan", "Diposting 2 hari yang lalu", "PDF");
        HBox materi2 = createMateriItem("📊", "Slide Presentasi Kalkulus", "Diposting 5 hari yang lalu", "PPTX");
        HBox materi3 = createMateriItem("🎥", "Video Tutorial Integral", "Diposting 1 minggu yang lalu", "MP4");
        
        materiList.getChildren().addAll(materi1, materi2, materi3);
        
        content.getChildren().addAll(headerLabel, materiList);
        
        return content;
    }
    
    private HBox createMateriItem(String icon, String title, String time, String type) {
        HBox item = new HBox(15);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(15));
        item.setStyle("-fx-background-color: rgba(0, 0, 0, 0.02); " +
                     "-fx-background-radius: 8; " +
                     "-fx-cursor: hand;");
        
        // Icon
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(20));
        
        // Info
        VBox infoBox = new VBox(5);
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        titleLabel.setTextFill(Color.web("#333333"));
        
        Label timeLabel = new Label(time);
        timeLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        timeLabel.setTextFill(Color.web("#888888"));
        
        infoBox.getChildren().addAll(titleLabel, timeLabel);
        
        // Type badge
        Label typeBadge = new Label(type);
        typeBadge.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        typeBadge.setTextFill(Color.web("#4A7C26"));
        typeBadge.setStyle("-fx-background-color: rgba(74, 124, 38, 0.1); " +
                          "-fx-background-radius: 4; " +
                          "-fx-padding: 2 6 2 6;");
        
        item.getChildren().addAll(iconLabel, infoBox, typeBadge);
        HBox.setHgrow(infoBox, Priority.ALWAYS);
        
        // Hover effect
        item.setOnMouseEntered(e -> {
            item.setStyle("-fx-background-color: rgba(0, 0, 0, 0.05); " +
                         "-fx-background-radius: 8; " +
                         "-fx-cursor: hand;");
        });
        
        item.setOnMouseExited(e -> {
            item.setStyle("-fx-background-color: rgba(0, 0, 0, 0.02); " +
                         "-fx-background-radius: 8; " +
                         "-fx-cursor: hand;");
        });
        
        return item;
    }
    
    private VBox createPemberitahuanContent() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        
        // Header
        Label headerLabel = new Label("Pemberitahuan");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        headerLabel.setTextFill(Color.web("#2D5016"));
        
        // Sample notifications
        VBox notificationsList = new VBox(10);
        
        HBox notif1 = createNotificationItem("📢", "Kelas besok dipindah ke ruang 205", "1 jam yang lalu");
        HBox notif2 = createNotificationItem("⚠️", "Ujian tengah semester akan dilaksanakan minggu depan", "3 jam yang lalu");
        HBox notif3 = createNotificationItem("📚", "Materi baru telah diupload ke sistem", "1 hari yang lalu");
        
        notificationsList.getChildren().addAll(notif1, notif2, notif3);
        
        content.getChildren().addAll(headerLabel, notificationsList);
        
        return content;
    }
    
    private HBox createNotificationItem(String icon, String message, String time) {
        HBox item = new HBox(15);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(15));
        item.setStyle("-fx-background-color: rgba(0, 0, 0, 0.02); " +
                     "-fx-background-radius: 8; " +
                     "-fx-cursor: hand;");
        
        // Icon container
        VBox iconContainer = new VBox();
        iconContainer.setAlignment(Pos.CENTER);
        iconContainer.setPrefWidth(35);
        iconContainer.setPrefHeight(35);
        iconContainer.setStyle("-fx-background-color: rgba(74, 124, 38, 0.1); " +
                              "-fx-background-radius: 17;");
        
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(16));
        iconContainer.getChildren().add(iconLabel);
        
        // Info
        VBox infoBox = new VBox(5);
        
        Label messageLabel = new Label(message);
        messageLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        messageLabel.setTextFill(Color.web("#333333"));
        messageLabel.setWrapText(true);
        
        Label timeLabel = new Label(time);
        timeLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        timeLabel.setTextFill(Color.web("#888888"));
        
        infoBox.getChildren().addAll(messageLabel, timeLabel);
        
        item.getChildren().addAll(iconContainer, infoBox);
        HBox.setHgrow(infoBox, Priority.ALWAYS);
        
        // Hover effect
        item.setOnMouseEntered(e -> {
            item.setStyle("-fx-background-color: rgba(0, 0, 0, 0.05); " +
                         "-fx-background-radius: 8; " +
                         "-fx-cursor: hand;");
        });
        
        item.setOnMouseExited(e -> {
            item.setStyle("-fx-background-color: rgba(0, 0, 0, 0.02); " +
                         "-fx-background-radius: 8; " +
                         "-fx-cursor: hand;");
        });
        
        return item;
    }
    
    public void show() {
        stage.show();
    }
    
    // Inner class for kelas data
    public static class KelasData {
        private String nama;
        private String pengajar;
        private String kode;
        private int jumlahSiswa;
        private String deskripsi;
        private String[] anggota;
        
        public KelasData(String nama, String pengajar, String kode, int jumlahSiswa, 
                        String deskripsi, String[] anggota) {
            this.nama = nama;
            this.pengajar = pengajar;
            this.kode = kode;
            this.jumlahSiswa = jumlahSiswa;
            this.deskripsi = deskripsi;
            this.anggota = anggota;
        }
        
        // Getters
        public String getNama() { return nama; }
        public String getPengajar() { return pengajar; }
        public String getKode() { return kode; }
        public int getJumlahSiswa() { return jumlahSiswa; }
        public String getDeskripsi() { return deskripsi; }
        public String[] getAnggota() { return anggota; }
        
        // Setters
        public void setNama(String nama) { this.nama = nama; }
        public void setPengajar(String pengajar) { this.pengajar = pengajar; }
        public void setKode(String kode) { this.kode = kode; }
        public void setJumlahSiswa(int jumlahSiswa) { this.jumlahSiswa = jumlahSiswa; }
        public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
        public void setAnggota(String[] anggota) { this.anggota = anggota; }
    }
}