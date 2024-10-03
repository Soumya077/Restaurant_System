
package com.database;
import java.sql.*;

public class DbConnect {
    public static Connection takeConnection()
    {
        Connection con = null ;
        
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String path = "jdbc:mysql://localhost:3306/billing_system";
            String user = "root";
            String password = "hello";
            
            con = DriverManager.getConnection(path, user, password);
                
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        return con ;
    }
}
