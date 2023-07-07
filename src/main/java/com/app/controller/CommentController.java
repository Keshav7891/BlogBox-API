package com.app.controller;

import java.util.List;

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
import com.app.payloads.CommentDto;
import com.app.payloads.CommentResponse;
import com.app.services.CommentService;
import com.app.utils.AppConstants;

@RestController
@RequestMapping("/api/")
public class CommentController {

	@Autowired
	private CommentService commentService;
	
	@PostMapping("/user/{userId}/posts/{postId}/comments/")
	public ResponseEntity<CommentDto> createComment(@Valid @RequestBody CommentDto commentDto , @PathVariable Integer userId , @PathVariable Integer postId){
		CommentDto savedcommentDto = this.commentService.createComment(commentDto, userId, postId);
		return new ResponseEntity<CommentDto>(savedcommentDto,HttpStatus.CREATED);
	}
	
	@GetMapping("/comments")
	public ResponseEntity<CommentResponse> getAllComments(@RequestParam(value = "pageNo" , defaultValue = AppConstants.DEFAULT_PAGE_NUMBER , required = false) Integer pageNo,
														   @RequestParam(value = "pageSize" , defaultValue = AppConstants.DEFAULT_PAGE_SIZE , required = false) Integer pageSize,
														   @RequestParam(value = "sortBy" , defaultValue = "id" , required = false) String sortBy,
														   @RequestParam(value = "sortDir" , defaultValue = AppConstants.DEFAULT_SORT_DIRECTION , required = false) String sortDir){
		CommentResponse allCommentsResponse = this.commentService.getAllComments(pageNo,pageSize,sortBy,sortDir);
		return new ResponseEntity<CommentResponse>(allCommentsResponse , HttpStatus.OK);
	}
	
	@GetMapping("/user/{userId}/comments")
	public ResponseEntity<List<CommentDto>> getUserComments(@PathVariable Integer userId){
		List<CommentDto> allUserComments = this.commentService.getUserComments(userId);
		return new ResponseEntity<List<CommentDto>>(allUserComments , HttpStatus.OK);
	}
	
	@GetMapping("/post/{postId}/comments")
	public ResponseEntity<List<CommentDto>> getPostComments(@PathVariable Integer postId){
		List<CommentDto> allPostComments = this.commentService.getPostComments(postId);
		return new ResponseEntity<List<CommentDto>>(allPostComments , HttpStatus.OK);
	}	
	
	@DeleteMapping("/comments/{commentId}")
	public ResponseEntity<ApiResponse> deleteComment(@PathVariable Integer commentId) {
		this.commentService.deleteComment(commentId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Comment deleted Successfully", true) , HttpStatus.OK);
	}	

	@PutMapping("/comments/{commentId}")
	 public ResponseEntity<CommentDto> updateComment(@RequestBody CommentDto commentDto , @PathVariable Integer commentId){
		 CommentDto updatedComment = this.commentService.updateComment(commentDto, commentId);
		 return new ResponseEntity<CommentDto>(updatedComment,HttpStatus.OK);
	 }
	
	@GetMapping("/comments/search/{keyword}")
	public ResponseEntity<List<CommentDto>> searchCommentsByKeyowrd(@PathVariable String keyword){
		List<CommentDto> result = this.commentService.searchComment(keyword);
		return new ResponseEntity<List<CommentDto>>(result , HttpStatus.OK);
	}
	
}
