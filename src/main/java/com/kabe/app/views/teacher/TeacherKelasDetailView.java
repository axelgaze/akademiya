package com.kabe.app.views.teacher;

import com.kabe.app.controllers.KelasController;
import com.kabe.app.models.Kelas;
import com.kabe.app.models.User;
import com.kabe.app.views.interfaces.ViewInterface;
import com.kabe.app.models.PemberitahuanKelas;
import com.kabe.app.controllers.UserController;
import com.kabe.app.models.Material;

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

import java.time.Duration;
import java.sql.Timestamp;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TeacherKelasDetailView implements ViewInterface {
    private Stage stage;
    private Scene scene;
    private BorderPane root;
    private VBox mainContent;
    private Kelas kelasData;
    private NavigationHandler navigationHandler;
    private KelasController kelasController;
    private UserController userController;
    private File selectedFile;

    public void setNavigationHandler(NavigationHandler handler) {
        this.navigationHandler = handler;
    }

    public TeacherKelasDetailView(Stage stage, Kelas kelasData, KelasController kelasController, UserController userController) {
        this.stage = stage;
        this.kelasData = kelasData;
        this.kelasController = kelasController;
        this.userController = userController;
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
        VBox materiList = new VBox(10);
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        
        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 15, 0));
        
        Label headerLabel = new Label("Materi Kelas");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        headerLabel.setTextFill(Color.web("#4A148C"));
        
        // File upload components
        HBox uploadContainer = new HBox(10);
        uploadContainer.setAlignment(Pos.CENTER_LEFT);
        
        // File path label
        Label fileLabel = new Label("Belum ada file dipilih");
        fileLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        fileLabel.setTextFill(Color.web("#666666"));
        
        // File chooser button
        Button chooseFileBtn = new Button("Pilih File");
        chooseFileBtn.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        chooseFileBtn.setStyle("-fx-background-color: #673AB7; " +
                            "-fx-background-radius: 6; " +
                            "-fx-text-fill: white; " +
                            "-fx-cursor: hand; " +
                            "-fx-padding: 8 15 8 15;");
        
        // Upload button
        Button uploadBtn = new Button("Upload");
        uploadBtn.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        uploadBtn.setStyle("-fx-background-color: #4CAF50; " +
                        "-fx-background-radius: 6; " +
                        "-fx-text-fill: white; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 8 15 8 15;");
        uploadBtn.setDisable(true); // Disabled until file is selected
        
        // File chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih File Materi");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("All Files", "*.*"),
            new FileChooser.ExtensionFilter("PDF", "*.pdf"),
            new FileChooser.ExtensionFilter("PPT", "*.ppt", "*.pptx"),
            new FileChooser.ExtensionFilter("Word", "*.doc", "*.docx"),
            new FileChooser.ExtensionFilter("Video", "*.mp4", "*.mov"),
            new FileChooser.ExtensionFilter("Image", "*.png", "*.jpg", "*.jpeg")
        );
        
        chooseFileBtn.setOnAction(e -> {
            selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                fileLabel.setText(selectedFile.getName());
                uploadBtn.setDisable(false);
            }
        });
        
        uploadBtn.setOnAction(e -> {
            String fileName = fileLabel.getText();
            if (!fileName.equals("Belum ada file dipilih")) {
                // Dapatkan user_id dari session atau dari mana pun Anda menyimpannya
                int uploaderId = userController.getUser().getId(); // Anda perlu implementasi method ini
                
                boolean success = kelasController.uploadMateri(kelasData.getId(), selectedFile, uploaderId);
                
                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Upload Berhasil");
                    alert.setHeaderText(null);
                    alert.setContentText("Materi " + fileName + " berhasil diupload!");
                    alert.showAndWait();
                    
                    // Reset form
                    fileLabel.setText("Belum ada file dipilih");
                    uploadBtn.setDisable(true);
                    
                    // Refresh daftar materi (Anda perlu implementasi ini)
                    refreshMateriList(materiList);
                    
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Upload Gagal");
                    alert.setHeaderText(null);
                    alert.setContentText("Gagal mengupload materi " + fileName);
                    alert.showAndWait();
                }
            }
        });
        
        uploadContainer.getChildren().addAll(chooseFileBtn, fileLabel, uploadBtn);
        
        header.getChildren().addAll(headerLabel, uploadContainer);
        HBox.setHgrow(headerLabel, Priority.ALWAYS);
        
        // Sample materials
        ScrollPane materiScroll = new ScrollPane();
        materiScroll.setFitToWidth(true);
        materiScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        materiScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        materiScroll.setStyle("-fx-background-color: transparent; " +
                            "-fx-background: transparent;");
        
        refreshMateriList(materiList);
        materiScroll.setContent(materiList);
        
        content.getChildren().addAll(header, materiScroll);
        VBox.setVgrow(materiScroll, Priority.ALWAYS);
        
        return content;
    }

    private void refreshMateriList(VBox materiList) {
        materiList.getChildren().clear();
        
        List<Material> materials = kelasController.getMaterialsForKelas(kelasData.getId());
        
        if (materials.isEmpty()) {
            Label emptyLabel = new Label("Belum ada materi tersedia");
            emptyLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
            emptyLabel.setTextFill(Color.web("#666666"));
            materiList.getChildren().add(emptyLabel);
            return;
        }
        
        for (Material material : materials) {
            HBox materiItem = createMateriItem(material);
            
            // Tambahkan event handler untuk download
            materiItem.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2) { // Double click untuk download
                    downloadMaterial(material.getId(), material.getFileName());
                }
            });
            
            materiList.getChildren().add(materiItem);
        }
    }

    private String formatDate(Timestamp timestamp) {
        // Implementasi format tanggal sesuai kebutuhan
        return "pada " + timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }

    private void downloadMaterial(int materialId, String fileName) {
        byte[] fileData = kelasController.downloadMaterial(materialId);
        
        if (fileData != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Simpan File");
            fileChooser.setInitialFileName(fileName);
            File file = fileChooser.showSaveDialog(stage);
            
            if (file != null) {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(fileData);
                    
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Download Berhasil");
                    alert.setHeaderText(null);
                    alert.setContentText("File berhasil disimpan di: " + file.getAbsolutePath());
                    alert.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                    showErrorAlert("Gagal menyimpan file");
                }
            }
        } else {
            showErrorAlert("Gagal mengunduh file");
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String getFileIcon(String fileType) {
        switch (fileType.toLowerCase()) {
            case "pdf": return "📄";
            case "doc":
            case "docx": return "📝";
            case "ppt":
            case "pptx": return "📊";
            case "xls":
            case "xlsx": return "📈";
            case "mp4":
            case "mov":
            case "avi": return "🎥";
            case "jpg":
            case "jpeg":
            case "png": return "🖼️";
            case "zip":
            case "rar": return "🗄️";
            default: return "📁";
        }
    }

    private HBox createMateriItem(Material material) {
        String icon = getFileIcon(material.getFileType());
        String title = material.getFileName();
        String time = "Diposting " + formatDate(material.getCreatedAt());
        String type = material.getFileType().toUpperCase();
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

        downloadBtn.setOnAction(e -> handleDownload(material));
        
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

    private void handleDownload(Material material) {
        // Tampilkan dialog penyimpanan file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Simpan File");
        fileChooser.setInitialFileName(material.getFileName());
        
        // Set ekstensi default sesuai tipe file
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
            material.getFileType().toUpperCase() + " files", 
            "*." + material.getFileType()
        );
        fileChooser.getExtensionFilters().add(extFilter);
        
        File file = fileChooser.showSaveDialog(stage);
        
        if (file != null) {
            try {
                // Ambil data dari controller
                byte[] fileData = kelasController.downloadMaterial(material.getId());
                
                if (fileData != null && fileData.length > 0) {
                    // Tulis ke file
                    Files.write(file.toPath(), fileData);
                    
                    // Tampilkan notifikasi
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Download Berhasil");
                    alert.setHeaderText(null);
                    alert.setContentText("File berhasil disimpan di:\n" + file.getAbsolutePath());
                    alert.showAndWait();
                } else {
                    showErrorAlert("File kosong atau tidak valid");
                }
            } catch (IOException ex) {
                showErrorAlert("Gagal menyimpan file: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
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
        
        // Create a text field and button for adding notifications inline
        TextField newNotifField = new TextField();
        newNotifField.setPromptText("Tulis pemberitahuan baru...");
        newNotifField.setStyle("-fx-background-radius: 8; -fx-padding: 8;");
        HBox.setHgrow(newNotifField, Priority.ALWAYS);
        
        Button submitNotifBtn = new Button("Tambah");
        submitNotifBtn.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        submitNotifBtn.setStyle("-fx-background-color: #673AB7; " +
                            "-fx-background-radius: 6; " +
                            "-fx-text-fill: white; " +
                            "-fx-cursor: hand; " +
                            "-fx-padding: 8 15 8 15;");
        
        HBox addNotifBox = new HBox(10);
        addNotifBox.getChildren().addAll(newNotifField, submitNotifBtn);
        addNotifBox.setAlignment(Pos.CENTER_LEFT);
        
        // Notification list
        ScrollPane notifScroll = new ScrollPane();
        notifScroll.setFitToWidth(true);
        notifScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        notifScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        notifScroll.setStyle("-fx-background-color: transparent; " +
                        "-fx-background: transparent;");
        
        VBox notificationsList = new VBox(10);
        refreshPemberitahuanList(notificationsList);
        
        // Add action for submit button
        submitNotifBtn.setOnAction(e -> {
            String isi = newNotifField.getText().trim();
            if (!isi.isEmpty()) {
                kelasController.addPemberitahuan(kelasData.getId(), isi);
                newNotifField.clear();
                refreshPemberitahuanList(notificationsList);
            }
        });
        
        notifScroll.setContent(notificationsList);
        
        content.getChildren().addAll(header, addNotifBox, notifScroll);
        VBox.setVgrow(notifScroll, Priority.ALWAYS);
        
        return content;
    }

    private void refreshPemberitahuanList(VBox container) {
        container.getChildren().clear();
        
        List<PemberitahuanKelas> pemberitahuanList = kelasController.getPemberitahuanKelas(kelasData.getId());
        for (PemberitahuanKelas pemberitahuan : pemberitahuanList) {
            String isiPemberitahuan = pemberitahuan.getIsi();
            String waktuPemberitahuan = formatTimeAgo(pemberitahuan.getCreatedTime());
            HBox notif = createNotificationItem("📢", isiPemberitahuan, waktuPemberitahuan, pemberitahuan.getId());
            container.getChildren().add(notif);
        }
    }

    private HBox createNotificationItem(String icon, String message, String time, int pemberitahuanId) {
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
        
        // Delete button only
        Button deleteBtn = new Button("Hapus");
        deleteBtn.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        deleteBtn.setStyle("-fx-background-color: transparent; " +
                        "-fx-text-fill: #D32F2F; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 5;");
        
        deleteBtn.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Konfirmasi Hapus");
            confirm.setHeaderText("Hapus Pemberitahuan");
            confirm.setContentText("Apakah Anda yakin ingin menghapus pemberitahuan ini?");
            
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    kelasController.deletePemberitahuan(pemberitahuanId);
                    refreshPemberitahuanList((VBox) item.getParent());
                }
            });
        });
        
        item.getChildren().addAll(iconContainer, infoBox, deleteBtn);
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