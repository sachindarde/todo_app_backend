package com.logicwind.todo.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.logicwind.todo.model.FoldersEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FoldersRepository extends ReactiveMongoRepository<FoldersEntity, ObjectId> {

	@Query(value = "{ 'username' : ?0 , 'slug' : ?1}")
	Mono<FoldersEntity> findByUsernameAndSlug(String username,String slug); 
	
	Flux<FoldersEntity> findByUsername(String username);
	
}
