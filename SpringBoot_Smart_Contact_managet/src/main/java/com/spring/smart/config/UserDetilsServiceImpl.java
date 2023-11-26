package com.spring.smart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.spring.smart.Dao.UserDao;
import com.spring.smart.entity.User;

public class UserDetilsServiceImpl implements UserDetailsService{

	@Autowired
	private UserDao dao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = dao.getUserByUserName(username);
		if(user==null)
		{
			throw new UsernameNotFoundException("count not fount user");
		}
		
		ConstomDetails c = new ConstomDetails(user);
		
		
		return c;
	}

}
