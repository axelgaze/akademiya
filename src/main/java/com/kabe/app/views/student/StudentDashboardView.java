package com.kabe.app.views.student;

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
import com.kabe.app.models.PemberitahuanKelas;
import com.kabe.app.models.Tugas;
import com.kabe.app.models.User;
import java.time.Duration;

import java.util.List;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.kabe.app.controllers.KelasController;
import com.kabe.app.controllers.TugasController;
import com.kabe.app.controllers.UserController;
import com.kabe.app.views.interfaces.ViewInterface;

public class StudentDashboardView implements ViewInterface {
    private Stage stage;
    private Scene scene;
    private BorderPane root;
    private VBox sidebar;
    private VBox mainContent;
    private NavigationHandler navigationHandler;
    private UserController userController;
    private KelasController kelasController; // Tambahkan
    private TugasController tugasController; // Tambahkan
    
    public StudentDashboardView(Stage stage, UserController userController, 
                              KelasController kelasController, TugasController tugasController) {
        this.userController = userController;
        this.kelasController = kelasController; // Inisialisasi
        this.tugasController = tugasController; // Inisialisasi
        this.stage = stage;
        initializeView();
    }

    public void setNavigationHandler(NavigationHandler handler) {
        this.navigationHandler = handler;
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

        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true); // Konten menyesuaikan lebar
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Nonaktifkan scroll horizontal
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Aktifkan scroll vertikal jika diperlukan
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;"); // Hilangkan latar belakang
        
        root.setLeft(sidebar);
        root.setCenter(scrollPane);
        
        scene = new Scene(root, 1200, 800);
        stage.setTitle("Akademiya - Dashboard");
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
        
        Button dashboardBtn = createMenuButton("🏠 Dashboard", true);
        Button tasksBtn = createMenuButton("📋 Tugas", false);
        Button classesBtn = createMenuButton("🏫 Kelas", false);
        Button calendarBtn = createMenuButton("📅 Kalender", false);
        Button profileBtn = createMenuButton("👤 Profile", false);
        
        navigationMenu.getChildren().addAll(dashboardBtn, tasksBtn, classesBtn, calendarBtn);

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
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));
        
        Label headerTitle = new Label("Dashboard");
        headerTitle.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        headerTitle.setTextFill(Color.web("#2D5016"));
        
        Label headerSubtitle = new Label("Ringkasan aktivitas pembelajaran Anda");
        headerSubtitle.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        headerSubtitle.setTextFill(Color.web("#4A7C26"));
        
        VBox headerText = new VBox(5);
        headerText.getChildren().addAll(headerTitle, headerSubtitle);
        
        header.getChildren().add(headerText);
        
        // Statistics cards
        HBox statisticsContainer = createStatisticsCards();
        
        // Recent activities
        VBox recentActivities = createRecentActivities();
        
        mainContent.getChildren().addAll(header, statisticsContainer, recentActivities);
    }
    
    private HBox createStatisticsCards() {
        HBox container = new HBox(20);
        container.setAlignment(Pos.CENTER);
        
        // Ambil data tugas dari database
        int userId = userController.getUser().getId();
        List<Tugas> semuaTugas = tugasController.getTugasBySiswa(userId);
        
        // Hitung statistik
        long completed = semuaTugas.stream()
            .filter(t -> "Sudah Dikumpulkan".equals(tugasController.getStatusTugas(t.getId(), userId)))
            .count();
        
        long pending = semuaTugas.stream()
            .filter(t -> {
                String status = tugasController.getStatusTugas(t.getId(), userId);
                return "Belum Dikumpulkan".equals(status) || "dalam_proses".equals(status);
            })
            .count();
        
        long overdue = semuaTugas.stream()
            .filter(t -> {
                String status = tugasController.getStatusTugas(t.getId(), userId);
                return "Terlambat".equals(status);
            })
            .count();
        
        // Ambil jumlah kelas
        int totalKelas = kelasController.getClassesByUser(userId).size();
        
        // Buat card dengan data nyata
        VBox completedCard = createStatCard("✅", "Tugas Selesai", String.valueOf(completed), Color.web("#4CAF50"));
        VBox pendingCard = createStatCard("⏳", "Tugas Tertunda", String.valueOf(pending), Color.web("#FF9800"));
        VBox overdueCard = createStatCard("⚠️", "Tugas Terlambat", String.valueOf(overdue), Color.web("#F44336"));
        VBox classesCard = createStatCard("🏫", "Total Kelas", String.valueOf(totalKelas), Color.web("#2196F3"));
        
        container.getChildren().addAll(completedCard, pendingCard, overdueCard, classesCard);
        
        return container;
    }
    
    private VBox createStatCard(String icon, String title, String value, Color color) {
        VBox card = new VBox(15);
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(250);
        card.setPrefHeight(140);
        card.setPadding(new Insets(20));
        
        // Card background
        card.setStyle("-fx-background-color: white; " +
                     "-fx-background-radius: 15; " +
                     "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                     "-fx-border-width: 1; " +
                     "-fx-border-radius: 15;");
        card.setEffect(new DropShadow(10, Color.web("#E0E0E0")));
        
        // Icon
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(32));
        
        // Value
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        valueLabel.setTextFill(color);
        
        // Title
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        titleLabel.setTextFill(Color.web("#666666"));
        
        card.getChildren().addAll(iconLabel, valueLabel, titleLabel);
        
        // Hover effect
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
        
        // Section header
        Label sectionTitle = new Label("Aktivitas Terbaru");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        sectionTitle.setTextFill(Color.web("#2D5016"));
        
        // Activities container
        VBox activitiesContainer = new VBox(10);
        activitiesContainer.setPadding(new Insets(20));
        activitiesContainer.setStyle("-fx-background-color: white; " +
                                "-fx-background-radius: 15; " +
                                "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                                "-fx-border-width: 1; " +
                                "-fx-border-radius: 15;");
        activitiesContainer.setEffect(new DropShadow(10, Color.web("#E0E0E0")));
        
        // Ambil aktivitas terbaru dari database
        int userId = userController.getUser().getId();
        List<Kelas> kelasList = kelasController.getClassesByUser(userId);
        
        for (Kelas kelas : kelasList) {
            List<PemberitahuanKelas> pemberitahuanList = kelasController.getPemberitahuanKelas(kelas.getId());
            
            for (PemberitahuanKelas pemberitahuan : pemberitahuanList) {
                HBox activityItem = createActivityItem(
                    "📢", 
                    "Pemberitahuan dari "+kelasController.getKelasById(pemberitahuan.getIdKelas())+": "+pemberitahuan.getIsi(), 
                    formatTimeAgo(pemberitahuan.getCreatedTime()), 
                    Color.web("#4CAF50")
                );
                activitiesContainer.getChildren().add(activityItem);
            }
        }
        
        // Tambahkan juga notifikasi tugas
        List<Tugas> tugasList = tugasController.getTugasBySiswa(userId);
        for (Tugas tugas : tugasList) {
            HBox activityItem = createActivityItem(
                "📋", 
                "Tugas baru: " + tugas.getTitle() + " untuk kelas " + getNamaKelas(tugas.getKelasId()),
                formatTimeAgo(tugas.getCreatedAt()), 
                Color.web("#2196F3")
            );
            activitiesContainer.getChildren().add(activityItem);
        }
        
        container.getChildren().addAll(sectionTitle, activitiesContainer);
        
        return container;
    }
    
    private String formatTimeAgo(Timestamp timestamp) {
        if (timestamp == null) {
            return "Tidak diketahui";
        }
        return formatTimeAgo(timestamp.toLocalDateTime());
    }

    // Helper method untuk format waktu
    private String formatTimeAgo(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "Tidak diketahui";
        }
        
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(dateTime, now);
        
        long seconds = duration.getSeconds();
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long weeks = days / 7;
        long months = days / 30;
        long years = days / 365;
        
        if (years > 0) {
            return years + " tahun yang lalu";
        } else if (months > 0) {
            return months + " bulan yang lalu";
        } else if (weeks > 0) {
            return weeks + " minggu yang lalu";
        } else if (days > 0) {
            return days + " hari yang lalu";
        } else if (hours > 0) {
            return hours + " jam yang lalu";
        } else if (minutes > 0) {
            return minutes + " menit yang lalu";
        } else {
            return "Baru saja";
        }
    }

    // Helper method untuk mendapatkan nama kelas
    private String getNamaKelas(int kelasId) {
        Kelas kelas = kelasController.getKelasById(kelasId);
        return kelas != null ? kelas.getNama() : "Kelas Tidak Diketahui";
    }
    
    private HBox createActivityItem(String icon, String description, String time, Color color) {
        HBox item = new HBox(15);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(15));
        item.setStyle("-fx-background-color: transparent; " +
                     "-fx-background-radius: 10; " +
                     "-fx-cursor: hand;");
        
        // Icon container
        VBox iconContainer = new VBox();
        iconContainer.setAlignment(Pos.CENTER);
        iconContainer.setPrefWidth(40);
        iconContainer.setPrefHeight(40);
        iconContainer.setStyle("-fx-background-color: " + toHexString(color.deriveColor(0, 1, 1, 0.1)) + "; " +
                              "-fx-background-radius: 20;");
        
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(18));
        iconContainer.getChildren().add(iconLabel);
        
        // Text container
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