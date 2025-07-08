package com.kabe.app.views.teacher;

import com.kabe.app.controllers.KelasController;
import com.kabe.app.views.interfaces.ViewInterface;
import com.kabe.app.controllers.UserController;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class BuatKelasView implements ViewInterface {
    private Stage stage;
    private Scene scene;
    private BorderPane root;
    private TextField namaField;
    private TextField kodeField;
    private TextArea deskripsiArea;
    private NavigationHandler navigationHandler;
    private KelasController kelasController;
    private UserController userController;

    public void setNavigationHandler(NavigationHandler handler) {
        this.navigationHandler = handler;
    }

    public BuatKelasView(Stage stage, KelasController kelasController, UserController userController) {
        this.kelasController = kelasController;
        this.userController = userController;
        this.stage = stage;
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
        
        scene = new Scene(root, 800, 600);
        stage.setTitle("Akademiya - Buat Kelas Baru");
        stage.setScene(scene);
    }

    private void createMainContent() {
        VBox mainContent = new VBox(30);
        mainContent.setPadding(new Insets(30));
        mainContent.setAlignment(Pos.TOP_CENTER);
        
        // Header
        Label headerLabel = new Label("Buat Kelas Baru");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        headerLabel.setTextFill(Color.web("#4A148C"));
        
        // Form container
        VBox formContainer = new VBox(20);
        formContainer.setPadding(new Insets(30));
        formContainer.setStyle("-fx-background-color: white; " +
                             "-fx-background-radius: 20; " +
                             "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                             "-fx-border-width: 1; " +
                             "-fx-border-radius: 20;");
        formContainer.setMaxWidth(600);
        
        // Nama Kelas field
        VBox namaContainer = new VBox(5);
        Label namaLabel = new Label("Nama Kelas");
        namaLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        namaLabel.setTextFill(Color.web("#333333"));
        
        namaField = new TextField();
        namaField.setPromptText("Masukkan nama kelas");
        namaField.setStyle("-fx-background-radius: 8; " +
                         "-fx-border-radius: 8; " +
                         "-fx-padding: 10;");
        namaField.setPrefHeight(40);
        
        namaContainer.getChildren().addAll(namaLabel, namaField);
        
        // Kode Kelas field
        VBox kodeContainer = new VBox(5);
        Label kodeLabel = new Label("Kode Kelas");
        kodeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        kodeLabel.setTextFill(Color.web("#333333"));
        
        kodeField = new TextField();
        kodeField.setPromptText("Masukkan kode kelas (unik)");
        kodeField.setStyle("-fx-background-radius: 8; " +
                         "-fx-border-radius: 8; " +
                         "-fx-padding: 10;");
        kodeField.setPrefHeight(40);
        
        kodeContainer.getChildren().addAll(kodeLabel, kodeField);
        
        // Deskripsi field
        VBox deskripsiContainer = new VBox(5);
        Label deskripsiLabel = new Label("Deskripsi Kelas");
        deskripsiLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        deskripsiLabel.setTextFill(Color.web("#333333"));
        
        deskripsiArea = new TextArea();
        deskripsiArea.setPromptText("Masukkan deskripsi kelas");
        deskripsiArea.setStyle("-fx-background-radius: 8; " +
                             "-fx-border-radius: 8; " +
                             "-fx-padding: 10;");
        deskripsiArea.setPrefHeight(120);
        deskripsiArea.setWrapText(true);
        
        deskripsiContainer.getChildren().addAll(deskripsiLabel, deskripsiArea);
        
        // Button container
        HBox buttonContainer = new HBox(15);
        buttonContainer.setAlignment(Pos.CENTER_RIGHT);
        
        Button batalButton = new Button("Batal");
        batalButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        batalButton.setStyle("-fx-background-color: rgba(103, 58, 183, 0.1); " +
                           "-fx-background-radius: 8; " +
                           "-fx-text-fill: #673AB7; " +
                           "-fx-cursor: hand; " +
                           "-fx-padding: 10 20 10 20;");
        
        batalButton.setOnMouseEntered(e -> {
            batalButton.setStyle("-fx-background-color: rgba(103, 58, 183, 0.2); " +
                               "-fx-background-radius: 8; " +
                               "-fx-text-fill: #673AB7; " +
                               "-fx-cursor: hand; " +
                               "-fx-padding: 10 20 10 20;");
        });
        
        batalButton.setOnMouseExited(e -> {
            batalButton.setStyle("-fx-background-color: rgba(103, 58, 183, 0.1); " +
                               "-fx-background-radius: 8; " +
                               "-fx-text-fill: #673AB7; " +
                               "-fx-cursor: hand; " +
                               "-fx-padding: 10 20 10 20;");
        });
        
        batalButton.setOnAction(e -> {
            if (navigationHandler != null) {
                navigationHandler.handleNavigation("Kelas");
            }
        });
        
        Button buatButton = new Button("Buat Kelas");
        buatButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        buatButton.setStyle("-fx-background-color: #673AB7; " +
                          "-fx-background-radius: 8; " +
                          "-fx-text-fill: white; " +
                          "-fx-cursor: hand; " +
                          "-fx-padding: 10 20 10 20;");
        
        buatButton.setOnMouseEntered(e -> {
            buatButton.setStyle("-fx-background-color: #5E35B1; " +
                              "-fx-background-radius: 8; " +
                              "-fx-text-fill: white; " +
                              "-fx-cursor: hand; " +
                              "-fx-padding: 10 20 10 20;");
        });
        
        buatButton.setOnMouseExited(e -> {
            buatButton.setStyle("-fx-background-color: #673AB7; " +
                              "-fx-background-radius: 8; " +
                              "-fx-text-fill: white; " +
                              "-fx-cursor: hand; " +
                              "-fx-padding: 10 20 10 20;");
        });
        
        buatButton.setOnAction(e -> {
            if (validateForm()) {
                createNewClass();
            }
        });
        
        buttonContainer.getChildren().addAll(batalButton, buatButton);
        
        // Add all components to form
        formContainer.getChildren().addAll(
            namaContainer,
            kodeContainer,
            deskripsiContainer,
            buttonContainer
        );
        
        mainContent.getChildren().addAll(headerLabel, formContainer);
        root.setCenter(mainContent);
    }

    private boolean validateForm() {
        if (namaField.getText().isEmpty()) {
            showAlert("Nama kelas tidak boleh kosong");
            return false;
        }
        
        if (kodeField.getText().isEmpty()) {
            showAlert("Kode kelas tidak boleh kosong");
            return false;
        }
        
        if (deskripsiArea.getText().isEmpty()) {
            showAlert("Deskripsi kelas tidak boleh kosong");
            return false;
        }
        
        return true;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Peringatan");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void createNewClass() {
        // Here you would typically save the class to your database
        String nama = namaField.getText();
        String kode = kodeField.getText();
        String deskripsi = deskripsiArea.getText();
        int pengajarId = userController.getUser().getId();
        if (kelasController.isKodeExists(kode)) {
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Gagal");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Kode kelas (" + kode + ") sudah ada!");
            successAlert.showAndWait();
        } else {
            kelasController.createKelas(nama, kode, deskripsi, pengajarId);
            // Show success message
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Sukses");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Kelas " + nama + " berhasil dibuat!");
            successAlert.showAndWait();
        }
        
        
    }

    public KelasController getKelasController() {
        return kelasController;
    }

    public void show() {
        stage.show();
    }
}