package com.kabe.app.controllers;

import com.kabe.app.dao.KelasDAO;
import com.kabe.app.models.Kelas;
import com.kabe.app.models.PemberitahuanKelas;
import com.kabe.app.models.User;
import com.kabe.app.models.Material;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.io.File;

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

    public boolean uploadMateri(int kelasId, File file, int uploaderId) {
        try {
            // Baca file menjadi byte array
            FileInputStream fis = new FileInputStream(file);
            byte[] fileData = new byte[(int) file.length()];
            fis.read(fileData);
            fis.close();
            
            // Dapatkan nama file dan ekstensi
            String fileName = file.getName();
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
            
            // Upload ke database
            return kelasDAO.uploadMateri(kelasId, fileName, fileType, fileData, uploaderId);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Material> getMaterialsForKelas(int kelasId) {
        return kelasDAO.getMaterialsByKelasId(kelasId);
    }

    public byte[] downloadMaterial(int materialId) {
        return kelasDAO.downloadMaterial(materialId);
    }
}