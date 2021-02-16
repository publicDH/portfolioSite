package com.prims.Controller;

import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.prims.Repository.FileData;
import com.prims.Repository.User;
import com.prims.Service.FileServices;

@Controller
public class DashboardController {

	private final FileServices fileServices;

	@Autowired
	public DashboardController(FileServices fileServices) {
		this.fileServices = fileServices;
	}
	
	@GetMapping("/dashboard/download")
	public ResponseEntity<Resource> download(@RequestParam(value="fileName")String fileName, HttpSession Session) throws IOException {
		Path path = Paths.get(Session.getAttribute("CurrentPath") + "\\" + fileName);
		String contentType = Files.probeContentType(path);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, contentType);
		headers.setContentDisposition(ContentDisposition.builder("attachment")
		        .filename(fileName, StandardCharsets.UTF_8)
		        .build());

		Resource resource = new InputStreamResource(Files.newInputStream(path));
		return new ResponseEntity<>(resource, headers, HttpStatus.OK);
	}
	
	
	@GetMapping("/dashboard/redirect")
	public String DashRedirect(@RequestParam(value="path")String Path, HttpSession Session) {
		
		String newDir = (String) Session.getAttribute("CurrentPath") + "\\" + Path;
		Session.setAttribute("CurrentPath",newDir);
		
		return "redirect:/dashboard/dashboard";
		
	}
	
	@GetMapping("/dashboard/redirect/home")
	public String DashRedirectHome(HttpSession Session) {
		
		Session.setAttribute("CurrentPath",((User) Session.getAttribute("User")).getDirectory());
		
		return "redirect:/dashboard/dashboard";
		
	}
	
	@GetMapping("/dashboard/redirect/back")
	public String DashRedirectBack(HttpSession Session) {
		
		String newDir = (String) Session.getAttribute("CurrentPath");
		String[] tempDir = newDir.split("\\\\");
		newDir = newDir.replace("\\" + tempDir[tempDir.length-1], "") ;
		Session.setAttribute("CurrentPath",newDir);
		
		return "redirect:/dashboard/dashboard";
		
	}

	@GetMapping("/dashboard/dashboard")
	public ModelAndView DashHome(ModelAndView mv, HttpSession Session) {
		
		List<FileData> FileLists;

		User user = (User) Session.getAttribute("User");
		System.out.println(user.getId());

		String Path = (String) Session.getAttribute("CurrentPath");

		if (Path != null) {
			
			FileLists = fileServices.getFiles(Path);

		} else {
			
			FileLists = fileServices.getFiles(user.getDirectory());
			Session.setAttribute("CurrentPath", user.getDirectory());
			Path = user.getDirectory();
			
		}
		
		Collections.sort(FileLists);
		
		mv.addObject("FileList", FileLists);
		String Temp[] = Path.split("\\\\");
		mv.addObject("CurrentPath", Temp[Temp.length-1]);
		if(user.getDirectory().equals(Path))
			mv.addObject("isHome", 1);
		else
			mv.addObject("isHome", 0);
		
		mv.setViewName("dashboard/dashboard");

		return mv;
	}

}
