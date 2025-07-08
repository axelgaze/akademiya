package com.kabe.app.models;

import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;
import com.kabe.app.models.PemberitahuanKelas;

public class Kelas {
        private int id;
        private String nama;
        private String kode;
        private String deskripsi;
        private int jumlahSiswa;
        private int pengajarId;
        private String namaPengajar;
        private List<User> daftarSiswa;
        private LocalDateTime created_time;
        private List<PemberitahuanKelas> pemberitahuanKelas;

        public Kelas() {

        }
    
        public Kelas(int id, String nama, String kode, String deskripsi, LocalDateTime created_time) {
            this.id = id;
            this.nama = nama;
            this.kode = kode;
            this.deskripsi = deskripsi;
            this.created_time = created_time;
            this.daftarSiswa = new ArrayList<>();
            this.pemberitahuanKelas = new ArrayList<>();
            this.jumlahSiswa = 0;
        }

        public Kelas(int id, String nama, String kode, String deskripsi, LocalDateTime created_time, int pengajarId) {
            this.id = id;
            this.nama = nama;
            this.kode = kode;
            this.deskripsi = deskripsi;
            this.created_time = created_time;
            this.daftarSiswa = new ArrayList<>();
            this.pemberitahuanKelas = new ArrayList<>();
            this.jumlahSiswa = 0;
            this.pengajarId = pengajarId;
        }
        
        // Getters
        public int getId() { return id; }
        public String getNama() { return nama; }
        public String getKode() { return kode; }
        public String getDeskripsi() { return deskripsi; }
        public int getJumlahSiswa() { return jumlahSiswa; }
        public int getPengajarId() {return pengajarId; }
        public List<User> getDaftarSiswa() { return daftarSiswa; }
        public String getNamaPengajar() { return namaPengajar; }
        public List<PemberitahuanKelas> getPemberitahuanKelas() {return pemberitahuanKelas;}
        public LocalDateTime getCreatedTime() { return created_time; }
        
        // Setters
        public void setId(int id) { this.id = id; }
        public void setNama(String nama) { this.nama = nama; }
        public void setKode(String kode) { this.kode = kode; }
        public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
        public void setJumlahSiswa(int jumlahSiswa) { this.jumlahSiswa = jumlahSiswa; }
        public void setPengajarId(int pengajarId) { this.pengajarId = pengajarId; }
        public void setDaftarSiswa(List<User> daftarSiswa) { this.daftarSiswa = daftarSiswa; }
        public void setNamaPengajar(String namaPengajar) { this.namaPengajar = namaPengajar; }
        public void setPemberitahuanKelas(List<PemberitahuanKelas> pemberitahuanKelas) { this.pemberitahuanKelas = pemberitahuanKelas;}
        public void setCreatedTime(LocalDateTime created_time) {
            this.created_time = created_time;
        }

        @Override
        public String toString() {
            return this.nama;
        }
    }