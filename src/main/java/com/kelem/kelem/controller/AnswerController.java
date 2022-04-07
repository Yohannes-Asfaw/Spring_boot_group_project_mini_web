package com.kelem.kelem.controller;

import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import com.kelem.kelem.dao.AnswerRepository;
import com.kelem.kelem.dao.QuestionRepository;
import com.kelem.kelem.dao.UserRepository;
import com.kelem.kelem.dao.VoteRepository;
import com.kelem.kelem.model.AnswerModel;
import com.kelem.kelem.model.QuestionModel;
import com.kelem.kelem.model.UserModel;
import com.kelem.kelem.model.Vote;
import com.kelem.kelem.model.QuestionModel.Status;
import com.kelem.kelem.services.AnswerService;
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
public class AnswerController {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    QuestionService questionService;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    AnswerService answerService;
    @Autowired
    VoteRepository voteRepository;

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
     * Returns the page the users use to answer a specifc question.
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/answer-question/{id}")
    public String ans(@PathVariable String id, Model model) {
        QuestionModel q = questionRepository.findById(Long.parseLong(id)).get();
        UserModel currentlyLoggedInUser = loggedInUser();
        model.addAttribute("q", q);
        model.addAttribute("answer", new AnswerModel());
        model.addAttribute("currentlyLoggedInUser", currentlyLoggedInUser);
        return "answer-question";
    }

    /**
     * This method handles the answers given by the users for a question.
     * @param id
     * @param ans
     * @param model
     * @return
     */
    @PostMapping("/answer-question/{id}")
    public String answered(@PathVariable String id, @Valid @ModelAttribute("answer") AnswerModel answer, Errors errors, Model model) {
        // Getting hold of the Principal's username
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        
        // finding the user from the user database based on the principal's name
        UserModel user = userRepository.findByUsername(username);

        // Check for errors
        if (null != errors && errors.getErrorCount() > 0) {
            QuestionModel q = questionRepository.findById(Long.parseLong(id)).get();
            UserModel currentlyLoggedInUser = loggedInUser();
            model.addAttribute("q", q);
            model.addAttribute("answer", answer);
            model.addAttribute("currentlyLoggedInUser", currentlyLoggedInUser);
            return "answer-question";
        }
        QuestionModel q = questionRepository.findById(Long.parseLong(id)).get();
        answer.setQuestion(q);
        answer.setUser(user);

        // Creating an instance of vote for the specific user
        Vote vote = new Vote();
        vote.setUpVote(1);
        vote.setDownVote(0);
        vote.setUserModel(user);

        // returns the saved answer with an id
        answer = answerRepository.save(answer);

        vote.setAnswerModel(answer);
        voteRepository.save(vote);
        q.setStatus(Status.ANSWERED.name());
        questionRepository.save(q);
        
        return "redirect:" + "/"+ q.getId() + "/all-answers";
    }

    // /**
    //  * The method shows the answer and the question the user just answered. Only for developement purposes.
    //  * @param id
    //  * @param model
    //  * @return
    //  */
    // @GetMapping("/just-answered/{id}")
    // public String justAnswered(@PathVariable String id, Model model) {
    //     QuestionModel q = questionRepository.findById(Long.parseLong(id)).get();
    //     AnswerModel ans = answerRepository.findByQuestion(q).get(0);
    //     model.addAttribute("q", q);
    //     model.addAttribute("ans", ans);
    //     return "answered-just-now";
    // }

    /**
     * returns all the answeres for a question.
     */
    @GetMapping("/{id}/all-answers")
    public String allAnswer(Model model, @PathVariable Long id) {
        
        QuestionModel questionModel = questionRepository.findById(id).get();
        
        List<AnswerModel> answer = answerRepository.findByQuestion(questionModel);
        HashMap<AnswerModel, Long> upVote = new HashMap<AnswerModel, Long>();
        HashMap<AnswerModel, Long> downVote = new HashMap<AnswerModel, Long>();

        // saves the up vote and down vote of the specific answer.
        for (AnswerModel am: answer) {
            upVote.put(am, voteRepository.findUpVote(am.getId()));
            downVote.put(am, voteRepository.findDownVote(am.getId()));
        }

        // if (answer.size() > 0){
            UserModel currentlyLoggedInUser = this.loggedInUser();
            model.addAttribute("currentlyLoggedInUser", currentlyLoggedInUser);
            model.addAttribute("question", questionModel);
            model.addAttribute("answer", answer);
            model.addAttribute("upVote", upVote);
            model.addAttribute("downVote", downVote);

            return "all-answers";
        // }
        // System.out.print("IT IS NOT WORKING");
        // return "error";
    }
    @GetMapping("/delete-answer/{id}")
    public String deleteAnswer(@PathVariable Long id) {
        AnswerModel answerModel = answerRepository.findById(id).get();
        QuestionModel questionModel = answerModel.getQuestion();
        answerService.delete(id);
        return String.format("redirect:/%s/all-answers", questionModel.getId());
    }

    /**
     * The get method for the form with which user is able to update their prior answers.
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/update-answer/{id}")
    public String updateAnswer(@PathVariable String id, Model model) {

        UserModel currentlyLoggedInUser = loggedInUser();
        AnswerModel answer = answerRepository.findById(Long.parseLong(id)).get();
        QuestionModel question = answer.getQuestion();
        if (currentlyLoggedInUser.getId() == answer.getUser().getId()) {
            model.addAttribute("answer", answer);
            model.addAttribute("currentlyLoggedInUser", currentlyLoggedInUser);
            model.addAttribute("question", question);
            return "update-answer";
        }
            return "error";
    }

    /**
     * This is where the user updates a question they have previously raised.
     * @param id
     * @param answer
     * @return
     */
    @PostMapping("/update-answer/{id}")
    public String updatedAnswer(@PathVariable Long id, @Valid @ModelAttribute("answer") AnswerModel answer, Errors errors, Model model) {

        UserModel currentlyLoggedInUser = this.loggedInUser();
        // checks if there are errors in validation
        if (null != errors && errors.getErrorCount() > 0) {
            AnswerModel answer1 = answerRepository.findById(id).get();
            QuestionModel question = answer1.getQuestion();
            model.addAttribute("answer", answer);
            model.addAttribute("currentlyLoggedInUser", currentlyLoggedInUser);
            model.addAttribute("question", question);
            return "update-answer";
        }
        AnswerModel answerModel = answerRepository.findById(id).get();
        QuestionModel questionModel = answerModel.getQuestion();
        if (currentlyLoggedInUser.getId() == (answerModel.getUser().getId())) {
            answerModel.setContent(answer.getContent());
            answerService.save(answerModel);
            return String.format("redirect:/%x/all-answers", questionModel.getId());
        }
            return "error";
        
    }
}