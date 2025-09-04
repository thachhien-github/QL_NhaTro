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
    private BigDecimal tienPhong;
    private BigDecimal tienDien;
    private BigDecimal tienNuoc;
    private BigDecimal tienXe;
    private BigDecimal tongTien;

    public HoaDon(String maHDon, String maPhong, int thang, int nam, BigDecimal tienPhong, BigDecimal tienDien, BigDecimal tienNuoc, BigDecimal tienXe, BigDecimal tongTien) {
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

    public HoaDon(String maHDon, String maPhong, int thang, int nam, BigDecimal tienPhong, BigDecimal tienDien, BigDecimal tienNuoc, BigDecimal tienXe) {
        this.maHDon = maHDon;
        this.maPhong = maPhong;
        this.thang = thang;
        this.nam = nam;
        this.tienPhong = tienPhong;
        this.tienDien = tienDien;
        this.tienNuoc = tienNuoc;
        this.tienXe = tienXe;
    }
    
    public HoaDon() {}

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

    public BigDecimal getTienPhong() {
        return tienPhong;
    }

    public void setTienPhong(BigDecimal tienPhong) {
        this.tienPhong = tienPhong;
    }

    public BigDecimal getTienDien() {
        return tienDien;
    }

    public void setTienDien(BigDecimal tienDien) {
        this.tienDien = tienDien;
    }

    public BigDecimal getTienNuoc() {
        return tienNuoc;
    }

    public void setTienNuoc(BigDecimal tienNuoc) {
        this.tienNuoc = tienNuoc;
    }

    public BigDecimal getTienXe() {
        return tienXe;
    }

    public void setTienXe(BigDecimal tienXe) {
        this.tienXe = tienXe;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }

    @Override
    public String toString() {
        return "HoaDon{" +
                "maHDon='" + maHDon + '\'' +
                ", maPhong='" + maPhong + '\'' +
                ", thang=" + thang +
                ", nam=" + nam +
                ", tienPhong=" + tienPhong +
                ", tienDien=" + tienDien +
                ", tienNuoc=" + tienNuoc +
                ", tienXe=" + tienXe +
                ", tongTien=" + tongTien +
                '}';
    }
    
    
    
}
