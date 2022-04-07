package com.kelem.kelem.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.kelem.kelem.dao.AnswerRepository;
import com.kelem.kelem.dao.ReportedAnswerRepository;
import com.kelem.kelem.dao.UserRepository;
import com.kelem.kelem.model.AnswerModel;
import com.kelem.kelem.model.QuestionModel;
import com.kelem.kelem.model.ReportedAnswerModel;
import com.kelem.kelem.model.UserModel;
import com.kelem.kelem.services.AnswerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ReportedAnswerController {
    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    AnswerService answerService;


    @Autowired
    ReportedAnswerRepository reportedAnswerRepository;
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
    @GetMapping("/reported-answers")
    public String reportedAnswersGet(Model model) {
        Iterable<ReportedAnswerModel> answer= reportedAnswerRepository.findAll();
        List<ReportedAnswerModel> output2 = StreamSupport.stream(answer.spliterator(), false)
                                            .collect(Collectors.toList());
        model.addAttribute("answer2", output2);
        UserModel currentlyLoggedInUser = loggedInUser();
        model.addAttribute("currentlyLoggedInUser", currentlyLoggedInUser);
        return "reported-answers";
    }
    /**
     * The method is used to report a answer that a member(s) have found inappropriate.
     * @param id
     * @return
     */
  
    

    @PostMapping("/report-answer/{id}")
    public String reportedAnswers(@PathVariable String id) {
        UserModel currentlyLoggUserModel = this.loggedInUser();
        AnswerModel answer= answerRepository.findById(Long.parseLong(id)).get();
        QuestionModel questionModel = answer.getQuestion();
        ReportedAnswerModel reportedAnswerModel= new ReportedAnswerModel();
        reportedAnswerModel.setAnswer(answer);
        reportedAnswerModel.setReporter(currentlyLoggUserModel);
        reportedAnswerRepository.save(reportedAnswerModel);
        return String.format("redirect:/%s/all-answers", questionModel.getId()); 
    }

    /**
     * Returns the list of reported answers to the view object. Can only be accessed by the admin.
     * @param model
     */
   

    /**
     * The function, which can only be called by an admin, removes a reported answer from the list of reported answers. If the admin resolves it, that is.
     * @param id
     * @return
     */
    @PostMapping("/resolve-report/{id}")
    public String resolve(@PathVariable String id) {
        ReportedAnswerModel reported = reportedAnswerRepository.findById(Long.parseLong(id)).get();
        AnswerModel reportedAnswer= reported.getAnswer();
        UserModel user = reportedAnswer.getUser();
        // reportedAnswerRepository.delete(reported);
        userRepository.delete(user);
        return "redirect:/admin";
    }

   /**
    * The function, which can only be called by an admin, removes a reported answer from the list of reported answers. If the admin dismisses it, that is. The difference from the upper function being that in the upper function the admin will also remove the user from the list of users as they will be removed from the system. 
    * @param id
    * @return
    */ 
    @PostMapping("/dismiss-report/{id}")
    public String deleteReport(@PathVariable String id) {
        ReportedAnswerModel reported = reportedAnswerRepository.findById(Long.parseLong(id)).get();
        reportedAnswerRepository.delete(reported);
        return "redirect:/admin/";
    }


}
