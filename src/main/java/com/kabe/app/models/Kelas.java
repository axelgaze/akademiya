package com.kabe.app.models;

import java.util.List;
import java.util.ArrayList;

public class Kelas {
        private String nama;
        private String pengajar;
        private String kode;
        private int jumlahSiswa;
        private String deskripsi;
        private String[] anggota;
        
        public Kelas(String nama, String pengajar, String kode, int jumlahSiswa, 
                        String deskripsi, String[] anggota) {
            this.nama = nama;
            this.pengajar = pengajar;
            this.kode = kode;
            this.jumlahSiswa = jumlahSiswa;
            this.deskripsi = deskripsi;
            this.anggota = anggota;
        }
        
        // Getters
        public String getNama() { return nama; }
        public String getPengajar() { return pengajar; }
        public String getKode() { return kode; }
        public int getJumlahSiswa() { return jumlahSiswa; }
        public String getDeskripsi() { return deskripsi; }
        public String[] getAnggota() { return anggota; }
        
        // Setters
        public void setNama(String nama) { this.nama = nama; }
        public void setPengajar(String pengajar) { this.pengajar = pengajar; }
        public void setKode(String kode) { this.kode = kode; }
        public void setJumlahSiswa(int jumlahSiswa) { this.jumlahSiswa = jumlahSiswa; }
        public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
        public void setAnggota(String[] anggota) { this.anggota = anggota; }
    }