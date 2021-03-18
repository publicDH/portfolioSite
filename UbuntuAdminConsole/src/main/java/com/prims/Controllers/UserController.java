package com.prims.Controllers;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.prims.Repository.User;
import com.prims.Service.FileServices;
import com.prims.Service.UserService;

@Controller
public class UserController {
	
	private final UserService userService;
	private final FileServices fileService;
	
	@Autowired
	public UserController(UserService userService, FileServices fileService) {
		this.userService = userService;
		this.fileService = fileService;
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
			if(newone.get().getIsAdmin() == true) {
				return "redirect:/dashboard";
			}else
			{
				return "redirect:/dashboard/filesystem";
			}
		}
		else
			return "loginFailed";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		
        session.invalidate();
		
		return "redirect:/";
	}
	
	@PostMapping("/createUser")
	public String createUser(User user) {
		String newUserDirectory = fileService.getMasterDirectory() + fileService.getDirectorySplit() + user.getId();
		fileService.makeDirectory(newUserDirectory);
		user.setDirectory(newUserDirectory);
		userService.joinUser(user);
		return "redirect:/dashboard/home";
	}
	
	@PostMapping("/deleteUser")
	public String DeleteUser(User user) {
		Optional<String> userpath = userService.getUserDirectoryById(user.getId());
		if(userpath != null)
			fileService.deleteForder(userpath.get());
		userService.deleteUserById(user.getId());
		return "redirect:/dashboard/home";
	}

}
