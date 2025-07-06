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

import com.kabe.app.views.interfaces.ViewInterface;

public class TeacherTaskDetailView implements ViewInterface {
    private Stage stage;
    private Scene scene;
    private VBox root;
    private NavigationHandler navigationHandler;
    
    // Task data
    private String title;
    private String className;
    private String deadline;
    private int submittedCount;
    private int totalStudents;

    public void setNavigationHandler(NavigationHandler handler) {
        this.navigationHandler = handler;
    }
    
    public TeacherTaskDetailView(Stage stage, String title, String className, 
                               String deadline, int submittedCount, int totalStudents) {
        this.stage = stage;
        this.title = title;
        this.className = className;
        this.deadline = deadline;
        this.submittedCount = submittedCount;
        this.totalStudents = totalStudents;
        initializeView();
    }
    
    private void initializeView() {
        root = new VBox(20);
        root.setPadding(new Insets(30));
        
        // Background with purple gradient
        LinearGradient backgroundGradient = new LinearGradient(
            0, 0, 1, 1, true, null,
            new Stop(0, Color.web("#F3E5F5")), // Light purple
            new Stop(0.5, Color.web("#E1BEE7")), // Medium purple
            new Stop(1, Color.web("#CE93D8"))  // Darker purple
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
        stage.setTitle("Akademiya - Detail Tugas (Guru)");
        stage.setScene(scene);
    }
    
    private void createHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));
        
        // Back button
        Button backBtn = new Button("← Kembali");
        backBtn.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        backBtn.setStyle("-fx-background-color: #9C27B0; " +  // Purple
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
        headerTitle.setTextFill(Color.web("#4A148C")); // Dark purple
        
        Label headerSubtitle = new Label("Kelola pengumpulan dan penilaian tugas");
        headerSubtitle.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        headerSubtitle.setTextFill(Color.web("#7B1FA2")); // Medium purple
        
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
        
        // Task title
        HBox titleContainer = new HBox(15);
        titleContainer.setAlignment(Pos.CENTER_LEFT);
        
        Label taskIcon = new Label("📝");
        taskIcon.setFont(Font.font(32));
        
        Label taskTitle = new Label(title);
        taskTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        taskTitle.setTextFill(Color.web("#4A148C")); // Dark purple
        
        titleContainer.getChildren().addAll(taskIcon, taskTitle);
        
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
        
        // Deadline info
        Label deadlineLabel = new Label("Deadline:");
        deadlineLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        deadlineLabel.setTextFill(Color.web("#666666"));
        
        Label deadlineValue = new Label(deadline);
        deadlineValue.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        deadlineValue.setTextFill(Color.web("#333333"));
        
        // Submission status
        Label statusLabel = new Label("Pengumpulan:");
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        statusLabel.setTextFill(Color.web("#666666"));
        
        Label statusValue = new Label(submittedCount + " dari " + totalStudents + " siswa");
        statusValue.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        statusValue.setTextFill(Color.web("#9C27B0")); // Purple
        
        infoGrid.add(classLabel, 0, 0);
        infoGrid.add(classValue, 1, 0);
        infoGrid.add(deadlineLabel, 0, 1);
        infoGrid.add(deadlineValue, 1, 1);
        infoGrid.add(statusLabel, 0, 2);
        infoGrid.add(statusValue, 1, 2);
        
        // Description
        VBox descriptionContainer = new VBox(10);
        descriptionContainer.setPadding(new Insets(20, 0, 0, 0));
        
        Label descriptionLabel = new Label("Deskripsi Tugas:");
        descriptionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        descriptionLabel.setTextFill(Color.web("#4A148C")); // Dark purple
        
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
        
        // Submitted tab
        Tab submittedTab = new Tab("Sudah Mengumpulkan (" + submittedCount + ")");
        VBox submittedContent = createSubmittedTabContent();
        submittedTab.setContent(submittedContent);
        
        // Not submitted tab
        Tab notSubmittedTab = new Tab("Belum Mengumpulkan (" + (totalStudents - submittedCount) + ")");
        VBox notSubmittedContent = createNotSubmittedTabContent();
        notSubmittedTab.setContent(notSubmittedContent);
        
        tabPane.getTabs().addAll(submittedTab, notSubmittedTab);
        root.getChildren().add(tabPane);
    }
    
    private VBox createSubmittedTabContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        
        // Search and filter
        HBox searchBox = new HBox(15);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        
        TextField searchField = new TextField();
        searchField.setPromptText("Cari siswa...");
        searchField.setPrefWidth(300);
        searchField.setPrefHeight(40);
        searchField.setStyle("-fx-background-color: white; " +
                           "-fx-background-radius: 10; " +
                           "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                           "-fx-border-width: 1; " +
                           "-fx-border-radius: 10; " +
                           "-fx-padding: 10;");
        
        ComboBox<String> filterCombo = new ComboBox<>();
        filterCombo.getItems().addAll("Semua", "Sudah Dinilai", "Belum Dinilai");
        filterCombo.setValue("Semua");
        filterCombo.setPrefWidth(150);
        filterCombo.setStyle("-fx-background-color: white; " +
                            "-fx-background-radius: 10; " +
                            "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                            "-fx-border-width: 1; " +
                            "-fx-border-radius: 10;");
        
        searchBox.getChildren().addAll(searchField, filterCombo);
        
        // Submissions list
        VBox submissionsList = new VBox(15);
        
        // Sample submissions (in a real app, this would come from data)
        HBox submission1 = createSubmissionItem("John Doe", "Tugas_Matematika_John.pdf", "2024-01-14 10:30", true, "A", "Bagus sekali!");
        HBox submission2 = createSubmissionItem("Jane Smith", "Tugas_Matematika_Jane.docx", "2024-01-14 11:45", false, "", "");
        HBox submission3 = createSubmissionItem("Mike Johnson", "Tugas_Matematika_Mike.pdf", "2024-01-15 09:15", true, "B", "Kurang lengkap");
        
        submissionsList.getChildren().addAll(submission1, submission2, submission3);
        
        content.getChildren().addAll(searchBox, submissionsList);
        return content;
    }
    
    private HBox createSubmissionItem(String studentName, String fileName, String submitTime, 
                                     boolean isGraded, String grade, String feedback) {
        HBox item = new HBox(20);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(15));
        item.setStyle("-fx-background-color: " + (isGraded ? "#F3E5F5" : "#F8F9FA") + "; " +
                     "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                     "-fx-border-radius: 10; " +
                     "-fx-background-radius: 10;");
        
        // Student info
        VBox studentInfo = new VBox(5);
        
        Label nameLabel = new Label(studentName);
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        nameLabel.setTextFill(Color.web("#4A148C")); // Dark purple
        
        Label fileLabel = new Label(fileName + " • " + submitTime);
        fileLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        fileLabel.setTextFill(Color.web("#666666"));
        
        studentInfo.getChildren().addAll(nameLabel, fileLabel);
        
        // Grade section
        VBox gradeSection = new VBox(5);
        gradeSection.setAlignment(Pos.CENTER_RIGHT);
        
        if (isGraded) {
            Label gradeLabel = new Label("Nilai: " + grade);
            gradeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            gradeLabel.setTextFill(Color.web("#4A148C")); // Dark purple
            
            Label feedbackLabel = new Label("Feedback: " + feedback);
            feedbackLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
            feedbackLabel.setTextFill(Color.web("#666666"));
            
            gradeSection.getChildren().addAll(gradeLabel, feedbackLabel);
        } else {
            Label notGradedLabel = new Label("Belum dinilai");
            notGradedLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
            notGradedLabel.setTextFill(Color.web("#FF9800")); // Orange
            
            gradeSection.getChildren().add(notGradedLabel);
        }
        
        // Action buttons
        HBox actionButtons = new HBox(10);
        actionButtons.setAlignment(Pos.CENTER_RIGHT);
        
        Button downloadBtn = new Button("⬇");
        downloadBtn.setFont(Font.font(14));
        downloadBtn.setStyle("-fx-background-color: #2196F3; " +
                            "-fx-text-fill: white; " +
                            "-fx-cursor: hand; " +
                            "-fx-background-radius: 15; " +
                            "-fx-min-width: 30; " +
                            "-fx-min-height: 30;");
        
        Button gradeBtn = new Button(isGraded ? "Ubah Nilai" : "Beri Nilai");
        gradeBtn.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        gradeBtn.setStyle("-fx-background-color: #9C27B0; " + // Purple
                         "-fx-text-fill: white; " +
                         "-fx-cursor: hand; " +
                         "-fx-background-radius: 15; " +
                         "-fx-padding: 5 15;");
        
        actionButtons.getChildren().addAll(downloadBtn, gradeBtn);
        
        item.getChildren().addAll(studentInfo, gradeSection, actionButtons);
        HBox.setHgrow(studentInfo, Priority.ALWAYS);
        
        return item;
    }
    
    private VBox createNotSubmittedTabContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        
        // List of students who haven't submitted
        VBox studentsList = new VBox(10);
        
        // Sample students (in a real app, this would come from data)
        HBox student1 = createStudentItem("Sarah Wilson", "sarah@example.com");
        HBox student2 = createStudentItem("Robert Brown", "robert@example.com");
        HBox student3 = createStudentItem("Emily Davis", "emily@example.com");
        
        studentsList.getChildren().addAll(student1, student2, student3);
        
        // Reminder button
        Button reminderBtn = new Button("Kirim Pengingat");
        reminderBtn.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        reminderBtn.setStyle("-fx-background-color: #9C27B0; " + // Purple
                           "-fx-text-fill: white; " +
                           "-fx-background-radius: 10; " +
                           "-fx-cursor: hand; " +
                           "-fx-padding: 15 30;");
        
        content.getChildren().addAll(studentsList, reminderBtn);
        return content;
    }
    
    private HBox createStudentItem(String name, String email) {
        HBox item = new HBox(20);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(15));
        item.setStyle("-fx-background-color: #F8F9FA; " +
                     "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                     "-fx-border-radius: 10; " +
                     "-fx-background-radius: 10;");
        
        Label avatar = new Label("👤");
        avatar.setFont(Font.font(24));
        
        VBox studentInfo = new VBox(5);
        
        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        nameLabel.setTextFill(Color.web("#4A148C")); // Dark purple
        
        Label emailLabel = new Label(email);
        emailLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        emailLabel.setTextFill(Color.web("#666666"));
        
        studentInfo.getChildren().addAll(nameLabel, emailLabel);
        
        Label statusLabel = new Label("Belum mengumpulkan");
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        statusLabel.setTextFill(Color.web("#F44336")); // Red
        
        item.getChildren().addAll(avatar, studentInfo, statusLabel);
        HBox.setHgrow(studentInfo, Priority.ALWAYS);
        
        return item;
    }
    
    public void show() {
        stage.show();
    }
}