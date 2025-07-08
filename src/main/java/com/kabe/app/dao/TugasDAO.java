package com.kabe.app.dao;

import com.kabe.app.utils.DatabaseConnector;
import com.kabe.app.models.*;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TugasDAO {
    private final DatabaseConnector dbConnector;

    public TugasDAO() {
        this.dbConnector = new DatabaseConnector();
    }

    public int addTugas(Tugas tugas) {
        String sql = "INSERT INTO tugas (kelas_id, title, file_name, file_type, file_data, uploader_id, deadline, deskripsi, tipe) VALUES (?, ?, ?, ?, ?, ?, ? ,?, ?)";

        try (Connection conn = dbConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Set parameters
            pstmt.setInt(1, tugas.getKelasId());
            pstmt.setString(2, tugas.getTitle());
            pstmt.setString(3, tugas.getFileName());
            pstmt.setString(4, tugas.getFileType());
            pstmt.setBytes(5, tugas.getFileData());
            pstmt.setInt(6, tugas.getUploaderId());
            pstmt.setTimestamp(7, tugas.getDeadline());
            pstmt.setString(8, tugas.getDeskripsi());
            pstmt.setString(9, tugas.getTipe());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating task failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating class failed, no ID obtained.");
                }
            } 
        } catch (SQLException e) {
                System.err.println("Error adding class: " + e.getMessage());
                return -1;
        }       
    }

    public List<Tugas> getTugasByTeacher(int userId) {
        String sql = "SELECT * FROM tugas WHERE uploader_id = ? ORDER BY created_at DESC";
        List<Tugas> tugasList = new ArrayList<>();

        try (Connection conn = dbConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Tugas tugas = new Tugas();
                    tugas.setId(rs.getInt("id"));
                    tugas.setKelasId(rs.getInt("kelas_id"));
                    tugas.setTitle(rs.getString("title"));
                    tugas.setFileName(rs.getString("file_name"));
                    tugas.setFileType(rs.getString("file_type"));
                    // Tidak mengambil file_data untuk efisiensi
                    tugas.setUploaderId(rs.getInt("uploader_id"));
                    tugas.setCreatedAt(rs.getTimestamp("created_at"));
                    tugas.setDeadline(rs.getTimestamp("deadline"));
                    tugas.setDeskripsi(rs.getString("deskripsi"));
                    tugas.setTipe(rs.getString("tipe"));
                    
                    tugasList.add(tugas);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting tasks by teacher: " + e.getMessage());
            e.printStackTrace();
        }
        
        return tugasList;
    }

    public List<Tugas> getTugasBySiswa(int siswaId) {
        String sql = "SELECT t.* FROM tugas t " +
                    "JOIN kelas k ON t.kelas_id = k.id " +
                    "JOIN kelas_siswa ks ON k.id = ks.idkelas " +
                    "WHERE ks.iduser = ? " +
                    "ORDER BY t.deadline ASC"; // Urutkan berdasarkan deadline
        
        List<Tugas> tugasList = new ArrayList<>();

        try (Connection conn = dbConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, siswaId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Tugas tugas = new Tugas();
                    tugas.setId(rs.getInt("id"));
                    tugas.setKelasId(rs.getInt("kelas_id"));
                    tugas.setTitle(rs.getString("title"));
                    tugas.setFileName(rs.getString("file_name"));
                    tugas.setFileType(rs.getString("file_type"));
                    // Tidak mengambil file_data untuk efisiensi
                    tugas.setUploaderId(rs.getInt("uploader_id"));
                    tugas.setCreatedAt(rs.getTimestamp("created_at"));
                    tugas.setDeadline(rs.getTimestamp("deadline"));
                    tugas.setDeskripsi(rs.getString("deskripsi"));
                    tugas.setTipe(rs.getString("tipe"));
                    
                    tugasList.add(tugas);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting tasks by student: " + e.getMessage());
            e.printStackTrace();
        }
        
        return tugasList;
    }

    public int uploadTugas(TugasSiswa tugasSiswa) {
        String sql = "INSERT INTO tugas_siswa (idtugas, idsiswa, file_name, file_data) " +
                    "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Set parameter
            pstmt.setInt(1, tugasSiswa.getIdTugas());
            pstmt.setInt(2, tugasSiswa.getIdSiswa());
            pstmt.setString(3, tugasSiswa.getFileName());
            pstmt.setBytes(4, tugasSiswa.getFileData());
            
            // Execute query
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Upload tugas gagal, tidak ada row yang terpengaruh.");
            }
            
            // Ambil ID yang baru dibuat
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Upload tugas gagal, tidak mendapatkan ID.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error uploading tugas: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    public List<TugasSiswa> getAllTugasSiswa(int idTugas, int idSiswa) {
        List<TugasSiswa> list = new ArrayList<>();
        String sql = "SELECT * FROM tugas_siswa WHERE idtugas = ? AND idsiswa = ? ORDER BY created_at DESC";
        
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idTugas);
            pstmt.setInt(2, idSiswa);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToTugasSiswa(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting all tugas siswa: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    private TugasSiswa mapResultSetToTugasSiswa(ResultSet rs) throws SQLException {
        TugasSiswa ts = new TugasSiswa();
        ts.setId(rs.getInt("id"));
        ts.setIdTugas(rs.getInt("idtugas"));
        ts.setIdSiswa(rs.getInt("idsiswa"));
        ts.setFileName(rs.getString("file_name"));
        ts.setFileData(rs.getBytes("file_data"));
        ts.setStatus(rs.getString("status"));
        ts.setNilai(rs.getString("nilai"));
        ts.setFeedback(rs.getString("feedback"));
        ts.setFeedbackAt(rs.getTimestamp("feedback_at"));
        ts.setCreatedAt(rs.getTimestamp("created_at"));
        return ts;
    }

    public boolean deleteTugasSiswa(int idTugasSiswa, int idSiswa) {
        String sql = "DELETE FROM tugas_siswa WHERE id = ? AND idsiswa = ?";
        
        try (Connection conn = dbConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idTugasSiswa);
            pstmt.setInt(2, idSiswa);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting tugas siswa: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStatus(int idTugas, int idSiswa) {
        // Pertama dapatkan status saat ini
        String getStatusSql = "SELECT status FROM tugas_siswa WHERE idtugas = ? AND idsiswa = ?";
        String updateStatusSql = "UPDATE tugas_siswa SET status = ? WHERE idtugas = ? AND idsiswa = ?";
        
        try (Connection conn = dbConnector.getConnection();
            PreparedStatement getStmt = conn.prepareStatement(getStatusSql);
            PreparedStatement updateStmt = conn.prepareStatement(updateStatusSql)) {
            
            // Dapatkan status saat ini
            getStmt.setInt(1, idTugas);
            getStmt.setInt(2, idSiswa);
            
            String currentStatus = "belum"; // default
            try (ResultSet rs = getStmt.executeQuery()) {
                if (rs.next()) {
                    currentStatus = rs.getString("status");
                }
            }
            
            // Tentukan status baru
            String newStatus = "belum".equalsIgnoreCase(currentStatus) ? "sudah" : "belum";
            
            // Update status
            updateStmt.setString(1, newStatus);
            updateStmt.setInt(2, idTugas);
            updateStmt.setInt(3, idSiswa);
            
            int affectedRows = updateStmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error toggling task status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public String getStatusTugas(int idTugas, int idSiswa) {
        // SQL to get both submission status and deadline in one query
        String sql = "SELECT ts.status, t.deadline " +
                    "FROM tugas t " +
                    "LEFT JOIN tugas_siswa ts ON t.id = ts.idtugas AND ts.idsiswa = ? " +
                    "WHERE t.id = ?";
        
        try (Connection conn = dbConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idSiswa);
            stmt.setInt(2, idTugas);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String status = rs.getString("status");
                    Timestamp deadline = rs.getTimestamp("deadline");
                    Timestamp now = new Timestamp(System.currentTimeMillis());
                    
                    // If there's a submission status
                    if (status != null) {
                        return "belum".equalsIgnoreCase(status) ? "Belum Dikumpulkan" : "Sudah Dikumpulkan";
                    }
                    // If no submission record exists
                    else {
                        if (now.after(deadline)) {
                            return "Terlambat";
                        } else {
                            return "Belum Dikumpulkan";
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting task status: " + e.getMessage());
            e.printStackTrace();
        }
        
        return "Error"; // Fallback if any error occurs
    }

    public int countUniquePengumpul(int idTugas) {
        String sql = "SELECT COUNT(DISTINCT idsiswa) FROM tugas_siswa " +
                     "WHERE idtugas = ? AND status = 'sudah'";
        
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idTugas);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error counting unique submitters: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0; // Return 0 jika terjadi error
    }

    public List<User> getSiswaSudahMengumpulkan(int idTugas) {
        List<User> siswaList = new ArrayList<>();
        String sql = "SELECT DISTINCT u.id, u.full_name, u.email, u.role " +
                    "FROM users u " +
                    "JOIN tugas_siswa ts ON u.id = ts.idsiswa " +
                    "WHERE ts.idtugas = ? AND ts.status = 'sudah' " +
                    "ORDER BY u.full_name";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idTugas);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    User siswa = mapResultSetToUser(rs);
                    siswaList.add(siswa);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting students who submitted: " + e.getMessage());
            e.printStackTrace();
        }
        
        return siswaList;
    }

    public List<User> getSiswaBelumMengumpulkan(int idTugas, int idKelas) {
        List<User> siswaList = new ArrayList<>();
        String sql = "SELECT u.id, u.full_name, u.email, u.role " +
                    "FROM users u " +
                    "JOIN kelas_siswa ks ON u.id = ks.iduser " +
                    "LEFT JOIN tugas_siswa ts ON u.id = ts.idsiswa AND ts.idtugas = ? " +
                    "WHERE ks.idkelas = ? AND u.role = 'siswa' " +
                    "AND (ts.status IS NULL OR ts.status = 'belum') " +
                    "ORDER BY u.full_name";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idTugas);
            pstmt.setInt(2, idKelas);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    User siswa = mapResultSetToUser(rs);
                    siswaList.add(siswa);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting students who haven't submitted: " + e.getMessage());
            e.printStackTrace();
        }
        
        return siswaList;
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setRole(rs.getString("role"));
        return user;
    }

    public byte[] downloadTugasSiswa(int idTugas, int idSiswa) {
        String sql = "SELECT file_data FROM tugas_siswa WHERE idtugas = ? AND idsiswa = ? ORDER BY created_at DESC LIMIT 1";
        
        try (Connection conn = dbConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idTugas);
            pstmt.setInt(2, idSiswa);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBytes("file_data");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error downloading student task: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public String getFileName(int idTugas, int idSiswa) {
        String sql = "SELECT file_name FROM tugas_siswa WHERE idtugas = ? AND idsiswa = ? ORDER BY created_at DESC LIMIT 1";
        
        try (Connection conn = dbConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idTugas);
            pstmt.setInt(2, idSiswa);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("file_name");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting file name: " + e.getMessage());
            e.printStackTrace();
        }
        return "tugas_siswa_" + idTugas + "_" + idSiswa + ".bin"; // Nama default jika tidak ditemukan
    }

    public boolean beriNilai(int idTugas, int idSiswa, String nilai) {
        String sql = "UPDATE tugas_siswa SET nilai = ? WHERE idtugas = ? AND idsiswa = ?";
        try (Connection conn = dbConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nilai);
            pstmt.setInt(2, idTugas);
            pstmt.setInt(3, idSiswa);
            
            int affectedRows = pstmt.executeUpdate();
            System.out.println(affectedRows);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error giving nilai: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean beriFeedback(int idTugas, int idSiswa, String feedback) {
        String sql = "UPDATE tugas_siswa SET feedback = ?, feedback_at = CURRENT_TIMESTAMP " +
                    "WHERE idtugas = ? AND idsiswa = ?";
        
        try (Connection conn = dbConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, feedback);
            pstmt.setInt(2, idTugas);
            pstmt.setInt(3, idSiswa);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error giving feedback: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public TugasSiswa getDetailTugasSiswa(int idTugas, int idSiswa) {
        String sql = "SELECT * FROM tugas_siswa WHERE idtugas = ? AND idsiswa = ? ORDER BY created_at DESC LIMIT 1";
        
        try (Connection conn = dbConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idTugas);
            pstmt.setInt(2, idSiswa);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTugasSiswa(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting task details: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}

