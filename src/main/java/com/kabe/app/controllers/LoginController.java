package com.kabe.app.controllers;
import com.kabe.app.dao.UserDAO;
import com.kabe.app.models.User;
import com.kabe.app.views.LoginView;

import javafx.scene.control.Alert;

public class LoginController {
    private UserDAO userDAO;
    private LoginView loginView;

    public LoginController(LoginView loginView) {
        this.userDAO = new UserDAO();
        this.loginView = loginView;
        setupEventHandlers();
    }
    
    private void setupEventHandlers() {
        loginView.getLoginButton().setOnAction(e -> handleLogin());
        loginView.getRegisterButton().setOnAction(e -> handleRegister());
    }

    private void handleLogin() {
        String username = loginView.getLoginUsername();
        String password = loginView.getLoginPassword();
        
        User user = userDAO.authenticate(username, password);
        
        if (user != null) {
            loginView.showAlert(Alert.AlertType.INFORMATION, "Login Berhasil", 
                "Selamat datang, " + user.getFullName());
            loginView.getOnLoginSuccess().run();
        } else {
            loginView.showAlert(Alert.AlertType.ERROR, "Login Gagal", 
                "Username atau password salah");
        }
    }

    private void handleRegister() {
        // Implementasi registrasi
        User newUser = new User();
        newUser.setUsername(loginView.getRegisterUsername());
        newUser.setPassword(loginView.getRegisterPassword());
        newUser.setFullName(loginView.getRegisterFullName());
        newUser.setEmail(loginView.getRegisterEmail());
        newUser.setRole(loginView.getRegisterRole());
        
        if (userDAO.register(newUser)) {
            loginView.showAlert(Alert.AlertType.INFORMATION, "Registrasi Berhasil", 
                "Akun berhasil dibuat, silakan login");
            loginView.switchToLogin();
        } else {
            loginView.showAlert(Alert.AlertType.ERROR, "Registrasi Gagal", 
                "Gagal membuat akun, coba lagi");
        }
    }
}