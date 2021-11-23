package com.egg.labOnline;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping; 

@Controller
public class AdminControlador {

	@GetMapping("/admin")
	public String getLoginPage(Model model) {
	    model.addAttribute("activeLink", "Admin");
	    return "admin/inicio";
	}

}


