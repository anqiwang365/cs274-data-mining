package com.example.demo;
import java.util.*;
public class User {

	int id;
	String user_id;
	String friends;//split by ","
	String top_category;
	String top_business;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getFriends() {
		return friends;
	}
	public void setFriends(String friends) {
		this.friends = friends;
	}
	public String getTop_category() {
		return top_category;
	}
	public void setTop_category(String top_category) {
		this.top_category = top_category;
	}
	public String getTop_business() {
		return top_business;
	}
	public void setTop_business(String top_business) {
		this.top_business = top_business;
	}
	
}
