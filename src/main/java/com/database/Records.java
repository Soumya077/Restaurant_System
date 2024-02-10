
package com.database;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
public class Records {
    public static boolean checkLogin(String username , String password , String department)
    {
        boolean status = false ; 
        
        try{
            Connection con = DbConnect.takeConnection();
            String query = "select * from user_info where username=? and password = ? and department = ?";
            
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,username);
            ps.setString(2,password);
            ps.setString(3,department);
            
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                status = true ;
                break ;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return status ;
        
    }
    
    public static ArrayList<ArrayList<String>> getItems(){
        ArrayList<ArrayList<String>> a = new ArrayList<ArrayList<String>>() ;
        try{
            Connection con = DbConnect.takeConnection();
            String query = "select item_name , price from item_list order by item_name" ;
            PreparedStatement ps = con.prepareCall(query);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                ArrayList<String> b = new ArrayList<String>();
                b.add(rs.getString(1));
                b.add(rs.getString(2));
                a.add(b);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return a ;
    }
    
    public static ArrayList<ArrayList<String>> getAllItems(){
        ArrayList<ArrayList<String>> a = new ArrayList<ArrayList<String>>() ;
        try{
            Connection con = DbConnect.takeConnection();
            String query = "select * from item_list order by id" ;
            PreparedStatement ps = con.prepareCall(query);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                ArrayList<String> b = new ArrayList<String>();
                b.add("" + rs.getInt(1));
                b.add(rs.getString(2));
                b.add(rs.getString(3));
                a.add(b);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return a ;
    }
    
    public static String getPrice(String item){
        String price = null ;
        try{
            Connection con = DbConnect.takeConnection();
            String query = "select price from item_list where item_name = ? " ;
            PreparedStatement ps = con.prepareCall(query);
            ps.setString(1,item);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                price = rs.getString(1);
                break ;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return price ;
    }
    
    public static int customerDet(String customer_name , String customer_contact , String bill_amt ,String cashier){
        int status = 0 ;
        try{
            
            java.util.Date d = new java.util.Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(d);
            
            Connection con = DbConnect.takeConnection();
            String query = "insert into orders(customer_name , customer_contact , bill_amt , date , cashier) values(?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,customer_name );
            ps.setString(2,customer_contact );
            ps.setString(3,bill_amt );
            ps.setString(4,date );
            ps.setString(5,cashier );
            
            status = ps.executeUpdate();
            
            con.close();
            
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return status ;
    }
    
    public static int getBillNum(){
        int num = 0 ;
        try{
            
            
            Connection con = DbConnect.takeConnection();
            String query = "select bill_no from orders order by bill_no desc";
            PreparedStatement ps = con.prepareStatement(query);
           
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                num = rs.getInt(1);
                break;
            }
            con.close();
            
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return num ;
    }
    
    public static int storeOrder(String customer_name , String customer_contact , String bill_amt ,String cashier, ArrayList<ArrayList<String>> order ){
        int status = customerDet(customer_name , customer_contact , bill_amt , cashier);
        int order_status = 0 ;
        if(status == 1){
            int bill_no = getBillNum();
            try{

                Connection con = DbConnect.takeConnection();
                String query = "insert into order_list(item_name , price , quantity , total , bill_no) values(?,?,?,?,?)";
                PreparedStatement ps = con.prepareStatement(query);
                
                con.setAutoCommit(false);
                
                for(ArrayList<String> a : order){
                    ps.setString(1, a.get(1) );
                    ps.setString(2,a.get(4) );
                    ps.setString(3,a.get(2) );
                    ps.setString(4, a.get(3) );
                    ps.setInt(5,bill_no );
                    
                    ps.addBatch();
                }
                
                ps.executeBatch();
                con.commit();
                order_status = 1 ;
                con.close();

            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        
        return order_status ;
    }
    
    public static int insertItem(String item , String price){
        int status = 0 ;
        ArrayList<ArrayList<String>> a = getItems();
        for(ArrayList<String> b : a){
            if( item.equalsIgnoreCase(b.get(0)) ){
                if(price.equals(b.get(1)))
                    return -2 ;
                else
                    return -1 ;
            } 
        }
        try{ 
            Connection con = DbConnect.takeConnection();
            String query = "insert into item_list(item_name , price) values(?,?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,item);
            ps.setString(2, price);
            
            status = ps.executeUpdate();
            
            con.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        return status ;
    }
    
    public static void deleteItem(String id){
        try{ 
            Connection con = DbConnect.takeConnection();
            String query = " delete from item_list where id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,id);
            ps.executeUpdate();
            
            con.close();
            
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void updateName(String id , String name){
        try{ 
            Connection con = DbConnect.takeConnection();
            String query = " update item_list set item_name = ? where id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,name);
            ps.setString(2,id);
            ps.executeUpdate();
            
            con.close();
            
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void updatePrice(String name , String price){
        try{ 
            Connection con = DbConnect.takeConnection();
            String query = " update item_list set price = ? where item_name=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,price);
            ps.setString(2,name);
            ps.executeUpdate();
            
            con.close();
            
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
   
}
