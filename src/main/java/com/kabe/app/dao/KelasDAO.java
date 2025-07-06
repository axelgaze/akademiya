package com.kabe.app.dao;

import com.kabe.app.models.Kelas;
import com.kabe.app.models.User;
import com.kabe.app.utils.DatabaseConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class KelasDAO {
    private final DatabaseConnector dbConnector;

    public KelasDAO() {
        this.dbConnector = new DatabaseConnector();
    }

    public int addKelas(Kelas kelas) {
        String sql = "INSERT INTO kelas (nama, kode, deskripsi, pengajar_id) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Set parameters
            pstmt.setString(1, kelas.getNama());
            pstmt.setString(2, kelas.getKode());
            pstmt.setString(3, kelas.getDeskripsi());
            pstmt.setInt(4, kelas.getPengajarId());
            
            // Execute insert
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating class failed, no rows affected.");
            }
            
            // Get the generated ID
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

    public boolean isKodeExists(String kode) {
        String sql = "SELECT COUNT(*) FROM kelas WHERE kode = ?";
        
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, kode);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking class code: " + e.getMessage());
        }
        return false;
    }

    public Kelas getKelasById(int id) {
        String sql = "SELECT * FROM kelas WHERE id = ?";
        
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Kelas(
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getString("kode"),
                    rs.getString("deskripsi"),
                    rs.getTimestamp("created_at").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting class: " + e.getMessage());
        }
        return null;
    }

    public List<User> getSiswa(int kelasId) {
        List<User> siswaList = new ArrayList<>();
        String sql = "SELECT u.id, u.full_name, u.email, u.role " +
                    "FROM users u " +
                    "JOIN kelas_siswa ks ON u.id = ks.iduser " +
                    "WHERE ks.idkelas = ? AND u.role = 'siswa'";
        
        try (Connection conn = dbConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, kelasId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                User siswa = new User();
                siswa.setId(rs.getInt("id"));
                siswa.setFullName(rs.getString("full_name"));
                siswa.setEmail(rs.getString("email"));
                siswa.setRole(rs.getString("role"));
                siswaList.add(siswa);
            }
        } catch (SQLException e) {
            System.err.println("Error getting students: " + e.getMessage());
        }
        return siswaList;
    }

    public int getJumlahSiswa(int kelasId) {
        String sql = "SELECT COUNT(*) FROM kelas_siswa WHERE idkelas = ?";
        
        try (Connection conn = dbConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, kelasId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting students: " + e.getMessage());
        }
        return 0;
    }

    public User getPengajar(int kelasId) {
        String sql = "SELECT u.id, u.full_name, u.email, u.role " +
                    "FROM users u " +
                    "JOIN kelas k ON u.id = k.pengajar_id " +
                    "WHERE k.id = ? AND u.role = 'pengajar'";
        
        try (Connection conn = dbConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, kelasId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                User pengajar = new User();
                pengajar.setId(rs.getInt("id"));
                pengajar.setFullName(rs.getString("full_name"));
                pengajar.setEmail(rs.getString("email"));
                pengajar.setRole(rs.getString("role"));
                return pengajar;
            }
        } catch (SQLException e) {
            System.err.println("Error getting teacher: " + e.getMessage());
        }
        return null;
    }

    public boolean addSiswaToKelas(int idkelas, int iduser) {
        String sql = "INSERT INTO kelas_siswa (idkelas, iduser) VALUES (?, ?)";
        
        try (Connection conn = dbConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idkelas);
            pstmt.setInt(2, iduser);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding student to class: " + e.getMessage());
            return false;
        }
    }

    public boolean removeSiswaFromKelas(int idkelas, int iduser) {
        String sql = "DELETE FROM kelas_siswa WHERE idkelas = ? AND iduser = ?";
        
        try (Connection conn = dbConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idkelas);
            pstmt.setInt(2, iduser);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error removing student from class: " + e.getMessage());
            return false;
        }
    }

    public boolean updatePengajar(int kelasId, int pengajarId) {
        String sql = "UPDATE kelas SET pengajar_id = ? WHERE id = ?";
        
        try (Connection conn = dbConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, pengajarId);
            pstmt.setInt(2, kelasId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating teacher: " + e.getMessage());
            return false;
        }
    }

    public List<Kelas> getKelasByPengajar(int pengajarId) {
        List<Kelas> kelasList = new ArrayList<>();
        String sql = "SELECT * FROM kelas WHERE pengajar_id = ?";
        
        try (Connection conn = dbConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, pengajarId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Kelas kelas = new Kelas();
                kelas.setId(rs.getInt("id"));
                kelas.setNama(rs.getString("nama"));
                kelas.setKode(rs.getString("kode"));
                kelas.setDeskripsi(rs.getString("deskripsi"));
                kelasList.add(kelas);
            }
        } catch (SQLException e) {
            System.err.println("Error getting classes by teacher: " + e.getMessage());
        }
        return kelasList;
    }
    
    private boolean deleteAllEnrollments(int kelasId) {
        String sql = "DELETE FROM kelas_siswa WHERE idkelas = ?";
        
        try (Connection conn = dbConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, kelasId);
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error deleting class enrollments: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteClass(int kelasId) {
        // First delete all student enrollments (to maintain referential integrity)
        if (!deleteAllEnrollments(kelasId)) {
            return false;
        }
        
        // Then delete the class itself
        String sql = "DELETE FROM kelas WHERE id = ?";
        
        try (Connection conn = dbConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, kelasId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting class: " + e.getMessage());
            return false;
        }
    }
}