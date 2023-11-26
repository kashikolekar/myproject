package com.spring.smart.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.smart.Dao.UserDao;
import com.spring.smart.entity.User;
import com.spring.smart.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeContoller {
	@Autowired
	private UserDao dao;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@RequestMapping("/")
	public String Home(Model m)
	{
		m.addAttribute("title","Home Kashinath - smart contact");
		return "home";
	}
	
	
	@RequestMapping("/about")
	public String About(Model m)
	{
		m.addAttribute("title","About Kashinath - smart contact");
		return "about";
	}
	
	@RequestMapping("/singup")
	public String SingUp(Model m)
	{
		m.addAttribute("title","Register Kashinath - smart contact");
		m.addAttribute("user", new User());
		return "singup";
	}
	
	@PostMapping("/data")
	public String SingupDat(@Valid @ModelAttribute("user") User user,BindingResult result,@RequestParam(value ="agreement" ,defaultValue = "false") boolean agreement,Model m,HttpSession session)		
	{
		try {
			
			
			
			if(!agreement)
			{
				System.out.println("you not agree terms and condition");
				throw new Exception("you not agree terms and condition");
			}
			
			if(result.hasErrors())
			{
				System.out.println(result);
				m.addAttribute("user",user);
				return "singup";
			}
			
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImg("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			System.out.println("Agreement = " + agreement);
			System.out.println("User" + user);
			
			
			User r = dao.save(user);
			
			System.out.println(r);
			
			m.addAttribute("user", new User());
			
				session.setAttribute("message", new Message("Ok done ","alert-success") );
			
				
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();	
			m.addAttribute("user", user);
			session.setAttribute("message", new Message("somthing went Wrong"+e.getMessage(),"alert-danger") );
			
			
		}
		return "singup";
	}
	
	@RequestMapping("/singin")
	public String Login(Model m)
	{
		m.addAttribute("title","Login Kashinath - smart contact");
		return "login";
	}
	
	@RequestMapping("/login_fail")
	public String Login_file()
	{
		return "login_fail";
	}
	
}
