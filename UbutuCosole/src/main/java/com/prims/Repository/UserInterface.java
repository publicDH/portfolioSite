package com.prims.Repository;

import java.util.Optional;

public interface UserInterface {
	
	User login(User user);
	User create(User user);
	Optional<User> findById(String id);
	
}
