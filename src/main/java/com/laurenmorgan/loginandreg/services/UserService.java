package com.laurenmorgan.loginandreg.services;

import java.util.List;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.laurenmorgan.loginandreg.models.LoginUser;
import com.laurenmorgan.loginandreg.models.User;
import com.laurenmorgan.loginandreg.repositories.UserRepository;

@Service
public class UserService {
	
	 	@Autowired
	    private UserRepository userRepo;
	    
	   
	    public User register(User newUser, BindingResult result) {
	       
	    	// TO-DO - Reject values or register if no errors:
	    	if(result.hasErrors()) {
	    		return null;
	    	}
	    	
	    	// Reject if email is taken (present in database)
	    	Optional<User> potentialUser = userRepo.findByEmail(newUser.getEmail());
	    	
	    	
	    	if(potentialUser.isPresent()) {
	    		result.rejectValue("email", "email", "This email already exists");
	    		return null;
	    	}
	    	// Reject if password doesn't match confirmation
	    	if(!newUser.getPassword().equals(newUser.getConfirm())) {
	    	    result.rejectValue("confirm", "Matches", "The Confirm Password must match Password!");
	    	    return null;
	    	}
	        
	    
	    	String hashed = BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt());
	    	System.out.println(hashed);
	    	
	        // Return null if result has errors
	    	newUser.setPassword(hashed);

	        // Hash and set password, save user to database
	        return userRepo.save(newUser);
	    }
	    
	    
	    public User login(LoginUser newLoginObject, BindingResult result) {
	       
	    	String passwordEntered = newLoginObject.getPassword();
	    	
	    	if(result.hasErrors()) {
	    		return null;
	    	}
	    	 // TO-DO - Reject values:
	        
	    	
	    	// Find user in the DB by email
	    	Optional<User> potentialUser = userRepo.findByEmail(newLoginObject.getEmail());
	    	
	    	// Reject if NOT present
	    	if(!potentialUser.isPresent()) {
	    		result.rejectValue("email", "email", "This user does not exist.");
	    		return null;
	    	}
	    	
	    	if(!newLoginObject.getPassword().equals(newLoginObject.getPassword())) {
	    	    result.rejectValue("confirm", "Matches", "The Confirm Password must match Password!");
	    	    return null;
	    	}
	    	
	    
	    	
	    
	    	
	   
	        // Reject if BCrypt password match fails
	    
	        // Return null if result has errors
	        // Otherwise, return the user object
	        return potentialUser.get();
	    }
	    
	    
	    
	    
	    
	    public List<User> allUsers() {
	        return userRepo.findAll();
	    }

	    public User createOrUpdate(User user) {
	        return userRepo.save(user);
	    }

	    public User findOne(Long id) {
	        Optional<User> optionalUser = userRepo.findById(id);
	        if(optionalUser.isPresent()) {
	            return optionalUser.get();
	        } else {
	            return null;
	        }
	    }
	
	    
	    

}
