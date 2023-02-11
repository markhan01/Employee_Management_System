package org.markhan.springboot.thymeleafdemo.controller;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.markhan.springboot.thymeleafdemo.dao.RoleRepository;
import org.markhan.springboot.thymeleafdemo.dao.UserRepository;
import org.markhan.springboot.thymeleafdemo.entity.Role;
import org.markhan.springboot.thymeleafdemo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RegistrationController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	private Logger logger = Logger.getLogger(getClass().getName());

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {

		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@GetMapping("/register")
	public String showMyLoginPage(Model theModel) {

		theModel.addAttribute("User", new User());

		return "registration-form";
	}

	@PostMapping("/processRegistrationForm")
	public ModelAndView processRegistrationForm(
			@Valid @ModelAttribute("User") User theUser, 
			@RequestParam String role,
			BindingResult theBindingResult,
			Model theModel) {

		String userName = theUser.getUsername();
		logger.info("Processing registration form for: " + userName);
		
		Role theRole = roleRepository.findByName(role);
		logger.info("Role selected: " + theRole.getName());

		// form validation
		if (theBindingResult.hasErrors()) {
			return new ModelAndView("registration-form");
		}

		// check the database if user already exists
		Optional<User> existing = userRepository.findUserByUsername(userName);
		if (existing.isPresent()) {
			theModel.addAttribute("User", new User());
			theModel.addAttribute("registrationError", "User name already exists.");

			logger.warning("User name already exists.");
			return new ModelAndView("registration-form");
		}
		
		Set<Role> roles = new HashSet<Role>();
		roles.add(theRole);
		theUser.setPassword(passwordEncoder.encode(theUser.getPassword()));
		theUser.setRoles(roles);
		

		// create user account
		userRepository.save(theUser);

		logger.info("Successfully created user: " + userName);

		return new ModelAndView("registration-success", "User", theUser);
	}
}