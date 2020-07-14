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

import com.logicwind.todo.model.FoldersEntity;
import com.logicwind.todo.repositories.FilesRepository;
import com.logicwind.todo.repositories.FoldersRepository;
import com.logicwind.todo.utility.WidgetUtility;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/folder")
public class FolderController {


	@Autowired
	private FoldersRepository folderRepository;

	@Autowired
	private FilesRepository filesRepository;

	@RequestMapping(value = { "/add", "/add/"},method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('USER_FREE','USER_STARTER','USER_DEVELOPER','USER_SUPER')")
	public Mono<ResponseEntity<?>> save(Authentication authentication,@RequestBody FoldersEntity entity){
		if (authentication != null) {
			String username = (String) authentication.getPrincipal();
			entity.setUsername(username);
			entity.setSlug(WidgetUtility.genrateRandomSlug());
			Date createdOn=new Date();
			entity.setCreatedOn(createdOn);
			entity.setUpdatedOn(createdOn);
			return folderRepository.save(entity).flatMap(update->{
				return Mono.just(ResponseEntity.ok(update));
			});
		}
		else {
			return Mono.just( ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
		}
	}

	@RequestMapping(value = { "/update", "/update/"},method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('USER_FREE','USER_STARTER','USER_DEVELOPER','USER_SUPER')")
	public Mono<ResponseEntity<FoldersEntity>> update(Authentication authentication,@RequestBody FoldersEntity entity){
		if (authentication != null) {
			String username = (String) authentication.getPrincipal();
			return folderRepository.findByUsernameAndSlug(username, entity.getSlug()).flatMap(newObject->{
				Date createdOn=new Date();
				entity.set_id(newObject.get_id());
				entity.setUpdatedOn(createdOn);
				return folderRepository.save(entity).flatMap(update->{
					return Mono.just(ResponseEntity.ok(update));
				});
			}).defaultIfEmpty(ResponseEntity.notFound().build());
		}
		else {
			return Mono.just( ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
		}
	}

	@RequestMapping(value = { "/list", "/list/"},method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('USER_FREE','USER_STARTER','USER_DEVELOPER','USER_SUPER')")
	public Flux<?> list(Authentication authentication){
		if (authentication != null) {
			String username = (String) authentication.getPrincipal();
			return folderRepository.findByUsername(username);
		}
		else {
			return Flux.just( ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
		}
	}

	@RequestMapping(value = { "/delete", "/delete/"},method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('USER_FREE','USER_STARTER','USER_DEVELOPER','USER_SUPER')")
	public Mono<?> delete(Authentication authentication,@RequestParam String slug){
		if (authentication != null) {
			String username = (String) authentication.getPrincipal();
			return folderRepository.findByUsernameAndSlug(username, slug).flatMap(newObject->{
				String folderSlug=newObject.getSlug();
				filesRepository.deleteAllFileInFolder(username, folderSlug).subscribe();
				return folderRepository.delete(newObject).flatMap(mapper->{
					return Mono.just( ResponseEntity.status(HttpStatus.ACCEPTED).build());
				});
			});
		}
		else {
			return Mono.just( ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
		}
	}

}
