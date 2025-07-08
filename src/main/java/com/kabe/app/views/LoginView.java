package com.kabe.app.views;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import com.kabe.app.controllers.UserController;

import com.kabe.app.dao.UserDAO;
import com.kabe.app.models.User;

public class LoginView {
    private Stage stage;
    private Scene scene;
    private StackPane root;
    private VBox loginForm;
    private VBox registerForm;
    private boolean isLoginMode = true;
    private Runnable onLoginSuccess;
    private UserDAO userDAO = new UserDAO();
    private Button loginButton;
    private Button registerButton;
    private User user;

    private TextField loginUsernameField;
    private PasswordField loginPasswordField;

    private TextField regFullNameField;
    private TextField regUsernameField;
    private TextField regEmailField;
    private PasswordField regPasswordField;
    private PasswordField regConfirmPasswordField;
    private RadioButton studentRadio;


    public void setOnLoginSuccess(Runnable onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
    }

    public Runnable getOnLoginSuccess() {
        return onLoginSuccess;
    }
    
    public LoginView(Stage stage) {
        this.stage = stage;
        initializeView();
    }

    public LoginView() {}
    
    private void initializeView() {
        root = new StackPane();
        
        // Background with Sumeru-inspired gradient
        LinearGradient backgroundGradient = new LinearGradient(
            0, 0, 1, 1, true, null,
            new Stop(0, Color.web("#2D5016")), // Dark forest green
            new Stop(0.3, Color.web("#3D6B1F")), // Medium forest green
            new Stop(0.7, Color.web("#4A7C26")), // Lighter forest green
            new Stop(1, Color.web("#5B8F31"))  // Sumeru green
        );
        
        BackgroundFill backgroundFill = new BackgroundFill(backgroundGradient, null, null);
        root.setBackground(new Background(backgroundFill));
        
        // Create main container
        VBox mainContainer = new VBox(30);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setMaxWidth(400);
        mainContainer.setPadding(new Insets(50));
        
        // Title
        Label titleLabel = new Label("AKADEMIYA");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        titleLabel.setTextFill(Color.web("#E8F5E8"));
        titleLabel.setEffect(new DropShadow(10, Color.web("#1A3009")));
        
        Label subtitleLabel = new Label("Platform Pengumpul Tugas Digital");
        subtitleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        subtitleLabel.setTextFill(Color.web("#C8E6C9"));
        
        VBox titleContainer = new VBox(5);
        titleContainer.setAlignment(Pos.CENTER);
        titleContainer.getChildren().addAll(titleLabel, subtitleLabel);
        
        // Create forms
        createLoginForm();
        createRegisterForm();
        
        // Form container
        StackPane formContainer     = new StackPane();
        formContainer.getChildren().addAll(registerForm, loginForm);
        
        // Toggle buttons
        HBox toggleContainer = createToggleButtons();
        
        mainContainer.getChildren().addAll(titleContainer, formContainer, toggleContainer);
        root.getChildren().add(mainContainer);
        
        scene = new Scene(root, 1200, 800);
        stage.setTitle("Akademiya - Login");
        stage.setScene(scene);
    }

    public Button getLoginButton() {
        return this.loginButton;
    }

    public Button getRegisterButton() {
        return this.registerButton;
    }

    public User getUser() {
        return this.user;
    }

    public void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void createLoginForm() {
        loginForm = new VBox(20);
        loginForm.setAlignment(Pos.CENTER);
        loginForm.setMaxWidth(350);
        loginForm.setPadding(new Insets(30));
        
        // Form background
        loginForm.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); " +
                          "-fx-background-radius: 15; " +
                          "-fx-border-color: rgba(255, 255, 255, 0.2); " +
                          "-fx-border-width: 1; " +
                          "-fx-border-radius: 15;");
        loginForm.setEffect(new DropShadow(20, Color.web("#1A3009")));
        
        // Form title
        Label formTitle = new Label("Masuk");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        formTitle.setTextFill(Color.web("#E8F5E8"));
        
        // Username field
        loginUsernameField = createStyledTextField("Username");
        
        // Password field
        loginPasswordField = createStyledPasswordField("Password");
        
        // Login button
        loginButton = createStyledButton("MASUK", Color.web("#4CAF50"));
                
        // Forgot password link
        Label forgotPasswordLabel = new Label("Lupa password?");
        forgotPasswordLabel.setTextFill(Color.web("#81C784"));
        forgotPasswordLabel.setFont(Font.font("Arial", 12));
        forgotPasswordLabel.setStyle("-fx-cursor: hand;");
        
        loginForm.getChildren().addAll(formTitle, loginUsernameField, loginPasswordField, 
                                      loginButton);
    }
    
    private void createRegisterForm() {
        registerForm = new VBox(20);
        registerForm.setAlignment(Pos.CENTER);
        registerForm.setMaxWidth(350);
        registerForm.setPadding(new Insets(30));
        registerForm.setVisible(false);
        
        // Form background
        registerForm.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); " +
                             "-fx-background-radius: 15; " +
                             "-fx-border-color: rgba(255, 255, 255, 0.2); " +
                             "-fx-border-width: 1; " +
                             "-fx-border-radius: 15;");
        registerForm.setEffect(new DropShadow(20, Color.web("#1A3009")));
        
        // Form title
        Label formTitle = new Label("Daftar");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        formTitle.setTextFill(Color.web("#E8F5E8"));
        
        // Full name field
        regFullNameField = createStyledTextField("Nama Lengkap");
        
        // Username field
        regUsernameField = createStyledTextField("Username");
        
        // Email field
        regEmailField = createStyledTextField("Email");
        
        // Password field
        regPasswordField = createStyledPasswordField("Password");
        
        // Confirm password field
        regConfirmPasswordField = createStyledPasswordField("Konfirmasi Password");
        
        // Role selection
        Label roleLabel = new Label("Peran:");
        roleLabel.setTextFill(Color.web("#E8F5E8"));
        roleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        ToggleGroup roleGroup = new ToggleGroup();
        studentRadio = new RadioButton("Pelajar");
        RadioButton teacherRadio = new RadioButton("Pengajar");
        
        studentRadio.setToggleGroup(roleGroup);
        teacherRadio.setToggleGroup(roleGroup);
        studentRadio.setSelected(true);
        
        studentRadio.setTextFill(Color.web("#E8F5E8"));
        teacherRadio.setTextFill(Color.web("#E8F5E8"));
        
        HBox roleContainer = new HBox(20);
        roleContainer.setAlignment(Pos.CENTER);
        roleContainer.getChildren().addAll(studentRadio, teacherRadio);
        
        // Register button
        registerButton = createStyledButton("DAFTAR", Color.web("#66BB6A"));

        registerForm.getChildren().addAll(formTitle, regFullNameField, regUsernameField, 
                                         regEmailField, regPasswordField, regConfirmPasswordField,
                                         roleLabel, roleContainer, registerButton);

        
    }
    
    private TextField createStyledTextField(String placeholder) {
        TextField textField = new TextField();
        textField.setPromptText(placeholder);
        textField.setFont(Font.font("Arial", 14));
        textField.setPrefHeight(40);
        textField.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); " +
                          "-fx-background-radius: 8; " +
                          "-fx-border-color: rgba(76, 175, 80, 0.3); " +
                          "-fx-border-width: 1; " +
                          "-fx-border-radius: 8; " +
                          "-fx-padding: 10;");
        
        // Focus effect
        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                textField.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); " +
                                  "-fx-background-radius: 8; " +
                                  "-fx-border-color: #4CAF50; " +
                                  "-fx-border-width: 2; " +
                                  "-fx-border-radius: 8; " +
                                  "-fx-padding: 10;");
            } else {
                textField.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); " +
                                  "-fx-background-radius: 8; " +
                                  "-fx-border-color: rgba(76, 175, 80, 0.3); " +
                                  "-fx-border-width: 1; " +
                                  "-fx-border-radius: 8; " +
                                  "-fx-padding: 10;");
            }
        });
        
        return textField;
    }

    // Untuk login
    public String getLoginUsername() {
        return loginUsernameField.getText();
    }

    public String getLoginPassword() {
        return loginPasswordField.getText();
    }

    // Untuk registrasi
    public String getRegisterFullName() {
        return regFullNameField.getText();
    }

    public String getRegisterUsername() {
        return regUsernameField.getText();
    }

    public String getRegisterEmail() {
        return regEmailField.getText();
    }

    public String getRegisterPassword() {
        return regPasswordField.getText();
    }

    public String getRegisterConfirmPassword() {
        return regConfirmPasswordField.getText();
    }

    public String getRegisterRole() {
        return studentRadio.isSelected() ? "siswa" : "pengajar";
    }
    
    private PasswordField createStyledPasswordField(String placeholder) {
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText(placeholder);
        passwordField.setFont(Font.font("Arial", 14));
        passwordField.setPrefHeight(40);
        passwordField.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); " +
                              "-fx-background-radius: 8; " +
                              "-fx-border-color: rgba(76, 175, 80, 0.3); " +
                              "-fx-border-width: 1; " +
                              "-fx-border-radius: 8; " +
                              "-fx-padding: 10;");
        
        // Focus effect
        passwordField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                passwordField.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); " +
                                      "-fx-background-radius: 8; " +
                                      "-fx-border-color: #4CAF50; " +
                                      "-fx-border-width: 2; " +
                                      "-fx-border-radius: 8; " +
                                      "-fx-padding: 10;");
            } else {
                passwordField.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); " +
                                      "-fx-background-radius: 8; " +
                                      "-fx-border-color: rgba(76, 175, 80, 0.3); " +
                                      "-fx-border-width: 1; " +
                                      "-fx-border-radius: 8; " +
                                      "-fx-padding: 10;");
            }
        });
        
        return passwordField;
    }
    
    private Button createStyledButton(String text, Color color) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        button.setPrefWidth(200);
        button.setPrefHeight(45);
        button.setTextFill(Color.WHITE);
        
        String baseStyle = String.format("-fx-background-color: %s; " +
                                        "-fx-background-radius: 8; " +
                                        "-fx-border-radius: 8; " +
                                        "-fx-cursor: hand;", 
                                        toHexString(color));
        button.setStyle(baseStyle);
        
        // Hover effect
        button.setOnMouseEntered(e -> {
            button.setStyle(String.format("-fx-background-color: %s; " +
                                         "-fx-background-radius: 8; " +
                                         "-fx-border-radius: 8; " +
                                         "-fx-cursor: hand;", 
                                         toHexString(color.brighter())));
            
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), button);
            scaleTransition.setToX(1.05);
            scaleTransition.setToY(1.05);
            scaleTransition.play();
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(baseStyle);
            
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), button);
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
            scaleTransition.play();
        });
        
        return button;
    }
    
    private HBox createToggleButtons() {
        HBox toggleContainer = new HBox(10);
        toggleContainer.setAlignment(Pos.CENTER);
        
        Button loginToggle = new Button("Masuk");
        Button registerToggle = new Button("Daftar");
        
        loginToggle.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        registerToggle.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        
        loginToggle.setPrefWidth(80);
        registerToggle.setPrefWidth(80);
        
        loginToggle.setTextFill(Color.web("#E8F5E8"));
        registerToggle.setTextFill(Color.web("#A5D6A7"));
        
        loginToggle.setStyle("-fx-background-color: transparent; " +
                            "-fx-border-color: #E8F5E8; " +
                            "-fx-border-width: 0 0 2 0; " +
                            "-fx-cursor: hand;");
        
        registerToggle.setStyle("-fx-background-color: transparent; " +
                               "-fx-border-color: transparent; " +
                               "-fx-cursor: hand;");
        
        // Toggle functionality
        loginToggle.setOnAction(e -> {
            if (!isLoginMode) {
                switchToLogin();
                updateToggleButtons(loginToggle, registerToggle);
            }
        });
        
        registerToggle.setOnAction(e -> {
            if (isLoginMode) {
                switchToRegister();
                updateToggleButtons(registerToggle, loginToggle);
            }
        });
        
        toggleContainer.getChildren().addAll(loginToggle, registerToggle);
        return toggleContainer;
    }
    
    public void switchToLogin() {
        isLoginMode = true;
        
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), registerForm);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            registerForm.setVisible(false);
            loginForm.setVisible(true);
            
            FadeTransition fadeIn = new FadeTransition(Duration.millis(200), loginForm);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fadeOut.play();
    }
    
    private void switchToRegister() {
        isLoginMode = false;
        
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), loginForm);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            loginForm.setVisible(false);
            registerForm.setVisible(true);
            
            FadeTransition fadeIn = new FadeTransition(Duration.millis(200), registerForm);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fadeOut.play();
    }
    
    private void updateToggleButtons(Button activeButton, Button inactiveButton) {
        activeButton.setTextFill(Color.web("#E8F5E8"));
        activeButton.setStyle("-fx-background-color: transparent; " +
                             "-fx-border-color: #E8F5E8; " +
                             "-fx-border-width: 0 0 2 0; " +
                             "-fx-cursor: hand;");
        
        inactiveButton.setTextFill(Color.web("#A5D6A7"));
        inactiveButton.setStyle("-fx-background-color: transparent; " +
                               "-fx-border-color: transparent; " +
                               "-fx-cursor: hand;");
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