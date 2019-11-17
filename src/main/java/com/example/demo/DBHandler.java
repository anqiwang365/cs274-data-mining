package com.example.demo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class DBHandler {
	
	private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

	
	public void writeUserToDB() throws SQLException {
		BufferedReader reader;
		BufferedWriter writer;
		String url = "jdbc:mysql://localhost:3306/data_mining";
        String user = "root";
        String password = "waq_0145_password&986";
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        } 
        catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found !!");
            return;
        }
        try {
            connect = DriverManager
                .getConnection(url, user, password);
            System.out.println("SQL Connection to database established!");
 
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
            System.out.println("Connection Failed! Check output console");
            return;
        }
        
		try {
			reader = new BufferedReader(new FileReader("/Users/waq/Downloads/yelp_dataset/user.json"));
			String line = reader.readLine();
			while(line!=null) {
				
				JsonObject jsonObject = new JsonParser().parse(line).getAsJsonObject();
				String user_id = jsonObject.get("user_id").toString();
				user_id = user_id.substring(1,user_id.length()-1);
				String friends = jsonObject.get("friends").toString();
				friends = friends.substring(1,friends.length()-1);
				String[] arr_friends = friends.split(",");
				StringBuilder topfriends = new StringBuilder();
				if(arr_friends.length>10) {
					for(int i=0;i<10;i++) {
						topfriends.append(arr_friends[i]+",");
					}	
					friends = topfriends.toString();
				}
				String query = "insert into user(user_id,friends) VALUES('"+user_id+"','"+friends+"');";
				PreparedStatement preparedStmt = connect.prepareStatement(query);
				preparedStmt.executeUpdate();
				System.out.println(user_id);
				line = reader.readLine();
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) throws SQLException {
		DBHandler demo = new DBHandler();
		demo.writeUserToDB();
	}
}
