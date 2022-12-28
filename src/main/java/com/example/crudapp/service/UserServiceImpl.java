package com.example.crudapp.service;

import java.util.Date;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.crudapp.avro.UserInfo;
import com.example.crudapp.dao.UserRepoistory;
import com.example.crudapp.dto.UserDTO;
import com.example.crudapp.entites.UserEntity;
import com.example.crudapp.util.UserMapper;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper mapper;

	@Autowired
	private  KafkaTemplate<String, UserInfo> template;

	private UserRepoistory userRepoistory;
	


	@Autowired
	public UserServiceImpl(UserRepoistory userRepoistory) {
	
		this.userRepoistory = userRepoistory;

	}

	@Override
	public UserDTO saveEntity(UserDTO userDto, String accessToken) {
		// TODO Auto-generated method stub

		UserEntity userEntity = mapper.userDtoToUserEntity(userDto);
		userEntity.setCreatedDate(new Date());
		Optional<UserEntity> userObj = Optional.ofNullable(userRepoistory.save(userEntity));

		if (userObj.isPresent()) {

			UserDTO userDTO = mapper.userEntityToUserDto(userObj.get());

			UserInfo userInfo = new UserInfo();
			userInfo.setFirstName(userDTO.getFirstName());
			userInfo.setLastName(userDTO.getLastName());
			userInfo.setUsername(userDTO.getUsername());

			
			template.send("user-p", accessToken, userInfo);

			userDTO.setStatus("Success");

			return userDTO;
		} else {

			userDto.setStatus("Failure");
		}

		return userDto;
	}

	@Override
	@Cacheable(value = "users")
	public List<UserDTO> getAllUsers() {

		return mapper.userEntityListToUerDTOList(userRepoistory.findAll());
	}

	@Override
	@Cacheable(value = "users", key = "#userId")
	public Object getUserById(String userId) {
		Map<String, String> map = new HashMap<>();
		Optional<UserEntity> userObj = userRepoistory.findById(userId);

		if (userObj.isPresent()) {

			return mapper.userEntityToUserDto(userObj.get());
		} else {
			map.put("Status", "UserId not Found");

			return map;
		}

	}

	@Override
	@CachePut(value = "users", key = "#userId")
	public UserDTO updateUser(UserDTO userDto, String userId) {
		Optional<UserEntity> userObj = userRepoistory.findById(userId);

		if (userObj.isPresent()) {

			UserEntity dtoToEntity = mapper.userDtoToUserEntity(userDto);

			UserEntity userEntity = userObj.get();
			dtoToEntity.setCreatedDate(userEntity.getCreatedDate());
			dtoToEntity.setLastModifiedDate(new Date());

			UserDTO userDTO = mapper.userEntityToUserDto(userRepoistory.save(dtoToEntity));

			userDTO.setStatus("Success");
			return userDTO;

		} else {

			userDto.setStatus("Unable to Update the status");
			return userDto;
		}

	}

	@Override
	public Map<String, Boolean> deleteUser(String userId) {
		Map<String, Boolean> map = new HashMap<>();
		Optional<UserEntity> userObj = userRepoistory.findById(userId);

		if (userObj.isPresent()) {
			userRepoistory.delete(userObj.get());
			map.put("Deleted", true);
		} else {
			map.put("Deleted", false);
		}
		return map;
	}
	
	/*
	 			Properties props = new Properties();
			props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "pkc-6ojv2.us-west4.gcp.confluent.cloud:9092");
			props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
					"org.apache.kafka.common.serialization.StringSerializer");
			props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
					"io.confluent.kafka.serializers.KafkaAvroSerializer");
			props.setProperty(ProducerConfig.ACKS_CONFIG, "all");
			props.setProperty("schema.registry.url", "https://psrc-mw0d1.us-east-2.aws.confluent.cloud");
			props.setProperty("basic.auth.user.info", "ZGCJFFRLCPYAGNAT:uAIz1tt95GyvcDVNLSTZIkMhGXJZCcTJ/FLQPOUARig0UlUY8paWthMt16jpf5Hy");
			props.setProperty("basic.auth.credentials.source", "USER_INFO");
			props.setProperty("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username='XPGZPJFWVQFDLDV3' password='6NWXB0A1wM1L2kWVUkgkuy/x5mPTiyoECHnBIr3nebo69bIxyAFf7e7qtwaWdW19';");
			props.setProperty("security.protocol", "SASL_SSL");
			props.setProperty("sasl.mechanism", "PLAIN");
			
			
			
			KafkaProducer<String, UserInfo> producer = new KafkaProducer<String, UserInfo>(props);
			ProducerRecord<String, UserInfo> record = new ProducerRecord<>("user-p", userObj.get().getId(), userInfo);

			record.headers().add("access_token", accessToken.toString().getBytes());
			producer.send(record); */

	 
	 

}
