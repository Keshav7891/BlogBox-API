package com.app.services;

import java.util.List;


import com.app.payloads.PostDto;
import com.app.payloads.PostResponse;

public interface PostService {
	
	//Create a Post
	public PostDto createPost(PostDto postDto , Integer userId , Integer categoryId);
	
	public PostDto getPostById(Integer postId);
	
	//Get all posts
	public PostResponse getAllPosts(Integer pageNo , Integer pageSize , String sortBy , String sortDir);
		
	// Delete a post
	public void deletePost(Integer postId);
	
	//update a post
	public PostDto updatePost(PostDto postDto , Integer postId);
		
	//get all posts of a user
	public List<PostDto> getUserPosts(Integer userId);
		
	// get all post from a category
	public List<PostDto> getCategoryPosts(Integer categoryId);
		
	// Search post
	public List<PostDto> searchPostByTitle(String keyword);
	
}
