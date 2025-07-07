package com.kabe.app.models;

import java.sql.Timestamp;

public class Material {
    private int id;
    private int kelasId;
    private String fileName;
    private String fileType;
    private byte[] fileData;
    private int uploaderId;
    private Timestamp createdAt;
    
    // Constructor, getters, dan setters
    public Material() {}
    
    public Material(int id, int kelasId, String fileName, String fileType, 
                   byte[] fileData, int uploaderId, Timestamp createdAt) {
        this.id = id;
        this.kelasId = kelasId;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileData = fileData;
        this.uploaderId = uploaderId;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public int getKelasId() {
        return kelasId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public int getUploaderId() {
        return uploaderId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setKelasId(int kelasId) {
        this.kelasId = kelasId;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public void setUploaderId(int uploaderId) {
        this.uploaderId = uploaderId;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    
}