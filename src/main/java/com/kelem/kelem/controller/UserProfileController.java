package com.kelem.kelem.controller;

import javax.validation.Valid;

import com.kelem.kelem.dao.QuestionRepository;
import com.kelem.kelem.dao.UserRepository;
import com.kelem.kelem.model.UserModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Controller
public class UserProfileController {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    private UserModel loggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        // finding the user from the user database based on the principal's name
        UserModel user = userRepository.findByUsername(username);
        return user;
    }

    @GetMapping("/update-UserProfile/{id}")
    public String updateUserProfile(@PathVariable String id, Model model) {

        UserModel currentlyLoggedInUser = this.loggedInUser();
        UserModel user = userRepository.findById(Long.parseLong(id)).get();
        if (currentlyLoggedInUser.getId() == user.getId()) {
            model.addAttribute("user", user);
            return "update-UserProfile";
        }
        return "error";

    }

    /**
     * This is where the user updates a user profile they have previously raised.
     * 
     * @param id
     * @param user
     * @return
     */
    @PostMapping("/update-userProfile/{id}")
    public String updateUserProfile(@PathVariable String id, @Valid @ModelAttribute("currentlyLoggedInUser") UserModel currentlyLoggedInUser, Errors errors, Model model) {

        // UserModel currentlyLoggedInUser = this.loggedInUser();


        // checks if there are errors in validation
        if (null != errors && errors.getErrorCount() > 0) {
            // Optional<UserModel> user = repo.findById(id);
            model.addAttribute("currentlyLoggedInUser", currentlyLoggedInUser);
            return "current";
        }


        UserModel userModel = userRepository.findById(Long.parseLong(id)).get();
        // we actually don't need this statement
        if (currentlyLoggedInUser.getId() == currentlyLoggedInUser.getId()) {
            // model.addAttribute("user", userModel);
            userModel.setUsername(currentlyLoggedInUser.getUsername());
            userModel.setPassword(currentlyLoggedInUser.getPassword());
            userModel.setFirstName(currentlyLoggedInUser.getFirstName());
            userModel.setLastName(currentlyLoggedInUser.getLastName());

            userRepository.save(userModel);
            return String.format("redirect:/currently-registered-user/%s", id);
        }
        return "error";

    }

    @GetMapping("/delete-userProfile/{id}")
    public String deleteUserProfile(@PathVariable String id) {
        UserModel currentlyLoggedInUser = this.loggedInUser();
        UserModel userModel = userRepository.findById(Long.parseLong(id)).get();
        if (currentlyLoggedInUser.getId() == userModel.getId()) {
            userRepository.delete(userModel);
            return "redirect:/logout";
        }
        else if (currentlyLoggedInUser.getRole().equals("ADMIN")) {
            userRepository.delete(userModel);
            return "redirect:/admin";
        }
        System.out.println(currentlyLoggedInUser.getRole() instanceof String);
        return "redirect:/logout";
    }
}
