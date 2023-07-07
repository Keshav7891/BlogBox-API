package com.app.services;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.app.entities.Category;
import com.app.entities.Post;
import com.app.entities.User;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.PostDto;
import com.app.payloads.PostResponse;
import com.app.repositories.CategoryRepo;
import com.app.repositories.PostRepo;
import com.app.repositories.UserRepo;


@Service
public class PostServiceIMPL implements PostService{
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	
	private Post dtoToEntity(PostDto postDto) {
		Post post = this.modelMapper.map(postDto, Post.class);
		return post;
	}
	
	private PostDto entityToDto(Post post) {
		PostDto postDto = this.modelMapper.map(post, PostDto.class);
		return postDto;
	}

	@Override
	public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {
		Post post = this.dtoToEntity(postDto);
		User user = this.userRepo.findById(userId).orElseThrow( ()-> new ResourceNotFoundException("User", "ID", userId) );
		Category category = this.categoryRepo.findById(categoryId).orElseThrow( () -> new ResourceNotFoundException("Category", "ID", categoryId) );
		
		post.setCategory(category);
		post.setUser(user);
		
		post.setDate(new Date());
		post.setImageName("Default.png");
		
		this.postRepo.save(post);
		return this.entityToDto(post);
	}

	@Override
	public PostResponse getAllPosts(Integer pageNo , Integer pageSize , String sortBy, String sortDir) {
		//Pageable pageable = PageRequest.of(pageNo, pageSize);
		
		//Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		
		Page<Post> allPostPage = this.postRepo.findAll(pageable);
		List<Post> allPost = allPostPage.getContent();
		List<PostDto> allPostDto = allPost.stream().map( (post) -> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		
		PostResponse postResponse = new PostResponse();
		postResponse.setAllPosts(allPostDto);
		postResponse.setPageNo(allPostPage.getNumber());
		postResponse.setPageSize(allPostPage.getSize());
		postResponse.setTotalElements(allPostPage.getTotalElements());
		postResponse.setTotalPages(allPostPage.getTotalPages());
		postResponse.setIsLast(allPostPage.isLast());
		
		return postResponse;
	}

	@Override
	public PostDto getPostById(Integer postId) {
		Post post = this.postRepo.findById(postId).orElseThrow( () -> new ResourceNotFoundException("Post", "ID", postId) );
		return this.entityToDto(post);
	}

	@Override
	public void deletePost(Integer postId) {
		this.postRepo.findById(postId).orElseThrow( () -> new ResourceNotFoundException("Post", "ID", postId) );
		this.postRepo.deleteById(postId);
	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) {
		Post post = this.postRepo.findById(postId).orElseThrow( () -> new ResourceNotFoundException("Post", "ID", postId) );
		
		
		post.setContent(postDto.getContent());
		post.setTitle(postDto.getTitle());
		post.setImageName(postDto.getImageName());
		
		Post updatedPost = this.postRepo.save(post);
		
		return this.entityToDto(updatedPost);
	}

	@Override
	public List<PostDto> getUserPosts(Integer userId) {
		User user = this.userRepo.findById(userId).orElseThrow( () -> new ResourceNotFoundException("User", "ID", userId) );
		List<Post> userPosts = this.postRepo.findByUser(user);
		List<PostDto> userPostsDto = userPosts.stream().map( (post) -> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		return userPostsDto;
	}

	@Override
	public List<PostDto> getCategoryPosts(Integer categoryId) {
		Category category = this.categoryRepo.findById(categoryId).orElseThrow( () -> new ResourceNotFoundException("Category", "ID", categoryId) );
		List<Post> categoryPosts = this.postRepo.findByCategory(category);
		List<PostDto> categoryPostsDto = categoryPosts.stream().map( (post) -> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		return categoryPostsDto;
	}

//	@Override
//	public List<PostDto> searchPostByTitle(String keyword) {
//		List<Post> foundPosts = this.postRepo.findAll().stream()
//							    .filter(post -> post.getTitle().toLowerCase().contains(keyword.toLowerCase()))
//							    .collect(Collectors.toList());
//	    List<PostDto> foundPostsDto = foundPosts.stream()
//							    		.map(post -> this.modelMapper.map(post, PostDto.class)).
//							    		collect(Collectors.toList());
//	    return foundPostsDto;
//	}
	
	
	@Override
	public List<PostDto> searchPostByTitle(String keyword) {
		List<Post> foundPosts = this.postRepo.searchByTitle("%" + keyword + "%");
	    List<PostDto> foundPostsDto = foundPosts.stream()
							    		.map(post -> this.modelMapper.map(post, PostDto.class)).
							    		collect(Collectors.toList());
	    return foundPostsDto;
	}
	
	

}
