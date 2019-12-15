package com.example.demo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Recommender {
	
	UserHandler userHander;
	HistoryHandler historyHander;
	RestaurantHandler resHander;
	public Recommender() {
		userHander=new UserHandler();
		historyHander=new HistoryHandler();
		resHander=new RestaurantHandler();
	}
	
	//based on similarity of category, find the most cloest friends
	//whether the category is in list of user's favorite category, category is similalr user.
	//if so, add to set
	public List<Restaurant> RestaurantrecommendByFriends(String user_id, String postal_code) throws SQLException{
		
		// get all friends
		List<String> result = new ArrayList<>();
		List<String> restaurants = historyHander.getTopResByUserId(user_id);
		List<String> category = resHander.getTopCategory(restaurants);
		List<String> friends = userHander.getFriends(user_id);
		HashMap<String,Double> map  = new HashMap<String, Double>();
		//calculate the similarity of each friends with current user
		for(String friend:friends) {
			List<String> ress = historyHander.getTopResByUserId(friend);
			List<String> friends_catogary = resHander.getTopCategory(ress);
			double similarity = calculateSimilarity(category,user_id, friends_catogary, friend);
			map.put(friend, similarity);
		}
		// sort friends by similarity
		List<Map.Entry<String, Double>> list = new ArrayList<>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>(){
			public int compare(Map.Entry<String, Double> a, Map.Entry<String, Double> b) {
				return (int) (b.getValue()-a.getValue());
			}
		});
		// get first 5 friends
		for(int i=0;i<5;i++) {
			String friend = list.get(i).getKey();
			List<String> friend_top_restaurants = historyHander.getTopResByUserId(friend);
			for(String friend_restaurant: friend_top_restaurants) {
				if(resHander.ifRestaurantInSamePostalCode(friend_restaurant, postal_code)) {
					result.add(friend_restaurant);
				}
			}
		}
		return resHander.getRestaurantByBusinessId(result);
	}
	
	public double calculateSimilarity(List<String> category,String res_user_id, List<String> friend_category, String des_user_id) {
		Set<String> cateory_set = new HashSet<>();
		for(String single:friend_category) {
			cateory_set.add(single);
		}
		for(String single:category) {
			cateory_set.add(single);
		}
		List<String> common = new ArrayList<>(cateory_set);
		List<Integer> res_count = new ArrayList<>();
		List<Integer> des_count = new ArrayList<>();
		for(int i=0;i<common.size();i++) {
			res_count.add(historyHander.countVisiedCatory(common.get(i), res_user_id));
			des_count.add(historyHander.countVisiedCatory(common.get(i), des_user_id));
		}
		
		if(common.size()==0)return 0;
		double DotProduct = 0;
		double euclid1 = 0;
		double euclid2 = 0;
		for(int i=0;i<res_count.size();i++) {
			DotProduct += res_count.get(i)* des_count.get(i);
			euclid1 += Math.pow(res_count.get(i), 2);
			euclid2 += Math.pow(des_count.get(i), 2);
		}
		euclid1 = Math.sqrt(euclid1);
		euclid2 = Math.sqrt(euclid2);
		
		return DotProduct/(euclid1*euclid2);
	}
	public static void main(String[] args) throws SQLException {
		String user_id = "FuTJWFYm4UKqewaosss1KA";
		String post_code = "85308";
		Recommender recomender = new Recommender();
		List<Restaurant> recommendByFriend = recomender.RestaurantrecommendByFriends(user_id, post_code);
		List<String> res = new ArrayList<>();
		for(Restaurant r:recommendByFriend) {
			res.add(r.getName());
		}
		System.out.println(res);
	}
}
