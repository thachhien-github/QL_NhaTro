/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ThachHien
 */
public class ChiSo {
    private String maCS;
    private String maPhong;
    private int thang;
    private int nam;
    private int chiSoDien;
    private int chiSoNuoc;

    public ChiSo() {
    }

    public ChiSo(String maCS, String maPhong, int thang, int nam, int chiSoDien, int chiSoNuoc) {
        this.maCS = maCS;
        this.maPhong = maPhong;
        this.thang = thang;
        this.nam = nam;
        this.chiSoDien = chiSoDien;
        this.chiSoNuoc = chiSoNuoc;
    }

    public String getMaCS() {
        return maCS;
    }

    public void setMaCS(String maCS) {
        this.maCS = maCS;
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

    public int getChiSoDien() {
        return chiSoDien;
    }

    public void setChiSoDien(int chiSoDien) {
        this.chiSoDien = chiSoDien;
    }

    public int getChiSoNuoc() {
        return chiSoNuoc;
    }

    public void setChiSoNuoc(int chiSoNuoc) {
        this.chiSoNuoc = chiSoNuoc;
    }
    
    
}
