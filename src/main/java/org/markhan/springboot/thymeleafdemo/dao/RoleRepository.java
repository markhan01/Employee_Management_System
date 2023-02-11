package org.markhan.springboot.thymeleafdemo.dao;


import java.util.Collection;

import org.markhan.springboot.thymeleafdemo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
	
	public Role findByName(String name);
}
