package com.example.crudapp.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.crudapp.entites.UserEntity;

@Repository
public interface UserRepoistory   extends MongoRepository<UserEntity, String>{

}
