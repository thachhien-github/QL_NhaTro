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

/**
 *
 * @author ThachHien
 */
public class PhongDAO {
    private Connection conn;

    public PhongDAO(Connection conn) {
        this.conn = conn;
    }

    // CREATE
    public boolean insert(Phong p) {
        String sql = "INSERT INTO Phong(MaPhong, TenPhong, LoaiPhong, GiaThue, TinhTrang) VALUES (?,?,?,?,?)";
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

    // READ ALL
    public List<Phong> getAll() {
        List<Phong> list = new ArrayList<>();
        String sql = "SELECT * FROM Phong";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Phong p = new Phong(
                        rs.getString("MaPhong"),
                        rs.getString("TenPhong"),
                        rs.getString("LoaiPhong"),
                        rs.getDouble("GiaThue"),
                        rs.getString("TinhTrang")
                );
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // READ by ID
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
                            rs.getDouble("GiaThue"),
                            rs.getString("TinhTrang")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // UPDATE
    public boolean update(Phong p) {
        String sql = "UPDATE Phong SET TenPhong=?, LoaiPhong=?, GiaThue=?, TinhTrang=? WHERE MaPhong=?";
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

    // DELETE
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
}
