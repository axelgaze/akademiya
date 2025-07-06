package com.kabe.app.views.teacher;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.List;
import java.util.ArrayList;

import com.kabe.app.controllers.KelasController;
import com.kabe.app.controllers.UserController;
import com.kabe.app.views.interfaces.KelasInterface;
import com.kabe.app.models.Kelas;
import com.kabe.app.models.*;

public class TeacherKelasView implements KelasInterface {
    private Stage stage;
    private Scene scene;
    private BorderPane root;
    private VBox sidebar;
    private VBox mainContent;
    private GridPane kelasGrid;
    private TextField searchField;
    private ComboBox<String> filterComboBox;
    private NavigationHandler navigationHandler;
    private Kelas selectedKelas;
    private UserController userController;
    private KelasController kelasController;

    public Kelas getSelectedKelas() {
        return selectedKelas;
    }

    public void setSelectedKelas(Kelas kelas) {
        this.selectedKelas = kelas;
    }

    public void setNavigationHandler(NavigationHandler handler) {
        this.navigationHandler = handler;
    }
    
    // Sample data
    private ObservableList<Kelas> kelasList = FXCollections.observableArrayList();
    
    public TeacherKelasView(Stage stage, UserController userController) {
        this.stage = stage;
        this.userController = userController;
        kelasController = new KelasController();
        initializeSampleData();
        initializeView();
    }
    
    private void initializeSampleData() {
        // For teacher view, show classes they teach
        String teacherName = userController.getUser().getFullName();
        kelasList.addAll(
            kelasController.getClassesByTeacher(userController.getUser().getId())
        );

        for (Kelas kelas : kelasList) {
            kelas.setJumlahSiswa(kelasController.getStudentCount(userController.getUser().getId()));
        }

        for (Kelas kelas : kelasList) {
            kelasController.assignTeacherToClass(kelas.getId(), userController.getUser().getId());
            kelas.setNamaPengajar(kelasController.getClassTeacher(kelas.getId()).getFullName());
        }
        
        for (Kelas kelas : kelasList) {
            List<User> daftarSiswaDatabase = kelasController.getStudentsByClass(kelas.getId());
            List<User> daftarSiswa = new ArrayList<>();
            for (User siswa : daftarSiswaDatabase) {
                daftarSiswa.add(siswa);
            }

            kelas.setDaftarSiswa(daftarSiswa);
        }

        for (Kelas kelas : kelasList) {
            kelas.setJumlahSiswa(kelasController.getStudentCount(kelas.getId())+1);
        }
    }
    
    private void initializeView() {
        root = new BorderPane();
        
        // Background with gradient
        LinearGradient backgroundGradient = new LinearGradient(
            0, 0, 1, 1, true, null,
            new Stop(0, Color.web("#F3E5F5")),
            new Stop(0.5, Color.web("#E8EAF6")),
            new Stop(1, Color.web("#C5CAE9"))
        );
        
        BackgroundFill backgroundFill = new BackgroundFill(backgroundGradient, null, null);
        root.setBackground(new Background(backgroundFill));
        
        createSidebar();
        createMainContent();

        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        
        root.setLeft(sidebar);
        root.setCenter(scrollPane);
        
        scene = new Scene(root, 1200, 800);
        stage.setTitle("Akademiya - Kelas (Guru)");
        stage.setScene(scene);
    }
    
    private void createSidebar() {
        sidebar = new VBox(20);
        sidebar.setPrefWidth(280);
        sidebar.setPadding(new Insets(20));
        
        // Sidebar background
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
        Button tasksBtn = createMenuButton("📋 Tugas", false);
        Button classesBtn = createMenuButton("🏫 Kelas", true);
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
        
        // User info
        VBox userInfo = new VBox(10);
        userInfo.setAlignment(Pos.CENTER);
        userInfo.setPadding(new Insets(20, 0, 0, 0));
        
        Label userAvatar = new Label("👨‍🏫"); // Teacher icon
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
        
        // Header with create button
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));
        
        VBox headerText = new VBox(5);
        
        Label headerTitle = new Label("Kelas Saya");
        headerTitle.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        headerTitle.setTextFill(Color.web("#4A148C"));
        
        Label headerSubtitle = new Label("Daftar kelas yang Anda ajar");
        headerSubtitle.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        headerSubtitle.setTextFill(Color.web("#7B1FA2"));
        
        headerText.getChildren().addAll(headerTitle, headerSubtitle);
        
        // Create class button
        Button createBtn = new Button("+ Buat Kelas Baru");
        createBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        createBtn.setStyle("-fx-background-color: #9C27B0; " +
                         "-fx-text-fill: white; " +
                         "-fx-background-radius: 8; " +
                         "-fx-padding: 10 20 10 20; " +
                         "-fx-cursor: hand;");
        createBtn.setOnMouseEntered(e -> {
            createBtn.setStyle("-fx-background-color: #7B1FA2; " +
                             "-fx-text-fill: white; " +
                             "-fx-background-radius: 8; " +
                             "-fx-padding: 10 20 10 20; " +
                             "-fx-cursor: hand;");
        });
        createBtn.setOnMouseExited(e -> {
            createBtn.setStyle("-fx-background-color: #9C27B0; " +
                             "-fx-text-fill: white; " +
                             "-fx-background-radius: 8; " +
                             "-fx-padding: 10 20 10 20; " +
                             "-fx-cursor: hand;");
        });
        createBtn.setOnAction(e -> {
            if (navigationHandler != null) {
                navigationHandler.handleNavigation("CreateClass");
            }
        });
        
        HBox.setHgrow(headerText, Priority.ALWAYS);
        header.getChildren().addAll(headerText, createBtn);
        
        // Search and filter section
        HBox searchSection = createSearchSection();
        
        // Kelas grid
        ScrollPane scrollPane = createKelasGrid();
        
        mainContent.getChildren().addAll(header, searchSection, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
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
        
        for (Kelas kelas : kelasList) {
            VBox kelasCard = createKelasCard(kelas);
            kelasGrid.add(kelasCard, column, row);
            
            column++;
            if (column >= maxColumns) {
                column = 0;
                row++;
            }
        }
    }
    
    private VBox createKelasCard(Kelas kelas) {
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
        
        // Students count
        Label studentsLabel = new Label("👥 " + kelasController.getStudentCount(kelas.getId()) + " siswa");
        studentsLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        studentsLabel.setTextFill(Color.web("#888888"));
        
        card.getChildren().addAll(codeContainer, nameLabel, studentsLabel);
        
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

    public int getSelectedKelasStudentCount(Kelas kelas) {
        return kelas.getJumlahSiswa();
    }

    public KelasController getKelasController() {
        return kelasController;
    }
    
    public void show() {
        stage.show();
    }
    
}