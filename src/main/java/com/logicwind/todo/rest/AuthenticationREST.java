package com.logicwind.todo.rest;

import com.logicwind.todo.model.AuthProvider;
import com.logicwind.todo.model.PublicProfile;
import com.logicwind.todo.model.User;
import com.logicwind.todo.repositories.UserRepository;
import com.logicwind.todo.security.JWTUtil;
import com.logicwind.todo.security.PBKDF2Encoder;
import com.logicwind.todo.security.model.AuthRequest;
import com.logicwind.todo.security.model.AuthResponse;
import com.logicwind.todo.security.model.NotAcceptableResponse;
import com.logicwind.todo.security.model.Role;
import com.logicwind.todo.security.model.SignUpRequest;

import java.util.Arrays;
import java.util.Date;

import javax.validation.Valid;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class AuthenticationREST {

	@Autowired
	ReactiveMongoTemplate mongoTemplate;

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private PBKDF2Encoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	

	@RequestMapping(value = "/user/me", method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('USER_FREE','USER_STARTER','USER_DEVELOPER','USER_SUPER')")
	public Mono<ResponseEntity<?>> user(Authentication authentication) {
		if (authentication != null) {
			String username = (String) authentication.getPrincipal();
			return userRepository.findByUsername(username).flatMap((user)->{
				return Mono.just( ResponseEntity.ok().body(new PublicProfile(user.getUsername(),user.getName(),user.getImageUrl(),user.getEmailVerified())));
			});
		}
		else {
			return Mono.just( ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
		}
	}

	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Mono<ResponseEntity<?>> login(@RequestBody AuthRequest ar) {
		return userRepository.findByUsername(ar.getUsername()).map((userDetails) -> {
			if (passwordEncoder.encode(ar.getPassword()).equals(userDetails.getPassword())) {
				return ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(userDetails)));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}
		}).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public Mono<ResponseEntity<?>> signup(@Valid @RequestBody SignUpRequest signUpRequest){
		return userRepository.existsByUsername(signUpRequest.getUsername()).flatMap((isAlreadyUsername)->{
			if(isAlreadyUsername) {
				return userRepository.findByUsername(signUpRequest.getUsername()).flatMap((userDetails)->{
					NotAcceptableResponse acceptableResponse=new NotAcceptableResponse("User Already exist with provide email address.", userDetails.getProvider().toString());
					return Mono.just(ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(acceptableResponse));
				});
			}
			else {
				if(signUpRequest.getName().length()>100) {
					return Mono.just(ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Name is too larger"));
				}
				if(signUpRequest.getPassword().length()>100) {
					return Mono.just(ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Password is too larger"));
				}
				if(signUpRequest.getUsername().length()>100) {
					return Mono.just(ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Name is too larger"));
				}
				User user = new User();
				user.setUsername(signUpRequest.getUsername());
				user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
				user.setName(signUpRequest.getName());
				user.setEnabled(true);
				user.setIsActive(true);
				user.setEmailVerified(false);
				user.setProvider(AuthProvider.local);
				//avtar01//avtar02//avtar03//avtar04
				user.setImageUrl(signUpRequest.getAvatar());
				user.set_id(new ObjectId(new Date()));
				user.setRoles(Arrays.asList(Role.ROLE_USER_FREE));
				return userRepository.save(user).flatMap((userDetails)->{
					return Mono.just(ResponseEntity.ok().body(new AuthResponse(jwtUtil.generateToken(userDetails))));
				});
			}
		});
	}
}
