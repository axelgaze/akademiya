package com.kabe.app.views.teacher;

import com.kabe.app.controllers.KelasController;
import com.kabe.app.models.Kelas;
import com.kabe.app.models.User;
import com.kabe.app.views.interfaces.ViewInterface;
import com.kabe.app.models.PemberitahuanKelas;

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

import java.time.Duration;

import java.time.LocalDateTime;
import java.util.List;

public class TeacherKelasDetailView implements ViewInterface {
    private Stage stage;
    private Scene scene;
    private BorderPane root;
    private VBox mainContent;
    private Kelas kelasData;
    private NavigationHandler navigationHandler;
    private KelasController kelasController;

    public void setNavigationHandler(NavigationHandler handler) {
        this.navigationHandler = handler;
    }

    public TeacherKelasDetailView(Stage stage, Kelas kelasData, KelasController kelasController) {
        this.stage = stage;
        this.kelasData = kelasData;
        this.kelasController = kelasController;
        initializeView();
    }

    private void initializeView() {
        root = new BorderPane();
        
        // Purple-themed background gradient
        LinearGradient backgroundGradient = new LinearGradient(
            0, 0, 1, 1, true, null,
            new Stop(0, Color.web("#F3E5F5")), // Light purple
            new Stop(0.5, Color.web("#EDE7F6")), // Very light purple
            new Stop(1, Color.web("#D1C4E9"))  // Soft purple
        );
        
        BackgroundFill backgroundFill = new BackgroundFill(backgroundGradient, null, null);
        root.setBackground(new Background(backgroundFill));
        
        createMainContent();
        root.setCenter(mainContent);
        
        scene = new Scene(root, 1200, 800);
        stage.setTitle("Akademiya - " + kelasData.getNama() + " (Guru)");
        stage.setScene(scene);
    }

    private void createMainContent() {
        mainContent = new VBox(30);
        mainContent.setPadding(new Insets(30));
        
        // Header with back button and disband button
        HBox header = createHeader();
        
        // Main content card
        VBox contentCard = createContentCard();
        
        mainContent.getChildren().addAll(header, contentCard);
        VBox.setVgrow(contentCard, Priority.ALWAYS);
    }

    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));
        
        // Back button
        Button backButton = new Button("← Kembali");
        backButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        backButton.setStyle("-fx-background-color: rgba(103, 58, 183, 0.1); " +
                           "-fx-background-radius: 8; " +
                           "-fx-text-fill: #673AB7; " +
                           "-fx-cursor: hand; " +
                           "-fx-padding: 10 20 10 20;");
        
        backButton.setOnMouseEntered(e -> {
            backButton.setStyle("-fx-background-color: rgba(103, 58, 183, 0.2); " +
                               "-fx-background-radius: 8; " +
                               "-fx-text-fill: #673AB7; " +
                               "-fx-cursor: hand; " +
                               "-fx-padding: 10 20 10 20;");
        });
        
        backButton.setOnMouseExited(e -> {
            backButton.setStyle("-fx-background-color: rgba(103, 58, 183, 0.1); " +
                               "-fx-background-radius: 8; " +
                               "-fx-text-fill: #673AB7; " +
                               "-fx-cursor: hand; " +
                               "-fx-padding: 10 20 10 20;");
        });
        
        backButton.setOnAction(e -> {
            if (navigationHandler != null) {
                navigationHandler.handleNavigation("Kelas");
            }
        });
        
        // Title section
        VBox titleSection = new VBox(5);
        
        Label headerTitle = new Label(kelasData.getNama());
        headerTitle.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        headerTitle.setTextFill(Color.web("#4A148C"));
        
        Label headerSubtitle = new Label(kelasData.getKode() + " • Anda adalah Pengajar");
        headerSubtitle.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        headerSubtitle.setTextFill(Color.web("#673AB7"));
        
        titleSection.getChildren().addAll(headerTitle, headerSubtitle);
        
        // Disband button
        Button disbandButton = new Button("Bubarkan Kelas");
        disbandButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        disbandButton.setStyle("-fx-background-color: #D32F2F; " +
                             "-fx-background-radius: 8; " +
                             "-fx-text-fill: white; " +
                             "-fx-cursor: hand; " +
                             "-fx-padding: 10 20 10 20;");
        
        disbandButton.setOnMouseEntered(e -> {
            disbandButton.setStyle("-fx-background-color: #B71C1C; " +
                                 "-fx-background-radius: 8; " +
                                 "-fx-text-fill: white; " +
                                 "-fx-cursor: hand; " +
                                 "-fx-padding: 10 20 10 20;");
        });
        
        disbandButton.setOnMouseExited(e -> {
            disbandButton.setStyle("-fx-background-color: #D32F2F; " +
                                 "-fx-background-radius: 8; " +
                                 "-fx-text-fill: white; " +
                                 "-fx-cursor: hand; " +
                                 "-fx-padding: 10 20 10 20;");
        });
        
        disbandButton.setOnAction(e -> {
            // Add logic to disband the class
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Konfirmasi Pembubaran");
            alert.setHeaderText("Bubarkan Kelas " + kelasData.getNama());
            alert.setContentText("Apakah Anda yakin ingin membubarkan kelas ini? Tindakan ini tidak dapat dibatalkan.");
            
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    kelasController.deleteClass(kelasData.getId());
                    if (navigationHandler != null) {
                        navigationHandler.handleNavigation("Kelas");
                    }
                }
            });
        });
        
        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        header.getChildren().addAll(backButton, titleSection, spacer, disbandButton);
        
        return header;
    }

    private VBox createContentCard() {
        VBox card = new VBox(20);
        card.setPadding(new Insets(30));
        card.setStyle("-fx-background-color: white; " +
                     "-fx-background-radius: 20; " +
                     "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                     "-fx-border-width: 1; " +
                     "-fx-border-radius: 20;");
        card.setEffect(new DropShadow(15, Color.web("#E0E0E0")));
        
        // Description
        Label descLabel = new Label(kelasData.getDeskripsi());
        descLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        descLabel.setTextFill(Color.web("#666666"));
        descLabel.setWrapText(true);
        
        // Tabs
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setPrefHeight(500);
        
        // Anggota tab
        Tab anggotaTab = new Tab("Anggota Kelas");
        VBox anggotaContent = createAnggotaContent();
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
        
        card.getChildren().addAll(descLabel, tabPane);
        VBox.setVgrow(tabPane, Priority.ALWAYS);
        
        return card;
    }

    private VBox createAnggotaContent() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        
        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 15, 0));
        
        Label headerLabel = new Label("Anggota Kelas (" + kelasData.getJumlahSiswa() + ")");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        headerLabel.setTextFill(Color.web("#4A148C"));
        
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
        HBox teacherItem = createMemberItem("👨‍🏫", kelasData.getNamaPengajar(), "Pengajar", Color.web("#673AB7"));
        membersList.getChildren().add(teacherItem);
        
        // Add students
        for (User student : kelasData.getDaftarSiswa()) {
            HBox studentItem = createMemberItem("👨‍🎓", student.getFullName(), "Pelajar", Color.web("#2196F3"));
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
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 15, 0));
        
        Label headerLabel = new Label("Materi Kelas");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        headerLabel.setTextFill(Color.web("#4A148C"));
        
        Button addMateriBtn = new Button("+ Tambah Materi");
        addMateriBtn.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        addMateriBtn.setStyle("-fx-background-color: #673AB7; " +
                             "-fx-background-radius: 6; " +
                             "-fx-text-fill: white; " +
                             "-fx-cursor: hand; " +
                             "-fx-padding: 8 15 8 15;");
        
        header.getChildren().addAll(headerLabel, addMateriBtn);
        HBox.setHgrow(headerLabel, Priority.ALWAYS);
        
        // Sample materials
        ScrollPane materiScroll = new ScrollPane();
        materiScroll.setFitToWidth(true);
        materiScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        materiScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        materiScroll.setStyle("-fx-background-color: transparent; " +
                             "-fx-background: transparent;");
        
        VBox materiList = new VBox(10);
        
        HBox materi1 = createMateriItem("📄", "Pengantar Matematika Lanjutan", "Diposting 2 hari yang lalu", "PDF");
        HBox materi2 = createMateriItem("📊", "Slide Presentasi Kalkulus", "Diposting 5 hari yang lalu", "PPTX");
        HBox materi3 = createMateriItem("🎥", "Video Tutorial Integral", "Diposting 1 minggu yang lalu", "MP4");
        HBox materi4 = createMateriItem("📝", "Latihan Soal Bab 1", "Diposting 1 minggu yang lalu", "DOCX");
        
        materiList.getChildren().addAll(materi1, materi2, materi3, materi4);
        materiScroll.setContent(materiList);
        
        content.getChildren().addAll(header, materiScroll);
        VBox.setVgrow(materiScroll, Priority.ALWAYS);
        
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
        typeBadge.setTextFill(Color.web("#673AB7"));
        typeBadge.setStyle("-fx-background-color: rgba(103, 58, 183, 0.1); " +
                          "-fx-background-radius: 4; " +
                          "-fx-padding: 2 6 2 6;");
        
        // Download button
        Button downloadBtn = new Button("⬇");
        downloadBtn.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        downloadBtn.setStyle("-fx-background-color: transparent; " +
                           "-fx-text-fill: #673AB7; " +
                           "-fx-cursor: hand; " +
                           "-fx-padding: 5;");
        
        // Delete button (only for teacher)
        Button deleteBtn = new Button("✕");
        deleteBtn.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        deleteBtn.setStyle("-fx-background-color: transparent; " +
                          "-fx-text-fill: #D32F2F; " +
                          "-fx-cursor: hand; " +
                          "-fx-padding: 5;");
        
        item.getChildren().addAll(iconLabel, infoBox, typeBadge, downloadBtn, deleteBtn);
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
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 15, 0));
        
        Label headerLabel = new Label("Pemberitahuan");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        headerLabel.setTextFill(Color.web("#4A148C"));
        
        Button addNotifBtn = new Button("+ Buat Pemberitahuan");
        addNotifBtn.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        addNotifBtn.setStyle("-fx-background-color: #673AB7; " +
                           "-fx-background-radius: 6; " +
                           "-fx-text-fill: white; " +
                           "-fx-cursor: hand; " +
                           "-fx-padding: 8 15 8 15;");
        
        header.getChildren().addAll(headerLabel, addNotifBtn);
        HBox.setHgrow(headerLabel, Priority.ALWAYS);
        
        // Sample notifications
        ScrollPane notifScroll = new ScrollPane();
        notifScroll.setFitToWidth(true);
        notifScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        notifScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        notifScroll.setStyle("-fx-background-color: transparent; " +
                           "-fx-background: transparent;");
        
        VBox notificationsList = new VBox(10);
        
        List<PemberitahuanKelas> pemberitahuanList = kelasController.getPemberitahuanKelas(kelasData.getId());
        for (PemberitahuanKelas pemberitahuan : pemberitahuanList) {
            String isiPemberitahuan = pemberitahuan.getIsi();
            String waktuPemberitahuan = formatTimeAgo(pemberitahuan.getCreatedTime());
            HBox notif = createNotificationItem("📢", isiPemberitahuan, waktuPemberitahuan);
            notificationsList.getChildren().add(notif);
        }

        notifScroll.setContent(notificationsList);
        
        content.getChildren().addAll(header, notifScroll);
        VBox.setVgrow(notifScroll, Priority.ALWAYS);
        
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
        iconContainer.setStyle("-fx-background-color: rgba(103, 58, 183, 0.1); " +
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
        
        // Options button with more actions for teacher
        MenuButton optionsBtn = new MenuButton("⋮");
        optionsBtn.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        optionsBtn.setStyle("-fx-background-color: transparent; " +
                          "-fx-text-fill: #888888; " +
                          "-fx-cursor: hand;");
        
        MenuItem editItem = new MenuItem("Edit");
        MenuItem deleteItem = new MenuItem("Hapus");
        optionsBtn.getItems().addAll(editItem, deleteItem);
        
        item.getChildren().addAll(iconContainer, infoBox, optionsBtn);
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

    public static String formatTimeAgo(LocalDateTime waktu) {
        LocalDateTime sekarang = LocalDateTime.now();
        Duration durasi = Duration.between(waktu, sekarang);

        long hari = durasi.toDays();
        long jam = durasi.toHours();
        long menit = durasi.toMinutes();

        if (hari > 1) {
            return hari + " hari yang lalu";
        } else if (jam > 1) {
            return jam + " jam yang lalu";
        } else {
            return menit + " menit yang lalu";
        }
    }

    public void show() {
        stage.show();
    }

    public Scene getScene() {
        return scene;
    }
}