package org.markhan.springboot.thymeleafdemo.controller;

import java.util.Optional;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.markhan.springboot.thymeleafdemo.dao.UserRepository;
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

@Controller
public class RegistrationController {

	@Autowired
	private UserRepository userRepository;
	
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
	public String processRegistrationForm(@Valid @ModelAttribute("User") User theUser, BindingResult theBindingResult,
			Model theModel) {

		String userName = theUser.getUsername();
		logger.info("Processing registration form for: " + userName);

		// form validation
		if (theBindingResult.hasErrors()) {
			return "registration-form";
		}

		// check the database if user already exists
		Optional<User> existing = userRepository.findUserByUsername(userName);
		if (existing.isPresent()) {
			theModel.addAttribute("User", new User());
			theModel.addAttribute("registrationError", "User name already exists.");

			logger.warning("User name already exists.");
			return "registration-form";
		}
		
		theUser.setPassword(passwordEncoder.encode(theUser.getPassword()));

		// create user account
		userRepository.save(theUser);

		logger.info("Successfully created user: " + userName);

		return "homepage";
	}
}