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

import com.app.entities.Comment;
import com.app.entities.Post;
import com.app.entities.User;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.CommentDto;
import com.app.payloads.CommentResponse;
import com.app.repositories.CommentRepo;
import com.app.repositories.PostRepo;
import com.app.repositories.UserRepo;

@Service
public class CommentServiceIMPL implements CommentService{

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private CommentRepo commentRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired 
	private PostRepo postRepo;
	
	private Comment dtoToEntity(CommentDto commentDto) {
		Comment comment = this.modelMapper.map(commentDto, Comment.class);
		return comment;
	}
	
	private CommentDto entityToDto(Comment comment) {
		CommentDto commentDto = this.modelMapper.map(comment, CommentDto.class);
		return commentDto;
	}
	
	
	@Override
	public CommentResponse getAllComments(Integer pageNo , Integer pageSize , String sortBy , String  sortDir) {
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		Page<Comment> foundCommentsPage = this.commentRepo.findAll(pageable);
		List<Comment> foundComments = foundCommentsPage.getContent();
		List<CommentDto> foundCommentsDto = foundComments.stream().map( (comment) -> this.entityToDto(comment) ).collect(Collectors.toList());
		CommentResponse commentResponse = new CommentResponse();
		
		commentResponse.setAllComments(foundCommentsDto);
		commentResponse.setPageSize(foundCommentsPage.getNumber());
		commentResponse.setPageSize(foundCommentsPage.getSize());
		commentResponse.setTotalElements(foundCommentsPage.getTotalElements());
		commentResponse.setTotalPages(foundCommentsPage.getTotalPages());
		commentResponse.setIsLast(foundCommentsPage.isLast());
		return commentResponse;
	}

	@Override
	public List<CommentDto> getUserComments(Integer userId) {
		User user = this.userRepo.findById(userId).orElseThrow( () -> new ResourceNotFoundException("User"," ID ", userId) );
		List<Comment> foundComments = user.getComments();
		List<CommentDto> foundCommentsDto = foundComments.stream().map( (comment) -> this.entityToDto(comment)).collect(Collectors.toList());
		return foundCommentsDto;
	}

	@Override
	public List<CommentDto> getPostComments(Integer postId) {
		Post post = this.postRepo.findById(postId).orElseThrow( () -> new ResourceNotFoundException("Post", " ID ", postId) );
		List<Comment> foundComments = post.getComments();
		List<CommentDto> foundCommentsDto = foundComments.stream().map( (comment) -> this.entityToDto(comment)).collect(Collectors.toList());
		return foundCommentsDto;
	}

	@Override
	public void deleteComment(Integer commentId) {
		this.commentRepo.findById(commentId).orElseThrow( () -> new ResourceNotFoundException("Comment", " ID ", commentId) );
		this.commentRepo.deleteById(commentId);
	}

	@Override
	public CommentDto updateComment(CommentDto commentDto, Integer commentId) {
		Comment comment = this.commentRepo.findById(commentId).orElseThrow( () -> new ResourceNotFoundException("Comment"," ID ", commentId) );
		comment.setContent(commentDto.getContent());
		Comment savedComment = this.commentRepo.save(comment);
		return this.entityToDto(savedComment);
	}

	@Override
	public List<CommentDto> searchComment(String keyword) {
		List<Comment> foundComment = this.commentRepo.searchByTitle("%" + keyword + "%");
		List<CommentDto> foundCommentDto = foundComment.stream().map( comment -> entityToDto(comment) ).collect(Collectors.toList());
		return foundCommentDto;
	}
	
	
	@Override
	public CommentDto createComment(CommentDto commentDto, Integer userId, Integer postId) {
		Comment comment = this.dtoToEntity(commentDto);
		User user = this.userRepo.findById(userId).orElseThrow( () -> new ResourceNotFoundException("User", " ID ", userId) );
		Post post = this.postRepo.findById(postId).orElseThrow( () -> new ResourceNotFoundException("Post", " ID ", postId) );
		comment.setUser(user);
		comment.setPost(post);
		Comment savedComment = this.commentRepo.save(comment);
		return this.entityToDto(savedComment);
	}

}
