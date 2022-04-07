package com.kelem.kelem.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.kelem.kelem.dao.QuestionRepository;
import com.kelem.kelem.dao.ReportedQuestionRepository;
import com.kelem.kelem.dao.UserRepository;
import com.kelem.kelem.model.QuestionModel;
import com.kelem.kelem.model.ReportedQuestionModel;
import com.kelem.kelem.model.UserModel;
import com.kelem.kelem.services.QuestionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ReportedQuestionController {
    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    QuestionService questionService;


    @Autowired
    ReportedQuestionRepository reportedQuestionRepository;
    /**
     * The function returns the currently logged in user - aka The Principal.
     * @return
     */
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
    /**
     * The method is used to report a question that a member(s) have found inappropriate.
     * @param id
     * @return
     */
    @PostMapping("/report-question/{id}")
    public String reportedQuestions(@PathVariable String id) {
        UserModel currentlyLoggUserModel = this.loggedInUser();
        QuestionModel q = questionRepository.findById(Long.parseLong(id)).get();
        ReportedQuestionModel reportedQuestionModel= new ReportedQuestionModel();
        reportedQuestionModel.setQuestion(q);
        reportedQuestionModel.setReporter(currentlyLoggUserModel);
        reportedQuestionRepository.save(reportedQuestionModel);
        return "redirect:/all-questions";
    }

    /**
     * Returns the list of reported questions to the view object. Can only be accessed by the admin.
     * @param model
     */
    @GetMapping("/reported-questions")
    public String reportedQuestionsGet(Model model) {
        Iterable<ReportedQuestionModel> q = reportedQuestionRepository.findAll();
        List<ReportedQuestionModel> output2 = StreamSupport.stream(q.spliterator(), false)
                                            .collect(Collectors.toList());
        UserModel currentlyLoggedInUser = loggedInUser();
        model.addAttribute("currentlyLoggedInUser", currentlyLoggedInUser);
        model.addAttribute("q", output2);
        return "reported-questions";
    }

    /**
     * The function, which can only be called by an admin, removes a reported question from the list of reported questions. If the admin resolves it, that is.
     * @param id
     * @return
     */
    @PostMapping("/ques-resolve-report/{id}")
    public String resolve(@PathVariable String id) {
        ReportedQuestionModel reported = reportedQuestionRepository.findById(Long.parseLong(id)).get();
        QuestionModel reportedQuestion = reported.getQuestion();
        UserModel user = reported.getQuestion().getUser();
        reportedQuestionRepository.delete(reported);
        questionRepository.delete(reportedQuestion);
        userRepository.delete(user);
        
        return "redirect:/admin";
    }

   /**
    * The function, which can only be called by an admin, removes a reported question from the list of reported questions. If the admin dismisses it, that is. The difference from the upper function being that in the upper function the admin will also remove the user from the list of users as they will be removed from the system. 
    * @param id
    * @return
    */ 
    @PostMapping("/ques-dismiss-report/{id}")
    public String deleteReport(@PathVariable String id) {
        ReportedQuestionModel reported = reportedQuestionRepository.findById(Long.parseLong(id)).get();
        reportedQuestionRepository.delete(reported);
        return "redirect:/admin";
    }


}
