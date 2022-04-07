package com.kelem.kelem.controller;

import java.util.Optional;


import com.kelem.kelem.dao.AnswerRepository;
import com.kelem.kelem.dao.UserRepository;
import com.kelem.kelem.dao.VoteRepository;
import com.kelem.kelem.model.AnswerModel;
import com.kelem.kelem.model.UserModel;
import com.kelem.kelem.model.Vote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class VoteController {

    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    UserRepository userRepository;

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
     * This methods handles up-voting of a user
     */
    @PostMapping("/up-vote/{id}")
    public String upVote(@PathVariable String id) {
        UserModel user = this.loggedInUser();
        Optional<AnswerModel> answerModel = answerRepository.findById(Long.parseLong(id));
        Vote userVote = voteRepository.findUserVote(user.getId(), answerModel.get().getId());
        
        if (userVote == null) {
            Vote uVote = new Vote();
            uVote.setUserModel(user);
            uVote.setUpVote(1);
            uVote.setDownVote(0);
            uVote.setAnswerModel(answerModel.get());
            voteRepository.save(uVote);
        } else {
            userVote.setUpVote(1);
            userVote.setDownVote(0);
            voteRepository.save(userVote);
        }
        return String.format("redirect:/%s/all-answers", answerModel.get().getQuestion().getId());
    }


    /**
     * This method handles the down voting of a user.
     * @param id
     * @return
     */
    @PostMapping("/down-vote/{id}")
    public String downVote(@PathVariable String id) {
        UserModel user = this.loggedInUser();
        Optional<AnswerModel> answerModel = answerRepository.findById(Long.parseLong(id));
        Vote userVote = voteRepository.findUserVote(user.getId(), answerModel.get().getId());
        
        if (userVote == null) {
            Vote uVote = new Vote();
            uVote.setUserModel(user);
            uVote.setUpVote(0);
            uVote.setDownVote(-1);
            uVote.setAnswerModel(answerModel.get());
            voteRepository.save(uVote);
        } else {
            userVote.setUpVote(0);
            userVote.setDownVote(-1);
            voteRepository.save(userVote);
        }
        // TODO: check if the user voted up or down.
        return String.format("redirect:/%s/all-answers", answerModel.get().getQuestion().getId());
    }
}
