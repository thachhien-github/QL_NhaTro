/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ThachHien
 */
public class Xe {
    private String maXe;
    private String maKT;
    private String bienSo;
    private String loaiXe;
    private double phiGiuXe;

    public Xe() {}

    public Xe(String maXe, String maKT, String bienSo, String loaiXe, double phiGiuXe) {
        this.maXe = maXe;
        this.maKT = maKT;
        this.bienSo = bienSo;
        this.loaiXe = loaiXe;
        this.phiGiuXe = phiGiuXe;
    }

    public String getMaXe() {
        return maXe;
    }

    public void setMaXe(String maXe) {
        this.maXe = maXe;
    }

    public String getMaKT() {
        return maKT;
    }

    public void setMaKT(String maKT) {
        this.maKT = maKT;
    }

    public String getBienSo() {
        return bienSo;
    }

    public void setBienSo(String bienSo) {
        this.bienSo = bienSo;
    }

    public String getLoaiXe() {
        return loaiXe;
    }

    public void setLoaiXe(String loaiXe) {
        this.loaiXe = loaiXe;
    }

    public double getPhiGiuXe() {
        return phiGiuXe;
    }

    public void setPhiGiuXe(double phiGiuXe) {
        this.phiGiuXe = phiGiuXe;
    }
    
    
}
