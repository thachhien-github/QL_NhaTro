/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.HoaDon;
import util.DBConnection;

/**
 *
 * @author ThachHien
 */
public class HoaDonDAO {

    // ===== Lấy tất cả hóa đơn =====
    public List<HoaDon> getAll() {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT MaHDon, MaPhong, Thang, Nam, TienPhong, TienDien, TienNuoc, TienXe, "
                + "(TienPhong + TienDien + TienNuoc + TienXe) AS TongTien "
                + "FROM HoaDon";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                HoaDon hd = new HoaDon(
                        rs.getString("MaHDon"),
                        rs.getString("MaPhong"),
                        rs.getInt("Thang"),
                        rs.getInt("Nam"),
                        rs.getDouble("TienPhong"),
                        rs.getDouble("TienDien"),
                        rs.getDouble("TienNuoc"),
                        rs.getDouble("TienXe"),
                        rs.getDouble("TongTien") // Tính trực tiếp từ SELECT
                );
                list.add(hd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ===== Lấy hóa đơn theo mã =====
    public HoaDon getById(String maHDon) {
        String sql = "SELECT MaHDon, MaPhong, Thang, Nam, TienPhong, TienDien, TienNuoc, TienXe, "
                + "(TienPhong + TienDien + TienNuoc + TienXe) AS TongTien "
                + "FROM HoaDon WHERE MaHDon=?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maHDon);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new HoaDon(
                            rs.getString("MaHDon"),
                            rs.getString("MaPhong"),
                            rs.getInt("Thang"),
                            rs.getInt("Nam"),
                            rs.getDouble("TienPhong"),
                            rs.getDouble("TienDien"),
                            rs.getDouble("TienNuoc"),
                            rs.getDouble("TienXe"),
                            rs.getDouble("TongTien") // Tính trực tiếp từ SELECT
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insert(HoaDon hd) {
        String sql = "INSERT INTO HoaDon(MaPhong, Thang, Nam, TienPhong, TienDien, TienNuoc, TienXe) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, hd.getMaPhong());
            ps.setInt(2, hd.getThang());
            ps.setInt(3, hd.getNam());
            ps.setDouble(4, hd.getTienPhong());
            ps.setDouble(5, hd.getTienDien());
            ps.setDouble(6, hd.getTienNuoc());
            ps.setDouble(7, hd.getTienXe());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa hóa đơn
    public boolean delete(String maHDon) {
        String sql = "DELETE FROM HoaDon WHERE MaHDon=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHDon);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
