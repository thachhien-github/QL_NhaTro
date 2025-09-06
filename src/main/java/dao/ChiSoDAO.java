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
import model.ChiSo;

/**
 *
 * @author ThachHien
 */
public class ChiSoDAO {
    private Connection conn;

    public ChiSoDAO(Connection conn) {
        this.conn = conn;
    }

    // Lấy tất cả chỉ số
    public List<ChiSo> getAll() {
        List<ChiSo> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiSoDN";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new ChiSo(
                        rs.getString("MaCS"),
                        rs.getString("MaPhong"),
                        rs.getInt("Thang"),
                        rs.getInt("Nam"),
                        rs.getInt("ChiSoDien"),
                        rs.getInt("ChiSoNuoc")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm
    public boolean insert(ChiSo cs) {
        String sql = "INSERT INTO ChiSoDN(MaCS,MaPhong,Thang,Nam,ChiSoDien,ChiSoNuoc) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cs.getMaCS());
            ps.setString(2, cs.getMaPhong());
            ps.setInt(3, cs.getThang());
            ps.setInt(4, cs.getNam());
            ps.setInt(5, cs.getChiSoDien());
            ps.setInt(6, cs.getChiSoNuoc());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật
    public boolean update(ChiSo cs) {
        String sql = "UPDATE ChiSoDN SET MaPhong=?, Thang=?, Nam=?, ChiSoDien=?, ChiSoNuoc=? WHERE MaCS=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cs.getMaPhong());
            ps.setInt(2, cs.getThang());
            ps.setInt(3, cs.getNam());
            ps.setInt(4, cs.getChiSoDien());
            ps.setInt(5, cs.getChiSoNuoc());
            ps.setString(6, cs.getMaCS());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa
    public boolean delete(String maCS) {
        String sql = "DELETE FROM ChiSoDN WHERE MaCS=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maCS);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
