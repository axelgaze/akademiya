package com.kabe.app.models;

public class LampiranSiswa {
    private Integer id;
    private Integer idTugas;     // FK ke tugas
    private String filePath;     // path file lokal
    private String namaFile;
}