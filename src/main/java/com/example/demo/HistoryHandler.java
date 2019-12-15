package com.example.demo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.Map.Entry;

public class HistoryHandler {

	Connection connect  = null;
	Statement statement = null;
	PreparedStatement preparedStatement = null;
	public Connection connectDB() {
		String url = "jdbc:mysql://localhost:3306/data_mining";
        String user = "root";
        String password = "waq_0145_password&986";
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
            // System.out.println("SQL Connection to database established!");
 
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
            System.out.println("Connection Failed! Check output console");
            
        }
		return connect;
	}
	
	//return top 10 restaurant business id and store the top restaurant back to user
	//return business id
	public List<String> getTopResByUserId(String userId) throws SQLException {

		ResultSet rst = null;
		connect = connectDB();
        try {
			statement = connect.createStatement();
			String sql = "SELECT count(*) as count, business_id FROM history WHERE user_id ='"+userId+"' group by business_id order by count desc;";
			rst = statement.executeQuery(sql);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        List<String> topList = new ArrayList<>();
		int count = 0;
		while(rst.next()) {
			////////
			if(count>10)break;
			topList.add(rst.getString("business_id"));
			count ++;
		}
		//add the user table
		try {
			statement = connect.createStatement();
			String top_business = convertName(topList);
			String sql = "update user SET top_business='"+top_business+"' where user_id ='"+userId+"';";
			statement.executeUpdate(sql);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return topList;
	}
	
	//return top 10 restaurant business id and store the top restaurant back to user from test dataset
	//return business id
	public List<String> getTestTopResByUserId(String userId) throws SQLException {

		ResultSet rst = null;
		connect = connectDB();
        try {
			statement = connect.createStatement();
			String sql = "SELECT count(*) as count, business_id FROM history_test WHERE user_id ='"+userId+"' and group by business_id order by count desc;";
			rst = statement.executeQuery(sql);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        List<String> topList = new ArrayList<>();
		int count = 0;
		while(rst.next()) {
			////////
			if(count>10)break;
			topList.add(rst.getString("business_id"));
			count ++;
		}
		return topList;
	}
	
	public int countVisiedCatory(String category_name, String user_id) {
		ResultSet rst = null;
		connect = connectDB();
		int result =0 ;
        try {
			statement = connect.createStatement();
			Statement statement1 = connect.createStatement();
			String sql = "SELECT business_id FROM history WHERE user_id ='"+user_id+"';";
			rst = statement.executeQuery(sql);
			while(rst.next()) {
				String category = "SELECT category FROM business WHERE business_id ='"+rst.getString("business_id")+"';";
				ResultSet rst_category = statement1.executeQuery(category);
				if(rst_category.next()) {
					if(rst_category.getString("category").contains("category_name")) {
						result++;
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return result;
	}
	
	public String convertName(List<String> businessList) {
		StringBuilder sb = new StringBuilder();
		for(String business:businessList) {
			sb.append(business);
			sb.append(",");
		}
		return sb.toString();
	}
	
	public static void main(String[] args) throws SQLException {
		HistoryHandler handler = new HistoryHandler();
		System.out.println(handler.getTopResByUserId("UPw5DWs_b-e2JRBS-t37Ag"));
	}	
}
