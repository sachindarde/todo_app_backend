package com.logicwind.todo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.logicwind.todo.security.model.Role;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString 
@AllArgsConstructor 
@NoArgsConstructor
@Document
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@Getter @Setter
	private ObjectId _id;
	
	@Email
	@NotNull
	private String username;//Store email address
	
	@NotNull
	@Getter @Setter
	private String name;//User full name
	
	@JsonIgnore
	@NotNull
	private String password;
	
	@Getter @Setter
	private String imageUrl;
	
	@Getter @Setter
	private String verifyCode;
	
	@Getter @Setter
	private Date codeExpire;

	@Getter @Setter
	@NotNull
    private Boolean emailVerified;
	
	@Getter @Setter
	private Boolean enabled;
	
	@Getter @Setter
	private Boolean isActive;

	@NotNull
	@Getter @Setter
    private AuthProvider provider;
	
	@Getter @Setter
	private List<Role> roles;
	
	@Override
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles.stream().map(authority -> new SimpleGrantedAuthority(authority.name())).collect(Collectors.toList());
	}

	@JsonIgnore
	@Override
	public String getPassword() {
		return password;
	}

	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	public User(String username, String password, Boolean enabled, List<Role> roles) {
		super();
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.roles = roles;
	}

}