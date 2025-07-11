package com.kabe.app.models;

public class User {
    private int id;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String role;
    
    // Constructor, getters, and setters
    public User() {}
    
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.fullName = "";
        this.email = "";
        this.role = "";
    }
    
    public User(String username, String password, String fullName, String email, String role) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }
    
    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}