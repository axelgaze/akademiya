package com.kabe.app.controllers;

import com.kabe.app.dao.TugasDAO;
import com.kabe.app.models.Kelas;
import com.kabe.app.models.PemberitahuanKelas;
import com.kabe.app.models.User;
import com.kabe.app.models.TugasSiswa;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import com.kabe.app.models.Tugas;
import com.kabe.app.models.Material;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.io.File;
import java.time.LocalDateTime;
import java.sql.Timestamp;

public class TugasController {
    private final TugasDAO tugasDAO;

    public TugasController() {
        this.tugasDAO = new TugasDAO();
    }

    public int addTugas(int kelasId, String title, String fileName, String fileType, byte[] fileData, int uploaderId, Timestamp deadline, String deskripsi, String tipe) {
        Tugas tugas = new Tugas();
        tugas.setKelasId(kelasId);
        tugas.setTitle(title);
        tugas.setFileName(fileName);
        tugas.setFileType(fileType);
        tugas.setFileData(fileData);
        tugas.setUploaderId(uploaderId);
        tugas.setDeadline(deadline);
        tugas.setDeskripsi(deskripsi);
        tugas.setTipe(tipe);

        return tugasDAO.addTugas(tugas);
    }

    public List<Tugas> getTugasByTeacher(int userId) {
        return tugasDAO.getTugasByTeacher(userId);
    }

    public List<Tugas> getTugasBySiswa(int siswaId) {
        return tugasDAO.getTugasBySiswa(siswaId);
    }

    public int uploadTugas(TugasSiswa tugas) {
        return tugasDAO.uploadTugas(tugas);
    }

    public List<TugasSiswa> getAllTugasSiswa(int idTugas, int idSiswa) {
        return tugasDAO.getAllTugasSiswa(idTugas, idSiswa);
    }

    public boolean deleteTugasSiswa(int idTugas, int idSiswa) {
        return tugasDAO.deleteTugasSiswa(idTugas, idSiswa);
    }

    public boolean updateStatus(int idTugas, int idSiswa) {
        return tugasDAO.updateStatus(idTugas, idSiswa);
    }

    public String getStatusTugas(int idTugas, int idSiswa) {
        return tugasDAO.getStatusTugas(idTugas, idSiswa);
    }

    public int countUniquePengumpul(int idTugas) {
        return tugasDAO.countUniquePengumpul(idTugas);
    }

    public List<User> getSiswaBelumMengumpulkan(int idTugas, int idKelas) {
        return tugasDAO.getSiswaBelumMengumpulkan(idTugas, idKelas);
    }

    public List<User> getSiswaSudahMengumpulkan(int idTugas) {
        return tugasDAO.getSiswaSudahMengumpulkan(idTugas);
    }

    public byte[] downloadTugasSiswa(int idTugas, int idSiswa) {
        return tugasDAO.downloadTugasSiswa(idTugas, idSiswa);
    }

    public String getFileName(int idTugas, int idSiswa) {
        return tugasDAO.getFileName(idTugas, idSiswa);
    }

    public boolean beriNilai(int idTugas, int idSiswa, String nilai) {
        return tugasDAO.beriNilai(idTugas, idSiswa, nilai);
    }

    public boolean beriFeedback(int idTugas, int idSiswa, String feedback) {
        return tugasDAO.beriFeedback(idTugas, idSiswa, feedback);
    }

    public TugasSiswa getDetailTugasSiswa(int idTugas, int idSiswa) {
        return tugasDAO.getDetailTugasSiswa(idTugas, idSiswa);
    }
}
