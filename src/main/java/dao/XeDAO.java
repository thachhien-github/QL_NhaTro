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
import model.Xe;
import util.DBConnection;

/**
 *
 * @author ThachHien
 */
public class XeDAO {

    private Connection conn;

    public XeDAO(Connection conn) {
        this.conn = conn;
    }

    // Lấy toàn bộ danh sách xe
    public List<Xe> getAllXe() throws SQLException {
        List<Xe> list = new ArrayList<>();
        String sql = "SELECT * FROM Xe";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Xe xe = new Xe(
                        rs.getString("MaXe"),
                        rs.getString("MaKT"),
                        rs.getString("BienSo"),
                        rs.getString("LoaiXe"),
                        rs.getDouble("PhiGiuXe")
                );
                list.add(xe);
            }
        }
        return list;
    }

    // Thêm xe
    public boolean insertXe(Xe xe) throws SQLException {
        String sql = "INSERT INTO Xe(MaXe, MaKT, BienSo, LoaiXe, PhiGiuXe) VALUES(?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, xe.getMaXe());
            ps.setString(2, xe.getMaKT());
            ps.setString(3, xe.getBienSo());
            ps.setString(4, xe.getLoaiXe());
            ps.setDouble(5, xe.getPhiGiuXe());
            return ps.executeUpdate() > 0;
        }
    }

    // Cập nhật xe
    public boolean updateXe(Xe xe) throws SQLException {
        String sql = "UPDATE Xe SET MaKT=?, BienSo=?, LoaiXe=?, PhiGiuXe=? WHERE MaXe=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, xe.getMaKT());
            ps.setString(2, xe.getBienSo());
            ps.setString(3, xe.getLoaiXe());
            ps.setDouble(4, xe.getPhiGiuXe());
            ps.setString(5, xe.getMaXe());
            return ps.executeUpdate() > 0;
        }
    }

    // Xóa xe
    public boolean deleteXe(String maXe) throws SQLException {
        String sql = "DELETE FROM Xe WHERE MaXe=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maXe);
            return ps.executeUpdate() > 0;
        }
    }

    

}
