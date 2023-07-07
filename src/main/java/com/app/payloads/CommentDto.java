package com.app.payloads;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

private int id;
	
	@NotEmpty
	@NotBlank
	@Size(min = 3 , message = "content Cannot Be Less Than 3 Characters")
	private String content;
	
	
}
