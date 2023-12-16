package connection;

import java.sql.*;

public class DbConnection {
    private static String user;
    private static String url;
    private static String pass="";
    private static Connection con;

    public static Connection getConnectDB(){
        try {
            user = "root";
            url="jdbc:mysql://localhost:3306/gestionEcole";
            Class.forName("com.mysql.cj.jdbc.Driver");
            try {
                con = DriverManager.getConnection(url,user,pass);
            } catch (SQLException e) {
                
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            
            e.printStackTrace();
        }
        
        return con;
    }
}
