package com.kabe.app.models;

import java.time.LocalDateTime;

public class PemberitahuanKelas {
    private int id;
    private int idKelas;
    private String isi;
    private LocalDateTime createdTime;

    public PemberitahuanKelas() {};

    public PemberitahuanKelas(int idKelas, String isi) {
        this.idKelas = idKelas;
        this.isi = isi;
    }


    public int getIdKelas() {
        return idKelas;
    }


    public String getIsi() {
        return isi;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setIdKelas(int idKelas) {
        this.idKelas = idKelas;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    
}
