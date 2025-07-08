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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.kabe.app.views.interfaces.ViewInterface;

import java.io.IOException;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.kabe.app.controllers.KelasController;
import com.kabe.app.controllers.UserController;
import com.kabe.app.models.Kelas;
import com.kabe.app.controllers.TugasController;

public class AddTaskView implements ViewInterface {
    private Stage stage;
    private Scene scene;
    private VBox root;
    private NavigationHandler navigationHandler;
    private UserController userController;
    private KelasController kelasController;
    private FileChooser fileChooser = new FileChooser();
    private File selectedFile;
    private String fileName;
    private String fileType;
    private byte[] fileData;
    private TugasController tugasController;

    private void initializeFileChooser() {
        fileChooser.setTitle("Pilih File Tugas");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("All Files", "*.*"),
            new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
            new FileChooser.ExtensionFilter("Word Documents", "*.docx", "*.doc"),
            new FileChooser.ExtensionFilter("PowerPoint", "*.pptx", "*.ppt"),
            new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );
    }
    
    public void setNavigationHandler(NavigationHandler handler) {
        this.navigationHandler = handler;
    }
    
    public AddTaskView(Stage stage, UserController userController, KelasController kelasController, TugasController tugasController) {
        this.userController = userController;
        this.kelasController = kelasController;
        this.tugasController = tugasController;
        this.stage = stage;
        initializeFileChooser();
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
        createTaskForm();
        
        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        
        scene = new Scene(scrollPane, 1200, 800);
        stage.setTitle("Akademiya - Buat Tugas Baru");
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
        
        Label headerTitle = new Label("Buat Tugas Baru");
        headerTitle.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        headerTitle.setTextFill(Color.web("#4A148C")); // Dark purple
        
        Label headerSubtitle = new Label("Buat dan publikasikan tugas baru untuk siswa");
        headerSubtitle.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        headerSubtitle.setTextFill(Color.web("#7B1FA2")); // Medium purple
        
        headerText.getChildren().addAll(headerTitle, headerSubtitle);
        
        header.getChildren().addAll(backBtn, headerText);
        root.getChildren().add(header);
    }
    
    private void createTaskForm() {
        VBox formContainer = new VBox(20);
        formContainer.setPadding(new Insets(30));
        formContainer.setStyle("-fx-background-color: white; " +
                             "-fx-background-radius: 15; " +
                             "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                             "-fx-border-width: 1; " +
                             "-fx-border-radius: 15;");
        formContainer.setEffect(new DropShadow(10, Color.web("#E0E0E0")));
        
        // Form grid
        GridPane formGrid = new GridPane();
        formGrid.setHgap(20);
        formGrid.setVgap(15);
        formGrid.setPadding(new Insets(10, 0, 20, 0));
        
        // Task title
        Label titleLabel = new Label("Judul Tugas:");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        titleLabel.setTextFill(Color.web("#666666"));
        
        TextField titleField = new TextField();
        titleField.setPromptText("Masukkan judul tugas");
        titleField.setPrefHeight(40);
        titleField.setStyle("-fx-background-color: white; " +
                           "-fx-background-radius: 10; " +
                           "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                           "-fx-border-width: 1; " +
                           "-fx-border-radius: 10; " +
                           "-fx-padding: 10;");
        
        // Description
        Label descLabel = new Label("Deskripsi Tugas:");
        descLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        descLabel.setTextFill(Color.web("#666666"));
        
        TextArea descArea = new TextArea();
        descArea.setPromptText("Masukkan deskripsi tugas");
        descArea.setPrefHeight(120);
        descArea.setWrapText(true);
        descArea.setStyle("-fx-background-color: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 10; " +
                        "-fx-padding: 10;");
        
        // Deadline
        Label deadlineLabel = new Label("Deadline:");
        deadlineLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        deadlineLabel.setTextFill(Color.web("#666666"));
        
        DatePicker deadlinePicker = new DatePicker();
        deadlinePicker.setPrefHeight(40);
        deadlinePicker.setStyle("-fx-background-color: white; " +
                               "-fx-background-radius: 10; " +
                               "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                               "-fx-border-width: 1; " +
                               "-fx-border-radius: 10;");
        
        // Class selection with search
        Label classLabel = new Label("Kelas:");
        classLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        classLabel.setTextFill(Color.web("#666666"));
        
        HBox classSelectionBox = new HBox(10);
        
        ComboBox<Kelas> classCombo = new ComboBox<>();
        for (Kelas kelas : kelasController.getClassesByTeacher(userController.getUser().getId())) {
            classCombo.getItems().add(kelas);
        }

        classCombo.setPromptText("Pilih kelas");
        classCombo.setPrefWidth(300);
        classCombo.setPrefHeight(40);
        classCombo.setStyle("-fx-background-color: white; " +
                          "-fx-background-radius: 10; " +
                          "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                          "-fx-border-width: 1; " +
                          "-fx-border-radius: 10;");
        
        TextField classSearchField = new TextField();
        classSearchField.setPromptText("Cari kelas...");
        classSearchField.setPrefHeight(40);
        classSearchField.setStyle("-fx-background-color: white; " +
                               "-fx-background-radius: 10; " +
                               "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                               "-fx-border-width: 1; " +
                               "-fx-border-radius: 10; " +
                               "-fx-padding: 10;");
        
        
        classSelectionBox.getChildren().addAll(classCombo);
        
        // Task type
        Label typeLabel = new Label("Tipe Tugas:");
        typeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        typeLabel.setTextFill(Color.web("#666666"));
        
        ToggleGroup typeGroup = new ToggleGroup();
        RadioButton individualBtn = new RadioButton("Individu");
        individualBtn.setToggleGroup(typeGroup);
        individualBtn.setSelected(true);
        individualBtn.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        individualBtn.setTextFill(Color.web("#000000"));
        
        RadioButton groupBtn = new RadioButton("Kelompok");
        groupBtn.setToggleGroup(typeGroup);
        groupBtn.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        groupBtn.setTextFill(Color.web("#000000"));
        
        HBox typeBox = new HBox(20);
        
        // File upload
        Label fileLabel = new Label("Material Tugas:");
        fileLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        fileLabel.setTextFill(Color.web("#666666"));
        
        Button uploadBtn = new Button("Pilih File");
        uploadBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        uploadBtn.setPrefHeight(40);
        uploadBtn.setStyle("-fx-background-color: #E3F2FD; " +
                         "-fx-text-fill: #1976D2; " +
                         "-fx-background-radius: 10; " +
                         "-fx-cursor: hand; " +
                         "-fx-padding: 0 20;");
        
        Label fileNameLabel = new Label("Belum ada file dipilih");
        fileNameLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        fileNameLabel.setTextFill(Color.web("#888888"));
        
        HBox fileBox = new HBox(15);
        fileBox.setAlignment(Pos.CENTER_LEFT);
        fileBox.getChildren().addAll(uploadBtn, fileNameLabel);
        
        // Add elements to grid
        formGrid.add(titleLabel, 0, 0);
        formGrid.add(titleField, 1, 0);
        formGrid.add(descLabel, 0, 1);
        formGrid.add(descArea, 1, 1);
        formGrid.add(deadlineLabel, 0, 2);
        formGrid.add(deadlinePicker, 1, 2);
        formGrid.add(classLabel, 0, 3);
        formGrid.add(classSelectionBox, 1, 3);
        formGrid.add(fileLabel, 0, 5);
        formGrid.add(fileBox, 1, 5);


        // Event handler upload button
        uploadBtn.setOnAction(e -> {
            selectedFile = fileChooser.showOpenDialog(stage);
            
            if (selectedFile != null) {
                // Dapatkan informasi file
                fileName = selectedFile.getName();
                fileType = getFileExtension(selectedFile);
                
                // Tampilkan nama file
                fileNameLabel.setText(fileName);
                
                // Untuk mendapatkan file_data (BLOB)
                try {
                    fileData = Files.readAllBytes(selectedFile.toPath());
                    
                    
                } catch (IOException ex) {
                    fileNameLabel.setText("Gagal membaca file!");
                    ex.printStackTrace();
                }
            }
        });
        
        // Submit button
        Button submitBtn = new Button("Publikasikan Tugas");
        submitBtn.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        submitBtn.setStyle("-fx-background-color: #9C27B0; " +
                          "-fx-text-fill: white; " +
                          "-fx-background-radius: 10; " +
                          "-fx-cursor: hand; " +
                          "-fx-padding: 15 30;");
        submitBtn.setOnAction(e -> {
            // Handle task submission here
            if (navigationHandler != null) {
                int kelas_id = classCombo.getValue().getId();
                String title = titleField.getText();
                String file_name = fileName;
                String file_type = fileType;
                byte[] file_data = fileData;
                int uploaderId = userController.getUser().getId();

                LocalDate selectedDate = deadlinePicker.getValue();
                LocalDateTime dateTime = selectedDate.atStartOfDay();
                Timestamp deadline = Timestamp.valueOf(dateTime);
                String deskripsi = descArea.getText();
                Toggle selectedToggle = typeGroup.getSelectedToggle();
                String selectedType = ((RadioButton) selectedToggle).getText();
                String tipe = "individu";

                tugasController.addTugas(kelas_id, title, file_name, file_type, file_data, uploaderId, deadline, deskripsi, tipe);
                navigationHandler.handleNavigation("Tugas");
            }
        });
        
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.getChildren().add(submitBtn);
        
        formContainer.getChildren().addAll(formGrid, buttonBox);
        root.getChildren().add(formContainer);
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastDot = name.lastIndexOf('.');
        if (lastDot == -1) return "";
        return name.substring(lastDot + 1).toLowerCase();
    }
    
    public void show() {
        stage.show();
    }
}