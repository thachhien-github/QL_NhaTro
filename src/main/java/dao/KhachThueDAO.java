/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.CallableStatement;
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

    // Lấy tất cả khách thuê
    public List<KhachThue> getAll() {
        List<KhachThue> list = new ArrayList<>();
        String sql = "SELECT * FROM KhachThue";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new KhachThue(
                        rs.getString("MaKT"),
                        rs.getString("HoTen"),
                        rs.getString("GioiTinh"),
                        rs.getDate("NgaySinh"),
                        rs.getString("CCCD"),
                        rs.getString("SDT"),
                        rs.getString("DiaChi"),
                        rs.getString("MaPhong")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm khách thuê
    public boolean insert(KhachThue kt) {
        String sql = "INSERT INTO KhachThue(MaKT,HoTen,GioiTinh,NgaySinh,CCCD,SDT,DiaChi,MaPhong) "
                + "VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kt.getMaKT());
            ps.setString(2, kt.getHoTen());
            ps.setString(3, kt.getGioiTinh());
            ps.setDate(4, kt.getNgaySinh());
            ps.setString(5, kt.getCccd());
            ps.setString(6, kt.getSdt());
            ps.setString(7, kt.getDiaChi());
            ps.setString(8, kt.getMaPhong());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật khách thuê
    public boolean update(KhachThue kt) {
        String sql = "UPDATE KhachThue SET HoTen=?, GioiTinh=?, NgaySinh=?, CCCD=?, SDT=?, DiaChi=?, MaPhong=? "
                + "WHERE MaKT=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kt.getHoTen());
            ps.setString(2, kt.getGioiTinh());
            ps.setDate(3, kt.getNgaySinh());
            ps.setString(4, kt.getCccd());
            ps.setString(5, kt.getSdt());
            ps.setString(6, kt.getDiaChi());
            ps.setString(7, kt.getMaPhong());
            ps.setString(8, kt.getMaKT());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa khách thuê
    public boolean delete(String maKT) {
        String sql = "{call sp_XoaKhach(?)}";
        try (CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, maKT);
            cs.execute(); // sp có nhiều DELETE -> executeUpdate() thường trả -1
            return true;  // nếu chạy tới đây coi như thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Tìm kiếm theo tên
    public List<KhachThue> searchByName(String keyword) {
        List<KhachThue> list = new ArrayList<>();
        String sql = "SELECT * FROM KhachThue WHERE HoTen LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new KhachThue(
                        rs.getString("MaKT"),
                        rs.getString("HoTen"),
                        rs.getString("GioiTinh"),
                        rs.getDate("NgaySinh"),
                        rs.getString("CCCD"),
                        rs.getString("SDT"),
                        rs.getString("DiaChi"),
                        rs.getString("MaPhong")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
