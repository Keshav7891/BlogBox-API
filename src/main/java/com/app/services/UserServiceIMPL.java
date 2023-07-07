package com.app.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.app.entities.User;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.UserDto;
import com.app.payloads.UserResponse;
import com.app.repositories.UserRepo;

@Service
public class UserServiceIMPL implements UserService{

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	

	private User dtoToEntity(UserDto userDto) {
		User user = this.modelMapper.map(userDto, User.class);
		return user;
	}
	
	private UserDto entityToDto(User user) {
		UserDto userDto = this.modelMapper.map(user, UserDto.class);
		return userDto;
	}

	@Override
	public UserDto createUser(UserDto userDto) {
		User user = this.dtoToEntity(userDto);
		User savedUser = this.userRepo.save(user);
		return this.entityToDto(savedUser);
	}

	@Override
	public UserDto updateUser(UserDto userDto, Integer userId) {
		User user = this.userRepo.findById(userId).orElseThrow( () -> new ResourceNotFoundException("User"," ID " , userId) );
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setPassword(userDto.getPassword());
		user.setAbout(userDto.getAbout());
		User updatedUser = this.userRepo.save(user);
		return this.entityToDto(updatedUser);
	}

	@Override
	public void deleteUser(Integer userId) {
		this.userRepo.findById(userId).orElseThrow( () -> new ResourceNotFoundException("User"," ID ", userId) );
		this.userRepo.deleteById(userId);
	}

	@Override
	public UserResponse getAllUsers(Integer pageNo , Integer pageSize , String sortBy, String sortDir) {
		
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		
		Page<User> allUsersPage = this.userRepo.findAll(pageable);
		List<User> allUsers = allUsersPage.getContent();
		List<UserDto> allUsersDto = allUsers.stream().map( (user) -> entityToDto(user)).collect(Collectors.toList());
		
		UserResponse userResponse = new UserResponse();
		userResponse.setAllUsers(allUsersDto);
		userResponse.setPageNo(allUsersPage.getNumber());
		userResponse.setPageSize(allUsersPage.getSize());
		userResponse.setTotalElements(allUsersPage.getTotalElements());
		userResponse.setTotalPages(allUsersPage.getTotalPages());
		userResponse.setIsLast(allUsersPage.isLast());
		
		return userResponse;
	}

	@Override
	public UserDto getUserByID(Integer userId) {
		User foundUser = this.userRepo.findById(userId).orElseThrow( () -> new ResourceNotFoundException("User", " ID " , userId));
		return this.entityToDto(foundUser);
	}
	
	
	
	
	
}
