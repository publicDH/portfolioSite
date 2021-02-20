package com.prims.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDBRepository extends MongoRepository<User, String> {
	
	public User findByid(String id);
}
