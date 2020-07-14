package com.logicwind.todo.model;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;

public class FilesEnitity {

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
	private String folderSlug;
	
	@Getter @Setter
	private String contentbody;
	
	@Getter @Setter
	private String folderTitle;
	
	@Getter @Setter
	private Boolean isImportant;
	
	@Getter @Setter
	private Date createdOn;
	
	@Getter @Setter
	private Date updatedOn;
}
