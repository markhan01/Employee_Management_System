package org.markhan.springboot.thymeleafdemo.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.markhan.springboot.thymeleafdemo.dao.RoleRepository;
import org.markhan.springboot.thymeleafdemo.dao.UserRepository;
import org.markhan.springboot.thymeleafdemo.entity.Role;
import org.markhan.springboot.thymeleafdemo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findUserByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not present"));
		return user;
	}
	
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

}
