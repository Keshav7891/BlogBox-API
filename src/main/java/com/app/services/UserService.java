package com.app.services;

import com.app.payloads.UserDto;
import com.app.payloads.UserResponse;

public interface UserService {
	
	//Create User
	public UserDto createUser(UserDto userDto);
	
	//Update User
	public UserDto updateUser(UserDto userDto , Integer userId);
	
	//Delete User
	public void deleteUser(Integer userId);
	
	//Get All User
	public UserResponse getAllUsers(Integer pageNo , Integer pageSize , String sortBy , String  sortDir);
	
	//Get User By ID
	public UserDto getUserByID(Integer userId);
	
	
}
