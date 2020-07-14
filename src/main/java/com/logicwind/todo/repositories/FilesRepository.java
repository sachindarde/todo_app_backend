package com.logicwind.todo.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.transaction.annotation.Transactional;

import com.logicwind.todo.model.FilesEnitity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FilesRepository  extends ReactiveMongoRepository<FilesEnitity, ObjectId> {

	@Query(value = "{ 'username' : ?0 , 'folderSlug' : ?1}")
	Flux<FilesEnitity> findByUsernameAndFolderSlug(String username,String folderSlug);
	
	@Query(value = "{ 'username' : ?0 , 'isImportant' : true}")
	Flux<FilesEnitity> findImportantFiles(String username);
	
	@Query(value = "{ 'username' : ?0 , 'isImportant' : true}",count = true)
	Mono<FilesEnitity> countImportantFiles(String username);
	
	@Query(value = "{ 'username' : ?0 , 'slug' : ?1}")
	Mono<FilesEnitity> findByUsernameAndSlug(String username,String slug); 
	
	@Query(value = "{ 'username' : ?0 , 'folderSlug' : ?1}",delete = true)
	@Transactional
	Mono<?> deleteAllFileInFolder(String username,String folderSlug);
}