package com.app.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.entities.Post;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.ApiResponse;
import com.app.payloads.PostDto;
import com.app.payloads.PostResponse;
import com.app.repositories.PostRepo;
import com.app.services.FileService;
import com.app.services.PostService;
import com.app.utils.AppConstants;

import jakarta.servlet.http.HttpServletResponse;



@RestController
@RequestMapping("/api/")
public class PostController {
	
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private PostRepo postRepo;
	
	@Value("${project.image}")
	private String path;
	
	
	
	@PostMapping("/user/{userId}/category/{categoryId}/posts")
	public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto , @PathVariable Integer categoryId , @PathVariable Integer userId){	
		PostDto savedPost = this.postService.createPost(postDto, userId, categoryId);
		return new ResponseEntity<>(savedPost , HttpStatus.CREATED);
	}
	
	
	
	@GetMapping("/posts/{postId}")
	public ResponseEntity<PostDto> getPostById(@PathVariable Integer postId) {
		System.out.println("Hello");
		PostDto postDto = this.postService.getPostById(postId);
		return new ResponseEntity<PostDto>(postDto,HttpStatus.OK);
	}
	
	
	@GetMapping("/user/{userId}/posts")
	public ResponseEntity<List<PostDto>> getUserPosts(@PathVariable Integer userId){
		List<PostDto> userPosts = this.postService.getUserPosts(userId);
		return new ResponseEntity<List<PostDto>>(userPosts,HttpStatus.OK);
	}
	
	
	@GetMapping("/category/{categoryId}/posts")
	public ResponseEntity<List<PostDto>> getCategoryPosts(@PathVariable Integer categoryId){
		List<PostDto> categoryPosts = this.postService.getCategoryPosts(categoryId);
		return new ResponseEntity<List<PostDto>>(categoryPosts,HttpStatus.OK);
	}
	
	@GetMapping("/posts")
	public ResponseEntity<PostResponse> getAllPosts(@RequestParam(value = "pageNo" , defaultValue = AppConstants.DEFAULT_PAGE_NUMBER , required = false) Integer pageNo,
													@RequestParam(value = "pageSize" , defaultValue = AppConstants.DEFAULT_PAGE_SIZE , required = false) Integer pageSize,
													@RequestParam(value = "sortBy" , defaultValue = "postId" , required = false) String sortBy,
													@RequestParam(value = "sortDir" , defaultValue = AppConstants.DEFAULT_SORT_DIRECTION , required = false) String sortDir){
		PostResponse allPostsResponse = this.postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
		return new ResponseEntity<PostResponse>(allPostsResponse , HttpStatus.OK);
	}
	
	
	@DeleteMapping("/posts/{postId}")
	public ResponseEntity<ApiResponse> deletePost(@PathVariable Integer postId) {
		this.postService.deletePost(postId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Post Deleted Successfully", true),HttpStatus.OK);
	}
	
	
	@PutMapping("/posts/{postId}")
	public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable Integer postId) {
		PostDto updatePost = this.postService.updatePost(postDto, postId);
		return new ResponseEntity<PostDto>(updatePost, HttpStatus.OK);

	}
	
	
	@GetMapping("/posts/search/{keywords}")
	public ResponseEntity<List<PostDto>> searchPostByTitle(@PathVariable String keywords) {
		List<PostDto> result = this.postService.searchPostByTitle(keywords);
		return new ResponseEntity<List<PostDto>>(result, HttpStatus.OK);
	}
	
	
	@PostMapping("/posts/{postId}/image/upload")
	public ResponseEntity<PostDto> uploadImage(@RequestParam("image") MultipartFile image , @PathVariable Integer postId) throws IOException{
		PostDto postDto = this.postService.getPostById(postId);
		String fileName = this.fileService.uploadImage(path, image);
		postDto.setImageName(fileName);
		PostDto updatedPost = this.postService.updatePost(postDto, postId);
		return new ResponseEntity<PostDto>(updatedPost,HttpStatus.OK);
	}
	
	@GetMapping(value = "/posts/{postId}/image", produces = MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage(@PathVariable Integer postId , HttpServletResponse response) throws IOException{
		Post post = this.postRepo.findById(postId).orElseThrow( () -> new ResourceNotFoundException("Post", " ID ", postId) );
		String imageName = post.getImageName();
		InputStream resource = this.fileService.getResource(path, imageName);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
		
	}
	
}
