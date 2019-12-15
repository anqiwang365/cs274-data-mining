package com.example.demo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class RestaurantHandler {
	
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
            //System.out.println("SQL Connection to database established!");
 
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
            System.out.println("Connection Failed! Check output console");
            
        }
        return connect;
	}
	//return categories of the recommended restaurants by a list of business_ids
	public List<String> getTopCategory(List<String> businessId) {
		List<String> categoryList = new ArrayList<>();
        ResultSet rst = null;
        connect = connectDB();
        HashMap<String, Integer> categoryCount = new HashMap<>();
        
        try {
			statement = connect.createStatement();
			for(int i=0;i<businessId.size();i++) {
				String sql = "SELECT * FROM business WHERE business_id ='"+businessId.get(i)+"';";
				rst = statement.executeQuery(sql);
				String temp_category = "";
				while(rst.next()) {
					temp_category = rst.getString("category");
					String[] user_categorys = temp_category.split(",");
					for(int j=0;j<user_categorys.length;j++) {
						String user_category = user_categorys[j];
						categoryCount.put(user_category, categoryCount.getOrDefault(user_category, 0)+1);
					}
				}
			}
			//sort hashmap by value
			List<Map.Entry<String, Integer>> list = new ArrayList<>(categoryCount.entrySet());
			Collections.sort(list, new Comparator<Map.Entry<String, Integer>>(){
				public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
					return b.getValue()-a.getValue();
				}
			});
			int count =0;
			for(Map.Entry<String, Integer> entry: list) {
				if(count > 5)break;
				categoryList.add(entry.getKey());
				count++;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return categoryList;
	}
	
	public List<Restaurant> getRestaurantByBusinessId(List<String> businessId){
		List<Restaurant> restaurant_List = new ArrayList<>();
		ResultSet rst = null;
		connect = connectDB();
		try {
			statement = connect.createStatement();
			for(int i=0;i<businessId.size();i++) {
				String sql = "SELECT * FROM business WHERE business_id ='"+businessId.get(i)+"';";
				rst = statement.executeQuery(sql);
				Restaurant restaurant = new Restaurant();
				restaurant.setId(rst.getInt("id"));
				restaurant.setCategory(rst.getString("category"));
				restaurant.setName(rst.getString("name"));
				restaurant.setPostal_code(rst.getString("postal_code"));
				restaurant_List.add(restaurant);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return restaurant_List;
		
	}
	public boolean ifRestaurantInSamePostalCode(String businessId, String postal_code){
		ResultSet rst = null;
		connect = connectDB();
		try {
			statement = connect.createStatement();
			String sql = "SELECT * FROM business WHERE business_id ='"+businessId+"';";
			rst = statement.executeQuery(sql);
			String code =rst.getString("postal_code");
			if(code.equals(postal_code))
				return true;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
	}
	
	public List<String> getPostalCodeByBusiness(List<String> business_ids){
		List<String> topRest = new ArrayList<>();
        ResultSet rst = null;
        connect = connectDB();
        try {
			statement = connect.createStatement();
			String sql = "select postal_code from business where business_id in "+ business_ids.toString() +";";
			rst = statement.executeQuery(sql);
			while(rst.next()) {
				topRest.add(rst.getString("postal_code"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return topRest;
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

	//step1
	// based on category find the top restaurant in certain zip_code (star>4)
	// return  restaurants( given category)
	public Set<Restaurant> getTopBusinessWithCategory(String category,String postal_code){
		
		Set<Restaurant> topRest = new HashSet<>();
        ResultSet rst = null;
        connect = connectDB();
        try {
			statement = connect.createStatement();
			String sql = "select * from business where category ='"+ category +"' "+"and postal_code= '"+postal_code+"'"+"and star > 4" ;
			rst = statement.executeQuery(sql);
			while(rst.next()) {
				Restaurant r = new Restaurant();
				r.setBusiness_id(rst.getString("business_id"));
				r.setCategory(rst.getString("category"));
				r.setName(rst.getString("name"));
				r.setStars(rst.getDouble("star"));
				topRest.add(r);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return topRest;
	}
	
	//last step 
	// if there is nothing return, just return most popular in current area
	public List<Restaurant> getTopBusinessWithPostalCode(String postal_code){
		
		List<Restaurant> topRest = new ArrayList<>();
        ResultSet rst = null;
        connect = connectDB();
        try {
			statement = connect.createStatement();
			String sql = "select * from business where postal_code= '"+postal_code+"'"+"and star > 4 and category like '%Restaurants%' order by star desc" ;
			rst = statement.executeQuery(sql);
			int count =0;
			while(rst.next()) {
				if(count>10)break;
				Restaurant r = new Restaurant();
				r.setBusiness_id(rst.getString("business_id"));
				r.setCategory(rst.getString("category"));
				r.setName(rst.getString("name"));
				r.setStars(rst.getDouble("star"));
				topRest.add(r);
				count++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return topRest;
	}
	
	//return a list of business names
	//given a zipcode , return top restaurant which is within users' category and are top restaurant
	public Set<Restaurant> getTopResInZipcode(String postal_code, List<String> categoryList){
		
		
        ResultSet rst = null;
        connect = connectDB();
        Map<String,List<Restaurant>> map = new HashMap<>();  //key category value:restaurant  
        for(String category:categoryList) {
        	map.put(category,new ArrayList<>());///
        }
        try {
			statement = connect.createStatement();
			String sql = "select * from business where star >4 and postal_code= '"+ postal_code+"';";
			rst = statement.executeQuery(sql);
			while(rst.next()) {
				Restaurant r = new Restaurant();
				String r_category  = rst.getString("category");
				r.setCategory(rst.getString("category")); 
				r.setBusiness_id(rst.getString("business_id"));
				r.setName(rst.getString("name"));
				if(map.containsKey(r_category)) {
					map.get(r_category).add(r);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        Set<Restaurant> topResByZip = new HashSet<>();
        for(String key:map.keySet()) {
        	List<Restaurant> rest = map.get(key);
        	for(Restaurant r:rest) {
        		topResByZip.add(r);
        	}
        }
        return topResByZip;
	}
}
