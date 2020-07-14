package com.logicwind.todo.repositories;


import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.logicwind.todo.model.User;
import reactor.core.publisher.Mono;
public interface UserRepository extends ReactiveMongoRepository<User, ObjectId> {
  Mono<User> findByUsername(String username);
  Mono<Boolean> existsByUsername(String username);  
}