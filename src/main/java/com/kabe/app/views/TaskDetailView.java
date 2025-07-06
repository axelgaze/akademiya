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
import java.util.List;

public class TaskDetailView {
    private Stage stage;
    private Scene scene;
    private VBox root;
    private NavigationHandler navigationHandler;
    
    // Task data
    private String title;
    private String className;
    private String teacher;
    private String deadline;
    private String status;
    private boolean isGroupTask;

    public interface NavigationHandler {
        void handleNavigation(String viewName);
    }

    public void setNavigationHandler(NavigationHandler handler) {
        this.navigationHandler = handler;
    }
    
    public TaskDetailView(Stage stage, String title, String className, String teacher, 
                         String deadline, String status, boolean isGroupTask) {
        this.stage = stage;
        this.title = title;
        this.className = className;
        this.teacher = teacher;
        this.deadline = deadline;
        this.status = status;
        this.isGroupTask = isGroupTask;
        initializeView();
    }
    
    private void initializeView() {
        root = new VBox(20);
        root.setPadding(new Insets(30));
        
        // Background with gradient
        LinearGradient backgroundGradient = new LinearGradient(
            0, 0, 1, 1, true, null,
            new Stop(0, Color.web("#F1F8E9")),
            new Stop(0.5, Color.web("#E8F5E8")),
            new Stop(1, Color.web("#C8E6C9"))
        );
        
        BackgroundFill backgroundFill = new BackgroundFill(backgroundGradient, null, null);
        root.setBackground(new Background(backgroundFill));
        
        createHeader();
        createTaskDetails();
        createTabPane();
        
        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        
        scene = new Scene(scrollPane, 1200, 800);
        stage.setTitle("Akademiya - Detail Tugas");
        stage.setScene(scene);
    }
    
    private void createHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));
        
        // Back button
        Button backBtn = new Button("← Kembali");
        backBtn.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        backBtn.setStyle("-fx-background-color: #4CAF50; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 10 20;");
        backBtn.setOnAction(e -> {
            if (navigationHandler != null) {
                navigationHandler.handleNavigation("Tugas");
            }
        });
        
        // Header text
        VBox headerText = new VBox(5);
        
        Label headerTitle = new Label("Detail Tugas");
        headerTitle.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        headerTitle.setTextFill(Color.web("#2D5016"));
        
        Label headerSubtitle = new Label("Informasi lengkap tentang tugas Anda");
        headerSubtitle.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        headerSubtitle.setTextFill(Color.web("#4A7C26"));
        
        headerText.getChildren().addAll(headerTitle, headerSubtitle);
        
        header.getChildren().addAll(backBtn, headerText);
        root.getChildren().add(header);
    }
    
    private void createTaskDetails() {
        VBox taskDetailsContainer = new VBox(20);
        taskDetailsContainer.setPadding(new Insets(30));
        taskDetailsContainer.setStyle("-fx-background-color: white; " +
                                    "-fx-background-radius: 15; " +
                                    "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                                    "-fx-border-width: 1; " +
                                    "-fx-border-radius: 15;");
        taskDetailsContainer.setEffect(new DropShadow(10, Color.web("#E0E0E0")));
        
        // Task title and icon
        HBox titleContainer = new HBox(15);
        titleContainer.setAlignment(Pos.CENTER_LEFT);
        
        Label taskIcon = new Label(isGroupTask ? "👥" : "📋");
        taskIcon.setFont(Font.font(32));
        
        VBox titleInfo = new VBox(5);
        
        Label taskTitle = new Label(title);
        taskTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        taskTitle.setTextFill(Color.web("#2D5016"));
        
        if (isGroupTask) {
            Label groupLabel = new Label("Tugas Kelompok");
            groupLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
            groupLabel.setTextFill(Color.web("#9C27B0"));
            titleInfo.getChildren().add(groupLabel);
        }
        
        titleInfo.getChildren().add(taskTitle);
        titleContainer.getChildren().addAll(taskIcon, titleInfo);
        
        // Task information grid
        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(30);
        infoGrid.setVgap(15);
        infoGrid.setPadding(new Insets(20, 0, 0, 0));
        
        // Class info
        Label classLabel = new Label("Kelas:");
        classLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        classLabel.setTextFill(Color.web("#666666"));
        
        Label classValue = new Label(className);
        classValue.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        classValue.setTextFill(Color.web("#333333"));
        
        // Teacher info
        Label teacherLabel = new Label("Pengajar:");
        teacherLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        teacherLabel.setTextFill(Color.web("#666666"));
        
        Label teacherValue = new Label(teacher);
        teacherValue.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        teacherValue.setTextFill(Color.web("#333333"));
        
        // Deadline info
        Label deadlineLabel = new Label("Deadline:");
        deadlineLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        deadlineLabel.setTextFill(Color.web("#666666"));
        
        Label deadlineValue = new Label(deadline);
        deadlineValue.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        deadlineValue.setTextFill(Color.web("#333333"));
        
        // Status info
        Label statusLabel = new Label("Status:");
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        statusLabel.setTextFill(Color.web("#666666"));
        
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
        
        infoGrid.add(classLabel, 0, 0);
        infoGrid.add(classValue, 1, 0);
        infoGrid.add(teacherLabel, 0, 1);
        infoGrid.add(teacherValue, 1, 1);
        infoGrid.add(deadlineLabel, 0, 2);
        infoGrid.add(deadlineValue, 1, 2);
        infoGrid.add(statusLabel, 0, 3);
        infoGrid.add(statusBadge, 1, 3);
        
        // Description
        VBox descriptionContainer = new VBox(10);
        descriptionContainer.setPadding(new Insets(20, 0, 0, 0));
        
        Label descriptionLabel = new Label("Deskripsi:");
        descriptionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        descriptionLabel.setTextFill(Color.web("#2D5016"));
        
        Label description = new Label("Kerjakan soal-soal yang telah diberikan dengan lengkap dan benar. " +
                                    "Sertakan langkah-langkah penyelesaian yang jelas dan rapi. " +
                                    "Pastikan semua jawaban disertai dengan penjelasan yang memadai dan " +
                                    "gunakan metode yang telah dipelajari di kelas.");
        description.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        description.setTextFill(Color.web("#333333"));
        description.setWrapText(true);
        
        descriptionContainer.getChildren().addAll(descriptionLabel, description);
        
        taskDetailsContainer.getChildren().addAll(titleContainer, infoGrid, descriptionContainer);
        root.getChildren().add(taskDetailsContainer);
    }
    
    private void createTabPane() {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setStyle("-fx-background-color: white; " +
                        "-fx-background-radius: 15; " +
                        "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 15;");
        tabPane.setEffect(new DropShadow(10, Color.web("#E0E0E0")));
        
        // Files tab
        Tab filesTab = new Tab("File Submission");
        VBox filesContent = createFilesTabContent();
        filesTab.setContent(filesContent);
        
        tabPane.getTabs().add(filesTab);
        
        // Group members tab (if group task)
        if (isGroupTask) {
            Tab groupTab = new Tab("Anggota Kelompok");
            VBox groupContent = createGroupTabContent();
            groupTab.setContent(groupContent);
            tabPane.getTabs().add(groupTab);
        }
        
        root.getChildren().add(tabPane);
    }
    
    private VBox createFilesTabContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        
        // Upload area
        VBox uploadArea = new VBox(15);
        uploadArea.setAlignment(Pos.CENTER);
        uploadArea.setPadding(new Insets(40));
        uploadArea.setStyle("-fx-background-color: #F8F9FA; " +
                           "-fx-border-color: #DEE2E6; " +
                           "-fx-border-width: 2; " +
                           "-fx-border-style: dashed; " +
                           "-fx-border-radius: 15; " +
                           "-fx-background-radius: 15;");
        
        Label uploadIcon = new Label("📁");
        uploadIcon.setFont(Font.font(48));
        
        Label uploadText = new Label("Klik untuk mengunggah file atau drag & drop");
        uploadText.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        uploadText.setTextFill(Color.web("#666666"));
        
        Label uploadSubtext = new Label("Mendukung PDF, DOC, DOCX, XLS, XLSX (Max 10MB)");
        uploadSubtext.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        uploadSubtext.setTextFill(Color.web("#888888"));
        
        Button uploadBtn = new Button("Pilih File");
        uploadBtn.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        uploadBtn.setStyle("-fx-background-color: #4CAF50; " +
                          "-fx-text-fill: white; " +
                          "-fx-background-radius: 10; " +
                          "-fx-cursor: hand; " +
                          "-fx-padding: 15 30;");
        
        uploadBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Pilih File Tugas");
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                new FileChooser.ExtensionFilter("Word Documents", "*.docx", "*.doc"),
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx", "*.xls")
            );
            
            List<File> files = fileChooser.showOpenMultipleDialog(stage);
            if (files != null) {
                for (File file : files) {
                    System.out.println("Selected file: " + file.getName());
                }
            }
        });
        
        uploadArea.getChildren().addAll(uploadIcon, uploadText, uploadSubtext, uploadBtn);
        
        // Uploaded files section
        VBox filesSection = new VBox(15);
        
        Label filesLabel = new Label("File yang Diunggah:");
        filesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        filesLabel.setTextFill(Color.web("#2D5016"));
        
        VBox filesList = new VBox(10);
        
        // Sample uploaded files
        HBox file1 = createFileItem("Tugas_Matematika_John.pdf", "2.3 MB", "2024-01-14 10:30");
        HBox file2 = createFileItem("Lampiran_Soal.docx", "1.8 MB", "2024-01-14 10:32");
        
        filesList.getChildren().addAll(file1, file2);
        
        filesSection.getChildren().addAll(filesLabel, filesList);
        
        // Submit button
        Button submitBtn = new Button("Kumpulkan Tugas");
        submitBtn.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        submitBtn.setStyle("-fx-background-color: #FF9800; " +
                          "-fx-text-fill: white; " +
                          "-fx-background-radius: 10; " +
                          "-fx-cursor: hand; " +
                          "-fx-padding: 15 30;");
        
        content.getChildren().addAll(uploadArea, filesSection, submitBtn);
        
        return content;
    }
    
    private HBox createFileItem(String fileName, String fileSize, String uploadTime) {
        HBox fileItem = new HBox(15);
        fileItem.setAlignment(Pos.CENTER_LEFT);
        fileItem.setPadding(new Insets(15));
        fileItem.setStyle("-fx-background-color: #F8F9FA; " +
                         "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                         "-fx-border-radius: 10; " +
                         "-fx-background-radius: 10;");
        
        Label fileIcon = new Label("📄");
        fileIcon.setFont(Font.font(24));
        
        VBox fileInfo = new VBox(5);
        
        Label nameLabel = new Label(fileName);
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        nameLabel.setTextFill(Color.web("#333333"));
        
        Label sizeLabel = new Label(fileSize + " • " + uploadTime);
        sizeLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        sizeLabel.setTextFill(Color.web("#888888"));
        
        fileInfo.getChildren().addAll(nameLabel, sizeLabel);
        
        HBox actions = new HBox(10);
        actions.setAlignment(Pos.CENTER_RIGHT);
        
        Button downloadBtn = new Button("⬇");
        downloadBtn.setFont(Font.font(14));
        downloadBtn.setStyle("-fx-background-color: #2196F3; " +
                            "-fx-text-fill: white; " +
                            "-fx-cursor: hand; " +
                            "-fx-background-radius: 15; " +
                            "-fx-min-width: 30; " +
                            "-fx-min-height: 30;");
        
        Button removeBtn = new Button("✕");
        removeBtn.setFont(Font.font(14));
        removeBtn.setStyle("-fx-background-color: #F44336; " +
                          "-fx-text-fill: white; " +
                          "-fx-cursor: hand; " +
                          "-fx-background-radius: 15; " +
                          "-fx-min-width: 30; " +
                          "-fx-min-height: 30;");
        
        actions.getChildren().addAll(downloadBtn, removeBtn);
        
        fileItem.getChildren().addAll(fileIcon, fileInfo, actions);
        HBox.setHgrow(fileInfo, Priority.ALWAYS);
        
        return fileItem;
    }
    
    private VBox createGroupTabContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        
        Label groupLabel = new Label("Anggota Kelompok 3:");
        groupLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        groupLabel.setTextFill(Color.web("#2D5016"));
        
        // Group members list
        VBox membersList = new VBox(15);
        
        HBox member1 = createMemberItem("John Doe", "Leader", "Sudah Mengumpulkan", true);
        HBox member2 = createMemberItem("Jane Smith", "Member", "Belum Mengumpulkan", false);
        HBox member3 = createMemberItem("Mike Johnson", "Member", "Sudah Mengumpulkan", false);
        HBox member4 = createMemberItem("Sarah Wilson", "Member", "Belum Mengumpulkan", false);
        
        membersList.getChildren().addAll(member1, member2, member3, member4);
        
        // Group progress
        VBox progressSection = new VBox(10);
        progressSection.setPadding(new Insets(20, 0, 0, 0));
        
        Label progressLabel = new Label("Progress Kelompok:");
        progressLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        progressLabel.setTextFill(Color.web("#2D5016"));
        
        ProgressBar progressBar = new ProgressBar(0.5);
        progressBar.setPrefWidth(400);
        progressBar.setPrefHeight(20);
        progressBar.setStyle("-fx-accent: #4CAF50;");
        
        Label progressText = new Label("2 dari 4 anggota sudah mengumpulkan");
        progressText.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        progressText.setTextFill(Color.web("#666666"));
        
        progressSection.getChildren().addAll(progressLabel, progressBar, progressText);
        
        content.getChildren().addAll(groupLabel, membersList, progressSection);
        
        return content;
    }
    
    private HBox createMemberItem(String name, String role, String submissionStatus, boolean isCurrentUser) {
        HBox memberItem = new HBox(20);
        memberItem.setAlignment(Pos.CENTER_LEFT);
        memberItem.setPadding(new Insets(15));
        memberItem.setStyle("-fx-background-color: " + (isCurrentUser ? "#E8F5E9" : "#F8F9FA") + "; " +
                           "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                           "-fx-border-radius: 10; " +
                           "-fx-background-radius: 10;");
        
        Label avatar = new Label("👤");
        avatar.setFont(Font.font(24));
        
        VBox memberInfo = new VBox(5);
        
        Label nameLabel = new Label(name + (isCurrentUser ? " (You)" : ""));
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        nameLabel.setTextFill(Color.web("#2D5016"));
        
        Label roleLabel = new Label(role);
        roleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        roleLabel.setTextFill(Color.web("#666666"));
        
        memberInfo.getChildren().addAll(nameLabel, roleLabel);
        
        // Status badge
        Label statusBadge = new Label(submissionStatus);
        statusBadge.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        statusBadge.setPadding(new Insets(5, 10, 5, 10));
        statusBadge.setStyle("-fx-background-radius: 15;");
        
        if (submissionStatus.equals("Sudah Mengumpulkan")) {
            statusBadge.setStyle("-fx-background-color: #E8F5E9; " +
                               "-fx-text-fill: #4CAF50; " +
                               "-fx-background-radius: 15;");
        } else {
            statusBadge.setStyle("-fx-background-color: #FFF3E0; " +
                               "-fx-text-fill: #FF9800; " +
                               "-fx-background-radius: 15;");
        }
        
        memberItem.getChildren().addAll(avatar, memberInfo, statusBadge);
        HBox.setHgrow(memberInfo, Priority.ALWAYS);
        
        return memberItem;
    }
    
    public void show() {
        stage.show();
    }
}