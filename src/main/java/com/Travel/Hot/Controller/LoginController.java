package com.Travel.Hot.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
	
	@GetMapping("/index")
	public String index() {
		return "login";
	}	
	
	/*@GetMapping("/hello")
	@ResponseBody
	public String Hello(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(!(auth instanceof AnonymousAuthenticationToken))
			return "redirect:/index";
		else
			return "Yooooo!!!!!!!!";
	}*/
	
	@GetMapping("/hello")
	@ResponseBody
	public String Hell() {
		System.out.println("Je suis dans login controller");
		return "Yooooo!!!!!!!!";
	}
	
	@GetMapping("/login")
	@ResponseBody
	public String Log() {
		System.out.println("Je suis dans login controller Login");
		return "NNNNNNNNNNNNNNNNNNNNNNNNNooooo!!!!!!!!";
	}

}
