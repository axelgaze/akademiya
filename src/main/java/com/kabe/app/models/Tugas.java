package com.kabe.app.models;

import java.util.List;

public class Tugas {
    private Integer id;
    private String judul;
    private String deskripsi;
    private String deadline;

    private Integer idMurid; // FK ke Siswa (Many to One)
    private String idKelas;  // optional, jika tiap tugas punya konteks kelas

    private List<LampiranSiswa> daftarLampiran;
}