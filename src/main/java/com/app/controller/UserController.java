package com.app.controller;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.payloads.ApiResponse;
import com.app.payloads.UserDto;
import com.app.payloads.UserResponse;
import com.app.services.UserService;
import com.app.utils.AppConstants;

@RestController
@RequestMapping("/api/")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	//Create User
	@PostMapping("/users")
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
		UserDto createdUserDto = this.userService.createUser(userDto);
		return new ResponseEntity<UserDto>(createdUserDto, HttpStatus.CREATED);
	}
	
	//Update User
	@PutMapping("/users/{userId}")
	public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto , @PathVariable Integer userId){
		UserDto updatedUserDto = this.userService.updateUser(userDto, userId);
		return new ResponseEntity<UserDto>(updatedUserDto,HttpStatus.OK);
	}
	
	//Delete User
	@DeleteMapping("/users/{userId}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable Integer userId){
		this.userService.deleteUser(userId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("User Deleted Successfully" , true),HttpStatus.OK);
	}
	
	//Get All Users
	@GetMapping("/users")
	public ResponseEntity<UserResponse> getAllUsers(@RequestParam(value = "pageNo" , defaultValue = AppConstants.DEFAULT_PAGE_NUMBER , required = false) Integer pageNo,
													 @RequestParam(value = "pageSize" , defaultValue = AppConstants.DEFAULT_PAGE_SIZE , required = false) Integer pageSize,
													 @RequestParam(value = "sortBy" , defaultValue = "id" , required = false) String sortBy,
													 @RequestParam(value = "sortDir" , defaultValue = AppConstants.DEFAULT_SORT_DIRECTION , required = false) String sortDir){
		UserResponse allUsersResponse = this.userService.getAllUsers(pageNo,pageSize,sortBy,sortDir);
		return new ResponseEntity<UserResponse>(allUsersResponse, HttpStatus.OK);
	}
	
	//Get User By ID
	@GetMapping("/users/{userId}")
	public ResponseEntity<UserDto> getAUserByID(@PathVariable Integer userId){
		System.out.println(userId);
		UserDto foundUser = this.userService.getUserByID(userId);
		return new ResponseEntity<UserDto>(foundUser, HttpStatus.OK);
	}
	
}
