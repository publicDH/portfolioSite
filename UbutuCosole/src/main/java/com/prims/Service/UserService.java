package com.prims.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prims.Repository.User;
import com.prims.Repository.UserDBRepository;

@Service
public class UserService {

	private final UserDBRepository userDBRepository;
	
	@Autowired
	public UserService(UserDBRepository userDBRepository) {
		this.userDBRepository = userDBRepository;
	}
	
	public Optional<User> login(User user){
		
		User data = userDBRepository.findById(user.getId()).orElse(null);
		//Optional<User> data = userDBRepository.findById(user.getId());
		if(data != null) {
			if(data.getPassword().equals(user.getPassword())){
				return Optional.ofNullable(data);
			}
		}
		return null;
		
	}
	
	public Optional<User> joinUser(User user) {
		validateDuplicateUser(user);
		userDBRepository.insert(user);
		return Optional.ofNullable(user);
	}
	
	private void validateDuplicateUser(User user) {
		userDBRepository.findById(user.getId())
		.ifPresent(m -> {
			throw new IllegalStateException("�̹� �����ϴ� �����Դϴ�.");
		});
	}
	
}
