package com.prims.Controller;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.prims.Repository.User;
import com.prims.Service.UserService;

@Controller
public class UserController {
	
	private final UserService userService;
	
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/")
	public String home() {
		return "login";
	}
	
	@GetMapping("/login")
	public String loginform() {
		return "login";
	}
	
	@PostMapping("/login")
	public String login(User user, HttpSession session) {
		Optional<User> newone = userService.login(user);
		
		if(newone != null) {
			session.setAttribute("User", newone.get());
			return "redirect:/dashboard/dashboard";
		}
		else
			return "loginFailed";
	}
	
	@GetMapping("/create")
	public String createUser() {
		User user = new User();
		user.setId("test2a");
		user.setDirectory("aa");
		user.setPassword("aa");
		user.setIsAdmin(false);
		userService.joinUser(user);
		return "user/createUserForm";
	}

}
