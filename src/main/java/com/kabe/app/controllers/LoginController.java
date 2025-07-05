package com.kabe.app.controllers;
import com.kabe.app.dao.UserDAO;
import com.kabe.app.models.User;
import com.kabe.app.views.LoginView;

public class LoginController {
    private UserDAO userDAO = new UserDAO();
    private LoginView loginView;
}