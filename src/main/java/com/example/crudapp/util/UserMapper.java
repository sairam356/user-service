package com.example.crudapp.util;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.example.crudapp.dto.UserDTO;
import com.example.crudapp.entites.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {

	public UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
//	@Mapping(target="userId", source="userEntity.id")
	UserDTO userEntityToUserDto(UserEntity userEntity);
//	@Mapping(target="id", source="userDTO.userId") 
	UserEntity userDtoToUserEntity(UserDTO userDTO);
//	@Mapping(target="userId", source="userEntity.id")
	List<UserDTO> userEntityListToUerDTOList(List<UserEntity> userEntity);

}
