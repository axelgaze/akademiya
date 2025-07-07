package com.kabe.app.dao;
import com.kabe.app.models.Material;

import com.kabe.app.models.Kelas;
import com.kabe.app.models.PemberitahuanKelas;
import com.kabe.app.models.User;
import com.kabe.app.utils.DatabaseConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
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
                Kelas kelas = new Kelas();
                kelas.setId(rs.getInt("id"));
                kelas.setNama(rs.getString("nama"));
                kelas.setKode(rs.getString("kode"));
                kelas.setDeskripsi(rs.getString("deskripsi"));
                kelas.setPengajarId(rs.getInt("pengajar_id"));
                kelas.setCreatedTime(rs.getTimestamp("created_at").toLocalDateTime());
                return kelas;
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

    public List<Kelas> getClassesByUser(int userId) {
        List<Kelas> kelasList = new ArrayList<>();
        String sql = "SELECT k.id, k.nama, k.kode, k.deskripsi, k.created_at, k.pengajar_id " +
                    "FROM kelas k " +
                    "JOIN kelas_siswa ks ON k.id = ks.idkelas " +
                    "WHERE ks.iduser = ?";
        
        try (Connection conn = dbConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Kelas kelas = new Kelas(
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getString("kode"),
                    rs.getString("deskripsi"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getInt("pengajar_id")
                );
                kelasList.add(kelas);
            }
        } catch (SQLException e) {
            System.err.println("Error getting classes by user: " + e.getMessage());
        }
        return kelasList;
    }

    public boolean addPemberitahuan(int kelasId, String isi) {
        String sql = "INSERT INTO pemberitahuankelas (kelasId, isi) VALUES (?, ?)";
        
        try (Connection conn = dbConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, kelasId);
            pstmt.setString(2, isi);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding pemberitahuan kelas: " + e.getMessage());
            return false;
        }
    }

    public List<PemberitahuanKelas> getPemberitahuanByKelas(int kelasId) {
        List<PemberitahuanKelas> pemberitahuanList = new ArrayList<>();
        String sql = "SELECT * FROM pemberitahuankelas WHERE kelasId = ? ORDER BY created_time DESC;";

        try (Connection conn = dbConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, kelasId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                PemberitahuanKelas pemberitahuanKelas = new PemberitahuanKelas();
                pemberitahuanKelas.setIdKelas(rs.getInt("kelasId"));
                pemberitahuanKelas.setIsi(rs.getString("isi"));
                pemberitahuanKelas.setId(rs.getInt("id"));
                pemberitahuanKelas.setCreatedTime(rs.getTimestamp("created_time").toLocalDateTime());
                pemberitahuanList.add(pemberitahuanKelas);

            }
        } catch (SQLException e) {
            System.err.println("Error getting pemberitahuan by classes: " + e.getMessage());
        }
        return pemberitahuanList;
    }

    public boolean deletePemberitahuan(int idpemberitahuan) {
        String sql = "DELETE FROM pemberitahuankelas WHERE id = ?";

        try (Connection conn = dbConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idpemberitahuan);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error removing pemberitahuan from class: " + e.getMessage());
            return false;
        }
    }

    public Kelas getKelasByKode(String kode) {
        String sql = "SELECT * FROM kelas WHERE kode = ?";

        try (Connection conn = dbConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, kode);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Kelas kelas = new Kelas();
                kelas.setId(rs.getInt("id"));
                kelas.setNama(rs.getString("nama"));
                kelas.setKode(rs.getString("kode"));
                kelas.setDeskripsi(rs.getString("deskripsi"));
                kelas.setCreatedTime(rs.getTimestamp("created_at").toLocalDateTime());
                return kelas;
            }

            

        } catch (SQLException e) {
            System.err.println("Error getting class by kode: " + e.getMessage());
        }

        return null;
    }

    public boolean addMaterial(int kelasId, String title, String description, String fileName, 
                          String fileType, byte[] fileData, int uploaderId) {
        String sql = "INSERT INTO materials (kelas_id, title, description, file_name, " +
                    "file_type, file_data, uploader_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, kelasId);
            pstmt.setString(2, title);
            pstmt.setString(3, description);
            pstmt.setString(4, fileName);
            pstmt.setString(5, fileType);
            pstmt.setBytes(6, fileData);
            pstmt.setInt(7, uploaderId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding material: " + e.getMessage());
            return false;
        }
    }

    public List<Material> getClassMaterials(int kelasId) {
        List<Material> materials = new ArrayList<>();
        String sql = "SELECT id, title, description, file_name, file_type, uploader_id, created_at " +
                    "FROM materials WHERE kelas_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = dbConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, kelasId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Material material = new Material();
                material.setId(rs.getInt("id"));
                material.setKelasId(kelasId);
                material.setTitle(rs.getString("title"));
                material.setDescription(rs.getString("description"));
                material.setFileName(rs.getString("file_name"));
                material.setFileType(rs.getString("file_type"));
                material.setUploaderId(rs.getInt("uploader_id"));
                material.setCreatedTime(rs.getTimestamp("created_at").toLocalDateTime());
                materials.add(material);
            }
        } catch (SQLException e) {
            System.err.println("Error getting class materials: " + e.getMessage());
        }
        return materials;
    }

    public byte[] downloadMaterial(int materialId) {
        String sql = "SELECT file_data FROM materials WHERE id = ?";
        
        try (Connection conn = dbConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, materialId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getBytes("file_data");
            }
        } catch (SQLException e) {
            System.err.println("Error downloading material: " + e.getMessage());
        }
        return null;
    }

    public boolean deleteMaterial(int materialId) {
        String sql = "DELETE FROM materials WHERE id = ?";
        
        try (Connection conn = dbConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, materialId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting material: " + e.getMessage());
            return false;
        }
    }

    public boolean leaveClass(int kelasId, int userId) {
        // Hapus semua materi yang diupload oleh user ini di kelas tersebut (opsional)
        
        // Keluarkan user dari kelas
        String sql = "DELETE FROM kelas_siswa WHERE idkelas = ? AND iduser = ?";
        try (Connection conn = dbConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, kelasId);
            pstmt.setInt(2, userId);
            ResultSet rs = pstmt.executeQuery();

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error leaving class: " + e.getMessage());
            return false;
        }
    }
}