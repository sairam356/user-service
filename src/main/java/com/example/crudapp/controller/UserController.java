package com.example.crudapp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.crudapp.dto.UserDTO;
import com.example.crudapp.service.UserService;

@RestController
@RequestMapping("/users")

public class UserController {

	@Autowired
	UserService userService;
	


	@PostMapping
	public UserDTO saveUser(@RequestBody UserDTO userDto,  @AuthenticationPrincipal Jwt jwt) {
          System.out.println(jwt.getTokenValue());
		return userService.saveEntity(userDto,jwt.getTokenValue());

	}

	@PutMapping("/{id}")
	public UserDTO updateUser(@RequestBody UserDTO userDto, @PathVariable(value = "id") String userId) {

		return userService.updateUser(userDto, userId);
	}

	@GetMapping
	public List<UserDTO> getAllEmployees() {
		return userService.getAllUsers();
	}

	@DeleteMapping("/{id}")
	public Map<String, Boolean> deleteUser(@PathVariable(value = "id") String userId) {

		return userService.deleteUser(userId);
	}

	@GetMapping("/{id}")
	public Object getUserById(@PathVariable(value = "id") String userId) {

		return userService.getUserById(userId);
	}

}
