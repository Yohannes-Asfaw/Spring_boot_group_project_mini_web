package com.kelem.kelem.controller;

import com.kelem.kelem.dao.UserRepository;
import com.kelem.kelem.model.UserModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @Autowired
    UserRepository userRepository;
    
    /**
     * This method returns the custom login page we designed.
     * @return
     */
    @GetMapping("/login")
    public String login(Model model) {
        UserModel currentlyLoggedInUser = this.loggedInUser();

        if (currentlyLoggedInUser != null) {
            return "redirect:/logout";
        }
        model.addAttribute("userModel", new UserModel());
        return "login";
    }

    private UserModel loggedInUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        
        // finding the user from the user database based on the principal's name
        UserModel user = userRepository.findByUsername(username);
        return user;
    }
}
