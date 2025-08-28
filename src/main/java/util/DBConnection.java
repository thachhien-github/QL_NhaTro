/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author ThachHien
 */
public class DBConnection {

    static String url = "jdbc:sqlserver://THACHHIEN:1433;databaseName=QuanLyNhaTro;encrypt=false";
    static String user = "sa";
    static String pass = "sa";
    
    public static Connection getConnection()
    {
        Connection conn=null;
        try {
            //nap driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            // 2. Kết nối CSDL
             conn = DriverManager.getConnection(url, user, pass);        
        } catch (Exception ex) {
            System.out.println("Loi: " + ex.toString());
        }
        return conn;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
