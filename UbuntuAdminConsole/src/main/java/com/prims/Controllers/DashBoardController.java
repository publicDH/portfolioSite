package com.prims.Controllers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.prims.Repository.FileData;
import com.prims.Repository.User;
import com.prims.Service.FileServices;
import com.prims.Service.SystemUsageServices;

@Controller
public class DashBoardController {

	private final FileServices fileServices;
	private final SystemUsageServices SystemServices;

	@Autowired
	public DashBoardController(FileServices fileServices, SystemUsageServices SystemServices) {
		this.fileServices = fileServices;
		this.SystemServices = SystemServices;
	}
	
	@GetMapping("/dashboard/shutdownSystem")
	public void shutdownSystem(HttpSession Session) throws IOException {
		
		User user = (User) Session.getAttribute("User");
		if(user.getIsAdmin() == true) {
			
			String shutdownCommand = null;
		    String operatingSystem = System.getProperty("os.name");

		    if (operatingSystem.startsWith("Win")) {
		    	  shutdownCommand = "shutdown.exe -s -t 0";
		    	} else if (operatingSystem.startsWith("Linux") || operatingSystem.startsWith("Mac")) {
		    	  shutdownCommand = "shutdown -h now";
		    	} else {
		    	  System.err.println("Shutdown unsupported operating system ...");
		    	}

		    Runtime.getRuntime().exec(shutdownCommand);
		}
	}
	
	@GetMapping("/dashboard/restartSystem")
	public void restartSystem(HttpSession Session) throws IOException {
		
		User user = (User) Session.getAttribute("User");
		if(user.getIsAdmin() == true) {
			
			String shutdownCommand = null;
		    String operatingSystem = System.getProperty("os.name");

		    if (operatingSystem.startsWith("Win")) {
		    	  shutdownCommand = "shutdown.exe -r -t 0";
		    	} else if (operatingSystem.startsWith("Linux") || operatingSystem.startsWith("Mac")) {
		    	  shutdownCommand = "shutdown -r now";
		    	} else {
		    	  System.err.println("Shutdown unsupported operating system ...");
		    	}

		    Runtime.getRuntime().exec(shutdownCommand);
		}
	}
	
	@RequestMapping("/dashboard/upload")
	public String fileupload(HttpServletRequest request, @RequestBody List<MultipartFile> files, HttpSession Session){
	    try{
	        for(int i=0;i<files.size();i++){
	            files.get(i).transferTo(new File(Session.getAttribute("CurrentPath") + fileServices.getDirectorySplit() + files.get(i).getOriginalFilename()));
	        }
	    }catch (IllegalStateException | IOException e){
	        e.printStackTrace();
	    }
	    return "redirect:/dashboard/filesystem";
	}
	
	@GetMapping("/dashboard/deletefile")
	public String fileDelete(@RequestParam(value="fileName")String fileName, HttpSession Session) throws IOException {
		String path = Session.getAttribute("CurrentPath") + fileServices.getDirectorySplit() + fileName;
		fileServices.deleteFile(path);
		
	    return "redirect:/dashboard/filesystem";
	}
	
	@GetMapping("/dashboard/download")
	public ResponseEntity<Resource> download(@RequestParam(value="fileName")String fileName, HttpSession Session) throws IOException {
		Path path = Paths.get(Session.getAttribute("CurrentPath") + fileServices.getDirectorySplit() + fileName);
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
		
		String newDir = (String) Session.getAttribute("CurrentPath") + fileServices.getDirectorySplit() + Path;
		Session.setAttribute("CurrentPath",newDir);
		
		return "redirect:/dashboard/filesystem";
		
	}
	
	@GetMapping("/dashboard/redirect/home")
	public String DashRedirectHome(HttpSession Session) {
		
		Session.setAttribute("CurrentPath",((User) Session.getAttribute("User")).getDirectory());
		
		return "redirect:/dashboard/filesystem";
		
	}
	
	@GetMapping("/dashboard/redirect/back")
	public String DashRedirectBack(HttpSession Session) {
		
		String newDir = (String) Session.getAttribute("CurrentPath");
		String[] tempDir = newDir.split(fileServices.getDirectorySplitDouble());
		newDir = newDir.replace(fileServices.getDirectorySplit() + tempDir[tempDir.length-1], "") ;
		Session.setAttribute("CurrentPath",newDir);
		
		return "redirect:/dashboard/filesystem";
		
	}
	
	@GetMapping("/dashboard/filesystem")
	public ModelAndView DashFileSystem(ModelAndView mv, HttpSession Session) {
		
		List<FileData> FileLists;
		

		User user = (User) Session.getAttribute("User");

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
		String Temp[] = Path.split(fileServices.getDirectorySplitDouble());
		mv.addObject("CurrentPath", Temp[Temp.length-1]);
		if(user.getDirectory().equals(Path))
			mv.addObject("isHome", 1);
		else
			mv.addObject("isHome", 0);
		
		mv.addObject("isAdmin", user.getIsAdmin());
		
		mv.setViewName("filesystem");

		return mv;
	}
	
	@GetMapping("/dashboard")
	public String DashHome(HttpSession Session) {
		
		User user = (User) Session.getAttribute("User");
		
		if(user.getIsAdmin() == true) {
			
			return "redirect:/dashboard/home";
			
		}else {

			return "redirect:/dashboard/filesystem";
		
		}
	}
	
	@GetMapping("/dashboard/home")
	public ModelAndView DashHome2(ModelAndView mv, HttpSession Session) {
		
		User user = (User) Session.getAttribute("User");
		mv.addObject("isAdmin", user.getIsAdmin());

		mv.setViewName("home");

		return mv;
	}

	@GetMapping("/dashboard/usage")
	public ModelAndView DashUsage(ModelAndView mv, HttpSession Session) {
		

		User user = (User) Session.getAttribute("User");
		
		String[] disk = SystemServices.getDiskSpace();
		Map<String, Integer> cpu = new HashMap<String, Integer>();
		int cpuLoad = SystemServices.getCPULoad();
		cpu.put("cpu", cpuLoad);
		
		Long UsedDisk = (long) (Long.parseLong(disk[0]) - Long.parseLong(disk[1]));
		
		Double UsableRam = ((double) SystemServices.getTotalRam() - (double) SystemServices.getRamLoad()) / 1073741824;
		Double UsedRam = (double) SystemServices.getRamLoad() / 1073741824;
		Double UsableDisk = Double.parseDouble(disk[1]) / 1024;
		Double UsingDisk = ((double) UsedDisk / 1024);
		
		Map<String, Double> usage = new HashMap<String, Double>();
		usage.put("UsableRam", UsableRam);
		usage.put("UsedRam", UsedRam);
		usage.put("UsableDisk", UsableDisk);
		usage.put("UsedDisk", UsingDisk);
		
		Map<String, Integer> Percentage = new HashMap<String, Integer>();
		Percentage.put("cpu", cpuLoad);
		Percentage.put("ram", Integer.parseInt(String.valueOf(Math.round((UsedRam / (UsedRam + UsableRam)) * 100))));
		Percentage.put("disk", Integer.parseInt(String.valueOf(Math.round((UsingDisk / (UsingDisk + UsableDisk)) * 100))));
		
		
		mv.addObject("cpuUsage", cpu);
		mv.addObject("usage", usage);
		mv.addObject("percentage", Percentage);
		mv.addObject("isAdmin", user.getIsAdmin());
		
		
		mv.setViewName("usage");

		return mv;
	}
	
}
