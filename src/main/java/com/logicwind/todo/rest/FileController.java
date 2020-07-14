package com.logicwind.todo.rest;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.logicwind.todo.model.FilesEnitity;
import com.logicwind.todo.repositories.FilesRepository;
import com.logicwind.todo.utility.WidgetUtility;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/file")
public class FileController {

	@Autowired
	private FilesRepository filesRepository;
	

	@RequestMapping(value = { "/add", "/add/"},method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('USER_FREE','USER_STARTER','USER_DEVELOPER','USER_SUPER')")
	public Mono<ResponseEntity<?>> save(Authentication authentication,@RequestBody FilesEnitity entity){
		if (authentication != null) {
			String username = (String) authentication.getPrincipal();
			entity.setUsername(username);
			entity.setSlug(WidgetUtility.genrateRandomSlug());
			Date createdOn=new Date();
			entity.setCreatedOn(createdOn);
			entity.setUpdatedOn(createdOn);
			return filesRepository.save(entity).flatMap(update->{
				return Mono.just(ResponseEntity.ok(update));
			});
		}
		else {
			return Mono.just( ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
		}
	}
	
	@RequestMapping(value = { "/update", "/update/"},method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('USER_FREE','USER_STARTER','USER_DEVELOPER','USER_SUPER')")
	public Mono<ResponseEntity<FilesEnitity>> update(Authentication authentication,@RequestBody FilesEnitity entity){
		if (authentication != null) {
			String username = (String) authentication.getPrincipal();
			return filesRepository.findByUsernameAndSlug(username, entity.getSlug()).flatMap(newObject->{
				Date createdOn=new Date();
				entity.set_id(newObject.get_id());
				entity.setUpdatedOn(new Date());
				entity.setUsername(username);
				entity.setIsImportant(newObject.getIsImportant());
				entity.setUpdatedOn(createdOn);
				return filesRepository.save(entity).flatMap(update->{
					return Mono.just(ResponseEntity.ok(update));
				});
			}).defaultIfEmpty(ResponseEntity.notFound().build());
		}
		else {
			return Mono.just( ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
		}
	}

	@RequestMapping(value = { "/delete", "/delete/"},method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('USER_FREE','USER_STARTER','USER_DEVELOPER','USER_SUPER')")
	public Mono<?> delete(Authentication authentication,@RequestParam String slug){
		if (authentication != null) {
			String username = (String) authentication.getPrincipal();
			return filesRepository.findByUsernameAndSlug(username, slug).flatMap(newObject->{
				return filesRepository.delete(newObject).flatMap(mapper->{
					return Mono.just( ResponseEntity.status(HttpStatus.ACCEPTED).build());
				});
			});
		}
		else {
			return Mono.just( ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
		}
	}

	@RequestMapping(value = { "/count/important", "/count/important/"},method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('USER_FREE','USER_STARTER','USER_DEVELOPER','USER_SUPER')")
	public Mono<?> countImp(Authentication authentication){
		if (authentication != null) {
			String username = (String) authentication.getPrincipal();
			return filesRepository.countImportantFiles(username);
		}
		else {
			return Mono.just( ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
		}
	}

	@RequestMapping(value = { "/toogle/important", "/toogle/important/"},method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('USER_FREE','USER_STARTER','USER_DEVELOPER','USER_SUPER')")
	public Mono<?> toogle(Authentication authentication,@RequestParam String slug){
		if (authentication != null) {
			String username = (String) authentication.getPrincipal();
			return filesRepository.findByUsernameAndSlug(username, slug).flatMap(newObject->{
				newObject.setIsImportant(!newObject.getIsImportant());
				return filesRepository.save(newObject).flatMap(mapper->{
					return Mono.just( ResponseEntity.status(HttpStatus.ACCEPTED).build());
				});
			});
		}
		else {
			return Mono.just( ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
		}
	}
	
	@RequestMapping(value = { "/list/important", "/list/important/"},method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('USER_FREE','USER_STARTER','USER_DEVELOPER','USER_SUPER')")
	public Flux<?> listImp(Authentication authentication){
		if (authentication != null) {
			String username = (String) authentication.getPrincipal();
			return filesRepository.findImportantFiles(username);
		}
		else {
			return Flux.just( ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
		}
	}

	@RequestMapping(value = { "/list/folder", "/list/folder/"},method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('USER_FREE','USER_STARTER','USER_DEVELOPER','USER_SUPER')")
	public Flux<?> listFile(Authentication authentication,@RequestParam String folderSlug){
		if (authentication != null) {
			String username = (String) authentication.getPrincipal();
			return filesRepository.findByUsernameAndFolderSlug(username,folderSlug);
		}
		else {
			return Flux.just( ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
		}
	}
	

	@RequestMapping(value = { "/detail", "/detail/"},method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('USER_FREE','USER_STARTER','USER_DEVELOPER','USER_SUPER')")
	public Mono<?> detail(Authentication authentication,@RequestParam String slug){
		if (authentication != null) {
			String username = (String) authentication.getPrincipal();
			return filesRepository.findByUsernameAndSlug(username, slug);
		}
		else {
			return Mono.just( ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
		}
	}
	
}
