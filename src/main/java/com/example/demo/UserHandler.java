package com.example.demo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


public class UserHandler {

	Connection connect  = null;
	Statement statement = null;
	PreparedStatement preparedStatement = null;
	
	public List<String> getAllUsers() throws SQLException{
		String url = "jdbc:mysql://localhost:3306/data_mining";
        String user = "root";
        String password = "waq_0145_password&986";
        ResultSet rst = null;
        List<String> users = new ArrayList<>();
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
            //System.out.println("SQL Connection to database established!");
 
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
            System.out.println("Connection Failed! Check output console");
            
        }
        
        try {
			statement = connect.createStatement();
			String sql = "SELECT user FROM user;";
			rst = statement.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(rst.next()) {
			String userId = rst.getString("user");
	        users.add(userId);
		}
        return users;
	}
	
	// get friends of user
	public List<String> getFriends(String userId) throws SQLException{
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
            //System.out.println("SQL Connection to database established!");
 
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
            System.out.println("Connection Failed! Check output console");
            
        }
        
        try {
			statement = connect.createStatement();
			String sql = "SELECT * FROM user WHERE user_id ='"+userId+"';";
			rst = statement.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(rst.next()) {
			String friendIds = rst.getString("friends");
	        List<String> friend_List = convertToList(friendIds);
	        return friend_List;
		}
        return new ArrayList<>();
        
	}
	
	//convert string to list
	public List<String> convertToList(String friends){
		//sList<String> res = new ArrayList();
		String[] list = friends.split(",");
		return Arrays.asList(list);
	}
	
	public static void main(String[] args) throws SQLException {
		UserHandler hander = new UserHandler();
		System.out.println(hander.getFriends("iAd8XaHxv05iXyEiZ62Ibw"));
	}
}
