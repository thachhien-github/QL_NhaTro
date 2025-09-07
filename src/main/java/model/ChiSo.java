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

    private String maPhong;
    private int thang;
    private int nam;
    private int dienCu;
    private int dienMoi;
    private int nuocCu;
    private int nuocMoi;
    private int chiSoDien;
    private int chiSoNuoc;

    public ChiSo() {
    }

    public ChiSo(String maPhong, int thang, int nam, int dienCu, int dienMoi, int nuocCu, int nuocMoi, int chiSoDien, int chiSoNuoc) {
        this.maPhong = maPhong;
        this.thang = thang;
        this.nam = nam;
        this.dienCu = dienCu;
        this.dienMoi = dienMoi;
        this.nuocCu = nuocCu;
        this.nuocMoi = nuocMoi;
        this.chiSoDien = chiSoDien;
        this.chiSoNuoc = chiSoNuoc;
    }

    public ChiSo(String maPhong, int thang, int nam, int chiSoDien, int chiSoNuoc) {
        this.maPhong = maPhong;
        this.thang = thang;
        this.nam = nam;
        this.chiSoDien = chiSoDien;
        this.chiSoNuoc = chiSoNuoc;
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

    public int getDienCu() {
        return dienCu;
    }

    public void setDienCu(int dienCu) {
        this.dienCu = dienCu;
    }

    public int getDienMoi() {
        return dienMoi;
    }

    public void setDienMoi(int dienMoi) {
        this.dienMoi = dienMoi;
    }

    public int getNuocCu() {
        return nuocCu;
    }

    public void setNuocCu(int nuocCu) {
        this.nuocCu = nuocCu;
    }

    public int getNuocMoi() {
        return nuocMoi;
    }

    public void setNuocMoi(int nuocMoi) {
        this.nuocMoi = nuocMoi;
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

    @Override
    public String toString() {
        return maPhong + " - " + thang + "/" + nam + " - Dien: " + chiSoDien + ", Nuoc: " + chiSoNuoc;
    }

}
