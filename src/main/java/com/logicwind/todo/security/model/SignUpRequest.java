package com.logicwind.todo.security.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString 
@AllArgsConstructor 
@NoArgsConstructor
public class SignUpRequest {
    
	@NotBlank
    @Email
    @Getter @Setter
    private String username;//email address

    @NotBlank
    @Getter @Setter
    private String password;
    
    @NotNull
    @Getter @Setter
	private String name;//User full name
	
	@NotNull
	@Getter @Setter
	private String avatar;//User profile name
}
