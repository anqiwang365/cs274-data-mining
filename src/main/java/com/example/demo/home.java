package com.example.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;
import java.util.*;


@RestController
public class home {
	
	HistoryHandler historyHandler = new HistoryHandler();
	UserHandler userHandler = new UserHandler();
	RestaurantHandler restaurantHandler = new RestaurantHandler();
	Recommender recommender = new Recommender();
	List<String> friendsNames = new ArrayList<>();
	//get recommend results
	@RequestMapping("/")
	public List<String> recommendByLocation(String user_id, String postal_code) throws SQLException {
		List<String> histroy_businessid =  historyHandler.getTopResByUserId(user_id);//user top business id
		List<String> category_list = restaurantHandler.getTopCategory(histroy_businessid);//
		List<Restaurant> restaurantByLocation = new ArrayList<>();
		for(String category:category_list) {
			Set<Restaurant> topBusiness = restaurantHandler.getTopBusinessWithCategory(category, postal_code);
			for(Restaurant r:topBusiness) {
				if(!restaurantByLocation.contains(r)) {
					restaurantByLocation.add(r);
				}
				
			}
		}
		
		List<Restaurant> recommendByFriend = recommender.RestaurantrecommendByFriends(user_id, postal_code);
		List<String> res = new ArrayList<>();
		for(Restaurant r:recommendByFriend) {
			res.add(r.getName());
		}
		for(Restaurant r:restaurantByLocation) {
			if(!res.contains(r.getName())) {
				res.add(r.getName());
			}
		}
		if(res.size()==0) {
			List<Restaurant> mostPopular = restaurantHandler.getTopBusinessWithPostalCode(postal_code);
			for(Restaurant r:mostPopular) {
				res.add(r.getName());
			}
		}
		return res;
	}
	
	public String showList(List<String> list) {
		StringBuilder sb = new StringBuilder();
		for(String item:list) {
			sb.append(item).append("/r/n");
		}
		return sb.toString();
	}
	
	// test if all's user recommended restaurant will show up in the test dataset. 
	public double testAccuracy() throws SQLException {
		int total =0;
		int matched = 0;
		// get all users from training dataset
		List<String> allUsers = userHandler.getAllUsers();
		total = allUsers.size();
		for(String user:allUsers) {
			// get all visited postal_code of user from history from training dataset
			List<String> topBusiness = historyHandler.getTopResByUserId(user);
			List<String> postal_codes = restaurantHandler.getPostalCodeByBusiness(topBusiness);
			List<String> result = new ArrayList<>();
			for(String postal_code: postal_codes) {
				// recommend restaurant based on visited postal_code from training dataset
				List<String> commendedRes = recommendByLocation(user,postal_code);
				for(String res:commendedRes) {
					result.add(res);
				}
			}
			// get all the visited restaurant from test dataset
			List<String> testBusiness = historyHandler.getTestTopResByUserId(user);
			// check if recommended res from training dataset is displayed in test dataset
			if(checkRecommendAccurency(result,testBusiness)) {
				matched++;
			}	
		}
		return matched/total;
	}
	public boolean checkRecommendAccurency(List<String> trained, List<String> testBusiness) {
		Set<String> set = new HashSet<>();
		for(String res:trained) {
			set.add(res);
		}
		for(String res:testBusiness) {
			if(set.contains(res)) {
				return true;
			}
		}
		return false;
	}
	public static void main(String[] args) throws SQLException {
		home test = new home();
		List<String> result = test.recommendByLocation("FuTJWFYm4UKqewaosss1KA", "85308");
		for(String res:result) {
			System.out.println(res);
		}
	}
}
