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
	//return top 10 restaurant business id and store the top restaurant back to user
	public List<String> getHistoryByUserId(String userId) throws SQLException {
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
			String sql = "SELECT * FROM history WHERE user_id ="+userId;
			rst = statement.executeQuery(sql);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        List<String> topList = new ArrayList<>();
		Map<String,Integer> map = new HashMap<>();//business, times
		int top = 0;
		while(rst.next()) {
			////////
			String business_id = rst.getString("business_id");
			map.put(business_id,map.getOrDefault(business_id, 0)+1);
		}
		List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String,Integer>>(map.entrySet());
		Collections.sort(list,new Comparator<Map.Entry<String, Integer>>(){
			public int compare(Entry<String,Integer> o1,Entry<String,Integer> o2) {
				return o1.getValue().compareTo((o2.getValue()-o1.getValue()));
			}
		});
		
		for(int i=0;i<10;i++) {
			Map.Entry<String,Integer> temp = list.get(i);
			topList.add(temp.getKey());
		}
		
//		String top_business = convertName(topList);
//		//insert top_buiness into user
//		
//		try {
//			statement = connect.createStatement();
//			String insertsql = "insert into user(top_business)"+"values"+top_business;
//			
//			rst = statement.executeQuery(insertsql);
//			
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return topList;
		
        
	}
	
	public String convertName(List<String> businessList) {
		StringBuilder sb = new StringBuilder();
		for(String business:businessList) {
			sb.append(business);
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	

	
}
