package com.app.payloads;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
	
	private Integer postId;
	
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String content;
    
    @FutureOrPresent(message = "Date should be in the future")
    private Date date;
    
    private String imageName;
    
	private CategoryDto category;
	
	private UserDto user;
	
	private List<CommentDto> comments;
}
