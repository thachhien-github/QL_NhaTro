/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import dao.ChiSoDAO;
import dao.HoaDonDAO;
import dao.PhongDAO;
import dao.XeDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.HoaDon;
import util.DBConnection;
import util.RefreshablePanel;

/**
 *
 * @author ADMIN
 */
public class HoaDonPanel extends javax.swing.JPanel implements RefreshablePanel {

    private HoaDonDAO hoaDonDAO = new HoaDonDAO();
    private PhongDAO phongDAO = new PhongDAO();
    private ChiSoDAO chiSoDAO = new ChiSoDAO();
    private XeDAO xeDAO = new XeDAO();

    private DefaultTableModel tableModel;

    public HoaDonPanel() {
        initComponents();
        initTable();
        loadComboBox();  // phải load trước
        refreshData();       // mới load dữ liệu bảng
        clearForm();

        cboMaPhong.addActionListener(e -> loadChiSoAndTinhTien());
        cboThang.addActionListener(e -> loadChiSoAndTinhTien());
        cboNam.addActionListener(e -> loadChiSoAndTinhTien());

    }

    // ===== Khởi tạo bảng =====
    private void initTable() {
        tableModel = new DefaultTableModel(
                new Object[]{"Mã HĐ", "Mã phòng", "Tháng", "Năm", "Tiền phòng", "Tiền điện", "Tiền nước", "Tiền xe", "Tổng tiền"},
                0
        );
        tblHoaDon.setModel(tableModel);
    }

    // ===== Load dữ liệu JTable =====
    @Override
    public void refreshData() {
        tableModel.setRowCount(0);
        List<HoaDon> list = hoaDonDAO.getAll();
        for (HoaDon hd : list) {
            tableModel.addRow(new Object[]{
                hd.getMaHDon(),
                hd.getMaPhong(),
                hd.getThang(),
                hd.getNam(),
                String.format("%,.0f đ", hd.getTienPhong()),
                String.format("%,.0f đ", hd.getTienDien()),
                String.format("%,.0f đ", hd.getTienNuoc()),
                String.format("%,.0f đ", hd.getTienXe()),
                String.format("%,.0f đ", hd.getTongTien())
            });
        }
        clearForm();
    }

    // ===== Load dữ liệu cho Combobox =====
    private void loadComboBox() {
        // --- MaPhong ---
        cboMaPhong.removeAllItems();
        List<String> listPhong = phongDAO.getAllMaPhong();
        for (String ma : listPhong) {
            cboMaPhong.addItem(ma);
        }

        // --- Tháng (1..12) ---
        cboThang.removeAllItems();
        for (int i = 1; i <= 12; i++) {
            cboThang.addItem(String.valueOf(i));
        }

        // --- Năm (2020..2030) ---
        cboNam.removeAllItems();
        for (int y = 2020; y <= 2030; y++) {
            cboNam.addItem(String.valueOf(y));
        }
    }

    // ====== Làm mới form ======
    private void clearForm() {
        txtMaHDon.setText("");
        cboMaPhong.setSelectedIndex(-1);
        cboThang.setSelectedIndex(-1);
        cboNam.setSelectedIndex(-1);
        txtTienNuoc.setText("");
        txtTienDien.setText("");
        txtKhoiNuoc.setText("");
        txtCSdien.setText("");
        txtTienXe.setText("");
        txtTienPhong.setText("");
        txtTongTien.setText("");
    }

    private void loadChiSoAndTinhTien() {
        try {
            if (cboMaPhong.getSelectedIndex() < 0
                    || cboThang.getSelectedIndex() < 0
                    || cboNam.getSelectedIndex() < 0) {
                return;
            }

            String maPhong = cboMaPhong.getSelectedItem().toString();
            int thang = Integer.parseInt(cboThang.getSelectedItem().toString());
            int nam = Integer.parseInt(cboNam.getSelectedItem().toString());

            // Lấy chỉ số điện nước từ DAO
            model.ChiSo cs = chiSoDAO.getByPhongThangNam(maPhong, thang, nam);

            if (cs != null) {
                int csDien = cs.getChiSoDien();
                int csNuoc = cs.getChiSoNuoc();

                txtCSdien.setText(String.valueOf(csDien));
                txtKhoiNuoc.setText(String.valueOf(csNuoc));

                double tienDien = csDien * 3000;
                double tienNuoc = csNuoc * 19200;

                txtTienDien.setText(String.valueOf(tienDien));
                txtTienNuoc.setText(String.valueOf(tienNuoc));

                // --- Load tiền phòng và xe ---
                double tienPhong = phongDAO.getTienPhongTheoMa(maPhong);
                txtTienPhong.setText(String.valueOf(tienPhong));

                double tienXe = new dao.XeDAO().tinhTienXeTheoPhong(maPhong);
                txtTienXe.setText(String.valueOf(tienXe));

                // --- Tổng cộng ---
                double tong = tienPhong + tienDien + tienNuoc + tienXe;
                txtTongTien.setText(String.valueOf(tong));
            } else {
                clearForm();
                JOptionPane.showMessageDialog(this, "Chưa có chỉ số cho "
                        + maPhong + " tháng " + thang + "/" + nam);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtMaHDon = new javax.swing.JTextField();
        btnTaoHoaDon = new javax.swing.JButton();
        btnInHoaDon = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnLamMoi = new javax.swing.JButton();
        cboMaPhong = new javax.swing.JComboBox<>();
        cboThang = new javax.swing.JComboBox<>();
        cboNam = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        txtTienNuoc = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtTienDien = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtKhoiNuoc = new javax.swing.JTextField();
        txtCSdien = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txtTongTien = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtTienXe = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtTienPhong = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHoaDon = new javax.swing.JTable();

        jButton1.setText("jButton1");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 204));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("QUẢN LÝ HÓA ĐƠN");

        jLabel9.setText("Mã phòng");

        jLabel10.setText("Năm");

        jLabel11.setText("Tháng");

        jLabel12.setText("Chỉ số điện");

        txtMaHDon.setEnabled(false);

        btnTaoHoaDon.setText("Tạo hóa đơn");
        btnTaoHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaoHoaDonActionPerformed(evt);
            }
        });

        btnInHoaDon.setText("In hóa đơn");
        btnInHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInHoaDonActionPerformed(evt);
            }
        });

        btnXoa.setText("Xóa");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnLamMoi.setText("Làm mới");
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiActionPerformed(evt);
            }
        });

        cboMaPhong.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cboThang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboThang.setNextFocusableComponent(cboNam);

        cboNam.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel16.setText("Mã hóa đơn");

        jLabel17.setText("Tiền điện");

        jLabel18.setText("Khối Nước");

        jLabel19.setText("Tiền nước");

        jLabel20.setText("Tổng tiền");

        jLabel2.setText("Tiền xe");

        jLabel3.setText("Tiền phòng");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnTaoHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnInHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTongTien)
                            .addComponent(txtTienPhong)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(cboThang, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboNam, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cboMaPhong, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtMaHDon, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(83, 83, 83)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtCSdien, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                                .addComponent(txtKhoiNuoc))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtTienNuoc, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                                .addComponent(txtTienXe))
                            .addComponent(txtTienDien, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(35, 35, 35)
                                        .addComponent(txtTienDien, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtKhoiNuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtCSdien, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTienNuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cboThang, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cboNam, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtMaHDon, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cboMaPhong, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtTienPhong))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtTienXe, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTaoHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        tblHoaDon.setModel(new javax.swing.table.DefaultTableModel(
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
        tblHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHoaDonMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblHoaDon);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 781, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tblHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonMouseClicked
        clearForm();
        int row = tblHoaDon.getSelectedRow();
        if (row >= 0) {
            String maHDon = tableModel.getValueAt(row, 0).toString();
            HoaDon hd = hoaDonDAO.getById(maHDon);
            if (hd != null) {
                txtMaHDon.setText(hd.getMaHDon());

                // ===== chọn MaPhong an toàn =====
                boolean found = false;
                for (int i = 0; i < cboMaPhong.getItemCount(); i++) {
                    if (cboMaPhong.getItemAt(i).equals(hd.getMaPhong())) {
                        cboMaPhong.setSelectedIndex(i);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    cboMaPhong.setSelectedIndex(-1); // không tìm thấy
                }

                cboThang.setSelectedItem(String.valueOf(hd.getThang()));
                cboNam.setSelectedItem(String.valueOf(hd.getNam()));
                txtTienPhong.setText(String.valueOf(hd.getTienPhong()));
                txtTienDien.setText(String.valueOf(hd.getTienDien()));
                txtTienNuoc.setText(String.valueOf(hd.getTienNuoc()));
                txtTienXe.setText(String.valueOf(hd.getTienXe()));

                double tong = hd.getTienPhong() + hd.getTienDien() + hd.getTienNuoc() + hd.getTienXe();
                txtTongTien.setText(String.valueOf(tong));
            }
        }
    }//GEN-LAST:event_tblHoaDonMouseClicked

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
        clearForm();
        refreshData();
    }//GEN-LAST:event_btnLamMoiActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        int row = tblHoaDon.getSelectedRow();
        if (row >= 0) {
            String maHDon = tableModel.getValueAt(row, 0).toString();
            if (hoaDonDAO.delete(maHDon)) {
                JOptionPane.showMessageDialog(this, "Xóa hóa đơn thành công!");
                refreshData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Chọn hóa đơn cần xóa!");
        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnInHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInHoaDonActionPerformed
        try {
            if (txtMaHDon.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần in!");
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn nơi lưu hóa đơn");
            fileChooser.setSelectedFile(new java.io.File("HoaDon_" + txtMaHDon.getText() + ".pdf"));

            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection != JFileChooser.APPROVE_OPTION) {
                return;
            }

            java.io.File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().toLowerCase().endsWith(".pdf")) {
                fileToSave = new java.io.File(fileToSave.getAbsolutePath() + ".pdf");
            }

            // Format tiền
            java.text.DecimalFormat df = new java.text.DecimalFormat("#,### VNĐ");

            // Font hỗ trợ tiếng Việt
            com.itextpdf.text.pdf.BaseFont bf = com.itextpdf.text.pdf.BaseFont.createFont(
                    "c:/windows/fonts/times.ttf",
                    com.itextpdf.text.pdf.BaseFont.IDENTITY_H,
                    com.itextpdf.text.pdf.BaseFont.EMBEDDED);
            com.itextpdf.text.Font fontTitle = new com.itextpdf.text.Font(bf, 20, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font fontNormal = new com.itextpdf.text.Font(bf, 12);
            com.itextpdf.text.Font fontBold = new com.itextpdf.text.Font(bf, 12, com.itextpdf.text.Font.BOLD);

            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
            com.itextpdf.text.pdf.PdfWriter.getInstance(document, new java.io.FileOutputStream(fileToSave));
            document.open();

            // ===== tên công ty =====
            com.itextpdf.text.Paragraph tenPhongTro = new com.itextpdf.text.Paragraph("HP APARTMENT", fontBold);
            tenPhongTro.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            document.add(tenPhongTro);

            // ===== Tiêu đề =====
            com.itextpdf.text.Paragraph title = new com.itextpdf.text.Paragraph("HÓA ĐƠN PHÒNG TRỌ", fontTitle);
            title.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            title.setSpacingBefore(10);
            title.setSpacingAfter(20);
            document.add(title);

            // ===== Bảng nội dung =====
            com.itextpdf.text.pdf.PdfPTable table = new com.itextpdf.text.pdf.PdfPTable(2);
            table.setWidthPercentage(90);
            table.setSpacingBefore(10);
            table.setSpacingAfter(20);

            // Tạo cell helper
            com.itextpdf.text.pdf.PdfPCell cell;

            table.addCell(new com.itextpdf.text.Phrase("Mã hóa đơn:", fontNormal));
            table.addCell(new com.itextpdf.text.Phrase(txtMaHDon.getText(), fontNormal));

            table.addCell(new com.itextpdf.text.Phrase("Mã phòng:", fontNormal));
            table.addCell(new com.itextpdf.text.Phrase(cboMaPhong.getSelectedItem().toString(), fontNormal));

            table.addCell(new com.itextpdf.text.Phrase("Tháng/Năm:", fontNormal));
            table.addCell(new com.itextpdf.text.Phrase(cboThang.getSelectedItem() + "/" + cboNam.getSelectedItem(), fontNormal));

            table.addCell(new com.itextpdf.text.Phrase("Tiền phòng:", fontNormal));
            table.addCell(new com.itextpdf.text.Phrase(df.format(Double.parseDouble(txtTienPhong.getText())), fontNormal));

            table.addCell(new com.itextpdf.text.Phrase("Tiền điện:", fontNormal));
            table.addCell(new com.itextpdf.text.Phrase(df.format(Double.parseDouble(txtTienDien.getText())), fontNormal));

            table.addCell(new com.itextpdf.text.Phrase("Tiền nước:", fontNormal));
            table.addCell(new com.itextpdf.text.Phrase(df.format(Double.parseDouble(txtTienNuoc.getText())), fontNormal));

            table.addCell(new com.itextpdf.text.Phrase("Tiền xe:", fontNormal));
            table.addCell(new com.itextpdf.text.Phrase(df.format(Double.parseDouble(txtTienXe.getText())), fontNormal));

            // Tổng tiền nổi bật
            cell = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase("TỔNG TIỀN:", fontBold));
            cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
            cell.setBackgroundColor(com.itextpdf.text.BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(df.format(Double.parseDouble(txtTongTien.getText())), fontBold));
            cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
            cell.setBackgroundColor(com.itextpdf.text.BaseColor.YELLOW);
            table.addCell(cell);

            document.add(table);

            // ===== Chữ ký =====
            com.itextpdf.text.Paragraph camOn = new com.itextpdf.text.Paragraph("Cảm ơn bạn đã sử dụng dịch vụ!", fontNormal);
            camOn.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            camOn.setSpacingBefore(30);
            document.add(camOn);

            com.itextpdf.text.Paragraph kyTen = new com.itextpdf.text.Paragraph("Người lập hóa đơn", fontNormal);
            kyTen.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
            kyTen.setSpacingBefore(50);
            document.add(kyTen);

            document.close();

            JOptionPane.showMessageDialog(this, "Xuất hóa đơn thành công!\nFile: " + fileToSave.getAbsolutePath());
            if (java.awt.Desktop.isDesktopSupported()) {
                java.awt.Desktop.getDesktop().open(fileToSave);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi in hóa đơn: " + e.getMessage());
        }
    }//GEN-LAST:event_btnInHoaDonActionPerformed

    private void btnTaoHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaoHoaDonActionPerformed
        try {
            String maPhong = (String) cboMaPhong.getSelectedItem();
            int thang = Integer.parseInt(cboThang.getSelectedItem().toString());
            int nam = Integer.parseInt(cboNam.getSelectedItem().toString());

            double tienPhong = Double.parseDouble(txtTienPhong.getText());
            double tienDien = Double.parseDouble(txtTienDien.getText());
            double tienNuoc = Double.parseDouble(txtTienNuoc.getText());
            double tienXe = Double.parseDouble(txtTienXe.getText());

            HoaDon hd = new HoaDon(null, maPhong, thang, nam,
                    tienPhong, tienDien, tienNuoc, tienXe, 0);

            if (hoaDonDAO.insert(hd)) {
                JOptionPane.showMessageDialog(this, "Tạo hóa đơn thành công!");
                refreshData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Tạo hóa đơn thất bại!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }//GEN-LAST:event_btnTaoHoaDonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnInHoaDon;
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnTaoHoaDon;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cboMaPhong;
    private javax.swing.JComboBox<String> cboNam;
    private javax.swing.JComboBox<String> cboThang;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblHoaDon;
    private javax.swing.JTextField txtCSdien;
    private javax.swing.JTextField txtKhoiNuoc;
    private javax.swing.JTextField txtMaHDon;
    private javax.swing.JTextField txtTienDien;
    private javax.swing.JTextField txtTienNuoc;
    private javax.swing.JTextField txtTienPhong;
    private javax.swing.JTextField txtTienXe;
    private javax.swing.JTextField txtTongTien;
    // End of variables declaration//GEN-END:variables

}
