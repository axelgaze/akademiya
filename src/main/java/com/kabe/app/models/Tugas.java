package com.kabe.app.models;

import java.sql.Timestamp;

public class Tugas {
    private int id;
    private String title;
    private int kelasId;
    private String fileName;
    private String fileType;
    private byte[] fileData;
    private int uploaderId;
    private String tipe;
    private Timestamp createdAt;
    private Timestamp deadline;
    private String deskripsi;

    public Tugas(){}
    
    public String getTitle() {
        return title;
    }
    public String getTipe() {
        return tipe;
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
    public Timestamp getDeadline() {
        return deadline;
    }
    public String getDeskripsi() {
        return deskripsi;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setTipe(String tipe) {
        this.tipe = tipe;
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
    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }
    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

}