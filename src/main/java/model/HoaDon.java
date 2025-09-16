/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;

/**
 *
 * @author ThachHien
 */
public class HoaDon {
    private String maHDon;
    private String maPhong;
    private int thang;
    private int nam;
    private double tienPhong;
    private double tienDien;
    private double tienNuoc;
    private double tienXe;
    private double tongTien;

    public HoaDon(String maHDon, String maPhong, int thang, int nam, double tienPhong, double tienDien, double tienNuoc, double tienXe, double tongTien) {
        this.maHDon = maHDon;
        this.maPhong = maPhong;
        this.thang = thang;
        this.nam = nam;
        this.tienPhong = tienPhong;
        this.tienDien = tienDien;
        this.tienNuoc = tienNuoc;
        this.tienXe = tienXe;
        this.tongTien = tongTien;
    }

    public String getMaHDon() {
        return maHDon;
    }

    public void setMaHDon(String maHDon) {
        this.maHDon = maHDon;
    }

    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    public int getThang() {
        return thang;
    }

    public void setThang(int thang) {
        this.thang = thang;
    }

    public int getNam() {
        return nam;
    }

    public void setNam(int nam) {
        this.nam = nam;
    }

    public double getTienPhong() {
        return tienPhong;
    }

    public void setTienPhong(double tienPhong) {
        this.tienPhong = tienPhong;
    }

    public double getTienDien() {
        return tienDien;
    }

    public void setTienDien(double tienDien) {
        this.tienDien = tienDien;
    }

    public double getTienNuoc() {
        return tienNuoc;
    }

    public void setTienNuoc(double tienNuoc) {
        this.tienNuoc = tienNuoc;
    }

    public double getTienXe() {
        return tienXe;
    }

    public void setTienXe(double tienXe) {
        this.tienXe = tienXe;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }
}
