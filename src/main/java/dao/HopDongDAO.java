package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.HopDong;
import util.DBConnection;

public class HopDongDAO {
    
    private Connection conn;

    public HopDongDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean insert(HopDong hd) {
        String sql = "INSERT INTO HopDong(MaHD, MaKT, MaPhong, NgayBatDau, NgayKetThuc) "
                   + "VALUES (?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, hd.getMaHD());
            ps.setString(2, hd.getMaKT());
            ps.setString(3, hd.getMaPhong());
            ps.setDate(4, hd.getNgayBatDau());
            if (hd.getNgayKetThuc() != null) ps.setDate(5, hd.getNgayKetThuc());
            else ps.setNull(5, Types.DATE);

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            // Vi phạm UQ/PK hay FK sẽ nhảy vào đây
            ex.printStackTrace();
            return false;
        }
    }

    public boolean update(HopDong hd) {
        String sql = "UPDATE HopDong SET MaKT=?, MaPhong=?, NgayBatDau=?, NgayKetThuc=? "
                   + "WHERE MaHD=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, hd.getMaKT());
            ps.setString(2, hd.getMaPhong());
            ps.setDate(3, hd.getNgayBatDau());
            if (hd.getNgayKetThuc() != null) ps.setDate(4, hd.getNgayKetThuc());
            else ps.setNull(4, Types.DATE);
            ps.setString(5, hd.getMaHD());

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maHD) {
        String sql = "DELETE FROM HopDong WHERE MaHD=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, maHD);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public HopDong findById(String maHD) {
        String sql = "SELECT * FROM HopDong WHERE MaHD=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<HopDong> getAll() {
        List<HopDong> list = new ArrayList<>();
        String sql = "SELECT * FROM HopDong ORDER BY MaHD";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    private HopDong map(ResultSet rs) throws SQLException {
        return new HopDong(
            rs.getString("MaHD"),
            rs.getString("MaKT"),
            rs.getString("MaPhong"),
            rs.getDate("NgayBatDau"),
            rs.getDate("NgayKetThuc")
        );
    }
}
