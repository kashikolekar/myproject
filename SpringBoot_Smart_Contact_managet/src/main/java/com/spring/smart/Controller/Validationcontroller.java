package com.spring.smart.Controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.spring.smart.Dao.ContactDao;
import com.spring.smart.Dao.UserDao;
import com.spring.smart.entity.Contact;
import com.spring.smart.entity.User;
import com.spring.smart.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class Validationcontroller {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ContactDao contactDao;
	
	@ModelAttribute
	public void AddCommenData(Model m,Principal principal) 
	{
		String username =principal.getName();
		System.out.println(username);
	
		
		//
		User user = this.userDao.getUserByUserName(username);
		
		System.out.println(user);
		
		m.addAttribute("user", user);
	}
	
	
	
	@RequestMapping("/index")
	public String Dashbord(Model m ,Principal principal)
	{
		
		System.out.println("Ok user/index");
		m.addAttribute("title", "User Dash_Bord");
		
		return "normal/user_dashbord";
	}
	
	@RequestMapping("/add-contact")
	public String AddContact(Model m)
	{
		m.addAttribute("title", "Add Contact");
		m.addAttribute("contact", new Contact());
		
		return "normal/add_contact";
	}
	
	
	@PostMapping("/process-contact")
	public String Process(@ModelAttribute @Valid Contact contact,BindingResult result,
			@RequestParam("img") MultipartFile file ,
			Principal principal,HttpSession session)
	{
		
		try {
		String name = principal.getName();
		
		User user = this.userDao.getUserByUserName(name);
		
		
		if(file.isEmpty())
		{
			contact.setImg("profile_photo.jpg");
		}
		else
		{
			contact.setImg(file.getOriginalFilename());
			
			File savefile = new ClassPathResource("static/image").getFile();
			
			Path path = Paths.get(savefile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			
			Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Image Upload");
		}
	
		contact.setUser(user);
		user.getCon().add(contact);
		
		this.userDao.save(user);
		System.out.println("Add data in Data base");
		
	session.setAttribute("message",new Message("you Contact is  Added !! Add new One", "success"));
		
		
		}catch (Exception e) {
			e.fillInStackTrace();
			session.setAttribute("message",new Message(" Somting Went Wrong !! Try Again", "danger"));
			
		}
		
	//	System.out.println("data"+contact);
		return "normal/add_contact";
		
	}
	
	@RequestMapping("/show-contact/{page}")
	public String ShowContact(@PathVariable("page") Integer page, Model m,Principal principal)
	{
		String username = principal.getName();
		
		User user = this.userDao.getUserByUserName(username);
	//	List<Contact> con = user.getCon();
		
		
		m.addAttribute("title", "Show Contact");
		
		Pageable pageRequest = PageRequest.of(page, 3);
		Page<Contact> contactall = this.contactDao.findContactByUser(user.getId(),pageRequest);
		
		m.addAttribute("contact", contactall);
		
		m.addAttribute("currentPage", page);
		m.addAttribute("totalPage", contactall.getTotalPages());
	  return "normal/show_contact";	
	}
	
	@RequestMapping("/{cid}/contact")
	public String ShowContactDetiles(@PathVariable("cid") Integer cid,Model m,Principal principal)
	{
		
		System.out.println(cid);
		
		
		Optional<Contact> contactoptional = this.contactDao.findById(cid);
		
		Contact contact  = contactoptional.get();
		
		
		String str  = principal.getName();
		User user = this.userDao.getUserByUserName(str);
		
		if(user.getId()==contact.getUser().getId())
		{
			m.addAttribute("contact", contact);
			m.addAttribute("title", "Conatct Details ");
		}
		else
		{
			m.addAttribute("title", "error");
		}
		
		
		return "normal/contact-detail";
	}
	
	@GetMapping("/delete/{cid}")
	public String DEleteContact(@PathVariable("cid") Integer cid,Model m,Principal principal,HttpSession session)
	{
		
		
		Optional<Contact> ocontact = this.contactDao.findById(cid);
		
		Contact contact = ocontact.get();
		String str  = principal.getName();
		User user = this.userDao.getUserByUserName(str);
		
		if(user.getId()==contact.getUser().getId())
		{
			//contact.setUser(null);
			
			//contact.getImg();			
			user.getCon().remove(contact);
			this.userDao.save(user);
			
		//	this.contactDao.delete(contact);
			session.setAttribute("message",new Message("conytact Deleted Succefully", "success") );
			
		}
		
		
		
		
		return "redirect:/user/show-contact/0";
	}
	
	@PostMapping("/opencontact/{cid}")  //
	public String UpdateContact( @PathVariable("cid") Integer cid, Model m)
	{
		
		m.addAttribute("title", "update contact");
		
		Optional<Contact> optioncontact = this.contactDao.findById(cid);
		
		Contact contact = optioncontact.get();
		
		m.addAttribute("contact", contact);
		
		return "normal/update_form";
	}
	
	
	@PostMapping("/process-update")
	public String UpdatePRocees(@ModelAttribute @Valid Contact contact,BindingResult result,@RequestParam("img") MultipartFile img ,Model m,HttpSession session,Principal principal)
	{
		System.out.println(contact.getName());
		System.out.println(contact.getCid());
		
		Contact contactold = this.contactDao.findById(contact.getCid()).get();
		
		try {
			
			if(!img.isEmpty())
			{
				
				/*
				 * File deletefile = new ClassPathResource("static/image").getFile(); File file
				 * = new File(deletefile, contactold.getImg()); file.delete();
				 */

				ClassPathResource classpathResource = new ClassPathResource("static/image/" + contactold.getImg());
				  if (classpathResource.exists()) {
		   
				        classpathResource.getFile().delete();
				      
				  }
				File savefile = new ClassPathResource("static/image").getFile();
				
				
				
				Path path = Paths.get(savefile.getAbsolutePath()+File.separator+img.getOriginalFilename());
				
				Files.copy(img.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
			
				
				contact.setImg(img.getOriginalFilename());
				
			}
			else
			{
				contact.setImg(contactold.getImg());
			}
			User user = this.userDao.getUserByUserName(principal.getName());
			
			contact.setUser(user);
			this.contactDao.save(contact);
			
			session.setAttribute("message", new Message("You Contact is Updated","success"));
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		
		return "redirect:/user/"+contact.getCid()+"/contact";
		
	}
	
	@GetMapping("/profile")
	public String YourProfile(Model m)
	{
		m.addAttribute("title", "profile paage");
		return "normal/profile";
	}
	
}
