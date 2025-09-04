/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;

/**
 *
 * @author ThachHien
 */
public class KhachThue {

    private String maKT;
    private String hoTen;
    private String gioiTinh;
    private Date ngaySinh;
    private String cccd;
    private String sdt;
    private String diaChi;
    private String maPhong;

    public KhachThue(String maKT, String hoTen, String gioiTinh, Date ngaySinh, String cccd, String sdt, String diaChi, String maPhong) {
        this.maKT = maKT;
        this.hoTen = hoTen;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.cccd = cccd;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.maPhong = maPhong;
    }

    public String getMaKT() {
        return maKT;
    }

    public void setMaKT(String maKT) {
        this.maKT = maKT;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    
    @Override
    public String toString() {
        return "KhachThue{"
                + "maKT='" + maKT + '\''
                + ", hoTen='" + hoTen + '\''
                + ", gioiTinh='" + gioiTinh + '\''
                + ", ngaySinh=" + ngaySinh
                + ", sdt='" + sdt + '\''
                + ", diaChi='" + diaChi + '\''
                + ", maPhong='" + maPhong + '\''
                + '}';
    }

}
