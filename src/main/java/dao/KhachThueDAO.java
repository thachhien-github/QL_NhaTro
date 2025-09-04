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
import model.KhachThue;

/**
 *
 * @author ThachHien
 */
public class KhachThueDAO {

    private Connection conn;

    public KhachThueDAO(Connection conn) {
        this.conn = conn;
    }

    // ===== CREATE =====
    public boolean insert(KhachThue kt) {
        String sql = "INSERT INTO KhachThue(MaKT, HoTen, GioiTinh, NgaySinh, CCCD, DiaChi, SDT, MaPhong) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kt.getMaKT());
            ps.setString(2, kt.getHoTen());
            ps.setString(3, kt.getGioiTinh());
            ps.setDate(4, kt.getNgaySinh());
            ps.setString(5, kt.getCccd());
            ps.setString(6, kt.getDiaChi());
            ps.setString(7, kt.getSdt());
            ps.setString(8, kt.getMaPhong());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===== READ ALL =====
    public List<KhachThue> getAll() {
        List<KhachThue> list = new ArrayList<>();
        String sql = "SELECT * FROM KhachThue";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                KhachThue kt = new KhachThue(
                        rs.getString("MaKT"),
                        rs.getString("HoTen"),
                        rs.getString("GioiTinh"),
                        rs.getDate("NgaySinh"),
                        rs.getString("CCCD"),
                        rs.getString("DiaChi"),
                        rs.getString("SDT"),
                        rs.getString("MaPhong")
                );
                list.add(kt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ===== READ by ID =====
    public KhachThue getById(String maKT) {
        String sql = "SELECT * FROM KhachThue WHERE MaKT = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKT);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new KhachThue(
                            rs.getString("MaKT"),
                            rs.getString("HoTen"),
                            rs.getString("GioiTinh"),
                            rs.getDate("NgaySinh"),
                            rs.getString("CCCD"),
                            rs.getString("DiaChi"),
                            rs.getString("SDT"),
                            rs.getString("MaPhong")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ===== UPDATE =====
    public boolean update(KhachThue kt) {
        String sql = "UPDATE KhachThue SET HoTen=?, GioiTinh=?, NgaySinh=?, CCCD=?, DiaChi=?, SDT=?, MaPhong=? WHERE MaKT=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kt.getHoTen());
            ps.setString(2, kt.getGioiTinh());
            ps.setDate(3, kt.getNgaySinh());
            ps.setString(4, kt.getCccd());
            ps.setString(5, kt.getDiaChi());
            ps.setString(6, kt.getSdt());
            ps.setString(7, kt.getMaPhong());
            ps.setString(8, kt.getMaKT());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===== DELETE =====
    public boolean delete(String maKT) {
        String sql = "DELETE FROM KhachThue WHERE MaKT=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKT);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
