package com.prims.Controller;

import javax.servlet.http.HttpSession;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.prims.Repository.User;

@Controller
public class DashboardController {
	
	@GetMapping("/dashboard/dashboard")
	public String DashHome(HttpSession Session) {
		
		User user = (User)Session.getAttribute("User");
		System.out.println(user.getId());
		
		return "dashboard/dashboard";
	}

}
