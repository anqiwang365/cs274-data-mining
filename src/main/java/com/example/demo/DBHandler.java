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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBHandler {
	
	private Connection connect = null;

	
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
			List<JsonObject> list = new ArrayList<>();
			reader = new BufferedReader(new FileReader("/Users/waq/Downloads/yelp_dataset/tip.json"));
			String line = reader.readLine();
			while(line!=null) {
				JsonObject jsonObject = new JsonParser().parse(line).getAsJsonObject();
				list.add(jsonObject);
//				String name = jsonObject.get("name").toString();
//				name = name.substring(1,name.length()-1);
//				name = name.replace("'","");
//				String business_id = jsonObject.get("business_id").toString();
//				business_id = business_id.substring(1,business_id.length()-1);
//				String postal_code = jsonObject.get("postal_code").toString();
//				postal_code = postal_code.substring(1,postal_code.length()-1);
//				double stars = Double.parseDouble(jsonObject.get("stars").toString());
//				int review_count = Integer.parseInt(jsonObject.get("review_count").toString());
//				String categories = jsonObject.get("categories").toString();
//				categories = categories.substring(1,categories.length()-1);
//				categories = categories.replace("'","");
//				String[] category = categories.split(",");
//				String category5 = "";
//				for(int i=0;i<category.length;i++) {
//					if(i>5)break;
//					category5 += category[i]+",";
//				}
//				categories = category5;
//				
//				String query = "insert into business(business_id, name, postal_code, star, review_count, category) VALUES('"+business_id+"','"+name+"','"+postal_code+"','"+stars+"','"+review_count+"','"+categories+"');";
//				PreparedStatement preparedStmt = connect.prepareStatement(query);
//				preparedStmt.executeUpdate();
//				System.out.println(business_id);
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
