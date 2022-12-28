package com.example.crudapp.service;

import java.util.List;
import java.util.Map;

import com.example.crudapp.dto.UserDTO;

public interface UserService {

	public UserDTO saveEntity(UserDTO userDto,String accessToken);

	public List<UserDTO> getAllUsers();

	public Object getUserById(String userId);
	
	public UserDTO updateUser(UserDTO userDto, String userId);
	
	public Map<String, Boolean> deleteUser(String userId);

}
