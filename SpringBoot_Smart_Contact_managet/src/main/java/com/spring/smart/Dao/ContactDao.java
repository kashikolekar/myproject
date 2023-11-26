package com.spring.smart.Dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spring.smart.entity.Contact;

public interface ContactDao extends JpaRepository<Contact, Integer> {

	
	//pegination...
	@Query("from Contact as c where c.user.id =:id")//Current page ,Contact par Oage information of pageable
	public Page<Contact> findContactByUser(@Param("id") int id,Pageable pageable);
	
	
}
