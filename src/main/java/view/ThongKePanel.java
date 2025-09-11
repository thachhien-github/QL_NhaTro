package view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import util.DBConnection;
import util.RefreshablePanel;

public class ThongKePanel extends javax.swing.JPanel implements RefreshablePanel {

    private Connection conn;
    private DefaultTableModel tableModel;
    private DefaultTableModel tongCongModel;

    public ThongKePanel() {
        initComponents();

        conn = DBConnection.getConnection();
        initTable();
        loadComboBox();

        chkTheoNam.setSelected(true); // mặc định thống kê theo năm
        cboThang.setEnabled(false);

        // tự động load dữ liệu năm mới nhất
        if (cboNam.getItemCount() > 0) {
            cboNam.setSelectedIndex(cboNam.getItemCount() - 1);
            int nam = Integer.parseInt(cboNam.getSelectedItem().toString());
            loadData(0, nam, true);
        }

        // Listener tự động cập nhật khi thay đổi combo hoặc checkbox
        cboNam.addActionListener(e -> autoLoadData());
        cboThang.addActionListener(e -> autoLoadData());
        chkTheoNam.addActionListener(e -> {
            cboThang.setEnabled(!chkTheoNam.isSelected());
            autoLoadData();
        });
    }

    // ======== KHỞI TẠO TABLE ========
    private void initTable() {
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{
            "Mã HĐ", "Tên Phòng", "Tiền Phòng", "Tiền Điện", "Tiền Nước", "Tiền Xe", "Tổng Tiền"
        });
        tblThongKe.setModel(tableModel);

        // bảng tổng cộng
        tongCongModel = new DefaultTableModel();
        tongCongModel.setColumnIdentifiers(new String[]{
            "Khoản mục", "Số tiền", "Tỷ lệ (%)"
        });
        tblTongCong.setModel(tongCongModel);
    }

    private void loadComboBox() {
        cboThang.removeAllItems();
        for (int i = 1; i <= 12; i++) {
            cboThang.addItem(String.valueOf(i));
        }

        cboNam.removeAllItems();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT DISTINCT Nam FROM HoaDon ORDER BY Nam ASC");
            while (rs.next()) {
                cboNam.addItem(String.valueOf(rs.getInt("Nam")));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi load năm: " + e.getMessage());
        }
    }

    private void loadData(int thang, int nam, boolean theoNam) {
        tableModel.setRowCount(0); // xóa dữ liệu cũ
        double dtPhong = 0, dtDien = 0, dtNuoc = 0, dtXe = 0, tongDT = 0;

        try {
            String sql;
            if (theoNam) {
                sql = "SELECT MaHDon, TenPhong, TienPhong, TienDien, TienNuoc, TienXe, TongTien "
                        + "FROM v_HoaDonChiTiet WHERE Nam = ?";
            } else {
                sql = "SELECT MaHDon, TenPhong, TienPhong, TienDien, TienNuoc, TienXe, TongTien "
                        + "FROM v_HoaDonChiTiet WHERE Thang = ? AND Nam = ?";
            }

            PreparedStatement pst = conn.prepareStatement(sql);
            if (theoNam) {
                pst.setInt(1, nam);
            } else {
                pst.setInt(1, thang);
                pst.setInt(2, nam);
            }

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("MaHDon"));
                row.add(rs.getString("TenPhong"));
                row.add(String.format("%,.0f đ", rs.getDouble("TienPhong")));
                row.add(String.format("%,.0f đ", rs.getDouble("TienDien")));
                row.add(String.format("%,.0f đ", rs.getDouble("TienNuoc")));
                row.add(String.format("%,.0f đ", rs.getDouble("TienXe")));
                row.add(String.format("%,.0f đ", rs.getDouble("TongTien")));
                tableModel.addRow(row);

                dtPhong += rs.getDouble("TienPhong");
                dtDien += rs.getDouble("TienDien");
                dtNuoc += rs.getDouble("TienNuoc");
                dtXe += rs.getDouble("TienXe");
                tongDT += rs.getDouble("TongTien");
            }

            // Cập nhật label
            lblDTphong.setText(String.format("%,.0f đ", dtPhong));
            lblDTdien.setText(String.format("%,.0f đ", dtDien));
            lblDTnuoc.setText(String.format("%,.0f đ", dtNuoc));
            lblDTxe.setText(String.format("%,.0f đ", dtXe));
            lblTongDT.setText(String.format("%,.0f đ", tongDT));

            if (tongDT > 0) {
                lblPTphong.setText(String.format("Chiếm %.2f%% tổng doanh thu", dtPhong / tongDT * 100));
                lblPTdien.setText(String.format("Chiếm %.2f%% tổng doanh thu", dtDien / tongDT * 100));
                lblPTnuoc.setText(String.format("Chiếm %.2f%% tổng doanh thu", dtNuoc / tongDT * 100));
                lblPTxe.setText(String.format("Chiếm %.2f%% tổng doanh thu", dtXe / tongDT * 100));
            } else {
                lblPTphong.setText("Chiếm 0% tổng doanh thu");
                lblPTdien.setText("Chiếm 0% tổng doanh thu");
                lblPTnuoc.setText("Chiếm 0% tổng doanh thu");
                lblPTxe.setText("Chiếm 0% tổng doanh thu");
            }

            // ==== Cập nhật bảng tổng cộng ====
            tongCongModel.setRowCount(0);
            if (tongDT > 0) {
                tongCongModel.addRow(new Object[]{"Tiền phòng", String.format("%,.0f đ", dtPhong), String.format("%.2f %%", dtPhong / tongDT * 100)});
                tongCongModel.addRow(new Object[]{"Tiền điện", String.format("%,.0f đ", dtDien), String.format("%.2f %%", dtDien / tongDT * 100)});
                tongCongModel.addRow(new Object[]{"Tiền nước", String.format("%,.0f đ", dtNuoc), String.format("%.2f %%", dtNuoc / tongDT * 100)});
                tongCongModel.addRow(new Object[]{"Tiền xe", String.format("%,.0f đ", dtXe), String.format("%.2f %%", dtXe / tongDT * 100)});
                tongCongModel.addRow(new Object[]{"TỔNG CỘNG", String.format("%,.0f đ", tongDT), "100 %"});
            } else {
                tongCongModel.addRow(new Object[]{"Tiền phòng", "0 đ", "0 %"});
                tongCongModel.addRow(new Object[]{"Tiền điện", "0 đ", "0 %"});
                tongCongModel.addRow(new Object[]{"Tiền nước", "0 đ", "0 %"});
                tongCongModel.addRow(new Object[]{"Tiền xe", "0 đ", "0 %"});
                tongCongModel.addRow(new Object[]{"TỔNG CỘNG", "0 đ", "0 %"});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi load dữ liệu: " + e.getMessage());
        }
    }

    private void autoLoadData() {
        try {
            if (cboNam.getSelectedItem() == null) {
                return;
            }
            int nam = Integer.parseInt(cboNam.getSelectedItem().toString());
            boolean theoNam = chkTheoNam.isSelected();
            int thang = 0;
            if (!theoNam && cboThang.getSelectedItem() != null) {
                thang = Integer.parseInt(cboThang.getSelectedItem().toString());
            }
            loadData(thang, nam, theoNam);
        } catch (Exception e) {
            // bỏ qua nếu chưa chọn đủ combo
        }
    }

    @Override
    public void refreshData() {
        autoLoadData();  // hoặc gọi loadData(0, năm, true) nếu muốn refresh mặc định
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        lblDTdien = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblPTdien = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        lblDTnuoc = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblPTnuoc = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        lblDTxe = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        lblPTxe = new javax.swing.JLabel();
        btnThongKe = new javax.swing.JButton();
        cboNam = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        cboThang = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        chkTheoNam = new javax.swing.JCheckBox();
        jPanel7 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        lblDTphong = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        lblPTphong = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        lblTongDT = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblThongKe = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblTongCong = new javax.swing.JTable();

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 204));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("QUẢN LÝ THỐNG KÊ");

        jPanel4.setBackground(new java.awt.Color(153, 153, 255));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/bulb_light_bulb_idea_icon_196981.png"))); // NOI18N

        lblDTdien.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblDTdien.setForeground(new java.awt.Color(255, 255, 255));
        lblDTdien.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDTdien.setText("999999 đ");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText("Doanh Thu Điện");

        lblPTdien.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblPTdien.setForeground(new java.awt.Color(255, 255, 255));
        lblPTdien.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPTdien.setText("Chiếm  % tổng doanh thu");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDTdien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE))
                    .addComponent(lblPTdien, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDTdien)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblPTdien, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(102, 204, 255));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/pipe_water_5925.png"))); // NOI18N

        lblDTnuoc.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblDTnuoc.setForeground(new java.awt.Color(255, 255, 255));
        lblDTnuoc.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDTnuoc.setText("999999 đ");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText("Doanh Thu Nước");

        lblPTnuoc.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblPTnuoc.setForeground(new java.awt.Color(255, 255, 255));
        lblPTnuoc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPTnuoc.setText("Chiếm  % tổng doanh thu");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE))
                    .addComponent(lblPTnuoc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblDTnuoc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDTnuoc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblPTnuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(255, 153, 0));

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/1452632162_inspiration-34_icon-icons.com_51105.png"))); // NOI18N

        lblDTxe.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblDTxe.setForeground(new java.awt.Color(255, 255, 255));
        lblDTxe.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDTxe.setText("999999 đ");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel16.setText("Phí Giữ Xe");

        lblPTxe.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblPTxe.setForeground(new java.awt.Color(255, 255, 255));
        lblPTxe.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPTxe.setText("Chiếm  % tổng doanh thu");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lblPTxe, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                    .addComponent(lblDTxe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDTxe)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblPTxe, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        btnThongKe.setBackground(new java.awt.Color(255, 153, 51));
        btnThongKe.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnThongKe.setForeground(new java.awt.Color(255, 255, 255));
        btnThongKe.setText("Thống kê");
        btnThongKe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThongKeActionPerformed(evt);
            }
        });

        cboNam.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel10.setText("năm");

        cboThang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel11.setText("Tháng");

        chkTheoNam.setText("Theo năm");

        jPanel7.setBackground(new java.awt.Color(0, 153, 153));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/1490886282-18-school-building_82486.png"))); // NOI18N

        lblDTphong.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblDTphong.setForeground(new java.awt.Color(255, 255, 255));
        lblDTphong.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDTphong.setText("999999 đ");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel13.setText("Phòng");

        lblPTphong.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblPTphong.setForeground(new java.awt.Color(255, 255, 255));
        lblPTphong.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPTphong.setText("Chiếm  % tổng doanh thu");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lblPTphong, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblDTphong, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDTphong)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblPTphong, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cboThang, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cboNam, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(chkTheoNam, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnThongKe, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cboThang, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(chkTheoNam, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cboNam))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(153, 204, 255));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/1486564172-finance-loan-money_81492.png"))); // NOI18N

        lblTongDT.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTongDT.setForeground(new java.awt.Color(255, 255, 255));
        lblTongDT.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTongDT.setText("999999 đ");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setText("Tổng Doanh Thu");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("100% tổng doanh thu");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTongDT, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTongDT)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tblThongKe.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblThongKe);

        tblTongCong.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tblTongCong);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnThongKeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThongKeActionPerformed
        autoLoadData();
    }//GEN-LAST:event_btnThongKeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnThongKe;
    private javax.swing.JComboBox<String> cboNam;
    private javax.swing.JComboBox<String> cboThang;
    private javax.swing.JCheckBox chkTheoNam;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblDTdien;
    private javax.swing.JLabel lblDTnuoc;
    private javax.swing.JLabel lblDTphong;
    private javax.swing.JLabel lblDTxe;
    private javax.swing.JLabel lblPTdien;
    private javax.swing.JLabel lblPTnuoc;
    private javax.swing.JLabel lblPTphong;
    private javax.swing.JLabel lblPTxe;
    private javax.swing.JLabel lblTongDT;
    private javax.swing.JTable tblThongKe;
    private javax.swing.JTable tblTongCong;
    // End of variables declaration//GEN-END:variables

}
