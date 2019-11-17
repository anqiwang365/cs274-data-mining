package com.example.demo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class restaurantHandler {
	
	Connection connect  = null;
	Statement statement = null;
	PreparedStatement preparedStatement = null;
	
	//return categories of the recommended restaurants by a list of business_ids
	public List<String> getCategory(List<String> businessId) {
		List<String> category = new ArrayList<>();
		String url = "jdbc:mysql://localhost:3306/data_mining";
        String user = "root";
        String password = "waq_0145_password&986";
        ResultSet rst = null;
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        } 
        catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found !!");
            
        }
        try {
            connect = DriverManager
                .getConnection(url, user, password);
            System.out.println("SQL Connection to database established!");
 
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
            System.out.println("Connection Failed! Check output console");
            
        }
        
        try {
			statement = connect.createStatement();
			for(int i=0;i<businessId.size();i++) {
				String sql = "SELECT * FROM business WHERE business_id = "+ businessId.get(i);
				rst = statement.executeQuery(sql);
				String temp_category = rst.getString("category");
				category.add(temp_category);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String top_category = convertName(category);
        try {
			statement = connect.createStatement();
			String insertsql = "insert into user(top_category)"+"values"+top_category;
			
			rst = statement.executeQuery(insertsql);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return category;
		
	}
	
	public String convertName(List<String> categories) {
		StringBuilder sb = new StringBuilder();
		for(String category:categories) {
			sb.append(category);
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}

}
