package com.kabe.app.controllers;

import com.kabe.app.dao.KelasDAO;
import com.kabe.app.models.Kelas;
import com.kabe.app.models.PemberitahuanKelas;
import com.kabe.app.models.User;
import java.util.List;

import com.kabe.app.models.Material;

public class KelasController {
    private final KelasDAO kelasDAO;

    public KelasController() {
        this.kelasDAO = new KelasDAO();
    }

    // Kelas Operations
    public int createKelas(String nama, String kode, String deskripsi, int pengajarId) {
        Kelas newKelas = new Kelas();
        newKelas.setNama(nama);
        newKelas.setKode(kode);
        newKelas.setDeskripsi(deskripsi);
        newKelas.setPengajarId(pengajarId);
        
        return kelasDAO.addKelas(newKelas);
    }

    public Kelas getKelasById(int id) {
        return kelasDAO.getKelasById(id);
    }

    public boolean isKodeExists(String kode) {
        return kelasDAO.isKodeExists(kode);
    }

    // Student Operations
    public List<User> getStudentsByClass(int kelasId) {
        return kelasDAO.getSiswa(kelasId);
    }

    public int getStudentCount(int kelasId) {
        return kelasDAO.getJumlahSiswa(kelasId);
    }

    public boolean addStudentToClass(int kelasId, int userId) {
        // You might want to add validation here
        return kelasDAO.addSiswaToKelas(kelasId, userId);
    }

    public boolean removeStudentFromClass(int kelasId, int userId) {
        return kelasDAO.removeSiswaFromKelas(kelasId, userId);
    }

    // Teacher Operations
    public User getClassTeacher(int kelasId) {
        return kelasDAO.getPengajar(kelasId);
    }

    public boolean assignTeacherToClass(int kelasId, int teacherId) {
        // You might want to add validation that the user is actually a teacher
        return kelasDAO.updatePengajar(kelasId, teacherId);
    }

    public List<Kelas> getClassesByTeacher(int teacherId) {
        return kelasDAO.getKelasByPengajar(teacherId);
    }

    public boolean deleteClass(int kelasId) {
        return kelasDAO.deleteClass(kelasId);
    }

    public boolean addPemberitahuan(int kelasId, String isi) {
        return kelasDAO.addPemberitahuan(kelasId, isi);
    }

    public List<PemberitahuanKelas> getPemberitahuanKelas(int kelasId) {
        return kelasDAO.getPemberitahuanByKelas(kelasId);
    }

    public boolean deletePemberitahuan(int pemberitahuanId) {
        return kelasDAO.deletePemberitahuan(pemberitahuanId);
    }

    public List<Kelas> getClassesByUser(int siswaid) {
        return kelasDAO.getClassesByUser(siswaid);
    }

    public Kelas getKelasByKode(String kode) {
        return kelasDAO.getKelasByKode(kode);
    }

    public boolean addMaterial(int kelasId, String title, String description, 
                         String fileName, String fileType, byte[] fileData, int uploaderId) {
        // Validasi ukuran file (misal maksimal 10MB)
        if (fileData.length > 10 * 1024 * 1024) {
            System.err.println("File size exceeds maximum limit (10MB)");
            return false;
        }
        
        // Validasi tipe file yang diizinkan
        if (!isValidFileType(fileType)) {
            System.err.println("Invalid file type");
            return false;
        }
        
        return kelasDAO.addMaterial(kelasId, title, description, fileName, fileType, fileData, uploaderId);
    }

    public List<Material> getClassMaterials(int kelasId) {
        return kelasDAO.getClassMaterials(kelasId);
    }

    public byte[] downloadMaterial(int materialId) {
        // Catat aktivitas download (opsional)
        // bisa ditambahkan logging ke tabel terpisah
        
        return kelasDAO.downloadMaterial(materialId);
    }

    public boolean deleteMaterial(int materialId) {
        return kelasDAO.deleteMaterial(materialId);
    }

    public boolean leaveClass(int kelasId, int userId) {
        // Validasi apakah user benar-benar terdaftar di kelas tersebut
        
        return kelasDAO.leaveClass(kelasId, userId);
    }

    private boolean isValidFileType(String fileType) {
        // Daftar tipe file yang diizinkan
        String[] allowedTypes = {"pdf", "docx", "pptx", "jpg", "png", "mp4"};
        for (String type : allowedTypes) {
            if (fileType.equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }
}