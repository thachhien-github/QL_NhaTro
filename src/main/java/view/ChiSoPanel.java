/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import dao.ChiSoDAO;
import dao.PhongDAO;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.ChiSo;
import util.DBConnection;
import util.RefreshablePanel;

/**
 *
 * @author ThachHien
 */
public class ChiSoPanel extends javax.swing.JPanel implements RefreshablePanel {

    private ChiSoDAO chiSoDAO;
    private PhongDAO phongDAO;

    public ChiSoPanel() {
        initComponents();

        Connection conn = DBConnection.getConnection();
        chiSoDAO = new ChiSoDAO(conn, null);
        phongDAO = new PhongDAO(conn);

        loadComboBoxPhong();
        loadComboBoxThangNam();
        refreshData();

        // Tự động tính chỉ số khi nhập
        txtDienMoi.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                tinhChiSoDien();
            }
        });
        txtNuocMoi.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                tinhChiSoNuoc();
            }
        });
    }

    private void loadComboBoxPhong() {
        cboMaPhong.removeAllItems();
        try {
            phongDAO.getAll().forEach(p -> cboMaPhong.addItem(p.getMaPhong()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi load phòng: " + e.getMessage());
        }
    }

    private void loadComboBoxThangNam() {
        cboThang.removeAllItems();
        for (int i = 1; i <= 12; i++) {
            cboThang.addItem(String.valueOf(i));
        }

        cboNam.removeAllItems();
        for (int y = 2020; y <= 2030; y++) {
            cboNam.addItem(String.valueOf(y));
        }
    }

    @Override
    public void refreshData() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{
            "Mã phòng", "Tháng", "Năm",
            "Điện cũ", "Điện mới", "Điện tiêu thụ",
            "Nước cũ", "Nước mới", "Nước tiêu thụ"
        });

        try {
            List<ChiSo> list = chiSoDAO.getAll();
            for (ChiSo cs : list) {
                model.addRow(new Object[]{
                    cs.getMaPhong(),
                    cs.getThang(),
                    cs.getNam(),
                    cs.getDienCu(),
                    cs.getDienMoi(),
                    cs.getChiSoDien(),
                    cs.getNuocCu(),
                    cs.getNuocMoi(),
                    cs.getChiSoNuoc()
                });
            }
            tblChiSo.setModel(model);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi load dữ liệu: " + e.getMessage());
        }
    }

    private void tinhChiSoDien() {
        try {
            int cu = Integer.parseInt(txtDienCu.getText());
            int moi = Integer.parseInt(txtDienMoi.getText());
            txtCSdien.setText(String.valueOf(moi - cu));
        } catch (Exception ignored) {
            txtCSdien.setText("0");
        }
    }

    private void tinhChiSoNuoc() {
        try {
            int cu = Integer.parseInt(txtNuocCu.getText());
            int moi = Integer.parseInt(txtNuocMoi.getText());
            txtCSnuoc.setText(String.valueOf(moi - cu));
        } catch (Exception ignored) {
            txtCSnuoc.setText("0");
        }
    }

    private void resetForm() {
        cboMaPhong.setSelectedIndex(0);
        cboThang.setSelectedIndex(0);
        cboNam.setSelectedIndex(0);
        txtDienCu.setText("");
        txtDienMoi.setText("");
        txtNuocCu.setText("");
        txtNuocMoi.setText("");
        txtCSdien.setText("");
        txtCSnuoc.setText("");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnLamMoi = new javax.swing.JButton();
        cboMaPhong = new javax.swing.JComboBox<>();
        cboThang = new javax.swing.JComboBox<>();
        cboNam = new javax.swing.JComboBox<>();
        txtDienMoi = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtDienCu = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtNuocCu = new javax.swing.JTextField();
        txtNuocMoi = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtCSdien = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtCSnuoc = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblChiSo = new javax.swing.JTable();

        jLabel9.setText("Mã phòng");

        jLabel10.setText("Năm");

        jLabel11.setText("Tháng");

        btnThem.setText("Thêm");
        btnThem.setNextFocusableComponent(btnSua);
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnSua.setText("Sửa ");
        btnSua.setNextFocusableComponent(btnXoa);
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnXoa.setText("Xóa");
        btnXoa.setNextFocusableComponent(btnLamMoi);
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnLamMoi.setText("Làm mới");
        btnLamMoi.setNextFocusableComponent(cboThang);
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiActionPerformed(evt);
            }
        });

        cboMaPhong.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboMaPhong.setNextFocusableComponent(txtDienCu);

        cboThang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboThang.setNextFocusableComponent(cboNam);

        cboNam.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboNam.setNextFocusableComponent(cboMaPhong);

        txtDienMoi.setNextFocusableComponent(txtNuocCu);

        jLabel17.setText("Chỉ số điện mới ");

        txtDienCu.setNextFocusableComponent(txtDienMoi);

        jLabel19.setText("Chỉ số nước mới ");

        txtNuocCu.setNextFocusableComponent(txtNuocMoi);

        txtNuocMoi.setNextFocusableComponent(btnThem);

        jLabel21.setText("Chỉ số điện cũ");

        jLabel22.setText("Chỉ số nước cũ ");

        txtCSdien.setEnabled(false);

        jLabel2.setText("Điện tiêu thụ");

        jLabel3.setText("Nước tiêu thụ");

        txtCSnuoc.setEnabled(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(cboThang, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cboNam, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cboMaPhong, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtDienCu)
                            .addComponent(txtCSdien)
                            .addComponent(txtDienMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtNuocMoi)
                            .addComponent(txtCSnuoc)
                            .addComponent(txtNuocCu, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboThang, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cboNam, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtNuocCu, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtDienCu, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(7, 7, 7)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cboMaPhong, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtDienMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtNuocMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtCSdien, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtCSnuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 204));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("QUẢN LÝ CHỈ SỐ");

        tblChiSo.setModel(new javax.swing.table.DefaultTableModel(
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
        tblChiSo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblChiSoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblChiSo);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tblChiSoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblChiSoMouseClicked
        int row = tblChiSo.getSelectedRow();
        if (row >= 0) {
            cboMaPhong.setSelectedItem(tblChiSo.getValueAt(row, 0).toString());
            cboThang.setSelectedItem(tblChiSo.getValueAt(row, 1).toString());
            cboNam.setSelectedItem(tblChiSo.getValueAt(row, 2).toString());
            txtDienCu.setText(tblChiSo.getValueAt(row, 3).toString());
            txtDienMoi.setText(tblChiSo.getValueAt(row, 4).toString());
            txtCSdien.setText(tblChiSo.getValueAt(row, 5).toString());
            txtNuocCu.setText(tblChiSo.getValueAt(row, 6).toString());
            txtNuocMoi.setText(tblChiSo.getValueAt(row, 7).toString());
            txtCSnuoc.setText(tblChiSo.getValueAt(row, 8).toString());
        }
    }//GEN-LAST:event_tblChiSoMouseClicked

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
        resetForm();
        refreshData();
    }//GEN-LAST:event_btnLamMoiActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        int row = tblChiSo.getSelectedRow();
        if (row >= 0) {
            String maPhong = tblChiSo.getValueAt(row, 0).toString();
            int thang = Integer.parseInt(tblChiSo.getValueAt(row, 1).toString());
            int nam = Integer.parseInt(tblChiSo.getValueAt(row, 2).toString());

            int confirm = JOptionPane.showConfirmDialog(this, "Xóa chỉ số này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    String sql = "DELETE FROM ChiSoDienNuoc WHERE MaPhong=? AND Thang=? AND Nam=?";
                    var ps = DBConnection.getConnection().prepareStatement(sql);
                    ps.setString(1, maPhong);
                    ps.setInt(2, thang);
                    ps.setInt(3, nam);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                    refreshData();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Lỗi xóa: " + e.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Chọn dòng cần xóa!");
        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        try {
            ChiSo cs = new ChiSo(
                    cboMaPhong.getSelectedItem().toString(),
                    Integer.parseInt(cboThang.getSelectedItem().toString()),
                    Integer.parseInt(cboNam.getSelectedItem().toString()),
                    Integer.parseInt(txtDienCu.getText()),
                    Integer.parseInt(txtDienMoi.getText()),
                    Integer.parseInt(txtNuocCu.getText()),
                    Integer.parseInt(txtNuocMoi.getText()),
                    Integer.parseInt(txtCSdien.getText()),
                    Integer.parseInt(txtCSnuoc.getText())
            );

            String sql = "UPDATE ChiSoDienNuoc SET DienCu=?, DienMoi=?, NuocCu=?, NuocMoi=? WHERE MaPhong=? AND Thang=? AND Nam=?";
            var ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, cs.getDienCu());
            ps.setInt(2, cs.getDienMoi());
            ps.setInt(3, cs.getNuocCu());
            ps.setInt(4, cs.getNuocMoi());
            ps.setString(5, cs.getMaPhong());
            ps.setInt(6, cs.getThang());
            ps.setInt(7, cs.getNam());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            refreshData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi sửa: " + e.getMessage());
        }
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        try {
            ChiSo cs = new ChiSo(
                    cboMaPhong.getSelectedItem().toString(),
                    Integer.parseInt(cboThang.getSelectedItem().toString()),
                    Integer.parseInt(cboNam.getSelectedItem().toString()),
                    Integer.parseInt(txtDienCu.getText()),
                    Integer.parseInt(txtDienMoi.getText()),
                    Integer.parseInt(txtNuocCu.getText()),
                    Integer.parseInt(txtNuocMoi.getText()),
                    Integer.parseInt(txtCSdien.getText()),
                    Integer.parseInt(txtCSnuoc.getText())
            );

            if (chiSoDAO.insert(cs)) {
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể thêm!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm: " + e.getMessage());
        }
    }//GEN-LAST:event_btnThemActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cboMaPhong;
    private javax.swing.JComboBox<String> cboNam;
    private javax.swing.JComboBox<String> cboThang;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblChiSo;
    private javax.swing.JTextField txtCSdien;
    private javax.swing.JTextField txtCSnuoc;
    private javax.swing.JTextField txtDienCu;
    private javax.swing.JTextField txtDienMoi;
    private javax.swing.JTextField txtNuocCu;
    private javax.swing.JTextField txtNuocMoi;
    // End of variables declaration//GEN-END:variables
}
