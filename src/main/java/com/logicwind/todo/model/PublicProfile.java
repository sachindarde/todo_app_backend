package com.logicwind.todo.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@ToString 
@AllArgsConstructor 
@NoArgsConstructor
@Document
public class PublicProfile {
	
	@Getter @Setter
	private String username;
	
	@Getter @Setter
	private  String name;
	
	@Getter @Setter
	private String imageUrl;
	
	@Getter @Setter
	private  Boolean emailVerified;
	
}
