package com.kelem.kelem.controller;

import java.util.List;

import com.kelem.kelem.model.AnswerModel;
import com.kelem.kelem.model.DiscussionModel;
import com.kelem.kelem.model.UserModel;
import com.kelem.kelem.dao.AnswerRepository;
import com.kelem.kelem.dao.DiscussionRepository;
import com.kelem.kelem.dao.UserRepository;
import com.kelem.kelem.services.DiscussionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class DiscussionController {
    @Autowired
    DiscussionRepository discussionRepository;
    @Autowired
    DiscussionService service;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    UserRepository userRepository;
    
    // @Autowired
    // discussionService discussionService;
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

    @GetMapping("{id}/discussions-on-answer")
    public String allDiscussionsOnAnAnswer(Model model, @PathVariable String id) {
        AnswerModel answer = answerRepository.findById(Long.parseLong(id)).get();
        List<DiscussionModel> discussion = discussionRepository.findByAnswer(answer);
        model.addAttribute("answer", answer);
        model.addAttribute("discussion", discussion);
        return "after-add-discussion";
    }
    @GetMapping("{id}/add-discussion")
    public String filldiscussion(Model model, @PathVariable String id) {
        AnswerModel answer = answerRepository.findById(Long.parseLong(id)).get();
        model.addAttribute("answer", answer);
        model.addAttribute("discussion", new DiscussionModel());
        return "add-discussion-page";
    }

    @PostMapping("{id}/add-discussion")
    public String addedDiscussion(@PathVariable String id, DiscussionModel discussion, Model model) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        // finding the user from the user database based on the principal's name
        UserModel user = userRepository.findByUsername(username);
        AnswerModel answer = answerRepository.findById(Long.parseLong(id)).get();
        
        discussion.setAnswer(answer);
        discussion.setUser(user);
        service.save(discussion);
        answerRepository.save(answer);
        model.addAttribute("answer", answer);
        model.addAttribute("discussion", discussion);
        return String.format("redirect:/%s/discussions-on-answer", id);
    }
    @GetMapping("update-discussion/{id}")
    public String updateDiscussion(@PathVariable Long id, Model model) {
        UserModel currentlyLoggedInUser = loggedInUser();
        DiscussionModel discussion = discussionRepository.findById(id).get();
        AnswerModel answer = discussion.getAnswer();
        if (currentlyLoggedInUser.getId() == answer.getUser().getId()) {
            model.addAttribute("discussion", discussion);
            return "update-discussion-page";
        }
            return "error";
        
    }
    @PostMapping("update-discussion/{id}")
    public String updatedDiscussion(@PathVariable Long id, DiscussionModel discussion, Model mode){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        // finding the user from the user database based on the principal's name
        Long answerId = discussionRepository.findByDiscussion_id(id);

        UserModel user = userRepository.findByUsername(username);
        AnswerModel answer = answerRepository.findById(answerId).get();
        
        discussion.setAnswer(answer);
        discussion.setUser(user);
        service.save(discussion);
        return String.format("redirect:/%s/discussions-on-answer", answerId);
    }
    
    @GetMapping("/delete-discussion/{id}")
    public String deleteDiscussion(@PathVariable Long id) {
        Long answerId = discussionRepository.findByDiscussion_id(id);
        service.delete(id);
        return String.format("redirect:/%s/discussions-on-answer", answerId);
    }

}