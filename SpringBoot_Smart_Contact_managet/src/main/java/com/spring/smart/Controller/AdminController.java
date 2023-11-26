package com.spring.smart.Controller;

import org.springframework.stereotype.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/Admin")
public class AdminController {

	
	@RequestMapping("/loginadmin")
	public String LoginAdmin()
	{
		return "admin/adminLogin";
	}
	
	
	
	
}
