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

    public boolean insert(HoaDon hd) {
        String sql = "INSERT INTO HoaDon (MaHDon, MaPhong, Thang, Nam, TienPhong, TienDien, TienNuoc, TienXe) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hd.getMaHDon());
            ps.setString(2, hd.getMaPhong());
            ps.setInt(3, hd.getThang());
            ps.setInt(4, hd.getNam());
            ps.setBigDecimal(5, hd.getTienPhong());
            ps.setBigDecimal(6, hd.getTienDien());
            ps.setBigDecimal(7, hd.getTienNuoc());
            ps.setBigDecimal(8, hd.getTienXe());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(HoaDon hd) {
        String sql = "UPDATE HoaDon SET TienPhong=?, TienDien=?, TienNuoc=?, TienXe=? "
                + "WHERE MaHDon=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, hd.getTienPhong());
            ps.setBigDecimal(2, hd.getTienDien());
            ps.setBigDecimal(3, hd.getTienNuoc());
            ps.setBigDecimal(4, hd.getTienXe());
            ps.setString(5, hd.getMaHDon());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(String maHDon) {
        String sql = "DELETE FROM HoaDon WHERE MaHDon=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHDon);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<HoaDon> getAll() {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                HoaDon hd = new HoaDon(
                        rs.getString("MaHDon"),
                        rs.getString("MaPhong"),
                        rs.getInt("Thang"),
                        rs.getInt("Nam"),
                        rs.getBigDecimal("TienPhong"),
                        rs.getBigDecimal("TienDien"),
                        rs.getBigDecimal("TienNuoc"),
                        rs.getBigDecimal("TienXe"),
                        rs.getBigDecimal("TongTien")
                );
                list.add(hd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
