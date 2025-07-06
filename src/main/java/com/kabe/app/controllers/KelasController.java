package com.kabe.app.controllers;

import com.kabe.app.dao.KelasDAO;
import com.kabe.app.models.Kelas;
import com.kabe.app.models.User;
import java.util.List;

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
}