package com.laurenmorgan.loginandreg.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.laurenmorgan.loginandreg.models.LoginUser;
import com.laurenmorgan.loginandreg.models.User;
import com.laurenmorgan.loginandreg.services.UserService;

@Controller
public class LoginController {
	  
    // Add once service is implemented:
     @Autowired
     private UserService userServ;
    
     @GetMapping("/")
     public String index(Model model) {
     
         // Bind empty User and LoginUser objects to the JSP
         // to capture the form input
         model.addAttribute("newUser", new User());
         model.addAttribute("newLogin", new LoginUser());
         return "index.jsp";
     }
     
     @PostMapping("/register")
     public String register(@Valid @ModelAttribute("newUser") User newUser, 
             BindingResult result, Model model, HttpSession session) {
         
         // TO-DO Later -- call a register method in the service 
         // to do some extra validations and create a new user!
         
    	 User user = userServ.register(newUser, result);
    	 
         if(user == null) {
             // Be sure to send in the empty LoginUser before 
             // re-rendering the page.
             model.addAttribute("newLogin", new LoginUser());
             
             return "index.jsp";
         } else {
        	 session.setAttribute("userId", user.getId());
        	 
        	 return "redirect:/home";
         }
         
         // No errors! 
         // TO-DO Later: Store their ID from the DB in session, 
         // in other words, log them in.
     
     }
     
     @GetMapping("/home")
     public String nextPage(Model model) {
     
     
         return "newPage.jsp";
     }
     
     
     @PostMapping("/login")
     public String login(@Valid @ModelAttribute("newLogin") LoginUser newLogin, 
             BindingResult result, Model model, HttpSession session) {
         
         // Add once service is implemented:
          User user = userServ.login(newLogin, result);
     
         if(result.hasErrors()) {
             model.addAttribute("newUser", new User());
             return "index.jsp";
         }
        
         
     
         // No errors! 
         // TO-DO Later: Store their ID from the DB in session, 
         // in other words, log them in.
     
         return "redirect:/home";
     }
     
 }