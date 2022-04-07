package com.kelem.kelem.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.kelem.kelem.dao.QuizesRepository;
import com.kelem.kelem.dao.UserRepository;
import com.kelem.kelem.model.Quiz;
import com.kelem.kelem.model.UserModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class QuizesController {

    @Autowired
    QuizesRepository quizesRepository;

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

    @GetMapping("/quizes")
    public String getQuizes(Model model) {
        UserModel currentlyLoggedInUser = loggedInUser();
        Iterable<Quiz> q = quizesRepository.findAll();
        List<Quiz> quizes = new ArrayList<Quiz>();
        q.forEach(quizes::add);
        model.addAttribute("quizes", quizes);
        model.addAttribute("currentlyLoggedInUser", currentlyLoggedInUser);
        return "quizes";
    }

    @GetMapping("/add-quiz")
    public String addQuiz(Model model) {
        UserModel currentlyLoggedInUser = loggedInUser();
        model.addAttribute("currentlyLoggedInUser", currentlyLoggedInUser);
        model.addAttribute("quiz", new Quiz());
        return "add-quiz";
    }

    @PostMapping("/add-quiz")
    public String postQuiz(@Valid @ModelAttribute("quiz") Quiz quiz, Errors errors, Model model) {
        // check if the logged in user is acutally an admin.

        // checks if there are errors in validation
        if (null != errors && errors.getErrorCount() > 0) {
            UserModel currentlyLoggedInUser = loggedInUser();
            model.addAttribute("currentlyLoggedInUser", currentlyLoggedInUser);
            model.addAttribute("quiz", quiz);
            return "add-quiz";
        }
        quizesRepository.save(quiz);
        return "redirect:/admin";
    }
}
