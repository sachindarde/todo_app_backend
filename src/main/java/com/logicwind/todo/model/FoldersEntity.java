package com.logicwind.todo.model;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
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
public class FoldersEntity{

	@Id
	@Getter @Setter
	private ObjectId _id;
	
	@Getter @Setter
	private String slug;
	
	@Getter @Setter
	private String title;
	
	@Getter @Setter
	private String username;
	
	@Getter @Setter
	private String colorCode;
	
	@Getter @Setter
	private Date createdOn;
	
	@Getter @Setter
	private Date updatedOn;
	
}
