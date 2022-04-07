package com.kelem.kelem.controller;

import java.util.List;

import javax.validation.Valid;

import com.kelem.kelem.dao.AnswerRepository;
import com.kelem.kelem.dao.QuestionRepository;
import com.kelem.kelem.dao.ReportedQuestionRepository;
import com.kelem.kelem.dao.TagRepositiory;
import com.kelem.kelem.dao.UserRepository;
import com.kelem.kelem.model.QuestionModel;
import com.kelem.kelem.model.TagModel;
import com.kelem.kelem.model.UserModel;
import com.kelem.kelem.model.QuestionModel.Status;
import com.kelem.kelem.services.QuestionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class QuestionController {
    
    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    QuestionService questionService;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    ReportedQuestionRepository reportedQuestionRepository;
    
    @Autowired
    TagRepositiory tagRepo;

    
    /**
     * Returns the page which the users will be able to add question.
     * @param model
     * @return
     */
    @GetMapping("/add-question")
    public String fillQuestion(Model model) {
        UserModel currentlyLoggedInUser = loggedInUser();
        List<TagModel> tag = (List<TagModel>) tagRepo.findAll();
        model.addAttribute("question", new QuestionModel());
        model.addAttribute("tag", tag);
        model.addAttribute("currentlyLoggedInUser", currentlyLoggedInUser);
        return "add-question-page";
    }

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
     * Method with which user raises question.
     * @param question
     * @return
     */
    @PostMapping("/add-question")
    public String addQuestion(@Valid @ModelAttribute("question") QuestionModel question, Errors errors, Model model) {
        UserModel currentlyLoggedInUser = this.loggedInUser();

        // checks if there are errors in validation
        if (null != errors && errors.getErrorCount() > 0) {
            List<TagModel> tag = (List<TagModel>) tagRepo.findAll();
            model.addAttribute("question", question);
            model.addAttribute("tag", tag);
            model.addAttribute("currentlyLoggedInUser", currentlyLoggedInUser);
            return "add-question-page";
        }
        question.setUser(currentlyLoggedInUser);
        question.setStatus(Status.UNANSWERED.name());
        questionRepository.save(question);
        return "redirect:/all-questions";
    }

    /**
     * The get method for the form with which user is able to update their prior questions.
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/update-question/{id}")
    public String updateQuestion(@PathVariable String id, Model model) {

        UserModel currentlyLoggedInUser = loggedInUser();
        System.out.println(currentlyLoggedInUser);
        QuestionModel questionModel = questionRepository.findById(Long.parseLong(id)).get();
        if (currentlyLoggedInUser.getId() == questionModel.getUser().getId()) {
            model.addAttribute("question", questionModel);
            model.addAttribute("currentlyLoggedInUser", currentlyLoggedInUser);
            return "update-question";
        }
            return "error";
        
    }

    /**
     * This is where the user updates a question they have previously raised.
     * @param id
     * @param question
     * @return
     */
    @PostMapping("/update-question/{id}")
    public String updateQuestionP(@PathVariable String id, @Valid @ModelAttribute("question") QuestionModel question, Errors errors, Model model) {

        UserModel currentlyLoggedInUser = this.loggedInUser();
        QuestionModel questionModel = questionRepository.findById(Long.parseLong(id)).get();

        // checks if there are errors in validation
        if (null != errors && errors.getErrorCount() > 0) {
            List<TagModel> tag = (List<TagModel>) tagRepo.findAll();
            model.addAttribute("question", question);
            model.addAttribute("currentlyLoggedInUser", currentlyLoggedInUser);
            return "update-question";
        }

        if (currentlyLoggedInUser.getId() == questionModel.getUser().getId()) {
            // model.addAttribute("question", questionModel);
            questionModel.setTopic(question.getTopic());
            // questionModel.setDescription(question.getDescription());
            questionModel.setContent(question.getContent());
            questionRepository.save(questionModel);
            return "redirect:/all-questions";
        }
            return "error";
        
    }

    /**
     * lists all the questions in the system.
     * @param model
     * @return
     */
    @GetMapping("/all-questions")
    public String allQuestions(Model model, String keyTag, String keyWord) {
        List<QuestionModel> q = questionService.allQuestions();
        List<TagModel> tags = (List<TagModel>) tagRepo.findAll();
        UserModel currentlyLoggedInUser = this.loggedInUser();
        model.addAttribute("currentlyLoggedInUser", currentlyLoggedInUser);
        if(keyTag != null){
            List<TagModel> tagModel = tagRepo.findByTag(keyTag);
            model.addAttribute("q", q);
            model.addAttribute("tag", tagModel);
        }else if(keyWord != null){
            List<QuestionModel> questionModel = questionRepository.findByContent(keyWord);
            model.addAttribute("q", questionModel);
            model.addAttribute("tag", tags);
        }
        else{
            model.addAttribute("q", q);
            model.addAttribute("tag", tags);
        }
        return "all-question";
    }
    /**
     * delete a question questions from the system.
     * @param model
     * @return
     */

    @GetMapping("/delete-question/{id}")
    public String deleteQuestion(@PathVariable Long id) {
        questionService.delete(id);
        return "redirect:/all-questions";
    }
    
}