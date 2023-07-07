package com.app.services;

import java.util.List;

import com.app.payloads.CommentDto;
import com.app.payloads.CommentResponse;


public interface CommentService {

	//Create A Comment ->(takes userId , postId)
		
		//Get All comments 
		CommentResponse getAllComments(Integer pageNo , Integer pageSize , String sortBy , String  sortDir);
		
		//Get All comments of a user
		List<CommentDto> getUserComments(Integer userId);
		
		//Get All comments of a post
		List<CommentDto> getPostComments(Integer postId);
		
		//Delete a Comment -> (takes COmmentId)
		void deleteComment(Integer commentId);
		
		//Update a comment -> (takes new comment + commentID)
		CommentDto updateComment(CommentDto commentDto , Integer commentId);
		
		// Search any comment with keyword
		List<CommentDto> searchComment(String keyword);

		CommentDto createComment(CommentDto commentDto, Integer userId, Integer postId);	
	
	
}
