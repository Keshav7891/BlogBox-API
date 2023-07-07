package com.app.payloads;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

	private int id;
	
	@NotEmpty
	@NotBlank
	@Size(min = 3 , message = "Username Must Be of Minimum 3 characters")
	private String name;
	
	
	@Email(message = "Email Not Valid")
	private String email;
	
	
	@NotEmpty
	@Size(min = 3 , message = "Password Should Be Greater Than 3 Characters")
	private String password;
	
	
	@NotEmpty
	private String about;
	
	
}
