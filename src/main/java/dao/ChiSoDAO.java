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
import model.ChiSo;
import java.util.ArrayList;
import java.util.List;
import util.DBConnection;

/**
 *
 * @author ThachHien
 */
public class ChiSoDAO {

    private Connection conn;

    public ChiSoDAO(Connection conn, Object par1) {
        this.conn = conn;
    }
    
    // Constructor mặc định (tự tạo connection)
    public ChiSoDAO() {
        this.conn = DBConnection.getConnection();
    }

    // Lấy toàn bộ chỉ số
    public List<ChiSo> getAll() throws SQLException {
        List<ChiSo> list = new ArrayList<>();
        String sql = "EXEC sp_XemChiSo";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                ChiSo cs = new ChiSo(
                        rs.getString("MaPhong"),
                        rs.getInt("Thang"),
                        rs.getInt("Nam"),
                        rs.getInt("DienCu"),
                        rs.getInt("DienMoi"),
                        rs.getInt("NuocCu"),
                        rs.getInt("NuocMoi"),
                        rs.getInt("ChiSoDien"),
                        rs.getInt("ChiSoNuoc")
                );
                list.add(cs);
            }
        }
        return list;
    }

    // Thêm mới chỉ số điện nước
    public boolean insert(ChiSo cs) throws SQLException {
        String sql = "EXEC sp_ThemChiSo ?,?,?,?,?,?,?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cs.getMaPhong());
            ps.setInt(2, cs.getThang());
            ps.setInt(3, cs.getNam());
            ps.setInt(4, cs.getDienCu());
            ps.setInt(5, cs.getDienMoi());
            ps.setInt(6, cs.getNuocCu());
            ps.setInt(7, cs.getNuocMoi());
            return ps.executeUpdate() > 0;
        }
    }

    // Cập nhật chỉ số
    public boolean update(ChiSo cs) throws SQLException {
        String sql = "EXEC sp_SuaChiSo ?,?,?,?,?,?,?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cs.getMaPhong());
            ps.setInt(2, cs.getThang());
            ps.setInt(3, cs.getNam());
            ps.setInt(4, cs.getDienCu());
            ps.setInt(5, cs.getDienMoi());
            ps.setInt(6, cs.getNuocCu());
            ps.setInt(7, cs.getNuocMoi());
            return ps.executeUpdate() > 0;
        }
    }

    // Xóa chỉ số
    public boolean delete(String maPhong, int thang, int nam) throws SQLException {
        String sql = "EXEC sp_XoaChiSo ?,?,?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPhong);
            ps.setInt(2, thang);
            ps.setInt(3, nam);
            return ps.executeUpdate() > 0;
        }
    }

    public ChiSo getByPhongThangNam(String maPhong, int thang, int nam) {
    String sql = "SELECT MaPhong, Thang, Nam, ChiSoDien, ChiSoNuoc FROM ChiSoDN WHERE MaPhong=? AND Thang=? AND Nam=?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, maPhong);
        ps.setInt(2, thang);
        ps.setInt(3, nam);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return new ChiSo(
                        rs.getString("MaPhong"),
                        rs.getInt("Thang"),
                        rs.getInt("Nam"),
                        rs.getInt("ChiSoDien"),
                        rs.getInt("ChiSoNuoc")
                );
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}


}
