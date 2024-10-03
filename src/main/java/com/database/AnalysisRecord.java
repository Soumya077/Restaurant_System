package com.database;

import java.sql.*;
import java.util.*;

public class AnalysisRecord {
    public static ArrayList<ArrayList<String>> top10Food(){
        ArrayList<ArrayList<String>> a = new ArrayList<>();
        try{
            Connection con = DbConnect.takeConnection();
            String query = "select sum(quantity),item_name from order_list group by item_name order by sum(quantity) desc limit 10";
            PreparedStatement ps = con.prepareCall(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                ArrayList<String> b = new ArrayList<>();
                b.add(rs.getString(1));
                b.add(rs.getString(2));
                a.add(b);
            }
            con.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return a ;
    }
    
    public static ArrayList<ArrayList<String>> top10Customer(){
        ArrayList<ArrayList<String>> a = new ArrayList<>();
        try{
            Connection con = DbConnect.takeConnection();
            String query = " select customer_contact,sum(bill_amt) from orders group by customer_contact order by sum(bill_amt) desc limit 10;";
            PreparedStatement ps = con.prepareCall(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                ArrayList<String> b = new ArrayList<>();
                b.add(rs.getString(1));
                b.add(rs.getString(2));
                a.add(b);
            }
            con.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return a ;
    }
    
    public static ArrayList<ArrayList<String>> betweendates(String date1 , String date2){
        ArrayList<ArrayList<String>> a = new ArrayList<>();
        try{
            Connection con = DbConnect.takeConnection();
            String query = "select sum(bill_amt),date from orders where date between ? and ? group by date";
            PreparedStatement ps = con.prepareCall(query);
            ps.setString(1, date1);
            ps.setString(2, date2);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                ArrayList<String> b = new ArrayList<>();
                b.add(rs.getString(1));
                b.add(rs.getString(2));
                a.add(b);
            }
            con.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return a ;
    }
}
