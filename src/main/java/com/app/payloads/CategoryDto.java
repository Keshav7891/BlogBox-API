package com.app.payloads;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

	private Integer categoryId;
	
	@NotBlank
	@Size(min = 3 , message = "Title Cannot Be Less Than 3 Characters")
	private String categoryTitle;
	
	
	@NotBlank
	@Size(min = 4 , message = "Description Cannot Be Less Than 4 Characters")
	private String categoryDescription;
	
}
