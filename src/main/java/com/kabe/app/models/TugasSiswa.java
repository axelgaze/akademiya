package com.kabe.app.models;

import java.sql.Timestamp;

public class TugasSiswa {
    private int id;
    private int idTugas;
    private int idSiswa;
    private String fileName;
    private byte[] fileData;
    private Timestamp createdAt;
    private String nilai;
    private String feedback;
    private Timestamp feedbackAt;

    public Timestamp getFeedbackAt() {
        return feedbackAt;
    }

    public void setFeedbackAt(Timestamp feedback_at) {
        this.feedbackAt = feedbackAt;
    }

    private String status; 
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    

    // Constructors
    public TugasSiswa() {}

    public TugasSiswa(int idTugas, int idSiswa, String fileName, byte[] fileData) {
        this.idTugas = idTugas;
        this.idSiswa = idSiswa;
        this.fileName = fileName;
        this.fileData = fileData;
    }

    public int getId() {
        return id;
    }

    public String getNilai() {
        return nilai;
    }

    public void setNilai(String nilai) {
        this.nilai = nilai;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdTugas() {
        return idTugas;
    }

    public void setIdTugas(int idTugas) {
        this.idTugas = idTugas;
    }

    public int getIdSiswa() {
        return idSiswa;
    }

    public void setIdSiswa(int idSiswa) {
        this.idSiswa = idSiswa;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
}