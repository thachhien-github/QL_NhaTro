/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Phong;
import util.DBConnection;

/**
 *
 * @author ThachHien
 */
public class PhongDAO {

    private Connection conn;

    public PhongDAO(Connection conn) {
        this.conn = conn;
    }

    // Constructor mặc định (tự tạo connection)
    public PhongDAO() {
        this.conn = DBConnection.getConnection();
    }

    public List<String> getAllMaPhong() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT MaPhong FROM Phong";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("MaPhong"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy toàn bộ phòng
    public List<Phong> getAll() {
        List<Phong> list = new ArrayList<>();
        String sql = "SELECT * FROM Phong";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Phong(
                        rs.getString("MaPhong"),
                        rs.getString("TenPhong"),
                        rs.getString("LoaiPhong"),
                        rs.getInt("GiaThue"),
                        rs.getString("TinhTrang")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy phòng theo mã
    public Phong getById(String maPhong) {
        String sql = "SELECT * FROM Phong WHERE MaPhong=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPhong);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Phong(
                            rs.getString("MaPhong"),
                            rs.getString("TenPhong"),
                            rs.getString("LoaiPhong"),
                            rs.getInt("GiaThue"),
                            rs.getString("TinhTrang")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Thêm phòng
    public boolean insert(Phong p) {
        String sql = "INSERT INTO Phong(MaPhong, TenPhong, LoaiPhong, GiaThue, TinhTrang) "
                + "VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getMaPhong());
            ps.setString(2, p.getTenPhong());
            ps.setString(3, p.getLoaiPhong());
            ps.setDouble(4, p.getGiaThue());
            ps.setString(5, p.getTinhTrang());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Sửa phòng
    public boolean update(Phong p) {
        String sql = "UPDATE Phong SET TenPhong=?, LoaiPhong=?, GiaThue=?, TinhTrang=? "
                + "WHERE MaPhong=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getTenPhong());
            ps.setString(2, p.getLoaiPhong());
            ps.setDouble(3, p.getGiaThue());
            ps.setString(4, p.getTinhTrang());
            ps.setString(5, p.getMaPhong());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa phòng
    public boolean delete(String maPhong) {
        String sql = "DELETE FROM Phong WHERE MaPhong=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPhong);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Tìm kiếm phòng theo tên
    public List<Phong> searchByName(String keyword) {
        List<Phong> list = new ArrayList<>();
        String sql = "SELECT p.MaPhong, p.TenPhong, p.LoaiPhong, p.GiaThue, "
                + "CASE WHEN EXISTS (SELECT 1 FROM HopDong h WHERE h.MaPhong = p.MaPhong) "
                + "     THEN N'Đã thuê' ELSE p.TinhTrang END AS TinhTrang "
                + "FROM Phong p WHERE p.TenPhong LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Phong(
                        rs.getString("MaPhong"),
                        rs.getString("TenPhong"),
                        rs.getString("LoaiPhong"),
                        rs.getDouble("GiaThue"),
                        rs.getString("TinhTrang")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Cập nhật trạng thái phòng
    public boolean updateTrangThai(String maPhong, String tinhTrang) {
        String sql = "UPDATE Phong SET TinhTrang=? WHERE MaPhong=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tinhTrang);
            ps.setString(2, maPhong);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy tiền phòng theo mã phòng
    public double getTienPhongTheoMa(String maPhong) {
        double giaThue = 0;
        String sql = "SELECT GiaThue FROM Phong WHERE MaPhong = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPhong);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    giaThue = rs.getDouble("GiaThue");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return giaThue;
    }

    public double getPhiGiuXeTheoMaPhong(String maPhong) {
        double tongPhiXe = 0;
        String sql = "SELECT SUM(x.PhiGiuXe) AS TongPhiXe "
                + "FROM Xe x "
                + "JOIN KhachThue kt ON x.MaKT = kt.MaKT "
                + "JOIN HopDong hd ON kt.MaKT = hd.MaKT "
                + "WHERE hd.MaPhong = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPhong);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tongPhiXe = rs.getDouble("TongPhiXe");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tongPhiXe;
    }

}
