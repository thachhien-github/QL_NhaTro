package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.HopDong;
import util.DBConnection;

public class HopDongDAO {

    private Connection conn;
    private PhongDAO phongDAO;

    public HopDongDAO(Connection conn) {
        this.conn = conn;
        this.phongDAO = new PhongDAO(conn);
    }

    // Lấy toàn bộ hợp đồng
    public List<HopDong> getAll() {
        List<HopDong> list = new ArrayList<>();
        String sql = "SELECT * FROM HopDong";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new HopDong(
                        rs.getString("MaHD"),
                        rs.getString("MaKT"),
                        rs.getString("MaPhong"),
                        rs.getDate("NgayBatDau"),
                        rs.getDate("NgayKetThuc")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm hợp đồng mới + cập nhật trạng thái phòng
    public boolean insert(HopDong hd) {
        String sql = "INSERT INTO HopDong(MaHD,MaKT,MaPhong,NgayBatDau,NgayKetThuc) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hd.getMaHD());
            ps.setString(2, hd.getMaKT());
            ps.setString(3, hd.getMaPhong());
            ps.setDate(4, hd.getNgayBatDau());
            ps.setDate(5, hd.getNgayKetThuc());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                phongDAO.updateTrangThai(hd.getMaPhong(), "Đã thuê");
            }
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa hợp đồng + cập nhật trạng thái phòng
    public boolean delete(String maHD, String maPhong) {
        String sql = "DELETE FROM HopDong WHERE MaHD=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                phongDAO.updateTrangThai(maPhong, "Trống");
            }
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật hợp đồng
    public boolean update(HopDong hd) {
        try {
            HopDong oldHD = getById(hd.getMaHD());
            if (oldHD == null) {
                return false;
            }

            // Nếu đổi phòng
            if (!oldHD.getMaPhong().equals(hd.getMaPhong())) {
                phongDAO.updateTrangThai(oldHD.getMaPhong(), "Trống");
                phongDAO.updateTrangThai(hd.getMaPhong(), "Đã thuê");
            }

            // Nếu có ngày kết thúc → phòng trống
            if (hd.getNgayKetThuc() != null) {
                phongDAO.updateTrangThai(hd.getMaPhong(), "Trống");
            } else {
                phongDAO.updateTrangThai(hd.getMaPhong(), "Đã thuê");
            }

            String sql = "UPDATE HopDong SET MaKT=?, MaPhong=?, NgayBatDau=?, NgayKetThuc=? WHERE MaHD=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, hd.getMaKT());
            ps.setString(2, hd.getMaPhong());
            ps.setDate(3, hd.getNgayBatDau());
            if (hd.getNgayKetThuc() != null) {
                ps.setDate(4, hd.getNgayKetThuc());
            } else {
                ps.setNull(4, java.sql.Types.DATE);
            }
            ps.setString(5, hd.getMaHD());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy hợp đồng theo mã
    public HopDong getById(String maHD) {
        String sql = "SELECT * FROM HopDong WHERE MaHD=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new HopDong(
                            rs.getString("MaHD"),
                            rs.getString("MaKT"),
                            rs.getString("MaPhong"),
                            rs.getDate("NgayBatDau"),
                            rs.getDate("NgayKetThuc")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy hợp đồng theo khách thuê
    public HopDong getByKhach(String maKT) {
        String sql = "SELECT * FROM HopDong WHERE MaKT=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKT);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new HopDong(
                            rs.getString("MaHD"),
                            rs.getString("MaKT"),
                            rs.getString("MaPhong"),
                            rs.getDate("NgayBatDau"),
                            rs.getDate("NgayKetThuc")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy hợp đồng theo phòng
    public HopDong getByPhong(String maPhong) {
        String sql = "SELECT * FROM HopDong WHERE MaPhong=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPhong);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new HopDong(
                            rs.getString("MaHD"),
                            rs.getString("MaKT"),
                            rs.getString("MaPhong"),
                            rs.getDate("NgayBatDau"),
                            rs.getDate("NgayKetThuc")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
